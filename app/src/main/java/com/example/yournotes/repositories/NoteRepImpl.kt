package com.example.yournotes.repositories

import androidx.room.RoomDatabase
import com.example.yournotes.database.NoteDataBase
import com.example.yournotes.database.NoteEntity
import kotlinx.coroutines.flow.Flow

class NoteRepImpl(private val database: NoteDataBase):NoteRepo {
    private val dao=database.noteDao()
    override suspend fun addNote(note: NoteEntity)=dao.addNote(note)

    override suspend fun deleteNote(note: NoteEntity)=dao.DeleteNote(note)



    override suspend fun getNote(): Flow<List<NoteEntity>> =dao.getNotes()
    override suspend fun updateNote(note: NoteEntity)=dao.updateNote(note)
}