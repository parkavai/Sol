package com.example.soleklart.ui.onBoarding

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.ViewGroup.LayoutParams.WRAP_CONTENT
import android.widget.ImageView
import android.widget.LinearLayout
import androidx.core.content.ContextCompat
import androidx.recyclerview.widget.RecyclerView
import androidx.viewpager2.widget.ViewPager2
import com.example.soleklart.R
import com.example.soleklart.databinding.OnboardingActivityBinding
import com.example.soleklart.ui.MainActivity

/**
 * Fragment containing an introduction to the application (onboarding), showing the
 * user the main functionalities of the app with a written description of how they can
 * be used. The fragment will run only once; on the first time of interacting the app.
 */
class OnBoardingActivity : AppCompatActivity() {

    private lateinit var onboardingItemsAdapter: OnboardingItemsAdapter
    private lateinit var indicatorsContainer: LinearLayout
    private lateinit var binding: OnboardingActivityBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = OnboardingActivityBinding.inflate(layoutInflater)
        setContentView(binding.root)

        setOnboardingItems()
        setupIndicators()
        setCurrentIndicator(0)
    }

    /**
     * setOnboardingItems sets onboarding for adapter
     */
    private fun setOnboardingItems() {
        onboardingItemsAdapter = OnboardingItemsAdapter(
            listOf(
                OnboardingItem(
                    onboardingImage = R.drawable.search_image2,
                    title = getString(R.string.search_title_onBoarding),
                    description = getString(R.string.search_description_onBoarding)
                ),
                OnboardingItem(
                    onboardingImage = R.drawable.results_image2,
                    title = getString(R.string.results_title_onBoarding),
                    description = getString(R.string.results_description_onBoarding)
                ),
                OnboardingItem(
                    onboardingImage = R.drawable.favorite_image2,
                    title = getString(R.string.favorite_title_onBoarding),
                    description = getString(R.string.favorite_description_onBoarding)
                ),
                OnboardingItem(
                    onboardingImage = R.drawable.sunfacts_image2,
                    title = getString(R.string.sunfacts_title_onBoarding),
                    description = getString(R.string.sunfacts_description_onBoarding)
                ),
                OnboardingItem(
                    onboardingImage = R.drawable.settings_image2,
                    title = getString(R.string.settings_title_onBoarding),
                    description = getString(R.string.settings_description_onBoarding)
                ),
            )
        )

        binding.onBoardingViewPager.adapter = onboardingItemsAdapter
        binding.onBoardingViewPager.registerOnPageChangeCallback(object :
            ViewPager2.OnPageChangeCallback() {
            override fun onPageSelected(position: Int) {
                super.onPageSelected(position)
                setCurrentIndicator(position)
            }
        })
        (binding.onBoardingViewPager.getChildAt(0) as RecyclerView).overScrollMode =
            RecyclerView.OVER_SCROLL_NEVER
        binding.ivNext.setOnClickListener {
            if (binding.onBoardingViewPager.currentItem + 1 < onboardingItemsAdapter.itemCount) {
                binding.onBoardingViewPager.currentItem += 1
            } else {
                navigateToHomeActivity()
            }
        }
        binding.buttonSkip.setOnClickListener {
            navigateToHomeActivity()
        }
    }

    /**
     * navigateToHomeActivity changes activity to main activity
     */
    private fun navigateToHomeActivity() {
        startActivity(Intent(applicationContext, MainActivity::class.java))
        finish()
    }

    /**
     * sets up top indicators for changing/navigation between onboardingItems
     */
    private fun setupIndicators() {
        indicatorsContainer = binding.indicatorsContainer
        val indicators = arrayOfNulls<ImageView>(onboardingItemsAdapter.itemCount)
        val layoutParams: LinearLayout.LayoutParams =
            LinearLayout.LayoutParams(WRAP_CONTENT, WRAP_CONTENT)
        layoutParams.setMargins(8, 0, 8, 0)
        for (i in indicators.indices) {
            indicators[i] = ImageView(applicationContext)
            indicators[i]?.let {
                it.setImageDrawable(
                    ContextCompat.getDrawable(
                        applicationContext,
                        R.drawable.indicator_inactive_background
                    )
                )
                it.layoutParams = layoutParams
                indicatorsContainer.addView(it)
            }
        }
    }

    /**
     * sets indicator at current position
     */
    private fun setCurrentIndicator(position: Int) {
        val childCount = indicatorsContainer.childCount
        for (i in 0 until childCount) {
            val imageView = indicatorsContainer.getChildAt(i) as ImageView
            if (i == position) {
                imageView.setImageDrawable(
                    ContextCompat.getDrawable(
                        applicationContext,
                        R.drawable.indicator_active_background
                    )
                )
            } else {
                imageView.setImageDrawable(
                    ContextCompat.getDrawable(
                        applicationContext,
                        R.drawable.indicator_inactive_background
                    )
                )
            }
        }
    }
}