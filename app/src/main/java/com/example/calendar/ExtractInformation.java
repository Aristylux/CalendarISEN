package com.example.calendar;

import static java.lang.Character.isDigit;

import android.os.Build;
import android.util.Log;

import androidx.annotation.RequiresApi;

import java.io.BufferedInputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Locale;
import java.util.Scanner;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import java.util.stream.Stream;

public class ExtractInformation {

    //private final static int MAX_CHARACTER = 15;
    private final static int MAX_CHARACTER = 10;
    /*
     * extractLessonName :
     * line is like : - Libellé.Cours : Semestre 5 - Mathématiques\n
     * return : "Mathématiques"
     *
     * if line is like : - Libellé.Cours : \n
     * SOSLine save that, this line is like : SUMMARY:CM Réunion A_DISTANCE YUSHCHENKO Lidiya\n
     * then the result is : "Réunion"
     */
    public static String extractLessonName(String line, String SOSLine) {
        //Log.d("myLog", "extract lesson");
        if(line.length() != 0) {
            StringBuilder lesson = new StringBuilder();
            int i = 5;
            while (line.charAt(i) != '-') {
                if (i + 1 == line.length()) {    //if nothing
                    return extractLessonNameExcept(SOSLine);
                    //return "";
                }
                i++;
            }
            for (int a = i + 2; a < line.length() - 2; a++) {    //-1 for delete last char (carriage return)
                lesson.append(line.charAt(a));
            }
            //Log.d("myLog", "lesson extracted : " + lesson);
            return lesson.toString();
        } else {
            return "Error";
        }
    }

    /*
     * extractLessonNameExcept :
     * line is like : SUMMARY:CM Réunion A_DISTANCE YUSHCHENKO Lidiya\n
     * return : "Réunion"
     * it is generally one word in this line when " - Libellé.Cours : \n" is empty
     */
    private static String extractLessonNameExcept(String line){
        //Log.d("myLog", "extract lesson save : " + line);
        if(line.length() != 0) {
            StringBuilder lesson = new StringBuilder();
            int i = 0;
            while (line.charAt(i) != ' ') {
                i++;
            }
            i++;
            //while (line.charAt(i) != ' ' || line.charAt(i) != '0' ){
            //while (line.charAt(i) != ' '){
            while (detectEndLessonName(line,i)){
                lesson.append(line.charAt(i));
                i++;
            }
            return lesson.toString();
        } else {
            return "Error";
        }
    }

    private static boolean detectEndLessonName(String line, int position){
        //Log.d("myLog", "detectEndLessonName save : " + line.charAt(position));
        if (line.charAt(position) == ' '){
            //it's maybe end of the name
            //if (isANumber(line.charAt(position+1)) || isAUpperCharacter(line.charAt(position+1))){
            if (isDigit(line.charAt(position+1)) || nextWord(line, position) || line.charAt(position + 1) == ' '){
            //if (isDigit(line.charAt(position+1)) || nextWord(line, position)){
                //it's the end
                return false;
            }
        }
        return true;
    }

    /*
    private static boolean isAUpperCharacter(char character) {
        boolean flag = isUpperCase(character);
        Log.d("myLog", character + " is a upper character ? : " + flag);
        return flag;
    }
    */

    private static boolean nextWord(String line, int position){
        //Log.d("myLog", "nextWord : " + line + " | " + position );
        final String amphi = "AMPHI";
        final String chalucet = "CHALU";
        final String outdoor = "EXT";
        int verification = 0;
        //line.charAt(position+1); //character 1 'A' for AMPHI and 'C' for CHALUCET
        //for(int i = 0; i < outdoor.length(); i++){
        for(int i = 0; i < outdoor.length(); i++){
            if (line.charAt(position+1) == '_') return true;
            if (line.charAt(position+1) == amphi.charAt(i) || line.charAt(position+1) == chalucet.charAt(i) || line.charAt(position+1) == outdoor.charAt(i)){
            //if (line.charAt(position+1) == amphi.charAt(i) || line.charAt(position+1) == chalucet.charAt(i)){
                verification++;
            }
            position++;
        }
        //Log.d("myLog", "verification : " + verification );
        //not 3 if a accent
        return verification == 3; //it's the same name
    }

/*
    private static boolean isANumber(char character) {
        boolean flag = isDigit(character);
        Log.d("myLog", character + " is a number ? : " + flag);
        return flag;
    }
*/

