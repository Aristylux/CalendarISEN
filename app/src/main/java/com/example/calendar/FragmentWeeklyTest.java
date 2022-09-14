package com.example.calendar;

import androidx.annotation.RequiresApi;
import androidx.cardview.widget.CardView;
import androidx.fragment.app.Fragment;
//import android.app.Fragment;

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
import java.util.Objects;

/**
 * Not used for the moment
 */

public class FragmentWeeklyTest extends Fragment {

    public FragmentWeeklyTest(){}

    @RequiresApi(api = Build.VERSION_CODES.O)
    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_weekly_test, container, false);
        Log.d("myLogN", "Weekly view");

        Bundle data = getArguments();
        if (data != null) {
            String date = data.getString("day");
            String dateFile = data.getString("dateFile");
            String firstName = data.getString("firstname");
            String lastName = data.getString("lastname");
            //FragmentDaily fragmentDailyMonday = new FragmentDaily();


            List<Course> listLessons = Routine.routineTest(date, dateFile, firstName, lastName);
            //convert date to int


        }
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
            cardView.setBackgroundTintList(ColorStateList.valueOf(requireActivity().getColor(R.color.selected)));
        }
        String newDate = CalculateUtil.calculateDate(date, count);
        Log.d("myLogN", "nextDay newDate : " + newDate + " | " + nbr);
        return newDate;
    }

    @RequiresApi(api = Build.VERSION_CODES.O)
    private void addView(View view, int idRecycler, int idNbrDay, String date, String dateFile, String firstName, String lastName, String nbrDay){
        //List<Course> listLessons = Routine.routine(view, date, dateFile, firstName, lastName);
        List<Course> listLessons = Routine.routineTest(date, dateFile, firstName, lastName);
        TextView textView = view.findViewById(idNbrDay);
        textView.setText(nbrDay);
        if (listLessons != null) {
            AdapterLesson myObjAdapter = new AdapterLesson(getContext(), listLessons);
            RecyclerView mRecyclerView = view.findViewById(idRecycler);
            mRecyclerView.setAdapter(myObjAdapter);
            mRecyclerView.setNestedScrollingEnabled(false);
            mRecyclerView.setLayoutManager(new LinearLayoutManager(getContext()));
        }
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
    }
}
