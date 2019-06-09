package com.theapache64.jaba.cli.models

data class Project(
    val name: String,
    val dir: String,
    val packageName: String,
    val architecture: Int,
    val isNeedGoogleFontsModule: Boolean,
    val isNeedNetworkModule: Boolean,
    val baseUrl: String?,
    val isNeedSplashScreen: Boolean,
    val isNeedLogInScreen: Boolean,
    val newMainName: String?
) {

    companion object {
        const val ARCH_MVVP = 1
        const val ARCH_MVP = 2
    }
}