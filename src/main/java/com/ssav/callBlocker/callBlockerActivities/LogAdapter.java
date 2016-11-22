package com.ssav.callBlocker.callBlockerActivities;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.graphics.Color;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;

import com.ssav.R;
import com.ssav.callBlocker.objects.BlockedActivity;

import java.util.ArrayList;

public class LogAdapter extends BaseAdapter
{

    private Activity activity;
    private ArrayList<BlockedActivity>blockedItems;

    public LogAdapter(Activity ac, ArrayList<BlockedActivity>list)
    {
        activity=ac;
        blockedItems=list;
    }

    @Override
    public int getCount()
    {
        return blockedItems.size();
    }

    @Override
    public Object getItem(int pos)
    {
        return blockedItems.get(pos);
    }

    @Override
    public long getItemId(int pos)
    {
        return 0;
    }

    @Override
    public View getView(final int position, View convertView, ViewGroup parent)
    {
        View vi=convertView;
        if(convertView == null)
        {
            LayoutInflater inflater = (LayoutInflater) activity.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            vi = inflater.inflate(R.layout.log_layout, null);
        }


        //---------------------------------------
        //Graphic Atributes
        //---------------------------------------
        final BlockedActivity blockedActivity=blockedItems.get(position);
        TextView title=(TextView)vi.findViewById(R.id.logEventTitleTxt);
        TextView message=(TextView)vi.findViewById(R.id.logEventMessageTxt);
        ImageButton deleteButton=(ImageButton)vi.findViewById(R.id.logDeleteButton);
        ImageButton actionButton=(ImageButton)vi.findViewById(R.id.logDefaultButton);
        ImageView bodyMessageImageView=(ImageView)vi.findViewById(R.id.logBodyMessageImage);
        TextView bodyMessage=(TextView)vi.findViewById(R.id.logBodyMessageTxt);

        title.setText(blockedActivity.getTitle());
        message.setText(blockedActivity.getMessage());


        deleteButton.setOnClickListener(new View.OnClickListener()
        {

            @Override
            public void onClick(View v)
            {
                AlertDialog dialog=new AlertDialog.Builder(v.getContext()).create();
                dialog.setTitle("Delete entry");
                dialog.setMessage("Are you sure you want to delete this from your log?");
                dialog.setButton(DialogInterface.BUTTON_POSITIVE, "Yes" ,new DialogInterface.OnClickListener()
                {

                    @Override
                    public void onClick(DialogInterface dialog, int which)
                    {
                        CallBlockerLogActivity.deleteFromLog(blockedActivity,position);
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


        if(blockedActivity.getTitle().startsWith("Message"))
        {
            actionButton.setImageResource(R.drawable.smsblack);
            title.setTextColor(Color.BLUE);
            bodyMessage.setVisibility(View.VISIBLE);
            bodyMessageImageView.setVisibility(View.VISIBLE);
            bodyMessage.setText(blockedActivity.getBodyMessage());
            actionButton.setOnClickListener(new View.OnClickListener()
            {

                @Override
                public void onClick(View v)
                {
                    AlertDialog dialog=new AlertDialog.Builder(v.getContext()).create();
                    dialog.setTitle("Reply message");
                    dialog.setMessage("This functionality is not available at the moment, check for updates soon!");
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
        }
        else
        {
            actionButton.setImageResource(R.drawable.phoneblack);
            title.setTextColor(Color.RED);
            bodyMessage.setVisibility(View.GONE);
            bodyMessageImageView.setVisibility(View.GONE);
            actionButton.setOnClickListener(new View.OnClickListener()
            {

                @Override
                public void onClick(View v)
                {
                    CallBlockerLogActivity.call(blockedActivity.getNumber());

                }
            });
        }

        return vi;

    }

}
