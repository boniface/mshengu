
/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package zm.hashcode.mshengu.app.util;

import java.io.Serializable;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;
import java.util.logging.Level;
import java.util.logging.Logger;
import zm.hashcode.mshengu.domain.products.DaysOfWeekEnum;

/**
 *
 * @author Ferox
 */
public class DateTimeFormatWeeklyHelper implements Serializable {

    private String returnDateFormat = "yyyy-MM-dd";
    private String tableHeaderFormat = "dd-MMM-yyyy";
    private final String standardDateFormat = "yyyy/MM/dd HH:mm:ss";
    private Date date;// = new Date();
    private int dayOfWeekToday;
    private int dayOfWeekYesterday;
    private int dayOfWeekEreyesterday;
    private int dayOfWeekTomorrow;
    private String dayOfWeekTodayStr;
    private String dayOfWeekYesterdayStr;
    private String dayOfWeekEreyesterdayStr;
    private String dayOfWeekTomorrowStr;
    private Date dateToday;
    private Date dateYesterday;
    private Date dateEreyesterday;
    private Date dateTomorrow;
    private String dateTodayStr;
    private String dateYesterdayStr;
    private String dateEreyesterdayStr;
    private String dateTomorrowStr;
    private int minusDaysToLastSunday;
    private int minusDaysToLastSaturday;

    private int tableYear;
    private int tableMonth;
    private String tableMonthStr;
    private int tableWeek;

    /**
     *
     * @param dateIn (any format format)
     * @return Date format 2010-07-14
     */
    public String getYearMonthDay(Date dateIn) {
        if (dateIn != null) {
            return new SimpleDateFormat(getReturnDateFormat()).format(dateIn);
        }
        return null;

    }
    
    public String getDayMonthYear(Date dateIn) {
        if (dateIn != null) {
            return new SimpleDateFormat(getTableHeaderFormat()).format(dateIn);
        }
        return null;

    }

    /*
     * resets the time in a date to zero e.g. Mon Jul 02 23:03:54 SAST 2012
     * to Mon Jul 02 00:00:00 SAST 2012 because the time part makes
     * a big difference btn two dates.
     * Can be used to compare to dates for equality, before or after
     *
     * @param dateIn Date
     * @return Date
     */
    public Date resetTimeOfDate(Date inDate) {
        //
        Calendar cal = Calendar.getInstance();
        cal.setTime(inDate);

        // Set time fields to zero
        cal.set(Calendar.HOUR_OF_DAY, 0);
        cal.set(Calendar.MINUTE, 0);
        cal.set(Calendar.SECOND, 0);
        cal.set(Calendar.MILLISECOND, 0);

        return cal.getTime();

    }


    /*
     * reset the time of a Date to 00:00:00 and resets the month to the
     * first day of the month
     *
     * @param dateIn Date
     * @return Date
     */
    public Date resetTimeAndMonthStart(Date inDate) {
        //
        Calendar cal = Calendar.getInstance();
        cal.setTime(resetTimeOfDate(inDate));

        // will reset to first day of current month
        cal.set(Calendar.DAY_OF_MONTH, 1);

        return cal.getTime();

    }

    /*
     * reset the time of a Date to 00:00:00 and resets the month to the
     * last day of the month
     *
     * @param dateIn Date
     * @return Date
     */
    public Date resetTimeAndMonthEnd(Date inDate) {
        //
        Calendar cal = Calendar.getInstance();
        cal.setTime(resetTimeOfDate(inDate));

        // will reset to Last day of current month
        cal.set(Calendar.DAY_OF_MONTH, cal.getActualMaximum(Calendar.DAY_OF_MONTH));

        return cal.getTime();

    }

    public Date getTodaysDate_No_HTMSM() {
        //
        Calendar cal = Calendar.getInstance();
        cal.setTime(date);

        // Set time fields to zero
        cal.set(Calendar.HOUR_OF_DAY, 0);
        cal.set(Calendar.MINUTE, 0);
        cal.set(Calendar.SECOND, 0);
        cal.set(Calendar.MILLISECOND, 0);

        return cal.getTime();

    }

