package com.joice.pdfreader.model;

import android.app.Application;
import android.os.AsyncTask;

import androidx.lifecycle.LiveData;

import java.util.List;

public class PdfRepository {

    private PdfFileDao pdfFileDao;
    private LiveData<List<PdfFile>> allPdfFiles;

    PdfRepository(Application application) {
        PdfFileRoomDatabase db = PdfFileRoomDatabase.getDatabase(application);
        pdfFileDao = db.pdfFileDao();
        allPdfFiles = pdfFileDao.getAllPdfFiles();
    }

    LiveData<List<PdfFile>> getAllPdfFiles() {
        return allPdfFiles;
    }

    LiveData<PdfFile> getPdfFile(String fileName) {
        return pdfFileDao.getPdfFileByName(fileName);
    }

    public void insert(PdfFile pdfFile) {
        new insertAsyncTask(pdfFileDao, "insert").execute(pdfFile);
    }

    public void update(PdfFile pdfFile) {
        new insertAsyncTask(pdfFileDao, "update").execute(pdfFile);
    }

    public void delete(PdfFile pdfFile) {
        new insertAsyncTask(pdfFileDao, "delete").execute(pdfFile);
    }

    private static class insertAsyncTask extends AsyncTask<PdfFile, Void, Void> {

        private final PdfFileDao pdfFileDao1;
        private final String action;

        insertAsyncTask(PdfFileDao dao, String action) {
            pdfFileDao1 = dao;
            this.action = action;
        }

        @Override
        protected Void doInBackground(final PdfFile... params) {
            switch (action) {
                case "insert":
                    pdfFileDao1.insert(params[0]);
                    break;
                case "update":
                    pdfFileDao1.update(params[0]);
                    break;
                case "delete":
                    pdfFileDao1.delete(params[0]);
                    break;
            }
            return null;
        }
    }
}
