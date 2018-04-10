package https;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.ServerSocket;
import java.net.Socket;
import java.nio.charset.Charset;
import java.util.HashMap;
import java.util.Map;
import java.util.Map.Entry;
import java.util.concurrent.Callable;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

import props.Infos;

public class ProxyHttp {
	   public static String proxyHost="127.0.0.1";
	   public static String proxyPort;
	   private ServerSocket serverSocket;
	   ExecutorService executorService=Executors.newFixedThreadPool(5);
	   static{
		   proxyPort=Infos.getInfoByKey("proxy_port");
	   }
	   public ProxyHttp() throws Exception{
		   try {
			   serverSocket=new ServerSocket(Integer.valueOf(proxyPort));
			   while(true){
				   executorService.submit(new ClientHandler(serverSocket.accept()));
			   }
		   } catch (Exception e) {
			   throw new Exception("启动代理服务器错误");
		   }
	   }
class ClientHandler implements Callable<Boolean>{
	private String reqLine;
	private String host;
	private int port;
	private StringBuilder data=new StringBuilder();
	private Map<String,String> clientHeader=new HashMap<String, String>();
	private Socket socket;
	
	public ClientHandler(Socket socket) {
		this.socket = socket;
	}

	@Override
	public Boolean call() throws Exception {
		OutputStream clientOutput = null;
        InputStream clientInput = null;
        Socket proxySocket = null;
        InputStream proxyInput = null;
        OutputStream proxyOutput = null;
        try {
        	clientOutput=socket.getOutputStream();
        	clientInput=socket.getInputStream();
        	parse(clientInput);
        	//连接目标服务器
        	proxySocket=new Socket(host,port);
        	System.out.println(proxySocket);
        	proxyOutput=proxySocket.getOutputStream();
        	StringBuilder sb=new StringBuilder(reqLine+"\r\n");
        	for (Entry<String, String> entry:clientHeader.entrySet()) {
        		  sb.append(entry.getKey()+":"+entry.getValue()+"\r\n");
			}
        	sb.append("\r\n");
        	System.out.println(sb.toString());
        	//根据HTTP method来判断是https还是http请求
            if ("CONNECT".equalsIgnoreCase(reqLine)) {//https先建立隧道
                clientOutput.write("HTTP/1.1 200 Connection Established\r\n\r\n".getBytes());
                clientOutput.flush();
            } else {
            	proxyOutput.write(sb.toString().getBytes());
            	proxyOutput.flush();
            }
        	//得到服务器返回
        	proxyInput=proxySocket.getInputStream();
        	int end=-1;
        	while ((end=proxyInput.read())!=-1) {
        		   clientOutput.write(end);
             }
        } catch (Exception e) {
        	e.printStackTrace();
		}finally{
			System.out.println("end");
			if (proxyInput != null) {
                try {
                    proxyOutput.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
            if (proxyOutput != null) {
                try {
                    proxyOutput.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
            if (proxySocket != null) {
                try {
                    proxySocket.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
            if (clientInput != null) {
                try {
                    clientInput.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
            if (clientOutput != null) {
                try {
                    clientOutput.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
            if (socket != null) {
                try {
                    socket.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
		}
		return true;
	}
	
	/**
	 * 解析客户端头部
	 * @param in
	 * @throws IOException
	 */
	private void parse(InputStream in) throws IOException{
		String sss=new String(HttpStreamReader.readHeaders(in),HttpUtils.charset);
		String headers_str[]=sss.split("\r\n");
		reqLine=headers_str[0];
		for(int i=1;i<headers_str.length;i++){
			String s=headers_str[i];
			if(s.equals(" "))break;
			String ss[]=s.split(":");
			clientHeader.put(ss[0], ss[1].trim());
		}
		if(clientHeader.containsKey("Host")){
			String req[]=clientHeader.get("Host").split(":");
			host=req[0];
			if(req.length>1){
				port=Integer.valueOf(req[1]);
			}else{
				if(clientHeader.get("Host").contains("https")){
					port=433;
				}else{
					port=80;
				}
			}
		}
	 }
  }
	public static void main(String[] args) throws Exception{
		new ProxyHttp();
	}
}
