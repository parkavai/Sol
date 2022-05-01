package com.example.soleklart.ui.bottomsheet

import android.content.Context
import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import androidx.lifecycle.LiveData
import androidx.lifecycle.Observer
import androidx.test.core.app.ApplicationProvider
import androidx.test.ext.junit.runners.AndroidJUnit4
import androidx.test.filters.SmallTest
import com.example.soleklart.ui.SharedViewModel
import com.google.android.gms.maps.model.LatLng
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.runBlocking
import kotlinx.coroutines.withContext
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith

@RunWith(AndroidJUnit4::class)
@SmallTest
class BottomSheetFragmentTest {
    @get:Rule
    val rule = InstantTaskExecutorRule()

    private lateinit var sharedViewModel: SharedViewModel
    private lateinit var bottomSheetViewModel: BottomSheetViewModel
    private lateinit var context: Context
    private var mockLatLng = LatLng(59.9, 10.8)

    @Before
    fun createViewModels() {
        context = ApplicationProvider.getApplicationContext()
        sharedViewModel = SharedViewModel()
        bottomSheetViewModel = BottomSheetViewModel(sharedViewModel)

    }

    @Test
    fun outputDataNotEmptyOnLatLongChange() {
        runBlocking {
            withContext(Dispatchers.Main) {
                sharedViewModel.setLatLng(mockLatLng)

            }
        }
        sharedViewModel.state.observeForTesting {
        }.also {
            bottomSheetViewModel.outData.observeForTesting {
                bottomSheetViewModel.outData.value?.let { it1 ->
                    assert(it1.isNotEmpty())
                }

            }
        }
    }

    /**
     * Observes a [LiveData] until the `block` is done executing.
     */
    private fun <T> LiveData<T>.observeForTesting(block: () -> Unit) {
        val observer = Observer<T> { }
        try {
            observeForever(observer)
            block()
        } finally {
            removeObserver(observer)
        }
    }
}

