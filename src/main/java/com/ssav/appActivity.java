package com.ssav;

import android.app.Activity;
import android.app.Dialog;
import android.content.Intent;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Typeface;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.Window;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;

import com.google.android.gcm.GCMRegistrar;
import com.ssav.DB.DBAdapter;
import com.ssav.Virusprotection.ReportActivity;
import com.ssav.Virusprotection.ScanSettings;
import com.ssav.antitheft.AegisActivity;
import com.ssav.antitheft.Utils;
import com.ssav.callBlocker.callBlockerActivities.CallBlockerMainActivity;
import com.ssav.Virusprotection.ScanActivity;

import java.sql.Date;
import java.text.SimpleDateFormat;

/**
 * Created by Minaz on 1/17/14.
 */
public class appActivity extends Activity {
    DBAdapter myDb;
    String fontPath = "fonts/ROCKB.TTF";
    Dialog supportDialog;
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

       // requestWindowFeature(Window.FEATURE_CUSTOM_TITLE);

        setContentView(R.layout.app_activity2);

       // getWindow().setFeatureInt(Window.FEATURE_CUSTOM_TITLE,R.layout.window_title);
        openDB();
        Cursor cursor = myDb.getAllRows();
        Typeface tf = Typeface.createFromAsset(getAssets(), fontPath);
        TextView txt1 = (TextView)findViewById(R.id.txt1);
        txt1.setTypeface(tf);
        TextView txt2 = (TextView)findViewById(R.id.txt2);
        txt2.setTypeface(tf);
        TextView txt3 = (TextView)findViewById(R.id.txt3);
        txt3.setTypeface(tf);
        TextView txt4 = (TextView)findViewById(R.id.txt4);
        txt4.setTypeface(tf);
        TextView txt5 = (TextView)findViewById(R.id.txt5);
        txt5.setTypeface(tf);
        TextView txt6 = (TextView)findViewById(R.id.txt6);
        txt6.setTypeface(tf);
       // TextView txt7 = (TextView)findViewById(R.id.txt7);
       // txt7.setTypeface(tf);
        ImageView btnCallSmsBlock = (ImageView)findViewById(R.id.img6);
       // Bitmap icon = BitmapFactory.decodeResource(getResources(), R.drawable.callsmsblock);
       // btnCallSmsBlock.setImageBitmap(icon);

        ImageView btnAntitheft = (ImageView)findViewById(R.id.img5);
        /*Bitmap icon2 = BitmapFactory.decodeResource(getResources(), R.drawable.antitheftnew);
        btnAntitheft.setImageBitmap(icon2);*/

        ImageView btnApplock = (ImageView)findViewById(R.id.img3);
       /* Bitmap icon1 = BitmapFactory.decodeResource(getResources(), R.drawable.lock);
        btnApplock.setImageBitmap(icon1);*/

        ImageView btnnvirusprotection = (ImageView)findViewById(R.id.img4);
      /*  Bitmap icon3 = BitmapFactory.decodeResource(getResources(), R.drawable.virusprotection);
        btnnvirusprotection.setImageBitmap(icon3);*/

        //ImageView btnactivateLicense = (ImageView)findViewById(R.id.img7);
        /*Bitmap icon4 = BitmapFactory.decodeResource(getResources(), R.drawable.activatelicence);
        btnactivateLicense.setImageBitmap(icon4);*/

        ImageView btnupdate = (ImageView)findViewById(R.id.img2);
        /*Bitmap icon5 = BitmapFactory.decodeResource(getResources(), R.drawable.update);
        btnupdate.setImageBitmap(icon5);*/

        ImageView btnscan = (ImageView)findViewById(R.id.img1);
        /*Bitmap icon6 = BitmapFactory.decodeResource(getResources(), R.drawable.scan);
        btnscan.setImageBitmap(icon6);
*/
        btnCallSmsBlock.setEnabled(true);
        btnApplock.setEnabled(true);
        btnAntitheft.setEnabled(true);
        //if(cursor.moveToFirst())
        {
           //if(cursor.getString(DBAdapter.COL_ACTIVATIONDT) != null)
            {
                //ImageButton button = (ImageButton) findViewById(R.id.img5);
                //button.setEnabled(true);

            }

            //trail copy
           // if(cursor.getString(DBAdapter.COL_LICTYPE) != null && cursor.getString(DBAdapter.COL_LICTYPE).equals("1"))
            {


               /* btnCallSmsBlock.setBackgroundResource(R.drawable.callblock);
                btnApplock.setBackgroundResource(R.drawable.applock);
                btnAntitheft.setBackgroundResource(R.drawable.antitheft);*/
            }

        }

       /* if (!GCMRegistrar.isRegisteredOnServer(this))
        {
           // Intent intent = new Intent(this, RegisterActivity.class);
            //startActivity(intent);
        }*/


    }


    public void CallBlock(View view) {
        Intent intent = new Intent(this, CallBlockerMainActivity.class);
        startActivity(intent);
       // finish();
    }

    public void Applock(View view) {
        Intent intent = new Intent(this, AppLockerActivity.class);
        //intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_NEW_TASK);
        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK |Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_SINGLE_TOP);
        startActivity(intent);
       // finish();
    }


    public void AntiTheft(View view) {
        Intent aeGisIntent = new Intent(this,
                AegisActivity.class);
       // aeGisIntent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        //aeGisIntent
          //      .addFlags(Intent.FLAG_ACTIVITY_EXCLUDE_FROM_RECENTS);

        startActivity(aeGisIntent);
        //finish();
    }

    public void ScanNow(View view) {
        Intent intent = new Intent(this, ScanActivity.class);
        startActivity(intent);
        // finish();
    }

    public void OnRegisterNow(View view) {
        Intent intent = new Intent(this, AppRegisterActivity.class);
        startActivity(intent);
        // finish();
    }

    public void VirusProtection(View view) {
        Intent intent = new Intent(this, ScanSettings.class);
        startActivity(intent);
        // finish();
    }

    public void OnUpdateNow1(View view) {

        Intent intent = new Intent(appActivity.this, UpdateActivity.class);
        startActivity(intent);

    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu items for use in the action bar
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.about_menu, menu);
        return super.onCreateOptionsMenu(menu);
       /* TextView txt8 = (TextView)findViewById(R.id.license_info);
        txt8.setTypeface(tf);*/
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

    private void openDB() {
        myDb = new DBAdapter(this);
        myDb.open();
    }
}
