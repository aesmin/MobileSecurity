package com.ssav.callBlocker.callBlockerActivities;

import android.app.Activity;
import android.app.ActivityManager;
import android.app.ActivityManager.RunningServiceInfo;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Typeface;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.util.Log;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.ssav.AppRegisterActivity;
import com.ssav.LicenseInfo;
import com.ssav.R;
import com.ssav.Virusprotection.ReportActivity;
import com.ssav.antitheft.Utils;
import com.ssav.callBlocker.callBlockerService.CallBlockerService;
import com.ssav.callBlocker.callListener.ServiceReciever;
import com.ssav.callBlocker.notificationCenter.CallBlockerToastNotification;
import com.ssav.callBlocker.objects.BlockedContact;

import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.Serializable;
import java.util.HashMap;

/**
 * @author Rodrigo Cifuentes G�mez
 *
 */
public class CallBlockerMainActivity extends Activity implements Serializable
{
    //---------------------------------------
    //Static References
    //---------------------------------------
    private static final long serialVersionUID = -1875124806221731429L;
    public static ActivityManager manager;
    public static Context applicationContext;
    public static boolean serviceIsRunning;
    public static int theme;
    String fontPath = "fonts/ROCKB.TTF";
    //---------------------------------------
    //Graphic Atributes
    //---------------------------------------
    private ImageView buttonStartService;
    private ImageView buttonStopService;
    private ImageView buttonBlacklist;
    private ImageView buttonLog;
    private Button buttonAppInfo;
    private Button buttonAboutDev;
    private Button buttonRateApp;
    private Button buttonSettings;
TextView txtstart,txtstop,txtlog,txtblacklist;
    //---------------------------------------
    //Data Structures
    //---------------------------------------
    public static HashMap<String, BlockedContact>blackList;


    @Override
    public View findViewById(int id) {
        return super.findViewById(id);
    }

    //-----------------------------------------------------------------------------
    //Activity Main Methods
    //-----------------------------------------------------------------------------
    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        SharedPreferences prefs=PreferenceManager.getDefaultSharedPreferences(this);
       // theme=Integer.parseInt(prefs.getString("call_blocker_theme_preference", "16974143"));
      //  setTheme(theme);

        super.onCreate(savedInstanceState);
        setContentView(R.layout.callsmsblock_activity);
        Typeface tf = Typeface.createFromAsset(getAssets(), fontPath);
        txtstart = (TextView)findViewById(R.id.txt10);
        txtstart.setTypeface(tf);
        txtstop=(TextView)findViewById(R.id.txt20);
        txtstop.setTypeface(tf);
        txtlog=(TextView)findViewById(R.id.txt30);
        txtlog.setTypeface(tf);
        txtblacklist=(TextView)findViewById(R.id.txt40);
        txtblacklist.setTypeface(tf);
        applicationContext=getApplicationContext();
        manager=(ActivityManager)getSystemService(Context.ACTIVITY_SERVICE);
        loadData();

        buttonStartService=(ImageView)findViewById(R.id.mainActivityStartCallBlockerService);
        buttonStartService.setOnClickListener(new View.OnClickListener()
        {

            @Override
            public void onClick(View v)
            {
               // startService(new Intent(getBaseContext(),CallBlockerService.class));
                startCallBlockerService();


            }
        });

        buttonStopService=(ImageView)findViewById(R.id.mainActivityStopCallBlockerService);
        buttonStopService.setOnClickListener(new View.OnClickListener()
        {

            @Override
            public void onClick(View v)
            {
                //stopService(new Intent(getBaseContext(),CallBlockerService.class));
                stopCallBlockerService();
            }
        });

        buttonBlacklist=(ImageView)findViewById(R.id.mainActivitySeeBlacklist);
        buttonBlacklist.setOnClickListener(new View.OnClickListener()
        {

            @Override
            public void onClick(View v)
            {

                if(checkIfServiceIsAlreadyRunning())
                {
                    AlertDialog dialog=new AlertDialog.Builder(v.getContext()).create();
                    dialog.setTitle("Blacklist");
                    dialog.setMessage("Right now service is running so the application will stop it, do you want to continue? You can start again the service from the menu");
                    dialog.setButton(DialogInterface.BUTTON_POSITIVE, "Yes" ,new DialogInterface.OnClickListener()
                    {

                        @Override
                        public void onClick(DialogInterface dialog, int which)
                        {
                            stopCallBlockerService();
                            Intent i=new Intent(CallBlockerMainActivity.this,CallBlockerBlacklistViewActivity.class);
                            startActivity(i);
                        }
                    });
                    dialog.setButton(DialogInterface.BUTTON_NEGATIVE,"No", new DialogInterface.OnClickListener()
                    {

                        @Override
                        public void onClick(DialogInterface dialog, int which)
                        {

                        }
                    });
                    dialog.setCancelable(false);
                    dialog.setIcon(R.drawable.advert);
                    dialog.show();
                }
                else
                {
                    Intent i=new Intent(CallBlockerMainActivity.this,CallBlockerBlacklistViewActivity.class);
                    startActivity(i);
                }
            }
        });

