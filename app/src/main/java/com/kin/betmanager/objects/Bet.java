package com.kin.betmanager.objects;

/**
 * Created by Kin on 3/14/18.
 */

public class Bet {
    public long id;
    public String title;
    public long bettingAgainst;
    public String opponentsBet;
    public String yourBet;
    public String termsAndConditions;
    public boolean isCompleted;

    public Bet (long id, String title, long bettingAgainst, String opponentsBet, String yourBet, String termsAndConditions, boolean isCompleted) {
        this.id = id;
        this.title = title;
        this.bettingAgainst = bettingAgainst;
        this.opponentsBet = opponentsBet;
        this.yourBet = yourBet;
        this.termsAndConditions = termsAndConditions;
        this.isCompleted = isCompleted;
    }
}
