package com.example.calendar;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.widget.RelativeLayout;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.AppCompatButton;

public class ActivitySetCopyright extends AppCompatActivity {

    private static final String UrlGITHUB = "https://github.com/Aristylux/CalendarISEN";
    private static final String UrlLICENCE = "http://creativecommons.org/licenses/by-nc-sa/2.0/fr/";

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_copyright);

        RelativeLayout close_button = findViewById(R.id.close_button);
        AppCompatButton buttonLicence = findViewById(R.id.licence_button);
        AppCompatButton buttonSourceCode = findViewById(R.id.source_code_button);

        close_button.setOnClickListener(view -> finish());
        buttonLicence.setOnClickListener(view -> LoadUrl(UrlLICENCE));
        buttonSourceCode.setOnClickListener(view -> LoadUrl(UrlGITHUB));
    }

    private void LoadUrl(String Url){
        Intent browserIntent = new Intent(Intent.ACTION_VIEW, Uri.parse(Url));
        startActivity(browserIntent);
    }
}
