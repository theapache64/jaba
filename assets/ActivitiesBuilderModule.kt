package $PACKAGE_NAME.di.modules

$LOGIN_ACTIVITY_IMPORT
import $PACKAGE_NAME.ui.activities.main.MainActivity
$SPLASH_ACTIVITY_IMPORT
import dagger.Module
import dagger.android.ContributesAndroidInjector

@Module
abstract class ActivitiesBuilderModule {

    /**
     * Activity
     */
    $SPLASH_ACTIVITY_BUILDER

    $LOGIN_ACTIVITY_BUILDER

    @ContributesAndroidInjector
    abstract fun getMainActivity(): MainActivity
}