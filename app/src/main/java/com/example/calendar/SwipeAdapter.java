package com.example.calendar;

import android.content.Context;
import android.os.Bundle;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentStatePagerAdapter;
import androidx.fragment.app.FragmentTransaction;
import androidx.viewpager.widget.PagerAdapter;

import java.util.List;

/**
 * Not used
 */

public class SwipeAdapter extends PagerAdapter {

    private Context context;
    private List<Course> courseList;

    public SwipeAdapter(Context context){
        this.context = context;
        //this.courseList = courseList;
    }

    @Override
    public int getCount() {
        return 5;
    }

    @Override
    public boolean isViewFromObject(@NonNull View view, @NonNull Object object) {
        return false;
    }

    @NonNull
    @Override
    public Object instantiateItem(@NonNull ViewGroup container, int position) {

        Fragment pageFragment = new TestFrag();
        Bundle data = new Bundle();
        /*
        data.putString("day", dataDateDay);
        data.putString("dateFile", dataDateFile);
        data.putString("firstname", dataFirstName);
        data.putString("lastname", dataLastName);
        pageFragment.setArguments(data);

         */
        return pageFragment;

        //return super.instantiateItem(container, position);
    }

}
