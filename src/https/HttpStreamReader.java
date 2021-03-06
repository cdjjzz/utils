package https;
 
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.Arrays;
/**
 * 
 * @author wensh.zhu 2017-03-11
 * 
 */
public class HttpStreamReader {
    /**
     * 行结束标识，\r\n(13,10)
     */
    public static final byte[] LINE_END = {13, 10};
    /**
     * 响应头结束标识，\r\n\r\n(13,10,13,10)
     */
    public static final byte[] ALL_END = {13, 10, 13, 10};
    
    /**
     * 读取1024字节
     * @param in
     * @return
     * @throws IOException
     */
    public static byte[] getBytes(InputStream in) throws IOException {
        byte[] buffer = new byte[1024];
        int len = 0;
        int ind = 0;
        while ((len = in.read(buffer, ind, buffer.length - ind)) > 0){
            if (len == buffer.length - ind) {//full 
                int l = buffer.length;
                byte[] newBuffer = new byte[l * 2];
                System.arraycopy(buffer, 0, newBuffer, 0, buffer.length);
                buffer = newBuffer;
                ind = l;
            } else {
                ind += len;
            }
        }
        byte[] result = new byte[ind];
        System.arraycopy(buffer, 0, result, 0, ind);
        return result;
    }
    /**
     * 读取头部
     * @param in
     * @return
     * @throws IOException
     */
    public static byte[] readHeaders(InputStream in) throws IOException {
        return read(in, ALL_END);
    }
     /**
      * 读取一行
      * @param in
      * @return
      * @throws IOException
      */
    public static byte[] readLine(InputStream in) throws IOException {
        return read(in,LINE_END);
    }
     
    public static byte[] read(InputStream in, byte[] endFlag) throws IOException {
         int ind = 0;
         int bt = 0;
         ByteArrayOutputStream arrayOutputStream=new ByteArrayOutputStream();
         byte buffer[]=new byte[1024];
    	 while ((bt = in.read()) != -1){ 
    		 if(ind==1024){
    			 arrayOutputStream.write(buffer);
    			 ind=0;
    		 }
    		 buffer[ind]=(byte)bt;
			 if (isTailEqual(buffer, ind, endFlag))
                 break;
             ind ++;
            
         }
    	 int newLen = ind + 1 - endFlag.length;
    	 if(newLen>0)
    	 arrayOutputStream.write(buffer,0,newLen);
         return arrayOutputStream.toByteArray();
    }
    /**
     * bts的后ends.length个字节是否和ends相等
     * @param bts
     * @param ends
     * @return
     */
    public static boolean isTailEqual(byte[] bts, int endIndex, byte[] ends){
        int btsLen = endIndex + 1;
        int endLen = ends.length;
         
        if (btsLen < endLen) return false;
         
        int tailFrom = btsLen - endLen;
        int tailTo = btsLen;
         
        byte[] tail = Arrays.copyOfRange(bts, tailFrom, tailTo);
         
        for (int i = 0; i < endLen; i++) 
            if (tail[i] != ends[i]) 
                return false;
         
        return true;
    }
}