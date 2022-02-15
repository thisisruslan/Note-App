package uz.gita.mynotesapplication.data.dao

import androidx.room.Dao
import androidx.room.Query
import uz.gita.mynotesapplication.data.entity.NoteEntity

@Dao
interface NoteDao : BaseDao<NoteEntity> {

    @Query("SELECT * FROM NoteEntity ORDER BY isPinned DESC, time DESC")
    fun getAllNotes(): List<NoteEntity>

    @Query("SELECT * FROM NoteEntity WHERE (title LIKE :text OR message LIKE :text) ORDER BY isPinned DESC, time DESC")
    fun searchNote(text: String): List<NoteEntity>

}