package com.ssav;

import android.app.Activity;
import android.content.Intent;
import android.database.Cursor;
import android.graphics.Typeface;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.webkit.WebView;
import android.widget.TextView;

import com.ssav.DB.DBAdapter;
import com.ssav.Virusprotection.ReportActivity;
import com.ssav.antitheft.Utils;

import java.sql.Date;
import java.text.DateFormat;
import java.text.SimpleDateFormat;

/**
 * Created by Minaz on 3/5/14.
 */
public class LicenseInfo extends Activity {
    DBAdapter myDb;
    private WebView webView;
    String fontPath = "fonts/ROCKB.TTF";
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.license_info);
        Typeface tf = Typeface.createFromAsset(getAssets(), fontPath);
        TextView txtUser = (TextView) findViewById(R.id.txtUser);
        txtUser.setTypeface(tf);
       TextView txtExpiry = (TextView) findViewById(R.id.txtExpiry);
        txtExpiry.setTypeface(tf);
        TextView txtVdB = (TextView) findViewById(R.id.txtVdB);
        txtVdB.setTypeface(tf);
        TextView txtVersion = (TextView)findViewById(R.id.txtVersion);
        txtVersion.setTypeface(tf);
        TextView txtnotRegistred = (TextView) findViewById(R.id.txtnotRegistred);
        txtnotRegistred.setTypeface(tf);
        webView = (WebView) findViewById(R.id.agreement_text);
        webView.loadUrl("file:///android_asset/LicenceAgreement.htm");
        openDB();
        Cursor cursor = myDb.getAllRows();
        if(cursor.moveToFirst())
        {

            if(cursor.getString(DBAdapter.COL_LICTYPE) != null && cursor.getString(DBAdapter.COL_LICTYPE).equals("0"))
            {
               txtnotRegistred.setVisibility(View.VISIBLE);
            }
            else if(cursor.getString(DBAdapter.COL_LICTYPE) != null && cursor.getString(DBAdapter.COL_LICTYPE).equals("1"))
            {
                SimpleDateFormat format = new SimpleDateFormat("MM/dd/yyyy");

                if(cursor.getString(DBAdapter.COL_VRSDBDT) != null && cursor.getString(DBAdapter.COL_VRSDBDT).equals(""))
                {
                    txtVdB.setText("Virus Database: " +format.format(Date.parse(cursor.getString(DBAdapter.COL_VRSDBDT))));
                }

                txtUser.setText("Licensed to: " + cursor.getString(DBAdapter.COL_USERNAME));
                txtExpiry.setText("License valid till: " + format.format(Date.parse(cursor.getString(DBAdapter.COL_EXPIRYDT))));
                txtUser.setVisibility(View.VISIBLE);
                txtExpiry.setVisibility(View.VISIBLE);

            }
        }
    }

    private void openDB() {
        myDb = new DBAdapter(this);
        myDb.open();
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


    public void OnUpdateNow(View view) {

        Intent intent = new Intent(LicenseInfo.this, UpdateActivity.class);
        startActivity(intent);

    }
}
