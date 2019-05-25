package $PACKAGE_NAME

import android.app.Activity
import android.app.Application
import android.content.Context
import com.theapache64.twinkill.TwinKill
import com.theapache64.twinkill.di.modules.ContextModule
import com.theapache64.twinkill.googlefonts.GoogleFonts
import com.theapache64.twinkill.network.di.modules.BaseNetworkModule
import com.theapache64.twinkill.network.utils.retrofit.interceptors.AuthorizationInterceptor
import com.theapache64.twinkill.network.utils.retrofit.interceptors.CurlInterceptor
import $PACKAGE_NAME.data.repositories.UserPrefRepository
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
            .baseNetworkModule(BaseNetworkModule(BASE_URL))
            .build()
            .inject(this)

        // TwinKill
        TwinKill.init(
            TwinKill
                .builder()
                .setNeedDeepCheckOnNetworkResponse(true)
                .addOkHttpInterceptor(AuthorizationInterceptor(userPrefRepository?.getUser()?.apiKey))
                .addOkHttpInterceptor(CurlInterceptor())
                .setDefaultFont(GoogleFonts.GoogleSansRegular)
                .build()
        )

    }


    companion object {
        private const val BASE_URL = "YOUR_API_URL_GOES_HERE"
    }
}
