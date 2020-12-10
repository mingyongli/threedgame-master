package com.ws3dm.app.util;

import android.graphics.Bitmap;
import android.graphics.Bitmap.Config;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.PorterDuff.Mode;
import android.graphics.PorterDuffXfermode;
import android.graphics.Rect;
import android.graphics.RectF;
import android.text.format.Time;
import android.util.Log;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.Locale;
import java.util.TimeZone;

/**
 * TimeUtils
 * 
 * @author <a href="http://www.trinea.cn" target="_blank">Trinea</a> 2013-8-24
 */
public class TimeUtil {//1547620979870   2019.1.16 14:43:44

	public static final SimpleDateFormat DATE_FORMAT_ALL = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");

	public static final SimpleDateFormat DEFAULT_DATE_FORMAT = new SimpleDateFormat("yyyy-MM-dd HH:mm");
	public static final SimpleDateFormat DATE_FORMAT_ENYMD = new SimpleDateFormat("yyyy-MM-dd");
	public static final SimpleDateFormat DATE_FORMAT_ENYM = new SimpleDateFormat("yyyy-MM");
	public static final SimpleDateFormat DATE_FORMAT_DATE_H = new SimpleDateFormat("yyyy-MM-dd-HH");
	public static final SimpleDateFormat DEFAULT_DATE_FORMAT_CN = new SimpleDateFormat("yyyy年-MM月-dd日  HH时:mm分");

	public static final SimpleDateFormat DATE_FORMAT_ENHMS = new SimpleDateFormat("HH:mm:ss");
	public static final SimpleDateFormat DATE_FORMAT_CNYM = new SimpleDateFormat("yyyy年M月");
	public static final SimpleDateFormat DATE_FORMAT_CNYMD = new SimpleDateFormat("yyyy年M月d日");
	public static final SimpleDateFormat DATE_FORMAT_CNMD = new SimpleDateFormat("M月d日");
	public static final SimpleDateFormat DATE_FORMAT_ENHM = new SimpleDateFormat("HH:mm");
	public static final SimpleDateFormat DATE_FORMAT_SHORTMD = new SimpleDateFormat("MM-dd");


	// public static final SimpleDateFormat DATE_FORMAT1 = new
	// SimpleDateFormat("yyyy/MM/dd");

	/**
	 * 判断是否为合法的日期时间字符串
	 * 
	 * @param str_input
	 * @param str_input
	 * @return boolean;符合为true,不符合为false
	 */
	public static boolean isDate(String str_input, String rDateFormat) {
		if (!(str_input==null||str_input.trim().length()==0)) {
			SimpleDateFormat formatter = new SimpleDateFormat(rDateFormat);
			formatter.setLenient(false);
			try {
				formatter.format(formatter.parse(str_input));
			} catch (Exception e) {
				return false;
			}
			return true;
		}
		return false;
	}

	/**
	 * long time to string
	 * 
	 * @param timeInMillis
	 * @param dateFormat
	 * @return
	 */
	public static String getTime(long timeInMillis, SimpleDateFormat dateFormat) {
		return dateFormat.format(new Date(timeInMillis * 1000));
	}

	/**
	 * long time to string, format is {@link #DEFAULT_DATE_FORMAT}
	 * 
	 * @param timeInMillis
	 * @return
	 */
	public static String getTimeENMD(long timeInMillis) {
		return getTime(timeInMillis, DATE_FORMAT_CNMD);
	}
	public static String getTimeCNMD(long timeInMillis) {
		return getTime(timeInMillis, DATE_FORMAT_CNMD);
	}

	public static String getTimeCNYMD(long timeInMillis) {
		return getTime(timeInMillis, DATE_FORMAT_CNYMD);
	}
	
	public static String getTimeCNYM(long timeInMillis) {
		return getTime(timeInMillis, DATE_FORMAT_CNYM);
	}
	
	public static String getTime(long timeInMillis) {
		return getTime(timeInMillis, DEFAULT_DATE_FORMAT);
	}
	
	public static String getTimeCN(long timeInMillis) {
		return getTime(timeInMillis, DEFAULT_DATE_FORMAT_CN);
	}
	
	public static String getTimeEN(long timeInMillis) {
		return getTime(timeInMillis, DATE_FORMAT_ENYMD);
	}

