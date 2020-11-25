import java.io.*;
import java.util.*;
import javax.microedition.io.*;
import javax.microedition.lcdui.*;
import javax.microedition.midlet.*;
import javax.microedition.rms.*;
import com.nokia.mid.sound.Sound;
import com.nokia.mid.ui.*;

public class GCanvas extends FullCanvas implements Runnable
{
	private boolean Ending_1 = true;
	private boolean Ending_2 = true;
	private static final int MILLIS_PER_UPDATE = 50;
	private volatile Thread animationThread = null; // animation thread
	private int Seven_Gods_position = -50;
	private int going_count = 0 ;
	private static Image ending_img[] = new Image[7];
	private int loading_Percent = 0;
	private Font fTime  = Font.getFont( Font.FACE_MONOSPACE, Font.STYLE_PLAIN, Font.SIZE_SMALL);

	//----------------Splash Screen varibles---------------
	private int scrW, scrH;
	private static Image imgSplash;
	private int refreshRate = 0 ;
	private static KikiKaikai midlet;
	private boolean isRunning=true;
	private int Setting_Screen_Count = 0;

	//----------------Logo Screen varibles------------------

	private static Image gameGod = null;
	private static Image gameTaito = null;
	private static final Font fGame = Font.getFont(Font.FACE_MONOSPACE, Font.STYLE_BOLD, Font.SIZE_LARGE);
	private String str;

	//----------------Menu Screen varibles------------------
	public static final int MENU_NEW = 0;
	public static final int MENU_CONTINUE = 1;
	public static int MENU_CURRENT = 0;
	private boolean ContinueFlag = false ;
	private int Menu_Screen_Count=0;
	private int menuType = 0;
	
	//---------------Instructions Screen Varibles--------------
	private int InstCount = 0;
	private static Image up = null ;
	private static Image dn = null ;
	private static Image[] items=new Image[8];
	
	//----------------Select Screen Varibles---------------
	private int stage_finished;
	private int TRdv=1;
	//----------------Game Screen Varibles----------------
	private static final int SOFT_KEY1 = -6;
	private static final int SOFT_KEY2 = -7;
	private static Image gameIcon = null;
	private int myCount2=0 ;
	private boolean isOperation = false;
	private static final int Screen_Splash = 0;
	private static final int Screen_Logo   = 1;
	private static final int Screen_Menu   = 2;
	private static final int Screen_Game   = 3;
	private static final int Screen_Select = 4;
	private static final int Screen_Setting= 5;
	private static final int Screen_Instructions = 6;
	private static final int Screen_About  = 7;
	private int Screen_Current= 0;

	private static Image topI;
	private static Image leftI;
	private static Image rightI;

	private NokiaGameEffects gameEffect = null;

	private static final int SOUND_2 = 2;
	private static final int SOUND_3 = 3;
	private static final int SOUND_4 = 4;
	private static final int SOUND_8 = 8;
	private static final int SOUND_9 = 9;
	private static final int SOUND_12 = 12;
	private static final int SOUND_13 = 13;
	private static final int SOUND_14 = 14;
	private static final int SOUND_15 = 15;
	private static final int SOUND_16 = 16;
	private static final int SOUND_18 = 18;
	private static final int SOUND_19 = 19;
	private static final int SOUND_20 = 20;
	private static final int SOUND_23 = 23;
	private static final int SOUND_24 = 24;
	private static final int SOUND_25 = 25;

	private static final byte[][][] WORLDMAP = {
		{
			{ 0x00, 0x20, 0x00, 0x20, 0x00},
			{ 0x20, 0x08, 0x29, 0x0A, 0x20},
			{ 0x00, 0x27, 0x00, 0x2B, 0x00},
			{ 0x20, 0x06, 0x20, 0x0C, 0x20},
			{ 0x00, 0x25, 0x00, 0x20, 0x00},
			{ 0x20, 0x04, 0x20, 0x00, 0x20},
			{ 0x00, 0x23, 0x00, 0x20, 0x00},
			{ 0x20, 0x02, 0x20, 0x00, 0x20},
			{ 0x00, 0x21, 0x00, 0x20, 0x00},
			{ 0x20, 0x00, 0x20, 0x00, 0x20},
		},
		{
			{ 0x00, 0x20, 0x00, 0x20, 0x00, 0x20, 0x00, 0x20, 0x00},
			{ 0x20, 0x00, 0x20, 0x00, 0x20, 0x00, 0x20, 0x01, 0x20},
			{ 0x00, 0x20, 0x00, 0x20, 0x00, 0x20, 0x00, 0x22, 0x00},
			{ 0x20, 0x00, 0x20, 0x00, 0x20, 0x00, 0x20, 0x03, 0x20},
			{ 0x00, 0x20, 0x00, 0x20, 0x00, 0x20, 0x00, 0x24, 0x00},
			{ 0x20, 0x00, 0x20, 0x00, 0x20, 0x00, 0x20, 0x05, 0x20},
			{ 0x00, 0x20, 0x00, 0x20, 0x00, 0x20, 0x00, 0x26, 0x00},
			{ 0x20, 0x00, 0x20, 0x00, 0x20, 0x00, 0x20, 0x07, 0x20},
			{ 0x00, 0x2E, 0x0D, 0x2C, 0x0B, 0x2A, 0x09, 0x28, 0x00},
			{ 0x20, 0x00, 0x20, 0x00, 0x20, 0x00, 0x20, 0x00, 0x20},
		},
		{
			{ 0x00, 0x20, 0x00, 0x20, 0x00},
			{ 0x20, 0x09, 0x28, 0x07, 0x20},
			{ 0x00, 0x2A, 0x00, 0x26, 0x00},
			{ 0x20, 0x0B, 0x20, 0x05, 0x20},
			{ 0x00, 0x2C, 0x00, 0x24, 0x00},
			{ 0x20, 0x0D, 0x20, 0x03, 0x20},
			{ 0x00, 0x2E, 0x00, 0x22, 0x00},
			{ 0x20, 0x00, 0x20, 0x01, 0x20},
			{ 0x00, 0x20, 0x00, 0x20, 0x00},
		},
		{
			{ 0x00, 0x20, 0x00, 0x20, 0x00, 0x20, 0x00, 0x20, 0x00, 0x20, 0x00, 0x20, 0x00, 0x20, 0x00, 0x20, 0x00, 0x20},
			{ 0x20, 0x10, 0x2F, 0x0E, 0x2D, 0x0C, 0x2B, 0x0A, 0x29, 0x08, 0x27, 0x06, 0x25, 0x04, 0x23, 0x02, 0x21, 0x00},
			{ 0x00, 0x31, 0x00, 0x20, 0x00, 0x20, 0x00, 0x20, 0x00, 0x20, 0x00, 0x20, 0x00, 0x20, 0x00, 0x20, 0x00, 0x20},
			{ 0x20, 0x12, 0x20, 0x00, 0x20, 0x00, 0x20, 0x00, 0x20, 0x00, 0x20, 0x00, 0x20, 0x00, 0x20, 0x00, 0x20, 0x00},
			{ 0x00, 0x20, 0x00, 0x20, 0x00, 0x20, 0x00, 0x20, 0x00, 0x20, 0x00, 0x20, 0x00, 0x20, 0x00, 0x20, 0x00, 0x20},
		},
		{
			{ 0x00, 0x20, 0x00, 0x20, 0x00, 0x20, 0x00, 0x20, 0x00, 0x20},
			{ 0x20, 0x01, 0x20, 0x00, 0x20, 0x00, 0x20, 0x00, 0x20, 0x00},
			{ 0x00, 0x22, 0x00, 0x20, 0x00, 0x20, 0x00, 0x20, 0x00, 0x20},
			{ 0x20, 0x03, 0x20, 0x00, 0x20, 0x00, 0x20, 0x00, 0x20, 0x00},
			{ 0x00, 0x24, 0x00, 0x20, 0x00, 0x20, 0x00, 0x20, 0x00, 0x20},
			{ 0x20, 0x05, 0x26, 0x07, 0x28, 0x09, 0x2A, 0x0B, 0x2C, 0x00},
			{ 0x00, 0x20, 0x00, 0x20, 0x00, 0x20, 0x00, 0x20, 0x0D, 0x20},
			{ 0x20, 0x00, 0x20, 0x00, 0x20, 0x00, 0x20, 0x00, 0x2E, 0x00},
			{ 0x00, 0x20, 0x00, 0x20, 0x00, 0x20, 0x00, 0x20, 0x0F, 0x20},
			{ 0x20, 0x00, 0x20, 0x00, 0x20, 0x00, 0x20, 0x00, 0x30, 0x00},
			{ 0x00, 0x20, 0x00, 0x20, 0x00, 0x20, 0x00, 0x20, 0x11, 0x20},
			{ 0x20, 0x00, 0x20, 0x00, 0x20, 0x00, 0x20, 0x00, 0x20, 0x00},
		},
		{
			{ 0x00, 0x20, 0x00, 0x20, 0x00, 0x20, 0x00, 0x20, 0x00, 0x20, 0x00, 0x20, 0x00},
			{ 0x20, 0x00, 0x20, 0x00, 0x20, 0x00, 0x20, 0x00, 0x20, 0x00, 0x20, 0x12, 0x20},
			{ 0x00, 0x20, 0x00, 0x20, 0x00, 0x20, 0x00, 0x20, 0x00, 0x20, 0x00, 0x31, 0x00},
			{ 0x20, 0x00, 0x20, 0x00, 0x20, 0x00, 0x20, 0x00, 0x20, 0x00, 0x20, 0x10, 0x20},
			{ 0x00, 0x20, 0x00, 0x20, 0x00, 0x20, 0x00, 0x20, 0x00, 0x20, 0x00, 0x2F, 0x00},
			{ 0x20, 0x00, 0x20, 0x00, 0x20, 0x00, 0x20, 0x00, 0x20, 0x00, 0x20, 0x0E, 0x20},
			{ 0x00, 0x20, 0x00, 0x20, 0x00, 0x20, 0x00, 0x20, 0x0A, 0x2B, 0x0C, 0x2D, 0x00},
			{ 0x20, 0x00, 0x20, 0x00, 0x20, 0x00, 0x20, 0x00, 0x29, 0x00, 0x20, 0x00, 0x20},
			{ 0x00, 0x21, 0x02, 0x23, 0x04, 0x25, 0x06, 0x27, 0x08, 0x20, 0x00, 0x20, 0x00},
			{ 0x20, 0x00, 0x20, 0x00, 0x20, 0x00, 0x20, 0x00, 0x20, 0x00, 0x20, 0x00, 0x20},
		},
		{
			{ 0x00, 0x20, 0x00, 0x20, 0x00, 0x20, 0x00},
			{ 0x20, 0x00, 0x20, 0x00, 0x20, 0x12, 0x20},
			{ 0x00, 0x20, 0x00, 0x20, 0x00, 0x31, 0x00},
			{ 0x20, 0x00, 0x20, 0x0E, 0x2F, 0x10, 0x20},
			{ 0x00, 0x20, 0x00, 0x2D, 0x00, 0x20, 0x00},
			{ 0x20, 0x00, 0x20, 0x0C, 0x20, 0x00, 0x20},
			{ 0x00, 0x20, 0x00, 0x2B, 0x00, 0x20, 0x00},
			{ 0x20, 0x00, 0x20, 0x0A, 0x20, 0x00, 0x20},
			{ 0x00, 0x27, 0x08, 0x29, 0x00, 0x20, 0x00},
			{ 0x20, 0x06, 0x20, 0x00, 0x20, 0x00, 0x20},
			{ 0x00, 0x25, 0x00, 0x20, 0x00, 0x20, 0x00},
			{ 0x20, 0x04, 0x20, 0x00, 0x20, 0x00, 0x20},
			{ 0x00, 0x23, 0x02, 0x21, 0x00, 0x20, 0x00},
			{ 0x20, 0x00, 0x20, 0x00, 0x20, 0x00, 0x20},
		},
	};

	private static final int [] Wxp = {((120 <<1)- 120 )<<16,((120 <<3)- 120 )<<16,((120 <<2)- 120 )<<16,((120 *17)- 120 )<<16,((120 <<1)- 120 )<<16,((120 <<1)- 120 )<<16,((120 <<2)- 120 )<<16};
	private static final int [] Wyp = {((128 *9)- 128 )<<16 ,((128 <<1)- 128 )<<16,((128 <<3)- 128 )<<16,((128 <<1)- 128 )<<16,((128 <<1)- 128 )<<16,((128 *9)- 128 )<<16 ,((128 *13)- 128 )<<16};
	private static final int [] Re_Wxp = {((120 <<2)- 120 )<<16,(120 <<1)<<16,120 <<16,120 <<16,(120 <<3)<<16,(120 *11)<<16,((120 <<2)+ 120 )<<16};
	private static final int [] Re_Wyp = {(128 <<1)<<16,(128 <<3)<<16,((128 <<2)+ 128 )<<16,128 <<16,((128 <<3)- 128 )<<16,(128 <<1)<<16,((128 <<1)+ 128 )<<16};

	private static final short [][] Map_char_num = {
		{0x00,0x01,0x02,0x03,0x04,0x05,0x06,0x07,0x08,0x09,0x0A,0x0B,0x0C,0x0D,0x0E,0x0F,0x10,0x11,0x12,0x13,0x14,0x15,0x16,0x17,0x18,0x19,0x1A,0x1B,0x1C,0x1D,0x1E,0x1F,0x20,0x21,0x22,0x23,0x24,0x25,0x26,0x27,0x28,127,128},
		{0x00,0x04,0x05,0x01,0x02,0x21,0x28,0x03,0x16,0x06,0x07,0x08,0x09,0x29,0x2A,0x2B,0x2C,0x2D,0x2E,0x2F,0x30,0x31,0x32,0x33,0x34,0x35,0x36,0x37,0x17,0x18,0x19,0x1A,0x38,0x39,0x3A,0x3B,0x3C,0x3D,0x22,0x23,0x24,0x25,0x26,0x27,127,128},
		{0x00,0x04,0x01,0x02,0x05,0x21,0x28,0x03,0x06,0x07,0x08,0x09,0x3A,0x3B,0x3C,0x3D,0x17,0x18,0x3E,0x19,0x1A,0x3F,0x40,0x41,0x42,0x43,0x44,0x2B,0x29,0x45,0x46,0x2A,0x39,0x38,0x22,0x23,0x24,0x25,0x26,0x27,127,128},
		{0x00,0x04,0x05,0x01,0x02,0x21,0x28,0x03,0x2B,0x06,0x07,0x2A,0x08,0x09,0x2C,0x2E,0x2D,0x2F,0x47,0x48,0x49,0x4A,0x35,0x17,0x18,0x19,0x1A,0x29,0x39,0x4B,0x4C,0x38,0x4D,0x4E,0x4F,0x50,0x43,0x44,0x3A,0x3B,0x3C,0x3D,0x0A,0x0B,0x0C,0x0D,0x51,0x41,0x42,0x45,0x46,0x22,0x23,0x24,0x25,0x26,0x27,127,128},
		{0x00,0x04,0x05,0x01,0x02,0x21,0x28,0x06,0x07,0x08,0x09,0x3F,0x40,0x41,0x42,0x43,0x44,0x2B,0x29,0x45,0x46,0x2A,0x39,0x4D,0x4E,0x4B,0x52,0x53,0x38,0x4F,0x50,0x4C,0x54,0x55,0x56,0x57,0x58,0x59,0x5A,0x5B,0x5C,0x5D,0x3A,0x3B,0x3C,0x3D,0x5E,0x03,0x5F,0x17,0x18,0x19,0x1A,0x0A,0x0B,0x0C,0x0D,0x22,0x23,0x24,0x25,0x26,0x27,127,128},
		{0x00,0x04,0x05,0x01,0x02,0x21,0x28,0x03,0x60,0x61,0x62,0x63,0x64,0x65,0x66,0x67,0x2A,0x38,0x06,0x07,0x3A,0x3B,0x08,0x09,0x3C,0x3D,0x29,0x2B,0x39,0x17,0x18,0x19,0x1A,0x68,0x69,0x6A,0x6B,0x6C,0x6D,0x6E,0x6F,0x70,0x71,0x72,0x73,0x74,0x75,0x76,0x77,0x78,0x79,0x7A,0x7B,0x7C,0x7D,0x7E,0x2D,0x2C,0x30,0x31,0x32,0x33,0x34,0x35,0x36,0x2E,0x2F,0x37,0x16,0x22,0x23,0x24,0x25,0x26,0x27,127,128},
		{0x00,0x04,0x05,0x01,0x02,0x21,0x28,0x03,0x79,0x7A,0x71,0x7B,0x7C,0x7D,0x7E,0x06,0x07,0x08,0x09,0x72,0x73,0x74,0x75,0x76,0x77,0x33,0x35,0x37,0x34,0x36,0x2C,0x2D,0x38,0x39,0x2A,0x4A,0x2B,0x29,0x2E,0x2F,0x30,0x31,0x32,0x17,0x18,0x19,0x1A,0x3A,0x3B,0x3C,0x3D,0x16,0x22,0x23,0x24,0x25,0x26,0x27,127,128}
	};

	private static final short [][] Zako_char_num = {
		{129 ,130 ,131 ,132 ,133 ,134 ,143 ,144 ,145 ,146 ,147 ,148 ,149 ,150 ,135 ,136 ,137 ,138 ,139 ,140 ,141 ,142
			,151 ,152 ,153 ,154 ,155 ,156 ,157 ,158 ,189 ,190 },
		{129 ,130 ,131 ,132 ,133 ,134 ,143 ,144 ,145 ,146 ,147 ,148 ,149 ,150 ,135 ,136 ,137 ,138 ,139 ,140 ,141 ,142
			,191 ,192 ,193 ,194 ,195 ,196 ,197 ,198 ,199 },
		{129 ,130 ,131 ,132 ,133 ,134 ,143 ,144 ,145 ,146 ,147 ,148 ,149 ,150 ,135 ,136 ,137 ,138 ,139 ,140 ,141 ,142
			,189 ,190 ,191 ,192 ,193 ,194 ,195 ,196 ,197 ,198 ,199 ,159 ,160 ,161 ,162 ,163 ,164 ,165 ,166 ,167 ,168 ,169 ,170 ,171 ,172 ,173 ,174 ,185 ,186 ,187 ,188 },
		{129 ,130 ,131 ,132 ,133 ,134 ,143 ,144 ,145 ,146 ,147 ,148 ,149 ,150 ,135 ,136 ,137 ,138 ,139 ,140 ,141 ,142
			,151 ,152 ,153 ,154 ,155 ,156 ,157 ,158 ,189 ,190 ,191 ,192 ,193 ,194 ,195 ,196 ,197 ,198 ,199 ,159 ,160 ,161 ,162 ,163 ,164 ,165 ,166 ,183 ,184 },
		{129 ,130 ,131 ,132 ,133 ,134 ,143 ,144 ,145 ,146 ,147 ,148 ,149 ,150 ,135 ,136 ,137 ,138 ,139 ,140 ,141 ,142
			,189 ,190 ,191 ,192 ,193 ,194 ,195 ,196 ,197 ,198 ,199 ,159 ,160 ,161 ,162 ,163 ,164 ,165 ,166 ,167 ,168 ,169 ,170 ,171 ,172 ,173 ,174 ,183 ,184 ,175 ,176 ,177 ,178 ,179 ,180 ,181 ,182 },
		{129 ,130 ,131 ,132 ,133 ,134 ,143 ,144 ,145 ,146 ,147 ,148 ,149 ,150 ,135 ,136 ,137 ,138 ,139 ,140 ,141 ,142
			,189 ,190 ,191 ,192 ,193 ,194 ,195 ,196 ,197 ,198 ,199 ,167 ,168 ,169 ,170 ,171 ,172 ,173 ,174 ,185 ,186 ,187 ,188 ,183 ,184 },
		{129 ,130 ,131 ,132 ,133 ,134 ,143 ,144 ,145 ,146 ,147 ,148 ,149 ,150 ,135 ,136 ,137 ,138 ,139 ,140 ,141 ,142
			,151 ,152 ,153 ,154 ,155 ,156 ,157 ,158 ,189 ,190 ,191 ,192 ,193 ,194 ,195 ,196 ,197 ,198 ,199 }
	};

	private static final short[][] Boss_char_num = {
		{200 ,201 ,202 ,203 ,204 ,205 ,206 ,207 ,208 ,271},
		{209 ,210 ,211 ,212 ,272},
		{213 ,214 ,215 ,216 ,217 ,218 ,219 ,220 ,221 ,222 ,223 ,224 ,225 ,226 ,227 ,228 ,273 },
		{229 ,230 ,231 ,232 ,274},
		{233 ,234 ,235 ,236 ,237 ,238 ,275},
		{239 ,240 ,241 ,242 ,243 ,244 ,245 ,246 ,247 ,248 ,249 ,250 ,251 ,252 ,253 ,254 ,255 ,256 ,257 ,258 ,259 ,260 ,261 ,262 ,276 },
		{263 ,264 ,265 ,266 ,267 ,268 ,269 ,270 ,277},
	};

	private static final short[][] Img_char_num = {
		{280 ,281 ,282 ,283 ,284 ,285 ,286 ,287 ,288 ,289 ,290 ,291 ,292 ,293 ,294 ,295 ,296 ,297 ,298 ,299 ,300 ,301 ,302 ,303 ,304
			,305 ,306 ,307 ,308 ,309 ,310 ,311 ,312 ,313 ,314 ,315 ,316 ,317 ,318 ,319 ,320
				,321 ,322 ,323 ,324 ,325 ,326 ,327 ,328
				,329 ,330 ,331 ,332 ,333 ,334 ,335 ,336 ,337 ,338
				,339 ,340 ,341 ,342 ,343
				,344 ,345
				,346 ,347 },
		{278 , 279 },
		{271 , 272 , 273 , 274 , 275 , 276 , 277 }
	};

	private static final int [][] Odou_pos = {{0x01980088},{0x00000000},{0x00F000B8, 0x00900120},{0x00900130},{0x00900218, 0x03E00490},{0x01900410},{0x02600118}};
	private static final int [] Torii_pos = {0x00980268,0x00000000,0x00000000,0x009800C0,0x03E002B8,0x00000000,0x00000000};
	private static final int [][] Ido_pos = {{0x00B000B5},{0x035803A5},{0x014000E5,0x00C80215},{0x04D0009D,0x0400009D,0x0260009D,0x0098009D},{0x02B8029D,0x0410029D},{0x0350042D,0x0378044D,0x040803F5,0x03D803CD,0x040803AD,0x03D80395,0x04D80315,0x053802AD},{0x01A8021D,0x02680185,0x02A80185,0x02A0015D}};
	private static final int [] Seven_Gods_offset_posx = {  2,-28,  6,-17,  4,-30,-15};
	private static final int [] Seven_Gods_offset_posy = { 32, 40, 56, 48, 66, 69, 83};
	private static final byte[] Seven_Gods_escape_posx = { 54, 40, 25, 15,  8,  4,  8, 15, 28, 40, 70, 84, 95,105,110,113,115, 40};
	private static final byte[] Seven_Gods_escape_posy = { 56, 54, 56, 64, 74, 85,104,114,120,124,124,119,114,104, 85, 74, 64,  0};
	private static final byte[] Ofuda_offset = {16,16, 8, 0, 0, 0, 8,16,16,16};
	private static final int [] Ofuda_direction_offset =  {0x00030000, 0x00021999, 0, -0x00021999, -0x00030000, -0x00021999, 0, 0x00021999, 0x00030000, 0x00021999};
	private static final int [] Oharai_direction_offset = {0x00107F89,0x0021089A,0x003219AB,0x00ABC432,0x00543BCD,0x00654CDE,0x00DEF765,0x00EF8076};
	private static final long[] Oharai_offset_x        =  {0x0B100F090405L,0x00070C000100L,0x010008010108L,0x00080D010200L,0x130E0F161A19L,0x1B130E1A191BL,0x191B131A1B13L,0x1C140F1B1A1CL};
	private static final long[] Oharai_offset_y        =  {0x11120B11120BL,0x100F11110904L,0x0B100F090405L,0x030402020A0FL,0x04020A04000AL,0x030402020A0FL,0x0B100F090405L,0x100F11110904L};
	private static final long Oharai_pose = 0x1918070615040302L;

	private static final int Wrold_boss_gate_x = 0x05B81113;
	private static final int Wrold_boss_gate_y = 0x011A3683;

	private static final int[] Map_boss_gate_x = {0x87658765,0xEDEDEDED,0x87658765,0x87658765,0x87658765,0x87658765,0x87658765};
	private static final int[] Map_boss_gate_y = {0x11110000,0x88776655,0x11110000,0x11110000,0x11110000,0xFFFFEEEE,0xFFFFEEEE};

	private static final int [] Boss_hit_area = {0x28180604,0x10101008,0x10101400,0x20100808,0x11110400,0x0F180C00,0x18180000};
	private static final byte[] Zuzu_offset_x = { 8, 8, 0, 8};
	private static final byte[] Zuzu_offset_y = {16, 8,24,16};
	private static final long Zuzu_offsetx = 0x00142F442F14L;
	private static final long Zuzu_offsety = 0x001414001414L;

	private static final int[] Raidenou_offset_x = {-2,10};
	private static final int[] Mabikon_offset_x = {  0,  8,  4,-24,  0,-20,  4,-24,  4,  8,  4,  4,  0,  4,  4,  4};
	private static final int[] Mabikon_offset_y = {  0,  0,  0,  0,  0,  0,  0,-16,  0,-32,  0,-16,  0,  0,  0,  0};
	private static final int[] Mabikon_add_offset_x = {  0,-24,-24,-24,  0, 24, 32, 24};
	private static final int[] Mabikon_add_offset_y = { 32, 16,  0,-16,-32,-16,  0, 16};

	private static final int[] Tama_offset = {0xFFFF0000, 0x00000000, 0x00010000};
	private static final int[] Hashiritai_posx = {0xFFF00000, 0x00780000, 0xFFF00000, 0x00780000};
	private static final int[] Hashiritai_posy = {0x00000000, 0x00000000, 0x00720000, 0x00720000};

	private static final long[][] Kidou_tbl   = {{0x00000000FFFD20L, 0x0000000004BFFFL},{0x004BFFFFFFD20L, 0x0FFFD2004BFFFL}};
	private static final int [] Kasabe_offset  = {26,18,0,-18,-25,-18,0,18,26,18};
	private static final byte[] Otama_offsetx = {0,10,20,30,40,50,60,70,80,70,60,50,40,30,20,10};
	private static final byte[] Otama_offsety = {30,40,45,40,30,20,15,20,30,40,45,40,30,20,15,20};

	private static final int[] Tobi_offset  = {  6,  6,  0, -6, -6, -6,  0,  6,  6,  6};
	private static final int[] Key_pos = {0x00B00150,0x01F00448,0x00A8015F,0x026000B8,0x03F00470,0x04F00310,0x018003E0};

	private static final int[] Zako_etc_tbl = {0,143 , 0, 151 , 0, 159 , 0, 167 , 0, 175 , 0, 183 , 0, 185 , 191 , 135 };
	private static final int[] Zako_hit_size = {0, 0x10100000, 0, 0x10100000, 0, 0x10180000, 0, 0x10180000, 0, 0x10180000, 0, 0x18180000, 0, 0x0C180000, 0x0C0C0604, 0x08080103};
	private static final int[] Zako_score_tbl = {0, 0x00C80064, 0, 0x00C80064, 0, 0x00C80064, 0, 0x012C00C8, 0, 0x012C00C8, 0, 0x00640000, 0, 0x00C80000, 0x012C0000, 0x00C80064};
	private static final byte[][] Jump_offsetx = {
		{ 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0},
		{-1, 0,-1,-1,-1,-1,-1,-1,-1,-1,-1,-1,-1,-1,-1,-1, 0,-1, 0, 0, 0},
		{ 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0},
		{ 1, 0, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 0, 1, 0, 0, 0}
	};

	private static final byte[][] Jump_offsety = {
		{-1,-1,-1,-1,-1, 0, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 0, 0, 0},
		{-1,-1,-1,-1,-1, 0,-1, 0, 0, 0, 0, 1, 0, 1, 1, 1, 1, 1, 0, 0, 0},
		{-1,-1,-1,-1,-1, 0,-1,-1,-1,-1,-1,-1,-1,-1,-1,-1,-1,-1, 0, 0, 0},
		{-1,-1,-1,-1,-1, 0,-1, 0, 0, 0, 0, 1, 0, 1, 1, 1, 1, 1, 0, 0, 0}
	};

	private static final String [] Boss_name = {"ZUZU","RAIDENOU","SENZAN","SANBA","SOUJYUSOU","MABIKON","MANUKE"};

	private static final String [] No = {"1","2","3","4","5","6","7"};
	private static final long Screen_max_num = 0x121211120E0E0CL;

