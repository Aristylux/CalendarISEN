package com.example.calendar;

import static com.example.calendar.FilesUtil.readNames;

import android.annotation.SuppressLint;
import android.app.AlertDialog;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.RelativeLayout;
import android.widget.TextView;

import androidx.fragment.app.Fragment;

public class FragmentSettings extends Fragment implements View.OnClickListener {

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_setting_menu, container, false);


        //getActivity().setContentView(R.layout.activity_settings);

        TextView firstname = view.findViewById(R.id.user_firstname);
        TextView lastname = view.findViewById(R.id.user_lastname);

        //in other thread :
        /*
        String Name;
        if((Name = read("stat5.txt", this)) != null){
            //there are something
            //extract the user names
            String[] splitName = Name.replaceAll("\n", "").split("\\.");
            String format_firstname = splitName[0].substring(0, 1).toUpperCase() + splitName[0].substring(1);
            String format_lastname = splitName[1].substring(0, 1).toUpperCase() + splitName[1].substring(1);
            firstname.setText(format_firstname);
            lastname.setText(format_lastname);
        }
        */



        String[] Names = readNames(view.getContext());
        if ((Names.length) != 0){
            String format_firstname = Names[0].substring(0, 1).toUpperCase() + Names[0].substring(1);
            String format_lastname = Names[1].substring(0, 1).toUpperCase() + Names[1].substring(1);
            firstname.setText(format_firstname);
            lastname.setText(format_lastname);
        }



        //getSupportFragmentManager().beginTransaction().replace(R.id.fragment_settings, new FragmentSettings()).commit();


        //RelativeLayout close_button = view.findViewById(R.id.close_button);
        RelativeLayout buttonLanguage = view.findViewById(R.id.select_lang);
        RelativeLayout buttonLightMode = view.findViewById(R.id.select_light_mode);
        RelativeLayout buttonLastName = view.findViewById(R.id.select_lastname);
        RelativeLayout buttonFirstName = view.findViewById(R.id.select_firstname);
        RelativeLayout buttonLike = view.findViewById(R.id.select_like);
        RelativeLayout buttonShare = view.findViewById(R.id.select_share);
        RelativeLayout buttonReport = view.findViewById(R.id.select_report);
        RelativeLayout buttonCopyright = view.findViewById(R.id.select_copyright);
        RelativeLayout buttonReset = view.findViewById(R.id.select_reset);

        //close_button.setOnClickListener(this);
        buttonLanguage.setOnClickListener(this);
        buttonLightMode.setOnClickListener(this);
        buttonLastName.setOnClickListener(this);
        buttonFirstName.setOnClickListener(this);
        buttonLike.setOnClickListener(this);
        buttonShare.setOnClickListener(this);
        buttonReport.setOnClickListener(this);
        buttonCopyright.setOnClickListener(this);
        buttonReset.setOnClickListener(this);

        return view;
    }

    @SuppressLint("NonConstantResourceId")
    @Override
    public void onClick(View view) {
        switch (view.getId()){
           // case R.id.close_button:
                //view.finish();
                //break;
            case R.id.select_lang:
                Log.d("myLogS", "click: select language");
                break;
            case R.id.select_light_mode:
                Log.d("myLogS", "click: dark mode");
                break;
            case R.id.select_lastname:
                Log.d("myLogS", "click: select last name");
                break;
            case R.id.select_firstname:
                Log.d("myLogS", "click: select first name");
                break;
            case R.id.select_like:
                Log.d("myLogS", "click: select like");
                break;
            case R.id.select_share:
                Log.d("myLogS", "click: select share");
                break;
            case R.id.select_report:
                Log.d("myLogS", "click: select report");
                break;
            case R.id.select_copyright:
                Log.d("myLogS", "click: select copyright");
                break;
            case R.id.select_reset:
                Log.d("myLogS", "click: reset");
                //open popup
                OpenPopupWarning();
                break;
        }
    }

    public AlertDialog ShowDialog(View view){
        AlertDialog.Builder dialogBuilder = new AlertDialog.Builder(view.getContext());
        dialogBuilder.setView(view);
        dialogBuilder.setCancelable(false);
        AlertDialog dialog = dialogBuilder.create();
        dialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
        dialog.show();
        return dialog;
    }

    public void OpenPopupWarning(){
        Log.d("myLog", "Popup delete !");
        //AlertDialog.Builder dialogBuilder = new AlertDialog.Builder(this);
        final View menuPopupView = getLayoutInflater().inflate(R.layout.popup_warning, null);

        Button confirm = menuPopupView.findViewById(R.id.confirm_button);
        Button cancel = menuPopupView.findViewById(R.id.cancel_button);
        AlertDialog dialog = ShowDialog(menuPopupView);
        /*
        dialogBuilder.setView(menuPopupView);
        dialogBuilder.setCancelable(false);
        AlertDialog dialog = dialogBuilder.create();
        dialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
        dialog.show();
        Log.d("myLog", "show");
        */

        confirm.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Log.d("myLog", "confirm");
                //delete
                FilesUtil.deleteFile(String.valueOf(getActivity().getFilesDir()),FilesUtil.fileNameSaveData);
                FilesUtil.deleteFile(String.valueOf(getActivity().getFilesDir()),FilesUtil.fileCourseData);
                dialog.dismiss();   //close popup
                OpenPopupConfirmation();
            }
        });

        cancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Log.d("myLog", "cancel");
                //close
                dialog.dismiss();   //close popup
            }
        });
    }

    public void OpenPopupConfirmation(){
        final View menuPopupView = getLayoutInflater().inflate(R.layout.popup_confirmation, null);
        Button ok_button = menuPopupView.findViewById(R.id.ok_button);
        AlertDialog dialog = ShowDialog(menuPopupView);
        ok_button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                dialog.dismiss();   //close popup
                //close app
                getActivity().moveTaskToBack(true);
                android.os.Process.killProcess(android.os.Process.myPid());
                System.exit(1);
            }
        });
    }

}
