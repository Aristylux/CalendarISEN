package com.example.calendar;

import static com.example.calendar.FilesUtil.*;

import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.app.AppCompatDelegate;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;
import androidx.viewpager.widget.ViewPager;

import android.Manifest;
import android.annotation.SuppressLint;
import android.content.Context;
import android.content.ContextWrapper;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.os.Build;
import android.os.Bundle;
import android.os.Environment;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.View;
import android.view.ViewParent;
import android.widget.Button;
import android.widget.TextView;

import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.android.material.textfield.TextInputEditText;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.time.DayOfWeek;
import java.time.LocalDate;
import java.time.ZoneId;
import java.util.Calendar;
import java.util.Date;
import java.util.Locale;

@RequiresApi(api = Build.VERSION_CODES.O)
public class MainActivity extends AppCompatActivity {

    //private static final int PERMISSION_STORAGE_CODE = 1000;
    //private static final int PERMISSION_READ_STORAGE_CODE = 100;
    private static final int REQUEST_CODE_FOR_PERMISSION = 1;

    TextView day,month;

    private static final String file_name_save = "stat5.txt";
    private static final String file_name_information = "information";
    @SuppressLint("SdCardPath")
    //private static final String path_file_save = "/data/user/0/com.example.calendar/files/" + file_name_save;
    //private final String path_file_save = getExternalFilesDir(null) + "/" + file_save;
    private static final String downloadPath = "/Android/data/com.example.calendar/files/";
    String firstName;
    String lastName;
    String dateFile;

    int dayOfTheWeek;
    int countDay, countWeek;

    private static final boolean DAILY = true;
    private static final boolean WEEKLY = false;
    boolean MODE = DAILY;

    private static final boolean NEXT = true;
    private static final boolean BEFORE = false;
    boolean TYPE;

    TextInputEditText textInputEditTextLastName;
    TextInputEditText textInputEditTextFirstName;
    View validateButton;
    PopupWelcome popupWelcome;

    private UserSettings settings;
    int lastPosition;

    @RequiresApi(api = Build.VERSION_CODES.O)
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        Log.d("myLog", "Start");
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        // ------ date ----
        day = findViewById(R.id.day);
        month = findViewById(R.id.month);

        Date currentTime = Calendar.getInstance().getTime();
        String formattedDate = DateFormat.getDateInstance(DateFormat.FULL).format(currentTime);

        Log.d("myLog", formattedDate);
        String[] splitDate = formattedDate.split(",");
        day.setText(splitDate[0]);
        month.setText(splitDate[1]);

        /*
        @SuppressLint("SimpleDateFormat") SimpleDateFormat formatter = new SimpleDateFormat("ddMMyyHHmmss");
        date = formatter.format(currentTime);
        Log.d("myLog", "Date Format : " + date);
         */
        //for file
        @SuppressLint("SimpleDateFormat") SimpleDateFormat formattedDateFile = new SimpleDateFormat("yyyyMMdd");
        //String dateFile = "20211214";
        dateFile = formattedDateFile.format(currentTime);
        Log.d("myLog", "Today : " + dateFile);
        /*
        String weekdays[] = new DateFormatSymbols(Locale.FRANCE).getWeekdays();
        Calendar c = Calendar.getInstance();
        Date date = new Date();
        c.setTime(date);
        int dayOfWeek = c.get(Calendar.DAY_OF_WEEK);
        Log.d("myLog", "Today : " + dayOfWeek);
        Log.d("myLog", "Today : " + weekdays[dayOfWeek]);
        */
        //for research file
        @SuppressLint("SimpleDateFormat") SimpleDateFormat formatterF = new SimpleDateFormat("ddMMyy");
        String dateResearch = formatterF.format(currentTime);
        Log.d("myLog", "Date Format : " + dateResearch);

