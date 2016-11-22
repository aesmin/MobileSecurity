package com.ssav.Virusprotection;
import android.app.Activity;
import android.os.Bundle;
import android.content.Intent;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.widget.TextView;

import com.ssav.AppRegisterActivity;
import com.ssav.LicenseInfo;
import com.ssav.R;
import com.ssav.antitheft.Utils;

/**
 * Created by Minaz on 2/8/14.
 */
public class DetailUpdateReport extends Activity {

    TextView txtScanItem;
    TextView txtUpdateTime;
    TextView txtFromDB;
    TextView txtToDB;
    TextView txtstatus;
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.detail_update_report);
        Intent i = getIntent();
        String metadata = i.getStringExtra("metadata");
        //System.out.println(i.getStringExtra("metadata"));

        String [] textParts=metadata.split(";;");
        txtScanItem = (TextView) findViewById(R.id.txtScanItem);
        txtScanItem.setText("Report For: " + textParts[0]);

        txtUpdateTime = (TextView) findViewById(R.id.txtUpdateTime);
        txtUpdateTime.setText(textParts[1]);

        txtFromDB = (TextView) findViewById(R.id.txtFromDB);
        txtFromDB.setText(textParts[2]);

        txtToDB = (TextView) findViewById(R.id.txtToDB);
        txtToDB.setText(textParts[3]);

        txtstatus = (TextView) findViewById(R.id.txtstatus);
        txtstatus.setText("Status: " + textParts[4]);
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

}
