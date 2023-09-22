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

    // The fragment initialization parameters
    private static final String ARG_FILES_UTIL = "arg_files_util";
    private static final String ARG_DATE = "arg_date";

    private FilesUtil filesUtil;
    private String date;
    final private static int day = 1;

    public FragmentWeekly(){
        // Required empty public constructor
    }

    /*
    public FragmentWeekly(FilesUtil filesUtil){
        this.filesUtil = filesUtil;
        this.date = filesUtil.getDateFile();
    }

    public FragmentWeekly(FilesUtil filesUtil, String date){
        this.filesUtil = filesUtil;
        this.date = date;
    }
*/
    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param filesUtil utility files
     * @return  A new instance of fragment FragmentWeekly.
     */
    public static FragmentWeekly newInstance(FilesUtil filesUtil){
        return newInstance(filesUtil, filesUtil.getDateFile());
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param filesUtil utility files
     * @param date      date
     * @return  A new instance of fragment FragmentWeekly.
     */
    public static FragmentWeekly newInstance(FilesUtil filesUtil, String date){
        FragmentWeekly fragment = new FragmentWeekly();
        Bundle args = new Bundle();
        args.putParcelable(ARG_FILES_UTIL, filesUtil);
        args.putString(ARG_DATE, date);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null){
            this.filesUtil = getArguments().getParcelable(ARG_FILES_UTIL);
            this.date = getArguments().getString(ARG_DATE);
        }
    }


    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_weekly, container, false);
        Log.d("myLogNW", "Weekly view");

        //for start to Monday
        if (Build.VERSION.SDK_INT < Build.VERSION_CODES.O) {
            Log.w("myLogNW", "higher version needed");
            return view;
        }
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
        Log.d("myLogNW", "~nextDay : " + dateInt);
        return String.valueOf(dateInt);
    }


    private String firstDay(String date, View view) {
        CardView cardView = null;
        Log.d("myLogNW", "nextDay : " + date);
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
            case 6: //it's saturday so, show next week
                count += 2;
                break;
            case 7: //it's sunday so, show monday next week
                count += 1;
                break;

        }
        // and if week is the current week !
        if (cardView != null) {
            Log.d("myLogNW", "set color");
            //cardView.setBackgroundTintList(ColorStateList.valueOf(Objects.requireNonNull(getActivity()).getColor(R.color.selected)));
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                cardView.setBackgroundTintList(ColorStateList.valueOf(requireActivity().getColor(R.color.selected)));
            }
        }
        String newDate = CalculateUtil.calculateDate(date, count);
        Log.d("myLogNW", "nextDay newDate : " + newDate + " | " + nbr);
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
