package ftps;

import java.io.Writer;

public interface Command {
	/** 
     * @param data    ��ftp�ͻ��˽��յĳ�ftp����֮������� 
     * @param writer  ��������� 
     * @param t       ������������Ӧ�Ĵ����߳� 
     * */  
    public void getResult(String data,Writer writer,Call t);  
}
