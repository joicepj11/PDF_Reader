package com.joice.pdfreader;

import android.graphics.Color;
import android.net.Uri;
import android.util.Log;

import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;

import com.github.barteksc.pdfviewer.PDFView;
import com.github.barteksc.pdfviewer.listener.OnErrorListener;
import com.github.barteksc.pdfviewer.listener.OnLoadCompleteListener;
import com.github.barteksc.pdfviewer.listener.OnPageChangeListener;
import com.github.barteksc.pdfviewer.listener.OnPageErrorListener;
import com.github.barteksc.pdfviewer.scroll.DefaultScrollHandle;
import com.github.barteksc.pdfviewer.util.FitPolicy;
import com.joice.pdfreader.model.PDFViewModel;
import com.joice.pdfreader.model.PdfFile;
import com.shockwave.pdfium.PdfDocument;
import org.androidannotations.annotations.AfterExtras;
import org.androidannotations.annotations.AfterViews;
import org.androidannotations.annotations.EActivity;
import org.androidannotations.annotations.Extra;
import org.androidannotations.annotations.ViewById;

import java.util.List;

@EActivity(R.layout.activity_pdfview)
public class PDFViewActivity extends AppCompatActivity implements OnPageChangeListener, OnLoadCompleteListener,
        OnPageErrorListener, OnErrorListener {

    private static final String TAG = PDFViewActivity.class.getSimpleName();
    private PDFViewModel pdfViewModel;
    PdfFile pdfFile;
    int previousPage = 0;
    @Extra
    String pdfFileName;

    @ViewById
    PDFView pdfView;

    boolean viewSet = false;


    @AfterViews
    void afterViews() {
        pdfView.setBackgroundColor(Color.LTGRAY);
        pdfViewModel = new ViewModelProvider(this).get(PDFViewModel.class);
        Log.d(TAG, "afterViews: " + pdfFileName);
        pdfViewModel.getPdfFile(pdfFileName).observe(this, this::displayPdfFile);
    }

    @AfterExtras
    void afterExtras() {
        setTitle(pdfFileName);
    }

    private void displayPdfFile(PdfFile pdfFile) {
        this.pdfFile = pdfFile;
        if (pdfFile == null) {
            finish();
        } else {
            previousPage = pdfFile.getPageNumber();
            Uri parse = Uri.parse(pdfFile.getFileLocation());
            if (!viewSet) {
                pdfView.fromUri(parse)
                        .defaultPage(pdfFile.getPageNumber())
                        .onPageChange(this)
                        .enableAnnotationRendering(true)
                        .onLoad(this)
                        .scrollHandle(new DefaultScrollHandle(this))
                        .spacing(2)
                        .onPageError(this)
                        .onError(this)
                        .enableSwipe(true)
                        .enableDoubletap(true)
                        .pageFitPolicy(FitPolicy.WIDTH)
                        .fitEachPage(true)
                        .load();
            }
            viewSet = true;
        }
    }


    @Override
    public void onPageChanged(int page, int pageCount) {
        if (previousPage != page) {
            pdfFile.setPageNumber(page);
            previousPage = page;
            pdfViewModel.update(pdfFile);
            setTitle(String.format("%s %s / %s", pdfFileName, page + 1, pageCount));
        }
    }

    @Override
    public void loadComplete(int nbPages) {
        printBookmarksTree(pdfView.getTableOfContents(), "-");
    }

    public void printBookmarksTree(List<PdfDocument.Bookmark> tree, String sep) {
        for (PdfDocument.Bookmark b : tree) {
            Log.e(TAG, String.format("%s %s, p %d", sep, b.getTitle(), b.getPageIdx()));
            if (b.hasChildren()) {
                printBookmarksTree(b.getChildren(), sep + "-");
            }
        }
    }

    @Override
    public void onPageError(int page, Throwable t) {
        Log.e(TAG, "Cannot load page " + page);
    }

    @Override
    public void onError(Throwable t) {
        Log.e(TAG, "Cannot load page ");
        pdfViewModel.delete(this.pdfFile);
        finish();
    }
}