	public static String getTimeENHM(long timeInMillis) {
		return getTime(timeInMillis, DATE_FORMAT_ENHM);
	}

	public static String getFormatTime(long timeInMillis) {
		return getTime(timeInMillis, DATE_FORMAT_ALL);
	}

	public static String getFormatTimeSimple(long timeInMillis) {
		return getTime(timeInMillis, DATE_FORMAT_ENYMD);
	}

	public static Integer getCurrentYear() {
		return getYear(System.currentTimeMillis());
	}

	public static Integer getCurrentMonth() {
		return getMonth(System.currentTimeMillis());
	}

	public static Integer getYear(long timeInMillis) {
		Calendar cal = Calendar.getInstance();
		cal.setTimeInMillis(timeInMillis);

		return cal.get(Calendar.YEAR);
	}

	public static Integer getMonth(long timeInMillis) {
		Calendar cal = Calendar.getInstance();
		cal.setTimeInMillis(timeInMillis);

		return cal.get(Calendar.MONTH) + 1;
	}

	public static Integer getDay(String timeInMillis) {
		return getDay(Long.parseLong(timeInMillis));
	}

	public static Integer getDay(long timeInMillis) {
		Calendar cal = Calendar.getInstance();
		cal.setTimeInMillis(timeInMillis*1000);

		return cal.get(Calendar.DAY_OF_MONTH);
	}

	public static Integer getHour(long timeInMillis) {
		Calendar cal = Calendar.getInstance();
		cal.setTimeInMillis(timeInMillis);

		return cal.get(Calendar.HOUR_OF_DAY);
	}

	public static String getDateStringByENYMD(Date date) {
		return DATE_FORMAT_ENYMD.format(date);
	}

	public static String getDateStringByENYM(Date date) {
		return DATE_FORMAT_ENYM.format(date);
	}

	public static String getDateStringByAll(Date date) {
		return DATE_FORMAT_ALL.format(date);
	}

	public static String getDateStringByHMS(Date date) {
		return DATE_FORMAT_ENHMS.format(date);
	}

	public static Date countDate(Date date, int increment) {
		Calendar calendar = Calendar.getInstance();
		calendar.setTime(date);
		calendar.add(Calendar.DATE, increment);
		date = calendar.getTime();
		return date;
	}

	public static Date countMonth(Date date, int increment) {
		Calendar calendar = Calendar.getInstance();
		calendar.setTime(date);
		calendar.add(Calendar.MONTH, increment);
		date = calendar.getTime();
		return date;
	}

	public static int daysBetween(Date beginDate, Date endDate) {
		Calendar cal = Calendar.getInstance();
		cal.setTime(beginDate);
		long time1 = cal.getTimeInMillis();
		cal.setTime(endDate);
		long time2 = cal.getTimeInMillis();
		long between_days = (time2 - time1) / (1000 * 3600 * 24);

		return Integer.parseInt(String.valueOf(between_days));
	}

	public static Date getMonthActualMax(long timeInMillis) {
		Calendar cal = Calendar.getInstance();
		cal.setTimeInMillis(timeInMillis);
		cal.set(Calendar.DAY_OF_MONTH,
				cal.getActualMaximum(Calendar.DAY_OF_MONTH));

		return cal.getTime();
	}

	public static Date getMonthActualMin(long timeInMillis) {
		Calendar cal = Calendar.getInstance();
		cal.setTimeInMillis(timeInMillis);
		cal.set(Calendar.DAY_OF_MONTH, 1);

		return cal.getTime();
	}

	public static Date getPreviousDate(Date startDate) {
		return countDate(startDate, -1);
	}

	// public static int getCurrentYear() {
	// Time t = new Time();
	// t.setToNow();
	// return t.year;
	// }
	//
	// public static int getCurrentMonth() {
	// Time t = new Time();
	// t.setToNow();
	// // return t.month + 1;
	// return t.month + 1;
	// }

	public static String getCNWeekDay(long time) {
		String[] weeks={"周日","周一","周二","周三","周四","周五","周六"};
		Calendar calendar = Calendar.getInstance();
		try {
			calendar.setTimeInMillis(time*1000);
		} catch (Exception e) {
			e.printStackTrace();
		}
		return weeks[calendar.get(Calendar.DAY_OF_WEEK)-1];
	}