    /*
     * formatLessonName :
     * check if the original name is too long for the title
     * if it is too long it is shortened
     */
    public static String formatLessonName(String lessonName, String SOSLine){
        if(lessonName.length() != 0) {
            StringBuilder newLesson = new StringBuilder();
            String anotherName = extractLessonNameExcept(SOSLine);
            //if (lessonName.length() > MAX_CHARACTER) {
            if (lessonName.length() > MAX_CHARACTER && anotherName.length() > MAX_CHARACTER) {
                //Log.d("myLogE", String.valueOf(lessonName.length()));
                //int j = 0;
                /*
                while (lessonName.charAt(j) != ' ') {
                    newLesson.append(lessonName.charAt(j));
                    j++;
                }
                */
                for (int i = 0; i < MAX_CHARACTER; i++){
                    newLesson.append(lessonName.charAt(i));
                }
                //Log.d("myLogE", newLesson.toString());
                //newLesson.append(' ');
                //add three dots
                for (int i = 0; i < 3; i++) {
                    newLesson.append('.');
                }
                return newLesson.toString();
            } else {
                //String anotherName = extractLessonNameExcept(SOSLine);
                //if (anotherName.length() < MAX_CHARACTER && !compareName(lessonName, anotherName)){
                if (!compareName(lessonName, anotherName)){
                    //Log.d("myLogE", "replace");
                    return anotherName;
                }
                return lessonName;
            }
        } else {
            return "No title";
        }
    }

    /*
     * compareName :
     * compare : Réseaux | RESEAU
     * this names are different
     */
    private static boolean compareName(String lessonName, String anotherName) {
        //Log.d("myLog", "compare : " + lessonName + " | " + anotherName);
        //calculate if 3 first letter contain a accent
        int compare = 4;
        if (Pattern.matches(".*[éàèêë]*.", lessonName)){
            compare--;
        }
        //show 3 first letters
        int verification = 0;
        for (int i = 0; i < 4; i++){
            Pattern pattern = Pattern.compile(String.valueOf(anotherName.charAt(i)), Pattern.CASE_INSENSITIVE);
            Matcher matcher = pattern.matcher(String.valueOf(lessonName.charAt(i)));
            //Log.d("myLog", "compare : " + lessonName.charAt(i) + " | " + anotherName.charAt(i));
            if (matcher.find()){
                //Log.d("myLog", "same : " + lessonName.charAt(i) + " | " + anotherName.charAt(i));
                verification++;
            }
        }
        if (verification >= compare && lessonName.length() == anotherName.length()+1){ //not 3 if a accent
            //Log.d("myLog", "it's the same");
            return true; //it's the same name
        } else {
            return false;
        }
    }

    /*
     * formatConcatenateLessonName :
     *
     * [SHES - Créativité, SHES - Créativité, SHES - Créativité, SHES - Créativité, SHES - Gestion de projet, SHES - Gestion de projet, SHES - Gestion de projet, SHES - Gestion de projet, SHES - Résolution de Problèmes, SHES - Résolution de Problèmes, SHES - Résolution de Problèmes, SHES - Résolution de Problèmes]
     * after check double :
     * "SHES - Créativité / SHES - Gestion de projet / SHES - Résolution de Problèmes"
     */
    public static String formatConcatenateLessonName(ArrayList<String> Lessons){
        //check doubles
        //Log.d("myLogE", String.valueOf(Lessons));
        Collections.sort(Lessons);
        //Log.d("myLogE", String.valueOf(Lessons));
        ArrayList<String> NewLessons = new ArrayList<>();
        ArrayList<String> CheckLessons = new ArrayList<>();
        for (int i = 0; i < Lessons.size(); i++){
            //research if Lessons.get(i) is in this list
            boolean research = false;
            for (int a = 0; a < CheckLessons.size(); a++){
                if (CheckLessons.get(a).equals(Lessons.get(i))){
                    //if item is already in the list
                    //do not add him in new list
                    research = true;
                }
            }
            //if the research has no found the element
            //add in new list and check list
            if (!research){
                CheckLessons.add(Lessons.get(i));
                NewLessons.add(Lessons.get(i));
            }
        }
        //Log.d("myLogE", String.valueOf(NewLessons));
        //end check double
        //concatenate
        StringBuilder LessonsConcatenate = new StringBuilder();
        for (int i = 0; i < NewLessons.size(); i++){
            if (i != 0){
                //LessonsConcatenate.append("\n");
                LessonsConcatenate.append(" / ");
            }
            LessonsConcatenate.append(NewLessons.get(i));
        }
        //Log.d("myLogE", LessonsConcatenate.toString());
        return LessonsConcatenate.toString();
    }

