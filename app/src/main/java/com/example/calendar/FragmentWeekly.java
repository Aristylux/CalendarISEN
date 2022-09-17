package com.example.calendar;

import androidx.annotation.RequiresApi;
import androidx.cardview.widget.CardView;
import androidx.fragment.app.Fragment;

import android.content.res.ColorStateList;
import android.os.Build;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.Nullable;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import java.util.List;

public class FragmentWeekly extends Fragment {

    FilesUtil filesUtil;
    String date;
    final private static int day = 1;

    public FragmentWeekly(FilesUtil filesUtil){
        this.filesUtil = filesUtil;
        this.date = filesUtil.getDateFile();
    }

    public FragmentWeekly(FilesUtil filesUtil, String date){
        this.filesUtil = filesUtil;
        this.date = date;
    }

    @RequiresApi(api = Build.VERSION_CODES.O)
    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_weekly, container, false);
        Log.d("myLogN", "Weekly view");

        //for start to Monday
        date = firstDay(date, view);
        String nbrDay = CalculateUtil.extractNbrDay(date);
        addView(view, R.id.RecyclerViewMonday, R.id.text_monday_day, date, nbrDay);

        date = CalculateUtil.calculateDate(date, day);
        nbrDay = CalculateUtil.extractNbrDay(date);
        addView(view, R.id.RecyclerViewTuesday, R.id.text_tuesday_day, date, nbrDay);

        date = CalculateUtil.calculateDate(date, day);
        nbrDay = CalculateUtil.extractNbrDay(date);
        addView(view, R.id.RecyclerViewWednesday, R.id.text_wednesday_day, date, nbrDay);

        date = CalculateUtil.calculateDate(date, day);
        nbrDay = CalculateUtil.extractNbrDay(date);
        addView(view, R.id.RecyclerViewThursday, R.id.text_thursday_day, date, nbrDay);

        date = CalculateUtil.calculateDate(date, day);
        nbrDay = CalculateUtil.extractNbrDay(date);
        addView(view, R.id.RecyclerViewFriday, R.id.text_friday_day, date, nbrDay);

        return view;
    }

    private String nextDay(String date) {
        int dateInt = CalculateUtil.strToNumber(date);
        dateInt++;
        Log.d("myLogN", "~nextDay : " + dateInt);
        return String.valueOf(dateInt);
    }

    @RequiresApi(api = Build.VERSION_CODES.O)
    private String firstDay(String date, View view) {
        CardView cardView = null;
        Log.d("myLogN", "nextDay : " + date);
        int count = 0;

        int nbr = CalculateUtil.nbrDayOfTheWeek(date);
        switch (nbr){
            case 1: //Monday (do nothing)
                cardView = view.findViewById(R.id.CardViewMonday);
                break;
            case 2:
                count -= 1;
                cardView = view.findViewById(R.id.CardViewTuesday);
                break;
            case 3:
                count -= 2;
                cardView = view.findViewById(R.id.CardViewWednesday);
                break;
            case 4:
                count -= 3;
                cardView = view.findViewById(R.id.CardViewThursday);
                break;
            case 5:
                count -= 4;
                cardView = view.findViewById(R.id.CardViewFriday);
                break;
                /*
            case 6: //it's saturday so, show next week
                count += 2;
                break;
            case 7: //it's sunday so, show monday next week
                count += 1;
                break;
                */
        }
        // and if week is the current week !
        if (cardView != null) {
            Log.d("myLogN", "set color");
            //cardView.setBackgroundTintList(ColorStateList.valueOf(Objects.requireNonNull(getActivity()).getColor(R.color.selected)));
            cardView.setBackgroundTintList(ColorStateList.valueOf(requireActivity().getColor(R.color.selected)));
        }
        String newDate = CalculateUtil.calculateDate(date, count);
        Log.d("myLogN", "nextDay newDate : " + newDate + " | " + nbr);
        return newDate;
    }

    @RequiresApi(api = Build.VERSION_CODES.O)
    private void addView(View view, int theId, int idNbrDay, String date, String nbrDay){
        List<Course> listLessons = Routine.routineTest(date, filesUtil);
        TextView textView = view.findViewById(idNbrDay);
        textView.setText(nbrDay);
        if (listLessons != null) {
            AdapterLesson myObjAdapter = new AdapterLesson(getContext(), listLessons);
            RecyclerView recyclerView = view.findViewById(theId);
            recyclerView.setAdapter(myObjAdapter);
            recyclerView.setNestedScrollingEnabled(false);
            recyclerView.setLayoutManager(new LinearLayoutManager(getContext()));
        }
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
    }
}
