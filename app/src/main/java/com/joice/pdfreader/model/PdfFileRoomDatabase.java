package com.joice.pdfreader.model;

import android.content.Context;

import androidx.room.Database;
import androidx.room.Room;
import androidx.room.RoomDatabase;

@Database(entities = {PdfFile.class}, version = 1, exportSchema = false)
public abstract class PdfFileRoomDatabase extends RoomDatabase {

    public abstract PdfFileDao pdfFileDao();

    private static PdfFileRoomDatabase INSTANCE;

    public static PdfFileRoomDatabase getDatabase(final Context context) {
        if (INSTANCE == null) {
            synchronized (PdfFileRoomDatabase.class) {
                if (INSTANCE == null) {
                    INSTANCE = Room.databaseBuilder(context.getApplicationContext(),
                            PdfFileRoomDatabase.class, "pdf_database")
                            .build();
                }
            }
        }
        return INSTANCE;
    }

}
