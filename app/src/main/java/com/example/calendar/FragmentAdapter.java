package com.example.calendar;

import android.content.Context;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentPagerAdapter;

/**
 * Not used
 */

public class FragmentAdapter extends FragmentPagerAdapter {

    public Context context;
    public int count;

    public FragmentAdapter(@NonNull FragmentManager fragmentManager, Context context, int count){
        super(fragmentManager);
        this.context = context;
        this.count = count;
    }

    @NonNull
    @Override
    public Fragment getItem(int position) {
        switch (position){
            case 0:
                //FragmentDaily fragmentDailyMondayAdapter = new FragmentDaily();
                //return fragmentDailyMondayAdapter;
        }
        return null;
    }

    @Override
    public int getCount() {
        return count;
    }
}