    public Date getTomorrowsDate_No_HTMSM() {
        //
        Calendar cal = Calendar.getInstance();
        cal.setTime(date);
        cal.add(Calendar.DAY_OF_MONTH, 1);

        // Set time fields to zero
        cal.set(Calendar.HOUR_OF_DAY, 0);
        cal.set(Calendar.MINUTE, 0);
        cal.set(Calendar.SECOND, 0);
        cal.set(Calendar.MILLISECOND, 0);

        return cal.getTime();

    }

    public Date getYesterdayssDate_No_HTMSM() {
        //
        Calendar cal = Calendar.getInstance();
        cal.setTime(date);
        cal.add(Calendar.DAY_OF_MONTH, -1);

        // Set time fields to zero
        cal.set(Calendar.HOUR_OF_DAY, 0);
        cal.set(Calendar.MINUTE, 0);
        cal.set(Calendar.SECOND, 0);
        cal.set(Calendar.MILLISECOND, 0);

        return cal.getTime();

    }

    public Date getDate_No_HMSM(int year, int month, int day) {
        Calendar cal = Calendar.getInstance();

        cal.set(Calendar.YEAR, year);
        cal.set(Calendar.MONTH, month);
        cal.set(Calendar.DAY_OF_MONTH, day);
        cal.set(Calendar.HOUR_OF_DAY, 0);
        cal.set(Calendar.MINUTE, 0);
        cal.set(Calendar.SECOND, 0);
        cal.set(Calendar.MILLISECOND, 0);
        return cal.getTime();
    }

    public Date getDate_No_HMSM(String dateIn) {
        Date dateOut = new Date();
        try {
            //
            Calendar cal = Calendar.getInstance();
            cal.setTime(new SimpleDateFormat(standardDateFormat).parse(dateIn));

            // Set time fields to zero
            cal.set(Calendar.HOUR_OF_DAY, 0);
            cal.set(Calendar.MINUTE, 0);
            cal.set(Calendar.SECOND, 0);
            cal.set(Calendar.MILLISECOND, 0);

            dateOut = cal.getTime();
        } catch (ParseException ex) {
            Logger.getLogger(DateTimeFormatWeeklyHelper.class.getName()).log(Level.SEVERE, null, ex);
        } finally {
            return dateOut;
        }

    }

    public Date getMondayDateFull() {
        Calendar calendar = Calendar.getInstance();
//        Date date = new Date();
        calendar.setTime(getDate());
        calendar.setFirstDayOfWeek(2);
        calendar.set(Calendar.DAY_OF_WEEK, Calendar.MONDAY);
        return calendar.getTime();

    }

    public String getMondayDateYYMMDD() {
        return getYearMonthDay(getMondayDateFull());
    }

    public Date getTuesdayDateFull() {
        Calendar calendar = Calendar.getInstance();
//        Date date = new Date();
        calendar.setTime(getDate());
        calendar.setFirstDayOfWeek(2);
        calendar.set(Calendar.DAY_OF_WEEK, Calendar.TUESDAY);
        return calendar.getTime();

    }

    public String getTuesdayDateYYMMDD() {
        return getYearMonthDay(getTuesdayDateFull());
    }

    public Date getWednesdayDateFull() {
        Calendar calendar = Calendar.getInstance();
//        Date date = new Date();
        calendar.setTime(getDate());
        calendar.setFirstDayOfWeek(2);
        calendar.set(Calendar.DAY_OF_WEEK, Calendar.WEDNESDAY);
        return calendar.getTime();

    }

    public String getWednesdayDateYYMMDD() {
        return getYearMonthDay(getWednesdayDateFull());
    }

    public Date getThursdayDateFull() {
        Calendar calendar = Calendar.getInstance();
//        Date date = new Date();
        calendar.setTime(getDate());
        calendar.setFirstDayOfWeek(2);
        calendar.set(Calendar.DAY_OF_WEEK, Calendar.THURSDAY);
        return calendar.getTime();

    }

