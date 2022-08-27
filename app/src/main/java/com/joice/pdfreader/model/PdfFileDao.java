package com.joice.pdfreader.model;

import androidx.lifecycle.LiveData;
import androidx.room.Dao;
import androidx.room.Delete;
import androidx.room.Insert;
import androidx.room.Query;
import androidx.room.Update;

import java.util.List;

@Dao
public interface PdfFileDao {

    @Insert
    void insert(PdfFile word);

    @Update
    void update(PdfFile word);

    @Delete
    void delete(PdfFile word);

    @Query("DELETE FROM pdf_file")
    void deleteAll();

    @Query("SELECT * from pdf_file ORDER BY id ASC")
    LiveData<List<PdfFile>> getAllPdfFiles();

    @Query("SELECT * from pdf_file where file_name like :fileName")
    LiveData<PdfFile> getPdfFileByName(String fileName);

}
