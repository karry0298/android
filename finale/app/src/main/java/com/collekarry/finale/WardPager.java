package com.collekarry.finale;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;

public class WardPager extends FragmentPagerAdapter {

    int tabCount;
    wardClass ward;


    public WardPager(FragmentManager fm, wardClass ward) {
        super(fm);
        this.tabCount = 3;
        this.ward = ward;
    }

    @Override
    public Fragment getItem(int position) {
        switch (position){
            case 0:
//                Fragment tab1 = new HeartRateFragment();
                return HeartRateFragment.newInstance("","");
            case 1:
//                MedicationFragment tab2 = new MedicationFragment();
                return MedicationFragment.newInstance(ward);
            case 2:
//                AppointmentsFragment tab3 = new AppointmentsFragment();
                return HistoryFragment.newInstance(ward);
            default :
                return null;
        }
    }

    @Override
    public int getCount() {
        return 3;
    }




}