    public String getThursdayDateYYMMDD() {
        return getYearMonthDay(getThursdayDateFull());
    }

    public Date getFridayDateFull() {
        Calendar calendar = Calendar.getInstance();
//        Date date = new Date();
        calendar.setTime(getDate());
        calendar.setFirstDayOfWeek(2);
        calendar.set(Calendar.DAY_OF_WEEK, Calendar.FRIDAY);
        return calendar.getTime();

    }

    public String getFridayDateYYMMDD() {
        return getYearMonthDay(getFridayDateFull());
    }

    public Date getSaturdayDateFull() {
        Calendar calendar = Calendar.getInstance();
//        Date date = new Date();
        calendar.setTime(getDate());
        calendar.setFirstDayOfWeek(2);
        calendar.set(Calendar.DAY_OF_WEEK, Calendar.SATURDAY);
        return calendar.getTime();

    }

    public String getSaturdayDateYYMMDD() {
        return getYearMonthDay(getSaturdayDateFull());
    }

    public Date getSundayDateFull() {
        Calendar calendar = Calendar.getInstance();
//        Date date = new Date();
        calendar.setTime(getDate());
        calendar.setFirstDayOfWeek(2);
        calendar.set(Calendar.DAY_OF_WEEK, Calendar.SUNDAY);
        return calendar.getTime();

    }

    public String getSundayDateYYMMDD() {
        return getYearMonthDay(getSundayDateFull());
    }

    public Date getLastSaturdayDateFull() {
        Calendar calendar2 = Calendar.getInstance();
//        Date date = new Date();
        calendar2.setTime(getDate());
        calendar2.setFirstDayOfWeek(2);
        calendar2.set(Calendar.DAY_OF_WEEK, Calendar.MONDAY);
        calendar2.add(Calendar.DAY_OF_MONTH, -2);
        return calendar2.getTime();

    }

    public String getLastSaturdayDateYYMMDD() {
        return getYearMonthDay(getLastSaturdayDateFull());
    }

    public Date getLastSundayDateFull() {
        Calendar calendar3 = Calendar.getInstance();
//        Date date = new Date();
        calendar3.setTime(getDate());
        calendar3.setFirstDayOfWeek(2);
        calendar3.set(Calendar.DAY_OF_WEEK, Calendar.MONDAY);
        calendar3.add(Calendar.DAY_OF_MONTH, -1);
        return calendar3.getTime();

    }

    public String getLastSundayDateYYMMDD() {
        return getYearMonthDay(getLastSundayDateFull());
    }

    public Date getNextMondayDateFull() {
        Calendar calendar3 = Calendar.getInstance();
//        Date date = new Date();
        calendar3.setTime(getDate());
        calendar3.setFirstDayOfWeek(2);
        calendar3.set(Calendar.DAY_OF_WEEK, Calendar.MONDAY);
        calendar3.add(Calendar.DAY_OF_MONTH, 7);
        return calendar3.getTime();

    }

    public String getNextMondayDateYYMMDD() {
        return getYearMonthDay(getNextMondayDateFull());
    }

    public Date getTodaysDateFull() {
        Calendar calendar4 = Calendar.getInstance();
//        Date date = new Date();
        calendar4.setTime(getDate());
        calendar4.setFirstDayOfWeek(2);
        return calendar4.getTime();

    }

    public String getTodaysDateYYMMDD() {
        return getYearMonthDay(getTodaysDateFull());
    }

    public Date getTomorrowDateFull() {
        Calendar calendar5 = Calendar.getInstance();
//        Date date = new Date();
        calendar5.setTime(getDate());
        calendar5.setFirstDayOfWeek(2);
        calendar5.add(Calendar.DAY_OF_MONTH, 1);
        return calendar5.getTime();

    }

    public String getTomorrowDateYYMMDD() {
        return getYearMonthDay(getTomorrowDateFull());
    }

    public Date getYesterdayDateFull() {
        Calendar calendar6 = Calendar.getInstance();
//        Date date = new Date();
        calendar6.setTime(getDate());
        calendar6.setFirstDayOfWeek(2);
        calendar6.add(Calendar.DAY_OF_MONTH, -1);
        return calendar6.getTime();

    }

