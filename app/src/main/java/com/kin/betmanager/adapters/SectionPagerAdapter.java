package com.kin.betmanager.adapters;

import android.content.Context;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.view.View;

import com.kin.betmanager.BetsListFragment;
import com.kin.betmanager.ContactsFragment;
import com.kin.betmanager.R;
import com.kin.betmanager.UpdatableFragment;

import java.util.HashMap;
import java.util.Map;

/**
 * Created by Kin on 3/3/18.
 */

public class SectionPagerAdapter extends FragmentPagerAdapter {

    private Context context = null;
    private int size = 0;
    public Map<Integer, UpdatableFragment> fragmentReferenceMap;


    public SectionPagerAdapter (Context context, FragmentManager fragmentManager, int size) {
        super(fragmentManager);
        this.context = context;
        fragmentReferenceMap = new HashMap<>(size);
        this.size = size;
    }

    @Override
    public Fragment getItem (int position) {
        UpdatableFragment fragment = null;
        switch (position) {
            case 0:
            case 1:
                fragment = new BetsListFragment();
                Bundle bundle = new Bundle();
                fragment.setArguments(bundle);
                bundle.putInt(BetsListFragment.IS_COMPLETED, position);
                break;
            case 2:
                fragment = new ContactsFragment();
                break;
        }
        fragmentReferenceMap.put(position, fragment);
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
        return size;
    }

    @Override
    public void destroyItem (View container, int position, Object object) {
        super.destroyItem(container, position, object);
        fragmentReferenceMap.remove(position);
    }
}
