package $PACKAGE_NAME.ui.activities.login

import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.util.Log
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.ViewModelProviders
import com.theapache64.twinkill.TwinKill
import com.theapache64.twinkill.network.utils.Resource
import com.theapache64.twinkill.network.utils.retrofit.interceptors.AuthorizationInterceptor
import com.theapache64.twinkill.ui.activities.base.BaseAppCompatActivity
import com.theapache64.twinkill.utils.extensions.bindContentView
import com.theapache64.twinkill.utils.extensions.toast
import $PACKAGE_NAME.R
import $PACKAGE_NAME.databinding.ActivityLogInBinding
import $PACKAGE_NAME.ui.activities.main.MainActivity
import dagger.android.AndroidInjection
import javax.inject.Inject

class LogInActivity : BaseAppCompatActivity(), LogInClickHandler {


    @Inject
    lateinit var factory: ViewModelProvider.Factory
    private lateinit var viewModel: LogInViewModel

    override fun onCreate(savedInstanceState: Bundle?) {
        AndroidInjection.inject(this)
        super.onCreate(savedInstanceState)

        // binding
        val binding = bindContentView<ActivityLogInBinding>(R.layout.activity_log_in)

        // hides progress for the first time
        binding.clpbLogin.hide()

        // getting viewModel
        this.viewModel = ViewModelProviders.of(this, factory).get(LogInViewModel::class.java)

        // binding viewModel to layout
        binding.viewModel = viewModel
        binding.clickHandler = this

        // watching for login
        viewModel.getLogIn().observe(this, Observer { it ->

            Log.e("X", "Status is ${it.status}")

            when (it.status) {

                Resource.Status.LOADING -> {
                    // showing progress
                    binding.clpbLogin.show()
                }

                Resource.Status.SUCCESS -> {
                    // hide loading
                    it.data?.let { response ->
                        binding.clpbLogin.hide()

                        // Setting auth interceptor value
                        setAuthInterceptorApiKey(response.data!!.theUser.apiKey)

                        // staring main activity
                        startActivity(MainActivity.getStartIntent(this))
                        finish()
                    }
                }

                Resource.Status.ERROR -> {
                    // hide loading
                    binding.clpbLogin.hide()

                    toast(it.message!!)
                }
            }
        })

    }

    fun setAuthInterceptorApiKey(apiKey: String) {
        var isKeySet = false
        for (interceptor in TwinKill.INSTANCE.interceptors) {
            if (interceptor is AuthorizationInterceptor) {
                interceptor.apiKey = apiKey
                isKeySet = true
                break
            }
        }

        require(isKeySet) { "Unable to find an AuthorizationInterceptor" }
    }

    override fun onLogInClicked() {
        viewModel.doLogIn()
    }

    companion object {
        const val ID = R.id.LOG_IN_ACTIVITY_ID

        fun getStartIntent(context: Context): Intent {
            val intent = Intent(context, LogInActivity::class.java)
            return intent
        }
    }
}
