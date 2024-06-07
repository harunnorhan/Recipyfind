package com.example.therecipeapp.data.source.local

import androidx.room.migration.Migration
import androidx.sqlite.db.SupportSQLiteDatabase

val MIGRATION_1_3 = object : Migration(1, 3) {
    override fun migrate(db: SupportSQLiteDatabase) {
        db.execSQL("""
            CREATE TABLE recipes_temp (
                id INTEGER PRIMARY KEY NOT NULL,
                title TEXT NOT NULL,
                image TEXT NOT NULL
            )
        """)
        db.execSQL("""
            INSERT INTO recipes_temp (id, title, image)
            SELECT id, title, image FROM recipes
        """)
        db.execSQL("DROP TABLE recipes")
        db.execSQL("ALTER TABLE recipes_temp RENAME TO recipes")
    }
}