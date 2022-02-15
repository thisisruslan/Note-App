package uz.gita.mynotesapplication.data

import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import uz.gita.mynotesapplication.app.App
import uz.gita.mynotesapplication.data.dao.NoteDao
import uz.gita.mynotesapplication.data.entity.NoteEntity

@Database(entities = [NoteEntity::class], version = 1)
abstract class NoteDatabase : RoomDatabase() {
    abstract fun getNoteDao() : NoteDao

    companion object {
        private lateinit var database : NoteDatabase

        fun getDataBase() : NoteDatabase {
            if (!Companion::database.isInitialized) {
                database = Room.databaseBuilder(App.instance, NoteDatabase::class.java, "NoteApp.db")
                    .allowMainThreadQueries()
                    .build()
            }
            return database
        }
    }

}