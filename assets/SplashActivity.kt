package $PACKAGE_NAME.ui.activities.splash

import android.os.Bundle
import android.os.Handler
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.ViewModelProviders
import com.theapache64.twinkill.ui.activities.base.BaseAppCompatActivity
import $PACKAGE_NAME.databinding.ActivitySplashBinding
import $PACKAGE_NAME.R

$LOGIN_ACTIVITY_IMPORT
import $PACKAGE_NAME.ui.activities.main.MainActivity
import dagger.android.AndroidInjection
import javax.inject.Inject

import com.theapache64.twinkill.utils.extensions.bindContentView

class SplashActivity : BaseAppCompatActivity() {

    @Inject
    lateinit var factory: ViewModelProvider.Factory

    override fun onCreate(savedInstanceState: Bundle?) {
        AndroidInjection.inject(this)
        super.onCreate(savedInstanceState)

        val binding = bindContentView<ActivitySplashBinding>(R.layout.activity_splash)
        val viewModel = ViewModelProviders.of(this, factory).get(SplashViewModel::class.java)
        binding.viewModel = viewModel

        // Watching activity launch command
        viewModel.getLaunchActivityEvent().observe(this, Observer { activityId ->

            when (activityId) {
                MainActivity.ID -> {
                    startActivity(MainActivity.getStartIntent(this))
                }
                    $LOGIN_LAUNCHER
                else -> throw IllegalArgumentException("Undefined activity id $activityId")
            }

            finish()

        })

        // Starting splash timer
        Handler().postDelayed({
            viewModel.goToNextScreen()
        }, SPLASH_DURATION)

    }


    companion object {
        private const val SPLASH_DURATION = 1000L
    }

}
