package com.ssav.Virusprotection;
import android.preference.ListPreference;
import android.view.View;
import android.view.ViewGroup;
import android.content.Context;
import android.util.AttributeSet;
/**
 * Created by Minaz on 2/5/14.
 */
public class EnumPreference extends ListPreference {

    public EnumPreference(Context aContext, AttributeSet attrs) {
        super(aContext,attrs);
    }

    @Override
    protected View onCreateView(ViewGroup parent) {
        //setSummary(getEntry());
        return super.onCreateView(parent);
    }

    @Override
    protected boolean persistString(String aNewValue) {
        if (super.persistString(aNewValue)) {
            //setSummary(getEntry());
            notifyChanged();
            return true;
        } else {
            return false;
        }
    }
}