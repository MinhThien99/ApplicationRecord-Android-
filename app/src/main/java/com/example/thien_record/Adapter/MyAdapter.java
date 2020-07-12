package com.example.thien_record.Adapter;

import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentPagerAdapter;

import com.example.thien_record.Fragment.FileViewerFragment;
import com.example.thien_record.Fragment.RecordFragment;

public class MyAdapter extends FragmentPagerAdapter {

    String[] titles = {"Record" , "Saved Recording "};
    private static int NUM_ITEMS = 3;

    public MyAdapter(FragmentManager fm) {
        super(fm);
    }

    @Override
    public Fragment getItem(int position) {
        return new RecordFragment();
    }


    @Override
    public int getCount() {
        return 1;
    }


    /*

    private String[] titles = {"Record" , "Saved Recording "};

    public MyAdapter(FragmentManager fm ) {

        super(fm );
    }

    @Override
    public Fragment getItem(int position) {
        switch (position) {
            case 0: {
                return new RecordFragment();
            }
            case 1: {
                return new FileViewerFragment();
            }
        }
        return null;
    }

    @Override
    public int getCount() {
        return titles.length;
    }

    @Nullable
    @Override
    public CharSequence getPageTitle(int position) {

        return titles[position];
    }

     */

}
