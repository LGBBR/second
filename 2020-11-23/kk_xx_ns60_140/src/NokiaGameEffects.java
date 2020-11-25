import com.nokia.mid.sound.Sound;
import com.nokia.mid.sound.SoundListener;
import com.nokia.mid.ui.DeviceControl;
import java.io.*;

class NokiaGameEffects implements SoundListener
{
	private volatile boolean isPaused = true;
	private volatile int currentSoundPriority = 0;
	private static final int priorityOthers		= 1; //highest priority

	private Sound sndTitleScrBGM = null;
	private Sound sndGodCaptured = null;
	private Sound sndGateClosed = null;
	private Sound sndBossBGM = null;
	private Sound sndGameOver = null;
	private Sound sndClear = null;
	private Sound sndShoot = null;
	private Sound sndPickup = null;
	private Sound sndEnemyDie = null;
	private Sound sndBossShoot = null;
	private Sound sndDie = null;
	private Sound sndEndMain = null;//ending main

	public NokiaGameEffects()
	{
    sndTitleScrBGM = getSound("t_scr_bgm.ott"); //title screen bgm
		sndGodCaptured = getSound("god_captured.ott"); //god captured
		sndGateClosed = getSound("gate_closed.ott"); //gate closed
		sndBossShoot = getSound("boss_shoot.ott"); //boss _shoot
		sndBossBGM = getSound("boss_bgm.ott"); //boss_bgm
		sndShoot = getSound("shoot.ott"); //shoot
		sndGameOver = getSound("game_over.ott"); //game over
		sndClear = getSound("clear.ott"); //clear
		sndDie = getSound("die.ott"); //die
		sndPickup = getSound("pickup.ott");//pickup items
		sndEnemyDie = getSound("enemy_die.ott"); //enemy_die
		sndEndMain = getSound("end_main.ott"); //ending main
		
		sndTitleScrBGM.setSoundListener(this);
		sndGodCaptured.setSoundListener(this);
		sndGateClosed.setSoundListener(this);
		sndBossShoot.setSoundListener(this);				
		sndBossBGM.setSoundListener(this);
		sndShoot.setSoundListener(this);
		sndGameOver.setSoundListener(this);
		sndClear.setSoundListener(this);
		sndDie.setSoundListener(this);
		sndPickup.setSoundListener(this);
		sndEnemyDie.setSoundListener(this);
		
	}

	public void soundStateChanged(Sound s, int event){
	}

