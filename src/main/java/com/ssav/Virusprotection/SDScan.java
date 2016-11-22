    package com.ssav.Virusprotection;

import java.io.OutputStreamWriter;

        import android.app.Activity;
        import android.app.AlertDialog;
        import android.app.AlertDialog.Builder;
        import android.content.Context;
        import android.content.DialogInterface;
        import android.content.DialogInterface.OnClickListener;
        import android.content.Intent;
        import android.content.res.Resources;
        import android.graphics.drawable.Drawable;
        import android.os.Bundle;
        import android.os.Handler;
        import android.util.Log;
        import android.view.Menu;
        import android.view.MenuInflater;
        import android.view.View;
        import android.widget.ProgressBar;
        import android.widget.TextView;

import com.ssav.R;

import java.io.File;
        import java.io.OutputStreamWriter;
        import java.io.PrintStream;
        import java.text.DateFormat;
        import java.text.SimpleDateFormat;
        import java.util.Date;

public class SDScan extends Activity
{
    private Handler handler = new Handler();
    private int noOfFiles = 0;
    private ProgressBar progressBar;
    private int progressStatus = 0;
    private String startDate = "";
    private String stopDate = "";
    private TextView textView;
    private TextView textView1;

    public int numberOfFiles(File paramFile)
    {
        int count = 0;
        File[] listFiles = paramFile.listFiles();
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

    protected void onCreate(Bundle paramBundle)
    {
        super.onCreate(paramBundle);
        setContentView(R.layout.fullscan);
        this.progressBar = ((ProgressBar)findViewById(R.id.progressBar1));
        Drawable localDrawable = getResources().getDrawable(R.drawable.customprogressbar);
        this.progressBar.setProgressDrawable(localDrawable);
        this.progressBar.setProgress(0);
        this.noOfFiles = numberOfFiles(new File("/mnt/"));
        this.progressBar.setMax(this.noOfFiles);
        this.textView = ((TextView)findViewById(R.id.textView2));
    //    this.textView1 = ((TextView)findViewById(2131361936));
        this.textView.setText("Scanning...");
        this.startDate = new SimpleDateFormat("yyyy/MM/dd - HH:mm:ss").format(new Date());
        new Thread(new Runnable()
        {
            public void run()
            {
                while (SDScan.this.progressStatus < SDScan.this.noOfFiles)
                {
                    //SDScan.access$012(SDScan.this, 1);
                    progressStatus+=1;
                    SDScan.this.handler.post(new Runnable()
                    {
                        public void run()
                        {
                            SDScan.this.progressBar.setProgress(SDScan.this.progressStatus);
                            SDScan.this.textView.setText("Scanned " + SDScan.this.progressStatus + " of " + SDScan.this.progressBar.getMax() + " file(s)");
                        }
                    });
                    try
                    {
                        Thread.sleep(200);
                    }
                    catch (InterruptedException localInterruptedException)
                    {
                        localInterruptedException.printStackTrace();
                    }
                }
                SimpleDateFormat localSimpleDateFormat = new SimpleDateFormat("yyyy/MM/dd - HH:mm:ss");
                Date localDate = new Date();
              //  SDScan.access$502(SDScan.this, localSimpleDateFormat.format(localDate));
                String str = "Memory card;;" + SDScan.this.startDate + ";;" + SDScan.this.stopDate + ";;" + SDScan.this.progressStatus + ";;Scanning Completed;;\r\n";
                SDScan.this.writeInLog(str);
                if (SDScan.this.progressStatus == SDScan.this.noOfFiles)
                {
                    Intent intent = new Intent(SDScan.this, ScanCompleted.class);
                    // intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                    intent.putExtra("data1", noOfFiles);
                    startActivity(intent);
                    finish();
                }
            }
        }).start();
    }

    public boolean onCreateOptionsMenu(Menu paramMenu)
    {
        getMenuInflater().inflate(R.menu.full_menu, paramMenu);
        return true;
    }

    protected void onDestroy()
    {
        super.onDestroy();
        this.stopDate = new SimpleDateFormat("yyyy/MM/dd - HH:mm:ss").format(new Date());
        writeInLog("Memory card;;" + this.startDate + ";;" + this.stopDate + ";;" + this.progressStatus + ";;Scan Interrupted;;\r\n");
    }

    public void showAlert(View paramView)
    {
        AlertDialog.Builder localBuilder = new AlertDialog.Builder(this);
        localBuilder.setTitle("Stop scan");
        localBuilder.setMessage("Are you sure you want to stop scan?").setCancelable(false).setPositiveButton("Yes", new DialogInterface.OnClickListener()
        {
            public void onClick(DialogInterface paramAnonymousDialogInterface, int paramAnonymousInt)
            {
                SDScan.this.finish();
            }
        }).setNegativeButton("No", new DialogInterface.OnClickListener()
        {
            public void onClick(DialogInterface paramAnonymousDialogInterface, int paramAnonymousInt)
            {
                paramAnonymousDialogInterface.cancel();
            }
        });
        localBuilder.create().show();
    }

    public void writeInLog(String paramString)
    {
        try
        {
            OutputStreamWriter localOutputStreamWriter = new OutputStreamWriter(getBaseContext().openFileOutput("ScanLog.txt", 32768));
            localOutputStreamWriter.append(paramString);
            localOutputStreamWriter.close();
            System.out.println("Writed in log succesfully");
            return;
        }
        catch (Exception localException)
        {
            Log.e("Error", localException.getMessage());
        }
    }
}

 
