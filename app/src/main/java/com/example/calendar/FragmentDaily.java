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
    /*
    @RequiresApi(api = Build.VERSION_CODES.O)
    private void routine(View view, String date, String dateFile, String firstName, String lastName){
        //String dateFile = "20220105";

        //String path = getExternalFilesDir(null).toString() + "/" + firstName + "." + lastName + "/axel.mezade_301221134425.ics";
        //String path = getExternalFilesDir(null).toString() + "/" + firstName + "." + lastName + "/" + firstName + "." + lastName + "_" + date + ".ics";
        //String path = getExternalFilesDir(null).toString() + "/" + firstName + "." + lastName + "/" + firstName + "." + lastName + "_" + dateFile + ".ics";

        //String path = Environment.DIRECTORY_DOWNLOADS + "/" + firstName + "." + lastName + "_" + dateFile + ".ics";
        //String path = Environment.getExternalStorageDirectory().toString() + "/Download" + "/" + firstName + "." + lastName + "_" + dateFile + ".ics";
        String path = Environment.getExternalStorageDirectory().toString() + "/Download" + "/" + firstName + "." + lastName + "_" + dateFile + ".ics";

        //String path = getExternalFilesDir(null).toString() + "/" + firstName + "." + lastName + "/axel.mezade_010122114122.ics";
        Log.d("myLog", path);
        //Log.d("myLog", dateFile);

        //Log.d("myLog", dateFile);
        //List listLine = ExtractInformation.checkDate(path, "20220103");
        //List listLine = ExtractInformation.checkDate(path, dateFile);
        List<Integer> listLine = ExtractInformation.checkDate(path, date);
        //Log.d("myLog", String.valueOf(ExtractInformation.checkDate(path, "20211214")));
        //Log.d("myLog", String.valueOf(listLine));
        if (listLine != null) {
            int size = listLine.size() / 3;
            int positionLine = 0;
            String buffering;
            ArrayList<String> classroom = new ArrayList<>();
            TextView stateLesson = view.findViewById(R.id.no_lesson);
            if (size != 0) {
                //Log.d("myLog", "Size : " + listLine.size());
                while (positionLine < listLine.size()) {
                    int count = 0;
                    String lessonTime = buffering = ExtractInformation.extractLine(path, listLine.get(positionLine) + 1);
                    Log.d("myLog", "Lesson time : " + lessonTime);

                    if ((positionLine - 3) > 0) {
                        positionLine -= 3;
                    }
                    Log.d("myLog", "Line : " + listLine.get(positionLine));
                    while (lessonTime.equals(buffering)) {
                        positionLine += 3;
                        //count++;
                        if (positionLine > listLine.size() - 1) {
                            if (count == 0) count++;
                            break;
                        }
                        count++;
                        lessonTime = ExtractInformation.extractLine(path, listLine.get(positionLine) + 1);
                    }
                    positionLine -= 3 * count;
                    //Log.d("myLog", "Duplication : " + count);
                    for (int i = 0; i < count; i++) {
                        //Log.d("myLog", "." + ExtractInformation.extractLine(path, listLine.get(positionLine) + 6));
                        classroom.add(ExtractInformation.extractClassRoom(ExtractInformation.extractLine(path, listLine.get(positionLine) + 6), ExtractInformation.extractLine(path, listLine.get(positionLine) + 11)));
                        positionLine += 3;
                    }
                    String lessonClassroom = ExtractInformation.concatenateClassRoom(classroom);
                    String lessonClassroomShort = ExtractInformation.formatClassRoom(lessonClassroom);
                    classroom.clear();
                    Log.d("myLog", "Lesson classroom : " + lessonClassroom);
                    positionLine -= 3 * count;
                    //Log.d("myLog", "Position : " + positionLine);
                    String lessonTitle = ExtractInformation.extractLine(path, listLine.get(positionLine) + 6);
                    String lessonName = ExtractInformation.extractLessonName(ExtractInformation.extractLine(path, listLine.get(positionLine) + 10), lessonTitle);
                    Log.d("myLog", "Return  : " + lessonName);
                    Log.d("myLog", "Line SOS : " + lessonTitle);
                    String lessonNameShort = ExtractInformation.formatLessonName(lessonName, lessonTitle);
                    String lessonType = ExtractInformation.extractLessonType(lessonTitle);
                    String lessonTeacher = ExtractInformation.extractTeachers(ExtractInformation.extractLine(path, listLine.get(positionLine) + 11));
                    String lessonDay_StartTime = ExtractInformation.extractLine(path, listLine.get(positionLine) + 1);
                    String lessonDay_EndTime = ExtractInformation.extractLine(path, listLine.get(positionLine) + 2);
                    String lessonDay = ExtractInformation.extractDay(lessonDay_StartTime);
                    String lessonStartTime = ExtractInformation.formatTime(ExtractInformation.extractTime(lessonDay_StartTime));
                    String lessonEndTime = ExtractInformation.formatTime(ExtractInformation.extractTime(lessonDay_EndTime));
                    String lessonDuration = ExtractInformation.calculateDuration(ExtractInformation.extractTime(lessonDay_StartTime), ExtractInformation.extractTime(lessonDay_EndTime));

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
                myObjAdapter = new MyAdapter(getContext(), listLessons);
                //myObjAdapter = new MyAdapter(getActivity(), listLessons);
                stateLesson.setVisibility(View.INVISIBLE);
            } else {
                //no lesson today !
                stateLesson.setVisibility(View.VISIBLE);
            }
            //myObjAdapter = new MyAdapter(getActivity() , listLessons);

            //MyAdapter myObjAdapter = new MyAdapter(this, listLessons);
            //show data
            mRecyclerView = view.findViewById(R.id.mRecyclerView);
            //mRecyclerView.addItemDecoration(new VerticalSpaceItemDecoration(30));
            mRecyclerView.setAdapter(myObjAdapter);
            mRecyclerView.setLayoutManager(new LinearLayoutManager(getContext()));
            //mRecyclerView.setLayoutManager(new LinearLayoutManager(getActivity());
        }
    }
    */

}
