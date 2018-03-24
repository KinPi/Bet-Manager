package com.kin.betmanager.activities;

import android.content.Intent;
import android.support.design.widget.AppBarLayout;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.EditText;

import com.kin.betmanager.R;
import com.kin.betmanager.objects.Bet;
import com.kin.betmanager.objects.Contact;

public class BetDetailActivity extends AppCompatActivity implements AppBarLayout.OnOffsetChangedListener {

    public static final String BET = "bet";
    public static final String CONTACT = "contact";

    private AppBarLayout appBarLayout;
    private EditText bettingAgainstEditText;
    private EditText opponentsBetEditText;
    private EditText yourBetEditText;
    private EditText termsAndConditionsEditText;
    private FloatingActionButton completionFAB;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_bet_detail);

        Intent intent = getIntent();
        Bet bet = intent.getParcelableExtra(BET);
        Contact contact = intent.getParcelableExtra(CONTACT);

        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        ActionBar actionBar = getSupportActionBar();
        actionBar.setDisplayHomeAsUpEnabled(true);

        actionBar.setTitle(bet.title);

        appBarLayout = (AppBarLayout) findViewById(R.id.appBarLayout);
        bettingAgainstEditText = (EditText) findViewById(R.id.betting_against_edittext);
        opponentsBetEditText = (EditText) findViewById(R.id.opponents_bet_edittext);
        yourBetEditText = (EditText) findViewById(R.id.your_bet_edittext);
        termsAndConditionsEditText = (EditText) findViewById(R.id.terms_and_conditions_edittext);
        completionFAB = (FloatingActionButton) findViewById(R.id.completion_floating_action_button);

        appBarLayout.addOnOffsetChangedListener(this);

        bettingAgainstEditText.setText(contact.name);
        opponentsBetEditText.setText(bet.opponentsBet);
        yourBetEditText.setText(bet.yourBet);
        termsAndConditionsEditText.setText(bet.termsAndConditions);
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

    public void onClickFloatingActionButton(View view) {

    }

    @Override
    public void onOffsetChanged(AppBarLayout appBarLayout, int verticalOffset) {
        if (verticalOffset < -350) {
            completionFAB.setVisibility(View.INVISIBLE);
        }
        else {
            completionFAB.setVisibility(View.VISIBLE);
        }
    }
}
