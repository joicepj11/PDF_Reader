package com.joice.pdfreader;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.os.ParcelFileDescriptor;
import android.provider.MediaStore;
import android.util.Log;

import androidx.activity.result.ActivityResult;
import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.appcompat.app.AppCompatActivity;
import androidx.documentfile.provider.DocumentFile;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.joice.pdfreader.databinding.ActivityMainBinding;
import com.joice.pdfreader.model.PDFViewModel;
import com.joice.pdfreader.model.PdfFile;
import com.shockwave.pdfium.PdfDocument;
import com.shockwave.pdfium.PdfiumCore;

import java.io.IOException;
import java.net.URISyntaxException;

public class MainActivity extends AppCompatActivity {

    private ActivityMainBinding binding;
    private PDFViewModel pdfViewModel;

    private static final String TAG = MainActivity.class.getSimpleName();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        binding = ActivityMainBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        setSupportActionBar(binding.toolbar);

        RecyclerView recyclerView = findViewById(R.id.recyclerview);
        final PdfFileListAdapter adapter = new PdfFileListAdapter(new PdfFileListAdapter.WordDiff(), new OnItemClickListener() {
            @Override
            public void onItemClick(String pdfFileName) {
                try {
                    Intent myIntent = new Intent(MainActivity.this, PDFViewActivity_.class);
                    myIntent.putExtra("pdfFileName", pdfFileName); //Optional parameters
                    MainActivity.this.startActivity(myIntent);
                }catch (Exception e){}
            }
        });
        recyclerView.setAdapter(adapter);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));

        pdfViewModel = new ViewModelProvider(this).get(PDFViewModel.class);
        pdfViewModel.getAllPDFFiles().observe(this, adapter::submitList);
        ActivityResultLauncher<Intent> resultLauncher = registerForActivityResult(
                new ActivityResultContracts.StartActivityForResult(),
                result -> {
                    if (result.getResultCode() == Activity.RESULT_OK) {
                        try {
                            savePdfDetails(MainActivity.this, result);
                        } catch (Exception e) {
                            e.printStackTrace();
                        }
                    }
                });

        binding.fab.setOnClickListener(view -> {
            Intent intent = new Intent(Intent.ACTION_OPEN_DOCUMENT);
            intent.setType("application/pdf");
            intent.addCategory(Intent.CATEGORY_OPENABLE);
            resultLauncher.launch(intent);
        });
    }

    @SuppressLint({"NewApi", "WrongConstant"})
    public void savePdfDetails(Context context, ActivityResult result) throws Exception {
        assert result.getData() != null;
        Uri uri = result.getData().getData();
        Intent data = result.getData();
        Log.d(TAG, data.toString());
        try {
            ParcelFileDescriptor pfd = context.getContentResolver().openFileDescriptor(uri, "r");
            PdfiumCore pdfiumCore = new PdfiumCore(context);
            PdfDocument pdfDocument = pdfiumCore.newDocument(pfd);
            pdfiumCore.openPage(pdfDocument, 0);
            PdfDocument.Meta meta = pdfiumCore.getDocumentMeta(pdfDocument);
            String fileName = meta.getTitle();
            if (fileName == null || fileName.isEmpty()){
                DocumentFile documentFile = DocumentFile.fromSingleUri(context, data.getData());
                fileName = documentFile.getName();
            }
            if (fileName.contains(".")) {
                fileName = fileName.substring(0, fileName.lastIndexOf('.'));
            }
            PdfFile pdfFile = new PdfFile(fileName, data.getData().toString(), meta.getAuthor());
            pdfViewModel.insert(pdfFile);
            final int takeFlags = data.getFlags()
                    & (Intent.FLAG_GRANT_READ_URI_PERMISSION
                    | Intent.FLAG_GRANT_WRITE_URI_PERMISSION);
            context.getContentResolver().takePersistableUriPermission(uri, takeFlags);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

}