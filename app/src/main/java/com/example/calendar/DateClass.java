package com.example.calendar;

import android.annotation.SuppressLint;
import android.os.Build;

import androidx.annotation.RequiresApi;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.time.LocalDate;
import java.util.Calendar;
import java.util.Date;

public class DateClass {
    String day, day_number, month, year;
    Date currentTime;
    SimpleDateFormat formattedDateFile;

    @SuppressLint("SimpleDateFormat")
    DateClass(){
        currentTime = Calendar.getInstance().getTime();
        String formattedDate = DateFormat.getDateInstance(DateFormat.FULL).format(currentTime);

        formattedDateFile = new SimpleDateFormat("yyyyMMdd");

        String[] splitDate = formattedDate.replace(",", "").split(" ");
        day = splitDate[0];
        day_number = splitDate[1];
        month = splitDate[2];
        year = splitDate[3];
    }

    @RequiresApi(api = Build.VERSION_CODES.O)
    DateClass (String date){
        LocalDate dated = LocalDate.parse(CalculateUtil.convertDate(date));
        day = String.valueOf(dated.getDayOfWeek()).substring(0,1).toUpperCase() + String.valueOf(dated.getDayOfWeek()).substring(1).toLowerCase();
        day_number = String.valueOf(dated.getDayOfMonth());
        month = String.valueOf(dated.getMonth()).substring(0,1).toUpperCase() + String.valueOf(dated.getMonth()).substring(1).toLowerCase();
        year = String.valueOf(dated.getYear());
    }

    String getDateMonth(){
        return month + " " + year;
    }

    String getDay(){
        return day + ", " + day_number;
    }

    String getSimpleDate(){
        return formattedDateFile.format(currentTime);
    }

    /* Translation date */
    /*

     SimpleDateFormat dateFrDay = new SimpleDateFormat("EEEE", Locale.FRANCE);
        String dateTranslate = dateFrDay.format(currentTime);
        String dateTranslateDayForm = dateTranslate.substring(0,1).toUpperCase() + dateTranslate.substring(1);
        SimpleDateFormat dateFrMonth = new SimpleDateFormat("d MMMM yyyy", Locale.FRANCE);
        String dateTranslateMonth = dateFrMonth.format(currentTime);
        String dateTranslateDayFormMonth = dateTranslateMonth.substring(0,2) + dateTranslateMonth.substring(2,3).toUpperCase() + dateTranslateMonth.substring(3);
        Log.d("myLog", "Date : " + " | " + dateTranslateDayForm + ", " + dateTranslateDayFormMonth);

     */

    /* number place Day and month  */
    /*

    Date date = new Date();
        LocalDate localDate = date.toInstant().atZone(ZoneId.systemDefault()).toLocalDate();
        int month = localDate.getMonthValue();
        DayOfWeek days = localDate.getDayOfWeek();
        //int dayInt = localDate.get(Calendar.days);
        dayOfTheWeek = DayOfWeek.valueOf(String.valueOf(days)).getValue();
        Log.d("myLog", "local date : " + localDate + " " + month + " " + days + " " + dayOfTheWeek);

     */
}
