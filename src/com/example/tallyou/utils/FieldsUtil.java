package com.example.tallyou.utils;

/**
 * @author zhangpengzhan
 * 
 *         2014��3��6�� ����12:21:03
 */
public class FieldsUtil {
	/**
	 * ��ס����״̬
	 */
	public final static String isRembered_PW = "rember_pw";

	/**
	 * ��ͬ���͵��ַ�
	 */
	public final static String mBoolean = "Boolean";
	public final static String mInt = "Int";
	public final static String mString = "String";

	public final static String UserID = "UserID";
	public final static String UserPW = "UserPW";
	public final static String WebAddress = "WebAddress";
	public final static String isAutoLoading = "AutoLoading";

	public final static int CUSTOM_DIALOG = 0x0023;
	public final static int DEFAULT_DIALOG = 0x0024;

	public final static String SDRootFile_String = android.os.Environment
			.getExternalStorageDirectory().toString();

	/**
	 * spark �汾
	 */
	public final static String Spark = "/Spark 2.6.3";

	public final static int CallBack_Value1 = 0x000010;

	public final static int mTime = 3;

	/**
	 * ���������Ƶ���ļ���ַ����Ƶ�ļ��ı�־
	 */
	public final static String VideoFile_R = "/TallYou/Receive/Video/";
	public final static String VideoFile_S = "/TallYou/Send/Video/";
	public final static String VideoFlag = "TallYou_Video_";
	public final static String VideoPath = "VideoPath";
	public final static String VideoSuffix = ".3gp";

	/**
	 * ������յ��������ļ���ַ�������ļ��ı�־
	 */
	public final static String VoiceFile_R = "/TallYou/Receive/Voice/";
	public final static String VoiceFile_S = "/TallYou/Send/Voice/";
	public final static String VoiceFlag = "TallYou_Voice_";
	public final static String VoicePath = "VoicePath";
	public final static String VoiceSuffix = ".amr";

	/**
	 * ������յ�ͼƬ���ļ���ַ��ͼƬ�ļ��ı�־
	 */
	public final static String ImageFile_R = "/TallYou/Receive/Image/";
	public final static String ImageFile_S = "/TallYou/Send/Image/";
	public final static String ImageFlag = "TallYou_Image_";
	public final static String ImagePath = "ImagePath";
	public final static String ImageSuffix = ".jpg";

	/**
	 * ������յĸ������ļ���ַ�͸����ļ��ı�־
	 */
	public final static String AccessoryFile_R = "/TallYou/Receive/Accessory/";
	public final static String AccessoryFile_S = "/TallYou/Send/Accessory/";
	public final static String AccessoryFlag = "TallYou_Accessory_";

	/**
	 * ��Ҫ���˵ı�����ĸ�������
	 */
	public final static String[] FilterStrings = { VoiceFlag, VideoFlag,
			ImageFlag, AccessoryFlag };

	/** ��Ƶˢ�¼�� */
	public final static int VideoPreRate = 1;
	/** ��ǰ��Ƶ��� */
	public static int tempPreRate = 0;
	/** ��Ƶ���� */
	public final static int VideoQuality = 85;

	/** ������Ƶ��ȱ��� */
	public final static float VideoWidthRatio = 1;
	/** ������Ƶ�߶ȱ��� */
	public final static float VideoHeightRatio = 1;

	/** ������Ƶ��� */
	public static int VideoWidth = 320;
	/** ������Ƶ�߶� */
	public static int VideoHeight = 240;
	/** ��Ƶ��ʽ���� */
	public static int VideoFormatIndex = 0;

}