    /*
     * extractLessonType :
     * line is like : SUMMARY:CM Maths 559 YUSHCHENKO Lidiya\n
     * return "CM"
     */
    public static String extractLessonType(String line){
        if(line.length() != 0) {
            StringBuilder type = new StringBuilder();
            int i = 0;
            //go after :
            while (line.charAt(i) != ':') {
                i++;
            }
            i++;
            while (line.charAt(i) != ' ') {
                type.append(line.charAt(i));
                i++;
            }
            return type.toString();
        } else {
            return "Error";
        }
    }

    /*
     * extractClassRoom :
     * line is : SUMMARY:CM Maths 559 YUSHCHENKO Lidiya\n
     * inter is :  - intervenant : YUSHCHENKO Lidiya\n
     * result is ; 559
     * or like : AMPHI,110...
     */
    public static String extractClassRoom(String line, String speakersLine){
        if(line.length() != 0 && speakersLine.length() != 0) {
            StringBuilder classroom = new StringBuilder();
            //System.out.println(line.charAt(line.length()-(inter.length()-15)));
            int a = line.length() - (speakersLine.length() - 15);

            while (line.charAt(a) != ' ') {
                classroom.insert(0, line.charAt(a));
                a--;
            }
            return classroom.toString();
        } else {
            return "Error";
        }
    }

    /*
     * concatenateClassRoom :
     * classRoom : ["515", "559", "560"]
     * return : "515 - 559 - 560"
     */
    public static String concatenateClassRoom(ArrayList<String> classRoom){
        StringBuilder classRooms = new StringBuilder();
        boolean first = true;
        for(int i = 0; i < classRoom.size(); i++){
            if (first) {
                classRooms = new StringBuilder(classRoom.get(i));
                first = false;
            } else {
                Pattern pattern = Pattern.compile(classRoom.get(i), Pattern.CASE_INSENSITIVE);
                Matcher matcher = pattern.matcher(classRooms);
                if(!matcher.find()){
                    classRooms.append(" - ").append(classRoom.get(i));
                }
            }
        }
        return classRooms.toString();
    }

    public static String formatClassRoom(String classRooms){
        StringBuilder newLesson = new StringBuilder();
        if(classRooms.length() > MAX_CHARACTER) {
            //Log.d("myLog", String.valueOf(classRooms.length()));
            //int j = 0;
            /*
            while (classRooms.charAt(j) != ' ') {
                newLesson.append(classRooms.charAt(j));
                j++;
            }
            */
            for (int i = 0; i < MAX_CHARACTER; i++){
                newLesson.append(classRooms.charAt(i));
            }
            //Log.d("myLog", newLesson.toString());
            newLesson.append(' ');
            for(int i = 0; i < 3; i++){
                newLesson.append('.');
            }
            return newLesson.toString();
        }else{
            return classRooms;
        }
    }

    /*
     * extract teachers
     * line is :  - intervenant : YUSHCHENKO Lidiya\n
     * result is : YUSHCHENKO Lidiya
     */
    public static String extractTeachers(String line){
        if(line.length() != 0) {
            String teacher;
            String str = line.replaceFirst(" - intervenant : ", "").replaceAll(" / ", "\n");
            //teacher = str.replaceAll("\n", "");
            teacher = str.substring(0, str.length() - 2);
            return teacher;
        } else {
            return "Error";
        }
    }


    public static String extractTeachersTest(String line){
        if(line.length() != 0) {
            String teacher;
            String str = line.replaceFirst(" - intervenant : ", "");
            //teacher = str.replaceAll("\n", "");
            teacher = str.substring(0, str.length() - 2);
            return teacher;
        } else {
            return "Error";
        }
    }

