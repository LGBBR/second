import	javax.microedition.rms.*;

public class RMSStore
{
	private final int REC_MAX = 5;
	private int[] rec;
	private String filename;

	public RMSStore(String name)
	{
		filename = name;
		rec = new int[ REC_MAX];
	}

	public	void	resetData()
	{
		rec [0] = 1; //sound		(0=off; 1=on),	default: 1
		rec [1] = 1; //vibration	(0=off; 1=on),	default: 1
		rec [2] = 1; //mission									default: 1
		rec [3] = 0; //score											default: 0
		rec [4] = 0; //auto shot default:0
	}

	public void setSoundFlag(boolean flag)
	{
		if (flag == true) rec [0] = 1;
		else  rec [0] = 0;
	}
	public boolean getSoundFlag()
	{
		if (rec [0] == 1) return true;
		else return false;
	}

	public void setAutoShotFlag(boolean flag)
	{
		if (flag == true) rec [4] = 1;
		else  rec [4] = 0;
	}
	public boolean getAutoShotFlag()
	{
		if (rec [4] == 1) return true;
		else return false;
	}

	public void setVibrationFlag(boolean flag)
	{
		if (flag == true) rec [1] = 1;
		else  rec [1] = 0;
	}
	public boolean getVibrationFlag()
	{
		if (rec [1] == 1) return true;
		else return false;
	}

	public void setHighMissionNum(int val)
	{
		rec [2] = val;
	}
	public int getHighMissionNum()
	{
		return rec [2];
	}

	public void setHighScore(int val)
	{
		rec [3] = val;
	}
	public int getHighScore()
	{
		return rec [3];
	}

	public void deleteData()
	{
		try
		{
			RecordStore.deleteRecordStore( filename);
		}
		catch( RecordStoreNotFoundException e)
		{
		}
		catch( RecordStoreException e)
		{
		}
		catch( Exception ex)
		{
		}
	}

	public	int loadData()
	{
		int i, rec_err;
		String	str;
		byte[] buff;
		RecordStore	recordStore = null;
		rec_err = 0;
		str = "";
		try
		{
			recordStore = RecordStore.openRecordStore( filename, false);
			buff = recordStore.getRecord(1);
			str = new String( buff);
			recordStore.closeRecordStore();

			for( i = 0; i < REC_MAX; i++)
			{
				rec[ i] = Integer.parseInt( str.substring( i*6, i*6+6));
			}
		}
		catch( RecordStoreNotOpenException e)
		{
			rec_err = 1;
		}
		catch( RecordStoreException e)
		{
			rec_err = 2;
		}

		return( rec_err);
	}

	public	int saveData()
	{
		//000001000001000007001835000000
		//means	sound ON
		//					vibration ON
		//					mission 7
		//					score 1835
		//          auto shot OFF
		//total only 30 bytes are needed
		//System.out.println("savaData() called");

		int			i, j;
		int			rec_err;
		String		str;
		byte[]		b;

		RecordStore	recordStore = null;
		deleteData();
		rec_err = 0;

		str = "";
		for( i = 0; i < REC_MAX; i++)
		{
			for( j = Integer.toString( rec[ i]).length(); j < 6; j++)
			{
				str += "0";
			}
			str += rec[ i];
		}

		b = str.getBytes();
		try
		{
			recordStore = RecordStore.openRecordStore( filename, true);
			if( recordStore.getNumRecords() == 0)
			{
				recordStore.addRecord( b, 0, b.length);
			}
			else
			{
				recordStore.setRecord( 1, b, 0, b.length);
			}
			recordStore.closeRecordStore();
		}
		catch( RecordStoreNotOpenException e)
		{
			rec_err = -1;
		}
		catch( RecordStoreException e)
		{
			rec_err = -2;
		}
		return( rec_err);
	}
}
