package com.kin.betmanager.activities;

import android.app.AlertDialog;
import android.support.design.widget.TabLayout;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.TextView;

import com.kin.betmanager.R;
import com.kin.betmanager.adapters.ContactsAdapter;
import com.kin.betmanager.adapters.ContactsSectionPagerAdapter;
import com.kin.betmanager.database.DatabaseHelper;
import com.kin.betmanager.objects.Contact;

public class ContactDetailActivity extends AppCompatActivity {
    private Contact contact;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_contact_detail);

        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);


        contact = getIntent().getParcelableExtra(ContactsAdapter.CONTACT);
        getSupportActionBar().setTitle(contact.name);

        ContactsSectionPagerAdapter adapter = new ContactsSectionPagerAdapter(this, getSupportFragmentManager(), 2, contact.id);
        ViewPager viewPager = (ViewPager) findViewById(R.id.pager);
        viewPager.setAdapter(adapter);

        TabLayout tabLayout = (TabLayout) findViewById(R.id.tabs);
        tabLayout.setupWithViewPager(viewPager, true);

    }

    @Override
    public boolean onCreateOptionsMenu (Menu menu) {
        getMenuInflater().inflate(R.menu.menu_item_details, menu);
        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem menuItem) {
        switch (menuItem.getItemId()) {
            case android.R.id.home:
                finish();
                return true;
            case R.id.delete:
                createDeleteAlertDialog().show();
                return true;
        }
        return false;
    }

    private AlertDialog createDeleteAlertDialog() {
        View alertLayout = getLayoutInflater().inflate(R.layout.alert_dialog_delete, null);
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle("Confirm Deletion");
        builder.setView(alertLayout);

        TextView deleteTextView = (TextView) alertLayout.findViewById(R.id.delete_textview);
        TextView cancelTextView = (TextView) alertLayout.findViewById(R.id.cancel_textview);

        final AlertDialog dialog = builder.create();

        deleteTextView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                DatabaseHelper.deleteContact(ContactDetailActivity.this, contact.id);
                DatabaseHelper.deleteAllBetsMadeWithContact(ContactDetailActivity.this, contact.id);
                dialog.dismiss();
                finish();
            }
        });

        cancelTextView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dialog.dismiss();
            }
        });

        return dialog;
    }

}