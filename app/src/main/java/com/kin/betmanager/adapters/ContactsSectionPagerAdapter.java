package com.kin.betmanager.adapters;

import android.content.Context;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;

import com.kin.betmanager.BetsListFragment;
import com.kin.betmanager.ContactsFragment;
import com.kin.betmanager.UpdatableFragment;

/**
 * Created by Kin on 3/16/18.
 */

public class ContactsSectionPagerAdapter extends SectionPagerAdapter {
    private long contactId;

    public ContactsSectionPagerAdapter(Context context, FragmentManager fragmentManager, int size, long contactId) {
        super(context, fragmentManager, size);
        this.contactId = contactId;
    }

    @Override
    public Fragment getItem(int position) {
        UpdatableFragment fragment = null;
        switch (position) {
            case 0:
            case 1:
                fragment = new BetsListFragment();
                Bundle bundle = new Bundle();
                fragment.setArguments(bundle);
                bundle.putInt(BetsListFragment.IS_COMPLETED, position);
                bundle.putLong(BetsListFragment.CONTACT_ID, contactId);
                break;
        }
        fragmentReferenceMap.put(position, fragment);
        return fragment;

    }
}
