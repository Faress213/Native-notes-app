package com.example.yournotes.database

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.OnConflictStrategy.Companion.REPLACE
import androidx.room.Query
import androidx.room.Update
import kotlinx.coroutines.flow.Flow


@Dao
interface NoteDao {
    @Insert
    fun addNote(note:NoteEntity)

    @Query("select * FROM notes")
    fun getNotes(): Flow<List<NoteEntity>>

    @Update(onConflict = REPLACE)
    fun updateNote(note:NoteEntity)

    @Delete
    fun DeleteNote(note:NoteEntity)

}