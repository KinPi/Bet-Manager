package com.kin.betmanager.adapters;

import android.content.Context;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.util.Log;

import com.kin.betmanager.BetsListFragment;
import com.kin.betmanager.R;

/**
 * Created by Kin on 3/3/18.
 */

public class SectionPagerAdapter extends FragmentPagerAdapter {

    private Context context = null;

    public SectionPagerAdapter (Context context, FragmentManager fragmentManager) {
        super(fragmentManager);
        this.context = context;
    }

    @Override
    public Fragment getItem (int position) {
        Fragment fragment = new BetsListFragment();
        Bundle bundle = new Bundle();
        switch (position) {
            case 0:
                bundle.putInt(BetsListFragment.IS_ON_GOING, 1);
                break;
            case 1:
                bundle.putInt(BetsListFragment.IS_ON_GOING, 0);
                break;
            case 2:
                bundle.putInt(BetsListFragment.IS_ON_GOING, 2);
                break;
        }
        fragment.setArguments(bundle);
        return fragment;

    }

    @Override
    public CharSequence getPageTitle (int position) {
        switch (position) {
            case 0:
                return context.getResources().getString(R.string.ongoing_bets);
            case 1:
                return context.getResources().getString(R.string.completed_bets);
            case 2:
                return context.getResources().getString(R.string.contacts);
        }
        return null;
    }

    @Override
    public int getCount () {
        return 3;
    }
}
