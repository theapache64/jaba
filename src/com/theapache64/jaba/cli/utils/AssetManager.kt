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
        private const val KEY_ESPRESSO_VERSION = "\$ESPRESSO_VERSION"
        private const val KEY_GRADLE_VERSION = "\$GRADLE_VERSION"
        private const val KEY_NETWORK_DEPS = "\$NETWORK_DEPS"
        private const val KEY_GOOGLE_FONTS_DEPS = "\$GOOGLE_FONTS_DEPS"
        private const val KEY_TWINKILL_NETWORK_MODULE_DEPS = "\$TWINKILL_NETWORK_MODULE_DEPS"
        private const val KEY_RETROFIT_VERSION = "\$RETROFIT_VERSION"
        private const val KEY_MENU_ITEM = "\$MENU_ITEM"
        private const val KEY_FULL_PACKAGE_NAME = "\$FULL_PACKAGE_NAME"
        private const val KEY_COMPONENT_NAME = "\$COMPONENT_NAME"
        private const val KEY_COMPONENT_NAME_LOWER_CASE = "\$COMPONENT_NAME_LOWER_CASE"
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
        private const val KEY_INTERNET_PERMISSION = "\$INTERNET_PERMISSION"
        private const val KEY_RETROFIT_LOGIN_METHOD = "\$RETROFIT_LOGIN_METHOD"
        private const val KEY_SPLASH_ACTIVITY_DECLARATION = "\$SPLASH_ACTIVITY_DECLARATION"
        private const val KEY_SPLASH_ACTIVITY_BUILDER = "\$SPLASH_ACTIVITY_BUILDER"
        private const val KEY_SPLASH_ACTIVITY_IMPORT = "\$SPLASH_ACTIVITY_IMPORT"
        private const val KEY_RETROFIT_LOGIN_IMPORTS = "\$RETROFIT_LOGIN_IMPORTS"
        private const val KEY_BASE_API_URL = "\$BASE_API_URL"


        private val RETROFIT_LOGIN_IMPORTS = """

            import androidx.lifecycle.LiveData
            import com.theapache64.twinkill.network.utils.Resource
            import ${'$'}PACKAGE_NAME.data.remote.login.LogInRequest
            import ${'$'}PACKAGE_NAME.data.remote.login.LogInResponse
            import retrofit2.http.Body
            import retrofit2.http.POST

        """.trimIndent()
        private val SPLASH_ACTIVITY_IMPORT = "import \$PACKAGE_NAME.ui.activities.splash.SplashActivity"
        private val SPLASH_ACTIVITY_BUILDER = """

            @ContributesAndroidInjector
            abstract fun getSplashActivity(): SplashActivity

        """.trimIndent()

        private val COMPONENT_ACTIVITY_BUILDER = """

            @ContributesAndroidInjector
            abstract fun get${'$'}COMPONENT_NAMEActivity(): ${'$'}COMPONENT_NAMEActivity

        """.trimIndent()


        private val SPLASH_ACTIVITY_DECLARATION = """

        <activity
                android:name=".ui.activities.splash.SplashActivity"
                android:theme="@style/AppTheme.NoActionBar">
            ${'$'}SPLASH_AS_MAIN
        </activity>

    """.trimIndent()

        private val projectBuildGradleFile by lazy { File("assets/project.build.gradle") }
        val userIcon by lazy { File("assets/ic_user.png") }
        val androidIcon by lazy { File("assets/ic_android.png") }
        val logOutIcon by lazy { File("assets/ic_logout_white.xml") }
        val colorsFile by lazy { File("assets/colors.xml") }
        val dimensFile by lazy { File("assets/dimens.xml") }

        private val RETROFIT_LOGIN_METHOD = """

             @POST("login")
            fun login(@Body logInRequest: LogInRequest): LiveData<Resource<LogInResponse>>

        """.trimIndent()
        private val DAGGER_NETWORK_MODULE_INIT = ".baseNetworkModule(BaseNetworkModule(BASE_URL))"
        private val TWINKILL_AUTHORIZATION_INIT =
            ".addOkHttpInterceptor(AuthorizationInterceptor(userPrefRepository?.getUser()?.apiKey))"

        private const val INTERNET_PERMISSION = "<uses-permission android:name=\"android.permission.INTERNET\"/>"

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
            "implementation \"com.theapache64.twinkill:network:\$twinkill_version\""
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

        """.trimIndent()


        private val USER_REPOSITORY_IMPORT = "import \$PACKAGE_NAME.data.repositories.UserPrefRepository"

        /**
         * Google fonts import
         */
        private const val KEY_GOOGLE_FONTS_IMPORT = "\$GOOGLE_FONTS_IMPORT"
        private val GOOGLE_FONTS_IMPORT = "import com.theapache64.twinkill.googlefonts.GoogleFonts"

        /**
         * Google fonts init
         */
        private const val KEY_GOOGLE_FONTS_INIT = "\$GOOGLE_FONTS_INIT"
        private const val GOOGLE_FONTS_INIT = " .setDefaultFont(GoogleFonts.GoogleSansRegular)"

        /**
         * Network module
         */
        private const val KEY_NETWORK_MODULE_INC = "\$NETWORK_MODULE_INC"
        private const val NETWORK_MODULE_INC = "NetworkModule::class,"

        /**
         * Preference module
         */
        private const val KEY_PREFERENCE_MODULE_INC = "\$PREFERENCE_MODULE_INC"
        private const val PREFERENCE_MODULE_INC = "PreferenceModule::class,"

        /**
         * Preference module import
         */
        private const val KEY_PREFERENCE_MODULE_IMPORT = "\$PREFERENCE_MODULE_IMPORT"
        private const val PREFERENCE_MODULE_IMPORT = "import com.theapache64.twinkill.di.modules.PreferenceModule"

        /**
         * LogIn imports
         */
        private const val KEY_LOGIN_IMPORTS = "\$LOGIN_IMPORTS"
        private val LOGIN_IMPORTS = """

            import androidx.lifecycle.LiveData
            import androidx.lifecycle.MutableLiveData
            import ${'$'}PACKAGE_NAME.data.repositories.UserPrefRepository

        """.trimIndent()

        private val LOGIN_IMPORTS_SPLASH_VM = """

            import ${'$'}PACKAGE_NAME.data.repositories.UserPrefRepository
            import ${'$'}PACKAGE_NAME.ui.activities.login.LogInActivity

        """.trimIndent()

        private const val KEY_ACTIVITY_ID = "\$ACTIVITY_ID"
        private val SPLASH_VM_CHECK_USER = """

            // if theUser == null -> login else main
            val user = userPrefRepository.getUser()
            val activityId = if (user == null) LogInActivity.ID else MainActivity.ID

            Log.i(TAG, "User is ${'$'}{user?.name}")

        """.trimIndent()

        private val SPLASH_VM_MAIN_ACT = "val activityId = MainActivity.ID"

        /**
         * User pref constructor
         */
        private const val KEY_USER_PREF_CONSTRUCTOR = "\$USER_PREF_CONSTRUCTOR"
        private const val USER_PREF_CONSTRUCTOR = " private val userPrefRepository: UserPrefRepository"

        /**
         * Logout methods
         */
        private const val KEY_LOGOUT_METHODS = "\$LOGOUT_METHODS"
        private val LOGOUT_METHODS = """

            private val isLoggedOut = MutableLiveData<Boolean>()
            fun getLoggedOut(): LiveData<Boolean> = isLoggedOut

            /**
             * Clears preference and logout user
             */
            fun logout() {
                userPrefRepository.clearUser()
                isLoggedOut.value = true
            }

        """.trimIndent()


        /**
         * LogIn strings
         */
        private const val KEY_LOGIN_STRINGS = "\$LOGIN_STRINGS"
        private val LOGIN_STRINGS = """

            <string name="hint_username">Username</string>
            <string name="hint_password">Password</string>
            <string name="action_login">LogIn</string>
            <string name="hint_remember_me">Remember Me</string>
            <string name="action_logout">LogOut</string>
            <string name="title_confirm">Confirm</string>
            <string name="message_logout_confirm">Do you really want to logout ?</string>

        """.trimIndent()


        /**
         * Splash VM Import
         */
        private const val KEY_SPLASH_VM_IMPORT = "\$SPLASH_VM_IMPORT"
        private const val SPLASH_VM_IMPORT = "import \$PACKAGE_NAME.ui.activities.splash.SplashViewModel"

        /**
         * LogIn VM Import
         */
        private const val KEY_LOGIN_VM_IMPORT = "\$LOGIN_VM_IMPORT"
        private const val LOGIN_VM_IMPORT = "import \$PACKAGE_NAME.ui.activities.login.LogInViewModel"

        /**
         * Splash VM bind
         */
        private const val KEY_SPLASH_VM_BIND = "\$SPLASH_VM_BIND"
        private val SPLASH_VM_BIND = """

            @Binds
            @IntoMap
            @ViewModelKey(SplashViewModel::class)
            abstract fun bindSplashViewModel(viewModel: SplashViewModel): ViewModel

        """.trimIndent()

        /**
         * LogIn VM bind
         */
        private const val KEY_LOGIN_VM_BIND = "\$LOGIN_VM_BIND"
        private val LOGIN_VM_BIND = """

            @Binds
            @IntoMap
            @ViewModelKey(LogInViewModel::class)
            abstract fun bindLogInViewModel(viewModel: LogInViewModel): ViewModel

        """.trimIndent()

        private val COMPONENT_VM_BIND = """

            @Binds
            @IntoMap
            @ViewModelKey(${'$'}COMPONENT_NAMEViewModel::class)
            abstract fun bind${'$'}COMPONENT_NAMEViewModel(viewModel: ${'$'}COMPONENT_NAMEViewModel): ViewModel

        """.trimIndent()


        /**
         * LogIN launcher
         */
        private const val KEY_LOGIN_LAUNCHER = "\$LOGIN_LAUNCHER"
        private val LOGIN_LAUNCHER = """

             LogInActivity.ID -> {
                startActivity(LogInActivity.getStartIntent(this))
             }

        """.trimIndent()

        private const val KEY_LOGIN_ACT_ID_RES = "\$LOGIN_ACT_ID_RES"
        private const val LOGIN_ACT_ID_RES = "<item name=\"LOG_IN_ACTIVITY_ID\" type=\"id\" />"

        /**
         * User pref repo inject
         */
        private const val KEY_USER_PREF_REPO_INJECT = "\$USER_PREF_REPO_INJECT"
        private val USER_PREF_REPO_INJECT = """

            var userPrefRepository: UserPrefRepository? = null
            @Inject set

        """.trimIndent()

        /**
         * Context module import
         */
        private const val KEY_CONTEXT_MODULE_IMPORT = "\$CONTEXT_MODULE_IMPORT"
        private const val CONTEXT_MODULE_IMPORT = "import com.theapache64.twinkill.di.modules.ContextModule"

        /**
         * Context module init
         */
        private const val KEY_CONTEXT_MODULE_INIT = "\$CONTEXT_MODULE_INIT"
        private const val CONTEXT_MODULE_INIT = ".contextModule(ContextModule(this))"

        /**
         * Base URL
         */
        private const val KEY_BASE_URL = "\$BASE_URL"
        private val BASE_URL = """
            companion object {
                private const val BASE_URL = "${'$'}BASE_API_URL"
            }
        """.trimIndent()

        private const val KEY_TOOLBAR = "\$TOOLBAR"
        private const val TOOLBAR = "setSupportActionBar(binding.toolbar)"
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
            .replace(KEY_JUNIT_VERSION, jUnitVersion)
            .replace(KEY_ESPRESSO_VERSION, espressoVersion)
            .replace(KEY_GRADLE_VERSION, gradleVersion)
            .replace(KEY_RETROFIT_VERSION, getRetrofitVersion())
            .replace(KEY_MATERIAL_VERSION, materialVersion)
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
            .replace(KEY_MENU_ITEM_HANDLER, getMenuItemHandler())
            .replace(KEY_LOGOUT_WATCHER, getLogOutWatcher())
            .replace(KEY_PACKAGE_NAME, project.packageName)
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
            .replace(KEY_SPLASH_ACTIVITY_DECLARATION, getSplashActivityDeclaration())
            .replace(KEY_SPLASH_AS_MAIN, getSplashAsMain())
            .replace(KEY_MAIN_AS_MAIN, getMainAsMain())
            .replace(KEY_INTERNET_PERMISSION, INTERNET_PERMISSION)
    }


    private fun getSplashActivityDeclaration(): String {
        return if (project.isNeedSplashScreen) {
            SPLASH_ACTIVITY_DECLARATION
        } else {
            ""
        }
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
            .replace(KEY_USER_PREF_REPO_INJECT, getUserPrefRepoInject())
            .replace(KEY_CONTEXT_MODULE_IMPORT, getContextModuleImport())
            .replace(KEY_CONTEXT_MODULE_INIT, getContextModuleInit())
            .replace(KEY_TWINKILL_NETWORK_MODULE_INIT, getTwinKillNetworkModuleInit())
            .replace(KEY_DAGGER_NETWORK_MODULE_INIT, getDaggerNetworkModuleInit())
            .replace(KEY_TWINKILL_AUTHORIZATION_INIT, getTwinKillAuthorizationInit())
            .replace(KEY_GOOGLE_FONTS_IMPORT, getGoogleFontsImport())
            .replace(KEY_GOOGLE_FONTS_INIT, getGoogleFontsInit())
            .replace(KEY_BASE_URL, getBaseUrl())
            .replace(KEY_BASE_API_URL, getBaseApiUrl())
            .replace(KEY_PACKAGE_NAME, project.packageName)
    }

    private fun getBaseUrl(): String {
        return if (project.isNeedNetworkModule) {
            BASE_URL
        } else {
            ""
        }
    }

    private fun getBaseApiUrl(): String {
        return if (project.isNeedNetworkModule) {
            project.baseUrl!!
        } else {
            ""
        }
    }

    private fun getContextModuleInit(): String {
        return if (project.isNeedLogInScreen) {
            CONTEXT_MODULE_INIT
        } else {
            ""
        }
    }

    private fun getContextModuleImport(): String {
        return if (project.isNeedLogInScreen) {
            CONTEXT_MODULE_IMPORT
        } else {
            ""
        }
    }

    private fun getUserPrefRepoInject(): String {
        return if (project.isNeedLogInScreen) {
            USER_PREF_REPO_INJECT
        } else {
            ""
        }
    }

    private fun getGoogleFontsInit(): String {
        return if (project.isNeedGoogleFontsModule) {
            GOOGLE_FONTS_INIT
        } else {
            ""
        }
    }

    private fun getGoogleFontsImport(): String {
        return if (project.isNeedGoogleFontsModule) {
            GOOGLE_FONTS_IMPORT
        } else {
            ""
        }
    }

    private fun getTwinKillAuthorizationInit(): String {
        return if (project.isNeedLogInScreen) {
            return TWINKILL_AUTHORIZATION_INIT
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
        return getAssetContent("MainViewModel.kt")
            .replace(KEY_LOGIN_IMPORTS, getLogInImports())
            .replace(KEY_USER_PREF_CONSTRUCTOR, getUserPrefConstructor())
            .replace(KEY_LOGOUT_METHODS, getLogOutMethods())
            .replace(KEY_PACKAGE_NAME, project.packageName)
    }

    fun getMainHandler(): String {
        return getAssetContent("MainHandler.kt")
            .replace(KEY_PACKAGE_NAME, project.packageName)
    }

    private fun getLogOutMethods(): String {
        return if (project.isNeedLogInScreen) {
            LOGOUT_METHODS
        } else {
            ""
        }
    }

    private fun getUserPrefConstructor(): String {
        return if (project.isNeedLogInScreen) {
            USER_PREF_CONSTRUCTOR
        } else {
            ""
        }
    }

    private fun getLogInImports(): String {
        return if (project.isNeedLogInScreen) {
            LOGIN_IMPORTS
        } else {
            ""
        }
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
            .replace(KEY_SPLASH_ACTIVITY_IMPORT, getSplashActivityImport())
            .replace(KEY_SPLASH_ACTIVITY_BUILDER, getSplashActivityBuilder())
            .replace(KEY_PACKAGE_NAME, project.packageName)
    }


    fun getActivityBuilder(componentName: String): String {
        return COMPONENT_ACTIVITY_BUILDER
            .replace(KEY_COMPONENT_NAME, componentName)
    }

    fun getVmBuilder(componentName: String): String {
        return COMPONENT_VM_BIND
            .replace(KEY_COMPONENT_NAME, componentName)
    }


    private fun getSplashActivityImport(): String {
        return if (project.isNeedSplashScreen) {
            SPLASH_ACTIVITY_IMPORT
        } else {
            ""
        }
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
            .replace(KEY_NETWORK_MODULE_INC, getNetworkModuleInclude())
            .replace(KEY_PREFERENCE_MODULE_IMPORT, getPreferenceModuleImport())
            .replace(KEY_PREFERENCE_MODULE_INC, getPreferenceModuleInclude())
    }

    private fun getPreferenceModuleImport(): String {
        return if (project.isNeedLogInScreen) {
            PREFERENCE_MODULE_IMPORT
        } else {
            ""
        }
    }

    private fun getPreferenceModuleInclude(): String {
        return if (project.isNeedLogInScreen) {
            PREFERENCE_MODULE_INC
        } else {
            ""
        }
    }

    private fun getNetworkModuleInclude(): String {
        return if (project.isNeedNetworkModule) {
            NETWORK_MODULE_INC
        } else {
            ""
        }
    }

    fun getNetworkModule(): String {
        return withPackageNameReplacedFromAssets("NetworkModule.kt")
    }

    fun getViewModelModule(): String {
        return getAssetContent("ViewModelModule.kt")
            .replace(KEY_SPLASH_VM_IMPORT, getSplashVmImport())
            .replace(KEY_LOGIN_VM_IMPORT, getLogInVmImport())
            .replace(KEY_SPLASH_VM_BIND, getSplashVmBind())
            .replace(KEY_LOGIN_VM_BIND, getLogInVmBind())
            .replace(KEY_PACKAGE_NAME, project.packageName)
    }

    private fun getLogInVmBind(): String {
        return if (project.isNeedLogInScreen) {
            LOGIN_VM_BIND
        } else {
            ""
        }
    }

    private fun getSplashVmBind(): String {
        return if (project.isNeedSplashScreen) {
            SPLASH_VM_BIND
        } else {
            ""
        }
    }

    private fun getLogInVmImport(): String {
        return if (project.isNeedLogInScreen) {
            LOGIN_VM_IMPORT
        } else {
            ""
        }
    }

    private fun getSplashVmImport(): String {
        return if (project.isNeedSplashScreen) {
            SPLASH_VM_IMPORT
        } else {
            ""
        }
    }


    fun getApiInterface(): String {
        return getAssetContent("ApiInterface.kt")
            .replace(KEY_RETROFIT_LOGIN_METHOD, getRetrofitLogInMethod())
            .replace(KEY_RETROFIT_LOGIN_IMPORTS, getRetrofitLogInImports())
            .replace(KEY_PACKAGE_NAME, project.packageName)
    }


    private fun getRetrofitLogInImports(): String {
        return if (project.isNeedLogInScreen) {
            RETROFIT_LOGIN_IMPORTS
        } else {
            ""
        }
    }

    private fun getRetrofitLogInMethod(): String {
        return if (project.isNeedLogInScreen) {
            RETROFIT_LOGIN_METHOD
        } else {
            ""
        }
    }

    fun getSplashViewModel(): String {
        return getAssetContent("SplashViewModel.kt")
            .replace(KEY_LOGIN_IMPORTS, getLogInImportsForSplashVM())
            .replace(KEY_ACTIVITY_ID, getActivityId())
            .replace(KEY_USER_PREF_CONSTRUCTOR, getUserPrefConstructor())
            .replace(KEY_PACKAGE_NAME, project.packageName)
    }

    private fun getActivityId(): String {
        return if (project.isNeedLogInScreen) {
            SPLASH_VM_CHECK_USER
        } else {
            SPLASH_VM_MAIN_ACT
        }
    }

    private fun getLogInImportsForSplashVM(): String {
        return if (project.isNeedLogInScreen) {
            LOGIN_IMPORTS_SPLASH_VM
        } else {
            ""
        }
    }

    fun getSplashActivity(): String {
        return getAssetContent("SplashActivity.kt")
            .replace(KEY_LOGIN_LAUNCHER, getLogInLauncher())
            .replace(KEY_LOGIN_ACTIVITY_IMPORT, getLogInActivityImport())
            .replace(KEY_PACKAGE_NAME, project.packageName)
    }

    fun getSplashLayout(): String {
        return withPackageNameReplacedFromAssets("activity_splash.xml")
    }

    private fun getLogInLauncher(): String {
        return if (project.isNeedLogInScreen) {
            LOGIN_LAUNCHER
        } else {
            ""
        }
    }

    private fun getSplashActivityBuilder(): String {
        return if (project.isNeedSplashScreen) {
            SPLASH_ACTIVITY_BUILDER
        } else {
            ""
        }
    }

    fun getStyles(): String {
        return getAssetContent("styles.xml")
    }

    fun getLogInActivity(): String {
        return withPackageNameReplacedFromAssets("LogInActivity.kt")
    }

    fun getLogInViewModel(): String {
        return withPackageNameReplacedFromAssets("LogInViewModel.kt")
    }

    fun getLogInHandler(): String {
        return withPackageNameReplacedFromAssets("LogInHandler.kt")
    }

    fun getIds(): String {
        return getAssetContent("ids.xml")
            .replace(KEY_LOGIN_ACT_ID_RES, getLogInActIdRes())
    }

    private fun getLogInActIdRes(): String {
        return if (project.isNeedLogInScreen) {
            LOGIN_ACT_ID_RES
        } else {
            ""
        }
    }

    fun getAuthRepository(): String {
        return withPackageNameReplacedFromAssets("AuthRepository.kt")
    }

    fun getAndroidVectorIcon(): String {
        return getAssetContent("ic_android_green_100dp.xml")
    }

    fun getLogInLayout(): String {
        return withPackageNameReplacedFromAssets("activity_log_in.xml")
    }

    fun getStringsXml(): String {
        return getAssetContent("strings.xml")
            .replace(KEY_LOGIN_STRINGS, getLogInStrings())
            .replace(KEY_APP_NAME, project.name)
    }

    private fun getLogInStrings(): String {
        return if (project.isNeedLogInScreen) {
            LOGIN_STRINGS
        } else {
            ""
        }
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

    fun getViewModel(fullPackageName: String, componentName: String): String {
        return getAssetContent("SomeViewModel.kt")
            .replace(KEY_FULL_PACKAGE_NAME, fullPackageName)
            .replace(KEY_COMPONENT_NAME, componentName)
    }

    fun getSomeActivity(
        appPackageName: String,
        fullPackageName: String,
        componentName: String,
        hasToolbar: Boolean
    ): String {


        return getAssetContent("SomeActivity.kt")
            .replace(KEY_FULL_PACKAGE_NAME, fullPackageName)
            .replace(KEY_COMPONENT_NAME_LOWER_CASE, StringUtils.camelCaseToSnackCase(componentName))
            .replace(KEY_PACKAGE_NAME, appPackageName)
            .replace(KEY_TOOLBAR, if (hasToolbar) TOOLBAR else "")
            .replace(KEY_COMPONENT_NAME, componentName)
    }

    fun getHandler(fullPackageName: String, componentName: String): String {
        return getAssetContent("SomeHandler.kt")
            .replace(KEY_FULL_PACKAGE_NAME, fullPackageName)
            .replace(KEY_COMPONENT_NAME, componentName)
    }

    fun getLayoutFile(fullPackageName: String, componentName: String): String {
        return getAssetContent("activity_some.xml")
            .replace(KEY_FULL_PACKAGE_NAME, fullPackageName)
            .replace(KEY_COMPONENT_NAME, componentName)
    }

    fun getLayoutFileWithToolbar(
        fullPackageName: String,
        componentName: String,
        componentNameLowerCase: String
    ): String {
        return getAssetContent("activity_some_toolbar.xml")
            .replace(KEY_FULL_PACKAGE_NAME, fullPackageName)
            .replace(KEY_COMPONENT_NAME_LOWER_CASE, componentNameLowerCase)
            .replace(KEY_COMPONENT_NAME, componentName)
    }

    fun getLayoutContentFile(fullPackageName: String, componentName: String, componentNameSnakeCase: String): String {
        return getAssetContent("content_some.xml")
            .replace(KEY_FULL_PACKAGE_NAME, fullPackageName)
            .replace(KEY_COMPONENT_NAME_LOWER_CASE, componentNameSnakeCase)
            .replace(KEY_COMPONENT_NAME, componentName)
    }
}