package com.example.calendar;

import android.os.Build;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.RequiresApi;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import java.util.List;

/**
 * FragmentDaily:
 *
 * Course for the day
 */

public class FragmentDaily extends Fragment {

    FilesUtil filesUtil;
    String date;

    public FragmentDaily(FilesUtil filesUtil, String date){
        this.filesUtil = filesUtil;
        this.date = date;
    }

    public FragmentDaily(FilesUtil filesUtil){
        this.filesUtil = filesUtil;
        this.date = filesUtil.getDateFile();
    }

    @RequiresApi(api = Build.VERSION_CODES.O)
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_daily, container, false);

        //retrieve and form data
        List<Course> listLessons = Routine.routineTest(date, filesUtil);     //Routine

        //populate
        if (listLessons != null) {
            AdapterLesson myObjAdapter = new AdapterLesson(getContext(), listLessons);
            RecyclerView recyclerView = view.findViewById(R.id.mRecyclerView);
            recyclerView.setAdapter(myObjAdapter);
            recyclerView.setLayoutManager(new LinearLayoutManager(getContext()));
        }
        return view;
    }
}