        SimpleDateFormat dateFrDay = new SimpleDateFormat("EEEE", Locale.FRANCE);
        String dateTranslate = dateFrDay.format(currentTime);
        String dateTranslateDayForm = dateTranslate.substring(0,1).toUpperCase() + dateTranslate.substring(1);
        SimpleDateFormat dateFrMonth = new SimpleDateFormat("d MMMM yyyy", Locale.FRANCE);
        String dateTranslateMonth = dateFrMonth.format(currentTime);
        String dateTranslateDayFormMonth = dateTranslateMonth.substring(0,2) + dateTranslateMonth.substring(2,3).toUpperCase() + dateTranslateMonth.substring(3);
        Log.d("myLog", "Date : " + formattedDate + " | " + dateTranslateDayForm + ", " + dateTranslateDayFormMonth);

        /*
        @SuppressLint("SimpleDateFormat") SimpleDateFormat formatterDayOfTheWeek = new SimpleDateFormat("F");
        String dayOfTheWeek = formatterDayOfTheWeek.format(currentTime);
        Log.d("myLog", "Day of the week : " + dayOfTheWeek);    //(1 to 5) (current month)
        */
        /*
        Calendar c = Calendar.getInstance();
        //c.setFirstDayOfWeek(Calendar.SUNDAY); //doesn't work
        dayOfTheWeek = c.get(Calendar.DAY_OF_WEEK)-1;  // -> saturday = 7
        Log.d("myLog", "Day of the week : " + dayOfTheWeek);
        */
        Date date = new Date();
        LocalDate localDate = date.toInstant().atZone(ZoneId.systemDefault()).toLocalDate();
        int month = localDate.getMonthValue();
        DayOfWeek days = localDate.getDayOfWeek();
        //int dayInt = localDate.get(Calendar.days);
        dayOfTheWeek = DayOfWeek.valueOf(String.valueOf(days)).getValue();
        Log.d("myLog", "local date : " + localDate + " " + month + " " + days + " " + dayOfTheWeek);

        //CalculateUtil.convertDate("20220108");

        // ---- check authorizations ----
        checkPermission();

        settings = (UserSettings) getApplication();
        loadSharedPreferences();

        //if directory is not exist
        //we do not need to ask names
        //deleteFile("appInfo");
        //showFile(String.valueOf(getFilesDir()), true);
        //save("test3", "hello.world!");
        /*
        String Name;
        if((Name = read(file_name_save, this)) != null){
            //there are something
            //extract the user names
            String[] splitName = Name.replaceAll("\n", "").split("\\.");
            firstName = splitName[0];
            lastName = splitName[1];
            Log.d("myLog", firstName + " " + lastName);
        }else {
            Log.d("myLog", "First time");
            CreatePopupWelcomeDialog();
        }*/

        //String[] test = readNames(file_name_save, this);
        //Log.d("myLogZ", "test : " + test.length);
        //for (String s : test) Log.d("myLogZ", "test : " + s);
        String[] Names = readNames(file_name_save, this);
        if ((Names.length) != 0){
            firstName = Names[0];
            lastName = Names[1];
            Log.d("myLog", "names exist: axel :" + firstName + " | mzd: " + lastName);
        } else {
            Log.d("myLog", "First time");
            PopupWelcome();
        }
        //CreatePopupWelcomeDialog();
        //showFile(String.valueOf(getFilesDir()), true);
        //showFile(String.valueOf(getExternalFilesDir(null)), true);

        //if(researchFile(getExternalFilesDir(null) + "/" + firstName + "." + lastName, dateResearch)){
        if(researchFile(Environment.getExternalStorageDirectory().toString() + "/Download", dateFile)){
            Log.d("myLog", "File already exist");
            //no download
        }else {
            Log.d("myLog", "File doesn't exist");
            if (firstName != null) {
                //String url = createUrl(firstName, lastName);          //create url
                //startDownloading(url, firstName, lastName, dateFile, false);    //download file
                Downloader downloader = new Downloader(this, popupWelcome, findViewById(R.id.main), file_name_save, dateFile);
                downloader.Downloading(this, dateFile, findViewById(R.id.main), firstName, lastName, false);
            }
        }
        //Log.d("myLog", "open fragment");
        //openFragment(dateFile);
        //Routine.executeRoutineTest(findViewById(R.id.main), this, 20, firstName, lastName, dateFile);

