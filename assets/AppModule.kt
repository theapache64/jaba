package $PACKAGE_NAME.di.modules

import android.app.Application
$PREFERENCE_MODULE_IMPORT
import dagger.Module
import dagger.Provides
import dagger.android.support.AndroidSupportInjectionModule

@Module(
    includes = [
        AndroidSupportInjectionModule::class,
        $NETWORK_MODULE_INC
        $PREFERENCE_MODULE_INC
        ViewModelModule::class,
        ActivitiesBuilderModule::class
    ]
)
class AppModule(private val application: Application) {

    @Provides
    fun provideApplication(): Application {
        return this.application
    }

}