    /*
     * formatTeacher :
     * "LAHAYE Jean-Claude / LECA Axel / LECA Axel / LECA Axel"
     *
     * return :
     * "LAHAYE Jean-Claude / LECA Axel"
     */
    public static String formatTeacher(String teachersLine){
        Log.d("myLogE", teachersLine);
        String[] splitLesson = teachersLine.split(" / ");
        Log.d("myLogE", String.valueOf(splitLesson.length));
        ArrayList<String> TeachersList = new ArrayList<>();
        for (int i = 0; i  < splitLesson.length; i++){
            Log.d("myLogE", i + " | " + splitLesson[i]);
            TeachersList.add(splitLesson[i]);
        }
        ArrayList<String> finalTeacher = checkDouble(TeachersList);
        Log.d("myLogE", String.valueOf(finalTeacher));
        StringBuilder formatTeachers = new StringBuilder();
        for (int i = 0; i < finalTeacher.size(); i++){
            if (i != 0){
                formatTeachers.append(" / ");
            }
            formatTeachers.append(finalTeacher.get(i));
        }
        //String teachers = teachersLine.replaceAll(" / ", "\n");
        return formatTeachers.toString();
    }

    public static ArrayList<String> checkDouble(ArrayList<String> list){
        //check doubles
        //Log.d("myLogE", String.valueOf(Lessons));
        Collections.sort(list);
        //Log.d("myLogE", String.valueOf(Lessons));
        ArrayList<String> newList = new ArrayList<>();
        ArrayList<String> CheckLessons = new ArrayList<>();
        for (int i = 0; i < list.size(); i++){
            //research if Lessons.get(i) is in this list
            boolean research = false;
            for (int a = 0; a < CheckLessons.size(); a++){
                if (CheckLessons.get(a).equals(list.get(i))){
                    //if item is already in the list
                    //do not add him in new list
                    research = true;
                }
            }
            //if the research has no found the element
            //add in new list and check list
            if (!research){
                CheckLessons.add(list.get(i));
                newList.add(list.get(i));
            }
        }
        //Log.d("myLogE", String.valueOf(NewLessons));
        //end check double
        return newList;
    }

    /*
     * extractDay :
     * extract day
     * line is : DTSTART:20210923T120000
     * result : 20210923
     */
    public static String extractDay(String line){
        if(line.length() != 0) {
            StringBuilder day = new StringBuilder();
            int a = 0;
            //move cursor at ":"
            while (line.charAt(a) != ':') {
                a++;
            }
            //save date
            a++;
            while (line.charAt(a) != 'T') {
                day.append(line.charAt(a));
                a++;
            }
            return day.toString();
        } else {
            return "Error";
        }
    }

    /*
     * extractTime :
     * extract timeStart/End
     * line like : 	DTSTART:20210923T120000
     * 		or		DTEND:20210923T140000
     * return : 1200
     * 		or	1400
     */
    public static String extractTime(String line){
        if(line.length() != 0) {
            StringBuilder time = new StringBuilder();
            int position = 0;
            //move cursor at "T"
            while (line.charAt(position) != ':') {
                position++;
            }
            while (line.charAt(position) != 'T') {
                position++;
            }
            for (int i = position + 1; i < position + 5; i++) {
                time.append(line.charAt(i));
            }
            return time.toString();
        } else {
            return "Error";
        }
    }

    /*
     * formatTime :
     * line like : 	1200
     * 		or		0800
     * return : 12h00
     * 		or	8h00
     */
    public static String formatTime(String time){
        StringBuilder formattedTime = new StringBuilder();
        int position = 0;
        if(time.charAt(position) == '0'){
            position++;
        }
        while(position < 4){
            formattedTime.append(time.charAt(position));
            if(position == 1){
                formattedTime.append('h');
            }
            position++;
        }
        return formattedTime.toString();
    }

    /*
     * formatTime :
     * line like : 	1200
     * 		or		0800
     * return : 12h00
     * 		or	08h00
     */
    public static String formatTimeTest(String time){
        StringBuilder formattedTime = new StringBuilder();
        int position = 0;
        while(position < 4){
            formattedTime.append(time.charAt(position));
            if(position == 1){
                formattedTime.append('h');
            }
            position++;
        }
        return formattedTime.toString();
    }

    /*
     * calculateDuration :
     * start and end like : 0800 1200
     * return : 4h
     */
    public static String calculateDuration(String start, String end){
        StringBuilder duration = new StringBuilder();
        int calculate = CalculateUtil.strToNumber(end) - CalculateUtil.strToNumber(start);
        //Log.d("myLog", String.valueOf(calculate));
        //format
        String durations = String.valueOf(calculate);   //convert
        //Log.d("myLog", String.valueOf(durations.length()));
        int position = 0;
        while(position < durations.length()){
            if(durations.charAt(position) != '0') {
                duration.append(durations.charAt(position));
                if (position == 0) {
                    duration.append('h');
                }
            }
            position++;
        }
        //Log.d("myLog", String.valueOf(duration));
        return String.valueOf(duration);
    }

