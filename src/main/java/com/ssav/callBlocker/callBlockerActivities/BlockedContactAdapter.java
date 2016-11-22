package com.ssav.callBlocker.callBlockerActivities;
import android.app.Activity;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageButton;
import android.widget.TextView;

import com.ssav.R;
import com.ssav.callBlocker.notificationCenter.CallBlockerToastNotification;
import com.ssav.callBlocker.objects.BlockedContact;

import java.util.ArrayList;
public class BlockedContactAdapter extends BaseAdapter
{
    private Activity activity;
    private ArrayList<BlockedContact>contacts;

    public BlockedContactAdapter(Activity activity, ArrayList<BlockedContact>contacts)
    {
        this.activity=activity;
        this.contacts=contacts;
    }

    @Override
    public int getCount()
    {
        return contacts.size();
    }

    @Override
    public Object getItem(int position)
    {
        return contacts.get(position);
    }

    @Override
    public long getItemId(int position)
    {
        return 0;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent)
    {
        View vi=convertView;
        if(convertView == null)
        {
            LayoutInflater inflater = (LayoutInflater) activity.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            vi = inflater.inflate(R.layout.blacklist_layout, null);
       }

        final BlockedContact cn=contacts.get(position);
        TextView name=(TextView)vi.findViewById(R.id.blacklistNameTxt);
        TextView number=(TextView)vi.findViewById(R.id.blacklistNumberTxt);

        name.setText(cn.getName());
        number.setText(cn.getNumber());

        final ImageButton callButton=(ImageButton)vi.findViewById(R.id.blacklistCallButton);
        final ImageButton smsButton=(ImageButton)vi.findViewById(R.id.blacklistSmsButton);
        final ImageButton deleteButton=(ImageButton)vi.findViewById(R.id.blacklistDeleteButton);

        if(cn.isBlockedForCalling())
        {
            callButton.setImageResource(R.drawable.phonered);
        }
        if(cn.isBlockedForMessages())
        {
            smsButton.setImageResource(R.drawable.smsred);
        }

        callButton.setOnClickListener(new View.OnClickListener()
        {

            @Override
            public void onClick(View v)
            {
                if(cn.isBlockedForCalling()==true)
                {
                    cn.setBlockedForCalling(false);
                    callButton.setImageResource(R.drawable.phoneblack);
                    CallBlockerToastNotification.showDefaultShortNotification(cn.getName()+" can call you");
                }
                else
                {
                    cn.setBlockedForCalling(true);
                    callButton.setImageResource(R.drawable.phonered);
                    CallBlockerToastNotification.showDefaultShortNotification(cn.getName()+" can not call you");
                }
            }
        });

        smsButton.setOnClickListener(new View.OnClickListener()
        {

            @Override
            public void onClick(View v)
            {
                if(cn.isBlockedForMessages()==true)
                {
                    cn.setBlockedForMessages(false);
                    smsButton.setImageResource(R.drawable.smsblack);
                    CallBlockerToastNotification.showDefaultShortNotification(cn.getName()+" can text you");
                }
                else
                {
                    cn.setBlockedForMessages(true);
                    smsButton.setImageResource(R.drawable.smsred);
                    CallBlockerToastNotification.showDefaultShortNotification(cn.getName()+" can not text you");
                }
            }
        });

        deleteButton.setOnClickListener(new View.OnClickListener()
        {

            @Override
            public void onClick(View v)
            {
                AlertDialog dialog=new AlertDialog.Builder(v.getContext()).create();
                dialog.setTitle("Delete contact");
                dialog.setMessage("Are you sure you want to delete this contact from blacklist?");
                dialog.setButton(DialogInterface.BUTTON_POSITIVE, "Yes" ,new DialogInterface.OnClickListener()
                {

                    @Override
                    public void onClick(DialogInterface dialog, int which)
                    {
                        CallBlockerMainActivity.blackList.remove(cn.getNumber());
                        CallBlockerBlacklistViewActivity.updateListData();
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
        });

        return vi;
    }

}
