package com.example.calendar;

import android.annotation.SuppressLint;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.RadioButton;
import android.widget.RelativeLayout;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.app.AppCompatDelegate;

/**
 * ActivityDarkMode:
 * used in : Settings
 *
 * Menu when you click on Setting -> Dark Mode
 * change screen :
 * -> Light
 * -> Dark
 * -> System
 */

public class ActivityDarkMode extends AppCompatActivity implements View.OnClickListener {

    UserSettings settings;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.fragment_theme);

        settings = (UserSettings) getApplication();

        RelativeLayout close_button = findViewById(R.id.close_button);

        RadioButton radioButtonLight = findViewById(R.id.radioButtonLight);
        RadioButton radioButtonDark = findViewById(R.id.radioButtonDark);
        RadioButton radioButtonSystem = findViewById(R.id.radioButtonSystem);

        loadPreferences(radioButtonLight, radioButtonDark, radioButtonSystem);

        close_button.setOnClickListener(this);
        radioButtonLight.setOnClickListener(this);
        radioButtonDark.setOnClickListener(this);
        radioButtonSystem.setOnClickListener(this);
    }

    private void loadPreferences(RadioButton radioButtonLight, RadioButton radioButtonDark, RadioButton radioButtonSystem) {
        SharedPreferences sharedPreferences = getSharedPreferences(UserSettings.PREFERENCE, MODE_PRIVATE);
        String theme = sharedPreferences.getString(UserSettings.CUSTOM_THEME, UserSettings.LIGHT_THEME);
        settings.setCustomTheme(theme);

        switch (settings.getCustomTheme()){
            case UserSettings.LIGHT_THEME:
                radioButtonLight.setChecked(true);
                break;
            case UserSettings.DARK_THEME:
                radioButtonDark.setChecked(true);
                break;
            case UserSettings.SYSTEM_THEME:
                radioButtonSystem.setChecked(true);
                break;
        }
    }

    @SuppressLint("NonConstantResourceId")
    @Override
    public void onClick(View view) {
        switch (view.getId()){
            case R.id.close_button:
                finish();
                break;
            case R.id.radioButtonLight:
                Log.d("myLogS", "Light mode");
                settings.setCustomTheme(UserSettings.LIGHT_THEME);
                AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_NO);
                break;
            case R.id.radioButtonDark:
                Log.d("myLogS", "Dark mode");
                settings.setCustomTheme(UserSettings.DARK_THEME);
                AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_YES);
                break;
            case R.id.radioButtonSystem:
                Log.d("myLogS", "System mode");
                settings.setCustomTheme(UserSettings.SYSTEM_THEME);
                AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_FOLLOW_SYSTEM);
                break;
        }
        SharedPreferences.Editor editor = getSharedPreferences(UserSettings.PREFERENCE, MODE_PRIVATE).edit();
        editor.putString(UserSettings.CUSTOM_THEME, settings.getCustomTheme());
        editor.apply();
    }
}
