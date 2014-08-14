package com.example.tallyou.utils;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.Locale;

/**
 * @author zhangpengzhan
 * 
 *         2014年3月9日 上午1:36:15 * private static String[] formats = new String[]
 *         { "yyyy-MM-dd", "yyyy-MM-dd HH:mm", "yyyy-MM-dd HH:mmZ",
 *         "yyyy-MM-dd HH:mm:ss.SSSZ", "yyyy-MM-dd'T'HH:mm:ss.SSSZ", };
 * 
 */
public class TimeRender {

	private static SimpleDateFormat formatBuilder;
	private static String[] formats = new String[] { "yyyy-MM-dd",
			"MM-dd HH:mm", "yyyy-MM-dd HH:mmZ",
			"yyyy-MM-dd HH:mm:ss.SSSZ", "yyyy-MM-dd'T'HH:mm:ss.SSSZ", };

	/**
	 * private static String[] formats = new String[] { "yyyy-MM-dd",
	 * "yyyy-MM-dd HH:mm", "yyyy-MM-dd HH:mmZ", "yyyy-MM-dd HH:mm:ss.SSSZ",
	 * "yyyy-MM-dd'T'HH:mm:ss.SSSZ", };
	 * 
	 * @param type
	 * @return
	 */
	public static String getDate(int type) {
		formatBuilder = new SimpleDateFormat(formats[type], Locale.CHINA);
		return formatBuilder.format(new Date());
	}

}
