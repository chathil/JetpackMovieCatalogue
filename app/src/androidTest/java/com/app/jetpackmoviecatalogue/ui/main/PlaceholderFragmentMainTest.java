package com.app.jetpackmoviecatalogue.ui.main;

import androidx.test.espresso.IdlingRegistry;
import androidx.test.espresso.contrib.RecyclerViewActions;
import androidx.test.rule.ActivityTestRule;

import com.app.jetpackmoviecatalogue.R;
import com.app.jetpackmoviecatalogue.utils.CustomAction;
import com.app.jetpackmoviecatalogue.utils.EspressoIdlingResource;

import org.junit.After;
import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;

import static androidx.test.espresso.Espresso.onView;
import static androidx.test.espresso.action.ViewActions.click;
import static androidx.test.espresso.assertion.ViewAssertions.matches;
import static androidx.test.espresso.matcher.ViewMatchers.isCompletelyDisplayed;
import static androidx.test.espresso.matcher.ViewMatchers.isDisplayed;
import static androidx.test.espresso.matcher.ViewMatchers.withId;
import static org.hamcrest.core.AllOf.allOf;

public class PlaceholderFragmentMainTest {

    @Rule
    public ActivityTestRule<MainActivity> activityScenarioRule
            = new ActivityTestRule<>(MainActivity.class);


    @Before
    public void setUp() {
        IdlingRegistry.getInstance().register(EspressoIdlingResource.getEspressoIdlingResource());
    }

    @Test
    public void swipeTest() {
        onView(withId(R.id.view_pager)).check(matches(isDisplayed()));
        onView(withId(R.id.view_pager)).perform(CustomAction.customLeftSwipe());
        onView(withId(R.id.view_pager)).perform(CustomAction.customLeftSwipe());
    }

    @Test
    public void clickAndDetailSwipeTest() {
        onView(allOf(isCompletelyDisplayed(), withId(R.id.rv_content))).perform(RecyclerViewActions.actionOnItemAtPosition(2, click()));
        onView(withId(R.id.view_pager)).check(matches(isDisplayed()));
        onView(withId(R.id.view_pager)).perform(CustomAction.customLeftSwipe());
        onView(withId(R.id.view_pager)).perform(CustomAction.customLeftSwipe());
        onView(withId(R.id.view_pager)).perform(CustomAction.customLeftSwipe());
        onView(withId(R.id.view_pager)).perform(CustomAction.customLeftSwipe());
    }

    @Test
    public void likeTest() {
        onView(allOf(isCompletelyDisplayed(), withId(R.id.rv_content))).perform(RecyclerViewActions.actionOnItemAtPosition(2, CustomAction.clickChildViewWithId(R.id.btn_like)));
        onView(withId(R.id.view_pager)).perform(CustomAction.customLeftSwipe());
        onView(withId(R.id.view_pager)).perform(CustomAction.customLeftSwipe());
    }

    @After
    public void after() {
        IdlingRegistry.getInstance().unregister(EspressoIdlingResource.getEspressoIdlingResource());
        activityScenarioRule.finishActivity();
    }
}