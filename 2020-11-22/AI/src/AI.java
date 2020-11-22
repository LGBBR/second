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
	Image heroImg[][]=new Image[4][3];
	Image currentImg;
	int x,y;
	int left=0,right=0,down=0,up=0;
	
	public MainCanvas(){
		try
		{
			/*
			给变量赋值
			语法：变量名称=value;
			*/
			for(int i=0;i<heroImg.length;i++)
				for(int j=0;j<heroImg[i].length;j++)
					heroImg[i][j]=Image.createImage("/sayo"+i+j+".png");
			currentImg=heroImg[3][1];
			x=120;
			y=100;
		}
		catch (IOException e)
		{
			e.printStackTrace();
		}
	}
	public void paint(Graphics g){
		g.setColor(220,20,60);
		g.fillRect(0,0,getWidth(),getHeight());
		g.drawImage(currentImg,x,y,0);//120：X坐标、100：Y坐标
	}
	public void keyPressed(int keyCode){
		int action=getGameAction(keyCode);
		/*
		action的值：UP、DOWN、LEFT、RIGHT
		*/
		if(action==LEFT){	
			currentImg=heroImg[0][1];
			x=x-2;
			left=left+1;
			if(left>1)
				left=0;
			if(left==1)
				currentImg=heroImg[0][0];
			if(left==0)
				currentImg=heroImg[0][2];	
		}
		if(action==RIGHT){	
			currentImg=heroImg[1][1];	
			x=x+2;
			right=right+1;
			if(right>1)
				right=0;
			if(right==1)
				currentImg=heroImg[1][2];
			if(right==0)
				currentImg=heroImg[1][0];
		}
		if(action==UP){			
			currentImg=heroImg[2][1];
			y=y-2;
			up=up+1;
			if(up>1)
				up=0;
			if(up==1)
				currentImg=heroImg[2][0];
			if(up==0)
				currentImg=heroImg[2][2];
		}
		if(action==DOWN){			
			currentImg=heroImg[3][1];		
			y=y+2;
			down=down+1;
			if(down>1)
				down=0;
			if(down==1)
				currentImg=heroImg[3][2];
			if(down==0)
				currentImg=heroImg[3][0];
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