package $PACKAGE_NAME.ui.activities.some

import android.content.Context
import android.content.Intent
import android.os.Bundle
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.ViewModelProviders
import $PACKAGE_NAME.R
import $PACKAGE_NAME.databinding.ActivitySomeBinding
import com.theapache64.twinkill.ui.activities.base.BaseAppCompatActivity
import com.theapache64.twinkill.utils.extensions.bindContentView
import dagger.android.AndroidInjection
import javax.inject.Inject

class SomeActivity : BaseAppCompatActivity(), SomeHandler {

    companion object {
        fun getStartIntent(context: Context): Intent {
            return Intent(context, SomeActivity::class.java).apply {
                // data goes here
            }
        }
    }


    @Inject
    lateinit var factory: ViewModelProvider.Factory

    private lateinit var binding: ActivitySomeBinding
    private lateinit var viewModel: SomeViewModel

    override fun onCreate(savedInstanceState: Bundle?) {
        AndroidInjection.inject(this)
        super.onCreate(savedInstanceState)

        binding = bindContentView(R.layout.activity_some)
        viewModel = ViewModelProviders.of(this, factory).get(SomeViewModel::class.java)

        binding.handler = this
        binding.viewModel = viewModel
    }
}
