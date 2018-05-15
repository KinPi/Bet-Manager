package com.kin.betmanager.activities;

import android.Manifest;
import android.app.Activity;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.media.Image;
import android.net.Uri;
import android.provider.ContactsContract;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;

import com.kin.betmanager.R;
import com.kin.betmanager.database.DatabaseHelper;

import butterknife.BindView;
import butterknife.ButterKnife;

public class NewBetActivity extends AppCompatActivity {
    public static final int PICK_CONTACT = 2015;

    public static final String NEW_BET_ID = "new bet id";
    public static final String BETTING_AGAINST_NAME = "betting against name";
    public static final String BETTING_AGAINST_IMAGE = "betting against image";

    private static final String TITLE = "title";
    private static final String BETTING_AGAINST = "betting against";
    private static final String OPPONENTS_BET = "opponenet's bet";
    private static final String YOUR_BET = "your bet";
    private static final String TERMS_AND_CONDITIONS = "terms and conditions";

    private static final int PERMISSION_REQUEST_CODE = 519;

    @BindView(R.id.title_edittext) EditText titleEditText;
    @BindView(R.id.betting_against_edittext) EditText bettingAgainstEditText;
    @BindView(R.id.opponents_bet_edittext) EditText opponentsBetEditText;
    @BindView(R.id.your_bet_edittext) EditText yourBetEditText;
    @BindView(R.id.terms_and_conditions_edittext) EditText termsAndConditionsEditText;

    @BindView(R.id.profile_picture) ImageView profilePicture;
    private String profileImageUri = "";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_new_bet);
        ButterKnife.bind(this);

        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        if (savedInstanceState != null) {
            titleEditText.setText(savedInstanceState.getString(TITLE));
            bettingAgainstEditText.setText(savedInstanceState.getString(BETTING_AGAINST));
            opponentsBetEditText.setText(savedInstanceState.getString(OPPONENTS_BET));
            yourBetEditText.setText(savedInstanceState.getString(YOUR_BET));
            termsAndConditionsEditText.setText(savedInstanceState.getString(TERMS_AND_CONDITIONS));
            profileImageUri = savedInstanceState.getString(BETTING_AGAINST_IMAGE);

            if (!profileImageUri.isEmpty()) {
                profilePicture.setImageURI(Uri.parse(profileImageUri));
            }
        }

        String bettingAgainstName = getIntent().getStringExtra(BETTING_AGAINST_NAME);
        if (bettingAgainstName != null) {
            bettingAgainstEditText.setText(bettingAgainstName);
        }

        String bettingAgainstImage = getIntent().getStringExtra(BETTING_AGAINST_IMAGE);
        if (bettingAgainstImage != null && !bettingAgainstImage.isEmpty()) {
            profilePicture.setImageURI(Uri.parse(bettingAgainstImage));
            profileImageUri = bettingAgainstImage;
        }
    }

    @Override
    protected void onSaveInstanceState (Bundle savedInstanceState) {
        savedInstanceState.putString(TITLE, titleEditText.getText().toString());
        savedInstanceState.putString(BETTING_AGAINST, bettingAgainstEditText.getText().toString());
        savedInstanceState.putString(OPPONENTS_BET, opponentsBetEditText.getText().toString());
        savedInstanceState.putString(YOUR_BET, yourBetEditText.getText().toString());
        savedInstanceState.putString(TERMS_AND_CONDITIONS, termsAndConditionsEditText.getText().toString());
        savedInstanceState.putString(BETTING_AGAINST_IMAGE, profileImageUri);
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
                            false,
                            profileImageUri);
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

    public void searchContacts (View view) {
        if (ContextCompat.checkSelfPermission(this, Manifest.permission.READ_CONTACTS) != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(this, new String [] {Manifest.permission.READ_CONTACTS}, PERMISSION_REQUEST_CODE);
        }
        else {
            Intent intent = new Intent(Intent.ACTION_PICK, ContactsContract.CommonDataKinds.Phone.CONTENT_URI);
            startActivityForResult(intent, PICK_CONTACT);
        }
    }

    @Override
    public void onRequestPermissionsResult (int requestCode, String permissions[], int[] grantResults) {
        switch(requestCode) {
            case PERMISSION_REQUEST_CODE: {
                if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                    Intent intent = new Intent(Intent.ACTION_PICK, ContactsContract.CommonDataKinds.Phone.CONTENT_URI);
                    startActivityForResult(intent, PICK_CONTACT);
                }
                else {
                    Toast.makeText(this, getString(R.string.unable_to_get_permission), Toast.LENGTH_LONG).show();
                }
            }
        }
    }

    @Override
    protected void onActivityResult (int requestCode, int resultCode, Intent intent) {
        if (requestCode == PICK_CONTACT && resultCode == RESULT_OK) {
            Uri contactUri = intent.getData();
            Cursor cursor = getContentResolver().query(contactUri, null, null, null, null);
            cursor.moveToFirst();

            int column = cursor.getColumnIndex(ContactsContract.CommonDataKinds.Phone.DISPLAY_NAME);
            bettingAgainstEditText.setText(cursor.getString(column));

            column = cursor.getColumnIndex(ContactsContract.CommonDataKinds.Phone.PHOTO_URI);

            try {
                profileImageUri = cursor.getString(column);
                profilePicture.setImageURI(Uri.parse(profileImageUri));
            }
            catch (Exception e) {
                profileImageUri = "";
                profilePicture.setImageDrawable(getResources().getDrawable(R.drawable.default_user));
            }
        }
    }

}
