package com.example.yournotes.database

import android.app.Application
import androidx.room.Room
import com.example.yournotes.repositories.NoteRepImpl
import com.example.yournotes.repositories.NoteRepo
import org.koin.core.context.startKoin
import org.koin.dsl.module

class KoinApp : Application() {
    override fun onCreate() {
        super.onCreate()

        val appModule = module {
            single {
                Room.databaseBuilder(this@KoinApp, NoteDataBase::class.java, "db")
                    .build()
            }
            single { get<NoteDataBase>().noteDao() }
            single<NoteRepo> { NoteRepImpl(get()) }
        }

        startKoin {
            modules(appModule)
        }
    }
}
