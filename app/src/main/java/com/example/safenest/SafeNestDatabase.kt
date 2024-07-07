package com.example.safenest

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase

@Database(entities = [ContactModel::class], version = 1, exportSchema = false)
public abstract class SafeNestDatabase :RoomDatabase(){
    abstract fun contactDAO():ContactDAO

    companion object{
        @Volatile
        private var INSTANCE: SafeNestDatabase ?= null
        fun getDatabase(context:Context):SafeNestDatabase {
            INSTANCE?.let {
                return it
            }
            return synchronized(SafeNestDatabase::class.java){
                val instance = Room.databaseBuilder(
                    context.applicationContext,
                    SafeNestDatabase::class.java, "SafeNest_db"
                ).build()

                INSTANCE = instance
                instance
            }
        }
    }
}