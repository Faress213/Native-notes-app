package com.example.yournotes.repositories

import com.example.yournotes.database.NoteEntity
import kotlinx.coroutines.flow.Flow

interface NoteRepo {
    suspend fun addNote(note:NoteEntity)
    suspend fun deleteNote(note:NoteEntity)
    suspend fun getNote():Flow<List<NoteEntity>>
    suspend fun updateNote(note:NoteEntity)
}