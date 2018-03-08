package com.kin.betmanager.database;

import android.content.ContentValues;
import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import com.kin.betmanager.R;

/**
 * Created by Kin on 3/6/18.
 */

public class DatabaseHelper extends SQLiteOpenHelper {
    private static final String DB_NAME = "BetManager";
    private static final int DB_VERSION = 1;

    private static DatabaseHelper databaseHelper = null;

    private static final String unformattedBetsTableSQLString =  "CREATE TABLE %s (" +
                                                                    "%s INTEGER PRIMARY KEY AUTOINCREMENT, " +
                                                                    "%s TEXT, " +
                                                                    "%s INTEGER, " +
                                                                    "%s TEXT, " +
                                                                    "%s TEXT, " +
                                                                    "%s TEXT, " +
                                                                    "%s INTEGER);";

    private static final String unformattedContactsTableSQLString = "CREATE TABLE %s (" +
                                                                        "%s INTEGER PRIMARY KEY AUTOINCREMENT, " +
                                                                        "%s TEXT, " +
                                                                        "%s INTEGER);";

    public static final String BETS_TABLE = "BETS";
    public static final String BET_ID = "_id";
    public static final String TITLE = "TITLE";
    public static final String BETTING_AGAINST = "BETTING_AGAINST";
    public static final String OPPONENTS_BET = "OPPONENTS_BET";
    public static final String YOUR_BET = "YOUR_BET";
    public static final String TERMS_AND_CONDITIONS = "TERMS_AND_CONDITIONS";
    public static final String COMPLETED = "COMPLETED";

    public static final String CONTACTS_TABLE = "CONTACTS";
    public static final String CONTACT_ID = "CONTACT_ID";
    public static final String CONTACT_NAME = "CONTACT_NAME";
    public static final String CONTACT_IMAGE = "CONTACT_IMAGE";

    private DatabaseHelper (Context context) {
        super(context, DB_NAME, null, DB_VERSION);
    }

    public static synchronized DatabaseHelper getInstance (Context context) {
        if (databaseHelper == null) {
            databaseHelper = new DatabaseHelper(context.getApplicationContext());
        }
        return databaseHelper;
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        String createBetsTableSqlString = String.format(unformattedBetsTableSQLString,
                BETS_TABLE, BET_ID, TITLE, BETTING_AGAINST, OPPONENTS_BET, YOUR_BET, TERMS_AND_CONDITIONS, COMPLETED);
        db.execSQL(createBetsTableSqlString);

        String createContactsTableSqlString = String.format(unformattedContactsTableSQLString,
                CONTACTS_TABLE, CONTACT_ID, CONTACT_NAME, CONTACT_IMAGE);
        db.execSQL(createContactsTableSqlString);

        insertNewContact(db, "Chris Manlapid", R.drawable.default_user);
        insertNewContact(db, "Dummy User", R.drawable.default_user);
        insertNewContact(db, "Dummy User 2", R.drawable.default_user);

        insertNewBet(db,
                "SAT Scores",
                1,
                "$10",
                "$5",
                "I win if I get over 1800 in the SAT.",
                false);

        insertNewBet(db,
                "GRE Scores",
                1,
                "$10",
                "$5",
                "I win if I get over 1800 in the SAT.",
                true);
    }

    @Override
    public void onUpgrade(SQLiteDatabase sqLiteDatabase, int oldVersion, int newVersion) {

    }

    private void insertNewContact(SQLiteDatabase db,
                                  String newContactName,
                                  int newContactImage) {
        ContentValues values = new ContentValues();
        values.put(CONTACT_NAME, newContactName);
        values.put(CONTACT_IMAGE, newContactImage);
        db.insert(CONTACTS_TABLE, null, values);
    }

    private static void insertNewBet (SQLiteDatabase db,
                                     String newTitle,
                                     int newBettingAgainst,
                                     String newOpponentsBet,
                                     String newYourBet,
                                     String newTermsAndConditions,
                                     boolean newCompleted) {
        ContentValues values = new ContentValues();
        values.put(TITLE, newTitle);
        values.put(BETTING_AGAINST, newBettingAgainst);
        values.put(OPPONENTS_BET, newOpponentsBet);
        values.put(YOUR_BET, newYourBet);
        values.put(TERMS_AND_CONDITIONS, newTermsAndConditions);
        values.put(COMPLETED, newCompleted);
        db.insert(BETS_TABLE, null, values);
    }
}
