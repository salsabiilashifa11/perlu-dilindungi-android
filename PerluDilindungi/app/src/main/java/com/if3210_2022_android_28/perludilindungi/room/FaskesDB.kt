package com.if3210_2022_android_28.perludilindungi.room

import android.app.Activity
import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import com.if3210_2022_android_28.perludilindungi.model.Faskes

@Database(
    entities = [Faskes::class],
    version = 2
)

abstract class FaskesDB : RoomDatabase(){

    abstract fun faskesDao() : FaskesDao

    companion object {

        @Volatile private var instance : FaskesDB? = null
        private val LOCK = Any()

        operator fun invoke(context: Activity) = instance ?: synchronized(LOCK){
            instance ?: buildDatabase(context).also {
                instance = it
            }
        }

        private fun buildDatabase(context: Context) = Room.databaseBuilder(
            context.applicationContext,
            FaskesDB::class.java,
            "perludilindungi.db"
        ).fallbackToDestructiveMigration().build()

    }
}