        /*
        lastPosition = 2;
        ViewPager viewPager = findViewById(R.id.view_pager);
        viewPager.setOffscreenPageLimit(1);
        //SwipeAdapter swipeAdapter = new SwipeAdapter(getSupportFragmentManager());
        SwipeAdapter swipeAdapter = new SwipeAdapter(this);
        viewPager.setAdapter(swipeAdapter);
        viewPager.setCurrentItem(lastPosition);
        viewPager.setOnPageChangeListener(new ViewPager.OnPageChangeListener() {
            @Override
            public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {

            }

            @Override
            public void onPageSelected(int position) {
                String nextDate, date;
                if (lastPosition < position) {
                    countDay++;   //next day
                    nextDate = CalculateUtil.calculateDate(dateFile, countDay);
                    date = verificationDate(nextDate, NEXT);

                } else {
                    countDay--;     //new day
                    nextDate = CalculateUtil.calculateDate(dateFile, countDay);
                    date = verificationDate(nextDate, BEFORE);
                }
                lastPosition = position;
                updateDate(date);
            }

            @Override
            public void onPageScrollStateChanged(int state) {


            }
        });

         */


        /*
        //CreatePopupWelcomeDialog();
        if (firstName != null){
            String url = createUrl(firstName, lastName);          //create url
            startDownloading(url, firstName, lastName, date);     //download file

            if(compareFiles(firstName, lastName, date) == 1){
                Log.d("myLog", "Equal !");
                // save name file in stats4.txt
                // delete file
            }else{
                Log.d("myLog", "New data !");
                //extract new file
            }
        }
        */

        Log.d("myLog", "end declaration");
        ContextWrapper contextWrapper = new ContextWrapper(getApplicationContext());
        //Log.d("myLogA", "analyzer path : " + path );
        Log.d("myLogA", "analyzer path : " + contextWrapper.getDir("file", Context.MODE_PRIVATE));

        Log.d("myLog", "End");
        //set menu navigation bar
        BottomNavigationView bottomNavigationView = findViewById(R.id.navigation_bar);
        bottomNavigationView.getMenu().getItem(1).setChecked(true);
        bottomNavigationView.setOnNavigationItemSelectedListener(navListener);
        //set first fragment
        Fragment fragment = new FragmentDaily();
        sendData(fragment, dateFile, dateFile, firstName, lastName);    //send data
        //show fragment
        //getSupportFragmentManager().beginTransaction().replace(R.id.fragment_container, fragment).commit();

        Button buttonNext = findViewById(R.id.button_next);
        Button buttonBefore = findViewById(R.id.button_before);

        buttonNext.setOnClickListener(view -> {
            Log.d("myLogN", "button Next clicked");
            Fragment selectedFragment;
            String nextDate;
            if (MODE == DAILY) {
                Log.d("myLogN", "button Next mode daily");
                selectedFragment = new FragmentDaily();
                countDay++;   //next day
                nextDate = CalculateUtil.calculateDate(dateFile, countDay);
            } else {
                Log.d("myLogN", "button Next mode weekly");
                selectedFragment = new FragmentWeekly();
                Log.d("myLogN", "1 : " + countWeek);
                countWeek += 7;
                Log.d("myLogN", "2 : " + countWeek);
                nextDate = CalculateUtil.calculateDate(dateFile, countWeek);
            }
            String date1 = verificationDate(nextDate, NEXT);
            sendData(selectedFragment, date1, dateFile, firstName, lastName);    //send data
            updateDate(date1);
        });