	public static int getWeekDay(String date) {
		Calendar calendar = Calendar.getInstance();
		try {
			calendar.setTime(DATE_FORMAT_ENYMD.parse(date));
		} catch (ParseException e) {
			e.printStackTrace();
		}
		int dayOfWeek = calendar.get(Calendar.DAY_OF_WEEK);
		if (dayOfWeek == 1) {
			dayOfWeek = 0;
		} else {
			dayOfWeek -= 1;
		}
		return dayOfWeek;
	}

//	判断是不是闰年
	public static boolean isLeapYear(int year) {
		if (year % 400 == 0 || year % 100 != 0 && year % 4 == 0) {
			return true;
		}
		return false;
	}

//	获取当前月份有多少天
	public static int getDaysOfMonth(int year, int month) {
		switch (month) {
		case 1:
		case 3:
		case 5:
		case 7:
		case 8:
		case 10:
		case 12:
			return 31;
		case 4:
		case 6:
		case 9:
		case 11:
			return 30;
		default:
			if (isLeapYear(year)) {
				return 29;
			}
			return 28;
		}
	}

	public static Date getTodayStartTime() {
		Calendar calendar = Calendar.getInstance();
		calendar.setTime(new Date());
		calendar.set(Calendar.HOUR_OF_DAY, 0);
		calendar.set(Calendar.MINUTE, 0);
		calendar.set(Calendar.SECOND, 0);
		calendar.set(Calendar.MILLISECOND, 0);
		return calendar.getTime();
	}

	public static Date getStartTime(Date date) {
		Calendar calendar = Calendar.getInstance();
		calendar.setTime(date);
		calendar.set(Calendar.HOUR_OF_DAY, 0);
		calendar.set(Calendar.MINUTE, 0);
		calendar.set(Calendar.SECOND, 0);
		calendar.set(Calendar.MILLISECOND, 0);

		return calendar.getTime();
	}

	/**
	 * 判断时间是不是今年
	 * @params date
	 * @return    是返回true，不是返回false
	 * Integer 类型的值是否相等，用  equals() 方法
	 */
	private static boolean isYear(long longString) {
		return getCurrentYear().equals(getYear(longString*1000l));
	}