    public static ArrayList<Integer> checkDate(String path, String Date){
        //Log.d("myLog", "check date");
        ArrayList<Integer> lines = new ArrayList<>();
        int line = 0;
        try {
            File myFile = new File(path);
            //Log.d("myLog", String.valueOf(myFile.exists()));
            Scanner myReader = new Scanner(myFile);
            myReader.useLocale(Locale.FRANCE);
            //Log.d("myLog", String.valueOf(myReader.hasNextLine()));
            while (myReader.hasNextLine()) {
                String data = myReader.nextLine();
                //Log.d("myLog", "data : " + data);
                Pattern pattern = Pattern.compile(Date, Pattern.CASE_INSENSITIVE);
                Matcher matcher = pattern.matcher(data);
                if (matcher.find()) {
                    lines.add(line);
                    //Log.d("myLog", "add lines : " + line);
                }
                line++;
            }
            myReader.close();
            //Log.d("myLog", "end check date");
            return lines;
        } catch (FileNotFoundException e) {
            Log.d("myLog", "checkDate, An error occurred: " + e.toString());
        }
        return null;
    }

    public static int lineFirstCourses(String path) {
        int numberLine = 1;
        try {
            File myFile = new File(path);
            Scanner myReader = new Scanner(myFile);
            while (myReader.hasNextLine()) {
                String data = myReader.nextLine();
                Pattern pattern = Pattern.compile("BEGIN:VEVENT", Pattern.CASE_INSENSITIVE);
                Matcher matcher = pattern.matcher(data);
                if(matcher.find()){
                    break;
                }
                numberLine++;
            }
            myReader.close();
        } catch (FileNotFoundException e) {
            Log.d("myLog", "An error occurred.");
            e.printStackTrace();
        }
        return numberLine;
    }

    static int numberOfLessons(String path){
        int numberLessons = 0;
        try {
            File myFile = new File(path);
            Scanner myReader = new Scanner(myFile);
            while (myReader.hasNextLine()) {
                String data = myReader.nextLine();
                Pattern pattern = Pattern.compile("BEGIN:VEVENT", Pattern.CASE_INSENSITIVE);
                Matcher matcher = pattern.matcher(data);
                if(matcher.find()){
                    numberLessons++;
                }
            }
            myReader.close();
        } catch (FileNotFoundException e) {
            Log.d("myLog", "An error occurred.");
            e.printStackTrace();
        }
        return numberLessons;
    }

    static int numberOfLines(String path){
        int numberLines = 0;
        try {
            File myFile = new File(path);
            Scanner myReader = new Scanner(myFile);
            while (myReader.hasNextLine()) {
                String data = myReader.nextLine();
                numberLines++;
            }
            myReader.close();
        } catch (FileNotFoundException e) {
            Log.d("myLog", "An error occurred.");
            e.printStackTrace();
        }
        return numberLines;
    }

    public static int countLinesOld(String filename) throws IOException {
        try (InputStream is = new BufferedInputStream(new FileInputStream(filename))) {
            byte[] c = new byte[1024];
            int count = 0;
            int readChars = 0;
            boolean empty = true;
            while ((readChars = is.read(c)) != -1) {
                empty = false;
                for (int i = 0; i < readChars; ++i) {
                    if (c[i] == '\n') {
                        ++count;
                    }
                }
            }
            return (count == 0 && !empty) ? 1 : count;
        }
    }

    @RequiresApi(api = Build.VERSION_CODES.O)
    public static String extractLine(String path, int numberLine){
        //Log.d("myLog", "line : " + (numberLine+1));
        //Log.d("myLog", String.valueOf(Paths.get(path)));
        String line;
        try (Stream<String> lines = Files.lines(Paths.get(path))){ //you need API 28 !
            line = lines.skip(numberLine-1).findFirst().get();
            return line;
        }
        catch (IOException e){
            Log.d("myLog", "ExtractLine, An error occurred: " + e.toString());
            //Log.d("myLog", path + " | " + numberLine);
        }
        return "";
    }


}
