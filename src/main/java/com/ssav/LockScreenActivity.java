package com.ssav;

import android.app.Activity;
import android.app.WallpaperManager;
import android.content.Intent;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;

import com.ssav.Virusprotection.ReportActivity;
import com.ssav.antitheft.Utils;

import gueei.binding.Binder;
import gueei.binding.Command;
import gueei.binding.Observable;
import gueei.binding.observables.BooleanObservable;
import gueei.binding.observables.StringObservable;
public class LockScreenActivity extends Activity {
        public static final String BlockedPackageName = "locked package name";
        public static final String BlockedActivityName = "locked activity name";
        public static final String ACTION_APPLICATION_PASSED = "com.ssav.applocker.applicationpassedtest";
        public static final String EXTRA_PACKAGE_NAME = "com.ssav.applocker.extra.package.name";
        
        @Override
        public void onCreate(Bundle savedInstanceState) {
            super.onCreate(savedInstanceState);
            Wallpaper.set(WallpaperManager.getInstance(this).getFastDrawable());
            Binder.setAndBindContentView(this, R.layout.lockscreen, this);
        }
        
        public final Observable<Drawable> Wallpaper = new Observable<Drawable>(Drawable.class);
        public final Command Number = new Command(){
                @Override
                public void Invoke(View view, Object... args) {
                        if ((args.length<1)||!(args[0] instanceof Integer)) return;
                        Integer number = (Integer)args[0];
                        Password.set(Password.get() + number);
                }
        };
        
        public final Command Clear = new Command(){
                @Override
                public void Invoke(View view, Object... args) {
                        Password.set("");
                }
        };
        public final Command Verify = new Command(){
                @Override
                public void Invoke(View view, Object... args) {
                        if (verifyPassword()){
                                Passed.set(true);
                                test_passed();
                        }else{
                                Passed.set(false);
                                Password.set("");
                        }
                }
        };
        public final BooleanObservable Passed = new BooleanObservable(false);

        private void test_passed() {
                this.sendBroadcast(
                                new Intent()
                                        .setAction(ACTION_APPLICATION_PASSED)
                                        .putExtra(
                                                        EXTRA_PACKAGE_NAME, getIntent().getStringExtra(BlockedPackageName)));
                finish();
        }
    
    public boolean verifyPassword(){
        if (Password.get() == null) return false;
        return Password.get().equals(AppLockerPreference.getInstance(this).getPassword());
    }
    
    public final StringObservable Password = new StringObservable("");
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
                return true;
            case R.id.support:
                Utils.createWebViewDialog("file:///android_asset/support.html", this);
                return true;

            case R.id.purchase:
                Utils.createWebViewDialog("file:///android_asset/purchase.html", this);
                return true;
        }
        return false;
    }

    @Override
        public void onBackPressed() {
        Intent intent = new Intent();
        intent
                .setAction(Intent.ACTION_MAIN)
                .addCategory(Intent.CATEGORY_HOME)
                .addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
        startActivity(intent);
        finish();
        }
}
