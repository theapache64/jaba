package $PACKAGE_NAME.di.modules

import com.theapache64.twinkill.network.di.modules.BaseNetworkModule
import $PACKAGE_NAME.data.remote.ApiInterface
import dagger.Module
import dagger.Provides
import retrofit2.Retrofit
import javax.inject.Singleton

@Module(includes = [BaseNetworkModule::class])
class NetworkModule {

    // Interface
    @Singleton
    @Provides
    fun provideApiInterface(retrofit: Retrofit): ApiInterface {
        return retrofit.create(ApiInterface::class.java)
    }

}