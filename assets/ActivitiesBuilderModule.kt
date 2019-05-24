package com.tp.heads.di.modules

import com.tp.heads.ui.activities.leaverequest.LeaveRequestActivity
import com.tp.heads.ui.activities.login.LogInActivity
import com.tp.heads.ui.activities.main.MainActivity
import com.tp.heads.ui.activities.splash.SplashActivity
import dagger.Module
import dagger.android.ContributesAndroidInjector

@Module
abstract class ActivitiesBuilderModule {

    /**
     * Activity
     */
    @ContributesAndroidInjector
    abstract fun getSplashActivity(): SplashActivity

    @ContributesAndroidInjector
    abstract fun getLogInActivity(): LogInActivity

    @ContributesAndroidInjector
    abstract fun getMainActivity(): MainActivity
}