	private   int[][] Gate_data ={
		{0x0084,0x0084,0x0084,0x0084,0xC005,0xC005,0xC005,0xC005},
		{0x0001,0x0080,0x9001,0x0080,0x9002,0x0080,0x9001,0x0080},
		{0x0084,0x0084,0x0084,0x0084,0xC005,0xC005,0xC005,0xC005},
		{0x0084,0x0084,0x0084,0x0084,0xC005,0xC005,0xC005,0xC005},
		{0x0084,0x0084,0x0084,0x0084,0xC005,0xC005,0xC005,0xC005},
		{0xB001,0xB001,0xB001,0xB001,0x0083,0x0083,0x0083,0x0083},
		{0xB001,0xB001,0xB001,0xB001,0x0083,0x0083,0x0083,0x0083},
	};

	private   short[][][] MAP = new short [19][16][15];

	private   int[][] Tourou_pos = {{0x0086036F,0x00D60257,0x008E0097,0x01CE00EF},{0x035E012F,0x039E03BF,0x01760417},{0x0176032F,0x017E0117,0x00CE01CF,0x00960217},{0x069E00DF,0x065600DF,0x036600DF,0x01FE00DF},{0x00CE026F,0x01C602E7,0x03D6036F},{0x010E041F,0x040E0467,0x051E033F,0x053E0127},{0x01360648,0x02460197,0x0266010F}};
	private   int[][] Kakushi_pos  = {{0x000D8428},{0x043A8138,0x00378268,0x023A03E8,0x05398458,0x01330410,0x001A8430},{0x041C0118},{0x040E00C8,0x000D00E8},{0x050A82E8},{0x05158428,0x04570368},{0x021801A0,0x031A01A0}};

	private   int [] Bresenham_tmp = new int[2];
	private   int Sayo[] = new int[6];
	private   int Ofuda[][] = new int[5][4];
	private   int Kemuri[][] = new int[3][4];
	private   int Tama[][] = new int[8][6];
	private   int Zako[][] = new int[11][12];
	private   int Item[] = new int[2];
	private   int Boss[] = new int[10];

	private boolean soundFlag = false;
	private boolean vibrationFlag = false;
	public  boolean alive = true;
	private boolean gameoverFlag = false;
	private int myCount=0;
	private   int Key_flg;
	private   int OfudaKey;
	private   int Zako_limit_timer;
	private   int Otama_start_posx;
	private   int Otama_start_posy;
	private   int Bakeuri_count;
	private   int Oharai;
	private int Scene  ;
	private int Scene_bak;
	private int Die_Scene;
	private int Clear_Scene;
	private int GETWIDTH;
	private int GETHEIGHT;
	private int Worldxpos;
	private int Worldypos;
	private   int Nowscreen;
	private   int Scroll_quantity = 0x00020000;
	private   int Rumuru_Scroll_quantity;
	private   int AllKeyCode;
	private   int Round_no =0;
	private   int Seven_Gods_pos;

	private   int Credit = 100;
	private   int High_Score = 0;
	private   int Score = 0;
	private   int Mute_flg;
	private   int Auto_Rennsya_flg;

	private   int Key_repeat_check;
	private   int Percent;
	private   int World_timer;
	private   int Time_stop;
	private   int Bomb;
	private   Graphics[] SCg = new Graphics[2];
	private static Image [] SCbuf = new Image[2];
	private static Image [] CharImage = new Image[348];
	private static Random Rnd = new Random();

	public GCanvas(KikiKaikai st){
		//----------------Splash Screen varibles---------------
		scrW = getWidth();
		scrH = getHeight();
		this.midlet = st;
		gameEffect = midlet.gameEffects;
		try
		{
			imgSplash  = Image.createImage("/taito.png");
			gameIcon = Image.createImage("/icon.png");
			gameGod = Image.createImage("/god.png");
			gameTaito = Image.createImage("/taito_t.png");
			topI = st.top;
			leftI = st.left;
			rightI = st.right;
			Screen_Current= Screen_Splash;
		}
		catch(Exception e){
			System.out.println("err to load image...");
		}

	}
	public void callGameScreen()
	{
		//----------------Game Screen Varibles------------------
		High_Score= midlet.getHighScore();
		going_count = 0;
		gameEffect = midlet.gameEffects;
		gameEffect.resume();
	}

	public void update(){
		if(true){
			if(Scene == 200 ){
			loading_Percent = 0;
			}
			else if(Scene == 300 ){
				Boss[0 ]		= Worldxpos;
				Boss[1 ]		= Worldypos;
				Boss[4 ]		= Worldxpos;
				Boss[5 ]		= Worldypos;
				playSoundTrack(SOUND_4); //Ph[4]
				Scene = 310 ;
			}
			else if(Scene == 340 ){
				myCount++;
				if(myCount>=90)
				{
					myCount=0;
					isOperation = false;
					Scene = 720 ;
					destroyImage(Map_char_num[Round_no]);//change by haitao
					destroyImage(Boss_char_num[Round_no]);//change by haitao
					destroyImage(Zako_char_num[Round_no]);//change by haitao
					drawGate();
					Round_no++;
					if(Round_no >= 7){
						Round_no = 0;
						Scene = 750 ;
						return;
					}
					else if(Clear_Scene <= Round_no){
						Clear_Scene = Round_no;
					}
					recordJob(true);
				}

			}
			else if(Scene == 60 ){
				if(Seven_Gods_pos <= -104)
				{
					map_data_read();
					Worldxpos = Wxp[Round_no];
					Worldypos = Wyp[Round_no];
					map_repaint();
					gameInit();
					gameReInit();
					zakoInit();
					drawGate();
					Key_flg = 0;
					Bakeuri_count = 0;
					Zako_limit_timer = 0;
					Scene = 200 ;//change by haitao
					isOperation = true;
				}
			}
			else if(Scene == 80 ){
				if(Seven_Gods_pos >= 0)
				{
					Scene = 90 ;//change by haitao
					myCount2=0;
				}
			}
			else if(Scene == 100 ){
			}
			else if(Scene == 410 ){
				if((Sayo[5]&0xFF) == 0){
					Scene = 420 ;
					System.gc();
				}
				else{
					Scene = 600 ;
				}
			}
		}
	}

	private void recordJob(boolean writeflags){
		if(High_Score < Score){
			High_Score = Score;
		}
		int temp = midlet.getHighMissionNum();
		if(Round_no+1 > temp)
		midlet.setHighMissionNum(Round_no+1);
		midlet.setHighScore(High_Score);
	}

	private void destroyImage(short char_image_no[]){
	  for(int i=0; i<char_image_no.length; i++){
			CharImage[char_image_no[i]] = null;
		}
		System.gc();
	}

	private   void gameInit(){
		Sayo[0] = Worldxpos+(((120 >>1)-12)<<16);
		Sayo[1] = Worldypos+(((128 >>1)-12)<<16);
		Seven_Gods_pos = ((120 >>1)<<8)+(128 >>1);
		for(int i=0; i<Tourou_pos[Round_no].length; i++){
			Tourou_pos[Round_no][i] &= 0x3FFFFFFF;
		}
		for(int i=0; i<Kakushi_pos[Round_no].length; i++){
			Kakushi_pos[Round_no][i] &= 0x3FFFFFFF;
		}
	}

	private   void gameReInit(){
		World_timer = 0;
		Sayo[2 ]	= 0 ;
		Sayo[3 ]	= 288 ;
		Sayo[4 ]	= 0;
		Ofuda[4 ][3 ]		= 0;
		for(int i=0; i<4; i++){
			Ofuda[2 ][i]			= 0xFFFFFFFF;
			Ofuda[3 ][i]				= Ofuda[4 ][0 ];
		}
		Oharai = 0xFFFFFFFF;
		OfudaKey = 0xFFFFFFFF;
		Boss[6 ]	= 0;
		Boss[7 ]		= 0x00300030;
		Boss[8 ]		= Boss_hit_area[Round_no];
		Boss[9 ]		= 0;
		Item[0 ] = 0;
		Item[1 ] = 330 ;
		Rumuru_Scroll_quantity = 0;
		Time_stop = 0;
		Bomb = 0;
	}
	private   void zakoInit(){
		for(int i=0; i< 6 ; i++){
			Tama[0 ][i] = 0;
			Tama[1 ][i] = 0;
			Tama[2 ][i] = 0;
			Tama[3 ][i] = 0;
			Tama[4 ][i] = 0;
			Tama[5 ][i] = 0;
			Tama[6 ][i] = 0;
			Tama[7 ][i] = 0;
		}

		for(int i=0; i< 12 ; i++){
			Zako[0 ][i] = 0;
			Zako[1 ][i] = 0;
			Zako[2 ][i] = 0;
			Zako[5 ][i] = 0;
			Zako[6 ][i] = 0;
			Zako[7 ][i] = 0;
			Zako[8 ][i] = 0;
			Zako[9 ][i] = 0;
			Zako[10 ][i] = 0;
		}

	}

	private  void CreateImageWait(String title, String scene, Graphics gg)
	{
		SCg[0].setColor(0x00, 0x00, 0x00);
		SCg[0].fillRect(0, 0, 120 , 128 );
	}

	private void map_data_read(){
		System.gc();
		try{
			DataInputStream din;
			InputStream in = getClass().getResourceAsStream("/map"+(Round_no+1)+".dat");
			din = new DataInputStream(in);
			for(int i=1; i<=(int)((Screen_max_num>>(Round_no<<3))&0x000000000000FF); i++){
				for(int y=0; y<16; y++){
					for(int x=0; x<15; x++){
						MAP[i][y][x] = din.readShort();
					}
				}
			}
			din.close();
		}
		catch(Exception e){
		}
	}
	private void map_repaint(){
		Nowscreen = (WORLDMAP[Round_no][(Worldypos>>16)/ 128 ][(Worldxpos>>16)/ 120 ])>>5;
		for(int y=0; y< 128 ; y+=8){
			for(int x=0; x< 120 ; x+=8){
				SCg[Nowscreen].drawImage(CharImage[(MAP[(WORLDMAP[Round_no][(Worldypos>>16)/ 128 ][(Worldxpos>>16)/ 120 ])&0x1F][y>>3][x>>3]&0x0000007F)], x, y, SCg[Nowscreen].TOP|SCg[Nowscreen].LEFT);
			}
		}
	}

	protected synchronized void drawGate(){
		for(int i=0; i<8; i++){
			short tmp = MAP[(WORLDMAP[Round_no][((Wrold_boss_gate_y>>(Round_no<<2))&0x0000000F)][((Wrold_boss_gate_x>>(Round_no<<2))&0x0000000F)])&0x1F][((Map_boss_gate_y[Round_no]>>(i<<2))&0x0000000F)][((Map_boss_gate_x[Round_no]>>(i<<2))&0x0000000F)];
			MAP[(WORLDMAP[Round_no][((Wrold_boss_gate_y>>(Round_no<<2))&0x0000000F)][((Wrold_boss_gate_x>>(Round_no<<2))&0x0000000F)])&0x1F][((Map_boss_gate_y[Round_no]>>(i<<2))&0x0000000F)][((Map_boss_gate_x[Round_no]>>(i<<2))&0x0000000F)] = (short)Gate_data[Round_no][i];
			if(Scene == 300 ){
				SCg[(WORLDMAP[Round_no][((Wrold_boss_gate_y>>(Round_no<<2))&0x0000000F)][((Wrold_boss_gate_x>>(Round_no<<2))&0x0000000F)])>>5].drawImage(CharImage[(Gate_data[Round_no][i]&0x0000007F)], (((Map_boss_gate_x[Round_no]>>(i<<2))&0x0000000F)<<3), (((Map_boss_gate_y[Round_no]>>(i<<2))&0x0000000F)<<3), SCg[Nowscreen].TOP|SCg[Nowscreen].LEFT);
			}
			Gate_data[Round_no][i] = (short)tmp;
		}
	}


	private void disp_odou(Graphics gg){
		for(int i=0; i<Odou_pos[Round_no].length; i++){
			if(
				(Round_no != 1)
					&&(((Odou_pos[Round_no][i]>>16)+64)>(Worldxpos>>16))
					&&((Odou_pos[Round_no][i]>>16)<((Worldxpos>>16)+ 120 ))
					&&(((Odou_pos[Round_no][i]&0x0000FFFF)+64)>(Worldypos>>16))
					&&((Odou_pos[Round_no][i]&0x0000FFFF)<((Worldypos>>16)+ 128 ))
					){
				if(((Zako_limit_timer&0x0000FF00) == 0)&&((Zako[0 ][4+i]&0x00FF0000) == 0)){
					if((Zako_limit_timer&0x01FF0000) == 0x00000000){
						playSoundTrack(SOUND_15); //Ph[15]
						Zako[0 ][4+i] += 0x00FF0000;
						Zako[7 ][4+i] = 0x00050005;
						Zako[9 ][4+i] = 0;
						Zako[10 ][4+i] = 0x02580000 ;
						if(((Sayo[1 ]>>16)+24)<=(Odou_pos[Round_no][i]&0x0000FFFF)){
							Zako[3 ][4+i] = (Odou_pos[Round_no][i]&0xFFFF0000)+0x00180000;
							Zako[4 ][4+i] = (Odou_pos[Round_no][i]-8)<<16;
							Zako[5 ][4+i] = 190 ;
							Zako[6 ][4+i] = 4;
							Zako[8 ][4+i] = 0x10100000;
						}
						else if((Sayo[0 ]>>16)<=((Odou_pos[Round_no][i]>>16)+20)){

							Zako[3 ][4+i] = (Odou_pos[Round_no][i]&0xFFFF0000)-0x00080000;
							Zako[4 ][4+i] = (Odou_pos[Round_no][i]+8)<<16;
							Zako[5 ][4+i] = 189 ;
							Zako[6 ][4+i] = 2;
							Zako[8 ][4+i] = 0x10100000;
						}
						else if(((Sayo[0 ]>>16)+24)>=((Odou_pos[Round_no][i]>>16)+44)){

							Zako[3 ][4+i] = (Odou_pos[Round_no][i]&0xFFFF0000)+0x00080000;
							Zako[4 ][4+i] = (Odou_pos[Round_no][i]+8)<<16;
							Zako[5 ][4+i] = 189 ;
							Zako[6 ][4+i] = 6;
							Zako[8 ][4+i] = 0x10103000;
						}

						Zako[1 ][4+i] = Zako[3 ][4+i];
						Zako[2 ][4+i] = Zako[4 ][4+i];
					}

					Zako_limit_timer += 0x00010000;
				}
				else if(((Zako[5 ][4+i] == 189 )||(Zako[5 ][4+i] == 190 ))&&((Zako[0 ][4+i]&0x00FF0000) != 0)){
					if(Zako[6 ][4+i] == 4){

						Zako[2 ][4+i] = Zako[4 ][4+i]-(Zako_limit_timer&0x00FF0000);
					}
					else if(Zako[6 ][4+i] == 2){

						Zako[1 ][4+i] = Zako[3 ][4+i]-(Zako_limit_timer&0x00FF0000);
					}
					else if(Zako[6 ][4+i] == 6){

						Zako[1 ][4+i] = Zako[3 ][4+i]+(Zako_limit_timer&0x00FF0000);
					}
					gg.drawImage(CharImage[Zako[5 ][4+i]], (Zako[1 ][4+i]>>16)-(Worldxpos>>16), (Zako[2 ][4+i]>>16)-(Worldypos>>16), gg.TOP|gg.LEFT);
					if((Time_stop == 0)&&((Zako_limit_timer&0x01FF0000) < 0x00200000)){
						Zako_limit_timer += 0x00010000;
						if((Zako_limit_timer&0x00FF0000) == 0x00200000){
							Zako_limit_timer += 0x01000000;
						}
					}
					else if(Time_stop == 0){
						Zako_limit_timer -= 0x00010000;
						if((Zako_limit_timer&0x00FF0000) == 0x00000000){
							Zako_limit_timer &= 0xFEFFFFFF;
							Zako_limit_timer |= 0x00003F00;

							Zako[0 ][4+i] = 0;
						}
					}
				}
				gg.drawImage(CharImage[127 ], (Odou_pos[Round_no][i]>>16)-(Worldxpos>>16), (Odou_pos[Round_no][i]&0x0000FFFF)-(Worldypos>>16), gg.TOP|gg.LEFT);
				if((Zako_limit_timer&0x0000FF00) != 0){
					Zako_limit_timer -= 0x00000100;
				}
			}
			else if(((Zako[5 ][4+i] == 189 )||(Zako[5 ][4+i] == 190 ))&&((Zako[0 ][4+i]&0x00FF0000) != 0)){
				Zako[0 ][4+i] = 0;
			}
		}
	}


	private void disp_torii(Graphics gg){
		if(
			(((Torii_pos[Round_no]>>16)+48)>(Worldxpos>>16))
				&&((Torii_pos[Round_no]>>16)<((Worldxpos>>16)+ 120 ))
				&&(((Torii_pos[Round_no]&0x0000FFFF)+24)>(Worldypos>>16))
				&&((Torii_pos[Round_no]&0x0000FFFF)<((Worldypos>>16)+ 128 ))
				){
			gg.drawImage(CharImage[128 ], (Torii_pos[Round_no]>>16)-(Worldxpos>>16), (Torii_pos[Round_no]&0x0000FFFF)-(Worldypos>>16), gg.TOP|gg.LEFT);
		}
	}


	private void disp_key(Graphics gg){
		if(
			(Key_flg == 0)
				&&(((Key_pos[Round_no]>>16)+16)>(Worldxpos>>16))
				&&((Key_pos[Round_no]>>16)<((Worldxpos>>16)+ 120 ))
				&&(((Key_pos[Round_no]&0x0000FFFF)+16)>(Worldypos>>16))
				&&((Key_pos[Round_no]&0x0000FFFF)<((Worldypos>>16)+ 128 ))
				){
			gg.drawImage(CharImage[329 ], (Key_pos[Round_no]>>16)-(Worldxpos>>16), (Key_pos[Round_no]&0x0000FFFF)-(Worldypos>>16), gg.TOP|gg.LEFT);
		}
	}


	private void disp_tourou(Graphics gg){
		for(int i=0; i<Tourou_pos[Round_no].length; i++){
			if(
				((((Tourou_pos[Round_no][i]&0x3FFFFFFF)>>16)+16)>(Worldxpos>>16))
					&&(((Tourou_pos[Round_no][i]&0x3FFFFFFF)>>16)<((Worldxpos>>16)+ 120 ))
					&&(((Tourou_pos[Round_no][i]&0x0000FFFF)+16)>(Worldypos>>16))
					&&((Tourou_pos[Round_no][i]&0x0000FFFF)<((Worldypos>>16)+ 128 ))
					){
				if((Tourou_pos[Round_no][i]>>30) == 0){
					gg.drawImage(CharImage[330 ], (Tourou_pos[Round_no][i]>>16)-(Worldxpos>>16), (Tourou_pos[Round_no][i]&0x0000FFFF)-(Worldypos>>16), gg.TOP|gg.LEFT);
				}
			}
		}
	}

	private void disp_kakushi(Graphics gg){
		for(int i=0; i<Kakushi_pos[Round_no].length; i++){
			if(
				((Kakushi_pos[Round_no][i]>>30) == 1)
					&&((((Kakushi_pos[Round_no][i]&0x00FFF000)>>12)+16)>(Worldxpos>>16))
					&&(((Kakushi_pos[Round_no][i]&0x00FFF000)>>12)<((Worldxpos>>16)+ 120 ))
					&&(((Kakushi_pos[Round_no][i]&0x00000FFF)+16)>(Worldypos>>16))
					&&((Kakushi_pos[Round_no][i]&0x00000FFF)<((Worldypos>>16)+ 128 ))
					){
				gg.drawImage(CharImage[Item[1 ]], ((Item[0 ]&0x3FFFFFFF)>>16)-(Worldxpos>>16), (Item[0 ]&0x0000FFFF)-(Worldypos>>16), gg.TOP|gg.LEFT);
			}
		}
	}

	private void disp_seven_gods(Graphics gg){
		for(int i=0; i<7; i++){
			gg.drawImage(CharImage[277 -i], (120 >>1)+Seven_Gods_offset_posx[i], Seven_Gods_offset_posy[i], gg.TOP|gg.LEFT);
		}
	}

	private void disp_seven_gods_escape(Graphics gg){
		bresenhamLine((Seven_Gods_pos&0x0000FF00)<<8, (Seven_Gods_pos&0x000000FF)<<16, Seven_Gods_escape_posx[Seven_Gods_pos>>16]<<16, Seven_Gods_escape_posy[Seven_Gods_pos>>16]<<16, 3);
		Seven_Gods_pos &= 0x001F0000;
		Seven_Gods_pos |= (Bresenham_tmp[0]>>16)<<8;
		Seven_Gods_pos |= Bresenham_tmp[1]>>16;
		if((((Seven_Gods_pos&0x0000FF00)>>8) == Seven_Gods_escape_posx[Seven_Gods_pos>>16])&&((Seven_Gods_pos&0x000000FF) == Seven_Gods_escape_posy[Seven_Gods_pos>>16])){
			if((Seven_Gods_pos&0xFFFF0000) < 0x00110000){
				Seven_Gods_pos += 0x00010000;
			}
		}
		gg.drawImage(CharImage[271 +Round_no], (Seven_Gods_pos&0x0000FF00)>>8, (Seven_Gods_pos&0x000000FF), gg.BOTTOM|gg.HCENTER);
	}

	private void drawOfudaOharai(Graphics gg){
		if((((AllKeyCode&0x00010000) != 0)||(((Auto_Rennsya_flg>>4) == 0x07FFFFFF)&&(Oharai == 0xFFFFFFFF)))&&(OfudaKey == 0xFFFFFFFF)){
			for(int i=0; i< 4 ; i++){
				if(Ofuda[2 ][i] == 0xFFFFFFFF){
					Ofuda[2 ][i] = Sayo[2 ];
					Ofuda[0 ][i] = Sayo[0 ] + ((Ofuda_offset[Ofuda[2 ][i]+2] - ((Ofuda[4 ][2 ]&0x00000002)<<1))<<16);
					Ofuda[1 ][i] = Sayo[1 ] + ((Ofuda_offset[Ofuda[2 ][i]] - ((Ofuda[4 ][2 ]&0x00000002)<<1))<<16);
					if(OfudaKey == 0xFFFFFFFF){
						OfudaKey = 0;
					}
					playSoundTrack(SOUND_12); //Ph[12]
					break;
				}
			}
			if((Auto_Rennsya_flg>>4) != 0x07FFFFFF){
				Oharai = 0xFFFFFFFF;
			}
		}
		else if((AllKeyCode & 0x00000001) != 0){//used for magic wand
			if(Oharai == 0xFFFFFFFF){
				Oharai = 0;
			}
		}
		if(OfudaKey != 0xFFFFFFFF){
			OfudaKey++;
		}
		if(OfudaKey == 4){
			OfudaKey = 0xFFFFFFFF;
		}
		for(int i=0; i< 4 ; i++){
			if(Ofuda[2 ][i] != 0xFFFFFFFF){
				Ofuda[0 ][i] += Ofuda_direction_offset[Ofuda[2 ][i]+2]*Ofuda[4 ][1 ];
				Ofuda[1 ][i] += Ofuda_direction_offset[Ofuda[2 ][i]]*Ofuda[4 ][1 ];
				gg.drawImage(CharImage[321  + Ofuda[4 ][3 ] + ((Ofuda[4 ][2 ]&0x00000002)<<1)],
					(Ofuda[0 ][i]-Worldxpos)>>16,
					(Ofuda[1 ][i]-Worldypos)>>16,
					gg.TOP|gg.LEFT
					);
				for(int j=0; j<Kakushi_pos[Round_no].length; j++){
					if(
						((Kakushi_pos[Round_no][j]>>30) == 0)
							&&(((Ofuda[0 ][i]>>16)+((Ofuda[4 ][2 ]&0x00000003)<<3)-1) > ((Kakushi_pos[Round_no][j]&0x00FFF000)>>12))
							&&((Ofuda[0 ][i]>>16) < (((Kakushi_pos[Round_no][j]&0x00FFF000)>>12)+15))
							&&(((Ofuda[1 ][i]>>16)+((Ofuda[4 ][2 ]&0x00000003)<<3)-1) > (Kakushi_pos[Round_no][j]&0x00000FFF))
							&&((Ofuda[1 ][i]>>16) < ((Kakushi_pos[Round_no][j]&0x00000FFF)+15))
							){
						Kemuri[0 ][i] = Ofuda[0 ][i]-(((Ofuda[4 ][2 ]&0x00000003)^0x3)<<18);
						Kemuri[1 ][i] = Ofuda[1 ][i]-(((Ofuda[4 ][2 ]&0x00000003)^0x3)<<18);
						Kemuri[2 ][i] = 0x0000000F;
						playSoundTrack(SOUND_13); //Ph[13]
						Item[0 ] = ((Kakushi_pos[Round_no][j]&0x00FFF000)<<4)+(Kakushi_pos[Round_no][j]&0x00000FFF);
						Item[1 ] = ((Kakushi_pos[Round_no][j]&0x0F000000)>>24)+ 331 ;
						Kakushi_pos[Round_no][j] |= 0x40000000;
					}
				}
				for(int j=0; j< 12 ; j++){
					if(
						((Zako[0 ][j]&0x00FFFF00) != 0)
							&&(((Ofuda[0 ][i]>>16)+((Ofuda[4 ][2 ]&0x00000003)<<3)-1) > ((Zako[1 ][j]>>16)+((Zako[8 ][j]&0x0000FF00)>>8)))
							&&((Ofuda[0 ][i]>>16) < ((Zako[1 ][j]>>16)+((Zako[8 ][j]&0x0000FF00)>>8)+((Zako[8 ][j]&0xFF000000)>>24)-1))
							&&(((Ofuda[1 ][i]>>16)+((Ofuda[4 ][2 ]&0x00000003)<<3)-1) > ((Zako[2 ][j]>>16)+(Zako[8 ][j]&0x000000FF)))
							&&((Ofuda[1 ][i]>>16) < ((Zako[2 ][j]>>16)+(Zako[8 ][j]&0x000000FF)+((Zako[8 ][j]&0x00FF0000)>>16)-1))
							){

						if(((Zako[9 ][j]&0x0000FF00) != 0)&&(Zako[5 ][j] == 151 )){
							continue;
						}
						Kemuri[0 ][i] = Ofuda[0 ][i]-(((Ofuda[4 ][2 ]&0x00000003)^0x3)<<18);
						Kemuri[1 ][i] = Ofuda[1 ][i]-(((Ofuda[4 ][2 ]&0x00000003)^0x3)<<18);
						Kemuri[2 ][i] = 0x0000000F;

						Zako[7 ][j]--;
						if(Zako[5 ][j] == 135 ){
							Percent++;
							if(Percent == 3){
								Item[0 ] = (Zako[1 ][j]&0xFFFF0000)+(Zako[2 ][j]>>16);
								setItem();
							}
						}
						else if(Zako[5 ][j] == 339 ){
							Item[0 ] = (Zako[1 ][j]&0xFFFF0000)+(Zako[2 ][j]>>16);
							Item[1 ] = 331 ;
						}
						else if(Zako[5 ][j] == 340 ){
							Item[0 ] = (Zako[1 ][j]&0xFFFF0000)+(Zako[2 ][j]>>16);
							Item[1 ] = 332 ;
						}
						else if(Zako[5 ][j] == 341 ){
							Item[0 ] = (Zako[1 ][j]&0xFFFF0000)+(Zako[2 ][j]>>16);
							Item[1 ] = 334 ;
						}
						else if(Zako[5 ][j] == 342 ){
							Item[0 ] = (Zako[1 ][j]&0xFFFF0000)+(Zako[2 ][j]>>16);
							Item[1 ] = 333 ;
						}
						else if(Zako[5 ][j] == 343 ){
							Item[0 ] = (Zako[1 ][j]&0xFFFF0000)+(Zako[2 ][j]>>16);
							Item[1 ] = 338 ;
						}
						if((Zako[7 ][j]&0x0000FFFF) == 0){
							Score += Zako[10 ][j]>>16;
							if (Score>999999) Score=999999;
							Zako[0 ][j] = 0;
							Zako[1 ][j] = 0;
							Zako[2 ][j] = 0;
							Zako[5 ][j] = 0;
							Zako[6 ][j] = 0;
							Zako[7 ][j] = 0;
							Zako[8 ][j] = 0;
							Zako[9 ][j] = 0;
							Zako[10 ][j] = 0;
						}
						playSoundTrack(SOUND_13); //Ph[13]
						if((Ofuda[4 ][2 ]&0x00000008) == 0){
							Ofuda[3 ][i] = 1;
							break;
						}
					}
				}
				if((Round_no == 5)&&((Boss[9 ]&0xFF000000) < 0x2F000000)){
				}
				else if(
					(Scene == 320 )
						&&(((Ofuda[0 ][i]>>16)+((Ofuda[4 ][2 ]&0x00000003)<<3)-1) > ((Boss[0 ]>>16)+((Boss[8 ]&0x0000FF00)>>8)))
						&&((Ofuda[0 ][i]>>16) < ((Boss[0 ]>>16)+((Boss[8 ]&0x0000FF00)>>8)+((Boss[8 ]&0xFF000000)>>24)-1))
						&&(((Ofuda[1 ][i]>>16)+((Ofuda[4 ][2 ]&0x00000003)<<3)-1) > ((Boss[1 ]>>16)+(Boss[8 ]&0x000000FF)))
						&&((Ofuda[1 ][i]>>16) < ((Boss[1 ]>>16)+(Boss[8 ]&0x000000FF)+((Boss[8 ]&0x00FF0000)>>16)-1))
						){
					Ofuda[3 ][i] = 1;
					Boss[7 ]--;
					if(((Boss[7 ]&0x0000FFFF)%12) == 0){
						SCg[(WORLDMAP[Round_no][((Wrold_boss_gate_y>>(Round_no<<2))&0x0000000F)][((Wrold_boss_gate_x>>(Round_no<<2))&0x0000000F)])>>5].drawImage(
							CharImage[40 ],
							40+(((((Boss[7 ]&0x0000FFFF)/(Boss[7 ]>>18))^0x11)&0x01)<<4),
							56+((((((Boss[7 ]&0x0000FFFF)/(Boss[7 ]>>18))^0x11)&0x02)^0x02)<<2),
							SCg[Nowscreen].TOP|SCg[Nowscreen].LEFT
							);
						SCg[(WORLDMAP[Round_no][((Wrold_boss_gate_y>>(Round_no<<2))&0x0000000F)][((Wrold_boss_gate_x>>(Round_no<<2))&0x0000000F)])>>5].drawImage(
							CharImage[40 ],
							40+8+(((((Boss[7 ]&0x0000FFFF)/(Boss[7 ]>>18))^0x11)&0x01)<<4),
							56+((((((Boss[7 ]&0x0000FFFF)/(Boss[7 ]>>18))^0x11)&0x02)^0x02)<<2),
							SCg[Nowscreen].TOP|SCg[Nowscreen].LEFT
							);
					}
					if((Boss[7 ]&0x0000FFFF) == 0){
						for(int j=0; j< 4 ; j++){
							Kemuri[0 ][j] = Boss[0 ]+(((j&0x01)<<5)<<16);
							Kemuri[1 ][j] = Boss[1 ]+(((j&0x02)<<3)<<16);
							Kemuri[2 ][j] = 0x0000000F;
						}
						Score += 5000;
						if (Score>999999) Score=999999;
						Scene = 330 ;
					}
					else{
						Kemuri[0 ][i] = Ofuda[0 ][i]-(((Ofuda[4 ][2 ]&0x00000003)^0x3)<<18);
						Kemuri[1 ][i] = Ofuda[1 ][i]-(((Ofuda[4 ][2 ]&0x00000003)^0x3)<<18);
						Kemuri[2 ][i] = 0x0000000F;
					}
					playSoundTrack(SOUND_13); //Ph[13]
				}
				Ofuda[3 ][i]--;
				if(Ofuda[3 ][i] <= 0){
					Ofuda[2 ][i]	= 0xFFFFFFFF;
					Ofuda[3 ][i]		= Ofuda[4 ][0 ];
				}
			}
		}
		Ofuda[4 ][3 ]++;
		Ofuda[4 ][3 ] &= 0x00000003;
		if(Oharai != 0xFFFFFFFF){
			gg.drawImage(CharImage[305  + ((Oharai_direction_offset[Sayo[2 ]]>>((Oharai>>1)<<2))&0x0000000F)],
				((Sayo[0 ]-Worldxpos)>>16)+(((int)(Oharai_offset_x[Sayo[2 ]]>>((Oharai>>1)<<3))&0xFF)-10),
				((Sayo[1 ]-Worldypos)>>16)+(((int)(Oharai_offset_y[Sayo[2 ]]>>((Oharai>>1)<<3))&0xFF)-5),
				gg.TOP|gg.LEFT
				);
			for(int j=0; j< 12 ; j++){
				if(
					((Zako[0 ][j]&0x00FFFF00) != 0)
						&&((Zako[5 ][j] < 183 )||(Zako[5 ][j] >= 255 ))
						&&(((Sayo[0 ]>>16)+((((int)(Oharai_offset_x[Sayo[2 ]]>>((Oharai>>1)<<3))&0xFF)-10)+15)) > ((Zako[1 ][j]>>16)+((Zako[8 ][j]&0x0000FF00)>>8)))
						&&(((Sayo[0 ]>>16)+(((int)(Oharai_offset_x[Sayo[2 ]]>>((Oharai>>1)<<3))&0xFF)-10)) < ((Zako[1 ][j]>>16)+((Zako[8 ][j]&0x0000FF00)>>8)+((Zako[8 ][j]&0xFF000000)>>24)-1))
						&&(((Sayo[1 ]>>16)+((((int)(Oharai_offset_y[Sayo[2 ]]>>((Oharai>>1)<<3))&0xFF)- 5)+15)) > ((Zako[2 ][j]>>16)+(Zako[8 ][j]&0x000000FF)))
						&&(((Sayo[1 ]>>16)+(((int)(Oharai_offset_y[Sayo[2 ]]>>((Oharai>>1)<<3))&0xFF)- 5)) < ((Zako[2 ][j]>>16)+(Zako[8 ][j]&0x000000FF)+((Zako[8 ][j]&0x00FF0000)>>16)-1))
						){
					if(((Zako[9 ][j]&0x0000FF00) != 0)&&(Zako[5 ][j] == 151 )){
						continue;
					}
					if(Zako[5 ][j] == 135 ){
						Percent+=3;
						if(Percent == 9){
							Item[0 ] = (Zako[1 ][j]&0xFFFF0000)+(Zako[2 ][j]>>16);
							setItem();
						}
					}
					Score += Zako[10 ][j]&0xFFFF;
					if (Score>999999) Score=999999;
					Zako[0 ][j] = (((Oharai_direction_offset[Sayo[2 ]]>>((Oharai>>1)<<2))&0x0000000F)%8)+1;
					playSoundTrack(SOUND_14); //Ph[14]
				}
			}
			for(int j=0; j<Tourou_pos[Round_no].length; j++){
				if(
					((Tourou_pos[Round_no][j]>>30) == 0)
						&&(((Sayo[0 ]>>16)+((((int)(Oharai_offset_x[Sayo[2 ]]>>((Oharai>>1)<<3))&0xFF)-10)+15)) > (Tourou_pos[Round_no][j]>>16))
						&&(((Sayo[0 ]>>16)+(((int)(Oharai_offset_x[Sayo[2 ]]>>((Oharai>>1)<<3))&0xFF)-10)) < ((Tourou_pos[Round_no][j]>>16)+3))
						&&(((Sayo[1 ]>>16)+((((int)(Oharai_offset_y[Sayo[2 ]]>>((Oharai>>1)<<3))&0xFF)- 5)+15)) > (Tourou_pos[Round_no][j]&0x0000FFFF))
						&&(((Sayo[1 ]>>16)+(((int)(Oharai_offset_y[Sayo[2 ]]>>((Oharai>>1)<<3))&0xFF)- 5)) < ((Tourou_pos[Round_no][j]&0x0000FFFF)+2))
						){
					playSoundTrack(SOUND_13); //Ph[13]
					Item[0 ] = (Tourou_pos[Round_no][j]-0x00060006);
					setItem();
					Tourou_pos[Round_no][j] |= 0x40000000;
				}
			}
			if(Oharai == 0){
				playSoundTrack(SOUND_16); //Ph[16]
			}
			Oharai++;
			if(Oharai == 12){
				Oharai = 0xFFFFFFFF;
			}
		}
	}

