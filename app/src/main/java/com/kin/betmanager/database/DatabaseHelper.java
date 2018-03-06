package com.kin.betmanager.database;

import android.content.ContentValues;
import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

/**
 * Created by Kin on 3/6/18.
 */

public class DatabaseHelper extends SQLiteOpenHelper {
    private static final String DB_NAME = "BetManager";
    private static final int DB_VERSION = 1;

    private static DatabaseHelper databaseHelper = null;

    private static final String unformattedCreateTableSQLString =  "CREATE TABLE %s (" +
                                                                    "%s INTEGER PRIMARY KEY AUTOINCREMENT, " +
                                                                    "%s TEXT, " +
                                                                    "%s TEXT, " +
                                                                    "%s TEXT, " +
                                                                    "%s TEXT, " +
                                                                    "%s TEXT, " +
                                                                    "%s INTEGER);";

    public static final String TABLE_NAME = "BETS";
    public static final String ID = "_id";
    public static final String TITLE = "TITLE";
    public static final String BETTING_AGAINST = "BETTING_AGAINST";
    public static final String OPPONENTS_BET = "OPPONENTS_BET";
    public static final String YOUR_BET = "YOUR_BET";
    public static final String TERMS_AND_CONDITIONS = "TERMS_AND_CONDITIONS";
    public static final String ONGOING = "ONGOING";

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
        String createTableSqlString = String.format(unformattedCreateTableSQLString,
                TABLE_NAME, ID, TITLE, BETTING_AGAINST, OPPONENTS_BET, YOUR_BET, TERMS_AND_CONDITIONS, ONGOING);
        db.execSQL(createTableSqlString);

        insertNewBet(db,
                "SAT Scores",
                "Chris Manlapid",
                "$10",
                "$5",
                "I win if I get over 1800 in the SAT.",
                true);

        insertNewBet(db,
                "GRE Scores",
                "Chris Manlapid",
                "$10",
                "$5",
                "I win if I get over 1800 in the SAT.",
                false);
    }

    @Override
    public void onUpgrade(SQLiteDatabase sqLiteDatabase, int oldVersion, int newVersion) {

    }

    private static void insertNewBet (SQLiteDatabase db,
                                     String newTitle,
                                     String newBettingAgainst,
                                     String newOpponentsBet,
                                     String newYourBet,
                                     String newTermsAndConditions,
                                     boolean newOnGoing) {
        ContentValues values = new ContentValues();
        values.put(TITLE, newTitle);
        values.put(BETTING_AGAINST, newBettingAgainst);
        values.put(OPPONENTS_BET, newOpponentsBet);
        values.put(YOUR_BET, newYourBet);
        values.put(TERMS_AND_CONDITIONS, newTermsAndConditions);
        values.put(ONGOING, newOnGoing);
        db.insert(TABLE_NAME, null, values);
    }
}
