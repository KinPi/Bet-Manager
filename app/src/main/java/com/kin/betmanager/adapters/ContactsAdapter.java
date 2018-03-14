package com.kin.betmanager.adapters;

import android.support.v7.widget.CardView;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.ViewGroup;
import android.widget.TextView;

import com.kin.betmanager.R;

import java.util.List;

/**
 * Created by Kin on 3/8/18.
 */

public class ContactsAdapter extends RecyclerView.Adapter<ContactsAdapter.ViewHolder> {
    private List<String> contactNames;

    public ContactsAdapter (List<String> contactNames) {
        this.contactNames = contactNames;
    }

    public void setContactNames (List<String> contactNames) {
        this.contactNames = contactNames;
    }

    @Override
    public int getItemCount () {
        return contactNames.size();
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
        nameTextView.setText(contactNames.get(position));
    }

    public static class ViewHolder extends RecyclerView.ViewHolder {
        private CardView cardView;

        public ViewHolder(CardView v) {
            super(v);
            cardView = v;
        }
    }

}
