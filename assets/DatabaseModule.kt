package $PACKAGE_NAME.di.modules

import android.content.Context
import androidx.room.Room
import $PACKAGE_NAME.data.local.AppDatabase
import com.theapache64.twinkill.di.modules.ContextModule
import dagger.Module
import dagger.Provides

@Module(includes = [ContextModule::class])
class DatabaseModule {

    @Provides
    fun provideAppDatabase(context: Context): AppDatabase {
        return Room.databaseBuilder(context, AppDatabase::class.java, "$PACKAGE_NAME_db")
            .build()
    }

    @Provides
    fun provideSampleDao(appDatabase: AppDatabase) = appDatabase.sampleDao()
}