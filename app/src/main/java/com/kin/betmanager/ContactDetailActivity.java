package com.kin.betmanager;

import android.support.design.widget.TabLayout;
import android.support.v4.view.ViewPager;
import android.support.v4.widget.NestedScrollView;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.view.MenuItem;

import com.kin.betmanager.adapters.ContactsAdapter;
import com.kin.betmanager.adapters.ContactsSectionPagerAdapter;
import com.kin.betmanager.adapters.SectionPagerAdapter;
import com.kin.betmanager.objects.Contact;

public class ContactDetailActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_contact_detail);

        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);


        Contact contact = getIntent().getParcelableExtra(ContactsAdapter.CONTACT);
        getSupportActionBar().setTitle(contact.name);

        ContactsSectionPagerAdapter adapter = new ContactsSectionPagerAdapter(this, getSupportFragmentManager(), 2, contact.id);
        ViewPager viewPager = (ViewPager) findViewById(R.id.pager);
        viewPager.setAdapter(adapter);

        TabLayout tabLayout = (TabLayout) findViewById(R.id.tabs);
        tabLayout.setupWithViewPager(viewPager, true);

    }

    @Override
    public boolean onOptionsItemSelected(MenuItem menuItem) {
        switch (menuItem.getItemId()) {
            case android.R.id.home:
                finish();
                return true;
        }
        return false;
    }

}