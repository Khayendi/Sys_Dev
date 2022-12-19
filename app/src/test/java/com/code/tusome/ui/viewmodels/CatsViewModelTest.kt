package com.code.tusome.ui.viewmodels

import com.code.tusome.Tusome
import com.code.tusome.models.Cat
import junit.framework.TestCase.assertNull
import junit.framework.TestCase.assertTrue
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.ExperimentalCoroutinesApi
import okhttp3.Dispatcher
import org.junit.After
import org.junit.Assert
import org.junit.Before
import org.junit.Rule
import org.junit.Test
@OptIn(ExperimentalCoroutinesApi::class)
class CatsViewModelTest {
    private lateinit var catsViewModel: CatsViewModel
    val app = Tusome()
//    @get:Rule
//    val instantTaskRule = InstantTaskExecutorRule()
//
//    // Make sure viewModelScope uses a test dispatcher
//    @get:Rule
//    val coroutinesDispatcherRule = CoroutineDispatcherRule()

    @Before
    fun setUp() {
        catsViewModel = CatsViewModel(app)
    }

    @After
    fun tearDown() {
    }

    @Test
    fun addCat() {

        val cat = catsViewModel.addCat(
            "test_unit",
            Cat("test", "test name", "test descr", "scci", "12/11/2022", "2hrs", "test invigilator")
        )
        assertNull(cat)
    }

    @Test
    fun getAllCats() {
    }

    @Test
    fun updateCat() {
    }

    @Test
    fun deleteCat() {
    }
}