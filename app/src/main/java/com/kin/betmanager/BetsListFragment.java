package com.kin.betmanager;


import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteException;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.widget.DividerItemDecoration;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.kin.betmanager.adapters.BetsAdapter;
import com.kin.betmanager.database.DatabaseHelper;
import com.kin.betmanager.objects.Bet;

import java.util.ArrayList;
import java.util.List;


/**
 * A simple {@link Fragment} subclass.
 */
public class BetsListFragment extends UpdatableFragment {
    public static final String CONTACT_ID = "contactId";
    public static final String IS_COMPLETED = "isCompleted";

    RecyclerView recyclerView;
    int isCompleted;
    long contactId;

    public BetsListFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment

        isCompleted = getArguments().getInt(IS_COMPLETED);
        contactId = getArguments().getLong(CONTACT_ID, -1);

        recyclerView = (RecyclerView) inflater.inflate(R.layout.fragment_viewpager, container, false);
        RecyclerView.LayoutManager layoutManager = new LinearLayoutManager(getActivity());
        recyclerView.setLayoutManager(layoutManager);

        DividerItemDecoration dividerItemDecoration = new DividerItemDecoration(
                recyclerView.getContext(),
                DividerItemDecoration.VERTICAL
        );
        recyclerView.addItemDecoration(dividerItemDecoration);
        return recyclerView;
    }

    @Override
    public void onResume() {
        super.onResume();
        updateData();
    }

    @Override
    public void updateData () {
        BetsAdapter adapter = (BetsAdapter) recyclerView.getAdapter();
        if (adapter == null) {
            adapter = new BetsAdapter(getActivity(), fetchBets());
            recyclerView.setAdapter(adapter);
        }
        else {
            adapter.setBets(fetchBets());
            adapter.notifyDataSetChanged();
        }
    }

    protected List<Bet> fetchBets () {
        DatabaseHelper myDatabaseHelper = DatabaseHelper.getInstance(getActivity());
        SQLiteDatabase db = null;
        List<Bet> betsLIst = new ArrayList<>();
        try {
            db = myDatabaseHelper.getReadableDatabase();
            String selectionString = DatabaseHelper.COMPLETED + " = ?";
            if (contactId != -1) {
                selectionString += " AND " + DatabaseHelper.BETTING_AGAINST + " = " + contactId;
            }
            Log.d("Selection: ", selectionString);

            Cursor cursor = db.query(DatabaseHelper.BETS_TABLE,
                    new String [] {
                            DatabaseHelper.BET_ID,
                            DatabaseHelper.TITLE,
                            DatabaseHelper.BETTING_AGAINST,
                            DatabaseHelper.OPPONENTS_BET,
                            DatabaseHelper.YOUR_BET,
                            DatabaseHelper.TERMS_AND_CONDITIONS,
                            DatabaseHelper.COMPLETED},
                    selectionString,
                    new String [] {Integer.toString(isCompleted)},
                    null, null, null);
            if (cursor.moveToFirst()) {
                do {
                    long id = cursor.getLong(0);
                    String title = cursor.getString(1);
                    long bettingAgainst = cursor.getLong(2);
                    String opponentsBet = cursor.getString(3);
                    String yourBet = cursor.getString(4);
                    String termsAndConditions = cursor.getString(5);
                    boolean completed = cursor.getInt(6) == 1;
                    Bet bet = new Bet(id, title, bettingAgainst, opponentsBet, yourBet, termsAndConditions, completed);
                    betsLIst.add(bet);
                } while (cursor.moveToNext());
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
        return betsLIst;
    }

}
