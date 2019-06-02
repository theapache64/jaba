package $PACKAGE_NAME.di.modules

import androidx.lifecycle.ViewModel
import com.theapache64.twinkill.di.modules.BaseViewModelModule
import com.theapache64.twinkill.utils.viewmodel.ViewModelKey
$LOGIN_VM_IMPORT
import $PACKAGE_NAME.ui.activities.main.MainViewModel
$SPLASH_VM_IMPORT
import dagger.Binds
import dagger.Module
import dagger.multibindings.IntoMap


@Module(includes = [BaseViewModelModule::class])
abstract class ViewModelModule {


    $SPLASH_VM_BIND

    $LOGIN_VM_BIND

    @Binds
    @IntoMap
    @ViewModelKey(MainViewModel::class)
    abstract fun bindMainViewModel(viewModel: MainViewModel): ViewModel
}