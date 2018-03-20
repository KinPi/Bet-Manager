package com.kin.betmanager.adapters;

import android.content.Context;
import android.content.Intent;
import android.support.v7.widget.CardView;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.kin.betmanager.activities.ContactDetailActivity;
import com.kin.betmanager.R;
import com.kin.betmanager.objects.Contact;

import java.util.List;

/**
 * Created by Kin on 3/8/18.
 */

public class ContactsAdapter extends RecyclerView.Adapter<ContactsAdapter.ViewHolder> {
    public static final String CONTACT = "contact";

    private Context context;
    private List<Contact> contacts;

    public ContactsAdapter (Context context, List<Contact> contacts) {
        this.context = context;
        this.contacts = contacts;
    }

    public void setContacts(List<Contact> contacts) {
        this.contacts = contacts;
    }

    @Override
    public int getItemCount () {
        return contacts.size();
    }

    @Override
    public ContactsAdapter.ViewHolder onCreateViewHolder (ViewGroup parent, int viewType) {
        CardView cardView = (CardView) LayoutInflater.from(parent.getContext()).inflate(R.layout.contact_card_view, parent, false);
        return new ViewHolder(cardView);
    }

    @Override
    public void onBindViewHolder (ViewHolder holder, final int position) {
        CardView cardView = holder.cardView;
        TextView nameTextView = (TextView) cardView.findViewById (R.id.contact_name_textview);
        nameTextView.setText(contacts.get(position).name);
        cardView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(ContactsAdapter.this.context, ContactDetailActivity.class);
                intent.putExtra(CONTACT, contacts.get(position));
                ContactsAdapter.this.context.startActivity(intent);
            }
        });
    }

    public static class ViewHolder extends RecyclerView.ViewHolder {
        private CardView cardView;

        public ViewHolder(CardView v) {
            super(v);
            cardView = v;
        }
    }

}
