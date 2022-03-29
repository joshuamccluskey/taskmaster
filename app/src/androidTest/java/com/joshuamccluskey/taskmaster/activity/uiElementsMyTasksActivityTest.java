package com.joshuamccluskey.taskmaster.activity;


import static androidx.test.espresso.Espresso.onView;
import static androidx.test.espresso.assertion.ViewAssertions.matches;
import static androidx.test.espresso.matcher.ViewMatchers.isDisplayed;
import static androidx.test.espresso.matcher.ViewMatchers.withId;
import static androidx.test.espresso.matcher.ViewMatchers.withParent;
import static androidx.test.espresso.matcher.ViewMatchers.withText;
import static org.hamcrest.Matchers.allOf;

import androidx.test.espresso.ViewInteraction;
import androidx.test.filters.LargeTest;
import androidx.test.rule.ActivityTestRule;
import androidx.test.runner.AndroidJUnit4;

import com.joshuamccluskey.taskmaster.R;

import org.junit.Rule;
import org.junit.Test;
import org.junit.runner.RunWith;

@LargeTest
@RunWith(AndroidJUnit4.class)
public class uiElementsMyTasksActivityTest {

    @Rule
    public ActivityTestRule<MyTasksActivity> mActivityTestRule = new ActivityTestRule<>(MyTasksActivity.class);

    @Test
    public void uiElementsMyTasksActivityTest() {
        ViewInteraction recyclerView = onView(
                allOf(withId(R.id.tasksListRecycleView),
                        withParent(withParent(withId(android.R.id.content))),
                        isDisplayed()));
        recyclerView.check(matches(isDisplayed()));

        ViewInteraction button = onView(
                allOf(withId(R.id.goToAddTaskButton), withText("ADD TASK"),
                        withParent(withParent(withId(android.R.id.content))),
                        isDisplayed()));
        button.check(matches(isDisplayed()));
    }
}
