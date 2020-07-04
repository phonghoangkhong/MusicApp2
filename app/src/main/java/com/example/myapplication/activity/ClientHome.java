package com.example.myapplication.activity;

import android.os.Bundle;

import androidx.appcompat.app.AppCompatActivity;
import androidx.viewpager.widget.ViewPager;

import com.example.myapplication.Adapter.ViewPagerAdapter;
import com.example.myapplication.R;
import com.example.myapplication.fragment.ListenMusicFragment;
import com.example.myapplication.fragment.LoveSongFragment;
import com.google.android.material.tabs.TabLayout;

public class ClientHome extends AppCompatActivity {
    ViewPager viewPager;
    TabLayout tabLayout;
    public ListenMusicFragment listenMusicFragment;

    public LoveSongFragment loveSongFragment;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_trang_chu2);
        viewPager = (ViewPager) findViewById(R.id.view_pager);
        tabLayout = (TabLayout) findViewById(R.id.tab_layout);

        viewPager = (ViewPager) findViewById(R.id.view_pager);
        setupViewPager(viewPager);

        tabLayout = (TabLayout) findViewById(R.id.tab_layout);
        tabLayout.setupWithViewPager(viewPager);
        setupTabIcons();
    }
    private void setupTabIcons() {
        tabLayout.getTabAt(1).setIcon(R.drawable.love);
        tabLayout.getTabAt(0).setIcon(R.drawable.listenmusic);

    }

    private void setupViewPager(ViewPager viewPager) {
        String user=getIntent().getStringExtra("user");
        listenMusicFragment = new ListenMusicFragment(user);
        loveSongFragment = new LoveSongFragment(user);
        ViewPagerAdapter adapter = new ViewPagerAdapter(getSupportFragmentManager());
        adapter.addFrag(listenMusicFragment, "");
        adapter.addFrag(loveSongFragment, "");

        viewPager.setAdapter(adapter);
    }
}
