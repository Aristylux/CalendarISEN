package com.example.calendar;

import static android.content.Context.MODE_PRIVATE;

import android.content.Context;
import android.util.Log;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.io.InputStreamReader;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Scanner;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class FilesUtil {

    static boolean researchFile(String path, String research){
        File directory = new File(path);
        File[] files = directory.listFiles();
        if(directory.canRead() && files != null) {      //show all files if show is true
            for (File file : files) {
                Pattern pattern = Pattern.compile(research, Pattern.CASE_INSENSITIVE);
                Matcher matcher = pattern.matcher(file.getName());
                if (matcher.find()) {
                    return true;
                }
            }
        }
        return false;
    }

    static boolean showFile(String path, boolean show){
        Log.d("myLog", "Path : " + path);
        File directory = new File(path);
        File[] files = directory.listFiles();
        if(directory.canRead() && files != null) {
            Log.d("myLog", "Number of files : " + files.length);
            //show all files if show is true
            for (File file : files) {
                if(show) Log.d("myLog", "Filename : " + file.getName());
            }
            return true; //success
        }else {
            Log.d("myLog", "Error, path is break");
            Log.d("myLog", " - Is directory : " + directory.isDirectory());
            Log.d("myLog", " - Can read     : " + directory.canRead());
            Log.d("myLog", "Number of files : " + Arrays.toString(files));
            return false; //error
        }
    }

    /*
    static int compareFiles(String fName, String lName, String date) {
        //String researchPath = downloadPath + fName + "." + lName + "/";
        //String researchPath = Environment.getExternalStorageDirectory().toString() + "/Pictures";
        String researchPath = getExternalFilesDir(null).toString() + "/" + fName + "." + lName;
        String presentFileName = fName + "." + lName + "_" + date + ".ics";
        if(showFile(researchPath, true)){
            File directory = new File(researchPath);
            File[] files = directory.listFiles();
            File presentFile = new File(researchPath + "/" + presentFileName);
            Log.d("myLog", "present Filename    : " + presentFile);
            assert files != null;
            while (!presentFile.exists()){
                //wait file exist (end download)
            }
            Log.d("myLog", String.valueOf(presentFile.exists()));
            if(files.length > 0) {
                File lastFile = new File(researchPath + "/" +  files[files.length-1].getName());
                Log.d("myLog", "last Filename       : " + lastFile);
                Log.d("myLog", String.valueOf(lastFile.exists()));
            /*
            File testFile = new File(getFilesDir() + "/stat.txt");
            Log.d("myLog", String.valueOf(testFile));
            Log.d("myLog", String.valueOf(presentFile.compareTo(testFile)));    //return 15
             */
    /*
                Log.d("myLog", String.valueOf(presentFile.compareTo(lastFile)));    //return 1
                showFile(String.valueOf(getFilesDir()), false);
                return presentFile.compareTo(lastFile);
            }else{
                Log.d("myLog", "nothing");
                return 0;
            }
        }else {
            return 0;
        }
    }
    */

    /*
    static public void createDirectory(String dirName){
        // : /storage/emulated/0/Android/data/com.example.calendar/files/test
        File file = new File(getExternalFilesDir(null) + "/" + dirName);
        Log.d("myLog", getExternalFilesDir(null) + "/" + dirName);

        if(!file.exists()){
            file.mkdir();
        }
    }

    static public void createDirectoryTest(String dirName){
        //create directory
        File directory = new File(getFilesDir() + "/" + dirName);
        if(!directory.exists()){
            directory.mkdir();
        }
    }
     */

    /*
     * deleteFile :
     * delete file or directory !
     */
    static public boolean deleteFile(String path, String file_name){
        File fileToDelete = new File(path, file_name);
        Log.d("myLogD", fileToDelete.toString());
        boolean deleted = fileToDelete.delete();
        return deleted;
    }

    /*
    static public void save(String file_name, String sentence){
        FileOutputStream outputStream = null;
        try {
            outputStream = openFileOutput(file_name, MODE_PRIVATE);
            outputStream.write(sentence.getBytes(StandardCharsets.UTF_8));
            Log.d("myLog", "Saved to " + getFilesDir() + " | data : " + sentence);
            outputStream.close();
        } catch (IOException e) {
            e.printStackTrace();
        }finally {
            if (outputStream != null){
                try {
                    outputStream.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }
    }
     */

    static public boolean saveNames(String file_name, String[] sentence, Context context){
        FileOutputStream outputStream = null;
        String carriageReturn = "\n";
        try {
            outputStream = context.openFileOutput(file_name, MODE_PRIVATE);
            for (String line : sentence) {
                outputStream.write(line.getBytes(StandardCharsets.UTF_8));
                outputStream.write(carriageReturn.getBytes(StandardCharsets.UTF_8));
            }
            outputStream.close();
            Log.d("myLogZ", "Successfully wrote to the file.");
            Log.d("myLog", "Saved to " + context.getFilesDir());
            return true;
        } catch (IOException e) {
            Log.d("myLogZ", "An error occurred : " + e);
            return false;
        }finally {
            if (outputStream != null){
                try {
                    outputStream.close();
                } catch (IOException e) {
                    e.printStackTrace();
                    Log.d("myLogZ", "An error occurred : " + e);
                }
            }
        }
    }

    /*
    static public boolean saveNames(String file_name, String[] sentence){
        Log.d("myLogZ", "Save");
        try {
            //FileWriter myWriter = new FileWriter(path + "/data.csv");
            FileWriter myWriter = new FileWriter(file_name);
            for (String line : sentence) {
                myWriter.write(line);
            }
            myWriter.close();
            Log.d("myLogZ", "Successfully wrote to the file.");
            return true;
        } catch (IOException e) {
            Log.d("myLogZ", "An error occurred : " + e);
            return false;
        }
    }
    */

    static public String[] readNames(String file_name, Context context){
        List<String> itemList = new ArrayList<String>();
        Scanner scanner = null;
        try {
            scanner = new Scanner(new File(context.getFilesDir() + "/" + file_name));
        } catch (FileNotFoundException e) {
            Log.d("myLog", "An error occurred : " + e);
        }
        if (scanner != null) {
            while (scanner.hasNextLine()) {
                itemList.add(scanner.nextLine());
            }
        }
        String[] itemsArray = new String[itemList.size()];
        itemsArray = itemList.toArray(itemsArray);
        return itemsArray;
    }

    static public String read(String file_name, Context context){
        FileInputStream fis = null;
        Log.d("myLog", "Read : " + file_name);
        //File file = new File(path_file_save);
        File file = new File(context.getFilesDir() + "/" + file_name);
        //File file = new File(getFilesDir(), file_name);
        if (!file.exists()) {
            Log.d("myLog", "File do not exist");
        }else {
            Log.d("myLog", "file exist");
            //Log.d("myLog", String.valueOf(getFilesDir()));
            try {
                //fis = openFileInput(file_name_save);
                fis = context.openFileInput(file_name);
                InputStreamReader inputStreamReader = new InputStreamReader(fis, StandardCharsets.UTF_8);
                StringBuilder stringBuilder = new StringBuilder();
                try(BufferedReader reader = new BufferedReader(inputStreamReader)) {
                    String line = reader.readLine();
                    while (line != null) {
                        stringBuilder.append(line).append('\n');
                        line = reader.readLine();
                    }
                    Log.d("myLog", String.valueOf(stringBuilder));
                    return String.valueOf(stringBuilder);
                }catch (IOException e){
                    Log.d("myLog", "read : error occurred:" + e.toString());
                }
            } catch (IOException e) {
                Log.d("myLog", "read : error occurred");
                e.printStackTrace();
            } finally {
                if (fis != null) {
                    try {
                        fis.close();
                    } catch (IOException e) {
                        Log.d("myLog", "read : error occurred");
                        e.printStackTrace();
                    }
                }
            }
        }
        return null;
    }
}