	public void setItem(){
		int kakuritsu = (Math.abs(Rnd.nextInt())%100);
		if(kakuritsu < 70){
			Item[1 ] = 331 ;
		}
		else if(kakuritsu < 80){
			Item[1 ] = 337 ;
		}
		else if(kakuritsu < 86){
			Item[1 ] = 332 ;
		}
		else if(kakuritsu < 92){
			Item[1 ] = 334 ;
		}
		else if(kakuritsu < 94){
			Item[1 ] = 333 ;
		}
		else if(kakuritsu < 96){
			Item[1 ] = 336 ;
		}
		else if(kakuritsu < 98){
			Item[1 ] = 335 ;
		}
		else if(kakuritsu < 100){
			Item[1 ] = 338 ;
		}
	}

	public void jumpZako(Graphics gg, int no){
		Zako[1 ][no] += Jump_offsetx[Zako[6 ][no]>>1][Zako[9 ][no]&0x0000FFFF]<<16;
		Zako[2 ][no] += Jump_offsety[Zako[6 ][no]>>1][Zako[9 ][no]&0x0000FFFF]<<16;

		if((((Zako[2 ][no]-Worldypos)>>16) < -16)||(((Zako[2 ][no]-Worldypos)>>16) >= 146)||(((Zako[1 ][no]-Worldxpos)>>16) < -16)||(((Zako[1 ][no]-Worldxpos)>>16) >= 136)){
			Zako[0 ][no] = 0;
		}
		gg.drawImage(CharImage[Zako[5 ][no]+Zako[6 ][no]], (Zako[1 ][no]-Worldxpos)>>16, (Zako[2 ][no]-Worldypos)>>16, gg.TOP|gg.LEFT);

		if((Zako[9 ][no]&0x0000FFFF) == 20){
			Zako[9 ][no] &= 0xFFFF0000;
			Zako[0 ][no] = 0;
			Zako[0 ][no] += 0x00FF0000;
		}
	}

	public void tobiZako(Graphics gg, int no){
		Zako[1 ][no] += Tobi_offset[(Zako[0 ][no]&0x000000FF)+1]<<16;
		Zako[2 ][no] += Tobi_offset[(Zako[0 ][no]&0x000000FF)-1]<<16;
		if((((Zako[2 ][no]-Worldypos)>>16) < -16)||(((Zako[2 ][no]-Worldypos)>>16) >= 146)||(((Zako[1 ][no]-Worldxpos)>>16) < -16)||(((Zako[1 ][no]-Worldxpos)>>16) >= 136)){
			Zako[0 ][no] = 0;
		}
		gg.drawImage(CharImage[Zako[5 ][no]], (Zako[1 ][no]-Worldxpos)>>16, (Zako[2 ][no]-Worldypos)>>16, gg.TOP|gg.LEFT);
	}

	private void drawSmoke(Graphics gg){
		for(int i=0; i< 4 ; i++){
			if(Kemuri[2 ][i] != 0){
				if(Kemuri[2 ][i] > 7){
					gg.drawImage(CharImage[345 ], (Kemuri[0 ][i]-Worldxpos)>>16, (Kemuri[1 ][i]-Worldypos)>>16, gg.TOP|gg.LEFT);
				}
				else{
					gg.drawImage(CharImage[344 ], (Kemuri[0 ][i]-Worldxpos)>>16, (Kemuri[1 ][i]-Worldypos)>>16, gg.TOP|gg.LEFT);
				}
				Kemuri[2 ][i]--;
				if((Scene == 330 )&&(Kemuri[2 ][3] == 0)){
					Scene = 340 ;
					World_timer = 0;
					Auto_Rennsya_flg = 0 ;
					playSoundTrack(SOUND_9); //Ph[9]
				}
			}
		}
	}

	private void drawBoss(Graphics gg){
		if(Round_no == 0){
			if((Boss[9 ]&0x003F0000) == 0x00300000){
				Boss[9 ] &= 0xFF00FFFF;
			}
			gg.drawImage(CharImage[200 ], (Boss[0 ]-Worldxpos)>>16, (Boss[1 ]-Worldypos)>>16, gg.TOP|gg.LEFT);
			gg.drawImage(CharImage[201 +((Boss[9 ]&0x0000000F)>>3)], (Boss[0 ]-Worldxpos)>>16, ((Boss[1 ]-Worldypos)>>16)+24, gg.TOP|gg.LEFT);
			gg.drawImage(CharImage[203 +((Boss[9 ]&0x0000000F)>>3)], ((Boss[0 ]-Worldxpos)>>16)+40, ((Boss[1 ]-Worldypos)>>16)+24, gg.TOP|gg.LEFT);
			gg.drawImage(CharImage[205 +((Boss[9 ]&0x00300000)>>20)], ((Boss[0 ]-Worldxpos)>>16)+Zuzu_offset_x[((Boss[9 ]&0x003F0000)>>20)], ((Boss[1 ]-Worldypos)>>16)+Zuzu_offset_y[((Boss[9 ]&0x003F0000)>>20)], gg.TOP|gg.LEFT);
			if(Scene == 320 ){
				Boss[9] += 0x00010001;
				Boss[9] &= 0x003FFF0F;
				bossMove();
				bossTamaMove(gg);
			}
		}
		else if(Round_no == 1){
			gg.drawImage(CharImage[209 ], (Boss[0 ]-Worldxpos)>>16, (Boss[1 ]-Worldypos)>>16, gg.TOP|gg.LEFT);
			gg.drawImage(CharImage[210 +((Boss[9 ]&0x00200000)>>21)], ((Boss[0 ]-Worldxpos)>>16)+Raidenou_offset_x[((Boss[9 ]&0x003F0000)>>21)], ((Boss[1 ]-Worldypos)>>16)+18, gg.TOP|gg.LEFT);
			if(Scene == 320 ){
				Boss[9 ] += 0x00010001;
				Boss[9 ] &= 0x003FFF0F;
				bossMove();
				bossTamaMove(gg);
			}
		}
		else if(Round_no == 2){
			gg.drawImage(CharImage[213 ], ((Boss[0 ]-Worldxpos)>>16)+16, (Boss[1 ]-Worldypos)>>16, gg.TOP|gg.LEFT);
			gg.drawImage(CharImage[214 +((Boss[9 ]&0x0000000F)>>3)], ((Boss[0 ]-Worldxpos)>>16)+16, ((Boss[1 ]-Worldypos)>>16)+16, gg.TOP|gg.LEFT);
			if((Tama[0 ][0]&0x0F200000) == 0x00200000){
				gg.drawImage(CharImage[216 +((Boss[9 ]&0x00200000)>>21)], ((Boss[0 ]-Worldxpos)>>16)+(((Boss[9 ]-Worldxpos)&0x00200000)>>18), ((Boss[1 ]-Worldypos)>>16)+8, gg.TOP|gg.LEFT);
			}
			else{
				gg.drawImage(CharImage[216 ], (Boss[0 ]-Worldxpos)>>16, ((Boss[1 ]-Worldypos)>>16)+8, gg.TOP|gg.LEFT);
			}
			if((Tama[0 ][0]&0x0F200000) == 0x02200000){
				gg.drawImage(CharImage[218 +((Boss[9 ]&0x00200000)>>21)], ((Boss[0 ]-Worldxpos)>>16)+32, ((Boss[1 ]-Worldypos)>>16)+8, gg.TOP|gg.LEFT);
			}
			else{
				gg.drawImage(CharImage[218 ], ((Boss[0 ]-Worldxpos)>>16)+32, ((Boss[1 ]-Worldypos)>>16)+8, gg.TOP|gg.LEFT);
			}

			if(Scene == 320 ){
				Boss[9 ] += 0x00010001;
				Boss[9 ] &= 0x003FFF0F;
				if((Boss[9 ]&0x00200000) != 0x00200000){
					bossMove();
				}
				bossTamaMove(gg);
			}
		}
		else if(Round_no == 3){
			gg.drawImage(CharImage[229 ], (Boss[0 ]-Worldxpos)>>16, (Boss[1 ]-Worldypos)>>16, gg.TOP|gg.LEFT);
			gg.drawImage(CharImage[230 +((Boss[9 ]&0x0000000F)>>3)], (Boss[0 ]-Worldxpos)>>16, ((Boss[1 ]-Worldypos)>>16)+24, gg.TOP|gg.LEFT);
			if(Scene == 320 ){
				Boss[9 ] += 0x00010001;
				Boss[9 ] &= 0xFF1FFF0F;
				bossMove();
				bossTamaMove(gg);
			}
		}
		else if(Round_no == 4){
			gg.drawImage(CharImage[236 +((Boss[9 ]&0x0000000F)>>3)], (Boss[0 ]-Worldxpos)>>16, ((Boss[1 ]-Worldypos)>>16)+24, gg.TOP|gg.LEFT);
			gg.drawImage(CharImage[233 ], (Boss[0 ]-Worldxpos)>>16, (Boss[1 ]-Worldypos)>>16, gg.TOP|gg.LEFT);
			gg.drawImage(CharImage[234 +((Boss[9 ]&0x40000000)>>30)], ((Boss[0 ]-Worldxpos+0xFFFE0000)>>16)-((Boss[9 ]&0x40000000)>>30), ((Boss[1 ]-Worldypos)>>16)+12+((Boss[9 ]&0x40000000)>>29), gg.TOP|gg.LEFT);

			if(((Boss[9 ]&0x000F0000) == 0x00000000)&&((Boss[9 ]&0x40000000) != 0x40000000)){
				Tama[3 ][0] = Tama[1 ][0];
				Tama[4 ][0] = Tama[2 ][0];
				Tama[1 ][0] = Boss[2 ];
				Tama[2 ][0] = Boss[3 ];
			}

			for(int i=0; i< 6 ; i++){
				if((i>0)&&((Boss[9 ]&0x000F0000) == 0x00000000)&&((Boss[9 ]&0xFF000000) <= 0x58000000)){
					Tama[3 ][i] = Tama[1 ][i];
					Tama[4 ][i] = Tama[2 ][i];
					Tama[1 ][i] = Tama[3 ][i-1];
					Tama[2 ][i] = Tama[4 ][i-1];
				}

				gg.drawImage(CharImage[238 ], (Tama[1 ][i]-Worldxpos)>>16, ((Tama[2 ][i]-Worldypos)>>16)+12+((Boss[9 ]&0x40000000)>>28), gg.TOP|gg.LEFT);
				if(
					1 == to_sayo_Hit(Tama[1 ][i],Tama[1 ][i]+0x00070000,Tama[2 ][i]+0x000C0000+(((Boss[9 ]&0x40000000)>>28)<<16),Tama[2 ][i]+0x000C0000+(((Boss[9 ]&0x40000000)>>28)<<16)+0x00060000,0)
						){
					break;
				}
			}
			if(Scene == 320 ){
				Boss[9 ] += 0x01010001;
				Boss[9 ] &= 0x7F03FF0F;
				if((Boss[9 ]&0x40000000) != 0x40000000){
					bossMove();
				}
				else{
					bossTamaMove(gg);
				}
			}
		}
		else if(Round_no == 5){
			if(Scene == 320 ){
				Boss[9 ] += 0x01000000;
				Boss[9 ] &= 0x7FFFFFFF;
				if((Boss[9 ]&0xFF000000) == 0x2F000000){
					int tmpx = (Sayo[0 ]-Boss[0 ])>>16;
					int tmpy = (Sayo[1 ]-Boss[1 ])>>16;
					if(((tmpx > -12)&&(tmpx < 12))&&(tmpy > 0)){
						Boss[6 ] = 0 ;
					}
					else if((tmpx < 0)&&(tmpy > 0)){
						Boss[6 ] = 1 ;
					}
					else if(((tmpy > -12)&&(tmpy < 12))&&(tmpx < 0)){
						Boss[6 ] = 2 ;
					}
					else if((tmpx < 0)&&(tmpy < 0)){
						Boss[6 ] = 3 ;
					}
					else if(((tmpx > -12)&&(tmpx < 12))&&(tmpy < 0)){
						Boss[6 ] = 4 ;
					}
					else if((tmpx > 0)&&(tmpy < 0)){
						Boss[6 ] = 5 ;
					}
					else if(((tmpy > -12)&&(tmpy < 12))&&(tmpx > 0)){
						Boss[6 ] = 6 ;
					}
					else if((tmpx > 0)&&(tmpy > 0)){
						Boss[6 ] = 7 ;
					}
					playSoundTrack(SOUND_20); //Ph[20]
				}
				else if((Boss[9 ]&0xFF000000) == 0x3F000000){
					Boss[9 ] += 0x00000100;
				}
				else if((Boss[9 ]&0xFF000000) == 0x4F000000){

					Boss[0 ] += (Mabikon_add_offset_x[Boss[6 ]]<<16) /4;
					Boss[1 ] += (Mabikon_add_offset_y[Boss[6 ]]<<16) /4;

					if(((Boss[0 ]-Worldxpos)>>16) < 0){
						Boss[0 ] = Worldxpos;
					}
					if(((Boss[0 ]-Worldxpos)>>16) > (120 -40)){
						Boss[0 ] = ((120 -40)<<16)+Worldxpos;
					}
					if(((Boss[1 ]-Worldypos)>>16) < 0){
						Boss[1 ] = Worldypos;
					}
					if(((Boss[1 ]-Worldypos)>>16) > (128 -24)){
						Boss[1 ] = ((128 -24)<<16)+Worldypos;
					}

					Boss[9 ] -= 0x00000100;
				}
				else if((Boss[9 ]&0xFF000000) == 0x00000000){
					Boss[6 ] = 0;
				}
				bossTamaMove(gg);
			}
			gg.drawImage(CharImage[239 +(Boss[6 ]<<1)+((Boss[9 ]&0x00000100)>>8)], ((Boss[0 ]-Worldxpos)>>16)+Mabikon_offset_x[(Boss[6 ]<<1)+((Boss[9 ]&0x00000100)>>8)], ((Boss[1 ]-Worldypos)>>16)+Mabikon_offset_y[(Boss[6 ]<<1)+((Boss[9 ]&0x00000100)>>8)], gg.TOP|gg.LEFT);

		}
		else if(Round_no == 6){
			int boss_etc = 263 ;
			if(Scene == 320 ){
				Boss[9 ] += 0x01010000;
				Boss[9 ] &= 0x7F3FFFFF;

				if((Boss[9 ]&0x00200000) != 0x00200000){
					bresenhamLine(Boss[0 ], Boss[1 ], Boss[4 ], Boss[5 ], 0);
					Boss[0 ] = Bresenham_tmp[0];
					Boss[1 ] = Bresenham_tmp[1];
				}

				if(((Boss[0 ]>>16) == (Boss[4 ]>>16))&&((Boss[1 ]>>16) == (Boss[5 ]>>16))){
					Boss[4 ] = (Sayo[0 ]&0xFFFF0000)+0x000C0000;
					Boss[5 ] = (Sayo[1 ]&0xFFFF0000)+0x000C0000;
				}

				int tmpx = (Sayo[0 ]-Boss[0 ])>>16;
				int tmpy = (Sayo[1 ]-Boss[1 ])>>16;

				if(((tmpx > -24)&&(tmpx < 24))&&(tmpy > 0)){
					boss_etc = 263 ;
				}
				else if(((tmpy > -24)&&(tmpy < 24))&&(tmpx < 0)){
					boss_etc = 265 ;
				}
				else if(((tmpx > -24)&&(tmpx < 24))&&(tmpy < 0)){
					boss_etc = 267 ;
				}
				else if(((tmpy > -24)&&(tmpy < 24))&&(tmpx > 0)){
					boss_etc = 269 ;
				}
				bossTamaMove(gg);
			}
			gg.drawImage(CharImage[boss_etc+((Boss[9 ]&0x10000000)>>28)], (Boss[0 ]-Worldxpos)>>16, (Boss[1 ]-Worldypos)>>16, gg.TOP|gg.LEFT);

		}

		if(Scene == 310 ){
			drawShadowString(gg, ""+Boss_name[Round_no], 120 >>1, ((GETWIDTH- 120 )>>1)+gg.getFont().getHeight(), gg.TOP|gg.HCENTER, 0xFF, 0xFF, 0xFF);
			Boss[9 ]++;
			if(Boss[9 ] == 70){
				Boss[9 ] = 0;
				Scene = 320 ;
			}
		}
	}


	private void bossMove()
	{
		if((Boss[9 ]&0x0000FF00) == 0x00000000)
		{
			if(Round_no == 0){
				Boss[4] = ((((int)((Zuzu_offsetx>>(Boss[6 ]<<3))&0xFF))<<16)+Worldxpos);
				Boss[5] = ((((int)((Zuzu_offsety>>(Boss[6 ]<<3))&0xFF))<<16)+Worldypos);
			}
			else if((Round_no == 1)||(Round_no == 3)){
				Boss[4 ] = ((((Math.abs(Rnd.nextInt())%72))<<16)+Worldxpos);
				Boss[5 ] = ((((Math.abs(Rnd.nextInt())%32))<<16)+Worldypos);
			}
			else if((Round_no == 2)||(Round_no == 4)){
				Boss[4 ] = ((((Math.abs(Rnd.nextInt())%72))<<16)+Worldxpos);
				Boss[5 ] = ((((Math.abs(Rnd.nextInt())%64))<<16)+Worldypos);
			}
			Boss[9 ] += 0x00000100;
		}
		else if((Boss[9 ]&0x0000FF00) == 0x00000100){
			Boss[2 ] = Boss[0 ];
			Boss[3 ] = Boss[1 ];
			if(Round_no == 4){
				bresenhamLine(Boss[0 ], Boss[1 ], Boss[4], Boss[5], 1);
			}
			else{
				bresenhamLine(Boss[0 ], Boss[1 ], Boss[4], Boss[5], 0);
			}
			Boss[0 ] = Bresenham_tmp[0];
			Boss[1 ] = Bresenham_tmp[1];
			if(((Boss[0 ]>>16) == (Boss[4 ]>>16))&&((Boss[1 ]>>16) == (Boss[5 ]>>16))){
				if(Round_no == 0){
					Boss[6 ]++;
					if(Boss[6 ] == 6){
						Boss[6 ] = 0;
					}
				}
				Boss[9 ] &= 0xFFFF00FF;
			}
		}
	}