        buttonBefore.setOnClickListener(view -> {
            Log.d("myLogN", "button Before clicked");
            Fragment selectedFragment;
            String nextDate;
            if (MODE == DAILY) {
                Log.d("myLogN", "button Before mode daily");
                selectedFragment = new FragmentDaily();
                countDay--;     //new day
                nextDate = CalculateUtil.calculateDate(dateFile, countDay);
            } else {
                Log.d("myLogN", "button Before clicked mode weekly");
                selectedFragment = new FragmentWeekly();
                Log.d("myLogN", "1 : " + countWeek);
                countWeek -= 7;
                Log.d("myLogN", "2 : " + countWeek);
                nextDate = CalculateUtil.calculateDate(dateFile, countWeek);
            }
            String date1 = verificationDate(nextDate, BEFORE);
            sendData(selectedFragment, date1, dateFile, firstName, lastName);    //send data
            updateDate(date1);
        });
    }

    /*
     * onRestart:
     * when the user return to the main activity,
     * deselect setting button in navigation bar
     * and select icon Daily or Weekly
     */
    @Override
    protected void onRestart() {
        super.onRestart();
        Log.d("myLog", "Restart");
        BottomNavigationView bottomNavigationView = findViewById(R.id.navigation_bar);
        bottomNavigationView.getMenu().getItem(2).setChecked(false);
        if (MODE == WEEKLY){
            bottomNavigationView.getMenu().getItem(0).setChecked(true);
        } else {
            bottomNavigationView.getMenu().getItem(1).setChecked(true);
        }
    }

    private String verificationDate(String date, boolean type){
        String dateVerified = date; //current date
        int nextDayOfTheWeek = CalculateUtil.nbrDayOfTheWeek(date);
        if (nextDayOfTheWeek == 6) {    //+2 SATURDAY to MONDAY
            if(type == NEXT){
                countDay += 2;
                countWeek += 2;
            } else {
                countDay -= 1;
                countWeek -= 1;
            }
            dateVerified = CalculateUtil.calculateDate(dateFile, countDay);
        } else if (nextDayOfTheWeek == 7) {      //+1 SUNDAY to MONDAY
            if(type == NEXT){
                countDay += 1;
                countWeek += 1;
            } else {
                countDay -= 2;
                countWeek -= 2;
            }
            dateVerified = CalculateUtil.calculateDate(dateFile, countDay);
        }
        return dateVerified;
    }

    /*
     * sendData:
     * send data to a new fragment
     * and open the fragment
     */
    private void sendData(Fragment fragment, String dataDateDay, String dataDateFile, String dataFirstName, String dataLastName ) {
        Bundle data = new Bundle();
        FragmentTransaction fragmentTransactionT = getSupportFragmentManager().beginTransaction();
        data.putString("day", dataDateDay);
        data.putString("dateFile", dataDateFile);
        data.putString("firstname", dataFirstName);
        data.putString("lastname", dataLastName);
        fragment.setArguments(data);
        fragmentTransactionT.replace(R.id.fragment_container, fragment);
        fragmentTransactionT.commit();
    }

    /*
     * checkPermission:
     * check permission user
     * if our application are not the permission
     */
    private void checkPermission() {
        if (ContextCompat.checkSelfPermission(this, Manifest.permission.WRITE_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED){
            //create popup maybe
            ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.WRITE_EXTERNAL_STORAGE}, REQUEST_CODE_FOR_PERMISSION);
        }
    }

    private void loadSharedPreferences() {
        SharedPreferences sharedPreferences = getSharedPreferences(UserSettings.PREFERENCE, MODE_PRIVATE);
        String theme = sharedPreferences.getString(UserSettings.CUSTOM_THEME, UserSettings.LIGHT_THEME);
        settings.setCustomTheme(theme);

        switch (settings.getCustomTheme()){
            case UserSettings.LIGHT_THEME:
                AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_NO);
                break;
            case UserSettings.DARK_THEME:
                AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_YES);
                break;
            case UserSettings.SYSTEM_THEME:
                AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_FOLLOW_SYSTEM);
                break;
        }
    }

    @SuppressLint("NonConstantResourceId")
    private final BottomNavigationView.OnNavigationItemSelectedListener navListener = item -> {
        Log.d("myLogN", "------------ CLICK -----------");
        TextView stateTypeView = findViewById(R.id.page_title);
        switch (item.getItemId()){
            case R.id.home_weekly:
                Log.d("myLogN", "Weekly");
                MODE = WEEKLY;
                stateTypeView.setText(R.string.home_page_title_weekly);
                //sendData(new FragmentWeekly(), dateFile, dateFile, firstName, lastName);
                sendData(new FragmentWeeklyTest(), dateFile, dateFile, firstName, lastName);
                int nbr = CalculateUtil.nbrDayOfTheWeek(dateFile);
                if (nbr == 6)
                    countWeek = 2;
                else if (nbr == 7)
                    countWeek = 1;
                else
                    countWeek = 0;
                updateDate(dateFile);
                break;
            case R.id.home_daily:
                Log.d("myLogN", "Daily");
                MODE = DAILY;
                stateTypeView.setText(R.string.home_page_title_daily);
                sendData(new FragmentDaily(), dateFile, dateFile, firstName, lastName);
                countDay = 0;
                updateDate(dateFile);
                break;
            case R.id.home_settings:
                Log.d("myLogN", "Settings");
                //open new activity
                Intent intent = new Intent(this, Settings.class);
                //Intent intent = new Intent(this, ActivitySettings.class);
                startActivity(intent);
                break;
        }
        return true;
    };

    /*
        private static class SwipeListener implements View.OnTouchListener{
            //initialize variable
            GestureDetector gestureDetector;
            //create constructor
            SwipeListener(View view) {
                //initialize threshold value
                int threshold = 100;
                int velocity_threshold = 100;

                //initialize simple gesture listener
                GestureDetector.SimpleOnGestureListener listener = new GestureDetector.SimpleOnGestureListener() {
                    @Override
                    public boolean onDown(MotionEvent e) {
                        return true;
                    }

                    @Override
                    public boolean onFling(MotionEvent e1, MotionEvent e2, float velocityX, float velocityY) {
                        float xDiff = e2.getX() - e1.getX();
                        float yDiff = e2.getY() - e1.getY();
                        try {
                            //when x is gather than y
                            if (Math.abs(xDiff) > Math.abs(yDiff)) {
                                //when x difference is gather than threshold
                                if (Math.abs(xDiff) > threshold && Math.abs(velocityX) > velocity_threshold) {

                                    if (xDiff > 0) {
                                        Log.d("myLogF", "right");
                                    } else {
                                        Log.d("myLogF", "left");
                                    }
                                    return true;
                                }
                            }
                        }catch (Exception e){
                            Log.d("myLog", "Error : " + e.toString());
                        }
                        return false;
                    }
                };
                //initialize gesture detector
                gestureDetector = new GestureDetector(listener);
                view.setOnTouchListener(this);
            }
            @SuppressLint("ClickableViewAccessibility")
            @Override
            public boolean onTouch(View view, MotionEvent motionEvent) {
                return gestureDetector.onTouchEvent(motionEvent);
            }
        }
    */

    private void updateDate(String date) {
        //Log.d("myLogNav", "new date");
        LocalDate dated = LocalDate.parse(CalculateUtil.convertDate(date));
        String monthForm = String.valueOf(dated.getMonth()).substring(0,1).toUpperCase() + String.valueOf(dated.getMonth()).substring(1).toLowerCase();
        String dayForm = String.valueOf(dated.getDayOfWeek()).substring(0,1).toUpperCase() + String.valueOf(dated.getDayOfWeek()).substring(1).toLowerCase();
        day.setText(dayForm);
        String monthStr = dated.getDayOfMonth() + " " + monthForm + " " + dated.getYear();
        month.setText(monthStr);
    }
    /*
    private boolean researchFile(String path, String research){
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

    private boolean showFile(String path, boolean show){
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

    private int compareFiles(String fName, String lName, String date) {
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
    private void startDownloading(String url, String fName, String lName, String date, boolean firstTime){
        Log.d("myLogD", "url : " + url);
        //create download request
        Uri downloadURI = Uri.parse(url);
        //test : https://speed.hetzner.de/1GB.bin
        //Uri downloadURI = Uri.parse("https://speed.hetzner.de/1GB.bin");

        String title = URLUtil.guessFileName(url,null,null);
        Log.d("myLogD", "title : " + title);

        //get download service
        DownloadManager downloadManager = (DownloadManager)getSystemService(DOWNLOAD_SERVICE);
        try {
            if (downloadManager != null){
                DownloadManager.Request request = new DownloadManager.Request(downloadURI);
                request.setAllowedNetworkTypes(DownloadManager.Request.NETWORK_WIFI | DownloadManager.Request.NETWORK_MOBILE);

                request.setTitle(title);    //set title in download notification
                request.setDescription("Downloading...");   //description
                //request.setNotificationVisibility(DownloadManager.Request.VISIBILITY_VISIBLE_NOTIFY_COMPLETED);
                request.setNotificationVisibility(DownloadManager.Request.VISIBILITY_VISIBLE);
                request.setAllowedOverMetered(true);
                request.setAllowedOverRoaming(true);

                request.allowScanningByMediaScanner();

                //request.setDestinationInExternalPublicDir(downloadPath + fName + "." + lName, fName + "." + lName + "_" + date + ".ics");
                //request.setDestinationInExternalPublicDir(getExternalFilesDir(null) + "/" + fName + "." + lName, fName + "." + lName + "_" + date + ".ics");
                Log.d("myLogD", String.valueOf(getFilesDir()));
                Log.d("myLogD", Environment.DIRECTORY_DOWNLOADS);
                //showFile(Environment.DIRECTORY_DOWNLOADS, true);
                //showFile(Environment.getExternalStorageDirectory().toString() + "/Download", true);
                //show file
                //Log.d("myLog", String.valueOf(Environment.getExternalStorageDirectory()));
                //Log.d("myLog", String.valueOf(Environment.getDataDirectory()));
                //Log.d("myLog", String.valueOf(Environment.getExternalStoragePublicDirectory("txt")));
                //Log.d("myLog", String.valueOf(Environment.getDownloadCacheDirectory()));
                if (firstTime){
                    registerReceiver(onTest, new IntentFilter(DownloadManager.ACTION_DOWNLOAD_COMPLETE));
                } else {
                    registerReceiver(onComplete, new IntentFilter(DownloadManager.ACTION_DOWNLOAD_COMPLETE));
                }

                //success in real phone
                request.setDestinationInExternalPublicDir(Environment.DIRECTORY_DOWNLOADS, fName + "." + lName + "_" + date + ".ics");
                //request.setDestinationInExternalPublicDir("/test_Temp", fName + "." + lName + "_" + date + ".ics");
                //request.setDestinationInExternalPublicDir(Environment.getExternalStorageDirectory() + "/" + fName + "." + lName, fName + "." + lName + "_" + date + ".ics");

                //request.setDestinationInExternalPublicDir(getExternalFilesDir(null).toString() + fName + "." + lName, fName + "." + lName + "_" + date + ".ics");

                downloadId = downloadManager.enqueue(request);
                Log.d("myLogD", "Download, " + Environment.DIRECTORY_DOWNLOADS + " | " + downloadId);
            }else{
                Intent intent = new Intent(Intent.ACTION_VIEW, downloadURI);
                startActivity(intent);
            }
        }catch (Exception e){
            Log.d("myLogD", "Error: " + e.toString());
        }
    }
    */


    /*
    public void runThreadVerification(){
        Runnable runnable = new Runnable() {
            @Override
            public void run() {
                int count = 0;
                String path = Environment.getExternalStorageDirectory().toString() + "/Download";
                String file_name = firstName + "." + lastName + "_" + dateFile + ".ics";
                String pathICS = path + "/" + file_name;

                Log.d("myLogTr", "Diff Thread : " + Thread.currentThread().getName());
                //check 10s -> time out;
                for (int i = 0; i < 20; i++) {
                    try {
                        Thread.sleep(500);
                        count++;
                        File file = new File(pathICS);
                        if (file.canRead()) break;
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                }
                Log.d("myLogTr", "End thread");


                if (count < 20){
                    //success
                    String[] names = {firstName, lastName};
                    if(saveNames(file_name_save,names, getApplicationContext())) {
                        Log.d("myLog", "dismiss");
                        //dialog.dismiss();   //close popup
                        popupWelcome.closePopup();
                        executeRoutine(count);
                    } else {
                        Log.d("myLog", "error sys");
                        //contact moderator
                    }
                } else {
                    //error
                    Log.d("myLog", "Error");
                    //popupWelcome.errorPopup();
                    //showPopupError();
                    //---
                    final View menuPopupView = getLayoutInflater().inflate(R.layout.popup_welcome, null);
                    textInputLayoutLastName = menuPopupView.findViewById(R.id.textInputLayoutUserLastName);
                    textInputEditTextLastName = menuPopupView.findViewById(R.id.textInputEditTextUserLastName);
                    textInputLayoutFirstName = menuPopupView.findViewById(R.id.textInputLayoutUserFirstName);
                    textInputEditTextFirstName = menuPopupView.findViewById(R.id.textInputEditTextUserFirstName);
                    validate = menuPopupView.findViewById(R.id.validate);

                    textInputLayoutLastName.setError(getText(R.string.message_lastname_incorrect));
                    textInputLayoutFirstName.setError(getText(R.string.message_firstname_incorrect));
                    textInputEditTextLastName.setTextColor(ColorStateList.valueOf(getColor(R.color.red)));
                    textInputEditTextFirstName.setTextColor(ColorStateList.valueOf(getColor(R.color.red)));
                    textInputLayoutLastName.setErrorIconDrawable(R.drawable.ic_baseline_error_24);
                    textInputLayoutLastName.setErrorIconTintList(ColorStateList.valueOf(getColor(R.color.red)));
                    textInputLayoutFirstName.setErrorIconDrawable(R.drawable.ic_baseline_error_24);
                    textInputLayoutFirstName.setErrorIconTintList(ColorStateList.valueOf(getColor(R.color.red)));
                    validate.setVisibility(View.VISIBLE);
                    //---
                }
                Log.d("myLogTr", "End of run()");   //terminated
            }
        };
        Thread thread = new Thread(runnable);
        thread.start();
        //showPopupError();
        //CreatePopupWelcomeDialog();
    }
     */

    /*
        BroadcastReceiver onComplete = new BroadcastReceiver() {
        public void onReceive(Context context, Intent intent) {
            // your code
            Log.d("myLogD", "Download finish on complete");

            Log.d("myLogTr", "Main Thread : " + Thread.currentThread().getName());
            Runnable runnable = new Runnable() {
                @Override
                public void run() {
                    int count = 0;
                    String path = Environment.getExternalStorageDirectory().toString() + "/Download";
                    String file_name = firstName + "." + lastName + "_" + dateFile + ".ics";
                    String pathICS = path + "/" + file_name;

                    Log.d("myLogTr", "Diff Thread : " + Thread.currentThread().getName());

                    //---
                    View snackMessage = getLayoutInflater().inflate(R.layout.snackbar_check, null);
                    Snackbar snackbar = Snackbar.make(findViewById(R.id.cl), "Updated.", Snackbar.LENGTH_INDEFINITE);
                    snackbar.getView().setBackgroundColor(Color.TRANSPARENT);
                    snackbar.setAnimationMode(BaseTransientBottomBar.ANIMATION_MODE_SLIDE);
                    Snackbar.SnackbarLayout snackbarLayout = (Snackbar.SnackbarLayout) snackbar.getView();
                    snackbarLayout.setPadding(0,0,0,0);
                    snackbarLayout.addView(snackMessage,0);
                    snackbar.show();
                    //---
                    //check 10s -> time out;
                    for (int i = 0; i < 20; i++) {
                        try {
                            Thread.sleep(500);
                            count++;
                            File file = new File(pathICS);
                            if (file.canRead()) break;
                        } catch (InterruptedException e) {
                            e.printStackTrace();
                        }
                    }
                    //snackbar.dismiss();

                    //executeRoutine(count);
                    //---
                    View snackMessage;
                    if (count < 20){
                        //success
                        View custom = getLayoutInflater().inflate(R.layout.snackbar_custom, null);
                        Routine.analyzer(findViewById(R.id.main), custom, firstName, lastName, dateFile);
                        snackMessage = getLayoutInflater().inflate(R.layout.snackbar_success, null);
                    } else {
                        //error
                        snackMessage = getLayoutInflater().inflate(R.layout.snackbar_failure, null);
                    }

                    Snackbar snackbar = Snackbar.make(findViewById(R.id.cl), "Updated.", Snackbar.LENGTH_SHORT);
                    snackbar.getView().setBackgroundColor(Color.TRANSPARENT);
                    snackbar.setAnimationMode(BaseTransientBottomBar.ANIMATION_MODE_SLIDE);
                    Snackbar.SnackbarLayout snackbarLayout = (Snackbar.SnackbarLayout) snackbar.getView();
                    snackbarLayout.setPadding(0,0,0,0);
                    snackbarLayout.addView(snackMessage,0);
                    snackbar.show();


                    //---
                    Log.d("myLogTr", "End of run()");   //terminated
                }
            };
            Thread thread = new Thread(runnable);
            thread.start();
        }
    };


     */


    /*
     * PopupWelcome:
     * show the popup welcome when the first time
     */
    public void PopupWelcome(){
        @SuppressLint("InflateParams") final View menuPopupView = getLayoutInflater().inflate(R.layout.popup_welcome, null);
        popupWelcome = new PopupWelcome(MainActivity.this, menuPopupView);
        popupWelcome.showPopup(this, menuPopupView);

        //validate = menuPopupView.findViewById(R.id.validate);

        validateButton = menuPopupView.findViewById(R.id.button_save);

        textInputEditTextLastName = menuPopupView.findViewById(R.id.textInputEditTextUserLastName);
        textInputEditTextFirstName = menuPopupView.findViewById(R.id.textInputEditTextUserFirstName);

        Downloader downloader = new Downloader(this, popupWelcome, findViewById(R.id.main), file_name_save, dateFile);

        validateButton.setOnClickListener(view -> {
            String[] names = popupWelcome.buttonActivated();
            Log.d("myLog", "names : " + names[0] + " - " + names[1]);
            downloader.Downloading(this, dateFile, findViewById(R.id.main), names[1], names[0], true);

            textInputEditTextLastName.addTextChangedListener(new TextWatcher() {
                @Override
                public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) { }

                @Override
                public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
                    popupWelcome.textLastNameChanged();
                }

                @Override
                public void afterTextChanged(Editable editable) { }
            });

            textInputEditTextFirstName.addTextChangedListener(new TextWatcher() {
                @Override
                public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) { }

                @Override
                public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
                    popupWelcome.textFirstNameChanged();
                }

                @Override
                public void afterTextChanged(Editable editable) { }
            });
        });
    }

    /*
    //handle permission result
    //@Override
    public void onRequestPermissionResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults){
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        if (requestCode == PERMISSION_STORAGE_CODE) {
            //if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_DENIED) {
            if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
            //permission granted from popup, perform download
                CreatePopupWelcomeDialog();
            }
            else {
                //permission denied from popup, show error message
                Toast.makeText(this, "Permission denied...", Toast.LENGTH_SHORT).show();
                ActivityCompat.requestPermissions(this,
                        new String[]{Manifest.permission.WRITE_EXTERNAL_STORAGE}, REQUEST_CODE_FOR_PERMISSION);
            }
        }
    }
    */
}