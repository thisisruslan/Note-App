package uz.gita.mynotesapplication.ui.viewmodel

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import uz.gita.mynotesapplication.data.NoteDatabase
import uz.gita.mynotesapplication.data.entity.NoteEntity

class MainViewModel : ViewModel() {
    private val _noteListLiveData = MutableLiveData<List<NoteEntity>>()
    val noteListLiveData: LiveData<List<NoteEntity>> get() = _noteListLiveData

    private val database = NoteDatabase.getDataBase().getNoteDao()

    fun loadData() {
        _noteListLiveData.value = database.getAllNotes()
    }

    fun searchNote(text:String) : List<NoteEntity>{
        return  database.searchNote(text)
    }

    fun getAllNotes() : List<NoteEntity>{
        return database.getAllNotes()
    }

    fun removeData(data: NoteEntity){
        database.delete(data)
        _noteListLiveData.value = database.getAllNotes()
    }

    fun updateData(data:NoteEntity){
        database.update(data)
        _noteListLiveData.value = database.getAllNotes()
    }

}