package com.example.calendar;


import static android.content.Context.DOWNLOAD_SERVICE;

import static com.example.calendar.FilesUtil.saveNames;

import android.app.DownloadManager;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.net.Uri;
import android.os.Build;
import android.os.Environment;
import android.os.Handler;
import android.util.Log;
import android.view.View;
import android.webkit.URLUtil;

import androidx.annotation.RequiresApi;

import java.io.File;

public class Downloader {

    Context context;
    PopupWelcome popupWelcome;
    String path, FirstName, LastName, fileNameSave, dateFile;
    View theView;

    Downloader(Context Context, PopupWelcome PopupWelcome, View view, String FileNameSave, String DateFile) {
        //menuPopupView = context.getLayoutInflater().inflate(R.layout.popup_welcome, null);
        //popupWelcome = new PopupWelcome(context, view);
        context = Context;
        popupWelcome = PopupWelcome;
        fileNameSave = FileNameSave;
        theView = view;
        dateFile = DateFile;
    }

    void Downloading(Context context, String date, View view, String firstName, String lastName, boolean firstTime) {
        LastName = lastName;
        FirstName = firstName;
        Log.d("myLogD", "last : " + lastName + " first " + firstName);
        String pathLo = Environment.getExternalStorageDirectory().toString() + "/Download";
        String file_name = firstName + "." + lastName + "_" + dateFile + ".ics";
        String pathICS = pathLo + "/" + file_name;
        path = pathICS;
        String url = createUrl(firstName, lastName);
        Log.d("myLogD", "url : " + url);
        Uri downloadURI = Uri.parse(url);
        String title = URLUtil.guessFileName(url, null, null);    //create title
        DownloadManager downloadManager = (DownloadManager) context.getSystemService(DOWNLOAD_SERVICE);  //get download service
        try {
            if (downloadManager != null) {
                DownloadManager.Request request = new DownloadManager.Request(downloadURI);
                request.setAllowedNetworkTypes(DownloadManager.Request.NETWORK_WIFI | DownloadManager.Request.NETWORK_MOBILE);

                request.setTitle(title);    //set title in download notification
                request.setDescription("Downloading...");   //description
                request.setNotificationVisibility(DownloadManager.Request.VISIBILITY_VISIBLE);
                request.setAllowedOverMetered(true);
                request.setAllowedOverRoaming(true);

                request.allowScanningByMediaScanner();
                //catching download complete event from android download manager which broadcast message
                if (firstTime) {
                    context.registerReceiver(onCompleteFirst, new IntentFilter(DownloadManager.ACTION_DOWNLOAD_COMPLETE));
                } else {
                    context.registerReceiver(onComplete, new IntentFilter(DownloadManager.ACTION_DOWNLOAD_COMPLETE));
                }

                request.setDestinationInExternalPublicDir(Environment.DIRECTORY_DOWNLOADS, firstName + "." + lastName + "_" + dateFile + ".ics");
                Log.d("myLogD", "download starting...");
                downloadManager.enqueue(request); //start download
                Log.d("myLogD", "wait...");
            } else {
                Intent intent = new Intent(Intent.ACTION_VIEW, downloadURI);
                context.startActivity(intent);
            }
        } catch (Exception e) {
            Log.d("myLog", "Error: " + e.toString());
        }
    }

    BroadcastReceiver onCompleteFirst = new BroadcastReceiver() {
        @RequiresApi(api = Build.VERSION_CODES.O)
        @Override
        public void onReceive(Context context, Intent intent) {
            Log.d("myLogD", "Download finish on testT");
            //success = true;
            runThreadTest();
        }
    };

    BroadcastReceiver onComplete = new BroadcastReceiver() {
        @RequiresApi(api = Build.VERSION_CODES.O)
        public void onReceive(Context context, Intent intent) {
            Log.d("myLogD", "Download finish on complete");
            File file = new File(path);
            Log.d("myLogTr", "file: " + file.canRead() + " | " + file.exists());
            if (file.canRead()) {
                //success
                Routine.executeRoutineTest(theView.findViewById(R.id.main), context, 19, FirstName, LastName, dateFile);
            } else {
                //error
                Log.d("myLog", "Error");
                Routine.executeRoutineTest(theView.findViewById(R.id.main), context, 20, FirstName, LastName, dateFile);
            }
        }
    };

    private void runThreadTest() {
        //Runnable runnable = new Runnable() {
        new Handler().postDelayed(new Runnable() {
            @RequiresApi(api = Build.VERSION_CODES.O)
            @Override
            public void run() {
                Log.d("myLogTr", "Diff Thread : " + Thread.currentThread().getName());
                Log.d("myLogTr", "Path: " + path);
                //check 10s -> time out;
                File file = new File(path);
                Log.d("myLogTr", "file: " + file.canRead() + " | " + file.exists());
                //if (file.canRead()) break;
                Log.d("myLogTr", "End thread");
                if (file.canRead()) {
                    //success
                    String[] names = {FirstName, LastName};
                    if (saveNames(fileNameSave, names, context)) {
                        Log.d("myLog", "dismiss");
                        //dialog.dismiss();   //close popup
                        popupWelcome.closePopup();
                        //executeRoutine(20);
                        Routine.executeRoutineTest(theView.findViewById(R.id.main), context, 19, FirstName, LastName, dateFile);
                    } else {
                        Log.d("myLog", "error sys");
                        //contact moderator
                    }
                } else {
                    //error
                    Log.d("myLog", "Error");
                    popupWelcome.errorPopup(context);
                }
                Log.d("myLogTr", "End of run()");   //terminated
            }
        }, 100);
        //};
        //Thread thread = new Thread(runnable);
        //thread.start();
    }

    /*
    private void runThreadTest() {
        Runnable runnable = new Runnable() {
            @Override
            public void run() {
                int count = 0;
                Log.d("myLogTr", "Diff Thread : " + Thread.currentThread().getName());
                Log.d("myLogTr", "Path: " + path);
                //check 10s -> time out;
                for (int i = 0; i < 20; i++) {
                    try {
                        Thread.sleep(500);
                        count++;
                        File file = new File(path);
                        Log.d("myLogTr", "file: " + file.canRead() + " | " + file.exists());
                        if (file.canRead()) break;
                    } catch (InterruptedException e) {
                        Log.d("myLog", "An error occurred:" + e.toString());
                    }
                }
                Log.d("myLogTr", "End thread");
                if (count < 20){
                    //success
                    String[] names = {FirstName, LastName};
                    if(saveNames(fileNameSave,names, context)) {
                        Log.d("myLog", "dismiss");
                        //dialog.dismiss();   //close popup
                        popupWelcome.closePopup();
                        //MainActivity.executeRoutine(count);
                    } else {
                        Log.d("myLog", "error sys");
                        //contact moderator
                    }
                } else {
                    //error
                    Log.d("myLog", "Error");
                    popupWelcome.errorPopup();
                }
                Log.d("myLogTr", "End of run()");   //terminated
            }
        };
        Thread thread = new Thread(runnable);
        thread.start();
    }
    */

    /*
     * createUrl:
     * create url :
     * https://ent-toulon.isen.fr/webaurion/ICS/firstName.lastName.ics
     * like :
     * https://ent-toulon.isen.fr/webaurion/ICS/axel.mezade.ics
     */
    private String createUrl(String firstName, String lastName) {
        return "https://ent-toulon.isen.fr/webaurion/ICS/" + firstName + "." + lastName + ".ics";
    }

}
