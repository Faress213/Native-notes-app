package com.example.yournotes.screens

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.yournotes.database.NoteEntity
import com.example.yournotes.repositories.NoteRepo
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import org.koin.core.component.KoinComponent
import org.koin.core.component.inject

class HomeViewModel : ViewModel(), KoinComponent {
    private val repo: NoteRepo by inject()

    private val _notes = MutableStateFlow<List<NoteEntity>>(emptyList())
    val notes = _notes.asStateFlow()

    private val _searchedNotes = MutableStateFlow<List<NoteEntity>?>(null)
    val searchedNotes = _searchedNotes.asStateFlow()

    init {
        getNotes()
    }

    private fun getNotes() {
        viewModelScope.launch(Dispatchers.IO) {
            repo.getNote().collect { data ->
                _notes.update { data }
            }
        }
    }

     fun deleteNote(note: NoteEntity) {
        viewModelScope.launch(Dispatchers.IO) {
            repo.deleteNote(note)
        }
    }

    fun addNote(note: NoteEntity) {
        viewModelScope.launch(Dispatchers.IO) {
            repo.addNote(note)
        }
    }

    fun searchNote(query: String) {
        if (query.isEmpty()) {
            _searchedNotes.update { null }
        } else {
            _searchedNotes.update {
                _notes.value.filter {
                    it.note.contains(query, ignoreCase = true)
                }
            }
        }
    }

    fun updateNote(note:NoteEntity){
        viewModelScope.launch (Dispatchers.IO){
            repo.updateNote(note)
        }
    }
}
