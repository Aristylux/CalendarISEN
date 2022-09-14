package com.example.calendar;

import static com.example.calendar.FilesUtil.*;

import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.app.AppCompatDelegate;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;

import android.Manifest;
import android.annotation.SuppressLint;
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
import android.widget.Button;
import android.widget.TextView;

import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.android.material.textfield.TextInputEditText;

@RequiresApi(api = Build.VERSION_CODES.O)
public class MainActivity extends AppCompatActivity {

    //  PERMISSION_STORAGE_CODE = 1000;
    //  PERMISSION_READ_STORAGE_CODE = 100;
    private static final int REQUEST_CODE_FOR_PERMISSION = 1;

    TextView day,month;

    private static final String file_name_save = "stat5.txt";
    private static final String file_name_information = "information";

    //private static final String path_file_save = "/data/user/0/com.example.calendar/files/" + file_name_save;
    //private final String path_file_save = getExternalFilesDir(null) + "/" + file_save;
    //private static final String downloadPath = "/Android/data/com.example.calendar/files/";
    String firstName;
    String lastName;
    String dateFile;

    CountClass count;

    private static final boolean DAILY = true;
    private static final boolean WEEKLY = false;
    boolean MODE = DAILY;

    private static final boolean NEXT = true;
    private static final boolean BEFORE = false;

    TextInputEditText textInputEditTextLastName;
    TextInputEditText textInputEditTextFirstName;
    View validateButton;
    PopupWelcome popupWelcome;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        Log.d("myLog", "Start");
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        // ------ date ----
        day = findViewById(R.id.day);
        month = findViewById(R.id.month);
        DateClass today = new DateClass();
        day.setText(today.getDay());
        month.setText(today.getDateMonth());

        dateFile = today.getSimpleDate();
        Log.d("myLog", "Today : " + dateFile);

        checkPermission();              //Check authorizations
        loadSharedPreferences();        //Load preference (dark mode,..)
        getNameGuest();                 //Get firstName and lastName
        getSchedule();                  //Download schedule

        count = new CountClass(NEXT, BEFORE);
        Log.d("myLog", "end declaration");
        //ContextWrapper contextWrapper = new ContextWrapper(getApplicationContext());
        //Log.d("myLogA", "analyzer path : " + contextWrapper.getDir("file", Context.MODE_PRIVATE));

        //set menu navigation bar
        BottomNavigationView bottomNavigationView = findViewById(R.id.navigation_bar);
        bottomNavigationView.getMenu().getItem(1).setChecked(true);
        bottomNavigationView.setOnNavigationItemSelectedListener(navListener);
        //set first fragment
        Fragment fragment = new FragmentDaily();
        sendData(fragment, dateFile, dateFile, firstName, lastName);    //send data
        //show fragment
        //getSupportFragmentManager().beginTransaction().replace(R.id.fragment_container, fragment).commit();

        //Buttons Next and Before

        Button buttonNext = findViewById(R.id.button_next);
        Button buttonBefore = findViewById(R.id.button_before);

