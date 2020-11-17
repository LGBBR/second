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
	Image downImg,leftImg,currentImg,upImg,rightImg,leftImg1,leftImg2,rightImg1,rightImg2,downImg1,downImg2,upImg1,upImg2;
	int x,y;
	static int left=0,right=0,down=0,up=0;
	public MainCanvas(){
		try
		{
			/*
			给变量赋值
			语法：变量名称=value;
			*/
			downImg=Image.createImage("/sayo10.png");
			leftImg=Image.createImage("/sayo12.png");
			upImg=Image.createImage("/sayo14.png");
			rightImg=Image.createImage("/sayo16.png");
			leftImg1=Image.createImage("/sayo02.png");
			leftImg2=Image.createImage("/sayo22.png");
			rightImg1=Image.createImage("/sayo26.png");
			rightImg2=Image.createImage("/sayo06.png");
			upImg1=Image.createImage("/sayo04.png");
			upImg2=Image.createImage("/sayo24.png");
			downImg1=Image.createImage("/sayo00.png");
			downImg2=Image.createImage("/sayo20.png");
			currentImg=downImg;
			x=120;
			y=100;
		}
		catch (IOException e)
		{
			e.printStackTrace();
		}
	}
	public void paint(Graphics g){
		g.setColor(0,0,110);
		g.fillRect(0,0,getWidth(),getHeight());
		g.drawImage(currentImg,x,y,0);//120：X坐标、100：Y坐标
	}
	public void keyPressed(int keyCode){
		int action=getGameAction(keyCode);
		/*
		action的值：UP、DOWN、LEFT、RIGHT
		*/
		if(action==LEFT){	
			currentImg=leftImg;
			x=x-2;
			left=left+1;
			if(left>1)
				left=0;
			if(left==1)
				currentImg=leftImg1;
			if(left==0)
				currentImg=leftImg2;
			
		}
		if(action==RIGHT){	
			currentImg=rightImg;	
			x=x+2;
			right=right+1;
			if(right>1)
				right=0;
			if(right==1)
				currentImg=rightImg2;
			if(right==0)
				currentImg=rightImg1;
		}
		if(action==UP){			
			currentImg=upImg;
			y=y-2;
			down=down+1;
			if(up>1)
				up=0;
			if(up==1)
				currentImg=upImg2;
			if(up==0)
				currentImg=upImg1;
		}
		if(action==DOWN){			
			currentImg=downImg;		
			y=y+2;
			down=down+1;
			if(down>1)
				down=0;
			if(down==1)
				currentImg=downImg2;
			if(down==0)
				currentImg=downImg1;
		}
		repaint();
		/*switch(action)
		{
			case LEFT :currentImg=leftImg;
			break;
			case RIGHT :currentImg=rightImg;
			break;
			case UP :currentImg=upImg;
			break;
			case DOWN :currentImg=downImg;
			break;
			default:break;
		}
		repaint();
		*/
	}
}