	public void pause()
	{
		setIsPaused(true);
		if (sndTitleScrBGM.getState() == Sound.SOUND_PLAYING) sndTitleScrBGM.stop();
		if (sndGodCaptured.getState() == Sound.SOUND_PLAYING) sndGodCaptured.stop();
		if (sndGateClosed.getState() == Sound.SOUND_PLAYING) sndGateClosed.stop();//stateChanged method will be called but the flag isPaused has been set to false to prevent playing background music again
		if (sndBossShoot.getState() == Sound.SOUND_PLAYING) sndBossShoot.stop();
		if (sndBossBGM.getState() == Sound.SOUND_PLAYING) sndBossBGM.stop();
		if (sndShoot.getState() == Sound.SOUND_PLAYING) sndShoot.stop();
		if (sndGameOver.getState() == Sound.SOUND_PLAYING) sndGameOver.stop();
		if (sndClear.getState() == Sound.SOUND_PLAYING) sndClear.stop();
		if (sndDie.getState() == Sound.SOUND_PLAYING) sndDie.stop();
		if (sndPickup.getState() == Sound.SOUND_PLAYING) sndPickup.stop();
		if (sndEnemyDie.getState() == Sound.SOUND_PLAYING) sndEnemyDie.stop();		
	}	//pause

public void resume()
{
	setIsPaused(false);  // Enable Sounds to be played.
	currentSoundPriority = 0;
}


public void playTitleScrBGM() //title screen bgm
{
	playSndTitleScrBGM(priorityOthers);
}	
private void playSndTitleScrBGM(int priority) //title screen bgm
{
	if ((isPaused ==false) && (priority >= currentSoundPriority)) {
		try {
			sndTitleScrBGM.play(1); // loop once
			currentSoundPriority = priority;
		}
		catch(Exception e) {
			currentSoundPriority = 0;
		}
	}
}

public void playGodCaptured() //god captured
{
	playSndGodCaptured(priorityOthers);
}
private void playSndGodCaptured(int priority) //god running away
{
	if ((isPaused ==false) && (priority >= currentSoundPriority)) {
		try {
			sndGodCaptured.play(1); // loop once
			currentSoundPriority = priority;
		}
		catch(Exception e) {
			currentSoundPriority = 0;
		}
	}
}

public void playGateClosed() //gate closed
{
	playSndGateClosed(priorityOthers);
}
private void playSndGateClosed(int priority) //gate closed
{
	if ((isPaused ==false) && (priority >= currentSoundPriority)) {
		try {
			sndGateClosed.play(1); // loop once
			currentSoundPriority = priority;
		}
		catch(Exception e) {
			currentSoundPriority = 0;
		}
	}
}


public void playBossBGM() //boss_bgm
{
	playSndBossBGM(priorityOthers);
}
private void playSndBossBGM(int priority) //boss_bgm
{
	if ((isPaused == false) && (priority >= currentSoundPriority)) {
		try {
			sndBossBGM.play(1); // loop once
			currentSoundPriority = priority;
		}
		catch(Exception e) {
			currentSoundPriority = 0;
		}
	}
}

public void playGameOver() //game over
{
	playSndGameOver(priorityOthers);
}
private void playSndGameOver(int priority) //game over
{
	if ((isPaused == false) && (priority >= currentSoundPriority)) {
		try {
			sndGameOver.play(1); // loop once
			currentSoundPriority = priority;
		}
		catch(Exception e) {
			currentSoundPriority = 0;
		}
	}
}

public void playClear() //clear
{
	playSndClear(priorityOthers);
}
private void playSndClear(int priority) //clear
{
	if ((isPaused == false) && (priority >= currentSoundPriority)) {
		try {
			sndClear.play(1); // loop once
			currentSoundPriority = priority;
		}
		catch(Exception e) {
			currentSoundPriority = 0;
		}
	}
}

public void playShoot() //shoot
{
	playSndShoot(priorityOthers);
}
private void playSndShoot(int priority) //shoot
{
	if ((isPaused == false) && (priority >= currentSoundPriority)) {
		try {
			sndShoot.play(1); // loop = 1
			currentSoundPriority = priority;
		}
		catch(Exception e) {
			currentSoundPriority = 0;
		}
	}
}

public void playPickup() //pick up items
{
	playSndPickup(priorityOthers);
}
private void playSndPickup(int priority)
{
	if ((isPaused == false) && (priority >= currentSoundPriority)) {
		try {
			sndPickup.play(1); // loop = 1
			currentSoundPriority = priority;
		}
		catch(Exception e) {
			currentSoundPriority = 0;
		}
	}
}

public void playEnemyDie() //enemy_die
{
	playSndEnemyDie(priorityOthers);
}
private void playSndEnemyDie(int priority) //enemy_die
{
	if ((isPaused == false) && (priority >= currentSoundPriority)) {
		try {
			sndEnemyDie.play(1); // loop = 1
			currentSoundPriority = priority;
		}
		catch(Exception e) {
			currentSoundPriority = 0;
		}
	}
}

public void playBossShoot() //boss shoot
{
	playSndBossShoot(priorityOthers);
}
private void playSndBossShoot(int priority) //boss shoot
{
	if ((isPaused == false) && (priority >= currentSoundPriority)) {
		try {
			sndBossShoot.play(1); // loop = 1
			currentSoundPriority = priority;
		}
		catch(Exception e) {
			currentSoundPriority = 0;
		}
	}
}

public void playDie() //die
{
	playSndDie(priorityOthers);
}
private void playSndDie(int priority) //die
{
	if ((isPaused == false) && (priority >= currentSoundPriority)) {
		try {
			sndDie.play(1); // loop = 1
			currentSoundPriority = priority;
		}
		catch(Exception e) {
			currentSoundPriority = 0;
		}
	}
}

public void playEndMain() //main
{
	sndEndMain.play(0);
}

public void terminate() //main
{
	sndEndMain.stop();
}

private void setIsPaused(boolean isPaused)
{
	this.isPaused = isPaused;
} //setIsPaused

private Sound getSound(String rcsName) {
	byte[] resource;
	ByteArrayOutputStream bout;
	Sound sd;
	try {
		InputStream in = getClass().getResourceAsStream("/ott/"+rcsName);
		bout = new ByteArrayOutputStream(320);
		byte[] bytes = new byte[320];
		int size;
		while ((size = in.read(bytes)) > 0) {
			bout.write(bytes, 0, size);
		}
		bout.close();
		in.close();
	} catch(Exception e) {
		return(null);
	}

	resource = bout.toByteArray();
	bout = null;
	try {
		sd = new Sound(resource,Sound.FORMAT_TONE );
	} catch(Exception e) {
		return(null);
	}
	return(sd);
}

} //class

