package com.example.calendar;

public class Course {

    private String lessonName;
    private String lessonNameShort;
    private String type;	//private = restricted access
    private int imageType;
    private String classRoom;
    private String classRoomShort;
    private String teacher;
    private String day;
    private String timeStart;
    private String timeEnd;
    private String duration;

    public Course (String lName, String lNameShort, String lType, int lImageType, String lClassroom, String lClassroomShort, String lTeacher, String lDay, String lTimeStart, String lRTimeEnd, String lDuration){
        this.lessonName = lName;
        this.lessonNameShort = lNameShort;
        this.type = lType;
        this.imageType = lImageType;
        this.classRoom = lClassroom;
        this.classRoomShort = lClassroomShort;
        this.teacher = lTeacher;
        this.day = lDay;
        this.timeStart = lTimeStart;
        this.timeEnd = lRTimeEnd;
        this.duration = lDuration;
    }

    //return the value of variable name
    public String getLessonName(){
        return lessonName;
    }
    //set a value
    public void setLessonName(String newName){
        this.lessonName = newName;
    }
    //---- Name short ----
    public String getLessonNameShort(){
        return lessonNameShort;
    }
    public void setLessonNameShort(String newName){
        this.lessonNameShort = newName;
    }

    //---- Type ----
    public String getLessonType(){
        return type;
    }
    public void setLessonType(String newLessonType){
        this.type = newLessonType;
    }

    //---- Type Image ----
    public int getImageLessonType(){
        return imageType;
    }
    public void setImageLessonType(int newImageLessonType){
        this.imageType = newImageLessonType;
    }

    //---- Classroom ----
    public String getClassRoom(){
        return classRoom;
    }
    public void setClassRoom(String newClass){
        this.classRoom = newClass;
    }

    //---- Classroom short ----
    public String getClassRoomShort(){
        return classRoomShort;
    }
    public void setClassRoomShort(String newClass){
        this.classRoomShort = newClass;
    }

    //---- teachers ----
    public String getTeachers(){
        return teacher;
    }
    public void setTeachers(String newTeachers){
        this.teacher = newTeachers;
    }

    //---- day ----
    public String getDay(){
        return day;
    }
    public void setDay(String newDay){
        this.day = newDay;
    }

    //---- timeStart ----
    public String getTimeStart(){
        return timeStart;
    }
    public void setTimeStart(String newTimeStart){
        this.timeStart = newTimeStart;
    }

    //---- timeEnd ----
    public String getTimeEnd(){
        return timeEnd;
    }
    public void setTimeEnd(String newTimeEnd){
        this.timeEnd = newTimeEnd;
    }

    public String getDuration(){
        return duration;
    }
    public void setDuration(String newDuration){
        this.duration = newDuration;
    }
}
