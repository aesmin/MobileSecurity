package com.ssav;

import android.app.Activity;
import android.content.Intent;
import android.database.Cursor;
import android.os.Bundle;
import android.view.View;
import android.webkit.WebView;

import com.google.android.gms.internal.el;
import com.ssav.DB.DBAdapter;
import com.ssav.callBlocker.callBlockerActivities.CallBlockerMainActivity;

import java.util.Date;


/**
 * Created by Minaz on 2/27/14.
 */
public class Agreement  extends Activity {

    DBAdapter myDb;
    private WebView webView;
    Date dt;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        openDB();

        Cursor cursor = myDb.getAllRows();

        if(cursor != null)
        {
            if(cursor.moveToFirst())
            {
                if(cursor.getInt(myDb.COL_LICACC) == 1)
                {
                    Intent intent = new Intent(this, appActivity.class);
                    startActivity(intent);
                    Agreement.this.finish();
                    cursor.close();
                }
                else
                {
                    setContentView(R.layout.agreement);

                    webView = (WebView) findViewById(R.id.agreement_text);
                    webView.loadUrl("file:///android_asset/LicenceAgreement.htm");
                }
            }
            else {
                setContentView(R.layout.agreement);

                webView = (WebView) findViewById(R.id.agreement_text);
                webView.loadUrl("file:///android_asset/LicenceAgreement.htm");
            }

        }
        else

        {
            setContentView(R.layout.agreement);

            webView = (WebView) findViewById(R.id.agreement_text);
            webView.loadUrl("file:///android_asset/LicenceAgreement.htm");
        }



        dt = new Date();
    }

    public void OnIAgree(View view) {
        Intent intent = new Intent(this, appActivity.class);
        startActivity(intent);

        myDb.deleteAll();
        myDb.insertRow(1, dt.toString(),1);
        // finish();
    }

    public void OnCancel(View view) {
        Agreement.this.finish();
        myDb.insertRow(1, dt.toString(),0);
    }

    private void openDB() {
        myDb = new DBAdapter(this);
        myDb.open();
    }
    private void closeDB() {
        myDb.close();
    }
}
