package $PACKAGE_NAME.data.repositories

import android.content.SharedPreferences
import androidx.core.content.edit
import com.squareup.moshi.Moshi
import $PACKAGE_NAME.data.remote.login.LogInResponse
import javax.inject.Inject
import javax.inject.Singleton


@Singleton
class UserPrefRepository @Inject constructor(
    private val sharedPreferences: SharedPreferences,
    private val moshi: Moshi
) {

    /**
     * Getting remember me data from shared preference
     */
    fun getRememberMeData(): Triple<String, String, Boolean> {

        val username = sharedPreferences.getString(KEY_USERNAME, "")!!
        val password = sharedPreferences.getString(KEY_PASSWORD, "")!!
        val rememberMe = sharedPreferences.getBoolean(KEY_REMEMBER_ME, false)

        return Triple(username, password, rememberMe)
    }

    /**
     * Getting user from shared pref
     */
    fun getUser(): LogInResponse.User? {

        // getting json theUser string from pref
        val userJson: String? = sharedPreferences.getString(LogInResponse.User.KEY, null)
        var user: LogInResponse.User? = null
        if (userJson != null) {
            // converting JSON to Model
            val moshiAdapter = moshi.adapter(LogInResponse.User::class.java)
            user = moshiAdapter.fromJson(userJson)
        }

        return user
    }

    /**
     * Saves theUser object to shared pref
     */
    fun saveUser(user: LogInResponse.User) {
        sharedPreferences.edit {
            val moshiAdapter = moshi.adapter(LogInResponse.User::class.java)
            val userJson = moshiAdapter.toJson(user)
            putString(LogInResponse.User.KEY, userJson)
        }
    }

    /**
     * Clear user data from preference
     */
    fun clearUser() {
        sharedPreferences.edit {
            putString(LogInResponse.User.KEY, null)
        }
    }


    companion object {
        const val KEY_USERNAME = "username"
        const val KEY_PASSWORD = "password"
        const val KEY_REMEMBER_ME = "remember_me"
    }
}