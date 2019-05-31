package com.theapache64.jaba.cli.utils

import com.theapache64.jaba.cli.models.Project
import java.io.File

/**
 * To get files from assets directory
 */
class AssetManager(
    private val project: Project
) {

    companion object {


        // app/build.gradle
        private const val KEY_PACKAGE_NAME = "\$PACKAGE_NAME"
        private const val KEY_APP_NAME = "\$APP_NAME"

        // project/build.gradle
        private const val KEY_KOTLIN_VERSION = "\$KOTLIN_VERSION"
        private const val KEY_COMPILE_SDK_VERSION = "\$COMPILE_SDK_VERSION"
        private const val KEY_MIN_SDK_VERSION = "\$MIN_SDK_VERSION"
        private const val KEY_TARGET_SDK_VERSION = "\$TARGET_SDK_VERSION"
        private const val KEY_APPCOMPAT_VERSION = "\$APPCOMPAT_VERSION"
        private const val KEY_KTX_VERSION = "\$KTX_VERSION"
        private const val KEY_CONSTRAINT_VERSION = "\$CONSTRAINT_VERSION"
        private const val KEY_MATERIAL_VERSION = "\$MATERIAL_VERSION"
        private const val KEY_JUNIT_VERSION = "\$JUNIT_VERSION"
        private const val KEY_RUNNER_VERSION = "\$RUNNER_VERSION"
        private const val KEY_ESPRESSO_VERSION = "\$ESPRESSO_VERSION"
        private const val KEY_GRADLE_VERSION = "\$GRADLE_VERSION"
        private const val KEY_NETWORK_DEPS = "\$NETWORK_DEPS"
        private const val KEY_GOOGLE_FONTS_DEPS = "\$GOOGLE_FONTS_DEPS"
        private const val KEY_TWINKILL_NETWORK_MODULE_DEPS = "\$TWINKILL_NETWORK_MODULE_DEPS"
        private const val KEY_RETROFIT_VERSION = "\$RETROFIT_VERSION"
        private const val KEY_MENU_ITEM = "\$MENU_ITEM"
        private const val KEY_MENU_ITEM_HANDLER = "\$MENU_ITEM_HANDLER"
        private const val KEY_LOGIN_ACTIVITY_DECLARATION = "\$LOGIN_ACTIVITY_DECLARATION"
        private const val KEY_SPLASH_AS_MAIN = "\$SPLASH_AS_MAIN"
        private const val KEY_MAIN_AS_MAIN = "\$MAIN_AS_MAIN"
        private const val KEY_LOGIN_ACTIVITY_IMPORT = "\$LOGIN_ACTIVITY_IMPORT"
        private const val KEY_LOGOUT_WATCHER = "\$LOGOUT_WATCHER"
        private const val KEY_LOGIN_ACTIVITY_BUILDER = "\$LOGIN_ACTIVITY_BUILDER"
        private const val KEY_TWINKILL_NETWORK_MODULE_INIT = "\$TWINKILL_NETWORK_MODULE_INIT"
        private const val KEY_TWINKILL_NETWORK_MODULE_IMPORTS = "\$TWINKILL_NETWORK_MODULE_IMPORTS"
        private const val KEY_USER_REPOSITORY_IMPORT = "\$USER_REPOSITORY_IMPORT"
        private const val KEY_DAGGER_NETWORK_MODULE_INIT = "\$DAGGER_NETWORK_MODULE_INIT"
        private const val KEY_TWINKILL_AUTHORIZATION_INIT = "\$TWINKILL_AUTHORIZATION_INIT"

        private val projectBuildGradleFile by lazy { File("assets/project.build.gradle") }
        val userIcon by lazy { File("assets/ic_user.png") }
        val androidIcon by lazy { File("assets/ic_android.png") }
        val logOutIcon by lazy { File("assets/ic_logout_white.xml") }
        val colorsFile by lazy { File("assets/colors.xml") }

        private val DAGGER_NETWORK_MODULE_INIT = ".baseNetworkModule(BaseNetworkModule(BASE_URL))"
        private val TWINKILL_AUTHORIZATION_INIT =
            ".addOkHttpInterceptor(AuthorizationInterceptor(userPrefRepository?.getUser()?.apiKey))"

        private val NETWORK_DEPS = """

    // Retrofit
    implementation "com.squareup.retrofit2:retrofit:${'$'}retrofit_version"
    implementation 'com.squareup.okhttp3:logging-interceptor:3.14.0'
    implementation "com.squareup.retrofit2:adapter-rxjava2:${'$'}retrofit_version"

    // Moshi
    implementation 'com.squareup.moshi:moshi:1.8.0'

        """

        private const val GOOGLE_FONTS_DEPS = "implementation \"com.theapache64.twinkill:google_fonts:0.0.1-alpha01\""
        private const val TWINKILL_NETWORK_MODULE_DEPS =
            "implementation \"com.theapache64.twinkill:network:\$twinkill_version"
        private const val RETROFIT_VERSION = "retrofit_version = '2.5.0'"

        private val LOGOUT_MENU_ITEM = """
            <item
            android:id="@+id/action_logout"
            android:icon="@drawable/ic_logout_white"
            android:title="@string/action_logout"
            app:showAsAction="always"/>
        """.trimIndent()


        private val DEFAULT_MENU_ITEM = """
            <item
            android:id="@+id/action_settings"
            android:orderInCategory="100"
            android:title="@string/action_settings"
            app:showAsAction="never" />
        """.trimIndent()

        private val MENU_ITEM_HANDLER_LOGOUT = """
            R.id.action_logout -> {

                val dialog = getConfirmDialog(R.string.title_confirm, R.string.message_logout_confirm) {
                    viewModel.logout()
                }

                dialog.show()

                true
            }
        """.trimIndent()

        private val MENU_ITEM_HANDLER_DEFAULT = "R.id.action_settings -> true"

        private val LOGIN_ACTIVITY_DECLARATION = """
            <!-- LogIn -->
            <activity
                android:name=".ui.activities.login.LogInActivity"
                android:theme="@style/AppTheme.NoActionBar"/>
        """.trimIndent()

        private val LAUNCHER_DECLARATION = """
            <intent-filter>
                <action android:name="android.intent.action.MAIN"/>

                <category android:name="android.intent.category.LAUNCHER"/>
            </intent-filter>
        """.trimIndent()

        private const val LOGIN_ACTIVITY_IMPORT = "import \$PACKAGE_NAME.ui.activities.login.LogInActivity"

        private val LOGOUT_WATCHER = """

            // Watching logout
            viewModel.getLoggedOut().observe(this, Observer { isLoggedOut ->

                if (isLoggedOut) {
                    startActivity(LogInActivity.getStartIntent(this))
                    finish()
                }

            })

        """.trimIndent()


        private val LOGIN_ACTIVITY_BUILDER = """

            @ContributesAndroidInjector
            abstract fun getLogInActivity(): LogInActivity

        """.trimIndent()

        private val TWINKILL_NETWORK_MODULE_INIT = """

            .setNeedDeepCheckOnNetworkResponse(true)
            .addOkHttpInterceptor(CurlInterceptor())

        """.trimIndent()

        private val TWINKILL_NETWORK_MODULE_IMPORTS = """

            import com.theapache64.twinkill.network.di.modules.BaseNetworkModule
            import com.theapache64.twinkill.network.utils.retrofit.interceptors.AuthorizationInterceptor
            import com.theapache64.twinkill.network.utils.retrofit.interceptors.CurlInterceptor
            import ${'$'}PACKAGE_NAME.data.repositories.UserPrefRepository

        """.trimIndent()


        private val USER_REPOSITORY_IMPORT = "import \$PACKAGE_NAME.data.repositories.UserPrefRepository"
    }

    /**
     * To return new app/build.gradle with given package name
     */
    fun getAppBuildGradle(): String {
        return withPackageNameReplacedFromAssets("app.build.gradle")
            .replace(KEY_NETWORK_DEPS, getNetworkDeps()) // Network
            .replace(KEY_GOOGLE_FONTS_DEPS, getGoogleFonts()) // GoogleFonts
            .replace(KEY_TWINKILL_NETWORK_MODULE_DEPS, getTwinKillNetworkModuleDeps()) // TwinKillNetwork
    }


    /**
     * To get project build gradle with given params
     */
    fun getProjectBuildGradle(
        kotlinVersion: String,
        compileSdkVersion: String,
        minSdkVersion: String,
        targetSdkVersion: String,
        appCompatVersion: String,
        ktxVersion: String,
        constraintVersion: String,
        materialVersion: String,
        jUnitVersion: String,
        runnerVersion: String,
        espressoVersion: String,
        gradleVersion: String
    ): String {

        return projectBuildGradleFile.readText()
            .replace(KEY_KOTLIN_VERSION, kotlinVersion)
            .replace(KEY_COMPILE_SDK_VERSION, compileSdkVersion)
            .replace(KEY_MIN_SDK_VERSION, minSdkVersion)
            .replace(KEY_TARGET_SDK_VERSION, targetSdkVersion)
            .replace(KEY_APPCOMPAT_VERSION, appCompatVersion)
            .replace(KEY_KTX_VERSION, ktxVersion)
            .replace(KEY_CONSTRAINT_VERSION, constraintVersion)
            .replace(KEY_MATERIAL_VERSION, materialVersion)
            .replace(KEY_JUNIT_VERSION, jUnitVersion)
            .replace(KEY_RUNNER_VERSION, runnerVersion)
            .replace(KEY_ESPRESSO_VERSION, espressoVersion)
            .replace(KEY_GRADLE_VERSION, gradleVersion)
            .replace(KEY_RETROFIT_VERSION, getRetrofitVersion())
    }

    private fun getRetrofitVersion(): String {
        return if (project.isNeedNetworkModule) {
            RETROFIT_VERSION
        } else {
            ""
        }
    }

    private fun getTwinKillNetworkModuleDeps(): String {
        return if (project.isNeedNetworkModule) {
            TWINKILL_NETWORK_MODULE_DEPS
        } else {
            ""
        }
    }

    private fun getGoogleFonts(): String {
        return if (project.isNeedGoogleFontsModule) {
            GOOGLE_FONTS_DEPS
        } else {
            ""
        }
    }

    private fun getNetworkDeps(): String {
        return if (project.isNeedNetworkModule) {
            NETWORK_DEPS
        } else {
            ""
        }
    }


    private fun withPackageNameReplacedFromAssets(fileName: String): String {
        return getAssetContent(fileName).replace(KEY_PACKAGE_NAME, project.packageName)
    }

    private fun getAssetContent(fileName: String): String {
        return File("assets/$fileName").readText()
    }

    /**
     * To get MainActivity.kt
     */
    fun getMainActivity(): String {
        return getAssetContent("MainActivity.kt")
            .replace(KEY_LOGIN_ACTIVITY_IMPORT, getLogInActivityImport())
            .replace(KEY_PACKAGE_NAME, project.packageName)
            .replace(KEY_MENU_ITEM_HANDLER, getMenuItemHandler())
            .replace(KEY_LOGOUT_WATCHER, getLogOutWatcher())
    }

    private fun getLogOutWatcher(): String {
        return if (project.isNeedLogInScreen) {
            LOGOUT_WATCHER
        } else {
            ""
        }
    }

    private fun getLogInActivityImport(): String {
        return if (project.isNeedLogInScreen) {
            LOGIN_ACTIVITY_IMPORT
        } else {
            ""
        }
    }

    private fun getMenuItemHandler(): String {
        return if (project.isNeedLogInScreen) {
            MENU_ITEM_HANDLER_LOGOUT
        } else {
            MENU_ITEM_HANDLER_DEFAULT
        }
    }

    /**
     * To get manifest file
     */
    fun getManifestFile(): String {
        return withPackageNameReplacedFromAssets("AndroidManifest.xml")
            .replace(KEY_LOGIN_ACTIVITY_DECLARATION, getLogInActivityDeclaration())
            .replace(KEY_SPLASH_AS_MAIN, getSplashAsMain())
            .replace(KEY_MAIN_AS_MAIN, getMainAsMain())
    }

    private fun getMainAsMain(): String {
        return if (!project.isNeedSplashScreen) {
            LAUNCHER_DECLARATION
        } else {
            ""
        }
    }

    private fun getSplashAsMain(): String {
        return if (project.isNeedSplashScreen) {
            LAUNCHER_DECLARATION
        } else {
            ""
        }
    }

    private fun getLogInActivityDeclaration(): String {
        return if (project.isNeedLogInScreen) {
            LOGIN_ACTIVITY_DECLARATION
        } else {
            ""
        }
    }

    /**
     * To get App.kt
     */
    fun getAppFile(): String {

        return getAssetContent("App.kt")
            .replace(KEY_TWINKILL_NETWORK_MODULE_IMPORTS, getTwinKillModuleImports())
            .replace(KEY_USER_REPOSITORY_IMPORT, getUserRepositoryImport())
            .replace(KEY_PACKAGE_NAME, project.packageName)
            .replace(KEY_TWINKILL_NETWORK_MODULE_INIT, getTwinKillNetworkModuleInit())
            .replace(KEY_DAGGER_NETWORK_MODULE_INIT, getDaggerNetworkModuleInit())
            .replace(KEY_TWINKILL_AUTHORIZATION_INIT, getTwinKillAuthorizationInit())
    }

    private fun getTwinKillAuthorizationInit(): String {
        return if (project.isNeedLogInScreen) {
            return KEY_TWINKILL_AUTHORIZATION_INIT
        } else {
            ""
        }
    }

    private fun getDaggerNetworkModuleInit(): String {
        return if (project.isNeedNetworkModule) {
            DAGGER_NETWORK_MODULE_INIT
        } else {
            ""
        }
    }

    private fun getUserRepositoryImport(): String {
        return if (project.isNeedLogInScreen) {
            return USER_REPOSITORY_IMPORT
        } else {
            ""
        }
    }

    private fun getTwinKillNetworkModuleInit(): String {
        return if (project.isNeedNetworkModule) {
            TWINKILL_NETWORK_MODULE_INIT
        } else {
            ""
        }
    }

    private fun getTwinKillModuleImports(): String {
        return if (project.isNeedNetworkModule) {
            return TWINKILL_NETWORK_MODULE_IMPORTS
        } else {
            ""
        }
    }

    fun getMainViewModel(): String {
        return withPackageNameReplacedFromAssets("MainViewModel.kt")
    }

    fun getActivityMainLayout(): String {
        return withPackageNameReplacedFromAssets("activity_main.xml")
    }

    fun getContentMainLayout(): String {
        return withPackageNameReplacedFromAssets("content_main.xml")
    }

    fun getActivityBuilder(): String {
        return getAssetContent("ActivitiesBuilderModule.kt")
            .replace(KEY_LOGIN_ACTIVITY_IMPORT, getLogInActivityImport())
            .replace(KEY_LOGIN_ACTIVITY_BUILDER, getLogInActivityBuilder())
            .replace(KEY_PACKAGE_NAME, project.packageName)
    }

    private fun getLogInActivityBuilder(): String {
        return if (project.isNeedLogInScreen) {
            LOGIN_ACTIVITY_BUILDER
        } else {
            ""
        }
    }

    fun getAppComponent(): String {
        return withPackageNameReplacedFromAssets("AppComponent.kt")
    }

    fun getUserRepository(): String {
        return withPackageNameReplacedFromAssets("UserPrefRepository.kt")
    }

    fun getLogInRequest(): String {
        return withPackageNameReplacedFromAssets("LogInRequest.kt")
    }

    fun getLogInResponse(): String {
        return withPackageNameReplacedFromAssets("LogInResponse.kt")
    }

    fun getAppModule(): String {
        return withPackageNameReplacedFromAssets("AppModule.kt")
    }

    fun getNetworkModule(): String {
        return withPackageNameReplacedFromAssets("NetworkModule.kt")
    }

    fun getViewModelModule(): String {
        return withPackageNameReplacedFromAssets("ViewModelModule.kt")
    }

    fun getApiInterface(): String {
        return withPackageNameReplacedFromAssets("ApiInterface.kt")
    }

    fun getSplashViewModel(): String {
        return withPackageNameReplacedFromAssets("SplashViewModel.kt")
    }

    fun getSplashActivity(): String {
        return withPackageNameReplacedFromAssets("SplashActivity.kt")
    }

    fun getStyles(): String {
        return withPackageNameReplacedFromAssets("styles.xml")
    }

    fun getLogInActivity(): String {
        return withPackageNameReplacedFromAssets("LogInActivity.kt")
    }

    fun getLogInViewModel(): String {
        return withPackageNameReplacedFromAssets("LogInViewModel.kt")
    }

    fun getLogInClickHandler(): String {
        return withPackageNameReplacedFromAssets("LogInClickHandler.kt")
    }

    fun getIds(): String {
        return getAssetContent("ids.xml")
    }

    fun getAuthRepository(): String {
        return withPackageNameReplacedFromAssets("AuthRepository.kt")
    }

    fun getSplashBg(): String {
        return getAssetContent("splash_bg.xml")
    }

    fun getAndroidVectorIcon(): String {
        return getAssetContent("ic_android_green_100dp.xml")
    }

    fun getLogInLayout(): String {
        return withPackageNameReplacedFromAssets("activity_log_in.xml")
    }

    fun getStringsXml(): String {
        return withAppNameReplacedFromAssets("strings.xml")
    }

    private fun withAppNameReplacedFromAssets(fileName: String): String {
        return getAssetContent(fileName).replace(KEY_APP_NAME, project.name)
    }

    fun getMenuMain(): String {
        return getAssetContent("menu_main.xml")
            .replace(KEY_MENU_ITEM, getMenuItem())
    }

    private fun getMenuItem(): String {
        return if (project.isNeedLogInScreen) {
            LOGOUT_MENU_ITEM
        } else {
            DEFAULT_MENU_ITEM
        }
    }
}