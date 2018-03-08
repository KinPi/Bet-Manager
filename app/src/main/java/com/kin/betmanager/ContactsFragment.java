package com.kin.betmanager;


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
    SQLiteDatabase db;
    List<String> contactNames;

    public ContactsFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        Log.v("Testing to see", "Hello World!");
        RecyclerView recyclerView = (RecyclerView) inflater.inflate(R.layout.fragment_contacts, container, false);
        DatabaseHelper databaseHelper = DatabaseHelper.getInstance(getActivity());
        try {
            db = databaseHelper.getReadableDatabase();
            Cursor cursor = db.query(DatabaseHelper.CONTACTS_TABLE,
                    new String [] {DatabaseHelper.CONTACT_ID, DatabaseHelper.CONTACT_NAME, DatabaseHelper.CONTACT_IMAGE},
                    null, null, null, null, null);

            contactNames = new ArrayList<>();
            Log.d("Cursor move to first", cursor.moveToFirst() + "");
            if (cursor.moveToFirst()) {
                do {
                    int id = cursor.getInt(0);
                    String name = cursor.getString(1);
                    int image = cursor.getInt(2);

                    Log.d("Cursor", id + " " + name + " " + image);
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

        ContactsAdapter adapter = new ContactsAdapter(contactNames);
        recyclerView.setAdapter(adapter);
        GridLayoutManager layoutManager = new GridLayoutManager(getActivity(), 2);
        recyclerView.setLayoutManager(layoutManager);

        return recyclerView;
    }

}
