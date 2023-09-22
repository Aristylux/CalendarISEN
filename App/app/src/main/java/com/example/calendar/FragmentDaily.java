package com.example.calendar;

import android.os.Build;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.Nullable;
import androidx.annotation.RequiresApi;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import java.util.List;

/**
 * FragmentDaily:
 * <p>
 * Course for the day
 */

public class FragmentDaily extends Fragment {

    // The fragment initialization parameters
    private static final String ARG_FILES_UTIL = "arg_files_util";
    private static final String ARG_DATE = "arg_date";

    private FilesUtil filesUtil;
    private String date;

    public FragmentDaily(){
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param filesUtil utility files
     * @return  A new instance of fragment FragmentDaily.
     */
    public static FragmentDaily newInstance(FilesUtil filesUtil){
        return newInstance(filesUtil, filesUtil.getDateFile());
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param filesUtil utility files
     * @param date      date
     * @return  A new instance of fragment FragmentDaily.
     */
    public static FragmentDaily newInstance(FilesUtil filesUtil, String date){
        FragmentDaily fragment = new FragmentDaily();
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
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_daily, container, false);

        //retrieve and form data
        List<Course> listLessons = null;     //Routine
        if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.O) {
            listLessons = Routine.routineTest(date, filesUtil);
        } else {
            Log.w("myLogW", "frag: higher version needed");
        }

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
