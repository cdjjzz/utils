package https;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.net.InetSocketAddress;
import java.net.Socket;
import java.net.URL;
import java.util.HashMap;
import java.util.Map;
import java.util.Map.Entry;
import java.util.zip.GZIPInputStream;

public class HttpUtils {
	private Socket socket;
	private InetSocketAddress inetSocketAddress;
	private int connectTimeout = 2000;  //���ӳ�ʱʱ��
    private int readTimeout = 5000;     //����ʱʱ��
    private String host;
    private int port = 80;
    private String charset = "UTF-8";
    private String resourcePath = "/";//��Դ·��
    
    private Map<String, String> headers = new HashMap<String, String>();
	
    private Map<String, String> respHeaders=new HashMap<String,String>();
    
    private int code;
    
    private String respMsg;
    
	
	
	public HttpUtils(String url) throws Exception {
		initRequestHeader();
		parseUrL(url);
	}
	/**
	 * ��ʼ��ͷ��
	 */
	private  void initRequestHeader(){
		headers.put("Connection", "keep-alive");
        headers.put("Accept", "text/html,application/xhtml+xml,application/xml;q=0.9,image/webp,*/*;q=0.8");
        //headers.put("Accept-Encoding", "gzip,deflate");
        headers.put("Accept-Language", "zh-CN,zh");
        headers.put("Content-Type", "text/html;charset=utf-8");
        //headers.put("Upgrade-Insecure-Requests", "1");
        //headers.put("User-Agent", "Mozilla/5.0 (Windows NT 10.0; Win64; x64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/61.0.3163.100 Safari/537.36");
	}
	/**
	 * ����url ����socket ����
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
	 * �����������http ����ͷ��Э��
	 * @param outputStream
	 */
	private void write(OutputStream outputStream,String request,String json,String ...parames)
	    throws Exception{
		StringBuilder sb=new StringBuilder(request+"\r\n");
		for(Entry<String, String> entry:headers.entrySet()){
			sb.append(entry.getKey()+": "+entry.getValue()+"\r\n");
		}
		if(json!=null){
			sb.append(json);
		}
		if(parames!=null&&parames.length>0){
			if((parames.length&1)!=0){
				throw new Exception("������name��value");
			}
			for(int i=0;i<parames.length;i+=2){
				sb.append(parames[i]+"="+parames[i+1]);
			}
		}
		sb.append("\r\n");
		System.out.println(sb.toString());
		outputStream.write(sb.toString().getBytes(charset));
	}
	/**
	 * ��ȡͷ��
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
        byte b[]=new byte[1024];
        int ind=0;
        //���ֽڶ�ȡ
        while (!(lenStr = new String(HttpStreamReader.readLine(in))).equals("0")) {
        	int len = Integer.valueOf(lenStr.toUpperCase(),16);//����16���Ʊ�ʾ
        	//һ��ĳ���
        	int l=0;
        	int bt=0;
        	while((bt=in.read())!=-1){
        		if(l==len)break;
        		if(ind==1024){
        			content.append(new String(b,"utf-8"));
       			 	ind=0;
       		 	}
        		b[ind++]=(byte)bt;
        		l++;
        	};
            in.skip(2);
        }
        content.append(new String(b,0,ind,"utf-8"));
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
	public String sendGet() throws Exception{
		OutputStream outputStream=null;
		InputStream inputStream=null;
		try {
			//new �׼���ַ
			inetSocketAddress=new InetSocketAddress(host, port);
			socket=new Socket();
		     //���׽ӵ�ַ�����׽����У����������ӣ���ȡʱ��
			socket.connect(inetSocketAddress, connectTimeout);
			socket.setSoTimeout(readTimeout);
			outputStream=socket.getOutputStream();
			write(outputStream,"GET "+resourcePath+" HTTP/1.1",null);
			inputStream=socket.getInputStream();
			readRespHeaders(inputStream);
			String body = null;
			String content=respHeaders.get("Content-Encoding");
	        if (respHeaders.containsKey("Transfer-Encoding")) {
	            body = readChunked(inputStream);
	        } else if(content==null||"".equals(content)){//δѹ������ͨ��Content-Length��ȡ����
	            int bodyLen = Integer.valueOf(respHeaders.get("Content-Length"));
	            byte[] bodyBts = new byte[bodyLen];
	            inputStream.read(bodyBts);
	            body = new String(bodyBts, charset);
	        }else{
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
	public void setCharset(String charset) {
		this.charset = charset;
	}
	
	public String getRespMsg() {
		return respMsg;
	}
	public static void main(String[] args) throws Exception{
		String  text="";
		try {
			HttpUtils httpUtils=new HttpUtils("www.qq.com");
			text=httpUtils.sendGet();
			//System.out.println(text);
			System.out.println(httpUtils.getRespHeaders("Transfer-Encoding"));
		} catch (Exception e) {
			e.printStackTrace();
			System.out.println(text);
			// TODO: handle exception
		}
		
	}
	
	
	
	

}