    public String getYesterdayDateYYMMDD() {
        return getYearMonthDay(getYesterdayDateFull());
    }

    public int getDayOfWeek() {
        Calendar calendar = Calendar.getInstance();
//        Date date = new Date();
        calendar.setTime(getDate());
        return calendar.get(Calendar.DAY_OF_WEEK);
    }

    public void resetDayOfWeek() {
        switch (getDayOfWeek()) {
            case 1:
                setDayOfWeekTomorrow(2);
                setDayOfWeekTomorrowStr(DaysOfWeekEnum.MONDAY.name());
                setDateTomorrow(getNextMondayDateFull());
                setDateTomorrowStr(getNextMondayDateYYMMDD());
                setDayOfWeekToday(1);
                setDayOfWeekYesterday(7);
                setDayOfWeekEreyesterday(6);
                setDayOfWeekTodayStr(DaysOfWeekEnum.SUNDAY.name());
                setDayOfWeekYesterdayStr(DaysOfWeekEnum.SATURDAY.name());
                setDayOfWeekEreyesterdayStr(DaysOfWeekEnum.FRIDAY.name());
                setDateToday(getSundayDateFull());
                setDateYesterday(getSaturdayDateFull());
                setDateEreyesterday(getFridayDateFull());
                setDateTodayStr(getSundayDateYYMMDD());
                setDateYesterdayStr(getSaturdayDateYYMMDD());
                setDateEreyesterdayStr(getFridayDateYYMMDD());
                break;
            case 2:
                setDayOfWeekTomorrow(3);
                setDayOfWeekTomorrowStr(DaysOfWeekEnum.TUESDAY.name());
                setDateTomorrow(getTuesdayDateFull());
                setDateTomorrowStr(getTuesdayDateYYMMDD());
                setDayOfWeekToday(2);
                setDayOfWeekYesterday(1);
                setDayOfWeekEreyesterday(7);
                setDayOfWeekTodayStr(DaysOfWeekEnum.MONDAY.name());
                setDayOfWeekYesterdayStr(DaysOfWeekEnum.SUNDAY.name());
                setDayOfWeekEreyesterdayStr(DaysOfWeekEnum.SATURDAY.name());
                setDateToday(getMondayDateFull());
                setDateYesterday(getLastSundayDateFull());
                setDateEreyesterday(getLastSaturdayDateFull());
                setDateTodayStr(getMondayDateYYMMDD());
                setDateYesterdayStr(getLastSundayDateYYMMDD());
                setDateEreyesterdayStr(getLastSaturdayDateYYMMDD());
//                dayOfWeek = DaysOfWeekEnum.MONDAY.name();
//                minusDaysToLastSunday = 1;
//                minusDaysToLastSaturday = 2;
                break;
            case 3:
//                minusDaysToLastSunday = 2;
//                minusDaysToLastSaturday = 3;
                setDayOfWeekTomorrow(4);
                setDayOfWeekTomorrowStr(DaysOfWeekEnum.WEDNESDAY.name());
                setDateTomorrow(getWednesdayDateFull());
                setDateTomorrowStr(getWednesdayDateYYMMDD());
                setDayOfWeekToday(3);
                setDayOfWeekYesterday(2);
                setDayOfWeekEreyesterday(1);
                setDayOfWeekTodayStr(DaysOfWeekEnum.TUESDAY.name());
                setDayOfWeekYesterdayStr(DaysOfWeekEnum.MONDAY.name());
                setDayOfWeekEreyesterdayStr(DaysOfWeekEnum.SUNDAY.name());
                setDateToday(getTuesdayDateFull());
                setDateYesterday(getMondayDateFull());
                setDateEreyesterday(getLastSundayDateFull());
                setDateTodayStr(getTuesdayDateYYMMDD());
                setDateYesterdayStr(getMondayDateYYMMDD());
                setDateEreyesterdayStr(getLastSundayDateYYMMDD());
//                dayOfWeek = DaysOfWeekEnum.TUESDAY.name();
                break;
            case 4:
//                minusDaysToLastSunday = 3;
//                minusDaysToLastSaturday = 4;
                setDayOfWeekTomorrow(5);
                setDayOfWeekTomorrowStr(DaysOfWeekEnum.THURSDAY.name());
                setDateTomorrow(getThursdayDateFull());
                setDateTomorrowStr(getThursdayDateYYMMDD());
                setDayOfWeekToday(4);
                setDayOfWeekYesterday(3);
                setDayOfWeekEreyesterday(2);
                setDayOfWeekTodayStr(DaysOfWeekEnum.WEDNESDAY.name());
                setDayOfWeekYesterdayStr(DaysOfWeekEnum.TUESDAY.name());
                setDayOfWeekEreyesterdayStr(DaysOfWeekEnum.MONDAY.name());
                setDateToday(getWednesdayDateFull());
                setDateYesterday(getTuesdayDateFull());
                setDateEreyesterday(getMondayDateFull());
                setDateTodayStr(getWednesdayDateYYMMDD());
                setDateYesterdayStr(getTuesdayDateYYMMDD());
                setDateEreyesterdayStr(getMondayDateYYMMDD());
                break;
            case 5:
//                minusDaysToLastSunday = 4;
//                minusDaysToLastSaturday = 5;
                setDayOfWeekTomorrow(6);
                setDayOfWeekTomorrowStr(DaysOfWeekEnum.FRIDAY.name());
                setDateTomorrow(getFridayDateFull());
                setDateTomorrowStr(getFridayDateYYMMDD());
                setDayOfWeekToday(5);
                setDayOfWeekYesterday(4);
                setDayOfWeekEreyesterday(3);
                setDayOfWeekTodayStr(DaysOfWeekEnum.THURSDAY.name());
                setDayOfWeekYesterdayStr(DaysOfWeekEnum.WEDNESDAY.name());
                setDayOfWeekEreyesterdayStr(DaysOfWeekEnum.TUESDAY.name());
                setDateToday(getThursdayDateFull());
                setDateYesterday(getWednesdayDateFull());
                setDateEreyesterday(getTuesdayDateFull());
                setDateTodayStr(getThursdayDateYYMMDD());
                setDateYesterdayStr(getWednesdayDateYYMMDD());
                setDateEreyesterdayStr(getTuesdayDateYYMMDD());
                break;
            case 6:
//                minusDaysToLastSunday = 5;
//                minusDaysToLastSaturday = 6;
                setDayOfWeekTomorrow(7);
                setDayOfWeekTomorrowStr(DaysOfWeekEnum.SATURDAY.name());
                setDateTomorrow(getSaturdayDateFull());
                setDateTomorrowStr(getSaturdayDateYYMMDD());
                setDayOfWeekToday(6);
                setDayOfWeekYesterday(5);
                setDayOfWeekEreyesterday(4);
                setDayOfWeekTodayStr(DaysOfWeekEnum.FRIDAY.name());
                setDayOfWeekYesterdayStr(DaysOfWeekEnum.THURSDAY.name());
                setDayOfWeekEreyesterdayStr(DaysOfWeekEnum.WEDNESDAY.name());
                setDateToday(getFridayDateFull());
                setDateYesterday(getThursdayDateFull());
                setDateEreyesterday(getWednesdayDateFull());
                setDateTodayStr(getFridayDateYYMMDD());
                setDateYesterdayStr(getThursdayDateYYMMDD());
                setDateEreyesterdayStr(getWednesdayDateYYMMDD());
                break;
            case 7:
//                minusDaysToLastSunday = 6;
//                minusDaysToLastSaturday = 7;
                setDayOfWeekTomorrow(1);
                setDayOfWeekTomorrowStr(DaysOfWeekEnum.SUNDAY.name());
                setDateTomorrow(getSundayDateFull());
                setDateTomorrowStr(getSundayDateYYMMDD());
                setDayOfWeekToday(7);
                setDayOfWeekYesterday(6);
                setDayOfWeekEreyesterday(5);
                setDayOfWeekTodayStr(DaysOfWeekEnum.SATURDAY.name());
                setDayOfWeekYesterdayStr(DaysOfWeekEnum.FRIDAY.name());
                setDayOfWeekEreyesterdayStr(DaysOfWeekEnum.THURSDAY.name());
                setDateToday(getSaturdayDateFull());
                setDateYesterday(getFridayDateFull());
                setDateEreyesterday(getThursdayDateFull());
                setDateTodayStr(getSaturdayDateYYMMDD());
                setDateYesterdayStr(getFridayDateYYMMDD());
                setDateEreyesterdayStr(getSundayDateYYMMDD());
                break;
            default:
//                dayOfWeek = "DAY_NOT_FOUND";
                break;

        }

//        return dayOfWeek;
    }
    /* public String getMondayDateYYMMDD() {
     return getYearMonthDay("");
     }
     System.out.println("Today:  return calendar.getTime();
     //        c.setTime(date;
     calendar.set(Calendar.DAY_OF_WEEK, Calendar.MONDAY;
     System.out.println("MONDAY: return calendar.getTime());
     //        c.setTime(date);
     calendar.set(Calendar.DAY_OF_WEEK, Calendar.TUESDAY);
     System.out.println("TUESDAY: return calendar.getTime());
     //        c.setTime(date);
     calendar.set(Calendar.DAY_OF_WEEK, Calendar.WEDNESDAY);
     System.out.println("WEDNESDAY: return calendar.getTime());
     //        c.setTime(date);
     calendar.set(Calendar.DAY_OF_WEEK, Calendar.THURSDAY);
     System.out.println("THURSDAY: return calendar.getTime());
     //        c.setTime(date);
     calendar.set(Calendar.DAY_OF_WEEK, Calendar.FRIDAY);
     System.out.println("FRIDAY: return calendar.getTime());
     //        c.setTime(date);
     calendar.set(Calendar.DAY_OF_WEEK, Calendar.SATURDAY);
     System.out.println("SATURDAY: return calendar.getTime());
     //        c.setTime(date);
     calendar.set(Calendar.DAY_OF_WEEK, Calendar.SUNDAY);
     System.out.println("SUNDAY: return calendar.getTime());
     */

