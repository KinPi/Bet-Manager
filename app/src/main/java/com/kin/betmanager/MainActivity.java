package com.kin.betmanager;

import android.app.Activity;
import android.content.Intent;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.design.widget.TabLayout;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;

import com.kin.betmanager.adapters.SectionPagerAdapter;
import com.kin.betmanager.database.DatabaseHelper;

public class MainActivity extends AppCompatActivity {

    private static final int NEW_BET_REQUEST_CODE = 0;
    private MenuItem createNewBetMenuItem;
    private FloatingActionButton createNewBetFAB;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        SectionPagerAdapter pagerAdapter = new SectionPagerAdapter(this, getSupportFragmentManager());
        ViewPager pager = (ViewPager) findViewById(R.id.pager);
        pager.setAdapter(pagerAdapter);

        TabLayout tabLayout = (TabLayout) findViewById(R.id.tabs);
        tabLayout.setupWithViewPager(pager);

        createNewBetFAB = (FloatingActionButton) findViewById(R.id.fab_create_new_bet);
    }

    @Override
    protected void onResume () {
        super.onResume();
        if (createNewBetMenuItem != null) {
            createNewBetMenuItem.setEnabled(true);
        }
        createNewBetFAB.setEnabled(true);
    }

    @Override
    public boolean onCreateOptionsMenu (Menu menu) {
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected (MenuItem item) {
        switch(item.getItemId()) {
            case R.id.action_create_new_bet:
                createNewBetMenuItem = item;
                createNewBetMenuItem.setEnabled(false);
                Intent intent = new Intent(this, NewBetActivity.class);
                startActivityForResult(intent, NEW_BET_REQUEST_CODE);
                return true;

            default:
                return super.onOptionsItemSelected(item);
        }

    }


    public void onClickFloatingActionButton(View view) {
        createNewBetFAB.setEnabled(false);
        Intent intent = new Intent(this, NewBetActivity.class);
        startActivityForResult(intent, NEW_BET_REQUEST_CODE);
    }

    @Override
    protected void onActivityResult (int requestCode, int resultCode, final Intent data) {
        if (requestCode == NEW_BET_REQUEST_CODE) {
            if (resultCode == Activity.RESULT_OK) {
                Snackbar snackbar = Snackbar.make(findViewById(R.id.pager), getString(R.string.bet_created_message), Snackbar.LENGTH_LONG);
                snackbar.setAction(getString(R.string.undo), new View.OnClickListener() {

                    @Override
                    public void onClick(View view) {
                        long betId = data.getLongExtra(NewBetActivity.NEW_BET_ID, -1);
                        DatabaseHelper.deleteBet(MainActivity.this, betId);
                    }

                });
                snackbar.show();
            }
        }
    }
}
