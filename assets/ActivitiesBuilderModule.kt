package $PACKAGE_NAME.di.modules

import $PACKAGE_NAME.ui.activities.login.LogInActivity
import $PACKAGE_NAME.ui.activities.main.MainActivity
import $PACKAGE_NAME.ui.activities.splash.SplashActivity
import dagger.Module
import dagger.android.ContributesAndroidInjector

@Module
abstract class ActivitiesBuilderModule {

    /**
     * Activity
     */
    @ContributesAndroidInjector
    abstract fun getSplashActivity(): SplashActivity

    @ContributesAndroidInjector
    abstract fun getLogInActivity(): LogInActivity

    @ContributesAndroidInjector
    abstract fun getMainActivity(): MainActivity
}