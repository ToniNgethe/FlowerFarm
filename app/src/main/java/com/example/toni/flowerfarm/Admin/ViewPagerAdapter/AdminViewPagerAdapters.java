package com.example.toni.flowerfarm.Admin.ViewPagerAdapter;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentStatePagerAdapter;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by toni on 3/26/17.
 */

public class AdminViewPagerAdapters extends FragmentStatePagerAdapter {

    private List<Fragment> fragmentList = new ArrayList<>();
    private List<String> title = new ArrayList<>();

    public AdminViewPagerAdapters(FragmentManager fm) {
        super(fm);
    }

    @Override
    public Fragment getItem(int position) {
        return fragmentList.get(position);
    }

    @Override
    public int getCount() {
        return fragmentList.size();
    }

    @Override
    public CharSequence getPageTitle(int position) {
        return title.get(position);
    }

    public void addFragment(Fragment m, String t){
        fragmentList.add(m);
        title.add(t);
    }
}
