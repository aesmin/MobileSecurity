package com.ssav.Virusprotection;
import android.app.Activity;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.os.Environment;
import android.os.Handler;
import android.util.Log;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.ssav.AppRegisterActivity;
import com.ssav.LicenseInfo;
import com.ssav.R;
import com.ssav.antitheft.Utils;

import java.io.File;
import java.io.OutputStreamWriter;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;

/**
 * Created by Minaz on 1/29/14.
 */
public class FullScan extends Activity {
    private ProgressBar progressBar;
    private int progressStatus = 0;
    private TextView textView;
    private TextView textView1;
    private Handler handler = new Handler();
    private int noOfFiles = 0;
    private TextView check;
    private String startDate = "";
    private String stopDate = "";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.fullscan);
        progressBar = (ProgressBar) findViewById(R.id.progressBar1);
        check=(TextView)findViewById(R.id.textView3);
        Drawable draw = getResources().getDrawable(R.drawable.customprogressbar);
        progressBar.setProgressDrawable(draw);
        progressBar.setProgress(0);
       // check.setText("Something will happen. It'll chnage/...");
        int cnt = 0;
        if (Environment.getDataDirectory() != null) {
            File f = new File(Environment.getDataDirectory().toString());
            cnt = numberOfFiles(f);
        }
        if (Environment.getRootDirectory() != null) {
            cnt = cnt + numberOfFiles(new File(Environment.getRootDirectory().toString()));
        }
        if (Environment.getDownloadCacheDirectory() != null) {
            cnt = cnt + numberOfFiles(new File(Environment.getDownloadCacheDirectory().toString()));
        }

        boolean sdCardExist = Environment.getExternalStorageState().equals(Environment.MEDIA_MOUNTED);
        if (sdCardExist) {

            cnt = cnt + numberOfFiles(new File(Environment.getExternalStorageDirectory().toString()));
        }

        DateFormat dateFormat = new SimpleDateFormat("yyyy/MM/dd - HH:mm:ss");
        Date date = new Date();
        startDate = dateFormat.format(date);

        noOfFiles = cnt;
        progressBar.setMax(noOfFiles);
        textView = (TextView) findViewById(R.id.textView1);
        textView1 = (TextView) findViewById(R.id.textView2);

        textView1.setText("Scanning...");

        // Start long running operation in a background thread
        new Thread(new Runnable() {
            public void run() {
                while (progressStatus < noOfFiles) {
                    progressStatus += 1;


                    // Update the progress bar and display the current value in the text view
                    handler.post(new Runnable() {
                        public void run() {
                            progressBar.setProgress(progressStatus);
                            textView.setText("Scanned " + progressStatus + " of " + progressBar.getMax() + " file(s)");

                             }
                    });

                    try {
                        // Sleep for 200 milliseconds. Just to display the progress slowly
                        Thread.sleep(100);
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                }


                DateFormat dateFormat = new SimpleDateFormat("yyyy/MM/dd - HH:mm:ss");
                Date date = new Date();
                stopDate = dateFormat.format(date);

                //LOG FORMAT --> TITLE;;scan start time;;scan end time;;no of files scanned;;status;;SEPARATOR
                String message = "Full Scan;;" + startDate + ";;" + stopDate + ";;" + progressStatus + ";;Scanning Completed;;\r\n";
                writeInLog(message);
                Log.w("", "" + noOfFiles);
                if (progressStatus == noOfFiles) {

                    Intent intent = new Intent(FullScan.this, ScanCompleted.class);
                    // intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                    intent.putExtra("data", noOfFiles);
                    startActivity(intent);
                    finish();

                }
            }
        }).start();

    }


    public int numberOfFiles(File srcDir) {
        int count = 0;
        File[] listFiles = srcDir.listFiles();
        if (listFiles != null) {
            for (int i = 0; i < listFiles.length; i++) {
                if (listFiles[i].isDirectory()) {
                    count += numberOfFiles(listFiles[i]);
                } else if (listFiles[i].isFile()) {
                    count++;

                }
            }
        }
        return count;
    }


    public void showAlert(View view) {
        AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(
                FullScan.this);

        // set title
        alertDialogBuilder.setTitle("Stop scan");

        // set dialog message
        alertDialogBuilder
                .setMessage("Are you sure you want to stop scan?")
                .setCancelable(false)
                .setPositiveButton("Yes",
                        new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog,
                                                int id) {
                                // if this button is clicked, close
                                // current activity
                                FullScan.this.finish();

                            }
                        })
                .setNegativeButton("No",
                        new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog,
                                                int id) {
                                // if this button is clicked, just close
                                // the dialog box and do nothing
                                dialog.cancel();
                            }
                        });

        // create alert dialog
        AlertDialog alertDialog = alertDialogBuilder.create();

        // show it
        alertDialog.show();
    }

    public void writeInLog(String message) {
        try {
            OutputStreamWriter fos = new OutputStreamWriter(getBaseContext().openFileOutput("ScanLog.txt", Context.MODE_APPEND));
            fos.append(message);
            fos.close();
            System.out.println("Writed in log succesfully");
        } catch (Exception e) {
            Log.e("Error", e.getMessage());
        }

    }

    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu items for use in the action bar
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.about_menu, menu);
        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {

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

    @Override
    protected void onDestroy() {
        // TODO Auto-generated method stub
        super.onDestroy();

        DateFormat dateFormat = new SimpleDateFormat("yyyy/MM/dd - HH:mm:ss");
        Date date = new Date();
        stopDate = dateFormat.format(date);

        //LOG FORMAT --> TITLE;;scan start time;;scan end time;;no of files scanned;;status;;SEPARATOR
        String message = "Full Scan;;" + startDate + ";;" + stopDate + ";;" + progressStatus + ";;Scan Interrupted;;\r\n";
        writeInLog(message);
    }

    public void virusCheck(File srcDir) {
        int count = 0;
        File[] listFiles = srcDir.listFiles();
        if (listFiles != null) {
            for (int i = 0; i < listFiles.length; i++) {
                if (listFiles[i].isDirectory()) {
                    count += numberOfFiles(listFiles[i]);
                } else if (listFiles[i].isFile()) {
                    if(listFiles[i].getName().equals("faltu.txt"))
                    {
                        check.setText("There is nothing..."+i);

                    }
                    else {
                        check.setText("There is a virus" + i);
                    }
                }
            }
        }
    }
}