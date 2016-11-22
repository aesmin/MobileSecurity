package com.ssav.callBlocker.callBlockerActivities;

import java.io.FileOutputStream;
import java.io.ObjectOutputStream;
import java.util.ArrayList;

import com.ssav.AppRegisterActivity;
import com.ssav.LicenseInfo;
import com.ssav.R;
import com.ssav.Virusprotection.ReportActivity;
import com.ssav.antitheft.Utils;
import com.ssav.callBlocker.notificationCenter.CallBlockerToastNotification;
import com.ssav.callBlocker.objects.BlockedContact;

import android.net.Uri;
import android.os.Bundle;
import android.provider.ContactsContract;
import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.graphics.drawable.GradientDrawable;
import android.graphics.drawable.GradientDrawable.Orientation;
import android.util.Log;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.ListView;
import android.widget.Toast;

public class CallBlockerBlacklistViewActivity extends Activity
{

    private static ListView blacklistView;
    private Button addToBlacklistButton;
    private static Activity instance;
    int size;


    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        //setTheme(CallBlockerMainActivity.theme);

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_call_blocker_blacklist_view);

        instance=this;
        blacklistView=(ListView)findViewById(R.id.blacklistListView);

        int[] colors = {0, 0xFF00FFFF, 0};
        blacklistView.setDivider(new GradientDrawable(Orientation.RIGHT_LEFT, colors));
        blacklistView.setDividerHeight(3);

        ArrayList<BlockedContact>list=new ArrayList<BlockedContact>();
        list.addAll(CallBlockerMainActivity.blackList.values());
        BlockedContactAdapter adapter=new BlockedContactAdapter(this, list);
        blacklistView.setAdapter(adapter);

        addToBlacklistButton=(Button)findViewById(R.id.addToBlacklistButton);
        addToBlacklistButton.setOnClickListener(new View.OnClickListener()
        {

            @Override
            public void onClick(View v)
            {
                selectContactFromDevice();
            }
        });
        size=adapter.getCount();
        Toast.makeText(getBaseContext(),"Size="+size,Toast.LENGTH_SHORT).show();
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
            case R.id.support:
                Utils.createWebViewDialog("file:///android_asset/support.html", this);
                return true;

            case R.id.purchase:
                Utils.createWebViewDialog("file:///android_asset/purchase.html", this);
                return true;
        }
        return false;
    }

    public void selectContactFromDevice()
    {
        Intent i=new Intent(Intent.ACTION_GET_CONTENT);
        i.setType(ContactsContract.CommonDataKinds.Phone.CONTENT_ITEM_TYPE);
        startActivityForResult(i, 1);
    }

        @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data)
    {
        if(data!=null)
        {
            Uri uri=data.getData();
            if(uri!=null)
            {
                Cursor c=null;
                try
                {
                    c=getContentResolver().query(uri, new String[]{ContactsContract.CommonDataKinds.Phone.DISPLAY_NAME,ContactsContract.CommonDataKinds.Phone.NUMBER,ContactsContract.CommonDataKinds.Phone.TYPE}, null, null, null);
                    if(c!=null && c.moveToFirst())
                    {
                        String name=c.getString(0);
                        String number=c.getString(1).replaceAll("[ ( | ) | \\- ]", "");
                        int type=c.getInt(2);
                        BlockedContact cn=new BlockedContact(name, number, type, true, true);

                        addContactToBlackList(cn);

                    }
                }
                finally
                {
                    if(c!=null)
                    {
                        c.close();
                    }
                }
            }
        }
    }

    public void addContactToBlackList(BlockedContact cn)
    {
     CallBlockerMainActivity.blackList.put(cn.getNumber(), cn);
        CallBlockerToastNotification.showDefaultShortNotification("Contact added to blacklist successfully");
        updateListData();
    }

    public void saveData()
    {
        try
        {
            FileOutputStream fos=openFileOutput("CallBlocker.data", Context.MODE_PRIVATE);
            ObjectOutputStream oos=new ObjectOutputStream(fos);
            oos.writeObject(CallBlockerMainActivity.blackList);
            fos.close();
            oos.close();
            CallBlockerToastNotification.showDefaultShortNotification("Saving Data...");
        }
        catch (Exception e)
        {
            Log.e("Error", e.getMessage());
        }
    }

    public static void updateListData()
    {
        ArrayList<BlockedContact>list=new ArrayList<BlockedContact>();
        list.addAll(CallBlockerMainActivity.blackList.values());
        BlockedContactAdapter adapter=new BlockedContactAdapter(instance, list);
        blacklistView.setAdapter(adapter);
    }

    @Override
    protected void onDestroy()
    {
        saveData();
        super.onDestroy();
    }
}
