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
     * �н�����ʶ��\r\n(13,10)
     */
    public static final byte[] LINE_END = {13, 10};
    /**
     * ��Ӧͷ������ʶ��\r\n\r\n(13,10,13,10)
     */
    public static final byte[] ALL_END = {13, 10, 13, 10};
    
    /**
     * ��ȡ1024�ֽ�
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
     * ��ȡͷ��
     * @param in
     * @return
     * @throws IOException
     */
    public static String readHeaders(InputStream in) throws IOException {
        return read(in, ALL_END);
    }
     /**
      * ��ȡһ��
      * @param in
      * @return
      * @throws IOException
      */
    public static String readLine(InputStream in) throws IOException {
        return read(in,LINE_END);
    }
     
    public static String read(InputStream in, byte[] endFlag) throws IOException {
         int ind = 0;
         int bt = 0;
         StringBuilder sb=new StringBuilder("0");
         byte buffer[]=new byte[1024];
    	 while ((bt = in.read()) != -1){ 
    		 if(ind==1024){
    			 sb.append(new String(buffer,"utf-8"));
    			 ind=0;
    		 }
    		 buffer[ind]=(byte)bt;
			 if (isTailEqual(buffer, ind, endFlag))
                 break;
             ind ++;
            
         }
    	 int newLen = ind + 1 - endFlag.length;
    	 sb.append(new String(buffer,0,newLen,"utf-8"));
         return sb.toString();
    }
    public static byte[] chunked(InputStream in) throws Exception {
        ByteArrayOutputStream tmpos = new ByteArrayOutputStream(4);
        ByteArrayOutputStream bytes = new ByteArrayOutputStream();
        int data = -1;
        int[] aaa = new int[2];
        byte[] aa = null;

        while ((data = in.read()) >= 0) {
            aaa[0] = aaa[1];
            aaa[1] = data;
            if (aaa[0] == 13 && aaa[1] == 10) {
                aa = tmpos.toByteArray();
                int num = 0;
                try {
                    num = Integer.parseInt(new String(aa, 0, aa.length - 1)
                            .trim(), 16);
                } catch (Exception e) {
                    System.out.println("aa.length:" + aa.length);
                    e.printStackTrace();
                }

                if (num == 0) {

                    in.read();
                    in.read();
                    return bytes.toByteArray();
                }
                aa = new byte[num];
                int sj = 0, ydlen = num, ksind = 0;
                while ((sj = (in.read(aa, ksind, ydlen))) < ydlen) {
                    ydlen -= sj;
                    ksind += sj;
                }

                bytes.write(aa);
                in.read();
                in.read();
                tmpos.reset();
            } else {
                tmpos.write(data);
            }
        }
        return tmpos.toByteArray();
    }
     
    /**
     * bts�ĺ�ends.length���ֽ��Ƿ��ends���
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