    /**
     * @return the returnDateFormat
     */
    public String getReturnDateFormat() {
        return returnDateFormat;
    }
    public String getTableHeaderFormat() {
        return tableHeaderFormat;
    }
    
    

    /**
     * @param returnDateFormat the returnDateFormat to set
     */
    private void setReturnDateFormat(String returnDateFormat) {
        this.returnDateFormat = returnDateFormat;
    }

    /**
     * @return the dayOfWeekToday
     */
    public int getDayOfWeekToday() {
        return dayOfWeekToday;
    }

    /**
     * @param dayOfWeekToday the dayOfWeekToday to set
     */
    private void setDayOfWeekToday(int dayOfWeekToday) {
        this.dayOfWeekToday = dayOfWeekToday;
    }

    /**
     * @return the dayOfWeekYesterday
     */
    public int getDayOfWeekYesterday() {
        return dayOfWeekYesterday;
    }

    /**
     * @param dayOfWeekYesterday the dayOfWeekYesterday to set
     */
    private void setDayOfWeekYesterday(int dayOfWeekYesterday) {
        this.dayOfWeekYesterday = dayOfWeekYesterday;
    }

    /**
     * @return the dayOfWeekEreyesterday
     */
    public int getDayOfWeekEreyesterday() {
        return dayOfWeekEreyesterday;
    }