	/**
	 * 判断时间是不是昨天
	 * @params date
	 * @return    是返回true，不是返回false
	 */
	private static boolean isYesterday(Long longString) {
		boolean isYesterday = false;
		try {
			Date date= DATE_FORMAT_ENYMD.parse(DATE_FORMAT_ENYMD.format(new Date()));
			if (longString < date.getTime() && longString > (date.getTime() -86400000)) {
				isYesterday = true;
			}
		} catch (ParseException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return isYesterday;
	}

	/**
	 * 判断时间是不是今天
	 * @params date
	 * @return    是返回true，不是返回false
	 */
	private static boolean isToday(Long longString) {
		Date now = new Date();
		SimpleDateFormat sf = new SimpleDateFormat("yyyyMMdd");
		//获取今天的日期
		String nowDay = sf.format(now);
		//对比的时间
		String day = sf.format(longString*1000l);

		return day.equals(nowDay);
	}

	public static String getFoolishTime2(String longString) {
		if (longString != null && !longString.equals("null")&& !longString.equals("")) {
			Long mTime=Long.parseLong(longString);
			String s;
			if(isToday(mTime/1000)){
				return DATE_FORMAT_ENHM.format(new Date(mTime));
			}else if(isYesterday(mTime)){
				return "昨天 "+DATE_FORMAT_ENHM.format(new Date(mTime));
			}else{
				return DATE_FORMAT_ENYMD.format(new Date(mTime));
			}
		}else{
			return "";
		}
	}

	public static String getFoolishTime(int longString) {//傻逼要的时间格式
		if(isYear(longString*1l)){
			if(isToday(longString*1l)){
				SimpleDateFormat df2 = new SimpleDateFormat("HH:mm");
				return getTime(longString, df2);
			}else{
				SimpleDateFormat df3 = new SimpleDateFormat("MM-dd HH:mm");
				return getTime(longString, df3);
			}
		}else{
			SimpleDateFormat df1 = new SimpleDateFormat("yyyy-MM-dd");
			return getTime(longString, df1);
		}
	}

	
	public static String getFriendlyTime(String longString) {
		return getFriendlyTime(longString,DATE_FORMAT_ENYMD);
	}
	
	public static String getFriendlyTime(String longString,SimpleDateFormat format) {
		if (longString != null && !longString.equals("null")&& !longString.equals("")) {
			Date now = new Date();
			String s;
			long longs = Long.parseLong(longString);
			long lon = now.getTime() - longs;
			if (lon > 0) {
				if (lon < 1000 * 60) {
					s = "刚刚";
//					s = lon / 1000 + "秒前";
				} else if (lon > 1000 * 60 && lon < 1000 * 3600) {
					s = lon / 60000 + "分钟前";
				} else if (lon > 1000 * 3600 && lon < 86400000) {
					s = lon / 3600000 + "小时前";
				} else if (lon > 86400000 && lon < 86400000 * 2) {
					s = lon / 86400000 + "天前";
				} else {
					s = format.format(new Date(longs));
				}
				return s;
			} else {
				return format.format(new Date(longs));
			}
		}else{
			return "";
		}
	}

	/**
	 * 判断是否在营业时间之内
	 *
	 * @param openTime
	 * @param closeTime
	 * @return
	 */
	public static boolean isBusiness(String openTime, String closeTime) {

		SimpleDateFormat df1 = new SimpleDateFormat("yyyy-MM-dd");
		String today = df1.format(new Date());
		SimpleDateFormat df = new SimpleDateFormat("yyyy-MM-dd HH:mm");
		try {
			Date start = df.parse(today + " " + openTime);
			Date end = df.parse(today + " " + closeTime);
			if (start.getTime() > end.getTime()) {
				// 开门时间大于关门时间，则关门时间延后一天
				Calendar endC = Calendar.getInstance();
				endC.setTime(end);
				endC.add(Calendar.DAY_OF_YEAR, 1);
				end = endC.getTime();

			}
			long nowTime = System.currentTimeMillis();
			if (start.getTime() <= nowTime && nowTime <= end.getTime()) {
				// 在营业时间之内
				return true;
			} else {
				return false;
			}
		} catch (ParseException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return true;
	}

	/**
	 * 判断time是否在from，to之内
	 *
	 * @params time 指定日期
	 * @param from 开始日期
	 * @param to   结束日期
	 * @return
	 */
	public static boolean belongCalendar(long from, long to) {
		long current=System.currentTimeMillis()/1000;
		boolean isIn=false;
		if(current<=to&&current>=from)
			isIn=true;
		return isIn;
	}

	/**
	 * @Function：判断是否是真实时间 
	 *
	 * @author： DKjuan，inputDateStr格式yyyy-MM-dd hh:mm
	 *
	 * @date ： 2016-8-12下午4:09:18
	 *
	 */
	public static boolean isTrueDate(String inputDateStr) {
		try {
			SimpleDateFormat df = new SimpleDateFormat("yyyy-MM-dd");
			Date currTime = df.parse(df.format(new Date(System
					.currentTimeMillis())));
			Date inputDate = df.parse(inputDateStr);

			if (inputDate.after(currTime)) { // before
				return true;
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
		return false;
	}

	/**
	 * 获取当前时间
	 *
	 * @return 当前时间 yyyy-MM-dd HH:mm:ss
	 */
	public static String dateTimeNow() {

		Calendar c = Calendar.getInstance();
		String dateTime = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss",
				Locale.CHINESE).format(c.getTime());
		return dateTime;
	}

	/**
	 * 获取当天日期
	 *
	 * @return 当天日期
	 */
	public static String dateDayNow() {
		Calendar c = Calendar.getInstance();
		Date now = c.getTime();
		c.add(Calendar.DATE, +1);
		String dateDay = new SimpleDateFormat("yyyy-MM-dd", Locale.CHINESE)
				.format(now);
		return dateDay;
	}
	
	/**
	 * 获取昨天日期
	 *
	 * @return 当天日期
	 */
	public static String dateYesterday() {
		Calendar c = Calendar.getInstance();
		c.set(Calendar.DATE, c.get(Calendar.DATE) - 1);
		Date now = c.getTime();
		String dateDay = new SimpleDateFormat("yyyy-MM-dd", Locale.CHINESE)
				.format(now);
		return dateDay;
	}
}