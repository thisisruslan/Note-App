package uz.gita.mynotesapplication.data.entity

import androidx.room.Entity
import androidx.room.PrimaryKey
import java.io.Serializable

@Entity
data class NoteEntity(
    @PrimaryKey(autoGenerate = true)
    val id: Int,
    val title: String,
    val message: String,
    val time: Long,
    var isPinned: Boolean
) : Serializable