package com.ssav.callBlocker.messageListener;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.telephony.SmsMessage;
import android.util.Log;
import android.widget.Toast;

import com.ssav.callBlocker.callBlockerService.CallBlockerService;
import com.ssav.callBlocker.objects.BlockedContact;

import java.io.OutputStreamWriter;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;
public class SmsReceiver extends BroadcastReceiver
{

    /*@Override
    public void onReceive(Context context, Intent intent)
    {
        this.context=context;
        //blockSms(intent);
    }
    public void blockSms(Intent intent)
    {
        *//*Bundle bundle = intent.getExtras();
        final SmsMessage[] msgs;
        if (bundle != null)
        {
            Object[] pdus = (Object[]) bundle.get("pdus");
            msgs = new SmsMessage[pdus.length];
            for (int i=0; i<msgs.length; i++)
            {
                msgs[i] = SmsMessage.createFromPdu((byte[])pdus[i]);
            }
            final BlockedContact cn=CallBlockerService.blackList.get(msgs[0].getOriginatingAddress());
            if(cn!=null && cn.isBlockedForMessages())
            {
                abortBroadcast();*//*
                Thread t=new Thread(new Runnable()
                {

                    @Override
                    public void run()
                    {
                        DateFormat dateFormat=new SimpleDateFormat("yyyy/MM/dd - HH:mm:ss");
                        Date date=new Date();
                        String currentDate=dateFormat.format(date);
                        //LOG FORMAT --> TITLE;;MESSAGE;;NAME;;NUMBER;;HOUR;;BODYMESSAGE;;SEPARATOR
                        String message="Message Blocked;;A message from "+cn.getName()+" ("+msgs[0].getOriginatingAddress()+") was blocked at "+currentDate+";;"+cn.getName()+";;"+msgs[0].getOriginatingAddress()+";;"+currentDate+";;"+msgs[0].getMessageBody()+";;\r\n";
                        writeInLog(message);
                    }
                });
                t.start();
            }
        }
    }*/

    @Override
    public void onReceive(Context context, Intent intent) {
        if (intent.getAction().equals("android.provider.Telephony.SMS_RECEIVED")) {
            Bundle extras = intent.getExtras();
            if (extras != null) {
                Object[] pdus = (Object[]) extras.get("pdus");

                if (pdus.length < 1) return; // Invalid SMS. Not sure that it's possible.
                StringBuilder sb = new StringBuilder();
                String sender = null;
                for (int i = 0; i < pdus.length; i++) {
                    SmsMessage message = SmsMessage.createFromPdu((byte[]) pdus[i]);
                    if (sender == null) sender = message.getOriginatingAddress();
                    String text = message.getMessageBody();
                    if (text != null) sb.append(text);
                }
                abortBroadcast();
            }
                return;
            }
        }


    }