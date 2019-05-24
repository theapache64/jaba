package com.theapache64.jaba.cli.utils

import java.io.File

object FullFileModels {

    private const val ASSET_APP_GRADLE_PATH = "assets/app.build.gradle"

    /**
     * To get build.gradle file
     */

    fun getAppGradle() {
        val assetAppGradle = File(ASSET_APP_GRADLE_PATH)
        var fileContents = assetAppGradle.readText()

    }

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