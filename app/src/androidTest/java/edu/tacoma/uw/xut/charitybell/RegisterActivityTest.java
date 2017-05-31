package edu.tacoma.uw.xut.charitybell;

import android.content.Context;
import android.support.test.InstrumentationRegistry;
import android.support.test.rule.ActivityTestRule;
import android.support.test.runner.AndroidJUnit4;

import org.junit.Rule;
import org.junit.Test;
import org.junit.runner.RunWith;

import java.util.Random;

import static android.support.test.espresso.Espresso.onView;
import static android.support.test.espresso.action.ViewActions.click;
import static android.support.test.espresso.action.ViewActions.typeText;
import static android.support.test.espresso.assertion.ViewAssertions.matches;
import static android.support.test.espresso.matcher.RootMatchers.withDecorView;
import static android.support.test.espresso.matcher.ViewMatchers.isDisplayed;
import static android.support.test.espresso.matcher.ViewMatchers.withId;
import static android.support.test.espresso.matcher.ViewMatchers.withText;
import static org.hamcrest.Matchers.is;
import static org.hamcrest.core.IsNot.not;
import static org.junit.Assert.*;

/**
 * Instrumentation test, which will execute on an Android device.
 *
 * @see <a href="http://d.android.com/tools/testing">Testing documentation</a>
 */
@RunWith(AndroidJUnit4.class)
public class RegisterActivityTest {

    @Rule
    public ActivityTestRule<LoginActivity> mActivityRule = new ActivityTestRule<>(
            LoginActivity.class);


    @Test
    public void useAppContext() throws Exception {
        // Context of the app under test.
        Context appContext = InstrumentationRegistry.getTargetContext();

        assertEquals("edu.tacoma.uw.xut.charitybell", appContext.getPackageName());
    }

    @Test
    public void testARegister() {

        Random random = new Random();
        //Generate an email address
        String email = "email" + (random.nextInt(400) + 1)
                + (random.nextInt(900) + 1) + (random.nextInt(700) + 1)
                + (random.nextInt(400) + 1) + (random.nextInt(100) + 1)
                + "@uw.edu";

        onView(withId(R.id.registerTextLink))
                .perform(click());

        // Type text and then press the button.
        onView(withId(R.id.nameRegister))
                .perform(typeText("TestName"));
        onView(withId(R.id.emailRegister))
                .perform(typeText(email));
        onView(withId(R.id.passwordRegister))
                .perform(typeText("test1@#"));
        onView(withId(R.id.registerButton))
                .perform(click());

    }
}
