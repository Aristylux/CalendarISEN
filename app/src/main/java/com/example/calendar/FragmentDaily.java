package com.example.calendar;

import android.os.Build;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.RequiresApi;
import androidx.fragment.app.Fragment;
//import android.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;
import java.util.List;

/**
 * FragmentDaily:
 *
 * Course for the day
 */

public class FragmentDaily extends Fragment {

    RecyclerView mRecyclerView;
    List<Course> listLessons = new ArrayList<>();
    AdapterLesson myObjAdapter;

    public FragmentDaily(){}

    @RequiresApi(api = Build.VERSION_CODES.O)
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_daily, container, false);

        //get argument
        Bundle data = getArguments();
        if (data != null) {
            String date = data.getString("day");
            String dateFile = data.getString("dateFile");
            String firstName = data.getString("firstname");
            String lastName = data.getString("lastname");

            //retrieve and form data
            listLessons = Routine.routineTest(date, dateFile, firstName, lastName);     //Routine

            //populate
            if (listLessons != null) {
                myObjAdapter = new AdapterLesson(getContext(), listLessons);
                mRecyclerView = view.findViewById(R.id.mRecyclerView);
                mRecyclerView.setAdapter(myObjAdapter);
                mRecyclerView.setLayoutManager(new LinearLayoutManager(getContext()));
            }
        }
        return view;
    }
}
