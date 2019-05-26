package $PACKAGE_NAME.di.modules

import androidx.lifecycle.ViewModel
import com.theapache64.twinkill.di.modules.BaseViewModelModule
import com.theapache64.twinkill.utils.viewmodel.ViewModelKey
//import $PACKAGE_NAME.ui.activities.login.LogInViewModel
import $PACKAGE_NAME.ui.activities.main.MainViewModel
//import $PACKAGE_NAME.ui.activities.splash.SplashViewModel
import dagger.Binds
import dagger.Module
import dagger.multibindings.IntoMap


@Module(includes = [BaseViewModelModule::class])
abstract class ViewModelModule {


    /*@Binds
    @IntoMap
    @ViewModelKey(SplashViewModel::class)
    abstract fun bindSplashViewModel(viewModel: SplashViewModel): ViewModel

    @Binds
    @IntoMap
    @ViewModelKey(LogInViewModel::class)
    abstract fun bindLogInViewModel(viewModel: LogInViewModel): ViewModel*/

    @Binds
    @IntoMap
    @ViewModelKey(MainViewModel::class)
    abstract fun bindMainViewModel(viewModel: MainViewModel): ViewModel
}