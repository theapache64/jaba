package $PACKAGE_NAME.ui.activities.main

$LOGIN_IMPORTS
import javax.inject.Inject

class MainViewModel @Inject constructor(
   $USER_PREF_CONSTRUCTOR
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