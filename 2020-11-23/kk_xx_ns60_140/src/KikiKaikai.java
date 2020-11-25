import java.io.*;
import java.util.*;
import javax.microedition.io.*;
import javax.microedition.rms.*;
import javax.microedition.midlet.MIDlet;
import javax.microedition.lcdui.*;
import com.nokia.mid.ui.*;
import com.nokia.mid.sound.*;

public class KikiKaikai extends MIDlet{

	public static final String ABOUT_NAME = "About";
	public static final String ABOUT = "KikiKaiKai\nVersion 1.4.0\n\n(C) 2003 TAITO. All Right Reserved.";
	public static final String OPTIONS = "Options";
	public static final String EXIT = "Exit";
	public static final String QUIT = "Quit";
	public static final String HISCORE = "High score: ";
	public static final String MENU_NAME = "KikiKaikai";
	public static final String[] NEW_GAME_MENU_ITEMS = {"New game","Select scene","Settings", "Instructions","About","Exit"};
	public static final String[] CONTINUE_GAME_MENU_ITEMS = {"Continue","New game","Select scene","Settings", "Instructions","About","Exit"};
	public static final int MENU_NEW = 0;
	public static final int MENU_CONTINUE = 1;
	public static final String SETTINGS_NAME = "Settings";
	public static final String SOUND_ON = "Sound on";
	public static final String SOUND_OFF = "Sound off";
	public static final String VIBRATION_ON = "Vibration on";
	public static final String VIBRATION_OFF = "Vibration off";
	public static final Command START_COMMAND = new Command("Start", Command.OK, 0);
	public static final String MISSION_SELECT_NAME = "Select scene";
	public static final String MISSION_SELECT = "Scene";
	public boolean isSoundOn;
	public boolean isVibrationOn;
	public boolean isAutoShotOn;
	private static final RMSStore res = new RMSStore("taito.kk");
	private Display	display ;
	private GCanvas Kc ;

	public  NokiaGameEffects gameEffects;

	public static Image top;
	public static Image left;
	public static Image right;
	public static Image die_img;
	
	public KikiKaikai(){

		display = Display.getDisplay(this);
		try
		{
			top = Image.createImage( "/top.png" );
			left = Image.createImage( "/left.png" );
			right = Image.createImage( "/right.png" );
			die_img = Image.createImage("/die.png");
		}
		catch(Exception e)
		{
			System.out.println("error load bg img "); return;
		}
		gameEffects = new NokiaGameEffects();
		if( res.loadData() != 0)
		{
			res.deleteData();
			res.resetData();
		}
		isSoundOn = res.getSoundFlag();
		isVibrationOn = res.getVibrationFlag();
		isAutoShotOn = res.getAutoShotFlag();
        //setHighMissionNum(7);
	}


	public void logoScreenExitRequest()
	{
		exitRequest();
	}

	public int getHighScore()
	{
		return res.getHighScore();
	}
	public void setHighScore ( int aScore )
	{
		res.setHighScore( aScore);
		res.saveData();
	}

	public void exitRequest()
	{
		destroyApp( true);
		notifyDestroyed();
	}

	public void setSoundFlag(boolean b){
		isSoundOn = b;
		res.setSoundFlag( isSoundOn);
		res.saveData();
	}
	public boolean getSoundFlag(){
		return isSoundOn;
	}

	public void setVibrationFlag(boolean b){
		isVibrationOn = b;
		res.setVibrationFlag( isVibrationOn);
		res.saveData();
	}
	public boolean getVibrationFlag(){
		return isVibrationOn;
	}

		public void setAutoShotFlag(boolean b){
		isAutoShotOn = b;
		res.setAutoShotFlag(isAutoShotOn);
		res.saveData();
	}
	public boolean getAutoShotFlag(){
		return isAutoShotOn;
	}

	protected void startApp()
	{
		Kc = new GCanvas(this);
		display.setCurrent(Kc);
		Kc.startGame();
	}

	public int getHighMissionNum()
	{
		return res.getHighMissionNum();
	}

	public void setHighMissionNum ( int aMission )
	{
		res.setHighMissionNum( aMission);
		res.saveData();
	}

	protected void pauseApp()
	{
	}

	protected void destroyApp( boolean unconditional )
	{
	}

}
