package com.ssav.Virusprotection;
import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.Typeface;
import android.os.Bundle;
import android.os.Environment;
import android.os.Handler;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.ssav.AppRegisterActivity;
import com.ssav.LicenseInfo;
import com.ssav.R;
import com.ssav.antitheft.Utils;

import java.io.File;

/**
 * Created by Minaz on 1/28/14.
 */
public class ScanActivity extends Activity {
    Button btnFullScan;
    Button btnSDScan;
    String fontPath = "fonts/ROCKB.TTF";
    ProgressDialog progressBar;
    private int progressBarStatus = 0;
    private Handler progressBarHandler = new Handler();
    TextView txtv;
    private long fileSize = 0;
    private int noOfFiles = 0;
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.scan);
      //  Typeface tf = Typeface.createFromAsset(getAssets(), fontPath);
       // TextView fulscan = (TextView)findViewById(R.id.txt11);
       // fulscan.setTypeface(tf);
       // TextView sdscan = (TextView)findViewById(R.id.txt21);
      //  sdscan.setTypeface(tf);

        boolean sdCardExist = Environment.getExternalStorageState().equals(
                Environment.MEDIA_MOUNTED);
        if (!sdCardExist) {
            Toast.makeText(getBaseContext(),"SD Card Does Not Exist",Toast.LENGTH_SHORT).show();
            btnSDScan.setEnabled(false);
            btnSDScan.setBackgroundColor(Color.parseColor("#FFFFFF"));
           // btnSDScan.sett("No SD Card");
          //  btnSDScan.setTextColor(Color.parseColor("#000000"));
            btnSDScan.getBackground().setAlpha(45);

        }
        else
            Toast.makeText(getBaseContext(),"SD Card Exist",Toast.LENGTH_SHORT).show();
        addListenerOnButton();
        /*txtv.setText( Environment
                .getExternalStorageDirectory().getPath()
        );*/
        //txtv.setText( Environment.getRootDirectory()
        ///   .getPath());

    }

    public void addListenerOnButton() {

        btnFullScan = (Button) findViewById(R.id.full_scan);
        btnFullScan.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getBaseContext(), FullScan.class);
                startActivity(intent);
            }
        });


       btnSDScan = (Button) findViewById(R.id.sd_scan);

        btnSDScan.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getBaseContext(), SDScan.class);
                startActivity(intent);
            }
        });

    }



    // file download simulator... a really simple
    public int doSomeTasks() {
        int cnt = numberOfFiles(new File(Environment.getRootDirectory().getPath()));
        while (fileSize <= cnt)
        {
            fileSize += 1;
            return cnt;

        }

		/*while (fileSize <= 1000000) {

			fileSize++;

			if (fileSize == 100000) {
				return 10;
			} else if (fileSize == 200000) {
				return 20;
			} else if (fileSize == 300000) {
				return 30;
			}
			// ...add your own

		}*/

        return cnt;

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

    public Integer countFiles(File folder, Integer count){

        File[] files = folder.listFiles();
        if(files != null)
        {
            for(File file: files){
                if(file.isFile()){
                    count++;
                }else{
                    countFiles(file, count);
                }
            }

        }



        return count;
    }

    public int numberOfFiles(File srcDir) {
        int count = 0;
        File[] listFiles = srcDir.listFiles();
        if(listFiles != null)
        {
            for(int i=0; i < listFiles.length; i++){
                if(listFiles[i].isDirectory()){
                    count += numberOfFiles(listFiles[i]);
                }else if(listFiles[i].isFile()){
                    count++;
                }
            }
        }
        return count;
    }

}