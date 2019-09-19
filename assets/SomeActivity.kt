package $FULL_PACKAGE_NAME

import android.content.Context
import android.content.Intent
import android.os.Bundle
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.ViewModelProviders
import $PACKAGE_NAME.R
import $PACKAGE_NAME.databinding.Activity$COMPONENT_NAMEBinding
import com.theapache64.twinkill.ui.activities.base.BaseAppCompatActivity
import com.theapache64.twinkill.utils.extensions.bindContentView
import dagger.android.AndroidInjection
import javax.inject.Inject

class $COMPONENT_NAMEActivity : BaseAppCompatActivity(), $COMPONENT_NAMEHandler {

    companion object {
        fun getStartIntent(context: Context): Intent {
            return Intent(context, $COMPONENT_NAMEActivity::class.java).apply {
                // data goes here
            }
        }
    }


    @Inject
    lateinit var factory: ViewModelProvider.Factory

    private lateinit var binding: Activity$COMPONENT_NAMEBinding
    private lateinit var viewModel: $COMPONENT_NAMEViewModel

    override fun onCreate(savedInstanceState: Bundle?) {
        AndroidInjection.inject(this)
        super.onCreate(savedInstanceState)

        binding = bindContentView(R.layout.activity_$COMPONENT_NAME_LOWER_CASE)
        $TOOLBAR
        viewModel = ViewModelProviders.of(this, factory).get($COMPONENT_NAMEViewModel::class.java)

        binding.handler = this
        binding.viewModel = viewModel
    }
}
