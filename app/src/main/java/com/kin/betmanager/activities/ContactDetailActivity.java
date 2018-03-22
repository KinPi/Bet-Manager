package com.kin.betmanager.activities;

import android.app.AlertDialog;
import android.content.Intent;
import android.support.design.widget.TabLayout;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.EditText;
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
            case R.id.action_create_new_bet:
                Intent intent = new Intent(this, NewBetActivity.class);
                intent.putExtra(NewBetActivity.BETTING_AGAINST_NAME, contact.name);
                startActivity(intent);
                return true;
            case R.id.action_edit:
                createEditContactAlertDialog().show();
                return true;
            case R.id.action_delete:
                createDeleteAlertDialog().show();
                return true;
        }
        return false;
    }

    private AlertDialog createEditContactAlertDialog() {
        View alertLayout = getLayoutInflater().inflate(R.layout.alert_dialog_edit_contact, null);
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle(getResources().getString(R.string.edit_contact));
        builder.setView(alertLayout);

        TextView editTextView = (TextView) alertLayout.findViewById(R.id.edit_textview);
        TextView cancelTextView = (TextView) alertLayout.findViewById(R.id.cancel_textview);
        final EditText nameEditText = (EditText) alertLayout.findViewById(R.id.edit_contact_name_edittext);
        nameEditText.setText(contact.name);

        final AlertDialog dialog = builder.create();

        editTextView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                contact.name = nameEditText.getText().toString();
                DatabaseHelper.updateName(ContactDetailActivity.this, contact.id, contact.name);
                dialog.dismiss();
                Intent intent = getIntent();
                intent.putExtra(ContactsAdapter.CONTACT, contact);
                finish();
                startActivity(intent);
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

    private AlertDialog createDeleteAlertDialog() {
        View alertLayout = getLayoutInflater().inflate(R.layout.alert_dialog_delete, null);
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle(getResources().getString(R.string.confirm_deletion));
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