	private void bossTamaMove(Graphics gg)
	{
		for(int i=0, j=0; i< 6 ; i++){
			if(Round_no == 0){
				if((Tama[0 ][i]&0x00FF0000) == 0x00FF0000){
					Tama[1 ][i] += (Tama_offset[(Tama[0 ][i]&0x0F000000)>>24]<<1)/4;
					Tama[2 ][i] += (0x00020000)/4;
					if((((Tama[2][i]-Worldypos)>>16) >= 128 )||((((Tama[1 ][i]-Worldxpos)>>16)+12) < 0)||(((Tama[1 ][i]-Worldxpos)>>16) >= 120 )){
						Tama[0][i] = 0;
					}
					gg.drawImage(CharImage[Tama[7 ][i]], (Tama[1 ][i]-Worldxpos)>>16, (Tama[2 ][i]-Worldypos)>>16, gg.TOP|gg.LEFT);
					if(
						1 == to_sayo_Hit(Tama[1 ][i],Tama[1 ][i]+0x000E0000,Tama[2 ][i],Tama[2 ][i]+0x000B0000,0)
							){
						break;
					}
				}
				else if(((Boss[9 ]&0x003F0000)>>16) == 0x00000020){
					Tama[0 ][i] = 0;
					Tama[0 ][i] += j<<24;
					Tama[0 ][i] += 0x00FF0000;
					Tama[1 ][i] = Boss[0]+0x00100000;
					Tama[2 ][i] = Boss[1 ]+0x00140000;
					Tama[7 ][i] = 208 ;
					j++;
					if(j == 3){
						playSoundTrack(SOUND_18); //Ph[18]
						break;
					}
				}
			}
			if(Round_no == 1){
				if((Tama[0 ][i]&0x00FF0000) == 0x00FF0000){
					bresenhamLine(Tama[1 ][i], Tama[2 ][i], Tama[5 ][i], Tama[6 ][i], 1);
					Tama[1 ][i] = Bresenham_tmp[0]-2;
					Tama[2 ][i] = Bresenham_tmp[1]-2;
					if(((Tama[1 ][i]>>16) == (Tama[5 ][i]>>16))&&((Tama[2 ][i]>>16) == (Tama[6 ][i]>>16))){
						Tama[0 ][i] = 0;
						Tama[0 ][i] += 0x0000FF00;
						Tama[7 ][i] = 347 ;
					}
					gg.drawImage(CharImage[Tama[7 ][i]], (Tama[1 ][i]-Worldxpos)>>16, (Tama[2 ][i]-Worldypos)>>16, gg.TOP|gg.LEFT);
					if(
						1 == to_sayo_Hit(Tama[1 ][i],Tama[1 ][i]+0x00070000,Tama[2 ][i],Tama[2 ][i]+0x000F0000,0)
							){
						break;
					}
				}
				else if((Tama[0 ][i]&0x0000FF00) == 0x0000FF00){
					Tama[0 ][i]++;
					if((Tama[0 ][i]&0x000000FF) == 120){
						Tama[0 ][i] = 0;
					}
					gg.drawImage(CharImage[Tama[7 ][i]], (Tama[1 ][i]-Worldxpos)>>16, (Tama[2 ][i]-Worldypos)>>16, gg.TOP|gg.LEFT);
					if(
						1 == to_sayo_Hit(Tama[1 ][i],Tama[1 ][i]+0x000A0000,Tama[2 ][i],Tama[2 ][i]+0x000B0000,0)
							){
						break;
					}
				}
				else if(((Boss[9 ]&0x003F0000)>>16) == 0x00000020){
					Tama[0 ][i] = 0;
					Tama[0 ][i] += 0x00FF0000;
					Tama[1 ][i] = Boss[0 ]+0x00100000;
					Tama[2 ][i] = Boss[1 ]+0x00140000;

					if(Boss[1 ] > Sayo[1 ]){
						Tama[5 ][i] = (Sayo[0 ]&0xFFFF0000)+0x000C0000;
						Tama[6 ][i] = (Sayo[1 ]&0xFFFF0000)+0x000C0000;
					}
					else{
						Tama[5 ][i] = ((Math.abs(Rnd.nextInt())%100)<<16)+Worldxpos;
						Tama[6 ][i] = (((Math.abs(Rnd.nextInt())%40)+60)<<16)+Worldypos;
					}
					Tama[7 ][i] = 212 ;
					playSoundTrack(SOUND_19); //Ph[19]
					break;
				}
			}
			else if(Round_no == 2){
				if((Tama[0 ][i]&0x00FF0000) == 0x00FF0000){
					Tama[1 ][i] += (Tama_offset[(Tama[0 ][i]&0x0F000000)>>24]);
					Tama[2 ][i] += (0x00010000);
					int char_no_offset = 0x00001210;
					if((Tama[0 ][i]&0x0F000000) == 0x01000000){
						gg.drawImage(CharImage[Tama[7 ][i]+((char_no_offset>>(Tama[0 ][i]&0x0000001C))&0x0000000F)], (Tama[1 ][i]-Worldxpos)>>16, (Tama[2 ][i]-Worldypos)>>16, gg.TOP|gg.LEFT);
						if(
							1 == to_sayo_Hit(Tama[1 ][i],Tama[1 ][i]+0x00070000,Tama[2 ][i],Tama[2 ][i]+0x000F0000+((((char_no_offset>>(Tama[0 ][i]&0x0000001C))&0x0000000F)<<3)<<16),0)
								){
							break;
						}
					}
					else{
						gg.drawImage(CharImage[Tama[7 ][i]+((char_no_offset>>(Tama[0 ][i]&0x0000000C))&0x0000000F)], (Tama[1 ][i]-Worldxpos)>>16, (Tama[2 ][i]-Worldypos)>>16, gg.TOP|gg.LEFT);
						if(
							1 == to_sayo_Hit(Tama[1 ][i]+0x00080000,Tama[1 ][i]+0x000F0000,Tama[2 ][i]+0x00080000,Tama[2 ][i]+0x000F0000,0)
								){
							break;
						}
					}

					Tama[0 ][i]++;
					if((((Tama[0 ][i]&0x0000000C)>>2) == 3)&&((Tama[0 ][i]&0x0F000000) != 0x01000000)){
						Tama[0 ][i] = 0;
					}
					else if(((Tama[0 ][i]&0x0000001C)>>2) == 5){
						Tama[0 ][i] = 0;
					}
				}
				else if(((Boss[9 ]&0x003F0000)>>16) == 0x00000020){
					Tama[0 ][i] = 0;
					Tama[0 ][i] += 0x00FF0000;
					if((Boss[0 ] <= Sayo[0 ])&&((Boss[0 ]+(56<<16)) > (Sayo[0 ]+(24<<16)))){

						Tama[0 ][i] += 0x01000000;
						Tama[1 ][i] = Boss[0 ]+0x00180000;
						Tama[2 ][i] = Boss[1 ]+0x00100000;
						Tama[7 ][i] = 226 ;
						playSoundTrack(SOUND_18); //Ph[18]
					}
					else if((Boss[0 ]+(56<<16)) <= (Sayo[0 ]+(24<<16))){
						Tama[0 ][i] += 0x02000000;
						Tama[1 ][i] = Boss[0 ]+0x00280000;
						Tama[2 ][i] = Boss[1 ]+0x00180000;
						Tama[7 ][i] = 223 ;
						playSoundTrack(SOUND_24); //Ph[24]
					}
					else{
						Tama[1 ][i] = Boss[0 ]+0xFFF80000;
						Tama[2 ][i] = Boss[1 ]+0x00180000;
						Tama[7 ][i] = 220 ;
						playSoundTrack(SOUND_24); //Ph[24]
					}
					break;
				}
			}
			else if(Round_no == 3){
				if((Tama[0][i]&0x00FF0000) == 0x00FF0000){
					Tama[1][i] += (Tama_offset[(Tama[0][i]&0x0F000000)>>24]);
					Tama[2][i] += (0x00018000);
					if((((Tama[2 ][i]-Worldypos)>>16) >= 128 )||((((Tama[1 ][i]-Worldxpos)>>16)+12) < 0)||(((Tama[1 ][i]-Worldxpos)>>16) >= 120 )){
						Tama[0 ][i] = 0;
					}
					gg.drawImage(CharImage[Tama[7 ][i]], (Tama[1 ][i]-Worldxpos)>>16, (Tama[2 ][i]-Worldypos)>>16, gg.TOP|gg.LEFT);
					if(
						1 == to_sayo_Hit(Tama[1 ][i],Tama[1 ][i]+0x00040000,Tama[2 ][i],Tama[2 ][i]+0x000B0000,0)
							){
						break;
					}
				}
				else if(((Boss[9 ]&0x001F0000)>>16) == 0x00000010){
					Tama[0 ][i] = 0;
					Tama[0 ][i] += j<<24;
					Tama[0 ][i] += 0x00FF0000;
					if((Boss[9 ]&0xFF000000) == 0){
						Tama[1 ][i] = Boss[0 ]+0x00020000;
						Tama[2 ][i] = Boss[1 ]+0x00180000;
					}
					else{
						Tama[1 ][i] = Boss[0 ]+0x002A0000;
						Tama[2 ][i] = Boss[1 ]+0x00180000;
					}
					Tama[7 ][i] = 232 ;
					j++;
					if(j == 1){
						Boss[9 ] ^= 0xFF000000;
					}
					if(j == 3){
						playSoundTrack(SOUND_18); //Ph[18]
						break;
					}
				}
			}
			else if(Round_no == 4){
				if((Boss[9 ]&0xFF000000) == 0x40000000){
					Tama[1 ][0] = Boss[0 ]+0xFFFD0000;
					Tama[2 ][0] = Boss[1 ];
					Tama[5 ][0] = Sayo[0 ]+0x000C0000;
					Tama[6 ][0] = Sayo[1 ]+0x000C0000;
					playSoundTrack(SOUND_18); //Ph[18]
					break;
				}
				else if((Boss[9 ]&0xFF000000) <= 0x58000000){
					bresenhamLine(Tama[1 ][0], Tama[2 ][0], Tama[5 ][0], Tama[6 ][0], 1);
					Tama[3 ][0] = Tama[1 ][0];
					Tama[4 ][0] = Tama[2 ][0];
					Tama[1 ][0] = Bresenham_tmp[0];
					Tama[2 ][0] = Bresenham_tmp[1];
					break;
				}
			}
			else if(Round_no == 5){
				if((Zako[0 ][i]&0x00FF0000) == 0x00FF0000){
					bresenhamLine(Zako[1 ][i], Zako[2 ][i], Zako[3 ][i], Zako[4 ][i], 0);
					Zako[1 ][i] = Bresenham_tmp[0];
					Zako[2 ][i] = Bresenham_tmp[1];
					if(((Zako[1 ][i]>>16) == (Zako[3 ][i]>>16))&&((Zako[2 ][i]>>16) == (Zako[4 ][i]>>16))){
						Zako[3 ][i] = (Sayo[0 ]&0xFFFF0000)+0x000C0000;
						Zako[4 ][i] = (Sayo[1 ]&0xFFFF0000)+0x000C0000;
					}
					int tmpx = (Sayo[0 ]-Zako[1 ][i])>>16;
					int tmpy = (Sayo[1 ]-Zako[2 ][i])>>16;

					if(((tmpx > -12)&&(tmpx < 12))&&(tmpy > 0)){
						Zako[5 ][i] = 255 ;
						Zako[8 ][i] = 0x0B150000;
					}
					else if(((tmpy > -12)&&(tmpy < 12))&&(tmpx < 0)){
						Zako[5 ][i] = 257 ;
						Zako[8 ][i] = 0x1C0C0000;
					}
					else if(((tmpx > -12)&&(tmpx < 12))&&(tmpy < 0)){
						Zako[5 ][i] = 259 ;
						Zako[8 ][i] = 0x0B150000;
					}
					else if(((tmpy > -12)&&(tmpy < 12))&&(tmpx > 0)){
						Zako[5 ][i] = 261 ;
						Zako[8 ][i] = 0x1C0C0000;
					}
					gg.drawImage(CharImage[Zako[5 ][i]+((Boss[9 ]&0x10000000)>>28)], (Zako[1 ][i]-Worldxpos)>>16, (Zako[2 ][i]-Worldypos)>>16, gg.TOP|gg.LEFT);

					if(
						1 == to_sayo_Hit(Zako[1 ][i]+((((Zako[8 ][i]&0x0000FF00)>>8))<<16),Zako[1 ][i]+((((Zako[8 ][i]&0x0000FF00)>>8)+((Zako[8 ][i]&0xFF000000)>>24)-1)<<16),Zako[2 ][i]+(((Zako[8 ][i]&0x000000FF))<<16),Zako[2 ][i]+(((Zako[8 ][i]&0x000000FF)+((Zako[8 ][i]&0x00FF0000)>>16)-1)<<16),0)
							){
						break;
					}

				}
				else if((Zako[0 ][i]&0x000000FF) != 0x00000000){
					tobiZako(gg, i);
				}
				else if((Boss[9 ]&0xFF000000) == 0x3F000000){
					Zako[0 ][i] = 0;
					Zako[0 ][i] += 0x00FF0000;
					int tmp = (Math.abs(Rnd.nextInt())%4);
					if(tmp == 0){
						Zako[1 ][i] = (0xFFE00000+Worldxpos);
						Zako[2 ][i] = (((Math.abs(Rnd.nextInt())% 128 )<<16)+Worldypos);
						Zako[5 ][i] = 261 ;
						Zako[8 ][i] = 0x1C0C0000;
					}
					else if(tmp == 1){
						Zako[1 ][i] = (0x00780000+Worldxpos);
						Zako[2 ][i] = ((Math.abs(Rnd.nextInt())% 128 )<<16)+Worldypos;
						Zako[5 ][i] = 257 ;
						Zako[8 ][i] = 0x1C0C0000;
					}
					else if(tmp == 2){
						Zako[1 ][i] = ((Math.abs(Rnd.nextInt())% 120 )<<16)+Worldxpos;
						Zako[2 ][i] = 0xFFF00000+Worldypos;
						Zako[5 ][i] = 255 ;
						Zako[8 ][i] = 0x0B150000;
					}
					else if(tmp == 3){
						Zako[1 ][i] = ((Math.abs(Rnd.nextInt())% 120 )<<16)+Worldxpos;
						Zako[2 ][i] = 0x00820000+Worldypos;
						Zako[5 ][i] = 259;
						Zako[8 ][i] = 0x0B150000;
					}
					Zako[3 ][i] = (Sayo[0 ]&0xFFFF0000)+0x000C0000;
					Zako[4 ][i] = (Sayo[1 ]&0xFFFF0000)+0x000C0000;
					Zako[7 ][i] = 0x00010001;
					Zako[10 ][i] = 0x00000000 ;
					break;
				}
			}
			if(Round_no == 6){
				if((Tama[0 ][i]&0x00FF0000) == 0x00FF0000){
					bresenhamLine(Tama[1 ][i], Tama[2 ][i], Tama[5 ][i], Tama[6 ][i], 1);
					Tama[1 ][i] = Bresenham_tmp[0];
					Tama[2 ][i] = Bresenham_tmp[1];

					if(((Tama[1 ][i]>>16) == (Tama[5 ][i]>>16))&&((Tama[2 ][i]>>16) == (Tama[6 ][i]>>16))){
						Tama[0 ][i] = 0;
					}
					gg.drawImage(CharImage[Tama[7 ][i]], (Tama[1 ][i]-Worldxpos)>>16, (Tama[2 ][i]-Worldypos)>>16, gg.TOP|gg.LEFT);
					if(
						1 == to_sayo_Hit(Tama[1 ][i],Tama[1 ][i]+0x00070000,Tama[2 ][i],Tama[2 ][i]+0x00050000,0)
							){
						break;
					}

				}
				else if((Boss[9 ]&0x003F0000) == 0x00100000){
					Tama[0 ][i] = 0;
					Tama[0 ][i] += 0x00FF0000;
					Tama[1 ][i] = Boss[0 ]+0x000C0000;
					Tama[2 ][i] = Boss[1 ]+0x000C0000;
					Tama[5 ][i] = Sayo[0 ];
					Tama[6 ][i] = Sayo[1 ];
					Tama[7 ][i] = 346 ;
					playSoundTrack(SOUND_18); //Ph[18]
					break;
				}
			}
		}
	}


	public void drawZako(Graphics gg){
		for(int i=0; i< 12 - 6 ; i++){
			Zako[9 ][i] += 0x00010001;
			Zako[9 ][i] &= 0x00FFFFFF;
			if((Zako[0 ][i]&0x00FF0000) == 0x00FF0000){
				if((Time_stop == 0)&&((Zako[5 ][i] == 131 )||(Zako[5 ][i] == 133 )||(Zako[5 ][i] == 129 ))){
					int check = 0;
					if(Zako[5 ][i] == 129 ){
						check = 1;
					}
					if(Zako[4 ][i] == 0x00000000){
						Zako[2 ][i] += ((Kidou_tbl[check][0]>>(Zako[9 ][i]&0x000000FF))&0x00000001)<<16;
						if(Zako[3 ][i] == 0xFFF00000){
							Zako[1 ][i] += ((Kidou_tbl[check][1]>>(Zako[9 ][i]&0x000000FF))&0x00000001)<<16;
						}
						else{
							Zako[1 ][i] -= ((Kidou_tbl[check][1]>>(Zako[9 ][i]&0x000000FF))&0x00000001)<<16;
						}
					}
					else{
						Zako[2 ][i] -= ((Kidou_tbl[check][1]>>(Zako[9 ][i]&0x000000FF))&0x00000001)<<16;
						if(Zako[3 ][i] == 0xFFF00000){
							Zako[1 ][i] += ((Kidou_tbl[check][0]>>(Zako[9 ][i]&0x000000FF))&0x00000001)<<16;
						}
						else{
							Zako[1 ][i] -= ((Kidou_tbl[check][0]>>(Zako[9 ][i]&0x000000FF))&0x00000001)<<16;
						}
					}
				}
				else if((Time_stop == 0)&&((Zako[5 ][i] == 130 )||((Zako[5 ][i] >= 339 )||(Zako[5 ][i] <= 343 )))){
					if(Zako[6 ][i] == 0){
						Zako[2 ][i] += 0x00020000;
					}
					else if(Zako[6 ][i] == 2){
						Zako[1 ][i] -= 0x00020000;
					}
					else if(Zako[6 ][i] == 4){
						Zako[2 ][i] -= 0x00020000;
					}
					else if(Zako[6 ][i] == 6){
						Zako[1 ][i] += 0x00020000;
					}
				}
				if((((Zako[2 ][i]-Worldypos)>>16) < -64)||(((Zako[2 ][i]-Worldypos)>>16) >= 194)||(((Zako[1 ][i]-Worldxpos)>>16) < -64)||(((Zako[1 ][i]-Worldxpos)>>16) >= 184)){
					Zako[0 ][i] = 0;
				}
				if(((Zako[5 ][i] == 131 )||(Zako[5 ][i] == 133 ))&&((Zako[9 ][i]&0x000000FF) < 24)){
					gg.drawImage(CharImage[Zako[5 ][i]+1], (Zako[1 ][i]-Worldxpos)>>16, (Zako[2 ][i]-Worldypos)>>16, gg.TOP|gg.LEFT);
				}
				else if(!((Zako[5 ][i] == 189 )||(Zako[5 ][i] == 190 ))){
					gg.drawImage(CharImage[Zako[5 ][i]], (Zako[1 ][i]-Worldxpos)>>16, (Zako[2 ][i]-Worldypos)>>16, gg.TOP|gg.LEFT);
				}
				if(
					1 == to_sayo_Hit(Zako[1 ][i]+((((Zako[8 ][i]&0x0000FF00)>>8))<<16),Zako[1 ][i]+((((Zako[8 ][i]&0x0000FF00)>>8)+((Zako[8 ][i]&0xFF000000)>>24)-1)<<16),Zako[2 ][i]+(((Zako[8 ][i]&0x000000FF))<<16),Zako[2 ][i]+(((Zako[8 ][i]&0x000000FF)+((Zako[8 ][i]&0x00FF0000)>>16)-1)<<16),0)
						){
					break;
				}
			}
			else if((Zako[0 ][i]&0x000000FF) != 0x00000000){
				tobiZako(gg, i);
			}
			else if((i<3)&((Zako[9 ][i]>>16) == (150/(Round_no+1)))&&((Round_no+(World_timer>>10)) >= i)){
				Zako[0 ][i] = 0;
				Zako[0 ][i] += 0x00FF0000;
				Zako[1 ][i] = Hashiritai_posx[(Math.abs(Rnd.nextInt())%4)]+Worldxpos;
				Zako[2 ][i] = Hashiritai_posy[(Math.abs(Rnd.nextInt())%4)]+Worldypos;
				Zako[3 ][i] = Zako[1 ][i]-Worldxpos;
				Zako[4 ][i] = Zako[2 ][i]-Worldypos;
				if((Math.abs(Rnd.nextInt())%2) == 0){
					if(Zako[3 ][i] == 0xFFF00000){
						Zako[5 ][i] = 131 ;
					}
					else{
						Zako[5 ][i] = 133 ;
					}
					Zako[8 ][i] = 0x10100000;
					Zako[10 ][i] = 0x00320064 ;
				}
				else{
					Zako[5 ][i] = 129 ;
					Zako[8 ][i] = 0x0E0E0000;
					Zako[10 ][i] = 0x00320064 ;
				}
				Zako[7 ][i] = 0x00010001;
				break;
			}
			else if((Zako_limit_timer&0x000000FF) == 0){
				Zako_limit_timer |= 0x0000001F;
				for(int j=0; j<Ido_pos[Round_no].length; j++){
					if(
						(((Ido_pos[Round_no][j]>>16)+16)>(Worldxpos>>16))
							&&((Ido_pos[Round_no][j]>>16)<((Worldxpos>>16)+ 120 ))
							&&(((Ido_pos[Round_no][j]&0x0000FFFF)+16)>(Worldypos>>16))
							&&((Ido_pos[Round_no][j]&0x0000FFFF)<((Worldypos>>16)+ 128 ))
							){
						if(
							(((Sayo[0 ]>>16) >= ((Ido_pos[Round_no][j]>>16)-1))&&((Sayo[0 ]>>16) <= ((Ido_pos[Round_no][j]>>16)+1)))
								||(((Sayo[1 ]>>16) >= ((Ido_pos[Round_no][j]&0x0000FFFF)-1))&&((Sayo[1 ]>>16) <= ((Ido_pos[Round_no][j]&0x0000FFFF)+1)))
								){
							playSoundTrack(SOUND_23); //Ph[23]
							Zako[0 ][i] = 0;
							Zako[0 ][i] += 0x00FF0000;
							Zako[1 ][i] = (Ido_pos[Round_no][j]&0xFFFF0000);
							Zako[2 ][i] = (Ido_pos[Round_no][j]<<16);
							Zako[10 ][i] = 0x00320064 ;
							Zako[8 ][i] = 0x0F0B0000;
							Zako[7 ][i] = 0x00010001;

							if((Bakeuri_count == 0x10)||(Bakeuri_count == 0x20)||(Bakeuri_count == 0x30)){
								Zako[5 ][i] = 339 ;
							}
							else if((Bakeuri_count == 0x40)||(Bakeuri_count == 0x80)){
								Zako[5 ][i] = 340 ;
							}
							else if(Bakeuri_count == 0x50){
								Zako[5 ][i] = 341 ;
							}
							else if((Bakeuri_count == 0x60)||(Bakeuri_count == 0xA0)){
								Zako[5 ][i] = 342 ;
							}
							else if(Bakeuri_count == 0xE0){
								Zako[5 ][i] = 343 ;
							}
							else{
								Zako[5 ][i] = 130 ;
							}
							Bakeuri_count++;
							Bakeuri_count &= 0x00FF;

							if((Sayo[1 ]>>16) > ((Ido_pos[Round_no][j]&0x0000FFFF)+1)){
								Zako[6 ][i] = 0;
							}
							else if((Sayo[1 ]>>16) < ((Ido_pos[Round_no][j]&0x0000FFFF)-1)){
								Zako[6 ][i] = 4;
							}
							else if((Sayo[0 ]>>16) > ((Ido_pos[Round_no][j]>>16)+1)){
								Zako[6 ][i] = 6;
							}
							else if((Sayo[0 ]>>16) < ((Ido_pos[Round_no][j]>>16)-1)){
								Zako[6 ][i] = 2;
							}
						}
					}
				}
			}

			if((Zako_limit_timer&0x000000FF) != 0){
				Zako_limit_timer--;
			}

			if((Zako[9 ][i]&0x000000FF) == 48){
				Zako[9 ][i] &= 0xFFFFFF00;
				Zako[9 ][i] += 0x00000100;

				if((Zako[5 ][i] == 131 )||(Zako[5 ][i] == 133 )){
					if(((Zako[9 ][i]>>8)%2) == 0){


					}
					else{


					}
				}
			}
		}
	}

