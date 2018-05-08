package com.kin.betmanager.activities;

import android.app.AlertDialog;
import android.content.Intent;
import android.support.design.widget.AppBarLayout;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.media.session.PlaybackStateCompat;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.kin.betmanager.R;
import com.kin.betmanager.database.DatabaseHelper;
import com.kin.betmanager.objects.Bet;
import com.kin.betmanager.objects.Contact;

import butterknife.BindView;
import butterknife.ButterKnife;

public class BetDetailActivity extends AppCompatActivity implements AppBarLayout.OnOffsetChangedListener {

    public static final String BET = "bet";
    public static final String CONTACT = "contact";

    @BindView(R.id.betting_against_edittext) EditText bettingAgainstEditText;
    @BindView(R.id.opponents_bet_edittext) EditText opponentsBetEditText;
    @BindView(R.id.your_bet_edittext) EditText yourBetEditText;
    @BindView(R.id.terms_and_conditions_edittext) EditText termsAndConditionsEditText;
    @BindView(R.id.completion_floating_action_button) FloatingActionButton completionFAB;

    private Bet bet;
    private Contact contact;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_bet_detail);
        ButterKnife.bind(this);

        Intent intent = getIntent();
        bet = intent.getParcelableExtra(BET);
        contact = intent.getParcelableExtra(CONTACT);

        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        toolbar.setTitle("");
        setSupportActionBar(toolbar);
        ActionBar actionBar = getSupportActionBar();
        actionBar.setDisplayHomeAsUpEnabled(true);

        AppBarLayout appBarLayout = (AppBarLayout) findViewById(R.id.appBarLayout);
        appBarLayout.addOnOffsetChangedListener(this);

        toolbar.setTitle(bet.title);

        bettingAgainstEditText.setText(contact.name);
        opponentsBetEditText.setText(bet.opponentsBet);
        yourBetEditText.setText(bet.yourBet);
        termsAndConditionsEditText.setText(bet.termsAndConditions);

        if (bet.isCompleted) {
            completionFAB.setImageResource(R.drawable.ic_loop_white_24dp);
        }
        else {
            completionFAB.setImageResource(R.drawable.ic_done_white_24dp);
        }
    }

    @Override
    public void onRestart () {
        super.onRestart();
        bet = DatabaseHelper.findBet(this, bet.id);
        Intent intent = new Intent(this, BetDetailActivity.class);
        intent.putExtra(BET, bet);
        intent.putExtra(CONTACT, contact);
        startActivity(intent);
        finish();
    }

    public void onClickFloatingActionButton(View view) {
        if (bet.isCompleted) {
            completionFAB.setImageResource(R.drawable.ic_done_white_24dp);
            bet.isCompleted = false;
            DatabaseHelper.updateBetCompletionStatus(this, bet.id, false);
            Toast.makeText(this, getString(R.string.changeToOngoing), Toast.LENGTH_LONG).show();
        }
        else {
            completionFAB.setImageResource(R.drawable.ic_loop_white_24dp);
            bet.isCompleted = true;
            DatabaseHelper.updateBetCompletionStatus(this, bet.id, true);
            Toast.makeText(this, getString(R.string.changeToCompleted), Toast.LENGTH_LONG).show();
        }
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

    @Override
    public boolean onCreateOptionsMenu (Menu menu) {
        getMenuInflater().inflate(R.menu.menu_bet_details, menu);
        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem menuItem) {
        switch (menuItem.getItemId()) {
            case android.R.id.home:
                finish();
                return true;
            case R.id.action_edit:
                Intent intent = new Intent(this, EditBetActivity.class);
                intent.putExtra(BET, bet);
                startActivity(intent);
                return true;
            case R.id.action_delete:
                createDeleteAlertDialog().show();
                return true;
        }
        return false;
    }

    private AlertDialog createDeleteAlertDialog() {
        View alertLayout = getLayoutInflater().inflate(R.layout.alert_dialog_delete, null);
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle(getResources().getString(R.string.confirm_deletion));
        builder.setView(alertLayout);

        TextView deleteTextView = (TextView) alertLayout.findViewById(R.id.delete_textview);
        TextView cancelTextView = (TextView) alertLayout.findViewById(R.id.cancel_textview);
        TextView deleteConfirmationTextView = (TextView) alertLayout.findViewById(R.id.delete_confirmation_textview);

        deleteConfirmationTextView.setText(getString(R.string.bet_delete_confirmation_text));

        final AlertDialog dialog = builder.create();

        deleteTextView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                DatabaseHelper.deleteBet(BetDetailActivity.this, bet.id);
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
