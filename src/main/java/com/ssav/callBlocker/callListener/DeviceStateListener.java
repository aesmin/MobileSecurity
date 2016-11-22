/*

package com.ssav.callBlocker.callListener;

import android.content.Context;
import android.os.RemoteException;
import android.telephony.PhoneStateListener;
import android.telephony.TelephonyManager;
import android.util.Log;
import android.widget.Toast;

import com.ssav.android.internal.telephony.ITelephony;
import com.ssav.callBlocker.callBlockerService.CallBlockerService;
import com.ssav.callBlocker.objects.BlockedContact;

import java.io.OutputStreamWriter;
import java.lang.reflect.Method;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;

public class DeviceStateListener extends PhoneStateListener {
    private ITelephony telephonyService;
    private Context context;


*/
/*
 @Override
    public void onCallStateChanged(int state, String incomingNumber) {
        switch (state) {

            case TelephonyManager.CALL_STATE_RINGING:
                TelephonyManager telephonyManager = (TelephonyManager) context.getSystemService(Context.TELEPHONY_SERVICE);
                try {
                    final BlockedContact block_number=CallBlockerService.blackList.get(incomingNumber);
                    Toast.makeText(context, "in"+block_number, Toast.LENGTH_LONG).show();
                    Class clazz = Class.forName(telephonyManager.getClass().getName());
                    Method method = clazz.getDeclaredMethod("getITelephony");
                    method.setAccessible(true);
                    ITelephony telephonyService = (ITelephony) method.invoke(telephonyManager);

                    //Checking incoming call number
                    System.out.println("Call "+block_number);
                    if (incomingNumber.equalsIgnoreCase("+91"+block_number)) {
                        //telephonyService.silenceRinger();//Security exception problem
                        telephonyService = (ITelephony) method.invoke(telephonyManager);
                        telephonyService.silenceRinger();
                         telephonyService.endCall();
                    }
                } catch (Exception e) {
                    Toast.makeText(context, e.toString(), Toast.LENGTH_LONG).show();
                }
                //Turn OFF the mu
                break;
            case PhoneStateListener.LISTEN_CALL_STATE:
        }
        super.onCallStateChanged(state, incomingNumber);
    }
}
*//*




    private void initializeTelephonyService() {
        try {
            TelephonyManager telephonyManager = (TelephonyManager) context.getSystemService(Context.TELEPHONY_SERVICE);
            Class clase = Class.forName(telephonyManager.getClass().getName());
            Method method = clase.getDeclaredMethod("getITelephony");
            method.setAccessible(true);
            telephonyService = (ITelephony) method.invoke(telephonyManager);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Override
    public void onCallStateChanged(int state, final String incomingNumber) {
        initializeTelephonyService();
         if (state == TelephonyManager.CALL_STATE_RINGING)
            {
            final BlockedContact cn = CallBlockerService.blackList.get(incomingNumber);

            if (cn != null && cn.isBlockedForCalling()) {
                try {

                    telephonyService.endCall();
*/
/*                  telephonyService.silenceRinger();
                      Thread t = new Thread(new Runnable() {
                            @Override
                            public void run() {
                                DateFormat dateFormat = new SimpleDateFormat("yyyy/MM/dd - HH:mm:ss");
                                Date date = new Date();
                                String currentDate = dateFormat.format(date);
                                //LOG FORMAT --> TITLE;;MESSAGE;;NAME;;NUMBER;;HOUR;;BODYMESSAGE(NULL);;SEPARATOR
                                String message = "Call Blocked;;A call from " + cn.getName() + " (" + incomingNumber + ") was blocked at " + currentDate + ";;" + cn.getName() + ";;" + incomingNumber + ";;" + currentDate + ";;NULL;;\r\n";
                                writeInLog(message);

                            }
                        });
                        t.start();*//*

                } catch (RemoteException e) {
                    e.printStackTrace();
                }
            }


        }
    }

    public void writeInLog(String message) {
        try {
            OutputStreamWriter fos = new OutputStreamWriter(context.openFileOutput("CallLog.txt", Context.MODE_APPEND));
            fos.append(message);
            fos.close();
            System.out.println("Writed in log succesfully");
        } catch (Exception e) {
            Log.e("Error", e.getMessage());
        }

    }
}






*/
