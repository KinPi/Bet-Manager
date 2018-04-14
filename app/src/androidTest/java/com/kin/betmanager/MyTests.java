package com.kin.betmanager;

import android.database.sqlite.SQLiteDatabase;
import android.support.test.espresso.Espresso;
import android.support.test.rule.ActivityTestRule;
import android.support.test.runner.AndroidJUnit4;

import com.kin.betmanager.activities.MainActivity;
import com.kin.betmanager.database.DatabaseHelper;

import org.junit.After;
import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.junit.runner.RunWith;

import static android.support.test.espresso.Espresso.onView;
import static android.support.test.espresso.assertion.ViewAssertions.matches;
import static android.support.test.espresso.matcher.ViewMatchers.isDisplayed;
import static android.support.test.espresso.matcher.ViewMatchers.withText;

@RunWith(AndroidJUnit4.class)
public class MyTests extends TestRoot {

    @Rule
    public ActivityTestRule<MainActivity> mMainActivity = new ActivityTestRule(MainActivity.class);

    @After
    public void tearDown () {
        // Wipe Database
        DatabaseHelper helper = DatabaseHelper.getInstance(mMainActivity.getActivity());
        SQLiteDatabase db = helper.getWritableDatabase();
        db.delete(DatabaseHelper.BETS_TABLE, null, null);
        db.delete(DatabaseHelper.CONTACTS_TABLE, null, null);
    }

    @Test
    public void testCreateNewBet () {
        clickViewWithId(R.id.action_create_new_bet);

        String title = "Test Create New Bet";
        String bettingAgainst = "Dummy User";

        enterText(R.id.title_edittext, title);
        enterText(R.id.betting_against_edittext, bettingAgainst);
        enterText(R.id.opponents_bet_edittext, "Nothing!");
        enterText(R.id.your_bet_edittext, "$10");
        Espresso.closeSoftKeyboard();
        enterText(R.id.terms_and_conditions_edittext, "Just a test.");
        clickViewWithId(R.id.action_done);

        onView(withText(title)).check(matches(isDisplayed()));
        onView(withText(bettingAgainst)).check(matches(isDisplayed()));
    }

}
