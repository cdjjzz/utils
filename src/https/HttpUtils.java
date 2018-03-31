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

import javax.net.ssl.SSLSocket;
import javax.net.ssl.SSLSocketFactory;

/**
 * 
 * @author pet-lsf
 *
 */
public class HttpUtils {
	private Socket socket;
	private InetSocketAddress inetSocketAddress;
	private int connectTimeout = 2000;  //���ӳ�ʱʱ��
    private int readTimeout = 50000;     //����ʱʱ��
    private String host;
    private int port = 80;
    public static String charset = "utf-8";
    private String resourcePath = "/";//��Դ·��
    
    private boolean tls=false;//ʹ��https
    
    
    private Map<String, String> headers = new HashMap<String, String>();//����ͷ
	
    private Map<String, String> respHeaders=new HashMap<String,String>();//����ͷ
    
    private int code;//������
    
    private String respMsg;//������Ϣ
    
    private boolean useProxy=false;
	
	
	public HttpUtils(String url) throws Exception {
		initRequestHeader();
		parseUrL(url);
	}
	public HttpUtils(String url,Boolean useProxy) throws Exception {
		this.useProxy=useProxy;
		initRequestHeader();
		parseUrL(url);
	}
	/**
	 * ��ʼ��ͷ��
	 */
	private  void initRequestHeader(){
		headers.put("Connection", "keep-alvie");
		headers.put("Accept", "*");
        headers.put("Accept-Encoding", "GZIP,deflate,sdch");
        headers.put("Accept-Language", "zh-CN,zh");
        headers.put("Content-Type", "text/html;"+charset+"");
        headers.put("Upgrade-Insecure-Requests", "1");
        headers.put("User-Agent", "Mozilla/5.0 (Windows NT 10.0; Win64; x64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/61.0.3163.100 Safari/537.36");
	}
	/**
	 * ����url ����socket ����
	 * 
	 */
	private void parseUrL(String url){
		try {
			if (url == null || url.length() == 0) 
	            throw new NullPointerException("uri can not be null");
			if(url.startsWith("https")){
				tls=true;
				port=443;
			}else{
					tls=false;
					port=80;
				 if (!url.startsWith("http"))
			            url = "http://" + url;
			}
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
	             
	            resourcePath =mainPart.substring(ipFlag);
	        } else {
	            host = mainPart;
	        }
	         
	        String hostVal = host;
	        if(tls){
	        	if (port != 443) hostVal += ":" + port;
	        }else{
	        	if (port != 80) hostVal += ":" + port;
	        }
	        headers.put("Host", hostVal);
		} catch (Exception e) {
		}
        
	}
	/**
	 * �����������http ����ͷ��Э��
	 * @param outputStream
	 */
	private void write(OutputStream outputStream,String request,String json)
	    throws Exception{
		StringBuilder sb=new StringBuilder(request+"\r\n");
		for(Entry<String, String> entry:headers.entrySet()){
			sb.append(entry.getKey()+": "+entry.getValue()+"\r\n");
		}
		/**
		 * post �������
		 */
		if(json!=null&&request.contains("POST")){
			sb.append("Content-Length:"+json.length()+"\r\n");
			sb.append("\r\n");
			outputStream.write(sb.toString().getBytes(charset));
			outputStream.flush();
			outputStream.write(json.getBytes());
			outputStream.flush();
		}else{
			sb.append("\r\n");
			outputStream.write(sb.toString().getBytes(charset));
			outputStream.flush();
		}
		System.out.println(sb.toString());
	}
	/**
	 * ��ȡͷ��
	 */
	public void readRespHeaders(InputStream in) throws Exception{
		String sss=HttpStreamReader.readHeaders(in);
		System.out.println(sss);
		String headers_str[]=sss.split("\r\n");
		String resphead[]=headers_str[0].split(" ");
		code=Integer.valueOf(resphead[1]);
		if(resphead.length>2)
		respMsg=resphead[2];
		respHeaders.clear();
		for(int i=1;i<headers_str.length;i++){
			String s=headers_str[i];
			String ss[]=s.split(": ");
			respHeaders.put(ss[0], ss[1].trim());
			//���ݷ��ر�������ȫ�ֱ����ʽ
			if(s.toLowerCase().contains("gb2312")){
				charset="gb2312";
			}
			if(s.toLowerCase().contains("iso-8859-1")){
				charset="iso-8859-1";
			}
		}
	}
	/**
     * �ֿ��ȡ
     * ������û����ǰ֪����Դ�Ĵ�С�����߲�Ը�⻨����Դ��ǰ������Դ��С
     * ���ͻ��http�ظ������м�һ��header��Transfer-Encoding:chunked��
     * ���Ƿֿ鴫�����˼��ÿһ�鶼ʹ�ù̶��ĸ�ʽ��ǰ���ǿ�Ĵ�С�����������ݣ�
     * Ȼ�����һ���С��0�������ͻ��˽�����ʱ�����Ҫע��ȥ��һЩ���õ��ֶΡ�
     * @param in
     * @return
     * @throws IOException
     */
	public  String readChunked(InputStream in) throws Exception {
        StringBuilder content = new StringBuilder("");
        String lenStr = "0";
        //���ֽڶ�ȡ
        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        while (!(lenStr = new String(HttpStreamReader.readLine(in))).equals("0")) {
        	int len = Integer.valueOf(lenStr.toUpperCase(),16);//����16���Ʊ�ʾ
        	byte b[]=new byte[len];
        	//һ��ĳ���
        	int l=0;
        	int bt=0;
        	int ind=0;
        	while((bt=in.read())!=-1){
        		if(l==len)break;
        		b[ind++]=(byte)bt;
        		l++;
        	};
        	while(in.read()!=10);
        	baos.write(b,0,len);
        }
        /**
         * ��ѹ
         */
		if("gzip".equals(respHeaders.get("Content-Encoding"))){
		    GZIPInputStream gzin=new GZIPInputStream(new ByteArrayInputStream(baos.toByteArray()));
		    baos.reset();
		    byte b[]=new byte[1024];
		    int count=-1;
		    while((count=gzin.read(b))!=-1){
		    	baos.write(b,0,count);
		    }
		    baos.close();
		    gzin.close();
		}
		content.append(baos.toString(charset));
        return content.toString();
    }
    /**
     * ��ȡ�������ݣ�ͨ��Connection��close �ر�����
     * @param in
     * @return
     * @throws IOException
     */
    public String readInput(InputStream in) throws IOException{
    	StringBuilder content = new StringBuilder("");
        try {
        	 if(respHeaders.containsKey("Content-Length")&&!respHeaders.containsKey("Content-Encoding")){
        		 byte b[]=new byte[Integer.valueOf(respHeaders.get("Content-Length"))];
        	     in.read(b);
        	   //  System.out.println(b.length);
        	     content.append(new String(b,charset));
        	 }else{
        		 byte b[]=new byte[1024];
        	     int l=-1;
	        	 while ((l=in.read(b))!=-1) {
	             	content.append(new String(b,0,l,charset));
	             }
        	 }
		} catch (Exception e) {
		}
        return content.toString();
    }
    
    
    public String sendGet(String ... parames) throws Exception{
    	return sendByGet(parames);
    }
    /**
     * �޲���
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
			//new �׽ӵ�ַ
			String req="GET "+resourcePath+" HTTP/1.1";
			if(useProxy){
				inetSocketAddress=new InetSocketAddress(ProxyHttp.proxyHost,Integer.valueOf(ProxyHttp.proxyPort));
				req="CONNECT "+resourcePath+" HTTP/1.1";
			}else{
				inetSocketAddress=new InetSocketAddress(host, port);
			}
			
			if(tls){
				socket=(SSLSocket)((SSLSocketFactory)SSLSocketFactory.getDefault()).createSocket();  
			}else{
				socket=new Socket();
			}
		     //���׽ӵ�ַ�����׽����У����������ӣ���ȡʱ��
			socket.connect(inetSocketAddress, connectTimeout);
			socket.setSoTimeout(readTimeout);
			outputStream=socket.getOutputStream();
			appendUrl(parames);
			write(outputStream,req,null);
			inputStream=socket.getInputStream();
			readRespHeaders(inputStream);
			String body = null;
			if(String.valueOf(code).startsWith("20")){
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
			}
	        return body;
		} catch (Exception e) {
			e.printStackTrace();
			return "get";
		}finally{
			if(outputStream!=null){
				outputStream.close();
			}
			if(inputStream!=null){
				inputStream.close();
			}
			if(socket!=null)
				socket.close();
			if(respHeaders.containsKey("Location")){
				initRequestHeader();
				parseUrL(respHeaders.get("Location"));
				return sendByGet();
			}
			
		}
	}
	/**
	 * 
	 * �ڷ���post ���� ����Ϊ����
	 *  ����ͷ���и�����������
	 *  post  \r\n 
	 *  heares  \r\n
	 *  \r\n
	 *  parames
	 * 
	 * 
	 * �����޲�����
	 * @return
	 */
    public String sendPost(String json,String ...parames) throws Exception{
    	return sendByPost(json,parames);
    }
	
	/**
	 * �����޲�����
	 * @return
	 */
    public String sendPost() throws Exception{
    	return sendByPost(null);
    }
	/**
	 * post ����
	 * @return
	 * @throws Exception
	 */
	private String sendByPost(String json,String ...parames)throws Exception{
		OutputStream outputStream=null;
		InputStream inputStream=null;
		try {
			//new �׽ӵ�ַ
			String req="POST "+resourcePath+" HTTP/1.1";
			if(useProxy){
				inetSocketAddress=new InetSocketAddress(ProxyHttp.proxyHost,Integer.valueOf(ProxyHttp.proxyPort));
				req="CONNECT "+resourcePath+" HTTP/1.1";
			}else{
				inetSocketAddress=new InetSocketAddress(host, port);
			}
			
			if(tls){
				socket=(SSLSocket)((SSLSocketFactory)SSLSocketFactory.getDefault()).createSocket();  
			}else{
				socket=new Socket();
			}
			if(tls){
				socket=(SSLSocket)((SSLSocketFactory)SSLSocketFactory.getDefault()).createSocket();  
			}else{
				socket=new Socket();
			}
		     //���׽ӵ�ַ�����׽����У����������ӣ���ȡʱ��
			socket.connect(inetSocketAddress, connectTimeout);
			socket.setSoTimeout(readTimeout);
			outputStream=socket.getOutputStream();
			appendUrl(parames);
			write(outputStream,req,json);
			inputStream=socket.getInputStream();
			readRespHeaders(inputStream);
			String body = null;
			if(String.valueOf(code).startsWith("20")){
		        if (respHeaders.containsKey("Transfer-Encoding")) {
		            body = readChunked(inputStream);
		        }else{
		        	//δʹ�÷ֿ飬�ж��Ƿ���Ҫ�Ƚ�ѹ
		        	if("gzip".equals(respHeaders.get("Content-Encoding"))){
			        	try {
			        		inputStream= new GZIPInputStream(inputStream);
						} catch (Exception e) {
							
						}
		        	}
		        	body=readInput(inputStream);
		        }
			}
	        return body;
		} catch (Exception e) {
			e.printStackTrace();
			return "post";
		}finally{
			if(outputStream!=null){
				outputStream.close();
			}
			if(inputStream!=null){
				inputStream.close();
			}
			if(socket!=null)
				socket.close();
			/**
			 * �����Ҫ�ض��򣬶��µ�ַ���µ�����
			 */
			if(respHeaders.containsKey("Location")){
				initRequestHeader();
				parseUrL(respHeaders.get("Location"));
				return sendByGet();
			}
		}
	}
	/**
	 * ·����ƴ�Ӳ���
	 * @param parames
	 * @throws Exception
	 */
	private void appendUrl(String ...parames)throws Exception{
		if(parames!=null&&parames.length>0){
			if(!resourcePath.contains("?")){
				resourcePath+="?1=1";
				
			}
			if((parames.length&1)!=0){
				throw new Exception("������name��value");
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
//			HttpUtils httpUtils=new HttpUtils("http://www.tuling123.com/openapi/api");
//			httpUtils.setHeaders("Content-Type", MimeType.JSON.getValue());
//			//text=httpUtils.sendPost(null,"{'j_username':'admin','j_password':'123456','j_validcode':'1a'}");
//			String jsonStr="{\"key\":\"dbc331197a0d43f1b68671240b2e5b5a\",\"info\":\"hhh\",\"userid\":\"224310\"}";
//			text=httpUtils.sendPost(jsonStr);
//			text=httpUtils.sendGet();
//			HttpUtils httpUtils=new HttpUtils("http://127.0.0.1:8099/rbchinfo/queryRbchByPage?page=1&rows=100");
//			text=httpUtils.sendGet();
			HttpUtils httpUtils=new HttpUtils("https://www.baidu.com",true);
			text=httpUtils.sendByGet();
			System.out.println(text);
		} catch (Exception e) {
			e.printStackTrace();
			System.out.println(text);
			// TODO: handle exception http://www.sina.com.cn/
		}
		
	}
	
	
	
	

}