	public void drawZako2(Graphics gg){
		for(int i= 6 ; i< 12 ; i++){
			Zako[9 ][i] += 0x00010001;
			if(Zako[5 ][i] == 175 ){
				Zako[9 ][i] &= 0x771FFFFF;
			}
			else{
				Zako[9 ][i] &= 0x777FFFFF;
			}
			if((Zako[0 ][i]&0x00FF0000) == 0x00FF0000){
				if(Zako[5 ][i] == 183 ){
					Zako[3 ][i] = Zako[1 ][i];
					Zako[4 ][i] = (Sayo[1 ]&0xFFFF0000);
				}
				else if((Time_stop == 0)&&(Zako[5 ][i] >= 191 )&&(Zako[5 ][i] <= 199 )){
					if((Zako[9 ][i]>>30) == 0x01){
						if(Zako[6 ][i] == 0){
							Zako[2 ][i] -= 0x00020000;
						}
						else if(Zako[6 ][i] == 1){
							Zako[1 ][i] += 0x00020000;
							Zako[2 ][i] -= 0x00020000;
						}
						else if(Zako[6 ][i] == 2){
							Zako[1 ][i] += 0x00020000;
						}
						else if(Zako[6 ][i] == 3){
							Zako[1 ][i] += 0x00020000;
							Zako[2 ][i] += 0x00020000;
						}
						else if(Zako[6 ][i] == 4){
							Zako[2 ][i] += 0x00020000;
						}
						else if(Zako[6 ][i] == 5){
							Zako[1 ][i] -= 0x00020000;
							Zako[2 ][i] += 0x00020000;
						}
						else if(Zako[6 ][i] == 6){
							Zako[1 ][i] -= 0x00020000;
						}
						else if(Zako[6 ][i] == 7){
							Zako[1 ][i] -= 0x00020000;
							Zako[2 ][i] -= 0x00020000;
						}

					}
					else if((Zako[9 ][i]>>29) == 0x01){
						if(Zako[6 ][i] == 0){
							Zako[2 ][i] += 0x00020000;
						}
						else if(Zako[6 ][i] == 1){
							Zako[1 ][i] -= 0x00020000;
							Zako[2 ][i] += 0x00020000;
						}
						else if(Zako[6 ][i] == 2){
							Zako[1 ][i] -= 0x00020000;
						}
						else if(Zako[6 ][i] == 3){
							Zako[1 ][i] -= 0x00020000;
							Zako[2 ][i] -= 0x00020000;
						}
						else if(Zako[6 ][i] == 4){
							Zako[2 ][i] -= 0x00020000;
						}
						else if(Zako[6 ][i] == 5){
							Zako[1 ][i] += 0x00020000;
							Zako[2 ][i] -= 0x00020000;
						}
						else if(Zako[6 ][i] == 6){
							Zako[1 ][i] += 0x00020000;
						}
						else if(Zako[6 ][i] == 7){
							Zako[1 ][i] += 0x00020000;
							Zako[2 ][i] += 0x00020000;
						}

						if((Zako[9 ][i]&0x000000FF) == 12){
							Zako[9 ][i] += 0x40000000;
						}
					}
					else if((Zako[9 ][i]>>28) == 0x01){
						Zako[3 ][i] = Sayo[0 ]+(Kasabe_offset[((Zako[9 ][i]>>24)&0x07)+2]<<16);
						Zako[4 ][i] = Sayo[1 ]+(Kasabe_offset[(Zako[9 ][i]>>24)&0x07]<<16);
						bresenhamLine(Zako[1 ][i], Zako[2 ][i], Zako[3 ][i], Zako[4 ][i], 2);
						Zako[1 ][i] = Bresenham_tmp[0];
						Zako[2 ][i] = Bresenham_tmp[1];
						if((Zako[3 ][i] == Zako[1 ][i])&&(Zako[4 ][i] == Zako[2 ][i])){
							if(((Zako[9 ][i]>>8)&0x00000007) == ((Zako[9 ][i]>>24)&0x00000007)){
								Zako[9 ][i] += 0x20000000;
								Zako[6 ][i] = ((Zako[9 ][i]>>24)&0x00000007);
								if(((Zako[9 ][i]>>24)&0x00000007) < 4){
									Zako[5 ][i] += ((Zako[9 ][i]>>24)&0x00000007)+4+1;
								}
								else{
									Zako[5 ][i] += ((Zako[9 ][i]>>24)&0x00000007)-4+1;
								}
								Zako[9 ][i] &= 0xFFFFFF00;
							}
							else{
								Zako[9 ][i] += 0x01000000;
								Zako[9 ][i] &= 0x777FFFFF;
							}
						}
					}
					else if(((Zako[9 ][i]>>28) == 0)&&((Math.abs(Zako[1 ][i]-Sayo[0 ])>>16) < 24)&&((Math.abs(Zako[2 ][i]-Sayo[1 ])>>16) < 24)){
						Zako[9 ][i] += 0x10000000 + ((Math.abs(Rnd.nextInt())%8)<<8);
					}
					else{
						Zako[3 ][i] = Sayo[0 ]+0x000C0000;
						Zako[4 ][i] = Sayo[1 ]+0x000C0000;

						bresenhamLine(Zako[1 ][i], Zako[2 ][i], Zako[3 ][i], Zako[4 ][i], 1);
						Zako[1 ][i] = Bresenham_tmp[0];
						Zako[2 ][i] = Bresenham_tmp[1];
					}
				}

				else if((Time_stop == 0)&&(Zako[5 ][i] == 135 )){

					bresenhamLine(Zako[1 ][i], Zako[2 ][i], Zako[3 ][i], Zako[4 ][i], 0);
					Zako[1 ][i] = Bresenham_tmp[0];
					Zako[2 ][i] = Bresenham_tmp[1];


					if((Zako[3 ][i] == Zako[1 ][i])&&(Zako[4 ][i] == Zako[2 ][i])){
						Zako[9 ][i] += 0x00000100;
						Zako[9 ][i] &= 0x777F0FFF;
						Zako[3 ][i] = Otama_start_posx + (Otama_offsetx[(Zako[9 ][i]>>8)&0xF]<<16);
						Zako[4 ][i] = Otama_start_posy + (Otama_offsety[(Zako[9 ][i]>>8)&0xF]<<16);
						if((((Zako[9 ][i]>>8)&0xF)==0)||(((Zako[9 ][i]>>8)&0xF)==1)||(((Zako[9 ][i]>>8)&0xF)==8)||(((Zako[9 ][i]>>8)&0xF)==9)){
							Zako[6 ][i] = 0;
						}
						else if((((Zako[9 ][i]>>8)&0xF)==10)||(((Zako[9 ][i]>>8)&0xF)==11)||(((Zako[9 ][i]>>8)&0xF)==14)||(((Zako[9 ][i]>>8)&0xF)==15)){
							Zako[6 ][i] = 2;
						}
						else if((((Zako[9 ][i]>>8)&0xF)==4)||(((Zako[9 ][i]>>8)&0xF)==5)||(((Zako[9 ][i]>>8)&0xF)==12)||(((Zako[9 ][i]>>8)&0xF)==13)){
							Zako[6 ][i] = 4;
						}
						else if((((Zako[9 ][i]>>8)&0xF)==2)||(((Zako[9 ][i]>>8)&0xF)==3)||(((Zako[9 ][i]>>8)&0xF)==6)||(((Zako[9 ][i]>>8)&0xF)==7)){
							Zako[6 ][i] = 6;
						}
					}

				}
				else{
					if((Zako[3 ][i]&0xFFFF0000)==(Zako[1 ][i]&0xFFFF0000)){
						Zako[4 ][i] = Sayo[1 ];
						if((Zako[2 ][i]&0xFFFE0000)<(Sayo[1 ]&0xFFFF0000)){
							Zako[6 ][i] = 0;
						}
						else{
							Zako[6 ][i] = 4;
						}

						if((Zako[4 ][i]&0xFFFF0000)==(Zako[2 ][i]&0xFFFF0000)){
							if((Zako[1 ][i]&0xFFFE0000)<(Sayo[0 ]&0xFFFF0000)){
								Zako[6 ][i] = 6;
							}
							else{
								Zako[6 ][i] = 2;
							}
							Zako[3 ][i] = Sayo[0 ];
						}
					}
					else if((Zako[4 ][i]&0xFFFF0000)==(Zako[2 ][i]&0xFFFF0000)){
						Zako[3 ][i] = Sayo[0 ];
						if((Zako[1 ][i]&0xFFFE0000)<(Sayo[0 ]&0xFFFF0000)){
							Zako[6 ][i] = 6;
						}
						else{
							Zako[6 ][i] = 2;
						}

						if((Zako[3 ][i]&0xFFFF0000)==(Zako[1 ][i]&0xFFFF0000)){
							if((Zako[2 ][i]&0xFFFE0000)<(Sayo[1 ]&0xFFFF0000)){
								Zako[6 ][i] = 0;
							}
							else{
								Zako[6 ][i] = 4;
							}
							Zako[4 ][i] = Sayo[1 ];
						}
					}

					if(Zako[5 ][i] == 151 ){
						Zako[3 ][i] = Sayo[0 ];
						Zako[4 ][i] = Sayo[1 ];
					}
				}

				if(
					(Round_no > 2)
						&&(Time_stop == 0)
						&&((Zako[5 ][i] == 143 )||(Zako[5 ][i] == 175 )||(Zako[5 ][i] == 167 ))
						&&((Tama[0 ][i- 6 ]&0x00FF0000)==0)
						&&(((Zako[9 ][i]&0x007F0000)>>16) == 0x007F)
						){
					Tama[0 ][i- 6 ] = 0;
					Tama[0 ][i- 6 ] += 0x00FF0000;
					Tama[1 ][i- 6 ] = Zako[1 ][i]+0x000C0000;
					Tama[2 ][i- 6 ] = Zako[2 ][i]+0x000C0000;
					Tama[5 ][i- 6 ] = Sayo[0 ]+0x000C0000;
					Tama[6 ][i- 6 ] = Sayo[1 ]+0x000C0000;
					Tama[7 ][i- 6 ] = 346 ;
				}
				else if(
					(Round_no > 2)
						&&(Time_stop == 0)
						&&(Zako[5 ][i] == 175 )
						&&((Tama[0 ][i- 6 ]&0x00FF0000)==0)
						&&(((Zako[9 ][i]&0x000F0000)>>16) == 0x000F)
						){
					Tama[0 ][i- 6 ] = 0;
					Tama[0 ][i- 6 ] += 0x00FF0000;
					Tama[1 ][i- 6 ] = Zako[1 ][i]+0x000C0000;
					Tama[2 ][i- 6 ] = Zako[2 ][i]+0x000C0000;
					Tama[5 ][i- 6 ] = Sayo[0 ]+0x000C0000;
					Tama[6 ][i- 6 ] = Sayo[1 ]+0x000C0000;
					Tama[7 ][i- 6 ] = 346 ;
				}

				if((Time_stop == 0)&&(Zako[5 ][i] == 183 )){
					if((Zako[1 ][i]&0xFFF00000)==(Sayo[0 ]&0xFFF00000)){

						bresenhamLine(Zako[1 ][i], Zako[2 ][i], Zako[3 ][i], Zako[4 ][i], 2);
						Zako[1 ][i] = Bresenham_tmp[0];
						Zako[2 ][i] = Bresenham_tmp[1];
					}
					else if(((Zako[9 ][i]&0x00000002)>>1) == 0){

						bresenhamLine(Zako[1 ][i], Zako[2 ][i], Zako[3 ][i], Zako[4 ][i], 0);
						Zako[1 ][i] = Bresenham_tmp[0];
						Zako[2 ][i] = Bresenham_tmp[1];
					}
				}
				else if((Time_stop == 0)&&(Zako[5 ][i] == 151 )){

					if((((MAP[(WORLDMAP[Round_no][((Zako[2 ][i]+0x00140000)>>16)/ 128 ][((Zako[1 ][i]+0x00080000)>>16)/ 120 ])&0x1F][((Math.abs((((Zako[2 ][i]+0x00140000)/(128 <<16))*(128 <<16))-(Zako[2 ][i]+0x00140000)))>>19)][((Math.abs((((Zako[1 ][i]+0x00080000)/(120 <<16))*(120 <<16))-(Zako[1 ][i]+0x00080000)))>>19)]&0x00000080)>>7) == 1)){
						Zako[9 ][i] &= 0xFFFF0000;
						Zako[0 ][i] = 0x0000FF00;
					}
					else{

						bresenhamLine(Zako[1 ][i], Zako[2 ][i], Zako[3 ][i], Zako[4 ][i], 1);
						Zako[1 ][i] = Bresenham_tmp[0];
						Zako[2 ][i] = Bresenham_tmp[1];


						if(((Zako[3 ][i]>>16) == (Zako[1 ][i]>>16))&&((Zako[4 ][i]>>16) == (Zako[2 ][i]>>16))){
							Zako[9 ][i] |= 0x0000FF00;
							Rumuru_Scroll_quantity = 0x00010000;
							Zako[1 ][i] = Sayo[0 ];
							Zako[2 ][i] = Sayo[1 ];
						}
					}
				}
				else if((Time_stop == 0)&&(((Zako[9 ][i]&0x00000002)>>1) == 0)){
					if((Zako[5 ][i] == 159 )||(Zako[5 ][i] == 167 )){

						if((((MAP[(WORLDMAP[Round_no][((Zako[2 ][i]+0x00140000)>>16)/ 128 ][((Zako[1 ][i]+0x00080000)>>16)/ 120 ])&0x1F][((Math.abs((((Zako[2 ][i]+0x00140000)/(128 <<16))*(128 <<16))-(Zako[2 ][i]+0x00140000)))>>19)][((Math.abs((((Zako[1 ][i]+0x00080000)/(120 <<16))*(120 <<16))-(Zako[1 ][i]+0x00080000)))>>19)]&0x00000080)>>7) == 1)){
							Zako[9 ][i] &= 0xFFFF0000;
							Zako[0 ][i] = 0x0000FF00;
						}
						else{

							bresenhamLine(Zako[1 ][i], Zako[2 ][i], Zako[3 ][i], Zako[4 ][i], 0);
							Zako[1 ][i] = Bresenham_tmp[0];
							Zako[2 ][i] = Bresenham_tmp[1];
						}
					}
					else if((Zako[5 ][i] == 143 )||(Zako[5 ][i] == 185 )){

						bresenhamLine(Zako[1 ][i], Zako[2 ][i], Zako[3 ][i], Zako[4 ][i], 0);
						Zako[1 ][i] = Bresenham_tmp[0];
						Zako[2 ][i] = Bresenham_tmp[1];
					}
				}
				else if((Time_stop == 0)&&(Zako[5 ][i] == 175 )&&((Zako[9 ][i]&0x001F0000)>>20) == 1){

					bresenhamLine(Zako[1 ][i], Zako[2 ][i], Zako[3 ][i], Zako[4 ][i], 0);
					Zako[1 ][i] = Bresenham_tmp[0];
					Zako[2 ][i] = Bresenham_tmp[1];
				}

				if((Zako[5 ][i] == 159 )||(Zako[5 ][i] == 167 )||(Zako[5 ][i] == 151 )){

					gg.drawImage(CharImage[Zako[5 ][i]+Zako[6 ][i]+((Zako[9 ][i]&0x00000004)>>2)], (Zako[1 ][i]-Worldxpos)>>16, (Zako[2 ][i]-Worldypos)>>16, gg.TOP|gg.LEFT);
				}
				else if(Zako[5 ][i] == 185 ){

					gg.drawImage(CharImage[Zako[5 ][i]+((Zako[6 ][i]&0x00000004)>>1)+((Zako[9 ][i]&0x00000004)>>2)], (Zako[1 ][i]-Worldxpos)>>16, (Zako[2 ][i]-Worldypos)>>16, gg.TOP|gg.LEFT);
				}
				else if(Zako[5 ][i] == 183 ){

					gg.drawImage(CharImage[Zako[5 ][i]+((Zako[9 ][i]&0x00000004)>>2)], (Zako[1 ][i]-Worldxpos)>>16, (Zako[2 ][i]-Worldypos)>>16, gg.TOP|gg.LEFT);
				}
				else if((Zako[5 ][i] >= 191 )&&(Zako[5 ][i] <= 199 )){

					gg.drawImage(CharImage[Zako[5 ][i]], (Zako[1 ][i]-Worldxpos)>>16, (Zako[2 ][i]-Worldypos)>>16, gg.TOP|gg.LEFT);
				}
				else if(Zako[5 ][i] == 135 ){
					gg.drawImage(CharImage[Zako[5 ][i]+Zako[6 ][i]], (Zako[1 ][i]-Worldxpos)>>16, (Zako[2 ][i]-Worldypos)>>16, gg.TOP|gg.LEFT);
				}
				else{

					gg.drawImage(CharImage[Zako[5 ][i]+Zako[6 ][i]+((Zako[9 ][i]&0x00000010)>>4)], (Zako[1 ][i]-Worldxpos)>>16, (Zako[2 ][i]-Worldypos)>>16, gg.TOP|gg.LEFT);
				}
				if((((Zako[2 ][i]-Worldypos)>>16) < -64)||(((Zako[2 ][i]-Worldypos)>>16) >= 194)||(((Zako[1 ][i]-Worldxpos)>>16) < -64)||(((Zako[1 ][i]-Worldxpos)>>16) >= 184)){
					Zako[0 ][i] = 0;
				}

				if(Zako[5 ][i] != 151 ){
					if(
						1 == to_sayo_Hit(Zako[1 ][i]+((((Zako[8 ][i]&0x0000FF00)>>8))<<16),Zako[1 ][i]+((((Zako[8 ][i]&0x0000FF00)>>8)+((Zako[8 ][i]&0xFF000000)>>24)-1)<<16),Zako[2 ][i]+(((Zako[8 ][i]&0x000000FF))<<16),Zako[2 ][i]+(((Zako[8 ][i]&0x000000FF)+((Zako[8 ][i]&0x00FF0000)>>16)-1)<<16),0)
							){
						break;
					}
				}

			}
			else if((Zako[0 ][i]&0x0000FF00) != 0x00000000){
				jumpZako(gg, i);
			}
			else if((Zako[0 ][i]&0x000000FF) != 0x00000000){
				tobiZako(gg, i);
			}

			if((Tama[0 ][i- 6 ]&0x00FF0000) == 0x00FF0000){

				bresenhamLine(Tama[1 ][i- 6 ], Tama[2 ][i- 6 ], Tama[5 ][i- 6 ], Tama[6 ][i- 6 ], 0);
				Tama[1 ][i- 6 ] = Bresenham_tmp[0];
				Tama[2 ][i- 6 ] = Bresenham_tmp[1];

				if(((Tama[1 ][i- 6 ]>>16) == (Tama[5 ][i- 6 ]>>16))&&((Tama[2 ][i- 6 ]>>16) == (Tama[6 ][i- 6 ]>>16))){
					Tama[0 ][i- 6 ] = 0;
				}

				gg.drawImage(CharImage[Tama[7 ][i- 6 ]], (Tama[1 ][i- 6 ]-Worldxpos)>>16, (Tama[2 ][i- 6 ]-Worldypos)>>16, gg.TOP|gg.LEFT);

				if(
					1 == to_sayo_Hit(Tama[1 ][i- 6 ],Tama[1 ][i- 6 ]+0x00070000,Tama[2 ][i- 6 ],Tama[2 ][i- 6 ]+0x00050000,0)
						){
					break;
				}
			}
		}
	}



	public void setZako2(int offsetx, int offsety, int destinationx, int destinationy, int direction, int etc){
		for(int i= 6 ; i< 12 ; i++){
			if((Zako[0 ][i]&0x00FFFFFF) == 0){
				Zako[8 ][i] = Zako_hit_size[etc];
				Zako[0 ][i] = 0;
				Zako[0 ][i] += 0x00FF0000;
				Zako[1 ][i] = Worldxpos+offsetx;
				Zako[2 ][i] = Worldypos+offsety;
				Zako[3 ][i] = Worldxpos+destinationx;
				Zako[4 ][i] = Worldypos+destinationy;
				Zako[5 ][i] = Zako_etc_tbl[etc];
				Zako[10 ][i] = Zako_score_tbl[etc];
				Zako[6 ][i] = direction;
				if(etc == 0x0000000B){
					Zako[7 ][i] = 0x00050005;
				}
				else{
					Zako[7 ][i] = 0x00010001;
				}
				Zako[9 ][i] = 0;
				break;
			}
		}
	}

	public int to_sayo_Hit(int posl, int posr, int posu, int posd, int item_flg){
		if(
			((Sayo[0 ]+ 0x000F0000 ) > posl)
				&&((Sayo[0 ]+ 0x00070000 ) < posr)
				&&((Sayo[1 ]+ 0x000F0000 ) > posu)
				&&((Sayo[1 ]+ 0x00080000 ) < posd)
				){
			if(item_flg == 0){

				Sayo[5 ]--;
				if(Sayo[5 ]!=0)
				playSoundTrack(SOUND_24);
				Sayo[5 ] &= 0x000000FF;
				Die_Scene = Scene;
				Scene = 400 ;
      		}
			return 1;
		}
		else{
			return 0;
		}

	}



	private void bresenhamLine(int x0, int y0, int x1, int y1, int lc){
		int E;
		int x, y, dx, dy, wx, wy, i;
		wx = Math.abs((x0>>16) - (x1>>16));
		wy = Math.abs((y0>>16) - (y1>>16));
		dx = (x0 < x1) ? 1 : -1;
		dy = (y0 < y1) ? 1 : -1;
		x = x0;
		y = y0;

		if(wx >= wy){
			E = -wx;
			for(i = 0; i <= lc; i++){
				if(x == x1){
					break;
				}
				x += dx<<16;
				E += 2*wy;
				if(E >= 0){
					E -= 2*wx;
					y += dy<<16;
				}
			}
		}
		else{
			E = -wy;
			for(i = 0; i <= lc; i++){
				if(y == y1){
					break;
				}
				y += dy<<16;
				E += 2*wx;
				if(E >= 0){
					E -= 2*wy;
					x += dx<<16;
				}
			}
		}
		Bresenham_tmp[0] = x;
		Bresenham_tmp[1] = y;
	}

	private void drawSayo(Graphics gg){
		if(Bomb != 0){
			zakoInit();
			gg.setColor(Math.abs(Rnd.nextInt())%0xFF, Math.abs(Rnd.nextInt())%0xFF, Math.abs(Rnd.nextInt())%0xFF);
			gg.fillArc(((Sayo[0 ]-Worldxpos+0x000C0000)>>16)-(((0x3F-Bomb)<<1)>>1), ((Sayo[1 ]-Worldypos+0x000C0000)>>16)-(((0x3F-Bomb)<<1)>>1), (0x3F-Bomb)<<1, (0x3F-Bomb)<<1, 0, 360);
		}
		if((Scene != 410 )&&(Scene != 420 )){
			if(Oharai != 0xFFFFFFFF){
				Sayo[3 ] = ((int)(Oharai_pose>>(Sayo[2 ]<<3))&0xFF)+ 278 ;
			}
			else{
				Sayo[3 ] = Sayo[2 ]+ 288 ;
				if((AllKeyCode&0x0000F000) != 0){
					if(Sayo[4 ] < 4){
						Sayo[3 ] -= 8;
					}
					else if((Sayo[4 ] >= 8)&&(Sayo[4 ] < 12)){
						Sayo[3 ] += 8;
					}
					if((Sayo[3 ] < 280 )||(Sayo[3 ] > 303 )){
						Sayo[3 ] = 288 ;
					}
					Sayo[4 ]++;
					Sayo[4 ] &= 0x0000000F;
				}
			}
		}
		else{
			Sayo[2 ]++;
			Sayo[2 ] &= 0x00000007;
			Sayo[4 ]++;
			if(Sayo[4 ] < 32){
				Sayo[3 ] = ((int)(Oharai_pose>>(Sayo[2 ]<<3))&0xFF)+ 278 ;
			}
			else{
				Sayo[3 ] = 304 ;
			}
		}

		if(Scene==400||Scene==405)
			gg.drawImage(midlet.die_img, (Sayo[0 ]-Worldxpos)>>16, (Sayo[1 ]-Worldypos)>>16, gg.TOP|gg.LEFT);
		else
			gg.drawImage(CharImage[Sayo[3 ]], (Sayo[0 ]-Worldxpos)>>16, (Sayo[1 ]-Worldypos)>>16, gg.TOP|gg.LEFT);

		if(
			(Key_flg == 0)
				&&(1== to_sayo_Hit(Key_pos[Round_no]&0xFFFF0000,(Key_pos[Round_no]&0xFFFF0000)+0x000F0000,Key_pos[Round_no]<<16,(Key_pos[Round_no]<<16)+0x000F0000,1))
				){
			playSoundTrack(SOUND_25); //Ph[25]
			Key_flg ^= 0xFFFFFFFF;
			drawGate();
		}
		if(
			1 == to_sayo_Hit((Item[0 ]&0x3FFF0000),(Item[0 ]&0x3FFF0000)+0x000F0000,((Item[0 ]&0x0000FFFF)<<16),((Item[0 ]&0x0000FFFF)<<16)+0x000F0000,1)
				){
			Score += 1000;
			if (Score>999999) Score=999999;
			if(Item[1 ] != 338 ){
				playSoundTrack(SOUND_25); //Ph[25]
			}
			if(Item[1 ] == 331 ){
				if(Ofuda[4 ][0 ] < (28>>(Ofuda[4 ][1 ]>>1))){
					Ofuda[4 ][0 ] += 4;
				}
			}
			else if(Item[1 ] == 337 ){
				Score += 1000;
				if (Score>999999) Score=999999;
			}
			else if(Item[1 ] == 332 ){

				Ofuda[4 ][2 ] &= 0xFFFFFFF7;
				Ofuda[4 ][2 ] |= 0x00000008;
			}
			else if(Item[1 ] == 334 ){

				Ofuda[4 ][2 ] &= 0xFFFFFFFE;
				Ofuda[4 ][2 ] |= 0x00000002;
			}
			else if(Item[1 ] == 333 ){

				if(Ofuda[4 ][1 ] != 2){
					Ofuda[4 ][1 ] = 2;
					Ofuda[4 ][0 ] = Ofuda[4 ][0 ]>>1;
					for(int i=0; i<4; i++){
						Ofuda[2 ][i]	= 0xFFFFFFFF;
						Ofuda[3 ][i]		= Ofuda[4 ][0 ];
					}
				}
			}
			else if(Item[1 ] == 335 ){
				if((Sayo[5 ]&0x00000F00) == 0){
					Sayo[5 ] |= 0x00000100;
				}
				else if((Sayo[5 ]&0x0000F000) == 0){
					Sayo[5 ] |= 0x00001000;
				}
				else if((Sayo[5 ]&0x000F0000) == 0){
					Sayo[5 ] |= 0x00010000;
				}
				else if((Sayo[5 ]&0x00F00000) == 0){
					Sayo[5 ] |= 0x00100000;
				}
				else if((Sayo[5 ]&0x0F000000) == 0){
					Sayo[5 ] |= 0x01000000;
				}
				else if((Sayo[5 ]&0xF0000000) == 0){
					Sayo[5 ] |= 0x10000000;
				}
			}
			else if(Item[1 ] == 336 ){
				if((Sayo[5 ]&0x00000F00) == 0){
					Sayo[5 ] |= 0x00000200;
				}
				else if((Sayo[5 ]&0x0000F000) == 0){
					Sayo[5 ] |= 0x00002000;
				}
				else if((Sayo[5 ]&0x000F0000) == 0){
					Sayo[5 ] |= 0x00020000;
				}
				else if((Sayo[5 ]&0x00F00000) == 0){
					Sayo[5 ] |= 0x00200000;
				}
				else if((Sayo[5 ]&0x0F000000) == 0){
					Sayo[5 ] |= 0x02000000;
				}
				else if((Sayo[5 ]&0xF0000000) == 0){
					Sayo[5 ] |= 0x20000000;
				}
			}
			else if(Item[1 ] == 338 ){
				Sayo[5 ]++;
			}
			Item[0 ] = 0;
		}
	}


	private void quitGame() //when game over
	{
		recordJob(true);
		gameEffect.terminate();
		myCount2=0;
		gameOverRequest();
	}

	public synchronized void keyPressed(int keyCode)
	{
		if (Screen_Current == Screen_Logo)
		{
			logoScreenkeyPressed(keyCode);
		}
		else if (Screen_Current == Screen_Menu)
		{
			menuScreenkeyPressed(keyCode);
		}
		else if (Screen_Current == Screen_Setting)
		{
			settingScreenkeyPressed(keyCode);
		}
		else if (Screen_Current == Screen_Instructions)
		{
			instructionsScreenkeyPressed(keyCode);
		}
		else if (Screen_Current == Screen_About)
		{
			aboutScreenkeyPressed(keyCode);
		}
		else if(Screen_Current == Screen_Game)
		{
			gameScreenkeyPressed(keyCode);
		}
		else if(Screen_Current == Screen_Select)
		{
			selectScreenkeyPressed(keyCode);
		}

	}

	public void keyReleased(int keyCode) {

		if(Screen_Current==Screen_Game)
		{
			AllKeyCode = 0;
			Key_repeat_check =0 ;
		}

	}

	protected synchronized void keyHandler(){
		if((AllKeyCode&0x00000002) == 0x00000002){
			if(Scene == 20 ){
				if(Credit == 0){
					Scene = 800 ;
				}
				else{
					Scene = 42 ;
				}
			}
		}

		if(((Key_repeat_check&0x00000400) == 0)&&((AllKeyCode&0x00000400) == 0x00000400)){
			Key_repeat_check |= 0x00000400;

			if(Scene == 200 ){
				if(((Sayo[5 ]&0x00000F00)>>8) != 0){
					if(((Sayo[5 ]&0x00000F00)>>8) == 1){


						for(int i=0; i< 6 ; i++){
							Tama[0 ][i] = 0;
							Tama[1 ][i] = 0;
							Tama[2 ][i] = 0;
							Tama[3 ][i] = 0;
							Tama[4 ][i] = 0;
							Tama[5 ][i] = 0;
							Tama[6 ][i] = 0;
							Tama[7 ][i] = 0;
						}
						Time_stop = 0xFF;
					}
					if(((Sayo[5 ]&0x00000F00)>>8) == 2){
						Bomb = 0x3F;
					}
					Sayo[5 ] &= 0xFFFFF0FF;
					Sayo[5 ] = ((Sayo[5 ]>>12)<<8)+(Sayo[5 ]&0xFF);
					Rumuru_Scroll_quantity = 0;
				}
			}
		}
		else if((AllKeyCode&0x00000400) == 0x00000000){
			Key_repeat_check &= 0xFFFFFFF7;
		}

		if(((Key_repeat_check&0x00000800) == 0)&&((AllKeyCode&0x00000800) == 0x00000800)){
			Key_repeat_check |= 0x00000800;
			Auto_Rennsya_flg ^= 0x7FFFFFFF;
			if(midlet.getAutoShotFlag()){
				midlet.setAutoShotFlag(false);
			}
			else{
				midlet.setAutoShotFlag(true);
			}
		}
		else if((AllKeyCode&0x00000800) == 0x00000000){
			Key_repeat_check &= 0xFFFFF7FF;
		}

		if(((Key_repeat_check&0x00000400) == 0)&&((AllKeyCode&0x00000400) == 0x00000400)){
			Key_repeat_check |= 0x00000400;

			Mute_flg ^= 0x7FFFFFFF;
		}
		else if((AllKeyCode&0x00000400) == 0x00000000){
			Key_repeat_check &= 0xFFFFFBFF;
		}

		if((Scene >= 200 )&&(Scene <= 340 )){
			if((AllKeyCode&0x00005000) == 0x00005000){
				Sayo[2 ] = 5 ;
			}
			else if((AllKeyCode&0x0000C000) == 0x0000C000){
				Sayo[2 ] = 7 ;
			}
			else if((AllKeyCode&0x00003000) == 0x00003000){
				Sayo[2 ] = 3 ;
			}
			else if((AllKeyCode&0x0000A000) == 0x0000A000){
				Sayo[2 ] = 1 ;

			}

			if((AllKeyCode&0x00002000) == 0x00002000){
				if((AllKeyCode&0x0000F000) == 0x00002000){
					Sayo[2 ] = 2 ;
				}
				Sayo[0 ] -= Scroll_quantity-Rumuru_Scroll_quantity;

				if(
					(((MAP[(WORLDMAP[Round_no][((Sayo[1 ]+ 0x00100000 )>>16)/ 128 ][((Sayo[0 ]+ 0x00070000 )>>16)/ 120 ])&0x1F][((Math.abs((((Sayo[1 ]+ 0x00100000 )/(128 <<16))*(128 <<16))-(Sayo[1 ]+ 0x00100000 )))>>19)][((Math.abs((((Sayo[0 ]+ 0x00070000 )/(120 <<16))*(120 <<16))-(Sayo[0 ]+ 0x00070000 )))>>19)]&0x00000080)>>7) == 1)
						||(((MAP[(WORLDMAP[Round_no][((Sayo[1 ]+ 0x00170000 )>>16)/ 128 ][((Sayo[0 ]+ 0x00070000 )>>16)/ 120 ])&0x1F][((Math.abs((((Sayo[1 ]+ 0x00170000 )/(128 <<16))*(128 <<16))-(Sayo[1 ]+ 0x00170000 )))>>19)][((Math.abs((((Sayo[0 ]+ 0x00070000 )/(120 <<16))*(120 <<16))-(Sayo[0 ]+ 0x00070000 )))>>19)]&0x00000080)>>7) == 1)
						){
					Sayo[0 ] += Scroll_quantity-Rumuru_Scroll_quantity;
				}
				else if((!(((Sayo[0 ]-Worldxpos)>>16) > 60 ))&&(Scene == 200 )){
					if(((Sayo[0 ]-Worldxpos)>>16) < 60 ){
						LEFT_Scroll(0x00050000 );
					}
					else{
						LEFT_Scroll(0);
					}
				}
			}
			else if((AllKeyCode&0x00004000) == 0x00004000){
				if((AllKeyCode&0x0000F000) == 0x00004000){
					Sayo[2 ] = 6 ;
				}

				Sayo[0 ] += Scroll_quantity-Rumuru_Scroll_quantity;
				if(
					(((MAP[(WORLDMAP[Round_no][((Sayo[1 ]+ 0x00100000 )>>16)/ 128 ][((Sayo[0 ]+ 0x000F0000 )>>16)/ 120 ])&0x1F][((Math.abs((((Sayo[1 ]+ 0x00100000 )/(128 <<16))*(128 <<16))-(Sayo[1 ]+ 0x00100000 )))>>19)][((Math.abs((((Sayo[0 ]+ 0x000F0000 )/(120 <<16))*(120 <<16))-(Sayo[0 ]+ 0x000F0000 )))>>19)]&0x00000080)>>7) == 1)
						||(((MAP[(WORLDMAP[Round_no][((Sayo[1 ]+ 0x00170000 )>>16)/ 128 ][((Sayo[0 ]+ 0x000F0000 )>>16)/ 120 ])&0x1F][((Math.abs((((Sayo[1 ]+ 0x00170000 )/(128 <<16))*(128 <<16))-(Sayo[1 ]+ 0x00170000 )))>>19)][((Math.abs((((Sayo[0 ]+ 0x000F0000 )/(120 <<16))*(120 <<16))-(Sayo[0 ]+ 0x000F0000 )))>>19)]&0x00000080)>>7) == 1)
						){
					Sayo[0 ] -= Scroll_quantity-Rumuru_Scroll_quantity;
				}
				else if((!(((Sayo[0 ]-Worldxpos)>>16) < 36 ))&&(Scene == 200 )){
					if(((Sayo[0 ]-Worldxpos)>>16) > 36 ){
						RIGHT_Scroll(0x00050000 );
					}
					else{
						RIGHT_Scroll(0);
					}
				}
			}

			if((AllKeyCode&0x00001000) == 0x00001000){
				if((AllKeyCode&0x0000F000) == 0x00001000){
					Sayo[2 ] = 4 ;
				}

				Sayo[1 ] -= Scroll_quantity-Rumuru_Scroll_quantity;
				if(
					(((MAP[(WORLDMAP[Round_no][((Sayo[1 ]+ 0x00100000 )>>16)/ 128 ][((Sayo[0 ]+ 0x00070000 )>>16)/ 120 ])&0x1F][((Math.abs((((Sayo[1 ]+ 0x00100000 )/(128 <<16))*(128 <<16))-(Sayo[1 ]+ 0x00100000 )))>>19)][((Math.abs((((Sayo[0 ]+ 0x00070000 )/(120 <<16))*(120 <<16))-(Sayo[0 ]+ 0x00070000 )))>>19)]&0x00000080)>>7) == 1)
						||(((MAP[(WORLDMAP[Round_no][((Sayo[1 ]+ 0x00100000 )>>16)/ 128 ][((Sayo[0 ]+ 0x000F0000 )>>16)/ 120 ])&0x1F][((Math.abs((((Sayo[1 ]+ 0x00100000 )/(128 <<16))*(128 <<16))-(Sayo[1 ]+ 0x00100000 )))>>19)][((Math.abs((((Sayo[0 ]+ 0x000F0000 )/(120 <<16))*(120 <<16))-(Sayo[0 ]+ 0x000F0000 )))>>19)]&0x00000080)>>7) == 1)
						){
					Sayo[1 ] += Scroll_quantity-Rumuru_Scroll_quantity;
				}
				else if((!(((Sayo[1 ]-Worldypos)>>16) > 64 ))&&(Scene == 200 )){
					if(((Sayo[1 ]-Worldypos)>>16) < 64 ){
						UP_Scroll(0x00050000 );
					}
					else{
						UP_Scroll(0);
					}
				}
			}
			else if((AllKeyCode&0x00008000) == 0x00008000){
				if((AllKeyCode&0x0000F000) == 0x00008000){
					Sayo[2 ] = 0 ;
				}

				Sayo[1 ] += Scroll_quantity-Rumuru_Scroll_quantity;
				if(
					(((MAP[(WORLDMAP[Round_no][((Sayo[1 ]+ 0x00170000 )>>16)/ 128 ][((Sayo[0 ]+ 0x00070000 )>>16)/ 120 ])&0x1F][((Math.abs((((Sayo[1 ]+ 0x00170000 )/(128 <<16))*(128 <<16))-(Sayo[1 ]+ 0x00170000 )))>>19)][((Math.abs((((Sayo[0 ]+ 0x00070000 )/(120 <<16))*(120 <<16))-(Sayo[0 ]+ 0x00070000 )))>>19)]&0x00000080)>>7) == 1)
						||(((MAP[(WORLDMAP[Round_no][((Sayo[1 ]+ 0x00170000 )>>16)/ 128 ][((Sayo[0 ]+ 0x000F0000 )>>16)/ 120 ])&0x1F][((Math.abs((((Sayo[1 ]+ 0x00170000 )/(128 <<16))*(128 <<16))-(Sayo[1 ]+ 0x00170000 )))>>19)][((Math.abs((((Sayo[0 ]+ 0x000F0000 )/(120 <<16))*(120 <<16))-(Sayo[0 ]+ 0x000F0000 )))>>19)]&0x00000080)>>7) == 1)
						){
					Sayo[1 ] -= Scroll_quantity-Rumuru_Scroll_quantity;
				}
				else if((!(((Sayo[1 ]-Worldypos)>>16) < 30 ))&&(Scene == 200 )){
					if(((Sayo[1 ]-Worldypos)>>16) > 30 ){
						DOWN_Scroll(0x00050000 );
					}
					else{
						DOWN_Scroll(0);
					}
				}
			}
			Scroll_quantity = 0x00020000;
		}
	}

