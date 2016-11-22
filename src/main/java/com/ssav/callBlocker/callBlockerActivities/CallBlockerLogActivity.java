package com.ssav.callBlocker.callBlockerActivities;

import android.app.Activity;
import android.content.Intent;
import android.content.res.Resources;
import android.graphics.drawable.GradientDrawable;
import android.graphics.drawable.GradientDrawable.Orientation;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.widget.ListView;
import android.widget.TabHost;

import com.ssav.AppRegisterActivity;
import com.ssav.LicenseInfo;
import com.ssav.R;
import com.ssav.Virusprotection.ReportActivity;
import com.ssav.antitheft.Utils;
import com.ssav.callBlocker.objects.BlockedActivity;
import com.ssav.callBlocker.objects.BlockedCall;
import com.ssav.callBlocker.objects.BlockedSms;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileWriter;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.util.ArrayList;

public class CallBlockerLogActivity extends Activity
{

    private TabHost tabhost;
    private Resources res;

    private static ArrayList<BlockedActivity> logList;
    private static ListView logListViewAll;
    private static ListView logListViewCalls;
    private static ListView logListViewSms;
    private static ArrayList<String>logBackup;
    private static Activity instance;

    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        setTheme(CallBlockerMainActivity.theme);
        res=getResources();

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_call_blocker_advanced_log);

        tabhost=(TabHost)findViewById(android.R.id.tabhost);
        tabhost.setup();

        TabHost.TabSpec spec=tabhost.newTabSpec("Calls/Sms");
        spec.setContent(R.id.tab1);
        spec.setIndicator("Calls/Sms",res.getDrawable(R.drawable.allblue));
        tabhost.addTab(spec);

        TabHost.TabSpec spec2=tabhost.newTabSpec("Calls");
        spec2.setContent(R.id.tab2);
        spec2.setIndicator("Calls",res.getDrawable(R.drawable.phoneblue));
        tabhost.addTab(spec2);

        TabHost.TabSpec spec3=tabhost.newTabSpec("Sms");
        spec3.setContent(R.id.tab3);
        spec3.setIndicator("Sms",res.getDrawable(R.drawable.smsblue));
        tabhost.addTab(spec3);

        tabhost.setCurrentTab(0);

        tabhost.setOnTabChangedListener(new TabHost.OnTabChangeListener()
        {

            @Override
            public void onTabChanged(String tabId)
            {

            }
        });

        instance=this;
        loadLog();
        int[] colors = {0, 0xFF00FFFF, 0};

        logListViewAll=(ListView)findViewById(R.id.logListViewAll);
        logListViewAll.setDivider(new GradientDrawable(Orientation.RIGHT_LEFT, colors));
        logListViewAll.setDividerHeight(3);
        logListViewAll.setAdapter(new LogAdapter(this, logList));

        logListViewCalls=(ListView)findViewById(R.id.logListViewCalls);
        logListViewCalls.setDivider(new GradientDrawable(Orientation.RIGHT_LEFT, colors));
        logListViewCalls.setDividerHeight(3);
        ArrayList<BlockedActivity>callsList=new ArrayList<BlockedActivity>();
        for(BlockedActivity blockac: logList)
        {
            if(blockac.getTitle().startsWith("Call"))
            {
                callsList.add(blockac);
            }
        }
        logListViewCalls.setAdapter(new LogAdapter(instance, callsList));

        logListViewSms=(ListView)findViewById(R.id.logListViewSms);
        logListViewSms.setDivider(new GradientDrawable(Orientation.RIGHT_LEFT, colors));
        logListViewSms.setDividerHeight(3);
        ArrayList<BlockedActivity>smsList=new ArrayList<BlockedActivity>();
        for(BlockedActivity blockac: logList)
        {
            if(blockac.getTitle().startsWith("Message"))
            {
                smsList.add(blockac);
            }
        }
        logListViewSms.setAdapter(new LogAdapter(instance, smsList));
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu items for use in the action bar
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.about_menu, menu);
        return super.onCreateOptionsMenu(menu);
    }

    @Override
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


    private void loadLog()
    {
        try
        {
            logList=new ArrayList<BlockedActivity>();
            logBackup=new ArrayList<String>();
            InputStreamReader isr=new InputStreamReader(openFileInput("CallLog.txt"));
            BufferedReader br=new BufferedReader(isr);
            String line=br.readLine();
            while(line !=null)
            {
                String [] textParts=line.split(";;");
                String title=textParts[0];
                String message=textParts[1];
                String name=textParts[2];
                String number=textParts[3];
                String hour=textParts[4];
                String bodyMessage=textParts[5];

                BlockedActivity bac;

                if(title.startsWith("Message"))
                {
                    bac=new BlockedSms(title, message, name, number, hour, bodyMessage);
                }
                else
                {
                    bac=new BlockedCall(title, message, name, number, hour);
                }

                logList.add(bac);
                logBackup.add(line);
                line=br.readLine();
            }
        }
        catch (Exception e)
        {
            e.printStackTrace();
            Log.e("Error", e.getMessage());
        }

    }

    public static void deleteFromLog(BlockedActivity toDelete,int pos)
    {
        logList.remove(toDelete);
        logBackup.remove(pos);

        logListViewAll.setAdapter(new LogAdapter(instance, logList));

        ArrayList<BlockedActivity>callsList=new ArrayList<BlockedActivity>();
        for(BlockedActivity blockac: logList)
        {
            if(blockac.getTitle().startsWith("Call"))
            {
                callsList.add(blockac);
            }
        }
        logListViewCalls.setAdapter(new LogAdapter(instance, callsList));

        ArrayList<BlockedActivity>smsList=new ArrayList<BlockedActivity>();
        for(BlockedActivity blockac: logList)
        {
            if(blockac.getTitle().startsWith("Message"))
            {
                smsList.add(blockac);
            }
        }
        logListViewSms.setAdapter(new LogAdapter(instance, smsList));

        replaceInLog(logBackup);
    }

    private static void replaceInLog(ArrayList<String> backup)
    {
        try
        {
            File f=new File("/data/data/com.ssav.callBlocker/files/CallLog.txt");
            f.renameTo(new File("/data/data/com.ssav.callBlocker/files/beforelastmodification.txt"));

            File newFile=new File("/data/data/com.ssav.callBlocker/files/CallLog.txt");
            if(!newFile.exists())
            {
                newFile.createNewFile();
            }

            FileWriter fw=new FileWriter(newFile,true);
            PrintWriter writer=new PrintWriter(fw);

            for(String st:backup)
            {
                writer.println(st);
            }


            writer.close();

            System.out.println("Log was modified successfully");
        }
        catch(Exception e)
        {
            Log.e("Error", e.getMessage());
        }
    }

    public static void call(String number)
    {
        Intent callIntent = new Intent(Intent.ACTION_CALL);
        callIntent.setData(Uri.parse("tel:"+number));
        instance.startActivity(callIntent);
    }

}
