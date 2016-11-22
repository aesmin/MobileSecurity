package com.ssav.callBlocker.callBlockerService;
import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.app.Service;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.IBinder;
import android.support.v4.app.NotificationCompat;
import android.telephony.TelephonyManager;
import android.util.Log;
import android.widget.Toast;

import com.ssav.R;
import com.ssav.callBlocker.callBlockerActivities.CallBlockerMainActivity;
//import com.ssav.callBlocker.callListener.DeviceStateListener;
import com.ssav.callBlocker.callListener.ServiceReciever;
import com.ssav.callBlocker.messageListener.SmsReceiver;
import com.ssav.callBlocker.notificationCenter.CallBlockerToastNotification;
import com.ssav.callBlocker.objects.BlockedContact;

import java.io.FileInputStream;
import java.io.ObjectInputStream;
import java.util.HashMap;

public class CallBlockerService extends Service
{

    public static final int notification_id = 111;

    //---------------------------------------
    //Listening Services
    //---------------------------------------
   /* private static ServiceReciever service=new ServiceReciever();
    private static SmsReceiver sms=new SmsReceiver();*/

    //---------------------------------------
    //Data Structures
    //---------------------------------------
    public static HashMap<String, BlockedContact> blackList;
    ServiceReciever service =new ServiceReciever();
    SmsReceiver sms= new SmsReceiver();


    @Override
    public IBinder onBind(Intent intent)
    {
        return null;
    }

    @Override
    public void onCreate()
    {
        loadData();
        super.onCreate();
    }


    @Override
    public void onDestroy()
    {

        CallBlockerToastNotification.showDefaultShortNotification("Turning call blocker off");
        unregisterReceiver(service);
        unregisterReceiver(sms);
        service=null;
        sms=null;
        cancelStatusBarNotification();
        super.onDestroy();
    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId)
    {

        IntentFilter ff=new IntentFilter("android.intent.action.PHONE_STATE_CHANGED");
        registerReceiver(service,ff);
        registerReceiver(sms, new IntentFilter("android.provider.Telephony.SMS_RECEIVED"));
        Toast.makeText(getBaseContext(),"Registered Receiver",Toast.LENGTH_SHORT).show();
        showStatusBarNotification("Call blocker is running now");
        return START_STICKY;
    }

    public void showStatusBarNotification(String message)
    {
        NotificationManager manager=(NotificationManager)getSystemService(NOTIFICATION_SERVICE);

        Intent i=new Intent(CallBlockerService.this, CallBlockerMainActivity.class);
        PendingIntent pi=PendingIntent.getActivity(this, 0, i, 0);
        Notification noti =new NotificationCompat.Builder(this)
                .setContentTitle("Call blocker notification")
                .setWhen(System.currentTimeMillis())
                .setContentText(message).setSmallIcon(R.drawable.running_not_icon)
                .setContentIntent(pi).build();
        noti.flags |= Notification.FLAG_NO_CLEAR;
        manager.notify(notification_id,noti);
    }

    public void cancelStatusBarNotification()
    {
        NotificationManager manager=(NotificationManager)getSystemService(NOTIFICATION_SERVICE);
        manager.cancel(notification_id);
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


}