	private void LEFT_Scroll(int force_scroll_quantity){
		if((((Worldypos>>16)% 128 ) == 0)&&(((WORLDMAP[Round_no][(Worldypos>>16)/ 128 ][((Worldxpos-(Scroll_quantity-Rumuru_Scroll_quantity+force_scroll_quantity))>>16)/ 120 ])&0x1F) != 0)){
			int oldWorldxpos = Worldxpos;
			Worldxpos -= (Scroll_quantity-Rumuru_Scroll_quantity+force_scroll_quantity);
			Nowscreen = (WORLDMAP[Round_no][(Worldypos>>16)/ 128 ][(Worldxpos>>16)/ 120 ])>>5;


			if(((Math.abs((((Worldxpos/(120 <<16))*(120 <<16))-Worldxpos))>>16)>>3)!=((Math.abs((((oldWorldxpos/(120 <<16))*(120 <<16))-oldWorldxpos))>>16)>>3)){
				for(int y=0; y< 128 ; y+=8){
					SCg[Nowscreen].drawImage(CharImage[(MAP[(WORLDMAP[Round_no][(Worldypos>>16)/ 128 ][(Worldxpos>>16)/ 120 ])&0x1F][y>>3][(Math.abs((((Worldxpos/(120 <<16))*(120 <<16))-Worldxpos))>>16)>>3]&0x0000007F)], ((Math.abs((((Worldxpos/(120 <<16))*(120 <<16))-Worldxpos))>>16)>>3)<<3, y, SCg[Nowscreen].TOP|SCg[Nowscreen].LEFT);
					int etc;
					if((etc = ((MAP[(WORLDMAP[Round_no][(Worldypos>>16)/ 128 ][(Worldxpos>>16)/ 120 ])&0x1F][y>>3][(Math.abs((((Worldxpos/(120 <<16))*(120 <<16))-Worldxpos))>>16)>>3]&0x00000F00)>>8)) > 0){
						MAP[(WORLDMAP[Round_no][(Worldypos>>16)/ 128 ][(Worldxpos>>16)/ 120 ])&0x1F][y>>3][(Math.abs((((Worldxpos/(120 <<16))*(120 <<16))-Worldxpos))>>16)>>3] &= 0xF0FF;
						setZako2(-((Zako_hit_size[etc-1]&0xFF000000)>>8), y<<16, 120 <<16, y<<16, 6, etc-1);
					}
				}
			}
		}
		else if((((Worldypos>>16)% 128 ) == 0)&&(((WORLDMAP[Round_no][(Worldypos>>16)/ 128 ][((Worldxpos-(Scroll_quantity-Rumuru_Scroll_quantity+force_scroll_quantity))>>16)/ 120 ])&0x1F) == 0)){
			Worldxpos = (((Worldxpos>>16)>>3)<<3)<<16;
			Nowscreen = (WORLDMAP[Round_no][(Worldypos>>16)/ 128 ][(Worldxpos>>16)/ 120 ])>>5;
		}
	}

	private void RIGHT_Scroll(int force_scroll_quantity){
		if((((Worldypos>>16)% 128 ) == 0)&&(((WORLDMAP[Round_no][(Worldypos>>16)/ 128 ][(((Worldxpos+(Scroll_quantity-Rumuru_Scroll_quantity+force_scroll_quantity))>>16)+ 120 )/ 120 ])&0x1F) != 0)){

			int oldWorldxpos = Worldxpos;
			Worldxpos += (Scroll_quantity-Rumuru_Scroll_quantity+force_scroll_quantity);

			Nowscreen = (WORLDMAP[Round_no][(Worldypos>>16)/ 128 ][(Worldxpos>>16)/ 120 ])>>5;

			if(
				(((oldWorldxpos>>16)% 120 ) == 0)||
					(((Math.abs((((Worldxpos/(120 <<16))*(120 <<16))-Worldxpos))>>16)>>3)!=((Math.abs((((oldWorldxpos/(120 <<16))*(120 <<16))-oldWorldxpos))>>16)>>3))
					){
				for(int y=0; y< 128 ; y+=8){
					SCg[Nowscreen^1].drawImage(CharImage[(MAP[(WORLDMAP[Round_no][(Worldypos>>16)/ 128 ][((Worldxpos>>16)/ 120 )+1])&0x1F][y>>3][(Math.abs((((Worldxpos/(120 <<16))*(120 <<16))-Worldxpos))>>16)>>3]&0x0000007F)], ((Math.abs((((Worldxpos/(120 <<16))*(120 <<16))-Worldxpos))>>16)>>3)<<3, y, SCg[Nowscreen^1].TOP|SCg[Nowscreen^1].LEFT);

					int etc;
					if((etc = ((MAP[(WORLDMAP[Round_no][(Worldypos>>16)/ 128 ][((Worldxpos>>16)/ 120 )+1])&0x1F][y>>3][(Math.abs((((Worldxpos/(120 <<16))*(120 <<16))-Worldxpos))>>16)>>3]&0x00000F00)>>8)) > 0){

						MAP[(WORLDMAP[Round_no][(Worldypos>>16)/ 128 ][((Worldxpos>>16)/ 120 )+1])&0x1F][y>>3][(Math.abs((((Worldxpos/(120 <<16))*(120 <<16))-Worldxpos))>>16)>>3] &= 0xF0FF;
						setZako2(120 <<16, y<<16, -(120 <<16), y<<16, 2, etc-1);
					}
				}
			}
		}
		else if((((Worldypos>>16)% 128 ) == 0)&&(((WORLDMAP[Round_no][(Worldypos>>16)/ 128 ][(((Worldxpos+(Scroll_quantity-Rumuru_Scroll_quantity+force_scroll_quantity))>>16)+ 120 )/ 120 ])&0x1F) == 0)){
			Worldxpos = ((((Worldxpos+(Scroll_quantity-Rumuru_Scroll_quantity+force_scroll_quantity))>>16)>>3)<<3)<<16;
			Nowscreen = (WORLDMAP[Round_no][(Worldypos>>16)/ 128 ][(Worldxpos>>16)/ 120 ])>>5;
		}
	}

	private void UP_Scroll(int force_scroll_quantity){
		if((((Worldxpos>>16)% 120 ) == 0)&&(((WORLDMAP[Round_no][((Worldypos-(Scroll_quantity-Rumuru_Scroll_quantity+force_scroll_quantity))>>16)/ 128 ][(Worldxpos>>16)/ 120 ])&0x1F) != 0)){

			int oldWorldypos = Worldypos;
			Worldypos -= (Scroll_quantity-Rumuru_Scroll_quantity+force_scroll_quantity);
			Nowscreen = (WORLDMAP[Round_no][(Worldypos>>16)/ 128 ][(Worldxpos>>16)/ 120 ])>>5;
			if(((Math.abs((((Worldypos/(128 <<16))*(128 <<16))-Worldypos))>>16)>>3)!=((Math.abs((((oldWorldypos/(128 <<16))*(128 <<16))-oldWorldypos))>>16)>>3)){
				for(int x=0; x< 120 ; x+=8){
					SCg[Nowscreen].drawImage(CharImage[(MAP[(WORLDMAP[Round_no][(Worldypos>>16)/ 128 ][(Worldxpos>>16)/ 120 ])&0x1F][(Math.abs((((Worldypos/(128 <<16))*(128 <<16))-Worldypos))>>16)>>3][x>>3]&0x0000007F)], x, ((Math.abs((((Worldypos/(128 <<16))*(128 <<16))-Worldypos))>>16)>>3)<<3, SCg[Nowscreen].TOP|SCg[Nowscreen].LEFT);
					int etc;
					if((etc = ((MAP[(WORLDMAP[Round_no][(Worldypos>>16)/ 128 ][(Worldxpos>>16)/ 120 ])&0x1F][(Math.abs((((Worldypos/(128 <<16))*(128 <<16))-Worldypos))>>16)>>3][x>>3]&0x00000F00)>>8)) > 0){
						MAP[(WORLDMAP[Round_no][(Worldypos>>16)/ 128 ][(Worldxpos>>16)/ 120 ])&0x1F][(Math.abs((((Worldypos/(128 <<16))*(128 <<16))-Worldypos))>>16)>>3][x>>3] &= 0xF0FF;
						setZako2(x<<16, -(Zako_hit_size[etc-1]&0x00FF0000), x<<16, 128 <<16, 0, etc-1);
					}
				}
			}
		}
		else if((((Worldxpos>>16)% 120 ) == 0)&&(((WORLDMAP[Round_no][((Worldypos-(Scroll_quantity-Rumuru_Scroll_quantity+force_scroll_quantity))>>16)/ 128 ][(Worldxpos>>16)/ 120 ])&0x1F) == 0)){
			Worldypos = (((Worldypos>>16)>>3)<<3)<<16;
			Nowscreen = (WORLDMAP[Round_no][(Worldypos>>16)/ 128 ][(Worldxpos>>16)/ 120 ])>>5;
		}
	}

	private void DOWN_Scroll(int force_scroll_quantity){
		if((((Worldxpos>>16)% 120 ) == 0)&&(((WORLDMAP[Round_no][(((Worldypos+(Scroll_quantity-Rumuru_Scroll_quantity+force_scroll_quantity))>>16)+ 128 )/ 128 ][(Worldxpos>>16)/ 120 ])&0x1F) != 0)){
			int oldWorldypos = Worldypos;
			Worldypos += (Scroll_quantity-Rumuru_Scroll_quantity+force_scroll_quantity);

			Nowscreen = (WORLDMAP[Round_no][(Worldypos>>16)/ 128 ][(Worldxpos>>16)/ 120 ])>>5;

			if(
				(((oldWorldypos>>16)% 128 ) == 0)||
					(((Math.abs((((Worldypos/(128 <<16))*(128 <<16))-Worldypos))>>16)>>3)!=((Math.abs((((oldWorldypos/(128 <<16))*(128 <<16))-oldWorldypos))>>16)>>3))
					){
				for(int x=0; x< 120 ; x+=8){
					SCg[Nowscreen^1].drawImage(CharImage[(MAP[(WORLDMAP[Round_no][((Worldypos>>16)/ 128 )+1][(Worldxpos>>16)/ 120 ])&0x1F][(Math.abs((((Worldypos/(128 <<16))*(128 <<16))-Worldypos))>>16)>>3][x>>3]&0x0000007F)], x, ((Math.abs((((Worldypos/(128 <<16))*(128 <<16))-Worldypos))>>16)>>3)<<3, SCg[Nowscreen^1].TOP|SCg[Nowscreen^1].LEFT);

					int etc;

					if((etc = ((MAP[(WORLDMAP[Round_no][((Worldypos>>16)/ 128 )+1][(Worldxpos>>16)/ 120 ])&0x1F][(Math.abs((((Worldypos/(128 <<16))*(128 <<16))-Worldypos))>>16)>>3][x>>3]&0x00000F00)>>8)) > 0){

						MAP[(WORLDMAP[Round_no][((Worldypos>>16)/ 128 )+1][(Worldxpos>>16)/ 120 ])&0x1F][(Math.abs((((Worldypos/(128 <<16))*(128 <<16))-Worldypos))>>16)>>3][x>>3] &= 0xF0FF;

						setZako2(x<<16, 128 <<16, x<<16, -(128 <<16), 4, etc-1);
					}
				}
			}
		}
		else if((((Worldxpos>>16)% 120 ) == 0)&&(((WORLDMAP[Round_no][(((Worldypos+(Scroll_quantity-Rumuru_Scroll_quantity+force_scroll_quantity))>>16)+ 128 )/ 128 ][(Worldxpos>>16)/ 120 ])&0x1F) == 0)){
			Worldypos = ((((Worldypos+(Scroll_quantity-Rumuru_Scroll_quantity+force_scroll_quantity))>>16)>>3)<<3)<<16;
			Nowscreen = (WORLDMAP[Round_no][(Worldypos>>16)/ 128 ][(Worldxpos>>16)/ 120 ])>>5;
		}
	}

	private void drawShadowString(Graphics gg, String str, int x, int y, int anchor, int r, int g, int b){
		gg.setColor(r>>1, g>>1, b>>1);
		gg.drawString(str, x+1, y+1, anchor);
		gg.setColor(r, g, b);
		gg.drawString(str, x, y, anchor);
	}
	public void run(){
		Thread currentThread = Thread.currentThread();
		try
		{
			// This ends when animationThread is set to null, or when
			// it is subsequently set to a new Thread.  Either way, the
			// current thread should terminate.
			while ( currentThread==animationThread)
			{
				Runtime runtime = Runtime.getRuntime();
				if(runtime.freeMemory() < 5000L) runtime.gc();
				long startTime = System.currentTimeMillis();
				if (animationThread!=null){

				if(Screen_Current == Screen_Game)
				{
					keyHandler();
					repaint();
					serviceRepaints();
				}
				else if (Screen_Current == Screen_Splash)
				{
					if(isRunning)
					{
						repaint();
						serviceRepaints();
						if (refreshRate <= 20) {
							refreshRate += 1;
						} else
						{
							logoScreenRequest();
							imgSplash = null;
						}
					}
				}
			}
				long elapsedTime = System.currentTimeMillis() - startTime;
				if (elapsedTime < MILLIS_PER_UPDATE)
				{
					synchronized(this)
					{
						wait(MILLIS_PER_UPDATE - elapsedTime);
					}
				}
				else
				{
					currentThread.yield();
				}
			}
		}
		catch(InterruptedException e) {}

	}


	private  void drawBackground( Graphics g ){
		g.setClip(0,0,176,208);
		g.setColor(0);
		g.fillRect( 0,0,176,208);
		g.drawImage( topI,0,0,0 );
		g.drawImage( leftI,0,8+44,0);
		g.drawImage( rightI, 20+128,8+44,0 );
	}

	private  void drawTitleBackground( Graphics g ){
		g.setClip(0,0,176,208);
		g.setColor(0);
		g.fillRect( 0,0,176,208);
		g.drawImage( topI,0,0,0 );
		g.drawImage( leftI,0,8+44,0);
		g.drawImage( rightI, 20+128,8+44,0 );
	}

	protected void paint(Graphics g){

		if (Screen_Current == Screen_Splash)
		{
			splashScreenPaint(g);
		}
		else if (Screen_Current == Screen_Logo)
		{
			logoScreenPaint(g);
		}
		else if (Screen_Current == Screen_Menu)
		{
			menuScreenPaint(g);
		}
		else if (Screen_Current == Screen_Setting)
		{
			settingScreenPaint(g);
		}
		else if (Screen_Current == Screen_Instructions)
		{
			instructionsScreenPaint(g);
		}
		else if (Screen_Current == Screen_About)
		{
			aboutScreenPaint(g);
		}
		else if(Screen_Current == Screen_Game)
		{
			gameScreenPaint(g);
		}
		else if(Screen_Current == Screen_Select)
		{
			selectScreenPaint(g);
		}
	}

	private void staff_roll_paint(Graphics g, int page){
		       going_count++;
				drawEnding(g);
				playEndingMusic();
	}




	class CreateImageThread extends Thread{
		Graphics GG;
		private int Sc, Of;
		private short[] Ci;
		private String Na;
		public CreateImageThread(Graphics gg, int sc, short [] char_image_no, String name, int offset){
			GG = gg;
			Sc = sc;
			Ci = char_image_no;
			Na = name;
			Of = offset;
			}
		public void run(){
			try{
				int size, len;
				int fnum;
				int[] fsize;
				byte[] tmp;

				DataInputStream din;
				ByteArrayOutputStream byteArr = new ByteArrayOutputStream();
				Percent=0;
				InputStream in = getClass().getResourceAsStream(Na);
				din = new DataInputStream(in);
				size = din.available();
				if(0 == size){
					size = 256;
				}
				tmp = new byte[size];
				fnum = din.readInt();
				fsize = new int[fnum];
				for(int i=0; i<fnum; i++){
					fsize[i] = din.readInt();
				}
				while((len = din.read(tmp)) >= 0){
					byteArr.write(tmp,0,len);
				}
				din.close();
				for(int i=0; i<=Ci.length; i++){
					System.gc();
					if(i != Ci.length){
						CharImage[Ci[i]] = Image.createImage(byteArr.toByteArray(), fsize[Ci[i]-Of]>>12, fsize[Ci[i]-Of]&0x00000FFF);
						Percent = ((10000/Ci.length)*((i+1)*100)/10000);
					}
					else{
						Percent = 100;
					}
					updateLoading(Sc,Percent);
					repaint();
				}
				byteArr.close();

				Scene = Sc;
			}
			catch(Exception e){
			}
		}
	}


	public void setSound(boolean soundFlag)
	{
		this.soundFlag = soundFlag;
	}

	public void setAutoShot(boolean autoFlag)
	{
		Auto_Rennsya_flg = 0;
		if(autoFlag)
			Auto_Rennsya_flg ^= 0x7FFFFFFF;
		else
			Auto_Rennsya_flg  = 0;
	}

	public void setVibration(boolean vibrationFlag){
		this.vibrationFlag = vibrationFlag;
	}

	public void setRound(int i){
		Round_no = i;
		Scene = 0;
	}

	private void pauseGame()
	{
		pauseGameRequest();
	}

	protected void hideNotify()
	{
		if(Screen_Current == Screen_Game)
		{
			gameEffect.pause();
			initialize(MENU_CURRENT);
			Screen_Current = Screen_Menu;
			repaint();
		}
		else if(Screen_Current == Screen_Splash)
		{
			gameEffect.pause();
			isRunning = false;
			Screen_Current = Screen_Splash;
			repaint();
		}
		else if(Screen_Current == Screen_Logo )
		{
			gameEffect.pause();
			Screen_Current = Screen_Logo;
			repaint();
		}
	animationThread = null;
	}

	protected void showNotify()
	{
		if(Screen_Current == Screen_Game||Screen_Current == Screen_Splash||Screen_Current == Screen_Logo)
		{
			if(Screen_Current == Screen_Game)
			{
				initialize(MENU_CURRENT);
				Screen_Current = Screen_Menu;
					if(animationThread == null)
					{
					animationThread = new Thread(this);
					animationThread.start();
					}
			}
			else if(Screen_Current == Screen_Splash)
			{
				Screen_Current = Screen_Splash;
				isRunning = true ;
			}
			else
			{
				Screen_Current = Screen_Logo;
			}
			gameEffect.resume();
			repaint();
			}
	}

	private void gameOver()
	{
		gameoverFlag = true;
	}

	private void stopSoundTrack(int type)
	{
	}


	private void playSoundTrack(int type)
	{
		if(soundFlag){
			switch(type)
			{
			case SOUND_2:
				gameEffect.playGodCaptured(); //god captured
				break;
			case SOUND_3:
				gameEffect.playGateClosed();//gate closed
				break;
			case SOUND_4:
				gameEffect.playBossBGM();//boss_bgm
				break;
			case SOUND_8:
				gameEffect.playGameOver(); //game over
				break;
			case SOUND_9:
				gameEffect.playClear(); //clear
				break;
			case SOUND_12:
				gameEffect.playShoot(); //shoot
				break;
			case SOUND_13:
				gameEffect.playPickup(); //pick up items
				break;
			case SOUND_14:
				gameEffect.playEnemyDie(); //enemy_die
				break;
			case SOUND_15: //SOUND_15 = SOUND_3
				gameEffect.playGateClosed(); //gate closed
				break;
			case SOUND_16: //SOUND_16 = SOUND_4
				gameEffect.playBossBGM();//boss bgm
				break;
			case SOUND_18:
				gameEffect.playBossShoot(); //boss shoot
				break;
			case SOUND_19: //SOUND_19 = SOUND_4
				gameEffect.playBossBGM(); //boss_bgm
				break;
			case SOUND_20: //SOUND_20 = SOUND_12
				gameEffect.playShoot(); //shoot
				break;
			case SOUND_23: //SOUND_23 = SOUND_13
				gameEffect.playPickup(); //pickup items
				break;
			case SOUND_24:
				gameEffect.playDie(); //die
				break;
			case SOUND_25: //SOUND_25 = SOUND_18
				gameEffect.playBossShoot(); //boss shoot
				break;
			}
		}
	}

	public void logoScreenRequest()
	{
		Screen_Current = Screen_Logo;
		gameEffect=midlet.gameEffects;
		gameEffect.resume();
		if(midlet.getSoundFlag())
        gameEffect.playTitleScrBGM();
		repaint();
	}

	public void initialize(int menuType)
	{
		this.menuType = menuType;
		Menu_Screen_Count = 0;
		if(menuType == MENU_CONTINUE )
		ContinueFlag = true ;
		else
		ContinueFlag = false;
	}

	public void menuScreenkeyPressed(int keyCode)
	{
		int j =0 ;
		if ( keyCode == SOFT_KEY2 ) {
			logoScreenRequest();
		}
		else if ( keyCode == SOFT_KEY1 ) {
			if ( menuType == MENU_CONTINUE ) {
				switch(Menu_Screen_Count){
				case 0:
					continueGameRequest();
					break;
				case 1:
					newGameRequest();
					break;
				case 2:
					missionSelectRequest();
					break;
				case 3:
					settingsRequest();
					break;
				case 4:
					instructionsRequest();
					break;
				case 5:
					aboutRequest();
					break;
				case 6:
					//alive = false ;
				animationThread = null;
					midlet.exitRequest();
					break;
				default:
					break;
				} //switch
			} else {
				switch(Menu_Screen_Count){
				case 0:
					newGameRequest();
					break;
				case 1:
					missionSelectRequest();
					break;
				case 2:
					settingsRequest();
					break;
				case 3:
					instructionsRequest();
					break;
				case 4:
					aboutRequest();
					break;
				case 5:
					//alive = false ;
				animationThread = null;
					midlet.exitRequest();
					break;
				default:
					break;
				} //switch
			} //if ( menuType == MENU_CONTINUE )
		}
		else
		{
			switch(keyCode)
			{
			case 50: // '2'
				Menu_Screen_Count--;
				if ( menuType == MENU_CONTINUE ) {
					if (Menu_Screen_Count <0)
						Menu_Screen_Count= 6 ;
				}
				else
				{
					if (Menu_Screen_Count <0)
						Menu_Screen_Count= 5 ;
				}
				repaint();
				serviceRepaints();
				break;
			case 56: // '8'
				Menu_Screen_Count++;
				if ( menuType == MENU_CONTINUE ) {
					if(Menu_Screen_Count>6)
						Menu_Screen_Count=0;
				}
				else
				{
					if(Menu_Screen_Count>5)
						Menu_Screen_Count=0;
				}
				repaint();
				serviceRepaints();
				break;

			case KEY_UP_ARROW : // '\006'
				Menu_Screen_Count--;
				if ( menuType == MENU_CONTINUE ) {
					if (Menu_Screen_Count <0)
						Menu_Screen_Count= 6 ;
				}
				else
				{
					if (Menu_Screen_Count <0)
						Menu_Screen_Count= 5 ;
				}
				repaint();
				serviceRepaints();
				break;

			case 53 : // '\b'

				if ( menuType == MENU_CONTINUE ) {
					switch(Menu_Screen_Count){
					case 0:
						continueGameRequest();
						break;
					case 1:
						newGameRequest();
						break;
					case 2:
						missionSelectRequest();
						break;
					case 3:
						settingsRequest();
						break;
					case 4:
						instructionsRequest();
						break;
					case 5:
						aboutRequest();
						break;
					case 6:
						//alive = false ;
				animationThread = null;
						midlet.exitRequest();
						break;
					default:
						break;
					} //switch
				} else {
					switch(Menu_Screen_Count){
					case 0:
						newGameRequest();
						break;
					case 1:
						missionSelectRequest();
						break;
					case 2:
						settingsRequest();
						break;
					case 3:
						instructionsRequest();
						break;
					case 4:
						aboutRequest();
						break;
					case 5:
					//alive = false ;
				animationThread = null;
						midlet.exitRequest();
						break;
					default:
						break;
					} //switch
				} //if ( menuType == MENU_CONTINUE )
				Menu_Screen_Count=0;
				break;

			case KEY_DOWN_ARROW : // '\006'
				Menu_Screen_Count++;
				if ( menuType == MENU_CONTINUE ) {
					if(Menu_Screen_Count>6)
						Menu_Screen_Count=0;
				}
				else
				{
					if(Menu_Screen_Count>5)
						Menu_Screen_Count=0;
				}
				repaint();
				serviceRepaints();
				break;
			default:

				try
				{
					j = getGameAction(keyCode);
				}
				catch(Exception exception) { }
				switch(j)
				{
				case FIRE : // '\b'
					if ( menuType == MENU_CONTINUE ) {
						switch(Menu_Screen_Count){
						case 0:
							continueGameRequest();
							break;
						case 1:
							newGameRequest();
							break;
						case 2:
							missionSelectRequest();
							break;
						case 3:
							settingsRequest();
							break;
						case 4:
							instructionsRequest();
							break;
						case 5:
							aboutRequest();
							break;
						case 6:
							//alive = false ;
				animationThread = null;
							midlet.exitRequest();
							break;
						default:
							break;
						} //switch
					} else {
						switch(Menu_Screen_Count){
						case 0:
							newGameRequest();
							break;
						case 1:
							missionSelectRequest();
							break;
						case 2:
							settingsRequest();
							break;
						case 3:
							instructionsRequest();
							break;
						case 4:
							aboutRequest();
							break;
						case 5:
							//alive = false ;
				animationThread = null;
							midlet.exitRequest();
							break;
						default:
							break;
						} //switch
					} //if ( menuType == MENU_CONTINUE )
					Menu_Screen_Count=0;
					break;
				}
				break;

			}

		}
	}

	public void logoScreenkeyPressed(int keyCode) {
		switch (keyCode)
		{
		case FullCanvas.KEY_SOFTKEY1:
			logoScreenOptionsRequest();
			break;
		case FullCanvas.KEY_SOFTKEY2:
			//alive = false ;
				animationThread = null;
			midlet.logoScreenExitRequest();
			break;
		}
	}



