package https;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.InetSocketAddress;
import java.net.Socket;
import java.util.HashMap;
import java.util.Map;
import java.util.Map.Entry;
import java.util.zip.GZIPInputStream;

public class HttpUtils {
	private Socket socket;
	private InetSocketAddress inetSocketAddress;
	private int connectTimeout = 2000;  //链接超时时间
    private int readTimeout = 5000;     //读超时时间
    private String host;
    private int port = 80;
    public static String charset = "utf-8";
    private String resourcePath = "/";//资源路径
    
    private Map<String, String> headers = new HashMap<String, String>();
	
    private Map<String, String> respHeaders=new HashMap<String,String>();
    
    private int code;
    
    private String respMsg;
    
	
	
	public HttpUtils(String url) throws Exception {
		initRequestHeader();
		parseUrL(url);
	}
	/**
	 * 初始化头部
	 */
	private  void initRequestHeader(){
		headers.put("Connection", "keep-alive");
        headers.put("Accept", "text/html,application/xhtml+xml,application/xml;q=0.9,image/webp,*/*;q=0.8");
        headers.put("Accept-Encoding", "GZIP,deflate,sdch");
        headers.put("Accept-Language", "zh-CN,zh");
        headers.put("Content-Type", "text/html;"+charset+"");
        //headers.put("Upgrade-Insecure-Requests", "1");
        //headers.put("User-Agent", "Mozilla/5.0 (Windows NT 10.0; Win64; x64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/61.0.3163.100 Safari/537.36");
	}
	/**
	 * 解析url 构建socket 连接
	 * 
	 */
	private void parseUrL(String url){
		try {
			if (url == null || url.length() == 0) 
	            throw new NullPointerException("uri can not be null");
	        if (!url.startsWith("http"))
	            url = "http://" + url;
	        String[] parts = url.split("//");
	         
	        String mainPart = parts[1];
	        int ipFlag = mainPart.indexOf("/");
	        if (ipFlag != -1) {
	            String ipPort = mainPart.substring(0, ipFlag);
	            String[] ipParts = ipPort.split(":");
	            if (ipParts.length > 1) {
	                host = ipParts[0];
	                String portStr = ipParts[1];
	                if (portStr != null && portStr.length() > 0)
	                    port = Integer.parseInt(portStr);
	            } else {
	                host = ipPort;
	            }
	             
	            resourcePath = mainPart.substring(ipFlag);
	        } else {
	            host = mainPart;
	        }
	         
	        String hostVal = host;
	        if (port != 80) hostVal += ":" + port;
	        headers.put("Host", hostVal);
		} catch (Exception e) {
		}
        
	}
	/**
	 * 向服务器发送http 请求头，协议
	 * @param outputStream
	 */
	private void write(OutputStream outputStream,String request,String json)
	    throws Exception{
		StringBuilder sb=new StringBuilder(request+"\r\n");
		for(Entry<String, String> entry:headers.entrySet()){
			sb.append(entry.getKey()+": "+entry.getValue()+"\r\n");
		}
		sb.append("\r\n");
		if(json!=null&&request.contains("POST")){
			sb.append(json);
		}
		System.out.println(sb.toString());
		outputStream.write(sb.toString().getBytes(charset));
		outputStream.flush();
	}
	/**
	 * 读取头部
	 */
	public void readRespHeaders(InputStream in) throws Exception{
		String sss=HttpStreamReader.readHeaders(in);
		String headers_str[]=sss.split("\r\n");
		String resphead[]=headers_str[0].split(" ");
		code=Integer.valueOf(resphead[1]);
		respMsg=resphead[2];
		for(int i=1;i<headers_str.length;i++){
			String s=headers_str[i];
			String ss[]=s.split(":");
			System.out.println(s);
			respHeaders.put(ss[0], ss[1].trim());
			if(s.toLowerCase().contains("gb2312")){
				charset="gb2312";
			}
			if(s.toLowerCase().contains("iso-8859-1")){
				charset="iso-8859-1";
			}
		}
	}
	/**
     * 分块读取
     * 服务器没法提前知道资源的大小，或者不愿意花费资源提前计算资源大小
     * ，就会把http回复报文中加一个header叫Transfer-Encoding:chunked，
     * 就是分块传输的意思。每一块都使用固定的格式，前边是块的大小，后面是数据，
     * 然后最后一块大小是0。这样客户端解析的时候就需要注意去掉一些无用的字段。
     * @param in
     * @return
     * @throws IOException
     */
	public  String readChunked(InputStream in) throws Exception {
        StringBuilder content = new StringBuilder("");
        String lenStr = "0";
        int ind=0;
        //单字节读取
        while (!(lenStr = new String(HttpStreamReader.readLine(in))).equals("0")) {
        	int len = Integer.valueOf(lenStr.toUpperCase(),16);//长度16进制表示
        	byte b[]=new byte[len];
        	//一块的长度
        	int l=0;
        	int bt=0;
        	while((bt=in.read())!=-1){
        		if(l==len)break;
        		b[ind++]=(byte)bt;
        		l++;
        	};
        	if("gzip".equals(respHeaders.get("Content-Encoding"))){
        		GZIPInputStream gzin = new GZIPInputStream(new ByteArrayInputStream(b));
                ByteArrayOutputStream baos = new ByteArrayOutputStream();
                byte[] buf = new byte[1024];
                int count = 0;
                while ((count = gzin.read(buf)) != -1) {
                    baos.write(buf, 0, count);
                }
                baos.close();
                gzin.close();
                content.append(new String(baos.toByteArray(),charset));
        	 }else{
        		 content.append(new String(b,charset));
        	 }
             
             in.skip(2);
        }
        return content.toString();
    }
    /**
     * 读取主体数据，通过Connection：close 关闭连接
     * @param in
     * @return
     * @throws IOException
     */
    public String readInput(InputStream in) throws IOException{
    	StringBuilder content = new StringBuilder("");
        byte b[]=new byte[1024];
        int l=-1;
        try {
        	 while ((l=in.read(b))!=-1) {
             	content.append(new String(b,0,l,charset));
             }
		} catch (Exception e) {
		}
        return content.toString();
    }
    
