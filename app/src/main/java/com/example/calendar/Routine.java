package com.example.calendar;

import static com.example.calendar.ExtractInformation.*;
import static com.example.calendar.ExtractInformation.formatConcatenateLessonName;
import static com.example.calendar.FilesUtil.getPathDownload;

import android.annotation.SuppressLint;
import android.content.Context;
import android.graphics.Color;
import android.os.Build;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.ProgressBar;

import androidx.annotation.RequiresApi;
import androidx.coordinatorlayout.widget.CoordinatorLayout;

import com.google.android.material.snackbar.BaseTransientBottomBar;
import com.google.android.material.snackbar.Snackbar;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.FileWriter;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class Routine {

    /*
    @RequiresApi(api = Build.VERSION_CODES.O)
    public static List<Course> routine(View view, String date, String dateFile, String firstName, String lastName) {

        //String path = getExternalFilesDir(null).toString() + "/" + firstName + "." + lastName + "/axel.mezade_301221134425.ics";
        String path = Environment.getExternalStorageDirectory().toString() + "/Download" + "/" + firstName + "." + lastName + "_" + dateFile + ".ics";
        Log.d("myLog", path);
        List<Integer> listLine = ExtractInformation.checkDate(path, date);  // dateFile = "20220105";
        if (listLine != null) {
            int size = listLine.size() / 3;
            int positionLine = 0;
            String buffering;
            ArrayList<String> classroom = new ArrayList<>();

            if (size != 0) {
                //Log.d("myLog", "Size : " + listLine.size());
                List<Course> listLessons = new ArrayList<>();
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
                    Log.d("myLog", "Duplication : " + count);
                    for (int i = 0; i < count; i++) {
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
                return listLessons;
            }
        }
        return null;
    }

     */
    /*
    @RequiresApi(api = Build.VERSION_CODES.O)
    public static List<Course> routine(View view, String date, String dateFile, String firstName, String lastName){
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

            //TextView stateLesson = view.findViewById(R.id.no_lesson);

            if (size != 0) {
                //Log.d("myLog", "Size : " + listLine.size());
                List<Course> listLessons = new ArrayList<>();
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
                //myObjAdapter = new MyAdapter(getContext(), listLessons);
                //myObjAdapter = new MyAdapter(getActivity(), listLessons);

                //stateLesson.setVisibility(View.INVISIBLE);
                return listLessons;
            } else {
                //no lesson today !

                //stateLesson.setVisibility(View.VISIBLE);
            }
            //myObjAdapter = new MyAdapter(getActivity() , listLessons);
            /*
            //MyAdapter myObjAdapter = new MyAdapter(this, listLessons);
            //show data
            mRecyclerView = view.findViewById(R.id.mRecyclerView);
            //mRecyclerView.addItemDecoration(new VerticalSpaceItemDecoration(30));
            mRecyclerView.setAdapter(myObjAdapter);
            mRecyclerView.setLayoutManager(new LinearLayoutManager(getContext()));
            //mRecyclerView.setLayoutManager(new LinearLayoutManager(getActivity());

        }
        return null;
    }
    */

    @RequiresApi(api = Build.VERSION_CODES.O)
    public static List<Course> routineTest(String date, String dateFile, String firstName, String lastName) {
        Log.d("myLogT", "Routine Test");
        //String path = Environment.getExternalStorageDirectory().toString() + "/Download" + "/data.csv";
        @SuppressLint("SdCardPath") String path = "/data/user/0/com.example.calendar/files/data.csv";

        List<Course> listLessons = new ArrayList<>();

        List<Integer> listLine = checkDate(path, date);  // dateFile = "20220105";
        //List<Integer> listLine = ExtractInformation.checkDate(path, "20220105");  // dateFile = "20220105";
        Log.d("myLogT", "listLine : " + listLine);

        if (listLine == null)
            return null;

        String timeStartBuffer, timeEndBuffer;
        ArrayList<String> classroom = new ArrayList<>();
        ArrayList<String> LessonNames = new ArrayList<>();
        //for (int i = 0; i < listLine.size(); i++) {
        int positionLine = 0;
        while (positionLine < listLine.size()) {
            int count = 0;
            String line = extractLine(path, listLine.get(positionLine) + 1);
            Log.d("myLogT", "line : " + line);
            String[] splitLesson = line.split("\\|");
            String timeStart = timeStartBuffer = splitLesson[1];
            String timeEnd = timeEndBuffer = splitLesson[2];
            Log.d("myLogT", "line : " + timeStartBuffer + " and " + timeEndBuffer);

            if ((positionLine - 1) > 0) {
                positionLine--;
            }

            while (timeStart.equals(timeStartBuffer) && timeEnd.equals(timeEndBuffer)) {
                positionLine++;
                if (positionLine > listLine.size() - 1) {
                    if (count == 0) count++;
                    break;
                }
                count++;
                //for new test
                line = extractLine(path, listLine.get(positionLine) + 1);
                splitLesson = line.split("\\|");
                timeStart = splitLesson[1];
                timeEnd = splitLesson[2];
            }
            positionLine -= count;
            Log.d("myLog", "Duplication : " + count);
            for (int i = 0; i < count; i++) {
                line = extractLine(path, listLine.get(positionLine) + 1);
                splitLesson = line.split("\\|");
                classroom.add(splitLesson[4]);
                LessonNames.add(splitLesson[6]);
                positionLine++;
            }

            String lessonClassroom = concatenateClassRoom(classroom);
            String lessonClassroomShort = formatClassRoom(lessonClassroom);
            classroom.clear();
            Log.d("myLog", "Lesson classroom : " + lessonClassroom);
            Log.d("myLog", "Lesson Names : " + LessonNames);
            String lessonConcatenate = formatConcatenateLessonName(LessonNames).replaceAll(" / ", "\n");
            LessonNames.clear();
            //String teachers = splitLesson[8].replaceAll(" / ", "\n");
            String teachers = formatTeacher(splitLesson[8]).replaceAll(" / ", "\n");

            int idImage = 0;
            switch (splitLesson[5]){
                case "PROJET":
                    idImage = R.drawable.type_document;
                    break;
                case "EXAMEN":
                    idImage = R.drawable.type_exam;
                    break;
                case "CM":
                    idImage = R.drawable.type_presentation;
                    break;
                case "REUNION":
                    idImage = R.drawable.type_meeting;
                    break;
                case "TD":
                    idImage =R.drawable.type_process;
                    break;
                case "CONFERENCE":
                    idImage = R.drawable.type_conference;
                    break;
                case "RATTRAPAGE":
                    idImage = R.drawable.type_fail_exam;
                    break;
                case "MINI_PROJET":
                    //idImage = R.drawable.;
                    break;
            }

            listLessons.add(new Course(
                    lessonConcatenate,
                    splitLesson[7],
                    splitLesson[5],
                    idImage,
                    lessonClassroom,
                    lessonClassroomShort,
                    teachers,
                    splitLesson[0],
                    splitLesson[1],
                    splitLesson[2],
                    splitLesson[3]));

        }
        return listLessons;
    }


    @RequiresApi(api = Build.VERSION_CODES.O)
    static void executeRoutineTest(View view, Context context, int count, FilesUtil filesUtil) {
        Log.d("myLog", "routine test");

        LayoutInflater layoutInflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        Runnable runnable = new Runnable() {
            @SuppressLint("InflateParams")
            @Override
            public void run() {
                View snackMessage;
                if (count < 20) {
                    //success
                    //View custom = layoutInflater.inflate(R.layout.snackbar_custom, null);
                    Routine.analyzer(view.findViewById(R.id.main), context, layoutInflater, filesUtil);
                    //Routine.analyzer(view.findViewById(R.id.main), layoutInflater, firstName, lastName, dateFile);
                    snackMessage = layoutInflater.inflate(R.layout.snackbar_success, null);
                } else {
                    //error
                    snackMessage = layoutInflater.inflate(R.layout.snackbar_failure, null);
                }
                Snackbar snackbar = Snackbar.make(view.findViewById(R.id.cl), "Updated.", Snackbar.LENGTH_SHORT);
                snackbar.getView().setBackgroundColor(Color.TRANSPARENT);
                snackbar.setAnimationMode(BaseTransientBottomBar.ANIMATION_MODE_SLIDE);
                Snackbar.SnackbarLayout snackBarLayout = (Snackbar.SnackbarLayout) snackbar.getView();
                snackBarLayout.setPadding(0, 0, 0, 0);
                snackBarLayout.addView(snackMessage, 0);
                snackbar.show();
            }
        };
        Thread thread = new Thread(runnable);
        thread.start();
    }


    @RequiresApi(api = Build.VERSION_CODES.O)
    public static boolean analyzer(View view, Context context, LayoutInflater layoutInflater, FilesUtil filesUtil) {
        CoordinatorLayout coordinatorLayout = view.findViewById(R.id.cl);
        Snackbar snackbar = Snackbar.make(coordinatorLayout, "Message", Snackbar.LENGTH_INDEFINITE);
        @SuppressLint("InflateParams") View custom = layoutInflater.inflate(R.layout.snackbar_custom, null);
        //snackbar.setAnimationMode(BaseTransientBottomBar.ANIMATION_MODE_FADE);
        snackbar.setAnimationMode(BaseTransientBottomBar.ANIMATION_MODE_SLIDE);
        snackbar.getView().setBackgroundColor(Color.TRANSPARENT);
        Snackbar.SnackbarLayout snackBarLayout = (Snackbar.SnackbarLayout) snackbar.getView();
        snackBarLayout.setPadding(0, 0, 0, 0);
        snackBarLayout.addView(custom, 0);
        snackbar.show();

        // ***********
        //String path = Environment.getExternalStorageDirectory().toString() + "/Download" + "/" + firstName + "." + lastName + "_" + dateFile + ".ics";
        //String path = Environment.getExternalStorageDirectory().toString() + "/Download";
        //String file_name = firstName + "." + lastName + "_" + dateFile + ".ics";
        //String pathICS = path + "/ISENCalendars/" + file_name;

        //String pathICS = context.getFilesDir() + "/" + file_name;

        //@SuppressLint("SdCardPath") String pathApp = "/data/user/0/com.example.calendar/files/";
        //Log.d("myLog", "path : " + pathICS);
        // *******************
        String fileName = filesUtil.getFileName();
        String pathICS = getPathDownload() + fileName;
        String pathApp = filesUtil.getPathInternalApp();
        String pathAppData = filesUtil.getPathFileCourseData();

        Log.d("myLogA", pathICS + " | " + fileName + " | " + pathApp + " | " + pathAppData);

        /*
        //check if file can be read
        boolean exit = false;
        File file = new File(pathICS);
        while (!exit) {
            if (file.canRead()) {
                exit = true;
            } else {
                Log.d("myLog", "Error, path is break");
                Log.d("myLog", " - Exist : " + file.exists());
                Log.d("myLog", " - Can read     : " + file.canRead());
            }
        }
        */

        //read file
        Log.d("myLogA", "Analyzer - Start");
        List<String> ListTest = new ArrayList<>();

        int lineFirstLesson = lineFirstCourses(pathICS);
        int nbr_lines = 0;
        try {
            nbr_lines = countLinesOld(pathICS);
        } catch (IOException e) {
            e.printStackTrace();
        }
        Log.d("myLogA", "Line first lesson : " + lineFirstLesson + " | number of line : " + nbr_lines);
        ProgressBar progressBarSnack = custom.findViewById(R.id.progress_bar_snack);
        progressBarSnack.setMax(nbr_lines);
        int a = 0;
        for (int i = lineFirstLesson; i < nbr_lines; i += 13) {
            //String line = ExtractInformation.extractLine(path, i);
            //Log.d("myLogT", "Line : " + line + " i : " + i);
            //Log.d("myLogT", "1 : " + ExtractInformation.extractLine(path, i + 6));
            //Log.d("myLogT", "2 : " + ExtractInformation.extractLine(path, i + 10));

            String lessonDay_StartTime = extractLine(pathICS, i + 1);
            String lessonDay_EndTime = extractLine(pathICS, i + 2);

            String lessonDay = extractDay(lessonDay_StartTime);
            String lessonStartTime = formatTimeTest(extractTime(lessonDay_StartTime));
            String lessonEndTime = formatTimeTest(extractTime(lessonDay_EndTime));
            String lessonDuration = calculateDuration(extractTime(lessonDay_StartTime), extractTime(lessonDay_EndTime));

            //String lessonStartTime = ExtractInformation.extractTime(lessonDay_StartTime);
            //String lessonEndTime = ExtractInformation.extractTime(lessonDay_EndTime);

            String lessonTitle = extractLine(pathICS, i + 6);
            String lessonName = extractLessonName(extractLine(pathICS, i + 10), lessonTitle);
            String lessonNameShort = formatLessonName(lessonName, lessonTitle);

            String lessonType = extractLessonType(lessonTitle);
            String lessonTeacher = extractTeachersTest(extractLine(pathICS, i + 11));

            String lessonClassRoom = extractClassRoom(extractLine(pathICS, i + 6), extractLine(pathICS, i + 11));

            String line = lessonDay + "|" + lessonStartTime + "|" + lessonEndTime + "|" + lessonDuration + "|" + lessonClassRoom + "|" + lessonType + "|" + lessonName + "|" + lessonNameShort + "|" + lessonTeacher + "\n";
            ListTest.add(line);
            //Log.d("myLogT", i + " Line : " + line);
            a++;
            progressBarSnack.setProgress(a * 13);
            Log.d("myLogA", a + " : " + line);
        }

        Log.d("myLogA", "list size " + ListTest.size() + " | " + a);
        Log.d("myLogA", "Start Sort");
        Collections.sort(ListTest);
        Log.d("myLogA", "End Sort");

        //save in other file ([dataCourse]?.csv)
        //Log.d("myLogT", "" + getFilesDir());
        String header = "Day|Start|End|Duration|Classroom|Type|Name lesson|Name short|Teachers\n";
        try {
            //FileWriter myWriter = new FileWriter(path + "/data.csv");
            FileWriter myWriter = new FileWriter(pathAppData);

            myWriter.write(header);
            for (String lesson : ListTest) {
                myWriter.write(lesson);
            }
            myWriter.close();
            Log.d("myLogA", "Successfully wrote to the file.");
        } catch (IOException e) {
            Log.d("myLogA", "An error occurred : " + e);
            e.printStackTrace();
        }

        //formatting each course (delete duplication, ...)

        //formattingFile(pathApp);
        /*
        //declarations
        List<String> list = new ArrayList<>();
        boolean first = true;
        //for (int i = 2; i < nbr_lines + 1; i++){
        for (int i = 10; i < 20 + 1; i++){    //for tests
            Log.d("myLogA", "-----------------------------------");
            String line = extractLine(pathApp + "data.csv", i);
            String compareLine = extractLine(pathApp + "data.csv", i - 1);
            Log.d("myLogA", i + " | " + line);
            Log.d("myLogA", i + " | " + compareLine);


            String[] splitLesson = line.split("\\|");
            String dateBuffer, timeStartBuffer, timeEndBuffer;
            String date = dateBuffer = splitLesson[0];
            String timeStart = timeStartBuffer = splitLesson[1];
            String timeEnd = timeEndBuffer = splitLesson[2];

            String[] splitCompareLesson = compareLine.split("\\|");
            String dateCompareBuffer, timeCompareStartBuffer, timeCompareEndBuffer;
            String dateCompare = dateCompareBuffer = splitCompareLesson[0];
            String timeCompareStart = timeCompareStartBuffer = splitCompareLesson[1];
            String timeCompareEnd = timeCompareEndBuffer = splitCompareLesson[2];
            Log.d("myLogA", "line : " + dateCompareBuffer + " | " + timeCompareStartBuffer + " and " + timeCompareEndBuffer);
            Log.d("myLogA", "line : " + dateBuffer + " | " + timeStartBuffer + " and " + timeEndBuffer);
            if (dateBuffer.equals(dateCompareBuffer) && timeStartBuffer.equals(timeCompareStartBuffer) && timeEndBuffer.equals(timeCompareEndBuffer)){
                Log.d("myLogA", "Same lines");
                //it is a duplication
                if (first) {
                    list.add(fusion(line, compareLine)); //fusion lines
                }
                first = false;
            } else {
                //save line
                list.add(line);
                first = true;
            }
        }

        //save in a new final file
        Log.d("myLogA", "Show");
        for (int i = 0; i < list.size(); i++){
            Log.d("myLogA", i + " | " + list.get(i));
        }
         */
        moveFile(pathICS + "/" + fileName, pathApp + "/" + fileName);
        snackbar.dismiss();
        Log.d("myLogA", "Analyzer - End");
        return true;
    }

    private static void moveFile(String pathSource, String pathTarget){
        File sourceLocation = new File(pathSource);
        File targetLocation = new File(pathTarget);
        Log.d("myLogAF", sourceLocation + " | " + targetLocation);

        try {
            if (sourceLocation.renameTo(targetLocation)) {
                Log.d("myLogAF", "Move file successful.");
            } else {
                Log.d("myLogAF", "Move file failed.");
                if (sourceLocation.exists()) {
                    InputStream in = new FileInputStream(sourceLocation);
                    OutputStream out = new FileOutputStream(targetLocation);
                    // Copy the bits from instream to outstream
                    byte[] buf = new byte[1024];
                    int len;
                    while ((len = in.read(buf)) > 0) {
                        out.write(buf, 0, len);
                    }
                    in.close();
                    out.close();
                    Log.d("myLogAF", "Copy file successful.");
                } else {
                    Log.d("myLogAF", "Copy file failed. Source file missing.");
                }
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    @RequiresApi(api = Build.VERSION_CODES.O)
    private static void formattingFile(String path) {

        //count number of line in new file
        int nbr_lines = 0;
        try {
            nbr_lines = countLinesOld(path + "data.csv");
        } catch (IOException e) {
            e.printStackTrace();
        }
        Log.d("myLogA", "Nbr Lines : " + nbr_lines);
        //read file
        ArrayList<Integer> lines = new ArrayList<>();
        for (int i = 2; i < nbr_lines + 1; i++) {
            String line = extractLine(path + "data.csv", i);
            Log.d("myLogA", i + " | " + line);
            String[] splitLine = line.split("\\|");
            String dateBuffer;
            String date = dateBuffer = splitLine[0]; //date
            while (date.equals(dateBuffer)) {
                if (i > nbr_lines - 1) break;
                lines.add(i);
                line = extractLine(path + "data.csv", i);
                splitLine = line.split("\\|");
                date = splitLine[0];
                i++;
            }

            //for each line in this day :
            int count = 0;
            for (int a = 0; a < lines.size(); a++) {
                String lineE = extractLine(path + "data.csv", lines.get(a));
                Log.d("myLogA", lines.get(a) + " | " + line);
                String[] splitLineE = lineE.split("\\|");
                String timeStartBuffer, timeEndBuffer;
                String timeStart = timeStartBuffer = splitLineE[1];
                String timeEnd = timeEndBuffer = splitLineE[2];
                while (timeStart.equals(timeStartBuffer) && timeEnd.equals(timeEndBuffer)) {
                    a++;
                    if (a > lines.size() - 1) {
                        if (count == 0) count++;
                        break;
                    }
                    count++;
                    //for new test
                    line = extractLine(path, lines.get(a));
                    splitLineE = line.split("\\|");
                    timeStart = splitLineE[1];
                    timeEnd = splitLineE[2];
                }
            }

            Log.d("myLogA", date + " | " + lines);
            lines.clear();
        }

    }

    private static String fusion(String line1, String line2) {
        String[] splitLine1 = line1.split("\\|");
        String[] splitLine2 = line2.split("\\|");

        ArrayList<String> listLessonName = new ArrayList<>();
        /*
        classroom.add(splitLesson[4]);
        LessonNames.add(splitLesson[6]);
        */

        //String lessonClassroomShort = formatClassRoom(lessonClassroom);

        String concatClass = splitLine1[4] + " / " + splitLine2[4];
        //String concatLesson = splitLine1[6] + " / " + splitLine2[6];
        listLessonName.add(splitLine1[6]);
        listLessonName.add(splitLine2[6]);
        String concatLesson = formatConcatenateLessonName(listLessonName);
        String finalLine = splitLine1[0] + "|" + splitLine1[1] + "|" + splitLine1[2] + "|" + splitLine1[3] + "|" + concatClass + "|" + splitLine1[5] + "|" + concatLesson + "|" + splitLine1[7] + "|" + formatTeacher(splitLine1[8]);

        return finalLine;
    }

}
