package com.ssav.Virusprotection;

import android.app.Activity;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.ssav.R;

import java.util.ArrayList;

/**
 * Created by Minaz on 2/8/14.
 */

public class ReportAdapter extends BaseAdapter
{

    private Activity activity;
    private ArrayList<ScanParameter> blockedItems;

    public ReportAdapter(Activity ac, ArrayList<ScanParameter>list)
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
            vi = inflater.inflate(R.layout.scanreport_layout, null);
        }


        //---------------------------------------
        //Graphic Atributes
        //---------------------------------------
        final ScanParameter blockedActivity=blockedItems.get(position);
        TextView time=(TextView)vi.findViewById(R.id.textViewTime);
        TextView item=(TextView)vi.findViewById(R.id.textViewScanItem);
        TextView data=(TextView)vi.findViewById(R.id.textViewMetaData);


        time.setText(blockedActivity.getScanStartTime());
        item.setText(blockedActivity.getTitle());

        data.setText(blockedActivity.getTitle() + ";;" + blockedActivity.getScanStartTime() + ";;" + blockedActivity.getScanEndTime()+";;"+ blockedActivity.getNumberOfFilesScanned() + ";;" + blockedActivity.getStatus());


        return vi;

    }

}

