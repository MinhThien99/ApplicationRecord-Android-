package com.example.thien_record;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentPagerAdapter;
import androidx.viewpager.widget.ViewPager;

import android.content.Context;
import android.os.Bundle;
import com.astuetz.PagerSlidingTabStrip;
import com.example.thien_record.Adapter.MyAdapter;
import com.example.thien_record.Fragment.FileViewerFragment;
import com.example.thien_record.Fragment.FirstFragment;
import com.example.thien_record.Fragment.RecordFragment;
import com.google.android.material.tabs.TabLayout;

import butterknife.BindView;
import butterknife.ButterKnife;

public class start extends AppCompatActivity {


    TabLayout tabs;
    ViewPager viewPager;
     Toolbar toolbar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_start);

        tabs = findViewById(R.id.tab_layout);
        viewPager = findViewById(R.id.pager);
        toolbar = findViewById(R.id.toolbar);

        setSupportActionBar(toolbar);

        viewPager.setAdapter(new MyPagerAdapter(getSupportFragmentManager(), start.this));
        tabs.setupWithViewPager(viewPager);



    }

    public static class MyPagerAdapter extends FragmentPagerAdapter {

        private Context mContext;
        private String[] tabTitles = {"Record" , "Save Recording"};

        public MyPagerAdapter(FragmentManager fragmentManager , Context context) {
            super(fragmentManager);
            mContext = context;
        }

        // Returns total number of pages
        @Override
        public int getCount() {
            return 2;
        }

        // Returns the fragment to display for that page
        @Override
        public Fragment getItem(int position) {
            switch (position) {
                case 0:
                    return new RecordFragment();
                case 1:
                    return new FileViewerFragment();
                default:
                    return null;
            }
        }

        // Returns the page title for the top indicator
        @Override
        public CharSequence getPageTitle(int position) {
            return tabTitles[position];
        }


    }

}