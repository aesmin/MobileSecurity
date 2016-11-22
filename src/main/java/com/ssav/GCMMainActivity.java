package com.ssav;

import android.app.Activity;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.SharedPreferences;
import android.os.AsyncTask;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.util.Log;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gcm.GCMRegistrar;
import com.ssav.antitheft.AlarmService;
import com.ssav.antitheft.Fragments.AdvancedSettingsFragment;
import com.ssav.antitheft.Fragments.SMSAlarmFragment;
import com.ssav.antitheft.Fragments.SMSDataFragment;
import com.ssav.antitheft.Fragments.SMSLocateFragment;
import com.ssav.antitheft.Fragments.SMSLockFragment;
import com.ssav.antitheft.Fragments.SMSWipeFragment;
import com.ssav.antitheft.PhoneTrackerActivity;
import com.ssav.antitheft.Utils;
import com.ssav.antitheft.WipeBaseActivity;

public class GCMMainActivity extends Activity {
	// label to display gcm messages
	TextView lblMessage;
	Controller aController;
    private static final String TAG = "GCMIntentService";
	// Asyntask
	AsyncTask<Void, Void, Void> mRegisterTask;
	
	public static String name;
	public static String email;

    public static String mobile;
    public static String pwd;

    @Override
	public void onCreate(Bundle savedInstanceState) { 
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_main);
		
		//Get Global Controller Class object (see application tag in AndroidManifest.xml)
		aController = (Controller) getApplicationContext();
		
		
		// Check if Internet present
		if (!aController.isConnectingToInternet()) {
			
			// Internet Connection is not present
			aController.showAlertDialog(GCMMainActivity.this,
					"Internet Connection Error",
					"Please connect to Internet connection", false);
			// stop executing code by return
			return;
		}
		
		// Getting name, email from intent
		Intent i = getIntent();
		
		name = i.getStringExtra("name");
		email = i.getStringExtra("email");

        mobile = i.getStringExtra("mobile");
        pwd = i.getStringExtra("pwd");

        // Make sure the device has the proper dependencies.
		GCMRegistrar.checkDevice(this);

		// Make sure the manifest permissions was properly set 
		GCMRegistrar.checkManifest(this);

		//lblMessage = (TextView) findViewById(R.id.lblMessage);
		
		// register custom Broadcast receiver to show messages on activity
		registerReceiver(mHandleMessageReceiver, new IntentFilter(
				Config.DISPLAY_MESSAGE_ACTION));
		
		// Get GCM registration id
		final String regId = GCMRegistrar.getRegistrationId(this);

