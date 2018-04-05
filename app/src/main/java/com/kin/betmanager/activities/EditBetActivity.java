package com.kin.betmanager.activities;

import android.content.Intent;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.EditText;
import android.widget.Toast;

import com.kin.betmanager.R;
import com.kin.betmanager.database.DatabaseHelper;
import com.kin.betmanager.objects.Bet;

public class EditBetActivity extends AppCompatActivity {
    Bet bet;

    private EditText titleEditText;
    private EditText opponentsBetEditText;
    private EditText yourBetEditText;
    private EditText termsAndConditionsEditText;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_edit_bet);

        Intent intent = getIntent();
        bet = intent.getParcelableExtra(BetDetailActivity.BET);

        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        ActionBar actionBar = getSupportActionBar();
        actionBar.setDisplayHomeAsUpEnabled(true);
        actionBar.setTitle(R.string.edit_bet);

        titleEditText = (EditText) findViewById(R.id.title_edittext);
        opponentsBetEditText = (EditText) findViewById(R.id.opponents_bet_edittext);
        yourBetEditText = (EditText) findViewById(R.id.your_bet_edittext);
        termsAndConditionsEditText = (EditText) findViewById(R.id.terms_and_conditions_edittext);

        titleEditText.setText(bet.title);
        opponentsBetEditText.setText(bet.opponentsBet);
        yourBetEditText.setText(bet.yourBet);
        termsAndConditionsEditText.setText(bet.termsAndConditions);
    }

    @Override
    public boolean onCreateOptionsMenu (Menu menu) {
        getMenuInflater().inflate(R.menu.menu_edit_bet, menu);
        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem menuItem) {
        switch (menuItem.getItemId()) {
            case android.R.id.home:
                finish();
                return true;
            case R.id.action_done:
                String inputTitle = titleEditText.getText().toString();
                String inputOpponentsBet = opponentsBetEditText.getText().toString();
                String inputYourBet = yourBetEditText.getText().toString();
                String inputTermsAndConditions = termsAndConditionsEditText.getText().toString();

                if (inputTitle.isEmpty()) {
                    Toast.makeText(this, "Title cannot be empty!", Toast.LENGTH_SHORT).show();
                } else if (inputOpponentsBet.isEmpty()) {
                    Toast.makeText(this, "Opponent's bet against cannot be empty!", Toast.LENGTH_SHORT).show();
                } else if (inputYourBet.isEmpty()) {
                    Toast.makeText(this, "Your bet cannot be empty!", Toast.LENGTH_SHORT).show();
                } else if (inputTermsAndConditions.isEmpty()) {
                    Toast.makeText(this, "Terms and conditions cannot be empty!", Toast.LENGTH_SHORT).show();
                }
                else {
                    DatabaseHelper.updateBet(this, bet.id, inputTitle, inputOpponentsBet, inputYourBet, inputTermsAndConditions);
                    finish();
                }
                return true;
        }
        return false;
    }
}
