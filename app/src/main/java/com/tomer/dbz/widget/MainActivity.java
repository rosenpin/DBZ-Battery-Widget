package com.tomer.dbz.widget;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.view.ViewPager;
import android.support.v7.app.ActionBarActivity;
import android.support.v7.widget.AppCompatSeekBar;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.ImageView;
import android.widget.SeekBar;
import android.widget.Toast;

import java.util.Random;


public class MainActivity extends ActionBarActivity {
    static SharedPreferences sharedPreferences;
    int position;
    Context context;
    SectionsPagerAdapter mSectionsPagerAdapter;

    ViewPager mViewPager;

    private static int[][] PICS() {
        if (sharedPreferences.getBoolean("gt", false)) {
            return Resources.gt_pics;
        } else {
            return Resources.pics;
        }
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        context = this;
        sharedPreferences = PreferenceManager.getDefaultSharedPreferences(this);
        mSectionsPagerAdapter = new SectionsPagerAdapter(getSupportFragmentManager());

        com.tomer.dbz.widget.CircleButton button = (com.tomer.dbz.widget.CircleButton) findViewById(R.id.circle_but);
        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                sharedPreferences.edit().putInt("selected", position).apply();
                Toast.makeText(getApplicationContext(), "Character selected", Toast.LENGTH_SHORT).show();
            }
        });
        toggleCb(R.id.percent_cb, "percent");
        toggleCb(R.id.time_cb, "time");
        toggleCb(R.id.gt_cb, "gt");
        toggleCb(R.id.white_font, "whitefont");
        ((AppCompatSeekBar) findViewById(R.id.min_battery)).setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
            @Override
            public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
                sharedPreferences.edit().putInt("min_battery", progress).apply();
            }

            @Override
            public void onStartTrackingTouch(SeekBar seekBar) {

            }

            @Override
            public void onStopTrackingTouch(SeekBar seekBar) {

            }
        });
        ((AppCompatSeekBar) findViewById(R.id.min_battery)).setProgress(sharedPreferences.getInt("min_battery", 0));
        mViewPager = (ViewPager) findViewById(R.id.pager);
        mViewPager.setAdapter(mSectionsPagerAdapter);
        mViewPager.setCurrentItem(sharedPreferences.getInt("selected", 0));
        mViewPager.setOnPageChangeListener(new ViewPager.OnPageChangeListener() {
            @Override
            public void onPageScrolled(int i, float v, int i2) {
            }

            @Override
            public void onPageSelected(int i) {
                position = i;
            }

            @Override
            public void onPageScrollStateChanged(int i) {

            }
        });
    }

    @Override
    protected void onPause() {
        super.onPause();
        finish();
    }

    @Override
    protected void onStop() {
        super.onStop();
        finish();
    }

    public void toggleCb(int id, final String sp) {
        CheckBox cb = (CheckBox) findViewById(id);
        cb.setChecked(sharedPreferences.getBoolean(sp, true));
        cb.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                sharedPreferences.edit().putBoolean(sp, isChecked).apply();
                if (sp.equals("gt")) {
                    finish();
                    startActivity(new Intent(getApplicationContext(), MainActivity.class));
                }
            }
        });
    }

    public static class PlaceholderFragment extends Fragment {
        int position;

        @SuppressLint("ValidFragment")
        public PlaceholderFragment(int position) {
            this.position = position;
        }

        public PlaceholderFragment() {
        }

        @Override
        public View onCreateView(LayoutInflater inflater, ViewGroup container,
                                 Bundle savedInstanceState) {
            View rootView = inflater.inflate(R.layout.fragment_main, container, false);
            ImageView img = (ImageView) rootView.findViewById(R.id.exImage);
            img.setImageResource(PICS()[position][new Random().nextInt(PICS()[position].length) % 2]);
            return rootView;
        }
    }

    public class SectionsPagerAdapter extends FragmentPagerAdapter {

        public SectionsPagerAdapter(FragmentManager fm) {
            super(fm);
        }

        @Override
        public Fragment getItem(int position) {
            System.out.println(String.valueOf(position));
            return new PlaceholderFragment(position);
        }

        @Override
        public int getCount() {
            return PICS().length;
        }
    }
}
