package it.unipv.payroll.util;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.GregorianCalendar;

public class TimeManagUtil {

	/*
	 * This class is used to manage the timestamp attribute in the case it's
	 * required Methods that require a string return a Calendar object that
	 * expresses that timestamp Other methods that require a Calendar object
	 * return a string that expresses the timestamp
	 */

	public static String CheckHoliday(String date) throws ParseException {
		Calendar daytobepaid = Calendar.getInstance();
		SimpleDateFormat sdf = new SimpleDateFormat("dd/mm/yyyy");
		daytobepaid.setTime(sdf.parse(date));
		int dayofweek = daytobepaid.get(Calendar.DAY_OF_WEEK);
		if(dayofweek != Calendar.SUNDAY && dayofweek != Calendar.SATURDAY )
			return date;
		else
			return "";
	}
	
	public static String NextFriday(Calendar today) {
		int dayOfWeek = today.get(Calendar.DAY_OF_WEEK);
		int daysUntilNextFriday = Calendar.FRIDAY - dayOfWeek;
		if(daysUntilNextFriday < 0){
		    daysUntilNextFriday = daysUntilNextFriday + 7;
		}
		Calendar nextFriday = (Calendar)today.clone();
		nextFriday.add(Calendar.DAY_OF_MONTH, daysUntilNextFriday);
		if(nextFriday.get(Calendar.WEEK_OF_YEAR) % 2 == 0){
		    nextFriday.add(Calendar.DAY_OF_MONTH, 7);
		}
	    return nextFriday.get(Calendar.DAY_OF_MONTH)+"/0" + nextFriday.get(0) +"/"+nextFriday.get(1);
	}
	
	public static String getLastDayOfMonth(Calendar day) {
		if(day.get(0)<10)
		return day.getActualMaximum(Calendar.DAY_OF_MONTH) + "/0" + day.get(0) +"/"+day.get(1);
		else
			return day.getActualMaximum(Calendar.DAY_OF_MONTH) + "/" + day.get(0) +"/"+day.get(1);
	}
	
	public static Calendar getGCobjectfromString(String Timestamp) throws ParseException {

		SimpleDateFormat sdf = new SimpleDateFormat("dd/MM/yyyy HH:mm:ss");
		Calendar c = new GregorianCalendar();
		c.setTime(sdf.parse(Timestamp));
		return c;
	}

	public static String getStringfromGCobject(Calendar c) {
		if (c == null)
			return "";
		SimpleDateFormat sdf = new SimpleDateFormat("dd/MM/yyyy HH:mm:ss");
		return sdf.format(c.getTime());
	}

	public static String getStringfromDayGCobject(Calendar c) {
		if (c == null)
			return "";
		SimpleDateFormat sdf = new SimpleDateFormat("dd/MM/yyyy");
		return sdf.format(c.getTime());
	}

	public static Calendar getDayGCobjectfromString(String Timestamp) throws ParseException {

		SimpleDateFormat sdf = new SimpleDateFormat("dd/MM/yyyy");
		Calendar c = new GregorianCalendar();
		c.setTime(sdf.parse(Timestamp));
		return c;
	}

	public static Calendar sumdaytoDayGCobjet(String Timestamp, int day) throws ParseException {
		Calendar c = getDayGCobjectfromString(Timestamp);
		if (day < 0)
			return null;
		c.add(Calendar.DAY_OF_MONTH, day);
		return c;
	}

	public static Calendar summonthtoDayGCobjet(String Timestamp, int month) throws ParseException {
		Calendar c = getDayGCobjectfromString(Timestamp);
		if (month < 0)
			return null;
		c.add(Calendar.MONTH, month);
		return c;
	}

	public static boolean ComparetoNow(String date) throws ParseException {

		Calendar now = new GregorianCalendar(); // now

		now.set(Calendar.HOUR_OF_DAY, 0);
		now.set(Calendar.MINUTE, 0);
		now.set(Calendar.SECOND, 0);
		now.set(Calendar.MILLISECOND, 0);

		Calendar c = getDayGCobjectfromString(date);

		c.set(Calendar.HOUR_OF_DAY, 0);
		c.set(Calendar.MINUTE, 0);
		c.set(Calendar.SECOND, 0);
		c.set(Calendar.MILLISECOND, 0);
		if (c.compareTo(now) == 0)
			return true; // it's ok if day, month and year are the same
		else
			return false;
	}

	public static boolean CheckFuture(String date) throws ParseException {

		Calendar c = getDayGCobjectfromString(date);
		Calendar now = new GregorianCalendar(); // now

		now.set(Calendar.HOUR_OF_DAY, 0);
		now.set(Calendar.MINUTE, 0);
		now.set(Calendar.SECOND, 0);
		now.set(Calendar.MILLISECOND, 0);

		c.set(Calendar.HOUR_OF_DAY, 0);
		c.set(Calendar.MINUTE, 0);
		c.set(Calendar.SECOND, 0);
		c.set(Calendar.MILLISECOND, 0);

		if (c.compareTo(now) <= 0)
			return true; // it's ok if date is before now or exactly now
		else
			return false;

	}
}
