package $PACKAGE_NAME

import android.app.Activity
import android.app.Application
import android.content.Context
import com.theapache64.twinkill.TwinKill
$CONTEXT_MODULE_IMPORT
$GOOGLE_FONTS_IMPORT
$TWINKILL_NETWORK_MODULE_IMPORTS
$USER_REPOSITORY_IMPORT
import $PACKAGE_NAME.di.components.DaggerAppComponent
import dagger.android.AndroidInjector
import dagger.android.DispatchingAndroidInjector
import dagger.android.HasActivityInjector
import javax.inject.Inject

class App : Application(), HasActivityInjector {

    @Inject
    lateinit var activityInjector: DispatchingAndroidInjector<Activity>

    $USER_PREF_REPO_INJECT

    override fun activityInjector(): AndroidInjector<Activity> = activityInjector

    override fun onCreate() {
        super.onCreate()

        // Dagger
        DaggerAppComponent.builder()
            $CONTEXT_MODULE_INIT
            $DAGGER_NETWORK_MODULE_INIT
            .build()
            .inject(this)

        // TwinKill
        TwinKill.init(
            TwinKill
                .builder()
                $TWINKILL_NETWORK_MODULE_INIT
                $TWINKILL_AUTHORIZATION_INIT
                $GOOGLE_FONTS_INIT
                .build()
        )

    }


    companion object {
        private const val BASE_URL = "http://theapache64.com/mock_api/get_json/jaba/"
    }
}