    /**
     * @param dayOfWeekEreyesterday the dayOfWeekEreyesterday to set
     */
    private void setDayOfWeekEreyesterday(int dayOfWeekEreyesterday) {
        this.dayOfWeekEreyesterday = dayOfWeekEreyesterday;
    }

    /**
     * @return the dayOfWeekTodayStr
     */
    public String getDayOfWeekTodayStr() {
        return dayOfWeekTodayStr;
    }

    /**
     * @param dayOfWeekTodayStr the dayOfWeekTodayStr to set
     */
    private void setDayOfWeekTodayStr(String dayOfWeekTodayStr) {
        this.dayOfWeekTodayStr = dayOfWeekTodayStr;
    }

    /**
     * @return the dayOfWeekYesterdayStr
     */
    public String getDayOfWeekYesterdayStr() {
        return dayOfWeekYesterdayStr;
    }

    /**
     * @param dayOfWeekYesterdayStr the dayOfWeekYesterdayStr to set
     */
    private void setDayOfWeekYesterdayStr(String dayOfWeekYesterdayStr) {
        this.dayOfWeekYesterdayStr = dayOfWeekYesterdayStr;
    }

    /**
     * @return the dayOfWeekEreyesterdayStr
     */
    public String getDayOfWeekEreyesterdayStr() {
        return dayOfWeekEreyesterdayStr;
    }

