package com.example.YuCeClient.util;

import com.example.YuCeClient.R;
import com.example.YuCeClient.background.HCApplicaton;

import java.util.Calendar;

/**
 * Created by xiaoyu on 2015/7/4.
 */
public class StringUtil {

    // 几分钟前，几小时前，昨天，几天前，一年前，仿照微信的展现形式
    public static String getTimeLineTime(String time) {
        StringBuilder sb = new StringBuilder();
        long mss = System.currentTimeMillis() - Long.parseLong(time);
        long oneMinute = 60 * 1000;
        long oneHour = oneMinute * 60;
        Calendar calendar = Calendar.getInstance();
        int intHour = calendar.get(Calendar.HOUR_OF_DAY);
        int intMinute = calendar.get(Calendar.MINUTE);
        int intSec = calendar.get(Calendar.SECOND);
        long yesterday = intHour * oneHour + intMinute * oneMinute + intSec * 1000;
        long oneDay = oneHour * 24;
        long oneYear = oneDay * 365;
        if (mss < oneMinute) {
            sb.append(HCApplicaton.getInstance().getString(R.string.oneMinAgo));
        } else if (mss < oneHour) {
            sb.append(mss / oneMinute).append(HCApplicaton.getInstance().getString(R.string.minsAgo));
        } else if (mss < yesterday) {
            sb.append(mss / oneHour).append(HCApplicaton.getInstance().getString(R.string.hoursAgo));
        } else if ((mss - yesterday) < oneDay) {
            sb.append(HCApplicaton.getInstance().getString(R.string.yestaday));
        } else if ((mss - yesterday) < oneYear) {
            sb.append((mss - yesterday) / oneDay).append(HCApplicaton.getInstance().getString(R.string.daysAgo));
        } else {
            sb.append((mss - yesterday) / oneYear).append(HCApplicaton.getInstance().getString(R.string.yearsAgo));
        }
        return sb.toString();
    }

    // 将时间转换成2012年08月12日 这种形式的字符串
    public static String getDateToString3(String time) {
        try {
            Calendar calendar = Calendar.getInstance();
            calendar.setTimeInMillis(Long.valueOf(time));
            int year = calendar.get(Calendar.YEAR);
            int month = calendar.get(Calendar.MONTH) + 1;
            int day = calendar.get(Calendar.DAY_OF_MONTH);
            StringBuffer sb = new StringBuffer();
            sb.append(year).append(HCApplicaton.getInstance().getString(R.string.year));
            sb.append(month).append(HCApplicaton.getInstance().getString(R.string.month)).append(day).append(HCApplicaton.getInstance().getString(R.string.day));
            return sb.toString();
        } catch (NumberFormatException e) {
            return time;
        }
    }

    //return such as 2015/06/13
    public static String getDateToString1(String time) {
        try {
            Calendar calendar = Calendar.getInstance();
            calendar.setTimeInMillis(Long.valueOf(time));
            int year = calendar.get(Calendar.YEAR);
            int month = calendar.get(Calendar.MONTH) + 1;
            int day = calendar.get(Calendar.DAY_OF_MONTH);
            StringBuffer sb = new StringBuffer();
            sb.append(year).append("/");
            if (month >= 10) {
                sb.append(month);
            } else {
                sb.append("0").append(month);
            }
            sb.append("/");
            if (day >= 10) {
                sb.append(day);
            } else {
                sb.append("0").append(day);
            }
            return sb.toString();
        } catch (NumberFormatException e) {
            return time;
        }
    }

    //return such as 2015-06-13 09:40:40
    public static String getDateToString2(String time) {
        try {
            Calendar calendar = Calendar.getInstance();
            calendar.setTimeInMillis(Long.valueOf(time));
            int year = calendar.get(Calendar.YEAR);
            int month = calendar.get(Calendar.MONTH) + 1;
            int day = calendar.get(Calendar.DAY_OF_MONTH);
            int hour = calendar.get(Calendar.HOUR_OF_DAY);
            int min = calendar.get(Calendar.MINUTE);
            int sec = calendar.get(Calendar.SECOND);
            StringBuffer sb = new StringBuffer();
            sb.append(year).append("-");
            if (month >= 10) {
                sb.append(month);
            } else {
                sb.append("0").append(month);
            }
            sb.append("-");
            if (day >= 10) {
                sb.append(day);
            } else {
                sb.append("0").append(day);
            }
            sb.append(" ");
            if (hour >= 10) {
                sb.append(hour);
            } else {
                sb.append("0").append(hour);
            }
            sb.append(":");
            if (min >= 10) {
                sb.append(min);
            } else {
                sb.append("0").append(min);
            }
            sb.append(":");
            if (sec >= 10) {
                sb.append(sec);
            } else {
                sb.append("0").append(sec);
            }
            return sb.toString();
        } catch (NumberFormatException e) {
            return time;
        }
    }
}
