package com.kin.betmanager;

import static android.support.test.espresso.Espresso.onView;
import static android.support.test.espresso.action.ViewActions.typeText;
import static android.support.test.espresso.matcher.ViewMatchers.withId;

import static android.support.test.espresso.action.ViewActions.click;

public class TestRoot {

    public void clickViewWithId (int id) {
        onView(withId(id)).perform(click());
    }

    public void enterText (int id, String text) {
        onView(withId(id)).perform(typeText(text));
    }
}
