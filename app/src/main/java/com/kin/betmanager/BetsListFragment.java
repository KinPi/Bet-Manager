package com.kin.betmanager;


import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteException;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.ListFragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.SimpleCursorAdapter;

import com.kin.betmanager.database.DatabaseHelper;


/**
 * A simple {@link Fragment} subclass.
 */
public class BetsListFragment extends ListFragment {

    public static final String IS_ON_GOING = "isOnGoing";

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

        isOnGoing = getArguments().getInt(IS_ON_GOING);

        try {
            DatabaseHelper databaseHelper = DatabaseHelper.getInstance(getActivity().getApplicationContext());
            db = databaseHelper.getReadableDatabase();
            cursor = db.query(DatabaseHelper.TABLE_NAME,
                    new String [] {DatabaseHelper.ID, DatabaseHelper.TITLE},
                    DatabaseHelper.ONGOING + " = ?",
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
        cursor.close();
        db.close();
    }

}