        buttonLog=(ImageView)findViewById(R.id.mainActivitySeeLog);
        buttonLog.setOnClickListener(new View.OnClickListener()
        {

            @Override
            public void onClick(View v)
            {
                if(checkIfServiceIsAlreadyRunning())
                {

                    AlertDialog dialog=new AlertDialog.Builder(v.getContext()).create();
                    dialog.setTitle("My log");
                    dialog.setMessage("Right now service is running so the application will stop it, do you want to continue? You can start again the service from the menu");
                    dialog.setButton(DialogInterface.BUTTON_POSITIVE, "Yes" ,new DialogInterface.OnClickListener()
                    {

                        @Override
                        public void onClick(DialogInterface dialog, int which)
                        {
                            stopCallBlockerService();
                            Intent i=new Intent(CallBlockerMainActivity.this,CallBlockerLogActivity.class);
                            startActivity(i);
                        }
                    });
                    dialog.setButton(DialogInterface.BUTTON_NEGATIVE,"No", new DialogInterface.OnClickListener()
                    {

                        @Override
                        public void onClick(DialogInterface dialog, int which)
                        {

                        }
                    });
                    dialog.setCancelable(false);
                    dialog.setIcon(R.drawable.advert);
                    dialog.show();
                }
                else
                {
                    Intent i=new Intent(CallBlockerMainActivity.this,CallBlockerLogActivity.class);
                    startActivity(i);
                }
            }
        });

        /*buttonAppInfo=(Button)findViewById(R.id.mainActivityAppInfo);
        buttonAppInfo.setOnClickListener(new View.OnClickListener()
        {

            @Override
            public void onClick(View v)
            {
                AlertDialog dialog=new AlertDialog.Builder(v.getContext()).create();
                dialog.setTitle("App info");
                dialog.setMessage("Super Star Mobile Security: Call and SMS Blocker");
                dialog.setButton(DialogInterface.BUTTON_POSITIVE, "Close" ,new DialogInterface.OnClickListener()
                {

                    @Override
                    public void onClick(DialogInterface dialog, int which)
                    {

                    }
                });
                dialog.setCancelable(false);
                dialog.setIcon(R.drawable.info);
                dialog.show();
            }
        });

        buttonAboutDev=(Button)findViewById(R.id.mainActivityAboutInfo);
        buttonAboutDev.setOnClickListener(new View.OnClickListener()
        {

            @Override
            public void onClick(View v)
            {
                AlertDialog dialog=new AlertDialog.Builder(v.getContext()).create();
                dialog.setTitle("about Us");
                dialog.setMessage("Super Star Technologies Pvt. Ltd. www.superstarav.com");
                dialog.setButton(DialogInterface.BUTTON_POSITIVE, "Close" ,new DialogInterface.OnClickListener()
                {

                    @Override
                    public void onClick(DialogInterface dialog, int which)
                    {

                    }
                });
                dialog.setCancelable(false);
                dialog.setIcon(R.drawable.about);
                dialog.show();
            }
        });

        buttonRateApp =(Button)findViewById(R.id.mainActivityRateApp);
        buttonRateApp.setOnClickListener(new View.OnClickListener()
        {

            @Override
            public void onClick(View v)
            {
                Uri uri=Uri.parse("market://details?id="+getApplicationContext().getPackageName());
                Intent i=new Intent(Intent.ACTION_VIEW,uri);

                try
                {
                    startActivity(i);
                }
                catch(ActivityNotFoundException e)
                {
                    uri=Uri.parse("https://play.google.com/store/apps/details?id="+getApplicationContext().getPackageName());
                    i=new Intent(Intent.ACTION_VIEW,uri);
                    startActivity(i);
                }
            }
        });

        buttonSettings=(Button)findViewById(R.id.mainActivitySettings);
        buttonSettings.setOnClickListener(new View.OnClickListener()
        {

            @Override
            public void onClick(View v)
            {
                Intent i=new Intent(CallBlockerMainActivity.this, SettingsActivity.class);
                startActivity(i);
            }
        });*/

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



    //-----------------------------------------------------------------------------
    //Call Blocker Methods
    //-----------------------------------------------------------------------------
    public void startCallBlockerService()
    {
        if(!checkIfServiceIsAlreadyRunning())
        {
            if(blackList.size()>0)
            {
                Intent i=new Intent(CallBlockerMainActivity.this, CallBlockerService.class);
                startService(i);
            }
            else
            {
                CallBlockerToastNotification.showDefaultShortNotification("Blacklist is empty, service can't start");
            }
        }
        else
        {
            CallBlockerToastNotification.showDefaultShortNotification("Service is running already");
        }
    }

    public void stopCallBlockerService()
    {
        if(checkIfServiceIsAlreadyRunning())
        {
            stopService(new Intent(CallBlockerMainActivity.this, CallBlockerService.class));
        }
    }

    public static boolean checkIfServiceIsAlreadyRunning()
    {
        for(RunningServiceInfo service: manager.getRunningServices(Integer.MAX_VALUE))
        {
            if(CallBlockerService.class.getName().equals(service.service.getClassName()))
            {
                return true;
            }
        }
        return false;
    }

    public void loadData()
    {
        try
        {
            FileInputStream fis=openFileInput("CallBlocker.data");
            ObjectInputStream objeto=new ObjectInputStream(fis);
            blackList=(HashMap<String, BlockedContact>)objeto.readObject();
            fis.close();
            objeto.close();
        }
        catch (Exception e)
        {
            blackList=new HashMap<String, BlockedContact>();
            Log.e("Error", e.getMessage());
        }
    }

    public void saveData()
    {
        try
        {
            FileOutputStream fos=openFileOutput("CallBlocker.data", Context.MODE_PRIVATE);
            ObjectOutputStream oos=new ObjectOutputStream(fos);
            oos.writeObject(blackList);
            fos.close();
            oos.close();
            CallBlockerToastNotification.showDefaultShortNotification("Saving Data...");
        }
        catch (Exception e)
        {
            Log.e("Error", e.getMessage());
        }
    }

}
