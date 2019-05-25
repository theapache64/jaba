package $PACKAGE_NAME.data.remote.login

import com.squareup.moshi.Json
import com.theapache64.twinkill.network.data.remote.base.BaseApiResponse


/**
 * Generated using MockAPI (https://github.com/theapache64/Mock-API) : Wed Jan 16 14:49:46 UTC 2019
 */
class LogInResponse(error: Boolean, message: String, data: Data?) :
    BaseApiResponse<LogInResponse.Data>(error, message, data) {

    data class Data(
        @Json(name = "user") val theUser: User
    )

    data class User(
        @Json(name = "name") val name: String,
        @Json(name = "api_key") val apiKey: String
    ) {
        companion object {
            val KEY = User::class.java.name
        }
    }
}