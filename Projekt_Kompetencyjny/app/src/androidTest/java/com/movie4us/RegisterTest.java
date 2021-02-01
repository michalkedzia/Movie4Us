package com.movie4us;

import androidx.test.core.app.ActivityScenario;
import androidx.test.espresso.Espresso;
import androidx.test.ext.junit.runners.AndroidJUnit4;
import loginRegister.Login;
import loginRegister.SignUp;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;

import static androidx.test.espresso.Espresso.onView;
import static androidx.test.espresso.action.ViewActions.click;
import static androidx.test.espresso.action.ViewActions.typeText;
import static androidx.test.espresso.Espresso.onView;
import static androidx.test.espresso.assertion.ViewAssertions.matches;
import static androidx.test.espresso.matcher.ViewMatchers.*;
import static androidx.test.espresso.matcher.ViewMatchers.withText;
import static org.hamcrest.Matchers.not;
@RunWith(AndroidJUnit4.class)
public class RegisterTest {

    @Before
    public void launchActivity() {
        ActivityScenario.launch(SignUp.class);
    }

    @Test
    public void userAlreadyRegister() throws InterruptedException {
        onView(withId(R.id.password)).perform(typeText("test"));
        onView(withId(R.id.username)).perform(typeText("test"));
        onView(withId(R.id.email)).perform(typeText("test@edu.pl"));
        onView(withId(R.id.fullname)).perform(typeText("test"));
        Espresso.closeSoftKeyboard();
        onView(withId(R.id.buttonSignUp)).perform(click());
        onView(withId(R.id.buttonSignUp))
                .check(matches(withText("Sign up".toUpperCase())));
    }

    @Test
    public void shouldReturnSignUpFailedWithNoData() {
        onView(withId(R.id.buttonSignUp)).perform(click());
        onView(withId(R.id.buttonSignUp))
                .check(matches(withText("Sign up Failed! Try again".toUpperCase())));
    }
}