    public String sendGet(String ... parames) throws Exception{
    	return sendByGet(parames);
    }
    /**
     * 无参数
     * @return
     * @throws Exception
     */
    public String sendGet() throws Exception{
    	return sendByGet();
    }
    
	private  String sendByGet(String ... parames) throws Exception{
		OutputStream outputStream=null;
		InputStream inputStream=null;
		try {
			//new 套接地址
			inetSocketAddress=new InetSocketAddress(host, port);
			socket=new Socket();
		     //将套接地址绑定在套接字中，并设置连接，读取时间
			socket.connect(inetSocketAddress, connectTimeout);
			socket.setSoTimeout(readTimeout);
			outputStream=socket.getOutputStream();
			appendUrl(parames);
			write(outputStream,"GET "+resourcePath+" HTTP/1.1",null);
			inputStream=socket.getInputStream();
			readRespHeaders(inputStream);
			String body = null;
	        if (respHeaders.containsKey("Transfer-Encoding")) {
	            body = readChunked(inputStream);
	        }else{
	        	if("gzip".equals(respHeaders.get("Content-Encoding"))){
		        	try {
		        		inputStream= new GZIPInputStream(inputStream);
					} catch (Exception e) {
						
					}
	        	}
	        	body=readInput(inputStream);
	        }
	        return body;
		} catch (Exception e) {
			e.printStackTrace();
			return null;
		}finally{
			if(outputStream!=null){
				outputStream.close();
			}
			if(inputStream!=null){
				inputStream.close();
			}
			if(socket!=null)
				socket.close();
		}
	}
	/**
	 * 发送无参请求
	 * @return
	 */
    public String sendPost(String json,String ...parames) throws Exception{
    	return sendByPost(json,parames);
    }
	
	/**
	 * 发送无参请求
	 * @return
	 */
    public String sendPost() throws Exception{
    	return sendByPost(null);
    }
	/**
	 * post 请求
	 * @return
	 * @throws Exception
	 */
	private String sendByPost(String json,String ...parames)throws Exception{
		OutputStream outputStream=null;
		InputStream inputStream=null;
		try {
			//new 套接地址
			inetSocketAddress=new InetSocketAddress(host, port);
			socket=new Socket();
		     //将套接地址绑定在套接字中，并设置连接，读取时间
			socket.connect(inetSocketAddress, connectTimeout);
			socket.setSoTimeout(readTimeout);
			outputStream=socket.getOutputStream();
			appendUrl(parames);
			write(outputStream,"POST "+resourcePath+" HTTP/1.1",json);
			inputStream=socket.getInputStream();
			readRespHeaders(inputStream);
			String body = null;
	        if (respHeaders.containsKey("Transfer-Encoding")) {
	            body = readChunked(inputStream);
	        }else{
	        	if("gzip".equals(respHeaders.get("Content-Encoding"))){
		        	try {
		        		inputStream= new GZIPInputStream(inputStream);
					} catch (Exception e) {
						
					}
	        	}
	        	body=readInput(inputStream);
	        }
	        return body;
		} catch (Exception e) {
			e.printStackTrace();
			return null;
		}finally{
			if(outputStream!=null){
				outputStream.close();
			}
			if(inputStream!=null){
				inputStream.close();
			}
			if(socket!=null)
				socket.close();
		}
	}
	
	private void appendUrl(String ...parames)throws Exception{
		if(parames!=null&&parames.length>0){
			if(!resourcePath.contains("?")){
				resourcePath+="?1=1";
				
			}
			if((parames.length&1)!=0){
				throw new Exception("请输入name和value");
			}
			for(int i=0;i<parames.length;i+=2){
				resourcePath+="&"+parames[i]+"="+parames[i+1];
			}
		}
	}
	public int getConnectTimeout() {
		return connectTimeout;
	}
	public void setConnectTimeout(int connectTimeout) {
		this.connectTimeout = connectTimeout;
	}
	
	public int getReadTimeout() {
		return readTimeout;
	}
	public void setReadTimeout(int readTimeout) {
		this.readTimeout = readTimeout;
	}
	public Map<String, String> getHeaders() {
		return headers;
	}
	public void setHeaders(Map<String, String> headers) {
		this.headers.putAll(headers);
	}
	public void setHeaders(String key,String value) {
		this.headers.put(key, value);
	}

	public String getRespHeaders(String key) {
		return respHeaders.get(key);
	}
	
	public Map<String, String> getRespHeaders() {
		return respHeaders;
	}
	
	public int getCode() {
		return code;
	}
	public String getCharset() {
		return charset;
	}
	public static void setCharset(String charset) {
		HttpUtils.charset = charset;
	}
	
	public String getRespMsg() {
		return respMsg;
	}
	public static void main(String[] args) throws Exception{
		String  text="";
		try {
			HttpUtils httpUtils=new HttpUtils("www.baidu.com");
			httpUtils.setHeaders("Content-Type","application/x-www-form-urlencoded;charset=UTF-8");
			text=httpUtils.sendPost("name=lsf");
			System.out.println(text);
			System.out.println(httpUtils.getCode());
		} catch (Exception e) {
			e.printStackTrace();
			System.out.println(text);
			// TODO: handle exception http://www.sina.com.cn/
		}
		
	}
	
	
	
	

}
