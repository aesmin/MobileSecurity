package com.ssav.callBlocker.callListener;

import android.app.Activity;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.telephony.PhoneStateListener;
import android.telephony.TelephonyManager;
import android.util.Log;

import com.ssav.android.internal.telephony.ITelephony;
import com.ssav.callBlocker.callBlockerActivities.BlockedContactAdapter;
import com.ssav.callBlocker.callBlockerService.CallBlockerService;
import com.ssav.callBlocker.objects.BlockedContact;

import java.io.OutputStreamWriter;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.concurrent.Executor;
import java.util.concurrent.Executors;

public class ServiceReciever extends BroadcastReceiver {
    private ITelephony telephonyService;
    private static TelephonyManager telephony;
    private Context context;


    @Override
    public void onReceive(Context context, Intent intent) {

        // TODO Auto-generated method stub
        Bundle myBundle = intent.getExtras();
        if (myBundle != null) {
            try {
                if (intent.getAction().equals("android.intent.action.PHONE_STATE")) {
                    String state = intent.getStringExtra(TelephonyManager.EXTRA_STATE);
                    if (state.equals(TelephonyManager.EXTRA_STATE_RINGING)) {
                        final String incomingNumber = intent.getStringExtra(TelephonyManager.EXTRA_INCOMING_NUMBER);
                            final BlockedContact cn = CallBlockerService.blackList.get(incomingNumber);
                            if (cn != null && cn.isBlockedForCalling()) {
                                TelephonyManager telephonyManager = (TelephonyManager) context.getSystemService(Context.TELEPHONY_SERVICE);
                                Class<?> classTelephony = Class.forName(telephonyManager.getClass().getName());
                                Method methodGetITelephony = classTelephony.getDeclaredMethod("getITelephony");
                                methodGetITelephony.setAccessible(true);
                                Object telephonyInterface = methodGetITelephony.invoke(telephonyManager);
                                Class<?> telephonyInterfaceClass = Class.forName(telephonyInterface.getClass().getName());
                                Method methodEndCall = telephonyInterfaceClass.getDeclaredMethod("endCall");
                                methodEndCall.invoke(telephonyInterface);
                            }
                        }
                    }

            } catch (NoSuchMethodException e) {
                e.printStackTrace();
            } catch (IllegalAccessException e) {
                e.printStackTrace();
            } catch (InvocationTargetException e) {
                e.printStackTrace();
            } catch (ClassNotFoundException e) {
                e.printStackTrace();
            }
        }
    }
}