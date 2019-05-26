package $PACKAGE_NAME.data.remote

import androidx.lifecycle.LiveData
import com.theapache64.twinkill.network.utils.Resource
import $PACKAGE_NAME.data.remote.login.LogInRequest
import $PACKAGE_NAME.data.remote.login.LogInResponse
import retrofit2.http.Body
import retrofit2.http.POST

interface ApiInterface {

    @POST("login")
    fun login(@Body logInRequest: LogInRequest): LiveData<Resource<LogInResponse>>
}