	public void logoScreenPaint(Graphics g) {
	   drawTitleBackground(g);
		g.drawImage( gameGod, scrW/2, 28+gameGod.getHeight()/2+22 , (Graphics.HCENTER | Graphics.VCENTER) );
		int h = g.getFont().getHeight();
		g.drawImage( gameTaito, scrW/2, 180-gameTaito.getHeight()/2-2, (Graphics.HCENTER | Graphics.VCENTER) );
		g.setColor(255,255,255);
		g.fillRect( 0, 208-20,  scrW, 20);
		g.setColor(0,0,0);
		g.setFont(fGame);
		g.drawString("Options", 2 , 208-18, Graphics.TOP|Graphics.LEFT);
		g.drawString("Exit", scrW-fGame.stringWidth("Exit"), 208-18, Graphics.TOP|Graphics.LEFT);
		g.setFont(fTime);
		g.setColor( 0xFF, 0xFF, 0xFF);
		g.drawString( "High score: "+midlet.getHighScore() ,  scrW/2 , scrH/2+23, Graphics.HCENTER | Graphics.BASELINE);
	}


	public void menuScreenPaint(Graphics g)
	{
		g.setColor(0xFF, 0xFF, 0xFF);
		g.fillRect(0, 0, 176, 208);

		g.setColor(0x00, 0x00, 0xFF);
		g.fillRect(0,((20*Menu_Screen_Count) + 40), 176, 17);

		g.setFont(fGame);
		g.setColor(0x00, 0x00, 0x00);
		g.drawImage(gameIcon, 20, 20, g.VCENTER | g.HCENTER);

		g.drawString("KikiKaikai", getWidth()>>1, 15, g.TOP|g.HCENTER);
		g.drawLine(0, 35, 176, 35);

		if(ContinueFlag == true){
			g.drawString("Continue", 3, 40, g.TOP | g.LEFT);
			g.drawString("New game", 3, 60, g.TOP | g.LEFT);
			g.drawString("Select scene", 3, 80, g.TOP | g.LEFT);
			g.drawString("Settings", 3, 100, g.TOP | g.LEFT);
			g.drawString("Instructions", 3, 120, g.TOP | g.LEFT);
			g.drawString("About", 3, 140, g.TOP | g.LEFT);
			g.drawString("Exit", 3, 160, g.TOP | g.LEFT);
		}
		else if(ContinueFlag == false){
			g.drawString("New game", 3, 40, g.TOP | g.LEFT);
			g.drawString("Select scene", 3, 60, g.TOP | g.LEFT);
			g.drawString("Settings", 3, 80, g.TOP | g.LEFT);
			g.drawString("Instructions", 3, 100, g.TOP | g.LEFT);
			g.drawString("About", 3, 120, g.TOP | g.LEFT);
			g.drawString("Exit", 3, 140, g.TOP | g.LEFT);
		}
		g.setFont(fGame);
		g.drawString("Select",2, 190, g.TOP|g.LEFT);
		g.drawString("Back", 174-fGame.stringWidth("Back"),190,g.TOP|g.LEFT);
	}

	public void gameScreenPaint(Graphics g)
	{
		try{
			drawBackground(g);
			g.setColor(255,255,255);
			g.fillRect( 0, 208-20,  176 , 20);
			g.setColor(0,0,0);
			g.setFont(fGame);
			g.drawString(midlet.OPTIONS, 2 , 208-18, Graphics.TOP|Graphics.LEFT);
			g.drawString(midlet.QUIT, 176 -fGame.stringWidth(midlet.QUIT), 208-18, Graphics.TOP|Graphics.LEFT);

			g.translate((GETWIDTH- 120 )>>1, ((144 - 128 )>>1)+44);
			g.setClip(0, 0, 120 , 128 );

			if((Scene >= 200 )&&(Scene <= 420 )){
				World_timer++;
				World_timer &= 0x7FFFFFFF;

				if(Time_stop > 0){
					Time_stop--;
				}
				if(Bomb > 0){
					Bomb--;
				}
				if(Scene <= 310 ){
					int check = 0;
					check |= ((MAP[(WORLDMAP[Round_no][((Sayo[1 ]+ 0x00100000 )>>16)/ 128 ][((Sayo[0 ]+ 0x00070000 )>>16)/ 120 ])&0x1F][((Math.abs((((Sayo[1 ]+ 0x00100000 )/(128 <<16))*(128 <<16))-(Sayo[1 ]+ 0x00100000 )))>>19)][((Math.abs((((Sayo[0 ]+ 0x00070000 )/(120 <<16))*(120 <<16))-(Sayo[0 ]+ 0x00070000 )))>>19)]>>12)&0x0000000F);
					check |= ((MAP[(WORLDMAP[Round_no][((Sayo[1 ]+ 0x00100000 )>>16)/ 128 ][((Sayo[0 ]+ 0x000F0000 )>>16)/ 120 ])&0x1F][((Math.abs((((Sayo[1 ]+ 0x00100000 )/(128 <<16))*(128 <<16))-(Sayo[1 ]+ 0x00100000 )))>>19)][((Math.abs((((Sayo[0 ]+ 0x000F0000 )/(120 <<16))*(120 <<16))-(Sayo[0 ]+ 0x000F0000 )))>>19)]>>12)&0x0000000F);
					check |= ((MAP[(WORLDMAP[Round_no][((Sayo[1 ]+ 0x00170000 )>>16)/ 128 ][((Sayo[0 ]+ 0x00070000 )>>16)/ 120 ])&0x1F][((Math.abs((((Sayo[1 ]+ 0x00170000 )/(128 <<16))*(128 <<16))-(Sayo[1 ]+ 0x00170000 )))>>19)][((Math.abs((((Sayo[0 ]+ 0x00070000 )/(120 <<16))*(120 <<16))-(Sayo[0 ]+ 0x00070000 )))>>19)]>>12)&0x0000000F);
					check |= ((MAP[(WORLDMAP[Round_no][((Sayo[1 ]+ 0x00170000 )>>16)/ 128 ][((Sayo[0 ]+ 0x000F0000 )>>16)/ 120 ])&0x1F][((Math.abs((((Sayo[1 ]+ 0x00170000 )/(128 <<16))*(128 <<16))-(Sayo[1 ]+ 0x00170000 )))>>19)][((Math.abs((((Sayo[0 ]+ 0x000F0000 )/(120 <<16))*(120 <<16))-(Sayo[0 ]+ 0x000F0000 )))>>19)]>>12)&0x0000000F);

					if((check&0x00000007)==1){
						LEFT_Scroll(0x00050000 );
						LEFT_Scroll(0x00050000 );
					}
					else if((check&0x00000007)==2){
						RIGHT_Scroll(0x00050000 );
						RIGHT_Scroll(0x00050000 );
					}
					else if((check&0x00000007)==3){
						UP_Scroll(0x00050000 );
						UP_Scroll(0x00050000 );
					}
					else if((check&0x00000007)==4){
						DOWN_Scroll(0x00050000 );
						DOWN_Scroll(0x00050000 );
					}
					else if((check&0x00000007)==5){
						Rumuru_Scroll_quantity = 0;

						for(int i= 6 ; i< 12 ; i++){
							if(((Zako[9 ][i]&0x0000FF00) != 0)&&((Zako[0 ][i]&0x00FF0000) == 0x00FF0000)&&(Zako[5 ][i] == 151 )){
								Zako[0 ][i] = (((Oharai_direction_offset[Sayo[2 ]]>>((Oharai>>1)<<2))&0x0000000F)%8)+1;
								playSoundTrack(SOUND_14);
							}
						}
					}
					if(((check&0x00000008)==8)&&(Scene == 200 )){
						System.gc();
						playSoundTrack(SOUND_3);

						Scene = 300 ;
						World_timer = 0;
						Boss[6 ] = 0;
						Boss[9 ] = 0;
						Rumuru_Scroll_quantity = 0;
						zakoInit();
						drawGate();
					}
				}
				g.drawImage(SCbuf[Nowscreen], (((Worldxpos/(120 <<16))*(120 <<16))-Worldxpos)>>16, (((Worldypos/(128 <<16))*(128 <<16))-Worldypos)>>16, g.TOP|g.LEFT);
				if((((Worldxpos>>16)% 120 ) == 0)&&(((Worldypos>>16)% 128 ) != 0)){
					g.drawImage(SCbuf[Nowscreen^1], ((((Worldxpos/(120 <<16))*(120 <<16))-Worldxpos)>>16), ((((Worldypos/(128 <<16))*(128 <<16))-Worldypos)>>16)+ 128 , g.TOP|g.LEFT);
				}
				if((((Worldypos>>16)% 128 ) == 0)&&(((Worldxpos>>16)% 120 ) != 0)){
					g.drawImage(SCbuf[Nowscreen^1], ((((Worldxpos/(120 <<16))*(120 <<16))-Worldxpos)>>16)+ 120 , ((((Worldypos/(128 <<16))*(128 <<16))-Worldypos)>>16), g.TOP|g.LEFT);
				}
				disp_odou(g);
				disp_tourou(g);
				if((Scene == 310 )||(Scene == 320 )){
					drawBoss(g);
					if(
						(Scene == 320 )
							&&(1 == to_sayo_Hit(Boss[0 ],Boss[0 ]+((((Boss[8 ]&0xFF000000)>>24)-1)<<16),Boss[1 ],Boss[1 ]+((((Boss[8 ]&0x00FF0000)>>16)-1)<<16),0))
							){
					}
				}
				if((Sayo[2 ] == 0 )||(Sayo[2 ] == 1 )||(Sayo[2 ] == 7 )){
					drawSayo(g);
					if((Scene >= 200 )&&(Scene <= 340 )){
						drawOfudaOharai(g);
					}
				}
				else{
					if((Scene >= 200 )&&(Scene <= 340 )){
						drawOfudaOharai(g);
					}
					drawSayo(g);
				}
				if(Scene == 200 ){
					drawZako(g);
					drawZako2(g);
				}
				drawSmoke(g);
				disp_kakushi(g);
				disp_key(g);
				g.drawImage(CharImage[Item[1 ]], ((Item[0 ]&0x3FFFFFFF)>>16)-(Worldxpos>>16), (Item[0 ]&0x0000FFFF)-(Worldypos>>16), g.TOP|g.LEFT);
				disp_torii(g);
				if(Scene == 340 ){
					disp_seven_gods_escape(g);
				}
				if(Scene == 400 ){
					drawSayo(g);
					playSoundTrack(SOUND_8); //Ph[8]
					Scene = 405;
				}
				else if(Scene == 405 ){
					try{
						Thread.sleep(1000);
					}
					catch(Exception e){
					}
					System.gc();
					if((Sayo[5 ]&0xFF) == 0){
						if(High_Score < Score){
							High_Score = Score;
						}
						recordJob(true);
						if(
							(Key_flg == 0)
								||((Die_Scene >= 300 )&&(Die_Scene <= 340 ))
								){
							drawGate();
						}
					}
					Sayo[4 ] = 0;
					Scene = 410 ;
				}
				else if(((Sayo[5 ]&0xFF) == 0)&&(Scene == 410 )&&(Sayo[3 ] == 304 )){
					drawShadowString(g, "Game over", 120 >>1, 128 >>1, g.TOP|g.HCENTER, 0xFF, 0xFF, 0xFF);
					drawShadowString(g, "Score: "+Score, 120 >>1, (128 >>1)+g.getFont().getHeight(), g.TOP|g.HCENTER, 0xFF, 0xFF, 0xFF);
					gameOver();
				}
				else if(Scene == 420 ){
					myCount2++;
					drawShadowString(g, "Game over", 120 >>1, 128 >>1, g.TOP|g.HCENTER, 0xFF, 0xFF, 0xFF);
					drawShadowString(g, "Score: "+Score, 120 >>1, (128 >>1)+g.getFont().getHeight(), g.TOP|g.HCENTER, 0xFF, 0xFF, 0xFF);
					gameOver();
					if(myCount2==70)
					quitGame();
				}
			}
			else if(Scene == 0 ){
				GETWIDTH = getWidth();
				GETHEIGHT = getHeight();
				SCbuf[0] = Image.createImage(120 , 128 );
				SCg[0] = SCbuf[0].getGraphics();
				SCbuf[1] = Image.createImage(120 , 128 );
				SCg[1] = SCbuf[1].getGraphics();
				for(int y=0; y<16; y++){
					for(int x=0; x<15; x++){
						MAP[0][y][x] = 0;
					}
				}
				Scene = 1 ;
			}
			else if(Scene == 1 ){
				Scene = 7 ;//change by haitao
				isOperation = false;
			}
			else if((Scene == 5 )||(Scene == 6 )){
				try{
					Thread.sleep(3000);
				}
				catch(Exception e){
				}
				System.gc();
				animationThread = null;
				midlet.destroyApp(false);
				midlet.notifyDestroyed();
			}
			else if(Scene == 7 ){
				CreateImageWait("", "Scene "+No[Round_no] , g);
				CreateImageThread Cit = new CreateImageThread(g, 700 , Img_char_num[0], "/img.dat", 278 );
				Cit.start();
				Scene = 900 ;
			}
			else if(Scene == 720 ){
				CreateImageWait("", "Scene "+No[Round_no] , g);
				CreateImageThread Cit = new CreateImageThread(g, 730 , Boss_char_num[Round_no], "/img_boss.dat", 200 );
				Cit.start();
				Scene = 900 ;
			}
			else if(Scene == 730 ){
				CreateImageWait("", "Scene "+No[Round_no] , g);
				CreateImageThread Cit = new CreateImageThread(g, 710 , Zako_char_num[Round_no], "/img_zako.dat", 129 );
				Cit.start();
				Scene = 900;
			}
			else if(Scene == 710 ){
				CreateImageWait("", "Scene "+No[Round_no] , g);
				CreateImageThread Cit = new CreateImageThread(g, 110 , Map_char_num[Round_no], "/img_map.dat", 0);
				Cit.start();
				Scene = 900 ;
			}
			else if(Scene == 700 ){
				CreateImageWait("", "Scene "+No[Round_no] , g);
				CreateImageThread Cit = new CreateImageThread(g, 42 , Img_char_num[1], "/img.dat", 278 );
				Cit.start();
				Scene = 900 ;
			}
			else if(Scene == 740 ){
				CreateImageWait("", "Scene "+No[Round_no], g );
				CreateImageThread Cit = new CreateImageThread(g, 50 , Img_char_num[2], "/img_boss.dat", 200 );
				Cit.start();
				Scene = 900 ;
			}
			else if(Scene == 750 ){
				//Scene = 90;
				//myCount2=0;
				try {
					byte buf[] = new byte[1000];
					DataInputStream din = null;
					InputStream in = 		getClass().getResourceAsStream("/ending.dat");
					din = new DataInputStream(in);
					int size = din.readInt();
					int[] img_size = new int[size];
					for( int i = 0 ; i < size  ; i ++ )
					{
						img_size[i] = din.readInt();
					}
					for( int i = 0 ; i < size  ; i ++ )
					{
						din.readFully( buf, 0, img_size[i] ) ;
						ending_img[i] = Image.createImage( buf, 0, img_size[i] ) ;
					}
					din.close();
					buf = null;
					System.gc();
					Scene = 90;
				  myCount2=0;
				}
				catch(Exception e) {}
			}
			else if(Scene == 760 ){
			}
			else if(Scene == 42 ){
				recordJob(true);
				//destroyImage(Img_char_num[1]);//change by haitao 18:28 14/08/2003
				Sayo[5] = 3;//change by haitao
				Score = 0;
				Ofuda[4 ][0 ]	= 12;
				Ofuda[4 ][1 ]		= 1;
				Ofuda[4 ][2 ]		= 0x00000001;
				if(Round_no == 0){
					//Scene = 750 ;//
					Scene = 720 ;//
				}
				else{
					Scene = 720 ;
				}
			}
			else if(Scene == 50){
    		Seven_Gods_pos = 0;
				Scene = 60 ;
				playSoundTrack(SOUND_2);
			}
			else if(Scene == 70){
				Seven_Gods_pos = -104;
				Scene = 80 ;
				playSoundTrack(SOUND_2);
			}
			else if(Scene == 90){
				SCg[0].setColor(0x00, 0x00, 0x00);
				SCg[0].fillRect(0, 0, 120 , 128 );
				Seven_Gods_pos = 128 <<8;
				staff_roll_paint(g,Seven_Gods_pos&0x000000FF);
			}
			else if(Scene == 100 ){
			}
			else if((Scene == 60 )||(Scene == 80 )){
				SCg[0].setColor(0x00, 0x00, 0x00);
				SCg[0].fillRect(0, 0, 120 , 128 );
				SCg[1].setColor(0x00, 0x00, 0x00);
				SCg[1].fillRect(0, 0, 120 , 128 );

				if(Scene == 60 ){
					if(Seven_Gods_pos > -104){
						Seven_Gods_pos--;
					}
				}
				else if(Scene == 80 ){
					if(Seven_Gods_pos < 0){
						Seven_Gods_pos++;
					}
				}
				disp_seven_gods(SCg[1]);
				SCg[0].drawImage(CharImage[292 ], 120 >>1, 128 -CharImage[292 ].getHeight(), SCg[0].TOP|SCg[0].HCENTER);
				g.drawImage(SCbuf[0], 0, 0, g.TOP|g.LEFT);
				g.drawImage(SCbuf[1], 0, -24+Seven_Gods_pos, g.TOP|g.LEFT);
			}
			else if((Scene == 900 )||(Scene == 20 )||(Scene == 40 )||(Scene == 810 )||(Scene == 1001)){
				g.drawImage(SCbuf[0], 0, 0, g.TOP|g.LEFT);
				if(Scene == 900)
				{
					g.setFont(fTime);
					g.setColor(0xFF>>1, 0xFF>>1, 0xFF>>1);
					g.drawString("Loading scene "+No[Round_no]+" ...", (120 >>1)+1, (g.getFont().getHeight()<<1)+1, g.TOP|g.HCENTER);
					g.setColor(0xFF, 0xFF, 0xFF);
					g.drawString("Loading scene "+No[Round_no]+" ...", (120 >>1), (g.getFont().getHeight()<<1), g.TOP|g.HCENTER);
					//-----------------------------------
					g.setColor(0xFF, 0xFF, 0xFF);
					g.drawRect(8, (128 >>1)+g.getFont().getHeight()-2, 103, g.getFont().getHeight()+3);
					g.setColor(0xFF, 0x00, 0x00);
					g.fillRect(10, (128 >>1)+g.getFont().getHeight(), loading_Percent, g.getFont().getHeight());
				}
				if(Scene == 1001)
				{
					g.setFont(fTime);
					g.setColor(0xFF>>1, 0xFF>>1, 0xFF>>1);
					g.drawString("Loading fortune gods ...", (120 >>1)+1, (g.getFont().getHeight()<<1)+1, g.TOP|g.HCENTER);
					g.setColor(0xFF, 0xFF, 0xFF);
					g.drawString("Loading fortune gods ...", (120 >>1), (g.getFont().getHeight()<<1), g.TOP|g.HCENTER);
				}

			}
			else if(Scene == 110 ){
        //map_data_read();//change by me 1/09/2003
				if(Round_no == 0){
					Scene = 740;//50 ;//
				}
				else{
					map_data_read();
					Worldxpos = Wxp[Round_no];
					Worldypos = Wyp[Round_no];
					map_repaint();
					gameInit();
					gameReInit();
					zakoInit();
					drawGate();
					Key_flg = 0;
					Bakeuri_count = 0;
					Zako_limit_timer = 0;
					setAutoShot(midlet.isAutoShotOn);
					Scene = 200 ;//change by me , start the game
					isOperation = true;
				}
			}
			else if(Scene == 600 ){
				g.setColor(0x00, 0x00, 0x00);
				g.fillRect(0, 0, 120 , 128 );
				drawShadowString(g, "Retry", 120 >>1, (90 >>1)-g.getFont().getHeight(), g.TOP|g.HCENTER, 0xFF, 0xFF, 0xFF);
				drawShadowString(g, "Score: "+Score, 120 >>1, (128 >>1)-g.getFont().getHeight(), g.TOP|g.HCENTER, 0xFF, 0xFF, 0xFF);
				drawShadowString(g, "Life: "+(Sayo[5 ]&0xFF), (120 >>1), 138 >>1, g.TOP|g.HCENTER, 0xFF, 0xFF, 0xFF);
				Scene = 610 ;
			}
			else if(Scene == 610 ){
				try{
					Thread.sleep(3000);
				}
				catch(Exception e){
				}
				System.gc();
				Scene = 620 ;
			}
			else if(Scene == 620 ){
				Ofuda[4 ][0 ]	= 12;
				Ofuda[4 ][1 ]		= 1;
				Ofuda[4 ][2 ]		= 0x00000001;
				gameReInit();
				zakoInit();
				Zako_limit_timer = 0xFFFFFFFF;
				if(Die_Scene != 200 ){
					drawGate();
					Worldxpos = Re_Wxp[Round_no];
					Worldypos = Re_Wyp[Round_no];
					Sayo[0 ] = Worldxpos+(((120 >>1)-12)<<16);
					Sayo[1 ] = Worldypos+(((128 >>1)-12)<<16);
					map_repaint();
				}
				System.gc();
				setZako2(0, 0xFFF00000, Otama_offsetx[0]<<16, Otama_offsety[0]<<16, 0, 0xF);
				setZako2(0, 0xFFE00000, Otama_offsetx[0]<<16, Otama_offsety[0]<<16, 0, 0xF);
				setZako2(0, 0xFFD00000, Otama_offsetx[0]<<16, Otama_offsety[0]<<16, 0, 0xF);

				Otama_start_posx = Worldxpos;
				Otama_start_posy = Worldypos+0xFFF00000;
				Percent = 0;

				Scene = 200 ;
			}
			else if(Scene == 800 ){
				SCg[0].setColor(0x00, 0x00, 0x00);
				SCg[0].fillRect(0, 0, 120 , 128 );
				drawShadowString(SCg[0], "No credit 1", 120 >>1, 0, SCg[0].TOP|SCg[0].HCENTER, 0xFF, 0xFF, 0xFF);
				Scene = 810 ;
			}

			else if(Scene == 820 ){
				SCg[0].setColor(0x00, 0x00, 0x00);
				SCg[0].fillRect(0, 0, 120 , 128 );
				drawShadowString(SCg[0], "Scene == 820 1", 120 >>1, 0, SCg[0].TOP|SCg[0].HCENTER, 0xFF, 0xFF, 0xFF);
				g.drawImage(SCbuf[0], 0, 0, g.TOP|g.LEFT);

				Scene = 830 ;

			}
			else if(Scene == 830 ){
				Scene = 840 ;
			}
			else if(Scene == 840 ){
				try{
					Thread.sleep(3000);
				}
				catch(Exception e){
				}
				System.gc();
				recordJob(true);
				//Scene = 10 ;
				Scene = 20 ;
			}

			if((Mute_flg&0x0000000F) > 0){
				if((Mute_flg>>4) == 0){
				}
				else{
				}
			}
			if((Auto_Rennsya_flg&0x0000000F) > 0){
				if((Auto_Rennsya_flg>>4) == 0){
				}
				else{
				}
			}

			update();
		}
		catch(Exception e){
			g.setColor(0xFF, 0x00, 0x00);
			g.drawString("err", 0, 128 -(g.getFont().getHeight()*3), 0);
		}
	}

	public void gameScreenkeyPressed(int keyCode)
	{
		if ( keyCode == SOFT_KEY2 )
		{
			if(Scene==90)
			gameoverFlag = true;
			if(isOperation)
			{
				gameoverFlag = true;
			}
		}
		if ( keyCode == SOFT_KEY1 ){
			if(isOperation)
			{
				this.pauseGame();
			}
		}

		if(gameoverFlag){
			this.quitGame();
		}

		if(keyCode == -5) // zhong
			AllKeyCode = 65536;
		else if(keyCode == -1) // up
			AllKeyCode = 4096;
		else if(keyCode == -2 ) //down
			AllKeyCode = 32768;
		else if(keyCode == -3) //left
			AllKeyCode = 8192;
		else if(keyCode == -4)	//right
			AllKeyCode = 16384;
		else if(keyCode == 48)//{}	//key0  //
			AllKeyCode = 1;
		else if(keyCode == 49)	//key1
			AllKeyCode = 2;
		else if(keyCode == 50)	//key2
			AllKeyCode = 4096; //AllKeyCode = 4;
		else if(keyCode == 51)//{}	//key3
			AllKeyCode = 8;
		else if(keyCode == 52)	//key4
			AllKeyCode = 8192;//AllKeyCode = 16;
		else if(keyCode == 53)	//key5
			AllKeyCode = 65536; //AllKeyCode = 32;
		else if(keyCode == 54)	//key6
			AllKeyCode = 16384; //AllKeyCode = 64;
		else if(keyCode == 55)//{}	//key7
			AllKeyCode = 128;
		else if(keyCode == 56)	//key8
			AllKeyCode = 32768; //AllKeyCode = 256;
		else if(keyCode == 57)//{}	//key9
			AllKeyCode = 512;
		else if(keyCode == 21)	//left function
			AllKeyCode = 131072;
		else if(keyCode == 22)	//right function
			AllKeyCode = 262144;
		else if(keyCode == 42)	//*
			AllKeyCode = 1024;
		else if(keyCode == 35)	//#
			AllKeyCode = 2048;
		else{
			AllKeyCode = 0;
		}
	}


	public void logoScreenOptionsRequest()
	{
		gameEffect.pause();
		initialize(MENU_CURRENT);
		Screen_Current = Screen_Menu;
		repaint();
		serviceRepaints();
	}

	public void settingScreenkeyPressed(int keyCode)
	{
		if ( keyCode == SOFT_KEY2 ) {
			settingScreenBackRequest();
		}
		else if ( keyCode == SOFT_KEY1 )
		{
			switch(Setting_Screen_Count)
			{
			case 0:
				if ( midlet.getSoundFlag() == true )
				{
					midlet.setSoundFlag(false);
					repaint();
				}
				else
				{
					midlet.setSoundFlag(true);
					repaint();
				}
				break;
			case 1:
				if ( midlet.getAutoShotFlag() == true )
				{
					midlet.setAutoShotFlag(false);
					repaint();
				}
				else
				{
					midlet.setAutoShotFlag(true);
					repaint();
				}
				break;
			}
		}
		else
		{
			int j = 0 ;
			try
			{
				j = getGameAction(keyCode);
			}
			catch(Exception exception) { }
			switch(j)
			{
			case UP:
				Setting_Screen_Count--;
				if(Setting_Screen_Count<0)
				{
					Setting_Screen_Count=1;
				}
				repaint();
				break;
			case DOWN:
				Setting_Screen_Count++;
				if(Setting_Screen_Count>1)
				{
					Setting_Screen_Count=0;
				}
				repaint();
				break;
			case FIRE : // '\b'
				switch(Setting_Screen_Count)
				{
				case 0:
					if ( midlet.getSoundFlag() == true )
					{
						midlet.setSoundFlag(false);
						repaint();
					}
					else
					{
						midlet.setSoundFlag(true);
						repaint();
					}
					break;
				case 1:
					if ( midlet.getAutoShotFlag() == true )
					{
						midlet.setAutoShotFlag(false);
						repaint();
					}
					else
					{
						midlet.setAutoShotFlag(true);
						repaint();
					}
					break;
				}
				break;
			}
		}

	}

	public void settingScreenPaint(Graphics g)
	{
		g.setColor(0xFF, 0xFF, 0xFF);
		g.fillRect(0, 0, 176, 208);

		g.setColor(0x00, 0x00, 0xFF);
		g.fillRect(0,((20*Setting_Screen_Count) + 40), 176, 17);

		g.setFont(Font.getFont(Font.FACE_MONOSPACE, Font.STYLE_BOLD, Font.SIZE_LARGE));
		g.setColor(0x00, 0x00, 0x00);
		g.drawImage(gameIcon, 20, 20, g.VCENTER | g.HCENTER);

		g.drawString("Settings", getWidth()>>1, 15, g.TOP|g.HCENTER);
		g.drawLine(0, 35, 176, 35);

		if(midlet.getSoundFlag()==true){
			g.drawString("Sound on ", 3, 40, g.TOP | g.LEFT);
		}else if(midlet.getSoundFlag()==false){
			g.drawString("Sound off", 3, 40, g.TOP | g.LEFT);
		}
		if(midlet.getAutoShotFlag()==true){
			g.drawString("Auto shoot on", 3, 60, g.TOP | g.LEFT);
		}else if(midlet.getAutoShotFlag()==false){
			g.drawString("Auto shoot off", 3, 60, g.TOP | g.LEFT);
		}
		g.setFont(fGame);
		g.drawString("Change",2, 190, g.TOP|g.LEFT);
		g.drawString("Back", 174-fGame.stringWidth("Back"),190,g.TOP|g.LEFT);
	}

	public void settingsRequest()
	{
		Screen_Current = Screen_Setting;
		repaint();
	}
	public void settingScreenBackRequest()
	{
		Setting_Screen_Count = 0;
		initialize(MENU_CURRENT);
		Screen_Current = Screen_Menu;
		repaint();
	}

