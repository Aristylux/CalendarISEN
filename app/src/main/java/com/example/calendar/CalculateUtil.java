package com.example.calendar;

import android.os.Build;
import android.util.Log;

import androidx.annotation.RequiresApi;

import java.time.DayOfWeek;
import java.time.LocalDate;

public class CalculateUtil {

    /*
     * nbrDayOfTheWeek :
     *
     * example :
     * 20220108
     * check what day is 8 January
     */
    @RequiresApi(api = Build.VERSION_CODES.O)
    public static int nbrDayOfTheWeek(String date){
        String dateStr = convertDate(date);  //convert 20220108 to 2022-01-08
        LocalDate currentDate = LocalDate.parse(dateStr);
        DayOfWeek days = currentDate.getDayOfWeek();
        return DayOfWeek.valueOf(String.valueOf(days)).getValue();
    }

    /*
     * convertDate :
     * convert 20220108 to 2022-01-08
     * for parse
     */
    public static String convertDate(String nextDate) {
        StringBuilder date = new StringBuilder(nextDate);
        date.insert(4,'-');
        date.insert(7,'-');
        //Log.d("myLog", date.toString());
        return date.toString();
    }

    /*
     * isLeap :
     * calculate if this year is leap or not
     * to determine if a year is leap :
     * it is divisible by 4 and but not by 100
     * for example : 2012 is leap but 1997 is not,
     *
     * but !
     * if the year is divisible by 4 and 100 this year is maybe lead
     * you need to verify if is divisible by 400.
     */
    private static boolean isLeap(int year) {
        if ((year % 4 == 0 && year % 100 != 0) || (year % 4 == 0 && year % 100 == 0 && year % 400 == 0)){
            //Log.d("myLogNav", year + " is leap");
            return true;
        }
        //Log.d("myLogNav", year + " is not leap");
        return false;
    }

    /*
     * calculateDate:
     *
     */
    public static String calculateDate(String date, int numberOfDay){
        final int NBR_DAY_FEBRUARY_LEAP = 29;
        final int NBR_DAY_FEBRUARY = 28;
        int[][] monthType = {
                {1, 31},
                {2, 0}, //29 if leap or 28
                {3, 31},
                {4, 30},
                {5, 31},
                {6, 30},
                {7, 31},
                {8, 31},
                {9, 30},
                {10, 31},
                {11, 30},
                {12, 31}
        };
        Log.d("myLogNC", "----------------");
        Log.d("myLogNC", "CalculateDate : " + date + " to " + numberOfDay );
        int len = date.length();
        String day = String.valueOf(date.charAt(len-2)) + date.charAt(len-1);
        String month = String.valueOf(date.charAt(len-4)) + date.charAt(len-3);
        String year = String.valueOf(date.charAt(0)) + date.charAt(1) + date.charAt(2) + date.charAt(3);
        int theDay = strToNumber(day);
        int theMonth = strToNumber(month);
        int theYear = strToNumber(year);
        Log.d("myLogNC", "before -> " + monthType[1][1] );
        if (isLeap(theYear)){
            monthType[1][1] = NBR_DAY_FEBRUARY_LEAP;
        } else {
            monthType[1][1] = NBR_DAY_FEBRUARY;
        }
        Log.d("myLogNC", "after -> " + monthType[1][1] );
        //if next
        if (numberOfDay >= 0) {
            Log.d("myLogNC", "-> " + monthType[theMonth - 1][1] );
            for (int i = 0; i < numberOfDay; i++) {
                //for (int i = numberOfDay; i < 0; i++) {
                theDay++;
                if (theDay == monthType[theMonth - 1][1] + 1) {
                    theDay = 1;
                    theMonth++;
                }
                if (theMonth > 13) {
                    theMonth = 1;
                    theYear++;
                    if (isLeap(theYear)) {
                        monthType[1][1] = NBR_DAY_FEBRUARY_LEAP;
                    } else {
                        monthType[1][1] = NBR_DAY_FEBRUARY;
                    }
                }
            }
        } else {    //numberOfDay < 0 (if before)
            Log.d("myLogNC", "-> " + monthType[theMonth - 1][1]);
            Log.d("myLogNC", "-> " + theYear + theMonth + theDay);
            for (int i = numberOfDay; i < 0; i++) {
                theDay--;
                Log.d("myLogNC", "-> " + theYear + theMonth + theDay);
                if (theDay == 0) {
                    theMonth--;
                    if (theMonth == 0){    //change year
                        theDay = monthType[theMonth][1];    //december
                    } else {
                        theDay = monthType[theMonth - 1][1];
                    }
                    //Log.d("myLogNC", "-> " + theDay + " | " + monthType[theMonth - 2][1]);
                }
                Log.d("myLogNC", "-> " + theYear + theMonth + theDay);
                if (theMonth == 0) {
                    theMonth = 12;
                    theYear--;
                    if (isLeap(theYear)) {
                        monthType[1][1] = NBR_DAY_FEBRUARY_LEAP;
                    } else {
                        monthType[1][1] = NBR_DAY_FEBRUARY;
                    }
                }
                Log.d("myLogNC", "-> " + theYear + theMonth + theDay);
            }
        }
        Log.d("myLogNC", "new : " + theYear + " | " + theMonth + " | " + theDay);
        String newMonth = String.valueOf(theMonth);
        String newDay = String.valueOf(theDay);
        String formattedMonth = newMonth, formattedDay = newDay;
        if(newMonth.length() < 2){
            formattedMonth = "0" + newMonth;
        }
        if(newDay.length() < 2){
            formattedDay = "0" + newDay;
        }
        String finalDate = theYear + formattedMonth + formattedDay;
        Log.d("myLogNC", "date : " + finalDate);
        Log.d("myLogNC", "----------------");
        return finalDate;
    }

