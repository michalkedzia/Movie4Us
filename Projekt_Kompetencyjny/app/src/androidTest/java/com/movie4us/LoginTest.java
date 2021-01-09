package com.movie4us;

import androidx.test.core.app.ActivityScenario;
import androidx.test.espresso.Espresso;
import androidx.test.ext.junit.runners.AndroidJUnit4;
import loginRegister.Login;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;

import static androidx.test.espresso.Espresso.onView;
import static androidx.test.espresso.action.ViewActions.click;
import static androidx.test.espresso.action.ViewActions.typeText;
import static androidx.test.espresso.matcher.ViewMatchers.withId;
import static androidx.test.espresso.Espresso.onView;
import static androidx.test.espresso.assertion.ViewAssertions.matches;
import static androidx.test.espresso.matcher.ViewMatchers.isDisplayed;
import static androidx.test.espresso.matcher.ViewMatchers.withId;
import static org.hamcrest.Matchers.not;

/**
 * Instrumented test, which will execute on an Android device.
 *
 * @see <a href="http://d.android.com/tools/testing">Testing documentation</a>
 */
@RunWith(AndroidJUnit4.class)
public class LoginTest {

  @Before
  public void launchActivity() {
    ActivityScenario.launch(Login.class);
  }

  @Test
  public void testClickActionBarItem() {
    onView(withId(R.id.password)).perform(typeText("test"));
    onView(withId(R.id.username)).perform(typeText("test"));
    Espresso.closeSoftKeyboard();
    onView(withId(R.id.buttonLogin)).perform(click());

      onView(withId(R.id.usernameToConnect))
              .perform(typeText("test"));
  }

}
