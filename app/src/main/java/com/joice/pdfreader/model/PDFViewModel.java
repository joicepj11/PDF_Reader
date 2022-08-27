package com.joice.pdfreader.model;

import android.app.Application;

import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.LiveData;

import java.util.List;

public class PDFViewModel extends AndroidViewModel {

    private final PdfRepository mRepository;

    private final LiveData<List<PdfFile>> allPDFFiles;

    public PDFViewModel(Application application) {
        super(application);
        mRepository = new PdfRepository(application);
        allPDFFiles = mRepository.getAllPdfFiles();
    }

    public LiveData<List<PdfFile>> getAllPDFFiles() {
        return allPDFFiles;
    }

    public LiveData<PdfFile> getPdfFile(String fileName) {
        return mRepository.getPdfFile(fileName);
    }

    public void insert(PdfFile pdfFile) {
        mRepository.insert(pdfFile);
    }

    public void update(PdfFile pdfFile) {
        mRepository.update(pdfFile);
    }

    public void delete(PdfFile pdfFile) {
        mRepository.delete(pdfFile);
    }
}