    /**
     * @param dayOfWeekEreyesterdayStr the dayOfWeekEreyesterdayStr to set
     */
    private void setDayOfWeekEreyesterdayStr(String dayOfWeekEreyesterdayStr) {
        this.dayOfWeekEreyesterdayStr = dayOfWeekEreyesterdayStr;
    }

    /**
     * @return the dateToday
     */
    public Date getDateToday() {
        return dateToday;
    }

    /**
     * @param dateToday the dateToday to set
     */
    private void setDateToday(Date dateToday) {
        this.dateToday = dateToday;
    }

    /**
     * @return the dateYesterday
     */
    public Date getDateYesterday() {
        return dateYesterday;
    }

    /**
     * @param dateYesterday the dateYesterday to set
     */
    private void setDateYesterday(Date dateYesterday) {
        this.dateYesterday = dateYesterday;
    }

    /**
     * @return the dateEreyesterday
     */
    public Date getDateEreyesterday() {
        return dateEreyesterday;
    }

    /**
     * @param dateEreyesterday the dateEreyesterday to set
     */
    private void setDateEreyesterday(Date dateEreyesterday) {
        this.dateEreyesterday = dateEreyesterday;
    }

    /**
     * @return the dateTodayStr
     */
    public String getDateTodayStr() {
        return dateTodayStr;
    }

    /**
     * @param dateTodayStr the dateTodayStr to set
     */
    private void setDateTodayStr(String dateTodayStr) {
        this.dateTodayStr = dateTodayStr;
    }

    /**
     * @return the dateYesterdayStr
     */
    public String getDateYesterdayStr() {
        return dateYesterdayStr;
    }

    /**
     * @param dateYesterdayStr the dateYesterdayStr to set
     */
    private void setDateYesterdayStr(String dateYesterdayStr) {
        this.dateYesterdayStr = dateYesterdayStr;
    }

    /**
     * @return the dateEreyesterdayStr
     */
    public String getDateEreyesterdayStr() {
        return dateEreyesterdayStr;
    }

    /**
     * @param dateEreyesterdayStr the dateEreyesterdayStr to set
     */
    private void setDateEreyesterdayStr(String dateEreyesterdayStr) {
        this.dateEreyesterdayStr = dateEreyesterdayStr;
    }

    /**
     * @return the minusDaysToLastSunday
     */
    public int getMinusDaysToLastSunday() {
        return minusDaysToLastSunday;
    }

    /**
     * @param minusDaysToLastSunday the minusDaysToLastSunday to set
     */
    private void setMinusDaysToLastSunday(int minusDaysToLastSunday) {
        this.minusDaysToLastSunday = minusDaysToLastSunday;
    }

    /**
     * @return the minusDaysToLastSaturday
     */
    public int getMinusDaysToLastSaturday() {
        return minusDaysToLastSaturday;
    }

    /**
     * @param minusDaysToLastSaturday the minusDaysToLastSaturday to set
     */
    private void setMinusDaysToLastSaturday(int minusDaysToLastSaturday) {
        this.minusDaysToLastSaturday = minusDaysToLastSaturday;
    }

    /**
     * @return the dayOfWeekTomorrowStr
     */
    public String getDayOfWeekTomorrowStr() {
        return dayOfWeekTomorrowStr;
    }

