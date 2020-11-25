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
class MainCanvas extends Canvas implements Runnable
{

	/*
	变量的声明
	语法：数据类型 变量名称（标识符）;
	*/
	Thread thread;
	Image heroImg[][]=new Image[4][3];
	Image currentImg,bossImg;
	int heroX,heroY,bossX,bossY,flag;
	
	public MainCanvas (){
		try
		{
			/*
			给变量赋值
			语法：变量名称=value;
			*/
			for(int i=0;i<heroImg.length;i++)
				for(int j=0;j<heroImg[i].length;j++)
					heroImg[i][j]=Image.createImage("/sayo"+i+j+".png");
			bossImg=Image.createImage("/zuzu000.png");
			currentImg=heroImg[3][1];
			heroX=120;
			heroY=100;
			bossX=0;
			bossY=0;
			flag=0;

			thread=new Thread(this);
			thread.start();

		}
		catch (IOException e)
		{
			e.printStackTrace();
		}
	}
public void run(){
		while(true){
			try
			{
				Thread.sleep(200);//FPS：屏幕刷新率
			}
			catch(InterruptedException e){
				e.printStackTrace();
			}
			if(bossX<heroX){
				bossX++;
			}
			else{
				bossX--;
			}

			if(bossY<heroY){
				bossY++;
			}else{
				bossY--;
			}
			repaint();
		}
	}

	public void paint(Graphics g){
		g.setColor(220,20,60);
		g.fillRect(0,0,getWidth(),getHeight());
		g.drawImage(currentImg,heroX,heroY,0);//120：X坐标、100：Y坐标
		g.drawImage(bossImg,bossX,bossY,0);
	}

	public void changePicAndMove(int direction)
	{
			if(flag==0)
		{
			currentImg=heroImg[direction][0];
			flag++;
		}
		else if(flag==1)
		{
				currentImg=heroImg[direction][2];	
				flag=0;
		}
	}
	public void keyPressed(int keyCode){
		int action=getGameAction(keyCode);
		/*
		action的值：UP、DOWN、LEFT、RIGHT
		*/
		if(action==LEFT){	
			heroX=heroX-2;
			changePicAndMove(0);
		}
		if(action==RIGHT){	
			heroX=heroX+2;
			changePicAndMove(1);
		}
		if(action==UP){			
			heroY=heroY-2;
			changePicAndMove(2);
		}
		if(action==DOWN){			
			heroY=heroY+2;
			changePicAndMove(3);
		}
		repaint();
		
	}
}