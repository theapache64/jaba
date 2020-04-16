package $PACKAGE_NAME.data.local

import androidx.room.Database
import androidx.room.RoomDatabase
import $PACKAGE_NAME.data.local.daos.SampleDao
import $PACKAGE_NAME.data.local.entities.SampleEntity

@Database(entities = [SampleEntity::class], version = 1)
abstract class AppDatabase : RoomDatabase() {
    abstract fun sampleDao(): SampleDao
}