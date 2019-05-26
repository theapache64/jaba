package $PACKAGE_NAME.ui.activities.splash

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.ViewModel
import com.theapache64.twinkill.utils.livedata.SingleLiveEvent
import $PACKAGE_NAME.data.repositories.UserPrefRepository
import $PACKAGE_NAME.ui.activities.login.LogInActivity
import $PACKAGE_NAME.ui.activities.main.MainActivity
import javax.inject.Inject

class SplashViewModel @Inject constructor(
    private val userPrefRepository: UserPrefRepository
) : ViewModel() {

    private val launchActivityEvent = SingleLiveEvent<Int>()

    fun getLaunchActivityEvent(): LiveData<Int> {
        return launchActivityEvent
    }

    fun checkUser() {
        // if theUser == null -> login else main
        val user = userPrefRepository.getUser()
        val activityId = if (user == null) LogInActivity.ID else MainActivity.ID

        Log.i(TAG, "User is ${user?.name}")

        // passing id with the finish notification
        launchActivityEvent.notifyFinished(activityId)
    }

    companion object {
        val TAG = SplashViewModel::class.java.simpleName
    }

}