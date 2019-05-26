package $PACKAGE_NAME.data.repositories

import androidx.lifecycle.LiveData
import com.theapache64.twinkill.network.utils.Resource
import $PACKAGE_NAME.data.remote.ApiInterface
import $PACKAGE_NAME.data.remote.login.LogInRequest
import $PACKAGE_NAME.data.remote.login.LogInResponse
import javax.inject.Inject
import javax.inject.Singleton


@Singleton
class AuthRepository @Inject constructor(
    private val apiInterface: ApiInterface
) {

    /**
     * do API call to login route
     */
    fun login(logInRequest: LogInRequest): LiveData<Resource<LogInResponse>> {
        return apiInterface.login(logInRequest)
    }
}