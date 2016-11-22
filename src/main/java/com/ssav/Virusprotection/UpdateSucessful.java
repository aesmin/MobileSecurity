package com.ssav.Virusprotection;

import android.app.Activity;
import android.content.Intent;
import android.graphics.Typeface;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.widget.TextView;

import com.ssav.AppRegisterActivity;
import com.ssav.LicenseInfo;
import com.ssav.R;
import com.ssav.antitheft.Utils;

/**
 * Created by Minaz on 2/6/14.
 */
public class UpdateSucessful  extends Activity {
    String fontPath = "fonts/ROCK.TTF";
    public void onCreate(Bundle savedInstanceState) {
        Typeface tf = Typeface.createFromAsset(getAssets(), fontPath);
        super.onCreate(savedInstanceState);
        setContentView(R.layout.updatesucessful);
        TextView txt1 = (TextView)findViewById(R.id.textView);
        txt1.setTypeface(tf);

        TextView txt2 = (TextView)findViewById(R.id.textView2);
        txt2.setTypeface(tf);
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
