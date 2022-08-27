package com.joice.pdfreader;

import android.text.method.ScrollingMovementMethod;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ScrollView;
import android.widget.TextView;
import androidx.annotation.NonNull;
import androidx.recyclerview.widget.DiffUtil;
import androidx.recyclerview.widget.ListAdapter;
import androidx.recyclerview.widget.RecyclerView;

import com.joice.pdfreader.model.PdfFile;

interface OnItemClickListener {
    void onItemClick(String pdfFileName);
}

class PdfViewHolder extends RecyclerView.ViewHolder {
    private final TextView pdfFileTextView;

    private PdfViewHolder(View itemView, OnItemClickListener listener) {
        super(itemView);
        pdfFileTextView = itemView.findViewById(R.id.textView);
        pdfFileTextView.setMovementMethod(new ScrollingMovementMethod());
        pdfFileTextView.setOnClickListener(v -> listener.onItemClick(pdfFileTextView.getText().toString()));
    }

    public void bind(String text) {
        pdfFileTextView.setText(text);
    }

    static PdfViewHolder create(ViewGroup parent, OnItemClickListener listener) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.recyclerview_item, parent, false);
        return new PdfViewHolder(view, listener);
    }
}


public class PdfFileListAdapter extends ListAdapter<PdfFile, PdfViewHolder> {

    private final OnItemClickListener listener;

    public PdfFileListAdapter(@NonNull DiffUtil.ItemCallback<PdfFile> diffCallback, OnItemClickListener listener) {
        super(diffCallback);
        this.listener = listener;
    }

    @NonNull
    @Override
    public PdfViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        return PdfViewHolder.create(parent, listener);
    }

    @Override
    public void onBindViewHolder(PdfViewHolder holder, int position) {
        PdfFile current = getItem(position);
        holder.bind(current.getFileName());
    }

    static class WordDiff extends DiffUtil.ItemCallback<PdfFile> {

        @Override
        public boolean areItemsTheSame(@NonNull PdfFile oldItem, @NonNull PdfFile newItem) {
            return oldItem == newItem;
        }

        @Override
        public boolean areContentsTheSame(@NonNull PdfFile oldItem, @NonNull PdfFile newItem) {
            return oldItem.getFileName().equals(newItem.getFileName());
        }
    }
}