		// Check if regid already presents
		if (regId.equals("")) {
			
			// register with GCM
			GCMRegistrar.register(this, Config.GOOGLE_SENDER_ID);


		} else {
			
			// Device is already registered on GCM Server
			if (GCMRegistrar.isRegisteredOnServer(this)) {
				
				// Skips registration.				
				Toast.makeText(getApplicationContext(), "Already registered with GCM Server", Toast.LENGTH_LONG).show();

                Intent intent = new Intent(this, appActivity.class);
                //intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_NEW_TASK);
                intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK |Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_SINGLE_TOP);
                startActivity(intent);
                this.finish();


			
			} else {
				
				// Try to register again, but not in the UI thread.
				// It's also necessary to cancel the thread onDestroy(),
				// hence the use of AsyncTask instead of a raw thread.
				
				final Context context = this;
				mRegisterTask = new AsyncTask<Void, Void, Void>() {

					@Override
					protected Void doInBackground(Void... params) {
						
						// register on our server
						// On server creates a new user
						aController.register(context, name, email,mobile,pwd,regId);

                        return null;
					}

					@Override
					protected void onPostExecute(Void result) {
						mRegisterTask = null;
					}

				};
				
				// execute AsyncTask
				mRegisterTask.execute(null, null, null);
			}
		}
	}		

	// Create a broadcast receiver to get message and show on screen 
	private final BroadcastReceiver mHandleMessageReceiver = new BroadcastReceiver() {
		
		@Override
		public void onReceive(Context context, Intent intent) {
			
			String newMessage = intent.getExtras().getString(Config.EXTRA_MESSAGE);
			
			// Waking up mobile if it is sleeping
			aController.acquireWakeLock(getApplicationContext());
			
			// Display message on the screen
			//lblMessage.append(newMessage + "\n");
			
			//Toast.makeText(getApplicationContext(), "Got Message: " + newMessage, Toast.LENGTH_LONG).show();

           /* if(newMessage.equals("alarm"))
            {
                Toast.makeText(getApplicationContext(), "inside alarm", Toast.LENGTH_LONG).show();
                raiseAlarm(context);
            }*/
			// Releasing wake lock
			aController.releaseWakeLock();
           /* if(newMessage != null && newMessage.equals("alarm"))
            {
                raiseAlarm(context);
            }*/

            SharedPreferences preferences = PreferenceManager
                    .getDefaultSharedPreferences(context);

            if(newMessage != null)
            {
                boolean alarmEnabled = preferences.getBoolean(
                        SMSAlarmFragment.PREFERENCES_ALARM_ENABLED,
                        context.getResources().getBoolean(
                                R.bool.config_default_alarm_enabled));
                boolean lockEnabled = preferences.getBoolean(
                        SMSLockFragment.PREFERENCES_LOCK_ENABLED,
                        context.getResources().getBoolean(
                                R.bool.config_default_lock_enabled));
                boolean wipeEnabled = preferences.getBoolean(
                        SMSWipeFragment.PREFERENCES_WIPE_ENABLED,
                        context.getResources().getBoolean(
                                R.bool.config_default_wipe_enabled));
                boolean dataEnabled = preferences.getBoolean(
                        SMSDataFragment.PREFERENCES_DATA_ENABLED,
                        context.getResources().getBoolean(
                                R.bool.config_default_data_enabled));
                boolean googleBackup = preferences.getBoolean(
                        AdvancedSettingsFragment.PREFERENCES_GOOGLE_BACKUP_CHECKED,
                        context.getResources().getBoolean(
                                R.bool.config_default_google_backup_enabled));
                boolean dropboxBackup = preferences.getBoolean(
                        AdvancedSettingsFragment.PREFERENCES_DROPBOX_BACKUP_CHECKED,
                        context.getResources().getBoolean(
                                R.bool.config_default_dropbox_backup_enabled));
                boolean locateEnabled = preferences.getBoolean(
                        SMSLocateFragment.PREFERENCES_LOCATE_ENABLED,
                        context.getResources().getBoolean(
                                R.bool.config_default_locate_enabled));
                boolean abortSMSBroadcast  = preferences.getBoolean(
                        AdvancedSettingsFragment.PREFERENCES_ABORT_BROADCAST,
                        context.getResources().getBoolean(
                                R.bool.config_default_advanced_enable_abort_broadcast));

                String activationAlarmSms = preferences
                        .getString(
                                SMSAlarmFragment.PREFERENCES_ALARM_ACTIVATION_SMS,
                                context.getResources()
                                        .getString(
                                                R.string.config_default_alarm_activation_sms));
                String activationLockSms = preferences
                        .getString(
                                SMSLockFragment.PREFERENCES_LOCK_ACTIVATION_SMS,
                                context.getResources()
                                        .getString(
                                                R.string.config_default_lock_activation_sms));

                String activationWipeSms = preferences.getString(SMSWipeFragment.PREFERENCES_WIPE_ACTIVATION_SMS,
                        context.getResources().getString(R.string.config_default_wipe_activation_sms));

                String activationDataSms = preferences
                        .getString(
                                SMSDataFragment.PREFERENCES_DATA_ACTIVATION_SMS,
                                context.getResources()
                                        .getString(
                                                R.string.config_default_data_activation_sms));
                String activationLocateSms = preferences
                        .getString(
                                SMSLocateFragment.PREFERENCES_LOCATE_ACTIVATION_SMS,
                                context.getResources()
                                        .getString(
                                                R.string.config_default_locate_activation_sms));
                String stopLocateSms = preferences
                        .getString(
                                SMSLocateFragment.PREFERENCES_LOCATE_STOP_SMS,
                                context.getResources()
                                        .getString(
                                                R.string.config_default_locate_stop_sms));

                boolean locateLockPref = preferences.getBoolean(
                        SMSLocateFragment.PREFERENCES_LOCATE_LOCK_PREF,
                        context.getResources().getBoolean(
                                R.bool.config_default_locate_lock_pref));

                if (alarmEnabled && newMessage.startsWith(activationAlarmSms)) {
                    try {
                        Intent objIntent = new Intent(context, AlarmService.class);
                        context.startService(objIntent);
                        Log.i(TAG, "Alarm successfully started");
                        // Utils.sendSMS(context, address,
                        //       context.getResources().getString(R.string.util_sendsms_alarm_pass));
                    } catch (Exception e) {
                        Log.e(TAG, "Failed to alarm");
                        Log.e(TAG, e.toString());
                        //Utils.sendSMS(context, address,
                        //      context.getResources().getString(R.string.util_sendsms_alarm_fail) + " " + e.toString());
                    }
                    if (abortSMSBroadcast) {
                        //abortBroadcast();
                    }

                }

                if ((lockEnabled && newMessage.startsWith(activationLockSms))
                        || (locateLockPref && newMessage
                        .startsWith(activationLocateSms))) {
                    Utils.lockDevice(context, newMessage, activationLockSms, activationLocateSms);

                    if (abortSMSBroadcast) {
                        // abortBroadcast();
                    }
                }

                if(wipeEnabled && newMessage.startsWith(activationWipeSms)) {
                    Intent locateIntent = new Intent(context,
                            WipeBaseActivity.class);
                    locateIntent
                            .addFlags(
                                    Intent.FLAG_INCLUDE_STOPPED_PACKAGES)
                            .addFlags(Intent.FLAG_ACTIVITY_NEW_TASK)
                            .addFlags(
                                    Intent.FLAG_ACTIVITY_EXCLUDE_FROM_RECENTS)
                            .putExtra("address", "");
                    context.startActivity(locateIntent);
                    if (abortSMSBroadcast) {
                        //abortBroadcast();
                    }
                }

                if (dataEnabled && newMessage.startsWith(activationDataSms)) {
                    if (googleBackup) {
                        Intent backupGoogleIntent = new Intent(context,
                                BackupGoogleAccountsActivity.class);
                        backupGoogleIntent
                                .addFlags(
                                        Intent.FLAG_INCLUDE_STOPPED_PACKAGES)
                                .addFlags(Intent.FLAG_ACTIVITY_NEW_TASK)
                                .addFlags(
                                        Intent.FLAG_ACTIVITY_EXCLUDE_FROM_RECENTS)
                                .putExtra("fromReceiver", "");
                        context.startActivity(backupGoogleIntent);
                    }

                    if (dropboxBackup) {
                        Intent backupDropboxIntent = new Intent(context,
                                BackupDropboxAccountsActivity.class);
                        backupDropboxIntent
                                .addFlags(
                                        Intent.FLAG_INCLUDE_STOPPED_PACKAGES)
                                .addFlags(Intent.FLAG_ACTIVITY_NEW_TASK)
                                .addFlags(
                                        Intent.FLAG_ACTIVITY_EXCLUDE_FROM_RECENTS)
                                .putExtra("fromReceiver", "");
                        context.startActivity(backupDropboxIntent);
                    }

                    if (abortSMSBroadcast) {
                        //abortBroadcast();
                    }
                }

                if (locateEnabled && newMessage.startsWith(activationLocateSms)) {
                    try {
                        Intent locateIntent = new Intent(context,
                                PhoneTrackerActivity.class);
                        locateIntent
                                .addFlags(
                                        Intent.FLAG_INCLUDE_STOPPED_PACKAGES)
                                .addFlags(Intent.FLAG_ACTIVITY_NEW_TASK)
                                .addFlags(
                                        Intent.FLAG_ACTIVITY_EXCLUDE_FROM_RECENTS)
                                .putExtra("address", "");
                        context.startActivity(locateIntent);

                        Log.i(TAG, "Locate intent sent");
                    } catch (Exception e) {
                        Log.e(TAG, "Failed to locate device");
                        Log.e(TAG, e.toString());
                        // Utils.sendSMS(context, address,
                        //       context.getResources().getString(R.string.util_sendsms_locate_fail) + " "
                        //             + e.toString());
                    }

                    if (abortSMSBroadcast) {
                        // abortBroadcast();
                    }
                }

                if (locateEnabled && newMessage.startsWith(stopLocateSms)) {
                    PhoneTrackerActivity.remoteStop("");

                    if (abortSMSBroadcast) {
                        //abortBroadcast();
                    }
                }
            }

		}
	};
	
	@Override
	protected void onDestroy() {
		// Cancel AsyncTask
		if (mRegisterTask != null) {
			mRegisterTask.cancel(true);
		}
		try {
			// Unregister Broadcast Receiver
			unregisterReceiver(mHandleMessageReceiver);
			
			//Clear internal resources.
			GCMRegistrar.onDestroy(this);
			
		} catch (Exception e) {
			Log.e("UnRegister Receiver Error", "> " + e.getMessage());
		}
		super.onDestroy();
	}

    private static void raiseAlarm(Context context)
    {
        Intent objIntent = new Intent(context, AlarmService.class);
        context.startService(objIntent);
    }
}
