package com.example.noting;

import android.os.Bundle;

import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import com.example.noting.databinding.FragmentAddNoteBinding;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import com.example.noting.databinding.FragmentAddNoteBinding;

public class Add_Note extends Fragment {

    private FragmentAddNoteBinding binding;
    private DatabaseHelper dbHelper;
    private Note existingNote;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        binding = FragmentAddNoteBinding.inflate(inflater, container, false);
        View rootView = binding.getRoot();

        dbHelper = new DatabaseHelper(requireContext());

        Bundle args = getArguments();
        if (args != null) {
            existingNote = (Note) args.getSerializable("note");
            if (existingNote != null) {
                binding.editTextTitle.setText(existingNote.getTitle());
                binding.editTextContent.setText(existingNote.getContent());
            }
        }

        binding.buttonAddNote.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                saveNote();
            }
        });

        return rootView;
    }

    private void saveNote() {
        String title = binding.editTextTitle.getText().toString().trim();
        String content = binding.editTextContent.getText().toString().trim();
        String userEmail = dbHelper.getLoggedInUserEmail(); // Get the logged-in user's email

        if (title.isEmpty() || content.isEmpty()) {
            // Display an error
            return;
        }

        if (existingNote == null) {
            // Add new note
            Note note = new Note();
            note.setTitle(title);
            note.setContent(content);
            dbHelper.addNote(note, userEmail);
        } else {
            // Update existing note
            existingNote.setTitle(title);
            existingNote.setContent(content);
            dbHelper.updateNote(existingNote, userEmail);
        }

        // Navigate back to notes list
        getActivity().getSupportFragmentManager().popBackStack();
    }
}
