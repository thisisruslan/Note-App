package uz.gita.mynotesapplication.ui.viewmodel

import androidx.lifecycle.ViewModel
import uz.gita.mynotesapplication.data.NoteDatabase
import uz.gita.mynotesapplication.data.entity.NoteEntity

class AddNoteViewModel : ViewModel() {
    private val database = NoteDatabase.getDataBase().getNoteDao()

    fun addNote(data : NoteEntity) {
        database.insert(data)
    }
}