    /*
     * extractDay:
     * 20220203
     * return "03"
     */
    static String extractNbrDay(String date){
        Log.d("myLog", date);
        String day = String.valueOf(date.charAt(6)) + date.charAt(7);
        Log.d("myLog", day + " | " + date.charAt(6) + " | " + date.charAt(7));
        return day;
    }


    /*
     * strToNumber :
     * convert a string number into integer
     */
    static int strToNumber(String strConvert){
        int number = 0;
        int[] numbers = {0,1,2,3,4,5,6,7,8,9};
        char[] numbersStr = {'0','1','2','3','4','5','6','7','8','9'};
        String stringToConvert =  reverse(strConvert);
        for (int i = 0 ; i < stringToConvert.length(); i++){
            for(int j = 0; j < numbers.length; j++){
                if (stringToConvert.charAt(i) == numbersStr[j]){
                    number += numbers[j]*power(10,i);
                }
            }
        }
        return number;
    }

    /*
     * reverse :
     * reverse a array
     * example : str = "abc"
     * return : "cba"
     */
    public static String reverse(String str){
        char buffer;
        StringBuilder reverseStr = new StringBuilder();
        for(int i = 0; i < str.length(); i++){
            buffer = str.charAt(i);
            reverseStr.insert(0, buffer);
        }
        return reverseStr.toString();
    }

    /*
     * power :
     * calculate a power of a number
     */
    public static int power(int number, int power){
        int value = 1;
        for (int i = 0; i < power ; i++){
            value *= number;
        }
        return value;
    }

