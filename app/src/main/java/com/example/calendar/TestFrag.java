package com.example.calendar;

import android.os.Build;
import android.os.Bundle;
import android.util.Log;
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

public class TestFrag extends Fragment {

    RecyclerView mRecyclerView;
    List<Course> listLessons = new ArrayList<>();
    AdapterLesson myObjAdapter;

    public TestFrag(){}

    @RequiresApi(api = Build.VERSION_CODES.O)
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.test_frag, container, false);

        //get argument
        Log.d("myLogH","Frag");
        Bundle data = getArguments();
        if (data != null) {
            Log.d("myLogH","In");
            String date = data.getString("day");
            String dateFile = data.getString("dateFile");
            String firstName = data.getString("firstname");
            String lastName = data.getString("lastname");
            //routine(view, date, dateFile, firstName, lastName);
            //listLessons = Routine.routine(view, date, dateFile, firstName, lastName);
            listLessons = Routine.routineTest(date, dateFile, firstName, lastName);

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
