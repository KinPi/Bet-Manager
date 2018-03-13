package com.kin.betmanager;


import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteException;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.ListFragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.SimpleCursorAdapter;

import com.kin.betmanager.database.DatabaseHelper;


/**
 * A simple {@link Fragment} subclass.
 */
public class BetsListFragment extends ListFragment {

    public static final String IS_COMPLETED = "isOnGoing";

    SQLiteDatabase db = null;
    Cursor cursor = null;
    int isOnGoing;

    public BetsListFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        //return inflater.inflate(R.layout.fragment_bets_list, container, false);

        isOnGoing = getArguments().getInt(IS_COMPLETED);

        try {
            DatabaseHelper databaseHelper = DatabaseHelper.getInstance(getActivity().getApplicationContext());
            db = databaseHelper.getReadableDatabase();
            cursor = db.query(DatabaseHelper.BETS_TABLE,
                    new String [] {DatabaseHelper.BET_ID, DatabaseHelper.TITLE},
                    DatabaseHelper.COMPLETED + " = ?",
                    new String[] {Integer.toString(isOnGoing)}, null, null, null );
            SimpleCursorAdapter adapter = new SimpleCursorAdapter(getActivity(),
                    android.R.layout.simple_list_item_1,
                    cursor,
                    new String[] {DatabaseHelper.TITLE},
                    new int[] {android.R.id.text1},
                    0);
            setListAdapter(adapter);

        }
        catch (SQLiteException e) {
            e.printStackTrace();
        }

        return super.onCreateView(inflater, container, savedInstanceState);
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        if (cursor != null)
            cursor.close();
        if (db != null)
            db.close();
    }

}
