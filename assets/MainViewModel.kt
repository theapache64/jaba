package $PACKAGE_NAME.ui.activities.main

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import $PACKAGE_NAME.data.repositories.UserPrefRepository
import javax.inject.Inject

class MainViewModel @Inject constructor(
    private val userRepository: UserPrefRepository
) : ViewModel() {


    private val isLoggedOut = MutableLiveData<Boolean>()
    fun getLoggedOut(): LiveData<Boolean> = isLoggedOut

    /**
     * Clears preference and logout user
     */
    fun logout() {
        userRepository.clearUser()
        isLoggedOut.value = true
    }
}