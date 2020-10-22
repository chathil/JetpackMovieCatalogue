package com.app.jetpackmoviecatalogue.ui.detail;

import android.content.Intent;
import android.os.Bundle;

import androidx.test.rule.ActivityTestRule;

import com.app.jetpackmoviecatalogue.R;
import com.app.jetpackmoviecatalogue.utils.CustomAction;
import com.app.jetpackmoviecatalogue.utils.FakeDataDummy;

import org.hamcrest.Matchers;
import org.junit.After;
import org.junit.Rule;
import org.junit.Test;

import java.util.ArrayList;

import static androidx.test.espresso.Espresso.onView;
import static androidx.test.espresso.action.ViewActions.swipeLeft;
import static androidx.test.espresso.assertion.ViewAssertions.matches;
import static androidx.test.espresso.matcher.ViewMatchers.isCompletelyDisplayed;
import static androidx.test.espresso.matcher.ViewMatchers.isDisplayed;
import static androidx.test.espresso.matcher.ViewMatchers.withId;
import static androidx.test.espresso.matcher.ViewMatchers.withText;

public class PlaceholderFragmentDetailTest {
    private static final String EXTRA_INDEX = "index";
    private static final String EXTRA_CONTENT = "content";
    private static final String SPARSE_ARRAY_KEY = "SPARSE_ARRAY_KEY";
    private static final String BUNDLE_KEY = "BUNDLE_KEY";

    @Rule
    public ActivityTestRule<DetailActivity> mActivityRule =
            new ActivityTestRule<DetailActivity>(DetailActivity.class) {
                @Override
                protected Intent getActivityIntent() {
                    Intent intent = new Intent();
                    Bundle bundle = new Bundle();
                    intent.putExtra(EXTRA_INDEX, 0);
                    bundle.putSparseParcelableArray(SPARSE_ARRAY_KEY, FakeDataDummy.generateDummyGenre());
                    intent.putExtra(BUNDLE_KEY, bundle);
                    intent.putParcelableArrayListExtra(EXTRA_CONTENT, new ArrayList<>(FakeDataDummy.generateDummyMovies()));
                    return intent;
                }
            };

    @Test
    public void swipe() {
        mActivityRule.getActivity();
        onView(Matchers.allOf(isCompletelyDisplayed(), withId(R.id.mv_title))).check(matches(withText("Terminator: Dark Fate")));
        onView(withId(R.id.view_pager)).check(matches(isDisplayed()));
        onView(withId(R.id.view_pager)).perform(CustomAction.customLeftSwipe());
        onView(withId(R.id.view_pager)).perform(CustomAction.customLeftSwipe());
    }

    @After
    public void after() {
        mActivityRule.finishActivity();
    }
}