package com.example.calendar;

import android.app.AlertDialog;
import android.app.Dialog;
import android.content.Context;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Build;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.RequiresApi;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import java.util.List;

public class AdapterWeekly extends RecyclerView.Adapter<AdapterWeekly.MViewHolder> {

    //String[] dataNameCourse, dataStartTimeCourse, dataEndTimeCourse, dataTypeCourse, dataClassroomCourse;
    Context mContext; //like database
    List<Course> listLessons;

    public AdapterWeekly(Context context, List<Course> list){
        this.mContext = context;
        this.listLessons = list;
    }

    @NonNull
    @Override
    public MViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        LayoutInflater inflater = LayoutInflater.from(mContext);
        View view = inflater.inflate(R.layout.item_vertical_course_test, parent, false);
        return new MViewHolder(view);
    }


    /*
     * update view
     */
    @RequiresApi(api = Build.VERSION_CODES.O)
    @Override
    public void onBindViewHolder(@NonNull MViewHolder holder, int position) {
        Course lesson = listLessons.get(position);
        //set recyclerView
        /*
        List<Course> listLessons = Routine.routineTest(date, dateFile, firstName, lastName);
        TextView textView = holder.recyclerView.findViewById(idNbrDay);
        textView.setText(nbrDay);
        if (listLessons != null) {
            AdapterLesson myObjAdapter = new AdapterLesson(getContext(), listLessons);
            RecyclerView mRecyclerView = view.findViewById(idRecycler);
            mRecyclerView.setAdapter(myObjAdapter);
            mRecyclerView.setNestedScrollingEnabled(false);
            mRecyclerView.setLayoutManager(new LinearLayoutManager(getContext()));
        }

         */


    }

    /*
     * count the number of element in the array
     * it is a number of the lessons
     */
    @Override
    public int getItemCount() {
        //return dataTypeCourse.length; //numbers of element
        return listLessons.size();
    }


    public static class MViewHolder extends RecyclerView.ViewHolder {

        ConstraintLayout mainLayout;

        RecyclerView recyclerView;

        public MViewHolder(@NonNull View itemView) {
            super(itemView);
            recyclerView = itemView.findViewById(R.id.recycler_view);

            mainLayout = itemView.findViewById(R.id.mainLayoutWeeklyTest);
        }
    }
}
