package com.kin.betmanager.activities;

import android.app.Activity;
import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.EditText;
import android.widget.Toast;

import com.kin.betmanager.R;
import com.kin.betmanager.database.DatabaseHelper;

public class NewBetActivity extends AppCompatActivity {

    public static final String NEW_BET_ID = "new bet id";

    private static final String TITLE = "title";
    private static final String BETTING_AGAINST = "betting against";
    private static final String OPPONENTS_BET = "opponenet's bet";
    private static final String YOUR_BET = "your bet";
    private static final String TERMS_AND_CONDITIONS = "terms and conditions";

    private EditText titleEditText;
    private EditText bettingAgainstEditText;
    private EditText opponentsBetEditText;
    private EditText yourBetEditText;
    private EditText termsAndConditionsEditText;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_new_bet);

        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        titleEditText = (EditText) findViewById(R.id.title_edittext);
        bettingAgainstEditText = (EditText) findViewById(R.id.betting_against_edittext);
        opponentsBetEditText = (EditText) findViewById(R.id.opponents_bet_edittext);
        yourBetEditText = (EditText) findViewById(R.id.your_bet_edittext);
        termsAndConditionsEditText = (EditText) findViewById(R.id.terms_and_conditions_edittext);

        if (savedInstanceState != null) {
            titleEditText.setText(savedInstanceState.getString(TITLE));
            bettingAgainstEditText.setText(savedInstanceState.getString(BETTING_AGAINST));
            opponentsBetEditText.setText(savedInstanceState.getString(OPPONENTS_BET));
            yourBetEditText.setText(savedInstanceState.getString(YOUR_BET));
            termsAndConditionsEditText.setText(savedInstanceState.getString(TERMS_AND_CONDITIONS));
        }
    }

    @Override
    protected void onSaveInstanceState (Bundle savedInstanceState) {
        savedInstanceState.putString(TITLE, titleEditText.getText().toString());
        savedInstanceState.putString(BETTING_AGAINST, bettingAgainstEditText.getText().toString());
        savedInstanceState.putString(OPPONENTS_BET, opponentsBetEditText.getText().toString());
        savedInstanceState.putString(YOUR_BET, yourBetEditText.getText().toString());
        savedInstanceState.putString(TERMS_AND_CONDITIONS, termsAndConditionsEditText.getText().toString());
        super.onSaveInstanceState(savedInstanceState);
    }

    @Override
    public boolean onCreateOptionsMenu (Menu menu) {
        getMenuInflater().inflate(R.menu.menu_new_bet, menu);
        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected (MenuItem menuItem) {
        switch (menuItem.getItemId()) {
            case android.R.id.home:
                finish();
                return true;

            case R.id.action_done :
                String inputTitle = titleEditText.getText().toString();
                String inputBettingAgainst = bettingAgainstEditText.getText().toString();
                String inputOpponentsBet = opponentsBetEditText.getText().toString();
                String inputYourBet = yourBetEditText.getText().toString();
                String inputTermsAndConditions = termsAndConditionsEditText.getText().toString();

                if (inputTitle.isEmpty()) {
                    Toast.makeText(this, "Title cannot be empty!", Toast.LENGTH_SHORT).show();
                } else if (inputBettingAgainst.isEmpty()) {
                    Toast.makeText(this, "Betting against cannot be empty!", Toast.LENGTH_SHORT).show();
                } else if (inputOpponentsBet.isEmpty()) {
                    Toast.makeText(this, "Opponent's bet against cannot be empty!", Toast.LENGTH_SHORT).show();
                } else if (inputYourBet.isEmpty()) {
                    Toast.makeText(this, "Your bet cannot be empty!", Toast.LENGTH_SHORT).show();
                } else if (inputTermsAndConditions.isEmpty()) {
                    Toast.makeText(this, "Terms and conditions cannot be empty!", Toast.LENGTH_SHORT).show();
                }
                else {
                    long newBetId = DatabaseHelper.insertNewBet(
                            this,
                            inputTitle,
                            inputBettingAgainst,
                            inputOpponentsBet,
                            inputYourBet,
                            inputTermsAndConditions,
                            false);
                    if (newBetId != -1) {
                        Intent returnIntent = new Intent();
                        returnIntent.putExtra(NEW_BET_ID, newBetId);
                        setResult(Activity.RESULT_OK, returnIntent);
                        finish();
                    }
                    else {
                        Log.e(NewBetActivity.class.getSimpleName(), "Failed to add new bet!");
                    }
                }
        }
        return super.onOptionsItemSelected(menuItem);
    }

}
