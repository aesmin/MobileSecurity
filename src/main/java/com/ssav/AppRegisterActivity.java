package com.ssav;

import android.app.Activity;
import android.content.Intent;
import android.graphics.Typeface;
import android.os.Bundle;
import android.text.Html;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import com.ssav.DB.DBAdapter;
import com.ssav.Virusprotection.ReportActivity;
import com.ssav.antitheft.Utils;

import java.util.Calendar;
import java.util.Date;


/**
 * Created by Minaz on 2/28/14.
 */
public class AppRegisterActivity extends Activity {
    DBAdapter myDb;
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        String fontPath = "fonts/ROCKB.TTF";
        setContentView(R.layout.activatelicense_activity);
        Typeface tf = Typeface.createFromAsset(getAssets(), fontPath);
        TextView txt1 = (TextView)findViewById(R.id.txt110);
        txt1.setTypeface(tf);
        TextView txt2 = (TextView)findViewById(R.id.txt210);
        txt2.setTypeface(tf);
        TextView txt3 = (TextView)findViewById(R.id.txt19);
        txt3.setTypeface(tf);
        //ImageView button = (ImageView) findViewById(R.id.bt_Activate);
        /*String styledText = "<span style=font-size:10px; font-family:ROCK;> "
                + "Activate" + "</span>" + "<br />"
                + "<small style=font-size:5px>" + "Activate using Product Key" + "</small>";*/
      // button.setText(Html.fromHtml(styledText));

     //   ImageView button1 = (ImageView) findViewById(R.id.bt_Renew);
       /* styledText = "<span style=font-size:10px; font-family:ROCK;> "
                + "Renew" + "</span>"  + "<br />"
                + "<small style=font-size:5px>" + "Renew using Renewal code" + "</small>";
       button1.setText(Html.fromHtml(styledText));
*/

     //   ImageView button2 = (ImageView) findViewById(R.id.bt_Trial);
      /* styledText = "<span style=font-size:10px; font-family:ROCK;> "
                + "Free Trial" + "</span>" + "<br />"
                + "<small style=font-size:5px>" + "Limited days free trial" + "</small>";
        button2.setText(Html.fromHtml(styledText));*/

        openDB();

    }

    public void OnTrial(View view) {

        Intent intent = new Intent(AppRegisterActivity.this, UserProfile.class);
        intent.putExtra("data", "1");
        startActivity(intent);

    }

    public void OnActivate(View view) {

        Intent intent = new Intent(AppRegisterActivity.this, UserProfile.class);
        intent.putExtra("data","0");
        startActivity(intent);

    }

    public void OnRenew(View view) {

        Intent intent = new Intent(AppRegisterActivity.this, UserProfile.class);
        intent.putExtra("data","2");
        startActivity(intent);

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


    private void openDB() {
        myDb = new DBAdapter(this);
        myDb.open();
    }
}
