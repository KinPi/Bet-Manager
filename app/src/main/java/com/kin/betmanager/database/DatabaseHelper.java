package com.kin.betmanager.database;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.DatabaseErrorHandler;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteException;
import android.database.sqlite.SQLiteOpenHelper;
import android.view.View;

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

//        insertNewContact(db, "Chris Manlapid", R.drawable.default_user);
//        insertNewContact(db, "Dummy User", R.drawable.default_user);
//        insertNewContact(db, "Dummy User 2", R.drawable.default_user);
//
//        insertNewBet(db,
//                "SAT Scores",
//                1,
//                "$10",
//                "$5",
//                "I win if I get over 1800 in the SAT.",
//                false);
//
//        insertNewBet(db,
//                "GRE Scores",
//                1,
//                "$10",
//                "$5",
//                "I win if I get over 1800 in the SAT.",
//                true);
    }

    @Override
    public void onUpgrade(SQLiteDatabase sqLiteDatabase, int oldVersion, int newVersion) {

    }

    private static long insertNewContact(SQLiteDatabase db,
                                  String newContactName,
                                  int newContactImage) {
        ContentValues values = new ContentValues();
        values.put(CONTACT_NAME, newContactName);
        values.put(CONTACT_IMAGE, newContactImage);
        return db.insert(CONTACTS_TABLE, null, values);
    }

    private static long insertNewBet (SQLiteDatabase db,
                                     String newTitle,
                                     long newBettingAgainst,
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
        return db.insert(BETS_TABLE, null, values);
    }

    /**
     *
     * @param context
     * @param newTitle
     * @param newBettingAgainst
     * @param newOpponentsBet
     * @param newYourBet
     * @param newTermsAndConditions
     * @param newCompleted
     * @return Returns new bet id, -1 if failed to insert.
     */
    public static long insertNewBet (Context context,
                                        String newTitle,
                                        String newBettingAgainst,
                                        String newOpponentsBet,
                                        String newYourBet,
                                        String newTermsAndConditions,
                                        boolean newCompleted) {

        DatabaseHelper myDatabaseHelper = DatabaseHelper.getInstance(context);
        SQLiteDatabase db = null;
        long newBetId = -1;

        try {
            db = myDatabaseHelper.getWritableDatabase();
            Cursor cursor = db.query(DatabaseHelper.CONTACTS_TABLE,
                    new String [] {DatabaseHelper.CONTACT_ID},
                    DatabaseHelper.CONTACT_NAME + " = ?",
                    new String [] {newBettingAgainst},
                    null, null, null);

            long contactId = -1;

            if (cursor.moveToFirst()) {
                contactId = cursor.getLong(0);
                cursor.close();
            }
            else {
                contactId = DatabaseHelper.insertNewContact(db, newBettingAgainst, R.drawable.default_user);
            }

            newBetId = DatabaseHelper.insertNewBet(
                                                    db,
                                                    newTitle,
                                                    contactId,
                                                    newOpponentsBet,
                                                    newYourBet,
                                                    newTermsAndConditions,
                                                    newCompleted);
        }
        catch (SQLiteException e) {
            e.printStackTrace();
        }
        catch (Exception e) {
            e.printStackTrace();
        }
        finally {
            if (db != null) {
                db.close();
            }
        }
        return newBetId;
    }

    public static void deleteBet (Context context, long betId) {
        DatabaseHelper myDatabaseHelper = DatabaseHelper.getInstance(context);
        SQLiteDatabase db = null;
        try {
            long opponentId = findOpponentIdGivenBetId(context, betId);
            db = myDatabaseHelper.getWritableDatabase();
            db.delete(DatabaseHelper.BETS_TABLE,
                    DatabaseHelper.BET_ID + " = ?",
                    new String [] {Long.toString(betId)});
            Cursor cursor = db.query(
                    DatabaseHelper.BETS_TABLE,
                    new String [] {DatabaseHelper.BET_ID},
                    DatabaseHelper.BETTING_AGAINST + " = ?",
                    new String [] {Long.toString(opponentId)},
                    null, null, null, null);
            if (cursor.getCount() == 0) {
                deleteContact(context, opponentId);
            }
            cursor.close();
        }
        catch (SQLiteException e) {
            e.printStackTrace();
        }
        finally {
            if (db != null) {
                db.close();
            }
        }
    }

    public static void deleteContact (Context context, long contactId) {
        DatabaseHelper myDatabaseHelper = DatabaseHelper.getInstance(context);
        SQLiteDatabase db = null;
        try {
            db = myDatabaseHelper.getWritableDatabase();
            db.delete(DatabaseHelper.CONTACTS_TABLE,
                    DatabaseHelper.CONTACT_ID + " = ?",
                    new String [] {Long.toString(contactId)});
            db.close();
        }
        catch (SQLiteException e) {
            e.printStackTrace();
        }
    }

    public static long findOpponentIdGivenBetId (Context context, long betId) {
        DatabaseHelper myDatabaseHelper = DatabaseHelper.getInstance(context);
        SQLiteDatabase db = null;
        long opponentId = -1;
        try {
            db = myDatabaseHelper.getReadableDatabase();
            Cursor cursor = db.query(
                    DatabaseHelper.BETS_TABLE,
                    new String [] {DatabaseHelper.BETTING_AGAINST},
                    DatabaseHelper.BET_ID + " = ?",
                    new String [] {Long.toString(betId)},
                    null, null, null, null);
            if (cursor.moveToFirst()) {
                opponentId = cursor.getLong(0);
            }
            cursor.close();
        }
        catch (SQLiteException e) {
            e.printStackTrace();
        }
        finally {
            if (db != null) {
                db.close();
            }
        }
        return opponentId;
    }
}
