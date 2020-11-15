import javax.microedition.lcdui.*;
import javax.microedition.midlet.*;
import java.io.*;

public class AI extends MIDlet
{
	Display display;
	MainCanvas mc;
	public AI(){
		display=Display.getDisplay(this);
		mc=new MainCanvas();
		display.setCurrent(mc);
	}
	public void startApp(){
	}
	public void destroyApp(boolean unc){
	}
	public void pauseApp(){
	}
}
class MainCanvas extends Canvas
{

	/*
	变量的声明
	语法：数据类型 变量名称（标识符）;
	*/
	Image img,img1,currentImg,img2,img3;
	public MainCanvas(){
		try
		{
			/*
			给变量赋值
			语法：变量名称=value;
			*/
			img=Image.createImage("/sayo10.png");
			img1=Image.createImage("/sayo12.png");
			img2=Image.createImage("/sayo14.png");
			img3=Image.createImage("/sayo16.png");
			currentImg=img;
		}
		catch (IOException e)
		{
			e.printStackTrace();
		}
	}
	public void paint(Graphics g){
		g.setColor(0,0,0);
		g.fillRect(0,0,getWidth(),getHeight());
		g.drawImage(currentImg,120,100,0);//120：X坐标、100：Y坐标
	}
	public void keyPressed(int keyCode){
		int action=getGameAction(keyCode);
		/*
		action的值：UP、DOWN、LEFT、RIGHT
		*/
		/*if(action==LEFT){
			
			currentImg=img1;
			System.out.println("向左转");
			
		}
		if(action==RIGHT){
			
			currentImg=img3;
			System.out.println("向右转");
			
		}
		if(action==UP){
			
			currentImg=img2;
			System.out.println("向上转");
			
		}
		if(action==DOWN){
			
			currentImg=img;
			System.out.println("向下转");
			
		}
		repaint();*/
		switch(action)
		{
			case LEFT :currentImg=img1;
			System.out.println("向左转");
			break;
			case RIGHT :currentImg=img3;
			System.out.println("向右转");
			break;
			case UP :currentImg=img2;
			System.out.println("向上转");
			break;
			case DOWN :currentImg=img;
			System.out.println("向下转");
			break;
			default:break;
		}
		repaint();
	}
}