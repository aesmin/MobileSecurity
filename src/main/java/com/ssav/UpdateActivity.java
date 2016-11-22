package com.ssav;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.os.Handler;
import android.util.Log;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.widget.ProgressBar;

import com.ssav.Virusprotection.ConnectionDetector;
import com.ssav.Virusprotection.ReportActivity;
import com.ssav.Virusprotection.UpdateSucessful;
import com.ssav.antitheft.Utils;

import java.io.OutputStreamWriter;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;

/**
 * Created by Minaz on 2/6/14.
 */
public class UpdateActivity extends Activity {
    private ProgressBar progressBar;
    private int progressStatus = 0;
    private Handler handler = new Handler();

    private String fromDate="";
    private String toDate="";
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);


        ConnectionDetector cd = new ConnectionDetector(getApplicationContext());

        Boolean isInternetPresent = cd.isConnectingToInternet(); // true or false
       // isInternetPresent =false;

        if(isInternetPresent)
        {
            setContentView(R.layout.update);

            DateFormat dateFormat=new SimpleDateFormat("yyyy/MM/dd - HH:mm:ss");
            Date date=new Date();
            fromDate=dateFormat.format(date);

            progressBar = (ProgressBar) findViewById(R.id.progressBar1);
            Drawable draw= getResources().getDrawable(R.drawable.customprogressbar);
            progressBar.setProgressDrawable(draw);
            progressBar.setProgress(0);
            // Start long running operation in a background thread
            new Thread(new Runnable() {
                public void run() {
                    while (progressStatus <= 100) {
                        progressStatus += 1;
                        // Update the progress bar and display the current value in the text view
                        handler.post(new Runnable() {
                            public void run() {
                                progressBar.setProgress(progressStatus);

                            }
                        });
                        try {
                            // Sleep for 200 milliseconds. Just to display the progress slowly
                            Thread.sleep(100);
                        } catch (InterruptedException e) {
                            e.printStackTrace();
                        }
                    }

                    if(progressStatus == 101)
                    {
                        DateFormat dateFormat=new SimpleDateFormat("yyyy/MM/dd - HH:mm:ss");
                        Date date=new Date();
                        toDate=dateFormat.format(date);

                        //LOG FORMAT --> TITLE;;update time;;from database;;to database;;status;;SEPARATOR
                        String message="Update;;"+fromDate+";;"+fromDate+";;"+toDate+";;Successfully Updated.;;\r\n";
                        writeInLog(message);

                        Intent intent = new Intent(getBaseContext(), UpdateSucessful.class);
                        intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                        startActivity(intent);
                        finish();

                    }

                }
            }).start();

    }
    else
    {
        setContentView(R.layout.updatefailed);
    }
    }

    @Override
    protected void onResume() {

        super.onResume();
        //setContentView(R.layout.updatesucessful);
    }
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu items for use in the action bar
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.about_menu, menu);
        return super.onCreateOptionsMenu(menu);
    }
    public boolean onOptionsItemSelected(MenuItem item) {
        switch(item.getItemId()) {
            case R.id.about_us:
                Utils.createWebViewDialog("file:///android_asset/aboutus.html", this);
                return true;
            case R.id.reports:
                Intent intent = new Intent(this, ReportActivity.class);
                startActivity(intent);
                return true;
            case R.id.license_info:
                Intent i = new Intent(this, LicenseInfo.class);
                startActivity(i);
                return true;
            case R.id.register:
                Intent register = new Intent(this, AppRegisterActivity.class);
                startActivity(register);
                return true;
            case R.id.support:
                Utils.createWebViewDialog("file:///android_asset/support.html", this);
                return true;

            case R.id.purchase:
                Utils.createWebViewDialog("file:///android_asset/purchase.html", this);
                return true;
        }
        return false;
    }


    public void writeInLog(String message)
    {
        try
        {
            OutputStreamWriter fos = new OutputStreamWriter(getBaseContext().openFileOutput("UpdateLog.txt", Context.MODE_APPEND));
            fos.append(message);
            fos.close();
            System.out.println("Writed in log succesfully");
        }
        catch(Exception e)
        {
            Log.e("Error", e.getMessage());
        }

    }

}
