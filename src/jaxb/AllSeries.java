package jaxb;

import java.io.File;
import java.io.FileInputStream;
import java.io.PrintStream;
import java.util.Arrays;

public class AllSeries {
	
	public static void main(String[] args) throws Exception {
		int a=9;
		int b=9;
		int c=9;
		int d=9;
		int count=0;
		System.setOut(new PrintStream(new File("C:/Users/pet-lsf/Desktop/allmath/不包含6xxx,x8xx,x2xx,1xxx,,xx7x,xx9x,xxx1且包含1,8,0,7,9,3数据.txt")));
		for (int i = 0; i <= a; i++) {
		   for (int j = 0; j <= b; j++) {
			 for (int k = 0; k <=c; k++) {
			  for (int l = 0;l <=d;l++) {
					if(count==8){
						count=0;
						System.out.println();
					}
					if(i==j&&i==k&&i==l){//四位数相等
						   continue;
					} 
					if((i==j&&i==k)||(i==j&&i==l)||(i==k&&i==l)||(j==k&&j==l)){//三位数相同
							continue;
					}
					if((i==j&&k==l)||(i==k&&j==l)||(i==l&&j==k)){//两位数相同
						     continue;
					}
					if(i==j-1&&j==k-1&&k==l-1){
						continue;
					}
					if(i==j+1&&j==k+1&&k==l+1){
						continue;
					}
					//查找最大
					int jj[]=new int[]{i,j,k,l};
					Arrays.sort(jj);
					boolean falg=false;
					for (int m = jj.length-1; m>0; m--) {
						if(jj[m]-1==jj[m-1]){
							falg=true;
						}else{
							falg=false;
							break;
						}
					}
					if(falg){
						continue;
					}
					 String text=i+""+j+""+k+""+l;
					 if((text.contains("1")&&text.contains("8")&&text.contains("0"))){
						continue;
					 }
					 if((text.contains("1")&&text.contains("7")&&text.contains("0"))){
							continue;
						 }
					 if((text.contains("1")&&text.contains("8")&&text.contains("7"))){
							continue;
						 }
					 if((text.contains("8")&&text.contains("0")&&text.contains("7"))){
							continue;
						 }
//					 if(text.contains("8")&&text.contains("7")){
//						 if(text.indexOf("8")!=text.lastIndexOf("8")){
//							 continue;
//						 }
//					 }
//					 if(text.contains("3")&&text.contains("2")){
//						 if(text.indexOf("3")!=text.lastIndexOf("3")){
//							 continue;
//						 }
//					 }
//					if((i==7&&j==9)||(i==7&&k==8)||(i==7&&l==3)||(j==9&&k==8)||(j==9&&l==3)||(k==8&&l==3))
//						  continue;
					if(i==6||j==8||j==2||i==1||l==7||k==9||l==1)
						continue;
//					 System.out.print(text+"\t");
//					  count++;
				  if(text.contains("1")||text.contains("8")||text.contains("0")||text.contains("7")||text.contains("9")||text.contains("3")){
						  System.out.print(text+"\t");
						  count++;
				   }
				}
				}
		 }
		}
		
	}

}
