package $PACKAGE_NAME

import android.app.Activity
import android.app.Application
import android.content.Context
import com.theapache64.twinkill.TwinKill
import com.theapache64.twinkill.di.modules.ContextModule
import com.theapache64.twinkill.googlefonts.GoogleFonts
$TWINKILL_NETWORK_MODULE_IMPORTS
import com.theapache64.twinkill.network.utils.retrofit.interceptors.AuthorizationInterceptor
import com.theapache64.twinkill.network.utils.retrofit.interceptors.CurlInterceptor
$USER_REPOSITORY_IMPORT
import $PACKAGE_NAME.di.components.DaggerAppComponent
import dagger.android.AndroidInjector
import dagger.android.DispatchingAndroidInjector
import dagger.android.HasActivityInjector
import javax.inject.Inject

class App : Application(), HasActivityInjector {

    @Inject
    lateinit var activityInjector: DispatchingAndroidInjector<Activity>

    var userPrefRepository: UserPrefRepository? = null
        @Inject set

    override fun activityInjector(): AndroidInjector<Activity> = activityInjector

    override fun onCreate() {
        super.onCreate()

        // Dagger
        DaggerAppComponent.builder()
            .contextModule(ContextModule(this))
            $DAGGER_NETWORK_MODULE_INIT
            .build()
            .inject(this)

        // TwinKill
        TwinKill.init(
            TwinKill
                .builder()
                $TWINKILL_NETWORK_MODULE_INIT
                $TWINKILL_AUTHORIZATION_INIT
                .setDefaultFont(GoogleFonts.GoogleSansRegular)
                .build()
        )

    }


    companion object {
        private const val BASE_URL = "http://theapache64.com/mock_api/get_json/jaba/"
    }
}
