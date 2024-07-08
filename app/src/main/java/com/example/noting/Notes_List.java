package com.example.noting;

import android.os.Bundle;

import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.recyclerview.widget.LinearLayoutManager;
import com.example.noting.databinding.FragmentNotesListBinding;
import java.util.ArrayList;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;

public class Notes_List extends Fragment implements NotesAdapter.OnNoteClickListener {

    private FragmentNotesListBinding binding;
    private NotesAdapter notesAdapter;
    private ArrayList<Note> noteList;
    private DatabaseHelper dbHelper;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        binding = FragmentNotesListBinding.inflate(inflater, container, false);
        View rootView = binding.getRoot();

        dbHelper = new DatabaseHelper(requireContext());
        String userEmail = dbHelper.getLoggedInUserEmail();
        noteList = dbHelper.getAllNotes(userEmail);

        notesAdapter = new NotesAdapter(getActivity(), noteList, this);
        binding.recyclerViewNotes.setLayoutManager(new LinearLayoutManager(getActivity()));
        binding.recyclerViewNotes.setAdapter(notesAdapter);

        binding.fabAddNote.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                navigateToAddEditNoteFragment(null);
            }
        });

        binding.btnLogout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                logoutUser();
            }
        });
        return rootView;
    }

    private void logoutUser() {
        dbHelper.logoutUser();
        FragmentManager fragmentManager = requireActivity().getSupportFragmentManager();
        FragmentTransaction transaction = fragmentManager.beginTransaction();
        transaction.replace(R.id.container_fragment, new Login_Fragment());
        transaction.commit();
    }

    @Override
    public void onDeleteClick(int position) {
        Note note = noteList.get(position);
        String userEmail = dbHelper.getLoggedInUserEmail();
        dbHelper.deleteNote(note.getId(), userEmail);
        noteList.remove(position);
        notesAdapter.notifyItemRemoved(position);
        Toast.makeText(requireContext(), "Note deleted", Toast.LENGTH_SHORT).show();
    }
    @Override
    public void onEditClick(int position) {
        Note note = noteList.get(position);
        navigateToAddEditNoteFragment(note);
    }
    private void navigateToAddEditNoteFragment(Note note) {
        Bundle bundle = new Bundle();
        bundle.putSerializable("note", note);
        Add_Note addNoteFragment = new Add_Note();
        addNoteFragment.setArguments(bundle);

        FragmentManager fragmentManager = requireActivity().getSupportFragmentManager();
        FragmentTransaction transaction = fragmentManager.beginTransaction();
        transaction.replace(R.id.container_fragment, addNoteFragment);
        transaction.addToBackStack(null);
        transaction.commit();
    }
}
