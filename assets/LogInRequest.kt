package $PACKAGE_NAME.data.remote.login

import com.squareup.moshi.Json

data class LogInRequest(
    @Json(name = "username") val username: String,
    @Json(name = "password") val password: String
)