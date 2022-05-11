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

    private fun setOnboardingItems() {
        onboardingItemsAdapter = OnboardingItemsAdapter(
            listOf(
                OnboardingItem(
                    onboardingImage = R.drawable.search_image2,
                    title = "Velg lokasjon for info om siktforhold",
                    description = "Her kan du søke i søkefeltet eller trykke på kartet"
                ),
                OnboardingItem(
                    onboardingImage = R.drawable.results_image2,
                    title = "Få info for valgt lokasjon",
                    description = "Her kan du se vær- og siktforhold under solnedgang og soloppgang"
                ),
                OnboardingItem(
                    onboardingImage = R.drawable.favorite_image2,
                    title = "Lagre dine favoritter",
                    description = "Her kan du slette og legge til, gøy!"
                ),
                OnboardingItem(
                    onboardingImage = R.drawable.sunfacts_image2,
                    title = "Les morsomme sunfacts!",
                    description = "Her kan du lære om solstråler og andre fenomener"
                ),
                OnboardingItem(
                    onboardingImage = R.drawable.settings_image2,
                    title = "Innstillinger",
                    description = "Bytt til darkmode, les spennende info om appen!"
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

    private fun navigateToHomeActivity() {
        startActivity(Intent(applicationContext, MainActivity::class.java))
        finish()
    }

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