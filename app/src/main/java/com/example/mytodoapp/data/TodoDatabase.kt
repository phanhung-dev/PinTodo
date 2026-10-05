package com.example.mytodoapp.data

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import androidx.room.migration.Migration
import androidx.sqlite.db.SupportSQLiteDatabase

@Database(
    entities = [Todo::class],
    version = 4,
    exportSchema = false
)
abstract class TodoDatabase : RoomDatabase() {

    abstract fun todoDao(): TodoDao

    companion object {

        private var instance: TodoDatabase? = null

        private val MIGRATION_1_2 = object : Migration(1, 2) {
            override fun migrate(db: SupportSQLiteDatabase) {
                db.execSQL(
                    "ALTER TABLE todos ADD COLUMN category TEXT"
                )
            }
        }

        private val MIGRATION_2_3 = object : Migration(2, 3) {
            override fun migrate(db: SupportSQLiteDatabase) {
                db.execSQL(
                    "ALTER TABLE todos " +
                            "ADD COLUMN priority TEXT NOT NULL DEFAULT 'NONE'"
                )
            }
        }

        private val MIGRATION_3_4 = object : Migration(3, 4) {
            override fun migrate(db: SupportSQLiteDatabase) {
                db.execSQL(
                    "ALTER TABLE todos ADD COLUMN dueDate TEXT"
                )

                db.execSQL(
                    "ALTER TABLE todos ADD COLUMN dueTimeMinutes INTEGER"
                )

                db.execSQL(
                    "ALTER TABLE todos ADD COLUMN reminderMinutesBefore INTEGER"
                )

                db.execSQL(
                    "ALTER TABLE todos " +
                            "ADD COLUMN repeatType TEXT NOT NULL DEFAULT 'NONE'"
                )

                db.execSQL(
                    "ALTER TABLE todos ADD COLUMN repeatCount INTEGER"
                )
            }
        }

        @Synchronized
        fun getDatabase(context: Context): TodoDatabase {
            val existingDatabase = instance

            if(existingDatabase != null) return existingDatabase

            val newDatabase = Room.databaseBuilder(
                context.applicationContext,
                TodoDatabase::class.java,
                "todo_database.db"
            )
                .addMigrations(MIGRATION_1_2, MIGRATION_2_3, MIGRATION_3_4)
                .build()

            instance = newDatabase

            return newDatabase
        }
    }
}