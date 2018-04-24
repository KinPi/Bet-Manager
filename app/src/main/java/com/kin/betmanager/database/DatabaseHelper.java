package com.kin.betmanager.database;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.SQLException;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteException;
import android.database.sqlite.SQLiteOpenHelper;

import com.kin.betmanager.R;
import com.kin.betmanager.activities.ContactDetailActivity;
import com.kin.betmanager.objects.Bet;
import com.kin.betmanager.objects.Contact;

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
                                                                        "%s TEXT);";

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

    }

    @Override
    public void onUpgrade(SQLiteDatabase sqLiteDatabase, int oldVersion, int newVersion) {

    }

    private static Contact insertNewContact(SQLiteDatabase db,
                                            String newContactName,
                                            String newContactImage) {
        ContentValues values = new ContentValues();
        values.put(CONTACT_NAME, newContactName);
        values.put(CONTACT_IMAGE, newContactImage);
        long id = db.insert(CONTACTS_TABLE, null, values);
        if (id != -1) {
            return new Contact(id, newContactName, newContactImage);
        }
        return null;
    }

    private static Bet insertNewBet (SQLiteDatabase db,
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
        long id = db.insert(BETS_TABLE, null, values);
        if (id != -1) {
            return new Bet(id, newTitle, newBettingAgainst, newOpponentsBet, newYourBet, newTermsAndConditions, newCompleted);
        }
        return null;
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
                                        boolean newCompleted,
                                        String profilePicture) {

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
                contactId = DatabaseHelper.insertNewContact(db, newBettingAgainst, profilePicture).id;
            }

            newBetId = DatabaseHelper.insertNewBet(
                                                    db,
                                                    newTitle,
                                                    contactId,
                                                    newOpponentsBet,
                                                    newYourBet,
                                                    newTermsAndConditions,
                                                    newCompleted).id;
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

    public static Bet findBet (Context context, long betId) {
        DatabaseHelper myDataBaseHelper = DatabaseHelper.getInstance(context);
        SQLiteDatabase db = null;
        Bet bet = null;
        try {
            db = myDataBaseHelper.getReadableDatabase();
            Cursor cursor = db.query(BETS_TABLE,
                    new String [] {TITLE, BETTING_AGAINST, OPPONENTS_BET, YOUR_BET, TERMS_AND_CONDITIONS, COMPLETED},
                    BET_ID + " = ?",
                    new String [] {Long.toString(betId)},
                    null , null, null);
            if (cursor.moveToFirst()) {
                bet = new Bet();
                bet.id = betId;
                bet.title = cursor.getString(0);
                bet.bettingAgainst = cursor.getLong(1);
                bet.opponentsBet = cursor.getString(2);
                bet.yourBet = cursor.getString(3);
                bet.termsAndConditions = cursor.getString(4);
                bet.isCompleted = cursor.getInt(5) == 1;
            }
            cursor.close();
        }
        catch (SQLException e) {
            e.printStackTrace();
        }
        finally {
            if (db != null) {
                db.close();
            }
        }
        return bet;
    }

    public static void updateBet (Context context,
                                  long betId,
                                  String newTitle,
                                  String newOpponentsBet,
                                  String newYourBet,
                                  String newTermsAndConditions) {
        DatabaseHelper myDataBaseHelper = DatabaseHelper.getInstance(context);
        SQLiteDatabase db = null;
        try {
            db = myDataBaseHelper.getWritableDatabase();
            ContentValues contentValues = new ContentValues();
            contentValues.put(TITLE, newTitle);
            contentValues.put(OPPONENTS_BET, newOpponentsBet);
            contentValues.put(YOUR_BET, newYourBet);
            contentValues.put(TERMS_AND_CONDITIONS, newTermsAndConditions);
            db.update(BETS_TABLE, contentValues, BET_ID + " = ?", new String[] {Long.toString(betId)});
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

    public static void updateBetCompletionStatus (Context context, long betId, boolean status) {
        DatabaseHelper myDatabaseHelper = DatabaseHelper.getInstance(context);
        SQLiteDatabase db = null;
        try {
            ContentValues contentValues = new ContentValues();
            contentValues.put(COMPLETED, status);
            db = myDatabaseHelper.getWritableDatabase();
            db.update(BETS_TABLE,
                    contentValues,
                    BET_ID + " = ?",
                    new String [] {Long.toString(betId)});
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

    public static boolean deleteBet (Context context, long betId) {
        DatabaseHelper myDatabaseHelper = DatabaseHelper.getInstance(context);
        SQLiteDatabase db = null;
        boolean isContactDeleted = false;
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
                isContactDeleted = true;
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
        return isContactDeleted;
    }

    public static void deleteAllBetsMadeWithContact (Context context, long contactId) {
        DatabaseHelper myDatabaseHelper = DatabaseHelper.getInstance(context);
        SQLiteDatabase db = null;
        try {
            db = myDatabaseHelper.getWritableDatabase();
            db.delete(DatabaseHelper.BETS_TABLE,
                    DatabaseHelper.BETTING_AGAINST + " = ?",
                    new String [] {Long.toString(contactId)});
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

    public static Contact findContact (Context context, long contactId) {
        DatabaseHelper myDatabaseHelper = DatabaseHelper.getInstance(context);
        Contact contact = null;
        SQLiteDatabase db = null;
        try {
            db = myDatabaseHelper.getWritableDatabase();
            Cursor cursor = db.query(DatabaseHelper.CONTACTS_TABLE,
                    new String [] {DatabaseHelper.CONTACT_NAME, DatabaseHelper.CONTACT_IMAGE},
                    DatabaseHelper.CONTACT_ID + " = ?",
                    new String [] {Long.toString(contactId)},
                    null, null, null);
            if (cursor.moveToFirst()) {
                String name = cursor.getString(0);
                String image = cursor.getString(1);
                contact = new Contact(contactId, name, image);
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
        return contact;
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

    public static void updateContactName(Context contactDetailActivity, long id, String newName) {
        DatabaseHelper myDatabaseHelper = DatabaseHelper.getInstance(contactDetailActivity);
        SQLiteDatabase db = null;
        try {
            db = myDatabaseHelper.getWritableDatabase();
            ContentValues values = new ContentValues();
            values.put(CONTACT_NAME, newName);
            db.update(CONTACTS_TABLE, values, DatabaseHelper.CONTACT_ID + " = ?", new String [] {Long.toString(id)});
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
}
