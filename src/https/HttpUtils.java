package https;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.net.InetSocketAddress;
import java.net.Socket;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class HttpUtils {
	
	private InetSocketAddress inetAddress;
	
	private Socket socket;
	
	private int default_port=80;
	
	
	
	
	public HttpUtils(String url) throws Exception {
		 Pattern pattern = Pattern  
	                .compile("(http://|https://|www){0,1}[^\u4e00-\u9fa5\\s]*?\\.(com|net|cn|me|tw|fr)[^\u4e00-\u9fa5\\s]*");
		 try {
			 Matcher matcher=pattern.matcher(url);
			 if(matcher.find()){
				  url=getIP(url);
				  try {
					  default_port=Integer.valueOf(url.split(":")[1]);
				  } catch (Exception e) {
				  }
				 inetAddress=new InetSocketAddress(url.split(":")[0],default_port);
				 init();
			 }else{
				 throw new RuntimeException("地址不合法");
			 }
		 } catch (Exception e) {
			  throw e;
		 }
		
		
		
	}
	public void get(String url) throws IOException{
		requestGetBefore(url);
		requertGetAfter();
	}
	
	private void init() throws IOException{
			socket=new Socket(inetAddress.getHostName(),inetAddress.getPort());
			socket.setSoTimeout(1000);
			System.out.println(socket);
	}
	private void requestGetBefore(String url) throws IOException{
		OutputStream outputStream=socket.getOutputStream();
		StringBuffer sb = new StringBuffer("GET "+url+" HTTP/1.1\r\n");  
        sb.append("Host: "+inetAddress.getHostName()+"\r\n");  
        sb.append("Accept: text/html,application/xhtml+xml,application/xml;q=0.9,image/webp,image/apng,*/*;q=0.8\r\n"); 
        sb.append("\r\n");  
        outputStream.write(sb.toString().getBytes());
        outputStream.flush();
        System.out.println(sb.toString());
	}
	private void requertGetAfter() throws IOException{
		BufferedReader bufferedReader=new BufferedReader(new InputStreamReader(socket.getInputStream(),"utf-8"));
		String result="";
		try {
			while((result=bufferedReader.readLine())!=null){//读取头部
				System.out.println(result);
			}
		} catch (Exception e) {
		}
		
	}
	
	private void requestPostBefore(){
		
	}
	private  String getIP(String url) {
        //使用正则表达式过滤，
        String re = "((http|ftp|https)://)(([a-zA-Z0-9._-]+)|([0-9]{1,3}.[0-9]{1,3}.[0-9]{1,3}.[0-9]{1,3}))(([a-zA-Z]{2,6})|(:[0-9]{1,4})?)";
        String str = "";
        // 编译正则表达式
        Pattern pattern = Pattern.compile(re);
        // 忽略大小写的写法
        // Pattern pat = Pattern.compile(regEx, Pattern.CASE_INSENSITIVE);
        Matcher matcher = pattern.matcher(url);
        //若url==http://127.0.0.1:9040或www.baidu.com的，正则表达式表示匹配
        if (matcher.matches()) {
            str = url;
        } else {
            String[] split2 = url.split(re);
            if (split2.length > 1) {
                String substring = url.substring(0, url.length() - split2[1].length());
                str = substring;
            } else {
                str = split2[0];
            }
        }
        return str;
    }
	public static void main(String[] args) throws Exception{
		//http://blog.csdn.net/study_zhxu/article/details/55212006
		new HttpUtils("blog.csdn.net").get("/study_zhxu");
	}
	
	
	
	

}
