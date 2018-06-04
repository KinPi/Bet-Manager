package com.kin.betmanager.adapters;

import android.content.Context;
import android.content.Intent;
import android.support.v7.widget.CardView;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.kin.betmanager.R;
import com.kin.betmanager.activities.BetDetailActivity;
import com.kin.betmanager.database.DatabaseHelper;
import com.kin.betmanager.objects.Bet;
import com.kin.betmanager.objects.Contact;

import java.util.List;

import jp.wasabeef.glide.transformations.CropCircleTransformation;

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
        final Bet bet = betsList.get(position);

        TextView titleTextView = cardView.findViewById(R.id.bet_card_view_title);
        titleTextView.setText(bet.title);

        final Contact contact;
        TextView bettingAgainstTextView = cardView.findViewById(R.id.bet_card_view_betting_against);
        if (holder.contact == null) {
            contact = DatabaseHelper.findContact(context, betsList.get(position).bettingAgainst);
        }
        else {
            contact = holder.contact;
        }
        bettingAgainstTextView.setText(contact.name);

        ImageView imageView = cardView.findViewById(R.id.profile_picture);
        if (contact == null || contact.image.isEmpty()) {
            Glide.with(context)
                    .load(R.drawable.default_user)
                    .bitmapTransform(new CropCircleTransformation(context))
                    .into(imageView);
        }
        else {
            Glide.with(context)
                    .load(contact.image)
                    .bitmapTransform(new CropCircleTransformation(context))
                    .into(imageView);
        }

        cardView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(context, BetDetailActivity.class);
                intent.putExtra(BetDetailActivity.BET, bet);
                intent.putExtra(BetDetailActivity.CONTACT, contact);
                context.startActivity(intent);
            }
        });
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
