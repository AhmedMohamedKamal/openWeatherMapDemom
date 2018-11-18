package com.demo.ahmed.weather.helpers;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;

/**
 * Created by elmar7om on 30/12/2017.
 */

public class StringHelper {
    public static String getDateFormat(String dateStr) {
        if (dateStr.length()<=10)
            return dateStr;
        try {
            SimpleDateFormat sdf = new SimpleDateFormat("M-yyyy-dd");
//        String dateInString = "31-08-1982 10:20:56";
            Date date = sdf.parse(dateStr.substring(0, 10));
            return date.toString().substring(0, 10);
        } catch (ParseException e) {
            e.printStackTrace();
        }
        return "";
    }

}