    /*
    private int calculateDate(String date, int numberOfDay ,boolean type){
        Log.d("myLogNav", "calculateDate : " + date + " to " + numberOfDay );
        int newDate = ExtractInformation.strToNumber(date);
        if (type){
            //addition
            newDate += numberOfDay;
        } else {
            //subtraction
            newDate -= numberOfDay;
        }
        //verification if date exist
        Log.d("myLogNav", "newDate : " + newDate);
        String dater = String.valueOf(newDate);
        int len = dater.length();
        Log.d("myLogNav", "len : " + len);
        String day = String.valueOf(dater.charAt(len-2)) + dater.charAt(len-1);
        Log.d("myLogNav", "day : " + day + " | " + dater.charAt(len-2) + " | " + dater.charAt(len-1));
        String month = String.valueOf(dater.charAt(len-4)) + dater.charAt(len-3);
        String year = String.valueOf(dater.charAt(0)) + dater.charAt(1) + dater.charAt(2) + dater.charAt(3);
        int newYear = ExtractInformation.strToNumber(year);
        int newMonth = ExtractInformation.strToNumber(month);

        if (newMonth > 13){
            newMonth = 1;
        }

        int[][] monthType = {
                {1, 31},
                {2, 28}, //29 ?
                {3, 31},
                {4, 30},
                {5, 31},
                {6, 30},
                {7, 31},
                {8, 31},
                {9, 30},
                {10, 31},
                {11, 30},
                {12, 31}
        };

        int newDay = ExtractInformation.strToNumber(day);
        Log.d("myLogNav", "len : " + monthType[newMonth+1][1] + " | " + newDay + " | " + newMonth);
        if (newDay > monthType[newMonth+1][1]){
            // if newMonth == 2 (February) check year to conclude if 28 or 29
            Log.d("myLogNav", "len : " + monthType[newMonth+1][1] + " | " + newDay + " | " + newMonth);
            newMonth++;
            newDay -= monthType[newMonth+1][1] + 1;
        }

        if (newMonth > 13){
            newYear++;
            newMonth = 1;
        }
        Log.d("myLogNav", "new : " + newYear + " | " + newMonth + " | " + newDay);
        return newDate;
    }
    */
    /*
        private String newDate(String currentDate, int positionDay, boolean nextOrBefore){
            String newDate;

            //convert dateFile in int
            int intNextDate = ExtractInformation.strToNumber(currentDate);
            intNextDate += positionDay;
            String nextDate = String.valueOf(intNextDate);
            //convert 20220108 to 2022-01-08
            String dateStr = ExtractInformation.convertDate(nextDate);
            Log.d("myLog", "Date file : " + currentDate);
            Log.d("myLog", "count : " + positionDay);
            Log.d("myLog", "Next date : " + nextDate);
            //LocalDate localDate = date.toInstant().atZone(ZoneId.systemDefault()).toLocalDate(); //2022-01-08
            //LocalDate currentDate = LocalDate.valueOf("2022-01-08");
            LocalDate localCurrentDate = LocalDate.parse(dateStr);
            DayOfWeek days = localCurrentDate.getDayOfWeek();
            int nextDayOfTheWeek = DayOfWeek.valueOf(String.valueOf(days)).getValue();
            String nextDay = String.valueOf(nextDayOfTheWeek);
            Log.d("myLog", "next day : " + nextDay);
            //if next day is Saturday or Sunday :
            //if (dayOfTheWeek == 6){
            if (nextDayOfTheWeek == 6){
                //+2 SATURDAY to MONDAY
                //convert current date in integer
                int intDate = ExtractInformation.strToNumber(nextDate);
                //+ or - 2
                if (nextOrBefore == NEXT) {
                    intDate += 2;
                    positionDay += 2;
                } else {
                    intDate -= 2;
                    positionDay -= 2;
                }
                newDate = String.valueOf(intDate);     //convert date in string and save
            } else if (nextDayOfTheWeek == 7){      //+1 SUNDAY to MONDAY
                int intDate = ExtractInformation.strToNumber(nextDate); //convert current date in integer
                if (nextOrBefore == NEXT) {
                    intDate += 1;
                    positionDay += 1;
                } else {
                    intDate -= 1;
                    positionDay -= 1;
                }
                newDate = String.valueOf(intDate);     //convert date in string and save
            } else {
                //current date
                newDate = nextDate;
            }
            return newDate;
        }
    */
}