	public void instructionsRequest()
	{
		try
		{
			//gameIcon = Image.createImage("/icon.png");
			up = Image.createImage("/up.png");
			dn = Image.createImage("/dn.png");
			byte buf[] = new byte[250];
			DataInputStream din = null;
			InputStream in = getClass().getResourceAsStream("/item.dat");
			din = new DataInputStream(in);
			int size = din.readInt();
			int[] img_size = new int[size];
			for( int i = 0 ; i < size  ; i ++ )
			{
				img_size[i] = din.readInt();
			}
			for( int i = 0 ; i < size  ; i ++ )
			{
				din.readFully( buf, 0, img_size[i] ) ;
				items[i] = Image.createImage( buf, 0, img_size[i] ) ;
			}
			din.close();
			buf = null;
			System.gc();
		}catch(Exception e){
			e.printStackTrace();
		}
		Screen_Current = Screen_Instructions;
		repaint();
	}


	public void instructionsScreenPaint(Graphics g){
		g.setColor(0xFF, 0xFF, 0xFF);
		g.fillRect(0, 0, 176, 208);

		g.setFont(Font.getFont(Font.FACE_MONOSPACE, Font.STYLE_BOLD, Font.SIZE_LARGE));
		g.setColor(0x00, 0x00, 0x00);
		g.drawImage(gameIcon, 20, 20, g.VCENTER | g.HCENTER);

		g.drawString("Instructions", getWidth()>>1, 15, g.TOP|g.HCENTER);
		g.drawLine(0, 35, 176, 35);

		if(InstCount == 0){
			g.setFont(Font.getFont(Font.FACE_MONOSPACE, Font.STYLE_PLAIN, Font.SIZE_SMALL));
			g.drawString("--------Key controls--------",getWidth()>>1, 40, g.TOP|g.HCENTER);
			g.drawString("4 or 6: Move left or right.", 3, 55, g.TOP|g.LEFT);
			g.drawString("2 or 8: Move up or down.", 3, 70, g.TOP|g.LEFT);
			g.drawString("5: Unleash talismans.", 3, 85, g.TOP|g.LEFT);
			g.drawString("0: Swing magical wand.", 3, 100, g.TOP|g.LEFT);
			g.drawString("#: Auto shoot on/off.", 3, 115, g.TOP|g.LEFT);
			g.drawString("*: Use crystal ball on hand.", 3, 130, g.TOP|g.LEFT);
			g.drawString("Note: the above key controls' ", 3, 145, g.TOP|g.LEFT);
			g.drawString("description are dedicated to",  3, 160, g.TOP|g.LEFT);
            g.drawString("the Nokia 7650.", 3, 175, g.TOP|g.LEFT);
		}else if(InstCount == 1){
			g.setFont(Font.getFont(Font.FACE_MONOSPACE, Font.STYLE_PLAIN, Font.SIZE_SMALL));
			g.drawString("------General purpose-------", getWidth()>>1, 40, g.TOP|g.HCENTER);
			g.drawString("You are Sayo, a shrine maiden", 3, 55, g.TOP|g.LEFT);
			g.drawString("whose destiny is to fend off evil", 3, 70, g.TOP|g.LEFT);
			g.drawString("spirits and to prevent them from", 3, 85, g.TOP|g.LEFT);
			g.drawString("doing evil. The 7 deities have been", 3, 100, g.TOP|g.LEFT);
			g.drawString("captured by the heads of the evil", 3, 115, g.TOP|g.LEFT);
			g.drawString("spirits and it is up to you to save", 3, 130, g.TOP|g.LEFT);
			g.drawString("them. There are 7 scenes in the", 3, 145, g.TOP|g.LEFT);
			g.drawString("game. To complete each scene,", 3, 160, g.TOP|g.LEFT);
			g.drawString("you have to rescue the deity by", 3, 175, g.TOP|g.LEFT);
		}else if(InstCount == 2){
			g.setFont(Font.getFont(Font.FACE_MONOSPACE, Font.STYLE_PLAIN, Font.SIZE_SMALL));
			g.drawString("------General purpose-------", getWidth()>>1, 40, g.TOP|g.HCENTER);
			g.drawString("defeating the evil head. You have", 3, 55, g.TOP|g.LEFT);
			g.drawString("2 weapons: talisman and magical", 3, 70, g.TOP|g.LEFT);
			g.drawString("wand. You can cast out the evil", 3, 85, g.TOP|g.LEFT);
			g.drawString("spirits by using the talismans and", 3, 100, g.TOP|g.LEFT);
			g.drawString("the magical wand. You will be", 3, 115, g.TOP|g.LEFT);
			g.drawString("immobilised if you are touched", 3, 130, g.TOP|g.LEFT);
			g.drawString("by the evil spirits. If you are", 3, 145, g.TOP|g.LEFT);
			g.drawString("unfortunately immobilised 3", 3, 160, g.TOP|g.LEFT);
			g.drawString("times, the game will be over.", 3, 175, g.TOP|g.LEFT);

		}else if(InstCount == 3){
			g.setFont(Font.getFont(Font.FACE_MONOSPACE, Font.STYLE_PLAIN, Font.SIZE_SMALL));
			g.drawString("-------Power-up items-------", getWidth()>>1, 40, g.TOP|g.HCENTER);
			g.drawString("Various hidden power-up items", 3, 55, g.TOP|g.LEFT);
			g.drawString("will appear when the stone", 3, 70, g.TOP|g.LEFT);
			g.drawString("lantern is brushed by the magical ", 3, 85, g.TOP|g.LEFT);
			g.drawString("wand. Such power-up items can",3, 100, g.TOP|g.LEFT);
			g.drawString("be collected along the way.",3, 115, g.TOP|g.LEFT);
			g.drawString("Yellow talisman",getWidth()>>1, 130, g.TOP|g.HCENTER);
			g.drawImage(items[4],getWidth()>>1, 153,g.VCENTER|g.HCENTER);
			g.drawString("Collecting this item will enlarge ", 3, 160, g.TOP|g.LEFT);
			g.drawString("your talismans.", 3, 175, g.TOP|g.LEFT);
		}else if(InstCount == 4){
			g.setFont(Font.getFont(Font.FACE_MONOSPACE, Font.STYLE_PLAIN, Font.SIZE_SMALL));
			g.drawString("-------Power-up items-------", getWidth()>>1, 40, g.TOP|g.HCENTER);
			g.drawString("Light-blue talisman", getWidth()>>1, 55, g.TOP|g.HCENTER);
			g.drawImage(items[2],getWidth()>>1, 78,g.VCENTER|g.HCENTER);
			g.drawString("Collecting this item will improve", 3, 85, g.TOP|g.LEFT);
			g.drawString("the range of your talismans.", 3, 100, g.TOP|g.LEFT);
			g.drawString("Red talisman", getWidth()>>1, 115,  g.TOP|g.HCENTER);
			g.drawImage(items[3],getWidth()>>1, 138,g.VCENTER|g.HCENTER);
			g.drawString("Collecting this item will allow your", 3, 145, g.TOP|g.LEFT);
			g.drawString("talismans to penetrate the evil ", 3, 160, g.TOP|g.LEFT);
		}
		else if(InstCount == 5){
			g.setFont(Font.getFont(Font.FACE_MONOSPACE, Font.STYLE_PLAIN, Font.SIZE_SMALL));
			g.drawString("-------Power-up items-------", getWidth()>>1, 40, g.TOP|g.HCENTER);
			g.drawString("spirits.", 3, 55, g.TOP|g.LEFT);
			g.drawString("Dark-blue talisman",getWidth()>>1,70,g.TOP|g.HCENTER);
			g.drawImage(items[5],getWidth()>>1,93,g.VCENTER|g.HCENTER);
			g.drawString("Collecting this item will increase", 3, 100, g.TOP|g.LEFT);
			g.drawString("the speed of your talismans.", 3, 115, g.TOP|g.LEFT);
			g.drawString("Light-blue crystal ball", getWidth()>>1,130,g.TOP|g.HCENTER);
			g.drawImage(items[6],getWidth()>>1,153,g.VCENTER|g.HCENTER);
			g.drawString("Collecting and using this item will", 3, 160, g.TOP|g.LEFT);
		}
		else if(InstCount == 6){
			g.setFont(Font.getFont(Font.FACE_MONOSPACE, Font.STYLE_PLAIN, Font.SIZE_SMALL));
			g.drawString("-------Power-up items-------", getWidth()>>1, 40, g.TOP|g.HCENTER);
			g.drawString("immobilise the evil spirits for a", 3, 55, g.TOP|g.LEFT);
			g.drawString("while.", 3, 70, g.TOP|g.LEFT);
			g.drawString("Yellow crystal ball", getWidth()>>1,85,g.TOP|g.HCENTER);
			g.drawImage(items[7],getWidth()>>1,108,g.VCENTER|g.HCENTER);
			g.drawString("Collecting and using this item will", 3, 115, g.TOP|g.LEFT);
			g.drawString("make all the evil spirits within", 3, 130, g.TOP|g.LEFT);
			g.drawString("sight to disappear.", 3, 145, g.TOP|g.LEFT);
			g.drawString("Rice dumpling", getWidth()>>1, 160, g.TOP|g.HCENTER);
		}
		else if(InstCount == 7){
			g.setFont(Font.getFont(Font.FACE_MONOSPACE, Font.STYLE_PLAIN, Font.SIZE_SMALL));
			g.drawString("-------Power-up items-------", getWidth()>>1, 40, g.TOP|g.HCENTER);
			g.drawImage(items[0],getWidth()>>1,63,g.VCENTER|g.HCENTER);
			g.drawString("Collecting this item will increase", 3, 70, g.TOP|g.LEFT);
			g.drawString("your score by 2000 points.", 3, 85, g.TOP|g.LEFT);
			g.drawString("Magical wand",  getWidth()>>1, 100 , g.TOP|g.HCENTER);
			g.drawImage(items[1],getWidth()>>1,123,g.VCENTER|g.HCENTER);
			g.drawString("Collecting this item will increase", 3, 130, g.TOP|g.LEFT);
			g.drawString("your life by 1.", 3, 145, g.TOP|g.LEFT);

		}
		else if(InstCount == 8){
			g.setFont(Font.getFont(Font.FACE_MONOSPACE, Font.STYLE_PLAIN, Font.SIZE_SMALL));
			g.drawString("-------Select scene--------", getWidth()>>1, 40, g.TOP|g.HCENTER);
			g.drawString("In the \"Select scene\" menu, you", 3, 55, g.TOP|g.LEFT);
			g.drawString("can select a scene which has", 3, 70, g.TOP|g.LEFT);
			g.drawString("already been completed.", 3, 85, g.TOP|g.LEFT);

		}

		g.setFont(Font.getFont(Font.FACE_MONOSPACE, Font.STYLE_BOLD, Font.SIZE_LARGE));
		if (InstCount==0){
			g.drawImage( dn, scrW/2, scrH-dn.getHeight()-2, (g.TOP | g.HCENTER) );
			g.setFont(fGame);
			g.drawString("More",2, 190, g.TOP|g.LEFT);
		}
		else if (InstCount==8) {
			g.drawImage( up, scrW/2, scrH-dn.getHeight()-up.getHeight()-2, (g.TOP | g.HCENTER) );
		}
		else {
			g.drawImage( up, scrW/2, scrH-dn.getHeight()-up.getHeight()-2, (g.TOP | g.HCENTER) );
			g.drawImage( dn, scrW/2, scrH-dn.getHeight()-2, (g.TOP | g.HCENTER) );
			g.setFont(fGame);
			g.drawString("More",2, 190, g.TOP|g.LEFT);
		}
		g.setFont(fGame);
		g.drawString("Back", 174-fGame.stringWidth("Back"),190,g.TOP|g.LEFT);
	}

	public void instructionsScreenkeyPressed(int keyCode)
	{
		int j =0 ;
		if ( keyCode == SOFT_KEY2 ) {
			instructionScreenBackRequest();
		}
		else if ( keyCode == SOFT_KEY1 ) {
			InstCount += 1;
			if(InstCount > 8){
				InstCount = 8;
			}
			repaint();
		}
		else
		{
			switch(keyCode)
			{
			case 56: // '2'
				InstCount += 1;
				if(InstCount > 8){
					InstCount = 8;
				}
				repaint();
				break;
			case 50: // '8'
				InstCount -= 1;
				if(InstCount < 0){
					InstCount = 0;
				}
				repaint();
				break;

			case KEY_DOWN_ARROW : // '\006'
				InstCount += 1;
				if(InstCount > 8){
					InstCount = 8;
				}
				repaint();
				break;

			case KEY_UP_ARROW : // '\006'
				InstCount -= 1;
				if(InstCount < 0){
					InstCount = 0;
				}
				repaint();
				break;

			}

		}
	}
	public void instructionScreenBackRequest()
	{
		initialize(MENU_CURRENT);
		Screen_Current = Screen_Menu;
		repaint();
	}

	public void aboutScreenPaint(Graphics g)
	{
		g.setColor(0xFF, 0xFF, 0xFF);
		g.fillRect(0, 0, 176, 208);

		g.setFont(Font.getFont(Font.FACE_MONOSPACE, Font.STYLE_BOLD, Font.SIZE_LARGE));
		g.setColor(0x00, 0x00, 0x00);
		g.drawImage(gameIcon, 20, 20, g.VCENTER | g.HCENTER);

		g.drawString("About", getWidth()>>1, 15, g.TOP|g.HCENTER);
		g.drawLine(0, 35, 176, 35);

		g.drawString("KikiKaikai Ver." + "1.4.0", getWidth()>>1, 40, g.TOP|g.HCENTER);
		g.drawString("Oct 25 2003", getWidth()>>1, 60, g.TOP|g.HCENTER);
		g.drawImage(gameTaito, 176>>1, 120, g.VCENTER | g.HCENTER);

		g.setFont(Font.getFont(Font.FACE_MONOSPACE, Font.STYLE_BOLD, Font.SIZE_LARGE));
		g.drawString("Back", 174-fGame.stringWidth("Back"),190,g.TOP|g.LEFT);
	}

	public void aboutRequest()
	{
		Screen_Current = Screen_About ;
		repaint();
	}


	public void aboutScreenkeyPressed(int keyCode)
	{
		if ( keyCode == SOFT_KEY2 ) {
			aboutScreenBackRequest();
		}

	}

	public void aboutScreenBackRequest()
	{
		initialize(MENU_CURRENT);
		Screen_Current = Screen_Menu;
		repaint();
	}


	public void missionSelectRequest()
	{
		stage_finished = midlet.getHighMissionNum();
		if(stage_finished==0)
		stage_finished = 1;
		TRdv= stage_finished;
		Screen_Current = Screen_Select;
		repaint();
	}

	public void missionSelectStartRequest(int misson)
	{
		MENU_CURRENT = MENU_CONTINUE ;
		setSound(midlet.isSoundOn);
		setVibration(midlet.isVibrationOn);
		setAutoShot(midlet.isAutoShotOn);
		setRound(misson-1);
		callGameScreen();
		Screen_Current = Screen_Game ;
		if(animationThread == null)
		{
		animationThread = new Thread(this);
		animationThread.start();
		}
	}

	public void missionSelectBackRequest()
	{
		initialize(MENU_CURRENT);
		Screen_Current = Screen_Menu;
		repaint();
	}


	public void selectScreenPaint(Graphics g)
	{
		int StartCount = 0;
		int i = 1;
		int TargetCount = 0;
		int MaxCount = 0;
		g.setColor(0xFF, 0xFF, 0xFF);
		g.fillRect(0, 0, 176, 208);
		g.setFont(Font.getFont(Font.FACE_MONOSPACE, Font.STYLE_BOLD, Font.SIZE_LARGE));
		g.setColor(0x00, 0x00, 0x00);
		g.drawString("Select scene", getWidth()>>1, 15, g.TOP | g.HCENTER);
		g.drawImage(gameIcon, 20, 20, g.VCENTER | g.HCENTER);
		g.drawLine(0, 35, 176, 35);
		if(stage_finished <= 7){
			TargetCount = TRdv;
			StartCount = 1;
			MaxCount = stage_finished;
		}
		else
		{
			if(TRdv <= 7){
				TargetCount = TRdv;
			}else{
				TargetCount = 7;
			}

			MaxCount = 7;
			StartCount = TRdv - 6;
			if((StartCount < 7) && (TRdv <= 7)){
				StartCount = 1;
			}
		}
		g.setFont(fGame);
		for(i = 0; i< stage_finished; i++){
			if(TargetCount == i+1){
				g.setColor(0x00, 0x00, 0xFF);
				g.fillRect(0,(20*i+40),176,18);
			}
		g.setColor(0x00, 0x00, 0x00);
		g.drawString("Scene " + (StartCount + i), 3, (20*i+40), g.TOP | g.LEFT);
		if(MaxCount == (i + 1)) break;
		}
		g.setColor(0x00, 0x00, 0x00);
		g.setFont(fGame);
		g.drawString("Start",2, 190, g.TOP|g.LEFT);
		g.drawString("Back", 174-fGame.stringWidth("Back"),190,g.TOP|g.LEFT);
	}

	public void selectScreenkeyPressed(int keyCode)
	{
		int j =0 ;
		if ( keyCode == SOFT_KEY2 ) {
			missionSelectBackRequest();
		}
		else if ( keyCode == SOFT_KEY1 ) {
			missionSelectStartRequest(TRdv);
		}
		else
		{
			switch(keyCode)
			{
			case 50: // '2'
				TRdv--;
				TRdv%=(stage_finished+1);
				if (TRdv == 0)
				TRdv= stage_finished;
				repaint();
				break;
			case 56: // '8'
				TRdv++;
				TRdv%=(stage_finished+1);
				if (TRdv == 0)
				TRdv= 1;
				repaint();
				break;

			case KEY_UP_ARROW : // '\006'
				TRdv--;
				TRdv%=(stage_finished+1);
				if (TRdv == 0)
				TRdv= stage_finished;
				repaint();
				break;

			case 53 : // '\b'
				missionSelectStartRequest(TRdv);
				break;

			case KEY_DOWN_ARROW : // '\006'
				TRdv++;
				TRdv%=(stage_finished+1);
				if (TRdv == 0)
				TRdv= 1;
				repaint();
				break;
			default:

				try
				{
					j = getGameAction(keyCode);
				}
				catch(Exception exception) { }
				switch(j)
				{
				case FIRE : // '\b'
					missionSelectStartRequest(TRdv);
					break;
				}
				break;

			}

		}
	}

	public void splashScreenPaint(Graphics g)
	{
		g.setColor(0x000000);
		g.fillRect( 0, 0,  scrW, scrH );
		g.drawImage( imgSplash, scrW/2, scrH/2, (g.VCENTER | g.HCENTER) );
	}

	public void gameOverRequest()
	{
		MENU_CURRENT = MENU_NEW;
		gameoverFlag = false ;
		gameEffect.pause();
		logoScreenRequest();
		animationThread = null;
	}

	public void pauseGameRequest()
	{
	   	gameEffect.pause();
		initialize(MENU_CURRENT);
		Screen_Current = Screen_Menu;
		repaint();
		animationThread = null;
	}

	public void newGameRequest()
	{
		MENU_CURRENT = MENU_CONTINUE;
		setSound(midlet.isSoundOn);
		setVibration(midlet.isVibrationOn);
		setAutoShot(midlet.isAutoShotOn);
		setRound(0);
		callGameScreen();
		Screen_Current = Screen_Game ;
		if(animationThread == null)
		{
		animationThread = new Thread(this);
		animationThread.start();
		}
	}

	public void continueGameRequest()
	{
		setSound(midlet.isSoundOn);
		setVibration(midlet.isVibrationOn);
		setAutoShot(midlet.isAutoShotOn);
		Screen_Current = Screen_Game ;
		gameEffect.resume();

		if(animationThread == null)
		{
		animationThread = new Thread(this);
		animationThread.start();
		}
		repaint();
	}

	public void drawEnding(Graphics g)
	{
		int Frame_no = going_count/100+1;
		g.translate(0-g.getTranslateX(),0-g.getTranslateY());
		g.translate((GETWIDTH- 128 )>>1, ((144 - 128 )>>1)+44);
		g.setClip(0, 0, 128 , 128 );
		switch(Frame_no)
		{
		case 1:
			g.setColor(0x000000);
			g.fillRect(0, 0, 128 , 128 );
			g.setColor(0xff, 0xff, 0xff);
			g.setFont(fTime);
			if(Seven_Gods_position <55){
				Seven_Gods_position+=2;
			}
			g.drawString("You revived the 7", (128>>1)-fTime.stringWidth("You revived the 7")/2,Seven_Gods_position-fTime.getHeight()*3-4-4,0);
			g.drawString("deities in this bizarre",(128>>1)-fTime.stringWidth("deities in this bizarre")/2,Seven_Gods_position-fTime.getHeight()*2-4-2,0);
			g.drawString("world at last!",(128>>1)-fTime.stringWidth("world at last!")/2,Seven_Gods_position-fTime.getHeight()*1-4,0);
			g.drawImage(gameGod,1,Seven_Gods_position,g.TOP|g.LEFT);
			g.drawImage(ending_img[0],(128 >>1)-ending_img[0].getWidth()/2, 125 -ending_img[0].getHeight(), g.TOP|g.LEFT);
			break;
		case 2:
			g.setColor(0x000000);
			g.fillRect(0, 0, 128 , 128 );
			g.setColor(0xff, 0xff, 0xff);
			g.setFont(fTime);
			g.drawString("Credits", (128>>1)-fTime.stringWidth("Credits")/2,(128>>1)-fTime.getHeight()/2,0);
			break;
		case 3:
			g.setColor(0x000000);
			g.fillRect(0, 0, 128 , 128 );
			g.setColor(0xff, 0xff, 0xff);
			g.setFont(fTime);
			g.drawString("Direction", (128>>1)-fTime.stringWidth("Direction")/2,16,g.TOP|g.LEFT);
			g.drawString("Keiji Fujita", (128>>1)-fTime.stringWidth("Keiji Fujita")/2,120 -ending_img[1].getHeight()-2*fTime.getHeight()-8-4,g.TOP|g.LEFT);
			g.drawString("(Orange Gum)", (128>>1)-fTime.stringWidth("(Orange Gum)")/2,120 -ending_img[1].getHeight()-fTime.getHeight()-8,g.TOP|g.LEFT);
			g.drawImage(ending_img[1], (128 >>1)-ending_img[1].getWidth()/2, 120 -ending_img[1].getHeight(), g.TOP|g.LEFT);
			break;
		case 4:
			g.setColor(0x000000);
			g.fillRect(0, 0, 128 , 128 );
			g.setColor(0xff, 0xff, 0xff);
			g.setFont(fTime);
			g.drawString("Code/Graphics", (128>>1)-fTime.stringWidth("Code/Graphics")/2,2,g.TOP|g.LEFT);
			g.drawString("Phan Seow Kang", (128>>1)-fTime.stringWidth("Phan Seow Kang")/2,125 -ending_img[2].getHeight()-10-4*(fTime.getHeight()+2),g.TOP|g.LEFT);
			g.drawString("Li Hai Tao", (128>>1)-fTime.stringWidth("Li Hai Tao")/2,125 -ending_img[2].getHeight()-10-3*(fTime.getHeight()+2),g.TOP|g.LEFT);
			g.drawString("Cheung Lap Yan", (128>>1)-fTime.stringWidth("Cheung Lap Yan")/2,125 -ending_img[2].getHeight()-10-2*(fTime.getHeight()+2),g.TOP|g.LEFT);
			g.drawString("(Orange Gum)", (128>>1)-fTime.stringWidth("(Orange Gum)")/2,125 -ending_img[2].getHeight()-8-fTime.getHeight(),g.TOP|g.LEFT);
			g.drawImage(ending_img[2], (128 >>1)-ending_img[2].getWidth()/2, 125 -ending_img[2].getHeight(), g.TOP|g.LEFT);
			break;
		case 5:
			g.setColor(0x000000);
			g.fillRect(0, 0, 128 , 128 );
			g.setColor(0xff, 0xff, 0xff);
			g.setFont(fTime);
			g.drawString("Special thanks", (128>>1)-fTime.stringWidth("Special thanks")/2,16,g.TOP|g.LEFT);
			g.drawString("Miho Rigdon", (128>>1)-fTime.stringWidth("Miho Rigdon")/2,120 -ending_img[3].getHeight()-3*fTime.getHeight()-8-6,g.TOP|g.LEFT);
			g.drawString("(Taito)", (128>>1)-fTime.stringWidth("(Taito)")/2,120 -ending_img[3].getHeight()-2*fTime.getHeight()-8-4,g.TOP|g.LEFT);
			g.drawImage(ending_img[3], (128 >>1)-ending_img[3].getWidth()/2, 120 -ending_img[3].getHeight(), g.TOP|g.LEFT);
			break;
		case 6:
			g.setColor(0x000000);
			g.fillRect(0, 0, 128 , 128 );
			g.setColor(0xff, 0xff, 0xff);
			g.setFont(fTime);
			g.drawString("Special thanks", (128>>1)-fTime.stringWidth("Special thanks")/2,16,g.TOP|g.LEFT);
			g.drawString("Kyoko Hashimoto", (128>>1)-fTime.stringWidth("Kyoko Hashimoto")/2,120 -ending_img[4].getHeight()-3*fTime.getHeight()-8-6,g.TOP|g.LEFT);
			g.drawString("(Taito)", (128>>1)-fTime.stringWidth("(Taito)")/2,120 -ending_img[4].getHeight()-2*fTime.getHeight()-8-4,g.TOP|g.LEFT);
			g.drawImage(ending_img[4], (128 >>1)-ending_img[4].getWidth()/2, 120 -ending_img[4].getHeight(), g.TOP|g.LEFT);
			break;
		case 7:
			g.setColor(0x000000);
			g.fillRect(0, 0, 128 , 128 );
			g.setColor(0xff, 0xff, 0xff);
			g.setFont(fTime);
			g.drawString("Supervisor", (128>>1)-fTime.stringWidth("Supervisor")/2,16,g.TOP|g.LEFT);
			g.drawString("Jun Nohara", (128>>1)-fTime.stringWidth("Jun Nohara")/2,120 -ending_img[5].getHeight()-3*fTime.getHeight()-8-6,g.TOP|g.LEFT);
			g.drawString("(Taito)", (128>>1)-fTime.stringWidth("(Taito)")/2,120 -ending_img[5].getHeight()-2*fTime.getHeight()-8-4,g.TOP|g.LEFT);
			g.drawImage(ending_img[5], (128 >>1)-ending_img[5].getWidth()/2, 120 -ending_img[5].getHeight(), g.TOP|g.LEFT);
			break;
		case 8:
		g.setColor(0x000000);
			g.fillRect(0, 0, 128 , 128 );
			g.setColor(0xff, 0xff, 0xff);
			g.setFont(fTime);
			g.drawString("(c)TAITO", (128>>1)-fTime.stringWidth("(c)TAITO")/2,(128>>1)-fTime.getHeight()/2,0);
			break;
		case 10:
		case 9:
			g.setColor(0x000000);
			g.fillRect(0, 0, 128 , 128 );
			g.setColor(0xff, 0xff, 0xff);
			g.setFont(fTime);
			g.drawString("Score: "+Score, (128>>1)-fTime.stringWidth("Score: "+Score)/2,(128>>1) -ending_img[6].getHeight()/2-fTime.getHeight()-8,0);
			g.drawImage(ending_img[6], (128 >>1)-ending_img[6].getWidth()/2, (128>>1) -ending_img[6].getHeight()/2, g.TOP|g.LEFT);
			g.drawString("The end", (128>>1)-fTime.stringWidth("The end")/2,(128>>1) +ending_img[6].getHeight()/2+8,0);
			break;
		}
	}

	public void playEndingMusic()
	{
		int Frame_no = going_count/100+1;
		switch(Frame_no)
		{
		case 1:
			if(Ending_1)
			{
				if(midlet.getSoundFlag())
				{
				gameEffect.pause();
				gameEffect.playEndMain();
				}
				Ending_1 = false;
			}
			break;
		case 10:
			if(Ending_2)
			{
				quitGame();
				Ending_2 = false;
			}
			break;
	}
	}

public void updateLoading(int Sc, int Percent){
if(Sc == 700)
{
loading_Percent = 5*Percent/100;
}
else if(Sc==42)
{
loading_Percent = 5+5*Percent/100;
}
else if(Sc==720)
{
if(Round_no == 0)
loading_Percent = 10+10*Percent/100;
else
loading_Percent = 20*Percent/100;
}
else if(Sc==730)
{
loading_Percent = 20+20*Percent/100;
}
else if(Sc==710)
{
loading_Percent = 40+40*Percent/100;
}
else if(Sc==110)
{
loading_Percent = 80+20*Percent/100;
}
}

public void startGame()
{
	if(animationThread == null)
	{
	animationThread = new Thread(this);
	animationThread.start();
	}
}

}