        buttonNext.setOnClickListener(view -> {
            Log.d("myLogN", "button Next clicked");
            changeButton(NEXT);
        });
        buttonBefore.setOnClickListener(view -> {
            Log.d("myLogN", "button Before clicked");
            changeButton(BEFORE);
        });
    }

    /**
     * onRestart:
     * when the user return to the main activity,
     * deselect setting button in navigation bar
     * and select icon Daily or Weekly
     **/
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

    /**
     * changeButton:
     * Action when button Next or Before is clicked.
     */
    private void changeButton(boolean type){
        Fragment selectedFragment;
        String nextDate;
        if (MODE == DAILY) {
            Log.d("myLogN", "button Before mode daily");
            selectedFragment = new FragmentDaily();
            count.countDay(type, 1); //new day
            nextDate = CalculateUtil.calculateDate(dateFile, count.getCountDay());
        } else {
            Log.d("myLogN", "button Before clicked mode weekly");
            selectedFragment = new FragmentWeekly();
            count.countWeek(type, 7);
            nextDate = CalculateUtil.calculateDate(dateFile, count.getCountWeek());
        }
        String verifiedDate = verificationDate(nextDate, type);
        sendData(selectedFragment, verifiedDate, dateFile, firstName, lastName);    //send data
        updateDate(verifiedDate);
    }

    /**
     * verificationDate:
     *
     * for avoid to show weekend
     * 6 -> SATURDAY
     * 7 -> SUNDAY
     */
    private String verificationDate(String date, boolean type){
        String dateVerified = date; //current date
        int nextDayOfTheWeek = CalculateUtil.nbrDayOfTheWeek(date);
        if (nextDayOfTheWeek == 6) {    //+2 -> SATURDAY to MONDAY
            count.countWeek(type, 2, 1);
            count.countDay(type, 2, 1);
            dateVerified = CalculateUtil.calculateDate(dateFile, count.getCountDay());
        } else if (nextDayOfTheWeek == 7) {      //+1 -> SUNDAY to MONDAY
            count.countWeek(type, 1, 2);
            count.countDay(type, 1, 2);
            dateVerified = CalculateUtil.calculateDate(dateFile, count.getCountDay());
        }
        return dateVerified;
    }

    /**
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

    /**
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
        UserSettings settings = (UserSettings) getApplication();
        SharedPreferences sharedPreferences = getSharedPreferences(UserSettings.PREFERENCE, MODE_PRIVATE);
        String theme = sharedPreferences.getString(UserSettings.CUSTOM_THEME, UserSettings.LIGHT_THEME);
        settings.setCustomTheme(theme);
        //set preference (UserSettings settings)
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
                sendData(new FragmentWeekly(), dateFile, dateFile, firstName, lastName);
                //sendData(new FragmentWeeklyTest(), dateFile, dateFile, firstName, lastName);  //bug but optimised
                count.avoidWeekend(dateFile);
                updateDate(dateFile);
                break;
            case R.id.home_daily:
                Log.d("myLogN", "Daily");
                MODE = DAILY;
                stateTypeView.setText(R.string.home_page_title_daily);
                sendData(new FragmentDaily(), dateFile, dateFile, firstName, lastName);
                count.setCountDay(0);
                updateDate(dateFile);
                break;
            case R.id.home_settings:
                Log.d("myLogN", "Settings");
                Intent intent = new Intent(this, Settings.class);   //open new activity
                //Intent intent = new Intent(this, ActivitySettings.class);
                startActivity(intent);
                break;
        }
        return true;
    };

    private void updateDate(String date) {
        DateClass newDate = new DateClass(date);
        day.setText(newDate.getDay());
        month.setText(newDate.getDateMonth());
    }

    /*
     * if directory is not exist
     * we do not need to ask names
     */
    public void getNameGuest(){
        String[] Names = readNames(file_name_save, this);
        if ((Names.length) != 0){
            firstName = Names[0];
            lastName = Names[1];
            Log.d("myLog", "names exist: axel :" + firstName + " | mzd: " + lastName);
        } else {
            Log.d("myLog", "First time");
            PopupWelcome();
        }
    }

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

    void getSchedule(){
        //if(researchFile(getExternalFilesDir(null) + "/" + firstName + "." + lastName, dateResearch)){
        if(researchFile(Environment.getExternalStorageDirectory().toString() + "/Download", dateFile)){
            Log.d("myLog", "File already exist");
            //no download
        }else {
            Log.d("myLog", "File doesn't exist");
            if (firstName != null) {
                Downloader downloader = new Downloader(this, popupWelcome, findViewById(R.id.main), file_name_save, dateFile);
                downloader.Downloading(this, dateFile, findViewById(R.id.main), firstName, lastName, false);
            }
        }

        Downloader downloader = new Downloader(this, popupWelcome, findViewById(R.id.main), file_name_save, dateFile);
        downloader.Downloading(this, dateFile, findViewById(R.id.main), firstName, lastName, false);
    }

}