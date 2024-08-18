package com.example.yournotes.database

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity("notes")
data class NoteEntity(
    @PrimaryKey(autoGenerate = true)
    val id:Int=0,
    @ColumnInfo("note")
    val note:String,
    @ColumnInfo("color")
    val color:Int,
    @ColumnInfo("time")
    val time:String
)
