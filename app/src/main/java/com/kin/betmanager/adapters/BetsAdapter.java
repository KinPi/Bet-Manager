package com.kin.betmanager.adapters;

import android.content.Context;
import android.support.v7.widget.CardView;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.ViewGroup;
import android.widget.TextView;

import com.kin.betmanager.R;
import com.kin.betmanager.database.DatabaseHelper;
import com.kin.betmanager.objects.Bet;
import com.kin.betmanager.objects.Contact;

import java.util.List;

/**
 * Created by Kin on 3/16/18.
 */

public class BetsAdapter extends RecyclerView.Adapter<BetsAdapter.ViewHolder> {

    private Context context;
    private List<Bet> betsList;

    public BetsAdapter (Context context, List<Bet> betsList) {
        this.context = context;
        this.betsList = betsList;
    }

    public void setBets (List<Bet> betsList) {
        this.betsList = betsList;
    }

    @Override
    public BetsAdapter.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        CardView cardView = (CardView) LayoutInflater.from(parent.getContext()).inflate(R.layout.bet_card_view, parent, false);
        return new ViewHolder(cardView);
    }

    @Override
    public void onBindViewHolder(BetsAdapter.ViewHolder holder, int position) {
        CardView cardView = holder.cardView;
        Bet bet = betsList.get(position);

        TextView titleTextView = cardView.findViewById(R.id.bet_card_view_title);
        titleTextView.setText(bet.title);

        Contact contact;
        TextView bettingAgainstTextView = cardView.findViewById(R.id.bet_card_view_betting_against);
        if (holder.contact == null) {
            contact = DatabaseHelper.findContact(context, betsList.get(position).bettingAgainst);
        }
        else {
            contact = holder.contact;
        }
        bettingAgainstTextView.setText(contact.name);
    }

    @Override
    public int getItemCount() {
        return betsList.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        private CardView cardView;
        Contact contact;

        public ViewHolder(CardView v) {
            super(v);
            cardView = v;
        }
    }
}
