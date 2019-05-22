package com.example.qlct.activity;

import android.support.annotation.Nullable;
import android.support.design.widget.TabLayout;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;

import com.example.qlct.adapter.PagerAdapter;
import com.example.qlct.fragment.AddFragment;
import com.example.qlct.fragment.OverViewFragment;
import com.example.qlct.fragment.PeriodicFragment;
import com.example.qlct.R;
import com.example.qlct.fragment.SubFragment;

import butterknife.BindView;
import butterknife.ButterKnife;

public class MainActivity extends AppCompatActivity {

    @BindView(R.id.view_pager)
    ViewPager viewPager;
    @BindView(R.id.tab_layout)
    TabLayout tabLayout;

    private PagerAdapter adapter;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        ButterKnife.bind(this);

        setupViewPager();
        tabLayout.setupWithViewPager(viewPager);
    }

    private void setupViewPager() {
        adapter = new PagerAdapter(getSupportFragmentManager());
        adapter.addFragment(new OverViewFragment(), "Thống kê");
        adapter.addFragment(new AddFragment(), "Thu");
        adapter.addFragment(new SubFragment(), "Chi");
        adapter.addFragment(new PeriodicFragment(), "Định kỳ");
        viewPager.setAdapter(adapter);
        viewPager.setOffscreenPageLimit(4);
    }



}
