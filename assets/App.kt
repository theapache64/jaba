package $PACKAGE_NAME

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
import dagger.android.HasAndroidInjector
import javax.inject.Inject

class App : Application(), HasAndroidInjector {

    $BASE_URL

    @Inject
    lateinit var androidInjector: DispatchingAndroidInjector<Any>

    override fun androidInjector(): AndroidInjector<Any> = androidInjector

    $USER_PREF_REPO_INJECT

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

}
