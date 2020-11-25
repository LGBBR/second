import java.io.*;
import java.util.*;
public class ConvertPNGImg
{

	/*
	Change the IMAGE_NO, and OUTPUT_FILE_NAME if needed.
	*/
	private static int IMAGE_NO = 8;
	private static String OUTPUT_FILE_NAME= "item.dat";


	public static void main(String[] args){
		FileInputStream fis = null;
		DataInputStream dis = null;
		DataOutputStream dos = null;
		int imageno = 0;
		Vector v = new Vector();
		try{
			FileOutputStream fos = new FileOutputStream(OUTPUT_FILE_NAME);
			dos = new DataOutputStream(fos);
				   while(imageno<IMAGE_NO){
							System.out.println("Begin to read images "+imageno+".png" );
							fis = new FileInputStream(imageno+".png");
							dis = new DataInputStream(fis);
							boolean eof = false;
							int i =0;
							try{
							while(!eof){  byte b = dis.readByte(); dos.writeByte(b);i++; }//System.out.println("is " + i + " " + b); i++; }  
							}catch(Exception e){eof=true; imageno++; v.add(""+i);continue; }
					 }//end while
		}
		catch(Exception e){e.printStackTrace();}
		/*for(int i = 0;i<v.size();i++){
			String s = (String) v.get(i);
			System.out.print(s+",");
		}*/		
		imageno = 0;
		try{
			FileOutputStream fos = new FileOutputStream(OUTPUT_FILE_NAME);
			dos = new DataOutputStream(fos);
			
			dos.writeInt(IMAGE_NO);
			for(int i=0;i<v.size();i++){
				String s = (String) v.get(i);
				int temp = Integer.parseInt(s);
				dos.writeInt(temp);
			}
			
				   while(imageno<IMAGE_NO){
							System.out.println("Begin to read images "+imageno+".png" );
							fis = new FileInputStream(imageno+".png");
							dis = new DataInputStream(fis);
							boolean eof = false;
							int i =0;
							try{
							while(!eof){  byte b = dis.readByte(); dos.writeByte(b);i++; }//System.out.println("is " + i + " " + b); i++; }  
							}catch(Exception e){eof=true; imageno++; v.add(""+i);continue; }
					 }//end while
		}
		catch(Exception e){e.printStackTrace();}
	}
}

