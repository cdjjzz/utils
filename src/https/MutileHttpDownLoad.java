package https;

import java.io.File;
import java.io.FileInputStream;
import java.io.InputStream;
import java.io.RandomAccessFile;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.concurrent.Callable;
import java.util.concurrent.CountDownLatch;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class MutileHttpDownLoad {
	private static final int useThreadCount=4;
	
	static ExecutorService executorService=Executors.newFixedThreadPool(useThreadCount);
	
	static CountDownLatch countDownLatch=new CountDownLatch(useThreadCount);
	
	public static void main(String[] args) throws Exception{
		// MutileHttpDownLoad.remoteDown("http://d.hiphotos.baidu.com/image/pic/item/f9198618367adab45913c15e87d4b31c8601e4e8.jpg");
		MutileHttpDownLoad.localCopy("d:/lsf.txt");
	}
	/**
	 * 多线程远程分段下载
	 * @throws Exception
	 */
	public static void remoteDown(String path) throws Exception{
		URL url=new URL(path);
		HttpURLConnection httpURLConnection=(HttpURLConnection)url.openConnection();
		httpURLConnection.setRequestMethod("GET");
		httpURLConnection.setConnectTimeout(500000);
		if(httpURLConnection.getResponseCode()==200){//判断当前地址是否可以连通
			long size = httpURLConnection.getContentLength();
            System.out.println("需要下载的文件大小：" + size);
            long blockSize = size/useThreadCount;
            for (int i = 1; i <= useThreadCount; i++) {
            	long startIndex=(i-1)*blockSize+0;
            	long endIndex=i!=useThreadCount?(i*blockSize-1):size;
            	executorService.submit(new Callable<Boolean>() {
    				@Override
    				public Boolean call() throws Exception {
    					HttpURLConnection connection = (HttpURLConnection) url.openConnection();
    	                connection.setRequestMethod("GET");
    	                System.out.println("开始位置："+startIndex+",结束位置："+endIndex);
    	                connection.setRequestProperty("Range", "bytes=" + startIndex + "-" + endIndex);
    	                connection.setConnectTimeout(5000);
    	                int code = connection.getResponseCode();
    	                System.out.println("code" + code);

    	                InputStream is = connection.getInputStream();
    	                String paths[]=path.split("\\.");
    	                File file = new File("d:/"+System.currentTimeMillis()+"."+paths[paths.length-1]);
    	                RandomAccessFile raf = new RandomAccessFile(file, "rw");
    	                raf.seek(startIndex);
    	                int len = 0;
    	                byte[] buffer = new byte[1024];
    	                while ((len = is.read(buffer)) != -1) {
    	                    raf.write(buffer, 0, len);
    	                }
    	                is.close();
    	                raf.close();
    	                countDownLatch.countDown();
    					return true;
    				}
    			});
			}
            countDownLatch.await();//等待其他线程执行
            System.out.println("文件下载成功");
            executorService.shutdown();
		}
	}
	/**
	 * 本地多线程copy
	 */
	public static void localCopy(String localPath) throws Exception{
		    File file=new File(localPath);
			long size = file.length();
            System.out.println("需要下载的文件大小：" + size);
            long blockSize = size/useThreadCount;
            String paths[]=localPath.split("\\.");
        	File file1 = new File("d:/"+System.currentTimeMillis()+"."+paths[paths.length-1]);
            for (int i = 1; i <= useThreadCount; i++) {
            	long startIndex=(i-1)*blockSize;
            	long endIndex=i!=useThreadCount?(i*blockSize-1):size;
            	long blockSiz=endIndex-startIndex+1;
            	executorService.submit(new Callable<Boolean>() {
    				@Override
    				public Boolean call() throws Exception {
    	                InputStream is =new FileInputStream(file);
    	                is.skip(startIndex);
    	                RandomAccessFile raf = new RandomAccessFile(file1, "rw");
    	                raf.seek(startIndex);
    	                int len = 0;
    	                byte[] buffer = null;
    	                if(blockSiz>1024){
    	                	buffer=new byte[1024];
    	                }else{
    	                	buffer=new byte[(int)blockSiz];
    	                }
    	                long count=0;
    	                while ((len = is.read(buffer)) != -1) {
    	                    raf.write(buffer, 0, len);
    	                    count+=len; 
    	                    if(blockSiz-count<1024)buffer=new byte[(int) (blockSiz-count)];
    	                    if(count==blockSiz)break;
    	                }
    	            	System.out.println("开始位置："+startIndex+",结束位置："+endIndex+",偏移量:"+count);
    	                is.close();
    	                raf.close();
    	                countDownLatch.countDown();
    					return true;
    				}
    			});
			}
            countDownLatch.await();//等待其他线程执行
            System.out.println("文件下载成功");
            executorService.shutdown();
	}
	
}
