package com.example.calendar;

import android.os.Build;

import androidx.annotation.RequiresApi;

public class CountClass {

    int countWeek, countDay;

    private static boolean NEXT;
    private static boolean BEFORE;

    CountClass(boolean next, boolean before){
        NEXT = next;
        BEFORE = before;
    }

    public void countDay(boolean type, int nbr){
        if(type == BEFORE)
            countDay = countDay - nbr;
        else
            countDay = countDay + nbr;
    }

    public void countDay(boolean type, int nbrNext, int nbrBefore){
        if(type == BEFORE)
            countDay(BEFORE, nbrBefore);
        else
            countDay(NEXT, nbrNext);
    }

    public void setCountDay(int nbr){
        countDay = nbr;
    }

    public int getCountDay(){
        return countDay;
    }

    public void countWeek(boolean type, int nbr){
        if(type == BEFORE)
            countWeek = countWeek - nbr;
        else
            countWeek = countWeek + nbr;
    }

    public void countWeek(boolean type, int nbrNext, int nbrBefore){
        if(type == BEFORE)
            countWeek(BEFORE, nbrBefore);
        else
            countWeek(NEXT, nbrNext);
    }

    public void setCountWeek(int nbr){
        countWeek = nbr;
    }

    public int getCountWeek(){
        return countWeek;
    }


    @RequiresApi(api = Build.VERSION_CODES.O)
    public void avoidWeekend(String date){
        int nbr = CalculateUtil.nbrDayOfTheWeek(date);
        if (nbr == 6)
            setCountWeek(2);
        else if (nbr == 7)
            setCountWeek(1);
        else
            setCountWeek(0);
    }

}
