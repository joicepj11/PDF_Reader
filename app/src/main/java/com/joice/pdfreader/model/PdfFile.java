package com.joice.pdfreader.model;

import androidx.annotation.NonNull;
import androidx.room.ColumnInfo;
import androidx.room.Entity;
import androidx.room.PrimaryKey;

@Entity(tableName = "pdf_file")
public class PdfFile {

    @NonNull
    @ColumnInfo(name = "file_name")
    String fileName;

    @NonNull
    @ColumnInfo(name = "file_location")
    String fileLocation;

    @ColumnInfo(name = "author_name")
    String authorName;

    @PrimaryKey(autoGenerate = true)
    int id;

    public PdfFile() {
    }

    public PdfFile(@NonNull String fileName, @NonNull String fileLocation, String authorName) {
        this.fileName = fileName;
        this.fileLocation = fileLocation;
        this.authorName = authorName;
    }

    public int getPageNumber() {
        return pageNumber;
    }

    public void setPageNumber(int pageNumber) {
        this.pageNumber = pageNumber;
    }

    @NonNull
    @ColumnInfo(name = "page_number")
    int pageNumber = 0;

    @NonNull
    public String getFileName() {
        return fileName;
    }

    public void setFileName(@NonNull String fileName) {
        this.fileName = fileName;
    }

    public String getAuthorName() {
        return authorName;
    }

    public void setAuthorName(String authorName) {
        this.authorName = authorName;
    }

    @NonNull
    public String getFileLocation() {
        return fileLocation;
    }

    public void setFileLocation(@NonNull String fileLocation) {
        this.fileLocation = fileLocation;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }
}