    /**
     * @param dayOfWeekTomorrowStr the dayOfWeekTomorrowStr to set
     */
    private void setDayOfWeekTomorrowStr(String dayOfWeekTomorrowStr) {
        this.dayOfWeekTomorrowStr = dayOfWeekTomorrowStr;
    }

    /**
     * @return the dateTomorrow
     */
    public Date getDateTomorrow() {
        return dateTomorrow;
    }

    /**
     * @param dateTomorrow the dateTomorrow to set
     */
    private void setDateTomorrow(Date dateTomorrow) {
        this.dateTomorrow = dateTomorrow;
    }

    /**
     * @return the dateTomorrowStr
     */
    public String getDateTomorrowStr() {
        return dateTomorrowStr;
    }

    /**
     * @param dateTomorrowStr the dateTomorrowStr to set
     */
    private void setDateTomorrowStr(String dateTomorrowStr) {
        this.dateTomorrowStr = dateTomorrowStr;
    }

    /**
     * @return the dayOfWeekTomorrow
     */
    public int getDayOfWeekTomorrow() {
        return dayOfWeekTomorrow;
    }

    /**
     * @param dayOfWeekTomorrow the dayOfWeekTomorrow to set
     */
    private void setDayOfWeekTomorrow(int dayOfWeekTomorrow) {
        this.dayOfWeekTomorrow = dayOfWeekTomorrow;
    }

    /**
     * @return the date
     */
    public Date getDate() {
        return date;
    }

    /**
     * @param date the date to set
     */
    public void setDate(Date date) {
        this.date = date;
        resetDayOfWeek();
    }

    public Date getDate(int day, int month, int year) {
        Calendar calendar = new GregorianCalendar();

        calendar.set(Calendar.YEAR, year);
        calendar.set(Calendar.MONTH, month);
        calendar.set(Calendar.DAY_OF_MONTH, day);
        return calendar.getTime();
    }

//        private int tableYear;
//    private int tableMonth;
//    private String tableMonthStr;
//    private int tableWeek;
    public int getYearForTbaleFormat() {
        Calendar calendar = new GregorianCalendar();
        calendar.setTime(getDate());
        System.out.println("Calendar.YEAR = " + Calendar.YEAR);
        return calendar.get(Calendar.YEAR);
    }

    public String getMonthForTbaleFormat() {
        Calendar calendar = new GregorianCalendar();
        calendar.setTime(getDate());
        String month = "";
        switch (calendar.get(Calendar.MONTH)) {
            case 0:
                month = "January";
                break;
            case 1:
                month = "February";
                break;
            case 2:
                month = "March";
                break;
            case 3:
                month = "April";
                break;
            case 4:
                month = "May";
                break;
            case 5:
                month = "June";
                break;
            case 6:
                month = "July";
                break;
            case 7:
                month = "August";
                break;
            case 8:
                month = "September";
                break;
            case 9:
                month = "October";
                break;
            case 10:
                month = "November";
                break;
            case 11:
                month = "December";
                break;
        }
//            return Calendar.MONTH;
        return month;
    }

    public int getWeekForTbaleFormat() {
        Calendar calendar = new GregorianCalendar();
        calendar.setTime(getDate());
        return calendar.get(Calendar.WEEK_OF_MONTH);
    }

    public String getMondayForTbaleFormat() {
        Calendar calendar = Calendar.getInstance();
        calendar.setFirstDayOfWeek(2);
        calendar.set(Calendar.DAY_OF_WEEK, Calendar.MONDAY);
        return getDayMonthYear(calendar.getTime());

    }
    
       public String getSundayForTbaleFormat() {
        Calendar calendar = Calendar.getInstance();
        calendar.setFirstDayOfWeek(2);
        calendar.set(Calendar.DAY_OF_WEEK, Calendar.SUNDAY);
        return getDayMonthYear(calendar.getTime());

    }
      
       public String getTodayForTbaleFormat() {
        return getDayMonthYear(new Date());

    }
}
