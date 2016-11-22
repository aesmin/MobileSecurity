package com.ssav.callBlocker.callBlockerActivities;

import com.ssav.R;
import com.ssav.callBlocker.notificationCenter.CallBlockerToastNotification;
import android.content.SharedPreferences;
import android.content.SharedPreferences.OnSharedPreferenceChangeListener;
import android.os.Bundle;
import android.preference.PreferenceActivity;
import android.preference.PreferenceManager;

public class SettingsActivity extends PreferenceActivity
{

    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
       // setTheme(CallBlockerMainActivity.theme);
        SharedPreferences prefs = PreferenceManager.getDefaultSharedPreferences(this);

        OnSharedPreferenceChangeListener listener = new SharedPreferences.OnSharedPreferenceChangeListener()
        {

            @Override
            public void onSharedPreferenceChanged(SharedPreferences sharedPreferences,
                                                  String key)
            {
                CallBlockerToastNotification.showDefaultShortNotification("This change will apply when application is restarted");

            }
        };

        prefs.registerOnSharedPreferenceChangeListener(listener);
        addPreferencesFromResource(R.xml.call_blocker_preferences);
    }
}
