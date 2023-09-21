package com.example.calendar;

import static com.example.calendar.Downloader.getSize;
import static com.example.calendar.FilesUtil.*;

import androidx.annotation.NonNull;
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
import android.os.Handler;
import android.os.Message;
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
    FilesUtil filesUtil;
    CountClass count;

    private static final boolean DAILY = true;
    private static final boolean WEEKLY = false;
    boolean MODE = DAILY;

    private static final boolean NEXT = true;
    private static final boolean BEFORE = false;

    BottomNavigationView bottomNavigationView;
    TextInputEditText textInputEditTextLastName;
    TextInputEditText textInputEditTextFirstName;
    View validateButton;
    PopupWelcome popupWelcome;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        Log.d("myLog", "Start");
        super.onCreate(savedInstanceState);     //crash when restart ()
        setContentView(R.layout.activity_main);
        // ------ date ----
        day = findViewById(R.id.day);
        month = findViewById(R.id.month);
        DateClass today = new DateClass();
        day.setText(today.getDay());
        month.setText(today.getDateMonth());

        //dateFile = today.getSimpleDate();
        Log.d("myLog", "Today : " + today.getSimpleDate());

        checkPermission();              //Check authorizations
        loadSharedPreferences();        //Load preference (dark mode,..)
        filesUtil = new FilesUtil(today.getSimpleDate(), this);

        //set menu navigation bar
        bottomNavigationView = findViewById(R.id.navigation_bar);
        setBottomNavigation();
        bottomNavigationView.setOnNavigationItemSelectedListener(navListener);

        if(getNameGuest()) {                 //Get firstName and lastName
            //get network (just inform)
            getSchedule();                  //Download schedule
            //set first fragment
            FragmentDaily fragmentDaily = FragmentDaily.newInstance(filesUtil);
            setFragment(fragmentDaily);
            //sendData(new FragmentDaily(filesUtil));    //send data
        }
        count = new CountClass(NEXT, BEFORE);

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
        setBottomNavigation();
    }

    private void setBottomNavigation(){
        final int WEEKLY_POS = 0;
        final int DAILY_POS = 1;
        final int SETTING_POS = 2;
        bottomNavigationView.getMenu().getItem(SETTING_POS).setChecked(false);
        if (MODE == WEEKLY)
            bottomNavigationView.getMenu().getItem(WEEKLY_POS).setChecked(true);
        else
            bottomNavigationView.getMenu().getItem(DAILY_POS).setChecked(true);
    }

    /**
     * changeButton:
     * Action when button Next or Before is clicked.
     */
    private void changeButton(boolean type){
        Fragment selectedFragment;
        String nextDate, verifiedDate;
        if (MODE == DAILY) {
            Log.d("myLogN", "button Before mode daily");
            count.countDay(type, 1); //new day
            nextDate = CalculateUtil.calculateDate(filesUtil.getDateFile(), count.getCountDay());
            verifiedDate = verificationDate(nextDate, type);
            selectedFragment = FragmentDaily.newInstance(filesUtil, verifiedDate);
        } else {
            Log.d("myLogN", "button Before clicked mode weekly");
            count.countWeek(type, 7);
            nextDate = CalculateUtil.calculateDate(filesUtil.getDateFile(), count.getCountWeek());
            verifiedDate = verificationDate(nextDate, type);
            selectedFragment = new FragmentWeekly(filesUtil, verifiedDate);
        }
        setFragment(selectedFragment);    //send data
        updateDate(verifiedDate);
    }

    Handler handler = new Handler(new Handler.Callback() {
        @Override
        public boolean handleMessage(@NonNull Message message) {
            switch (message.what){
                case 1:
                    FragmentDaily fragmentDaily = FragmentDaily.newInstance(filesUtil);
                    setFragment(fragmentDaily);
                    break;
            }
            return true;
        }
    });

    /**
     * verificationDate:
     * <p>
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
            dateVerified = CalculateUtil.calculateDate(filesUtil.getDateFile(), count.getCountDay());
        } else if (nextDayOfTheWeek == 7) {      //+1 -> SUNDAY to MONDAY
            count.countWeek(type, 1, 2);
            count.countDay(type, 1, 2);
            dateVerified = CalculateUtil.calculateDate(filesUtil.getDateFile(), count.getCountDay());
        }
        return dateVerified;
    }

    /**
     * sendData:
     * send data to a new fragment
     * and open the fragment
     */
    private void setFragment(Fragment fragment) {
        FragmentTransaction fragmentTransaction = getSupportFragmentManager().beginTransaction();
        fragmentTransaction.replace(R.id.fragment_container, fragment);
        fragmentTransaction.commit();
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
                AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_NO);
                //AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_YES);
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
                setFragment(new FragmentWeekly(filesUtil));
                //sendData(new FragmentWeeklyTest(), dateFile, dateFile, firstName, lastName);  //bug but optimised
                count.avoidWeekend(filesUtil.getDateFile());
                updateDate(filesUtil.getDateFile());
                break;
            case R.id.home_daily:
                Log.d("myLogN", "Daily");
                MODE = DAILY;
                stateTypeView.setText(R.string.home_page_title_daily);
                FragmentDaily fragmentDaily = FragmentDaily.newInstance(filesUtil);
                setFragment(fragmentDaily);
                count.setCountDay(0);
                updateDate(filesUtil.getDateFile());
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

    /**
     * updateDate:
     * update banner date
     */
    private void updateDate(String date) {
        DateClass newDate = new DateClass(date);
        day.setText(newDate.getDay());
        month.setText(newDate.getDateMonth());
    }

    /**
     * if directory is not exist
     * we do not need to ask names
     */
    public boolean getNameGuest(){
        String[] Names = readNames(this);
        if ((Names.length) != 0){
            filesUtil.setFirstName(Names[0]);
            filesUtil.setLastName(Names[1]);
            Log.d("myLog", "Names exist: " + filesUtil.getFirstName() + " | " + filesUtil.getLastName());
            return true;
        } else {
            Log.d("myLog", "First time");
            PopupWelcome();
            return false;
        }
    }

    /**
     * PopupWelcome:
     * show the popup welcome when the first time
     */
    public void PopupWelcome(){
        @SuppressLint("InflateParams") final View menuPopupView = getLayoutInflater().inflate(R.layout.popup_welcome, null);
        popupWelcome = new PopupWelcome(MainActivity.this, menuPopupView);
        popupWelcome.showPopup(this, menuPopupView);

        validateButton = menuPopupView.findViewById(R.id.button_save);

        textInputEditTextLastName = menuPopupView.findViewById(R.id.textInputEditTextUserLastName);
        textInputEditTextFirstName = menuPopupView.findViewById(R.id.textInputEditTextUserFirstName);

        Downloader downloader = new Downloader(this, popupWelcome, findViewById(R.id.main), filesUtil, handler);

        validateButton.setOnClickListener(view -> {
            String[] names = popupWelcome.buttonActivated();
            Log.d("myLog", "names : " + names[0] + " - " + names[1]);
            filesUtil.setFirstName(names[1]);
            filesUtil.setLastName(names[0]);
            downloader.Downloading(true);

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

    /**
     * getSchedule:
     * download schedule
     */
    void getSchedule(){
        if(researchFile(getPathDownload(), filesUtil.getDateFile())){
            Log.d("myLog", "File already exist");
            //no download
            /* ---> get if the length is the same or not */
        }else {
            Log.d("myLog", "File doesn't exist");
            if (filesUtil.getFirstName() != null) {
                //Downloader downloader = new Downloader(this, popupWelcome, findViewById(R.id.main), file_name_save, dateFile, firstName, lastName);
                //downloader.Downloading(this, false);
            }
        }
        getSize(filesUtil);
        Downloader downloader = new Downloader(this, popupWelcome, findViewById(R.id.main), filesUtil, handler);
        downloader.Downloading(false);
    }
}