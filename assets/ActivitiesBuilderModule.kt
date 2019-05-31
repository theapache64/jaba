package $PACKAGE_NAME.di.modules

$LOGIN_ACTIVITY_IMPORT
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

    $LOGIN_ACTIVITY_BUILDER

    @ContributesAndroidInjector
    abstract fun getMainActivity(): MainActivity
}