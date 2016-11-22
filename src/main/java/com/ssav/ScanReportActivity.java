package com.ssav;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.TextView;

import com.ssav.Virusprotection.DetailScanReport;
import com.ssav.Virusprotection.ReportActivity;
import com.ssav.Virusprotection.ReportAdapter;
import com.ssav.Virusprotection.ScanParameter;
import com.ssav.Virusprotection.ScanReport;
import com.ssav.antitheft.Utils;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.util.ArrayList;

/**
 * Created by Minaz on 2/7/14.
 */
public class ScanReportActivity extends Activity{
    private static ArrayList<ScanParameter> logList;
    private static ArrayList<ScanParameter> NewlogList;
    private static ListView logListViewScan;
    private static ArrayList<String>logBackup;
    private static ArrayAdapter<String> listAdapter;

    protected void onCreate(Bundle savedInstanceState)
    {super.onCreate(savedInstanceState);
        setContentView(R.layout.scan_report);

        loadLog();


        logListViewScan=(ListView)findViewById(R.id.logListViewScan);
        //logListViewScan.setDivider(new GradientDrawable(GradientDrawable.Orientation.RIGHT_LEFT, colors));
        logListViewScan.setDividerHeight(3);

        listAdapter = new ArrayAdapter<String>(getBaseContext(), android.R.layout.activity_list_item,logBackup);
        logListViewScan.setAdapter(new ReportAdapter(this, NewlogList));

        logListViewScan.setOnItemClickListener(new OnItemClickListener() {
            public void onItemClick(AdapterView<?> parent, View view,
                                    int position, long id) {



                // selected item
                String data = ((TextView)view.findViewWithTag("textViewMetaData")).getText().toString();

                // Launching new Activity on selecting single List Item
                Intent i = new Intent(getApplicationContext(), DetailScanReport.class);
                // sending data to new activity
                i.putExtra("metadata", data);
                startActivity(i);

            }
        });
    }
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
            logList=new ArrayList<ScanParameter>();
            NewlogList=new ArrayList<ScanParameter>();
            logBackup=new ArrayList<String>();
            InputStreamReader isr=new InputStreamReader(getApplicationContext().openFileInput("ScanLog.txt"));
            BufferedReader br=new BufferedReader(isr);
            String line=br.readLine();
            while(line !=null)
            {
                String [] textParts=line.split(";;");
                String title=textParts[0];
                String scansttime=textParts[1];
                String scanendtime=textParts[2];
                String nooffilesscanned=textParts[3];
                String status=textParts[4];
                String scantime=scansttime;

                ScanParameter bac;

                bac = new ScanReport(title, scansttime, scanendtime, nooffilesscanned,scantime, status);

                logList.add(bac);
                logBackup.add(line);
                line=br.readLine();
            }

            for (int i=logList.size() - 1; i != 0; i--)
            {
                NewlogList.add(logList.get(i));
            }
        }
        catch (Exception e)
        {
            e.printStackTrace();
            Log.e("Error", e.getMessage());
        }

    }
}
