package com.kin.betmanager;


import android.content.res.Configuration;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteException;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.kin.betmanager.adapters.ContactsAdapter;
import com.kin.betmanager.database.DatabaseHelper;

import java.util.ArrayList;
import java.util.List;


/**
 * A simple {@link Fragment} subclass.
 */
public class ContactsFragment extends Fragment {
    RecyclerView recyclerView;

    public ContactsFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        recyclerView = (RecyclerView) inflater.inflate(R.layout.fragment_contacts, container, false);
        int numContactsPerRow;
        if (getResources().getConfiguration().orientation == Configuration.ORIENTATION_PORTRAIT) {
            numContactsPerRow = 2;
        }
        else {
            numContactsPerRow = 4;
        }
        GridLayoutManager layoutManager = new GridLayoutManager(getActivity(), numContactsPerRow);
        recyclerView.setLayoutManager(layoutManager);

        return recyclerView;
    }

    @Override
    public void onResume() {
        super.onResume();
        updateData();
    }

    public void updateData () {
        ContactsAdapter adapter = (ContactsAdapter) recyclerView.getAdapter();
        if (adapter == null) {
            adapter = new ContactsAdapter(fetchAllContacts());
            recyclerView.setAdapter(adapter);
        }
        else {
            adapter.setContactNames(fetchAllContacts());
            adapter.notifyDataSetChanged();
        }
    }

    private List<String> fetchAllContacts () {
        DatabaseHelper databaseHelper = DatabaseHelper.getInstance(getActivity());
        SQLiteDatabase db = null;
        List<String> contactNames = null;
        try {
            db = databaseHelper.getReadableDatabase();
            Cursor cursor = db.query(DatabaseHelper.CONTACTS_TABLE,
                    new String [] {DatabaseHelper.CONTACT_ID, DatabaseHelper.CONTACT_NAME, DatabaseHelper.CONTACT_IMAGE},
                    null, null, null, null, null);

            contactNames = new ArrayList<>();
            if (cursor.moveToFirst()) {
                do {
                    int id = cursor.getInt(0);
                    String name = cursor.getString(1);
                    int image = cursor.getInt(2);
                    contactNames.add(name);
                } while (cursor.moveToNext());
            }

            cursor.close();

        }
        catch (SQLiteException e) {
            Log.d("Exception thrown!", "");
            e.printStackTrace();
        }
        finally {
            if (db != null) {
                db.close();
            }
        }
        return contactNames;
    }

}
