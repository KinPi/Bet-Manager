package com.kin.betmanager.activities;

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

import com.kin.betmanager.R;
import com.kin.betmanager.adapters.SectionPagerAdapter;
import com.kin.betmanager.database.DatabaseHelper;
import com.kin.betmanager.fragments.UpdatableFragment;

import java.util.Map;

import butterknife.BindView;
import butterknife.ButterKnife;

public class MainActivity extends AppCompatActivity {

    private static final int NEW_BET_REQUEST_CODE = 0;
    private static final String VIEW_PAGER_POSITION = "view pager position";
    private MenuItem createNewBetMenuItem;

    @BindView(R.id.fab_create_new_bet) FloatingActionButton createNewBetFAB;
    @BindView(R.id.pager) ViewPager viewPager;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        ButterKnife.bind(this);

        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        SectionPagerAdapter pagerAdapter = new SectionPagerAdapter(this, getSupportFragmentManager(), 3);
        viewPager.setAdapter(pagerAdapter);

        TabLayout tabLayout = (TabLayout) findViewById(R.id.tabs);
        tabLayout.setupWithViewPager(viewPager);

        if (savedInstanceState != null) {
            int viewPagerPosition = savedInstanceState.getInt(VIEW_PAGER_POSITION);
            viewPager.setCurrentItem(viewPagerPosition);
        }
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
    protected void onSaveInstanceState (Bundle savedInstanceState) {
        savedInstanceState.putInt(VIEW_PAGER_POSITION, viewPager.getCurrentItem());
        super.onSaveInstanceState(savedInstanceState);
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

                        Map<Integer, UpdatableFragment> fragmentReferenceMap = ((SectionPagerAdapter) viewPager.getAdapter()).fragmentReferenceMap;
                        for (Map.Entry<Integer, UpdatableFragment> entry : fragmentReferenceMap.entrySet()) {
                            if (entry.getValue() != null)
                                entry.getValue().updateData();
                        }

                    }

                });
                snackbar.show();
            }
        }
    }
}
