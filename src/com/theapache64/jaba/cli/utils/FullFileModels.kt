package com.theapache64.jaba.cli.utils

object FullFileModels {

    /**
     * To get ViewModel model
     */
    fun getViewModel(name: String, packageName: String): String {
        return """
            package $packageName

            import androidx.lifecycle.ViewModel
            import javax.inject.Inject

            class $name @Inject constructor() : ViewModel() {

            }
        """.trimIndent()
    }

    /**
     * To get app model
     */
    fun getAppModel(packageName: String, isNeedGoogleFonts: Boolean): String {
        return """

            package $packageName

            import android.app.Application
            import com.theapache64.twinkill.TwinKill
            import com.theapache64.twinkill.googlefonts.GoogleFonts

            class App : Application() {

                override fun onCreate() {
                    super.onCreate()

                    TwinKill.init(
                        TwinKill.Builder()
                            ${if (isNeedGoogleFonts) ".setDefaultFont(GoogleFonts.GoogleSansRegular)" else ""}
                            .build()
                    )
                }
            }

        """.trimIndent()
    }
}