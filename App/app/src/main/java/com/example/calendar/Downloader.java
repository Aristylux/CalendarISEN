package com.example.calendar;

import static android.content.Context.DOWNLOAD_SERVICE;

import static com.example.calendar.FilesUtil.getDirISENCalendar;
import static com.example.calendar.FilesUtil.getFileNameSaveData;
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
import java.io.IOException;
import java.net.URL;
import java.net.URLConnection;

public class Downloader {

    Context context;
    PopupWelcome popupWelcome;
    String dateFile, url;
    View theView;
    FilesUtil filesUtil;
    Handler handler;

    Downloader(Context Context, PopupWelcome PopupWelcome, View view, FilesUtil filesUtil, Handler handler) {
        context = Context;
        popupWelcome = PopupWelcome;
        theView = view;
        this.dateFile = filesUtil.getDateFile();
        this.filesUtil = filesUtil;
        this.handler = handler;
    }

    void Downloading(boolean firstTime) {
        url = createUrl(filesUtil.getFirstName(), filesUtil.getLastName());
        Log.d("myLogD", "url : " + url);
        Uri downloadURI = Uri.parse(url);
        String title = URLUtil.guessFileName(url, null, null);    //create title
        DownloadManager downloadManager = (DownloadManager) context.getSystemService(DOWNLOAD_SERVICE);  //get download service
        try {
            if (downloadManager != null) {
                DownloadManager.Request request = new DownloadManager.Request(downloadURI);
                request.setAllowedNetworkTypes(DownloadManager.Request.NETWORK_WIFI | DownloadManager.Request.NETWORK_MOBILE);
                request.setTitle(title);    //set title in download notification
                request.setDescription("Downloading..");   //description
                request.setNotificationVisibility(DownloadManager.Request.VISIBILITY_VISIBLE);
                request.setAllowedOverMetered(true);
                request.setAllowedOverRoaming(true);
                request.allowScanningByMediaScanner();

                //catching download complete event from android download manager which broadcast message
                if (firstTime) {
                    File dir = new File(FilesUtil.getPathDownload());   //"/Download/ISENCalendars/"
                    dir.mkdirs(); // creates needed dir
                    context.registerReceiver(onCompleteFirst, new IntentFilter(DownloadManager.ACTION_DOWNLOAD_COMPLETE));
                } else {
                    context.registerReceiver(onComplete, new IntentFilter(DownloadManager.ACTION_DOWNLOAD_COMPLETE));
                }
                request.setDestinationInExternalPublicDir(Environment.DIRECTORY_DOWNLOADS, "/" + getDirISENCalendar() + filesUtil.getFileName());
                //-> File file = new File(path);
                //request.setDestinationUri(Uri.fromFile(file));    //Error: java.lang.SecurityException: Unsupported path

                Log.d("myLogD", "Download starting...");
                downloadManager.enqueue(request); //start download
                Log.d("myLogD", "Wait...");
            } else {
                Intent intent = new Intent(Intent.ACTION_VIEW, downloadURI);
                context.startActivity(intent);
            }
        } catch (Exception e) {
            Log.d("myLog", "Error: " + e);
        }
    }

    BroadcastReceiver onCompleteFirst = new BroadcastReceiver() {
        @RequiresApi(api = Build.VERSION_CODES.O)
        @Override
        public void onReceive(Context context, Intent intent) {
            Log.d("myLogD", "Download finish on testT");
            runThread();
        }
    };

    BroadcastReceiver onComplete = new BroadcastReceiver() {
        @RequiresApi(api = Build.VERSION_CODES.O)
        public void onReceive(Context context, Intent intent) {
            Log.d("myLogD", "Download finish on complete");
            File file = new File(filesUtil.getPathDownloadedFile());
            Log.d("myLogTr", "file: " + file.canRead() + " | " + file.exists());
            if (file.canRead()) {   //success
                Routine.executeRoutineTest(theView.findViewById(R.id.main), context, true, filesUtil, handler);
            } else {                //error
                Log.d("myLogTr", "Error");
                Routine.executeRoutineTest(theView.findViewById(R.id.main), context, false, filesUtil, handler);
            }
        }
    };

    @RequiresApi(api = Build.VERSION_CODES.O)
    private void runThread() {
        new Handler().postDelayed(() -> {
            Log.d("myLogTr", "Diff Thread : " + Thread.currentThread().getName());
            Log.d("myLogTr", "Path: " + filesUtil.getPathDownloadedFile());
            File file = new File(filesUtil.getPathDownloadedFile());
            Log.d("myLogTr", "file: " + file.canRead() + " | " + file.exists());
            if (file.canRead()) {   //success
                String[] names = {filesUtil.getFirstName(), filesUtil.getLastName()};
                if (saveNames(getFileNameSaveData(), names, context)) {
                    popupWelcome.closePopup();
                    Routine.executeRoutineTest(theView.findViewById(R.id.main), context, true, filesUtil, handler);
                } else {
                    Log.d("myLogTr", "error sys");
                    //contact moderator
                }
            } else {    //error
                Log.d("myLogTr", "Error");
                popupWelcome.errorPopup(context);
            }
            Log.d("myLogTr", "End of run()");   //terminated
        }, 100);
    }

    /**
     * createUrl:
     * create url :
     * https://ent-toulon.isen.fr/webaurion/ICS/firstName.lastName.ics
     * like :
     * https://ent-toulon.isen.fr/webaurion/ICS/axel.mezade.ics
     */
    public static String createUrl(String firstName, String lastName) {
        return "https://ent-toulon.isen.fr/webaurion/ICS/" + firstName + "." + lastName + ".ics";
    }

    public static void getSize(FilesUtil filesUtil){
        String url = createUrl(filesUtil.getFirstName(), filesUtil.getLastName());
        new Thread(() -> {
            try {
                URL myUrl = new URL(url);
                URLConnection urlConnection = myUrl.openConnection();
                urlConnection.connect();
                int file_size = urlConnection.getContentLength();
                Log.d("myLogDS", "file_size = " + file_size);
            } catch (IOException e) {
                e.printStackTrace();
            }
        }).start();
    }
}
