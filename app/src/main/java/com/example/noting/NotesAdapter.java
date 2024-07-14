package com.example.noting;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.TextView;
import androidx.annotation.NonNull;
import androidx.appcompat.widget.AppCompatImageView;
import androidx.recyclerview.widget.RecyclerView;
import java.util.ArrayList;

public class NotesAdapter extends RecyclerView.Adapter<NotesAdapter.ViewHolder> {

    private Context context;
    private ArrayList<Note> notesList;
    private OnNoteClickListener onNoteClickListener;

    public NotesAdapter(Context context, ArrayList<Note> notesList, OnNoteClickListener onNoteClickListener ) {
        this.context = context;
        this.notesList = notesList;
        this.onNoteClickListener = onNoteClickListener;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.item_note, parent, false);
        return new ViewHolder(view, onNoteClickListener);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        Note note = notesList.get(position);
        holder.textViewTitle.setText(note.getTitle());
        holder.textViewContent.setText(note.getContent());
    }

    @Override
    public int getItemCount() {
        return notesList.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {
        TextView textViewTitle, textViewContent;
        AppCompatImageView btnEdit, btnDelete;
        OnNoteClickListener onNoteClickListener;

        public ViewHolder(@NonNull View itemView, OnNoteClickListener onNoteClickListener) {
            super(itemView);
            textViewTitle = itemView.findViewById(R.id.textViewTitle);
            textViewContent = itemView.findViewById(R.id.textViewContent);
            btnEdit = itemView.findViewById(R.id.btnEdit);
            btnDelete = itemView.findViewById(R.id.btnDelete);
            this.onNoteClickListener = onNoteClickListener;

            btnEdit.setOnClickListener(this);
            btnDelete.setOnClickListener(this);
        }

        @Override
        public void onClick(View v) {
            if (v.getId() == R.id.btnEdit) {
                onNoteClickListener.onEditClick(getAdapterPosition());
            } else if (v.getId() == R.id.btnDelete) {
                onNoteClickListener.onDeleteClick(getAdapterPosition());
            }
        }
    }

    public interface OnNoteClickListener {
        void onEditClick(int position);
        void onDeleteClick(int position);
    }
}

