package com.code.tusome.ui.fragments

import androidx.fragment.app.testing.FragmentScenario
import androidx.fragment.app.testing.launchFragmentInContainer
import androidx.lifecycle.Lifecycle
import androidx.test.espresso.Espresso.onView
import androidx.test.espresso.matcher.ViewMatchers.withId
import androidx.test.ext.junit.runners.AndroidJUnit4
import org.junit.Assert.*

import org.junit.Before
import org.junit.runner.RunWith
import com.code.tusome.R
import org.junit.Test

@RunWith(AndroidJUnit4::class)
class SplashFragmentTest {
private lateinit var scenario: FragmentScenario<SplashFragment>
    @Before
    fun setUp() {
        scenario = launchFragmentInContainer(themeResId = R.style.Theme_Tusome_FullScreen)
        scenario.moveToState(Lifecycle.State.INITIALIZED)
    }
    @Test
    fun logoVisible(){
        assertNotNull(onView(withId(R.id.logo_iv)))
    }
}