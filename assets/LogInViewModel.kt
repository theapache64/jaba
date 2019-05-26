package $PACKAGE_NAME.ui.activities.login

import android.util.Log
import androidx.lifecycle.*
import com.theapache64.twinkill.network.utils.Resource
import $PACKAGE_NAME.data.remote.login.LogInRequest
import $PACKAGE_NAME.data.remote.login.LogInResponse
import $PACKAGE_NAME.data.repositories.AuthRepository
import $PACKAGE_NAME.data.repositories.UserPrefRepository
import javax.inject.Inject

class LogInViewModel @Inject constructor(
    private val authRepository: AuthRepository,
    private val userPrefRepository: UserPrefRepository
) : ViewModel() {

    // layout data
    var username: String
    var password: String
    var rememberMe: Boolean

    // activity data
    private val logInRequest = MutableLiveData<LogInRequest>()
    private val logInResponse = Transformations.switchMap(logInRequest, authRepository::login)

    private val logInMerger = MediatorLiveData<Resource<LogInResponse>>()

    init {

        val (username, password, rememberMe) = userPrefRepository.getRememberMeData()

        this.username = username
        this.password = password
        this.rememberMe = rememberMe

        // if login response change
        logInMerger.addSource(logInResponse) {

            Log.d("X", "Change occurred to loginResponse ${it.status}")

            if (it.status == Resource.Status.SUCCESS) {
                // logged in, save theUser
                userPrefRepository.saveUser(it.data!!.data!!.theUser)
            }

            logInMerger.value = it
        }
    }

    fun doLogIn() {

        // small validation
        if (this.username.trim().isEmpty() || this.password.trim().isEmpty()) {
            logInMerger.value = Resource.error("Invalid credentials")
            return
        }

        logInRequest.value = LogInRequest(
            username, password
        )
    }

    fun getLogIn(): LiveData<Resource<LogInResponse>> = logInMerger


}