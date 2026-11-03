package com.eco.cleanup.data.local

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import androidx.sqlite.db.SupportSQLiteDatabase
import com.eco.cleanup.data.local.dao.KategoriDao
import com.eco.cleanup.data.local.dao.LevelDao
import com.eco.cleanup.data.local.dao.SampahDao
import com.eco.cleanup.data.local.entity.KategoriEntity
import com.eco.cleanup.data.local.entity.LevelEntity
import com.eco.cleanup.data.local.entity.SampahEntity
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

@Database(
    entities = [KategoriEntity::class, SampahEntity::class, LevelEntity::class],
    version = 1,
    exportSchema = false
)
abstract class CleanUpDatabase : RoomDatabase() {

    abstract fun kategoriDao(): KategoriDao
    abstract fun sampahDao(): SampahDao
    abstract fun levelDao(): LevelDao

    companion object {
        @Volatile
        private var INSTANCE: CleanUpDatabase? = null

        fun getInstance(context: Context): CleanUpDatabase {
            return INSTANCE ?: synchronized(this) {
                val instance = Room.databaseBuilder(
                    context.applicationContext,
                    CleanUpDatabase::class.java,
                    "cleanup_database"
                )
                    .addCallback(object : Callback() {
                        override fun onCreate(db: SupportSQLiteDatabase) {
                            super.onCreate(db)
                            // Seed data on first create
                            INSTANCE?.let { database ->
                                CoroutineScope(Dispatchers.IO).launch {
                                    DatabaseSeeder.seed(database)
                                }
                            }
                        }
                    })
                    .build()
                INSTANCE = instance
                // Seed immediately after build on first run
                CoroutineScope(Dispatchers.IO).launch {
                    DatabaseSeeder.seedIfEmpty(instance)
                }
                instance
            }
        }
    }
}

