package com.example.calendar;

import android.os.Build;
import android.os.Bundle;

import androidx.annotation.RequiresApi;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.RecyclerView;

import android.os.Environment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import java.util.ArrayList;
import java.util.List;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link BlankFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class BlankFragment extends Fragment {

    RecyclerView mRecyclerView;
    List<Course> listLessons = new ArrayList<>();
    AdapterLesson myObjAdapter;

    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;

    public BlankFragment() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment BlankFragment.
     */
    // TODO: Rename and change types and number of parameters
    public static BlankFragment newInstance(String param1, String param2) {
        BlankFragment fragment = new BlankFragment();
        Bundle args = new Bundle();
        args.putString(ARG_PARAM1, param1);
        args.putString(ARG_PARAM2, param2);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            mParam1 = getArguments().getString(ARG_PARAM1);
            mParam2 = getArguments().getString(ARG_PARAM2);
        }
    }

    @RequiresApi(api = Build.VERSION_CODES.O)
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_blank, container, false);

        Log.d("myLog", "fragment");

        String dateFile = "";
        Bundle data = getArguments();
        if(data != null) {
            dateFile = data.getString("time");
        }
        //String dateFile = "20220105";

        //String path = getExternalFilesDir(null).toString() + "/" + firstName + "." + lastName + "/axel.mezade_301221134425.ics";
        //String path = getExternalFilesDir(null).toString() + "/" + firstName + "." + lastName + "/" + firstName + "." + lastName + "_" + date + ".ics";
        //String path = getExternalFilesDir(null).toString() + "/" + firstName + "." + lastName + "/" + firstName + "." + lastName + "_" + dateFile + ".ics";

        //String path = Environment.DIRECTORY_DOWNLOADS + "/" + firstName + "." + lastName + "_" + dateFile + ".ics";
        //String path = Environment.getExternalStorageDirectory().toString() + "/Download" + "/" + firstName + "." + lastName + "_" + dateFile + ".ics";
        String path = Environment.getExternalStorageDirectory().toString() + "/Download" + "/" + "axel" + "." + "mezade" + "_" + dateFile + ".ics";

        //String path = getExternalFilesDir(null).toString() + "/" + firstName + "." + lastName + "/axel.mezade_010122114122.ics";
        Log.d("myLog", path);
        //Log.d("myLog", dateFile);

        //Log.d("myLog", dateFile);
        //List listLine = ExtractInformation.checkDate(path, "20220103");
        //List listLine = ExtractInformation.checkDate(path, dateFile);
        List<Integer> listLine = ExtractInformation.checkDate(path, dateFile);
        //Log.d("myLog", String.valueOf(ExtractInformation.checkDate(path, "20211214")));
        Log.d("myLog", String.valueOf(listLine));
        /*
        if(listLine != null) {
            int size = listLine.size() / 3;
            int positionLine = 0;
            String buffering;
            ArrayList<String> classroom = new ArrayList<>();
            TextView stateLesson = view.findViewById(R.id.no_lesson);
            if (size != 0) {
                Log.d("myLog", "Size : " + listLine.size());
                while (positionLine < listLine.size()) {
                    int count = 0;
                    String lessonTime = buffering = ExtractInformation.extractLine(path, listLine.get(positionLine) + 1);
                    Log.d("myLog", "lesson time : " + lessonTime);

                    if ((positionLine - 3) > 0) {
                        positionLine -= 3;
                    }
                    Log.d("myLog", "position : " + positionLine + " line : " + listLine.get(positionLine));
                    while (lessonTime.equals(buffering)) {
                        positionLine += 3;
                        if (positionLine > listLine.size() - 1) break;
                        count++;
                        lessonTime = ExtractInformation.extractLine(path, listLine.get(positionLine) + 1);
                    }
                    positionLine -= 3 * count;
                    //Log.d("myLog", "Duplication : " + count);
                    for (int i = 0; i < count; i++) {
                        classroom.add(ExtractInformation.extractClassRoom(ExtractInformation.extractLine(path, listLine.get(positionLine) + 6), ExtractInformation.extractLine(path,  listLine.get(positionLine) + 11)));
                        positionLine += 3;
                    }
                    String lessonClassroom = ExtractInformation.concatenateClassRoom(classroom);
                    String lessonClassroomShort = ExtractInformation.formatClassRoom(lessonClassroom);
                    classroom.clear();
                    //Log.d("myLog", lessonClassroom);
                    positionLine -= 3 * count;
                    //Log.d("myLog", "Position : " + positionLine);
                    String lessonName = ExtractInformation.extractLessonName(ExtractInformation.extractLine(path, listLine.get(positionLine) + 10));
                    String lessonNameShort = ExtractInformation.formatLessonName(lessonName);
                    String lessonType = ExtractInformation.extractLessonType(ExtractInformation.extractLine(path, listLine.get(positionLine) + 6));
                    String lessonTeacher = ExtractInformation.extractTeachers(ExtractInformation.extractLine(path, listLine.get(positionLine) + 11));
                    String lessonDay = ExtractInformation.extractDay(ExtractInformation.extractLine(path,  listLine.get(positionLine) + 1));
                    String lessonStartTime = ExtractInformation.formatTime(ExtractInformation.extractTime(ExtractInformation.extractLine(path,  listLine.get(positionLine) + 1)));
                    String lessonEndTime = ExtractInformation.formatTime(ExtractInformation.extractTime(ExtractInformation.extractLine(path,  listLine.get(positionLine) + 2)));
                    String lessonDuration = ExtractInformation.calculateDuration(ExtractInformation.extractTime(ExtractInformation.extractLine(path, listLine.get(positionLine) + 1)), ExtractInformation.extractTime(ExtractInformation.extractLine(path,  listLine.get(positionLine) + 2)));

                    positionLine += 3 * count;
                    listLessons.add(new Course(
                            lessonName,
                            lessonNameShort,
                            lessonType,
                            lessonClassroom,
                            lessonClassroomShort,
                            lessonTeacher,
                            lessonDay,
                            lessonStartTime,
                            lessonEndTime,
                            lessonDuration));
                }
                myObjAdapter = new MyAdapter(getActivity(), listLessons);
                stateLesson.setVisibility(View.INVISIBLE);
            } else {
                //no lesson today !
                stateLesson.setVisibility(View.VISIBLE);
            }
            myObjAdapter = new MyAdapter(getActivity() , listLessons);

            //MyAdapter myObjAdapter = new MyAdapter(this, listLessons);
            //show data
            mRecyclerView = view.findViewById(R.id.mRecyclerView);
            mRecyclerView.addItemDecoration(new VerticalSpaceItemDecoration(30));
            mRecyclerView.setAdapter(myObjAdapter);
            mRecyclerView.setLayoutManager(new LinearLayoutManager(getActivity()));
        }
        */
        return view;
    }
}