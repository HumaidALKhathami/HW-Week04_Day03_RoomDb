package com.shady.room

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import androidx.sqlite.db.SupportSQLiteDatabase
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.launch
import java.time.chrono.HijrahChronology.INSTANCE


@Database(entities = [Word::class], version = 1,exportSchema = false)
abstract class WordRoomDatabase : RoomDatabase() {

    abstract fun wordDao(): WordDao

    companion object {
        @Volatile
        private var INSTANCE : WordRoomDatabase? = null

        fun getDatabase( context : Context) : WordRoomDatabase {

            return INSTANCE ?: synchronized(this){
                val instance = Room.databaseBuilder(
                    context.applicationContext, WordRoomDatabase::class.java,
                    "word_database"
                ).build()
                INSTANCE = instance

                instance
            }
        }
    }


    private class WordDatabaseCallback(
        private val scope: CoroutineScope
    ) : RoomDatabase.Callback() {

        override fun onCreate(db: SupportSQLiteDatabase) {
            super.onCreate(db)
            INSTANCE?.let { database ->
                scope.launch {
                    populateDatabase(database.wordDao())
                }
            }
        }

        suspend fun populateDatabase(wordDao: WordDao) {

            wordDao.deleteAll()


            var word = Word("I'm")
            wordDao.insert(word)
            word = Word("THE BEST")
            wordDao.insert(word)


        }
    }

}