package uz.gita.mynotesapplication.ui.viewmodel

import androidx.lifecycle.ViewModel
import uz.gita.mynotesapplication.data.NoteDatabase
import uz.gita.mynotesapplication.data.entity.NoteEntity

class InfoNoteViewModel : ViewModel() {
    private val database = NoteDatabase.getDataBase().getNoteDao()

    fun editNote(data : NoteEntity) {
        database.update(data)
    }

    fun deleteNote(data: NoteEntity) {
        database.delete(data)
    }
}