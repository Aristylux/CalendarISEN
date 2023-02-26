package com.example.calendar;

import android.app.AlertDialog;
import android.app.Dialog;
import android.content.Context;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.recyclerview.widget.RecyclerView;

import java.util.List;

public class AdapterLesson extends RecyclerView.Adapter<AdapterLesson.MyViewHolder> {

    //String[] dataNameCourse, dataStartTimeCourse, dataEndTimeCourse, dataTypeCourse, dataClassroomCourse;
    Context mContext; //like database
    List<Course> listLessons;
    Dialog testDialog;

    /*
    public MyAdapter(Context context,String[] nameCourse, String[] startTimeCourse, String[] endTimeCourse, String[] typeCourse, String[] classroomCourse){
        mContext = context;
        dataNameCourse = nameCourse;
        dataStartTimeCourse = startTimeCourse;
        dataEndTimeCourse = endTimeCourse;
        dataTypeCourse = typeCourse;
        dataClassroomCourse = classroomCourse;
    }
    */

    public AdapterLesson(Context context, List<Course> list){
        this.mContext = context;
        this.listLessons = list;
    }

    @NonNull
    @Override
    public MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        LayoutInflater inflater = LayoutInflater.from(mContext);
        View view = inflater.inflate(R.layout.item_vertical_course_test, parent, false);
        return new MyViewHolder(view);
    }

    /*
     * update view
     */
    /*
    @Override
    public void onBindViewHolder(@NonNull MyViewHolder holder, int position) {
        holder.textNameCourse.setText(dataNameCourse[position]);
        holder.textStartTimeCourse.setText(dataStartTimeCourse[position]);
        holder.textEndTimeCourse.setText(dataEndTimeCourse[position]);
        holder.textTypeCourse.setText(dataTypeCourse[position]);
        holder.textClassroomCourse.setText(dataClassroomCourse[position]);

        //on clic, show popup

        holder.mainLayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(mContext, PopupCourse.class);
                intent.putExtra("data1", dataClassroomCourse[position]);
                mContext.startActivity(intent);
            }
        });
    }
    */
    @Override
    public void onBindViewHolder(@NonNull MyViewHolder holder, int position) {
        Course lesson = listLessons.get(position);
        holder.textNameCourse.setText(lesson.getLessonNameShort());
        holder.textStartTimeCourse.setText(lesson.getTimeStart());
        holder.textEndTimeCourse.setText(lesson.getTimeEnd());
        holder.textTypeCourse.setText(lesson.getLessonType());
        holder.textClassroomCourse.setText(lesson.getClassRoomShort());

        //on clic, show popup
        holder.mainLayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                AlertDialog.Builder builder = new AlertDialog.Builder(view.getRootView().getContext());
                View dialogView = LayoutInflater.from(view.getRootView().getContext()).inflate(R.layout.popup_course, null);
                //ImageView closeButton;

                TextView popup_name_course,
                        popup_classroom_course,
                        popup_start_time_course,
                        popup_end_time_course,
                        popup_duration_course,
                        popup_type_course,
                        popup_teacher_course;
                ImageView type;

                popup_name_course = dialogView.findViewById(R.id.course_name);
                popup_classroom_course = dialogView.findViewById(R.id.classroom_subtitle);
                popup_start_time_course = dialogView.findViewById(R.id.time_start_subtitle);
                popup_end_time_course = dialogView.findViewById(R.id.time_end_subtitle);
                popup_duration_course = dialogView.findViewById(R.id.time_duration_subtitle);
                popup_type_course = dialogView.findViewById(R.id.type_subtitle);
                popup_teacher_course = dialogView.findViewById(R.id.teacher_subtitle);
                type = dialogView.findViewById(R.id.image_type_course);
/*
                popup_name_course = epicDialog.findViewById(R.id.course_name);
                popup_classroom_course = epicDialog.findViewById(R.id.classroom_subtitle);
                popup_start_time_course = epicDialog.findViewById(R.id.time_start_subtitle);
                popup_end_time_course = epicDialog.findViewById(R.id.time_end_subtitle);
                popup_duration_course = epicDialog.findViewById(R.id.time_duration_subtitle);
                popup_type_course = epicDialog.findViewById(R.id.type_subtitle);
                popup_teacher_course = epicDialog.findViewById(R.id.teacher_subtitle);
*/
                popup_name_course.setText(lesson.getLessonName());
                popup_classroom_course.setText(lesson.getClassRoom());
                popup_start_time_course.setText(lesson.getTimeStart());
                popup_end_time_course.setText(lesson.getTimeEnd());
                popup_duration_course.setText(lesson.getDuration());
                popup_type_course.setText(lesson.getLessonType());
                popup_teacher_course.setText(lesson.getTeachers());
                //replace : popup_image_lesson_type.setText(lesson.getImage());
                type.setImageResource(lesson.getImageLessonType());
                /*
                switch (lesson.getLessonType()){
                    case "PROJET":
                        type.setImageResource(R.drawable.type_document);
                        break;
                    case "EXAMEN":
                        type.setImageResource(R.drawable.type_exam);
                        break;
                    case "CM":
                        type.setImageResource(R.drawable.type_presentation);
                        break;
                    case "REUNION":
                        type.setImageResource(R.drawable.type_meeting);
                        break;
                    case "TD":
                        type.setImageResource(R.drawable.type_process);
                        break;
                    case "CONFERENCE":
                        type.setImageResource(R.drawable.type_conference);
                        break;
                    case "RATTRAPAGE":
                        type.setImageResource(R.drawable.type_fail_exam);
                        break;
                    //default:
                    //    type.setScaleType();
                }

                 */

                /*
                closeButton = (ImageView) dialogView.findViewById(R.id.close_button);
                closeButton.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        builder.set
                    }
                });*/
                //dialogView.setBackground(new ColorDrawable(Color.TRANSPARENT));
                //dialogView.setBackground(new ColorDrawable(Color.TRANSPARENT));
                builder.setView(dialogView);
                //epicDialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
                //epicDialog.show();
                builder.setCancelable(true);
                testDialog = builder.create();
                testDialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
                testDialog.show();
                //builder.show();
            }
        });
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
    /*
    public static class MyViewHolder extends RecyclerView.ViewHolder {

        TextView textNameCourse,textStartTimeCourse, textEndTimeCourse, textTypeCourse,  textClassroomCourse;

        ConstraintLayout mainLayout;

        Dialog myDialog;

        public MyViewHolder(@NonNull View itemView) {
            super(itemView);
            textNameCourse = itemView.findViewById(R.id.course_title);
            textStartTimeCourse = itemView.findViewById(R.id.course_start_time);
            textEndTimeCourse = itemView.findViewById(R.id.course_end_time);
            textTypeCourse = itemView.findViewById(R.id.type_course);
            textClassroomCourse = itemView.findViewById(R.id.course_classroom);

            mainLayout = itemView.findViewById(R.id.mainLayoutCourse);

            //myDialog = new Dialog(this);
        }
    }
     */
    public static class MyViewHolder extends RecyclerView.ViewHolder {

        TextView textNameCourse,textStartTimeCourse, textEndTimeCourse, textTypeCourse,  textClassroomCourse;

        ConstraintLayout mainLayout;

        public MyViewHolder(@NonNull View itemView) {
            super(itemView);
            textNameCourse = itemView.findViewById(R.id.course_title);
            textStartTimeCourse = itemView.findViewById(R.id.course_start_time);
            textEndTimeCourse = itemView.findViewById(R.id.course_end_time);
            textTypeCourse = itemView.findViewById(R.id.type_course);
            textClassroomCourse = itemView.findViewById(R.id.course_classroom);

            mainLayout = itemView.findViewById(R.id.mainLayoutCourse);
        }
    }
}
