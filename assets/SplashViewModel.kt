package $PACKAGE_NAME.ui.activities.splash

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.ViewModel
import com.theapache64.twinkill.utils.livedata.SingleLiveEvent
$LOGIN_IMPORTS
import $PACKAGE_NAME.ui.activities.main.MainActivity
import $PACKAGE_NAME.BuildConfig
import javax.inject.Inject

class SplashViewModel @Inject constructor(
    $USER_PREF_CONSTRUCTOR
) : ViewModel() {

    val versionName = "v${BuildConfig.VERSION_NAME}"

    private val launchActivityEvent = SingleLiveEvent<Int>()

    fun getLaunchActivityEvent(): LiveData<Int> {
        return launchActivityEvent
    }

    fun goToNextScreen() {

        $ACTIVITY_ID

        // passing id with the finish notification
        launchActivityEvent.value = activityId
    }

    companion object {
        val TAG = SplashViewModel::class.java.simpleName
    }

}