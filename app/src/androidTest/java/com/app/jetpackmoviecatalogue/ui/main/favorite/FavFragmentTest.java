package com.app.jetpackmoviecatalogue.ui.main.favorite;

import androidx.test.espresso.IdlingRegistry;
import androidx.test.espresso.contrib.RecyclerViewActions;
import androidx.test.rule.ActivityTestRule;

import com.app.jetpackmoviecatalogue.R;
import com.app.jetpackmoviecatalogue.ui.main.MainActivity;
import com.app.jetpackmoviecatalogue.utils.EspressoIdlingResource;
import com.app.jetpackmoviecatalogue.utils.CustomAction;

import org.junit.After;
import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;

import static androidx.test.espresso.Espresso.onView;
import static androidx.test.espresso.action.ViewActions.swipeLeft;
import static androidx.test.espresso.assertion.ViewAssertions.matches;
import static androidx.test.espresso.matcher.ViewMatchers.isCompletelyDisplayed;
import static androidx.test.espresso.matcher.ViewMatchers.isDisplayed;
import static androidx.test.espresso.matcher.ViewMatchers.withId;
import static org.hamcrest.core.AllOf.allOf;

public class FavFragmentTest {

    @Rule
    public ActivityTestRule<MainActivity> activityScenarioRule
            = new ActivityTestRule<>(MainActivity.class);

    @Before
    public void setUp() {
        IdlingRegistry.getInstance().register(EspressoIdlingResource.getEspressoIdlingResource());
    }

    @Test
    public void likeAndUnlikeTest() {
        //note: please make sure the first item (terminator) is not liked.
        // or like at least a movie in order to make this work.
        // it will be null if its empty and the test will fail.
        onView(allOf(isCompletelyDisplayed(), withId(R.id.rv_content))).perform(RecyclerViewActions.actionOnItemAtPosition(1, CustomAction.clickChildViewWithId(R.id.btn_like)));
        onView(withId(R.id.view_pager)).perform(swipeLeft());
        onView(withId(R.id.view_pager)).perform(swipeLeft());
        onView(withId(R.id.fav_pager)).check(matches(isDisplayed()));
        onView(allOf(isCompletelyDisplayed(), withId(R.id.rv_content))).perform(RecyclerViewActions.actionOnItemAtPosition(1, CustomAction.clickChildViewWithId(R.id.btn_like)));
    }

    @After
    public void after() {
        IdlingRegistry.getInstance().unregister(EspressoIdlingResource.getEspressoIdlingResource());
        activityScenarioRule.finishActivity();
    }

}