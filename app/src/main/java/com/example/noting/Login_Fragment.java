package com.example.noting;

import android.content.Intent;
import android.os.Bundle;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.Toast;
import com.google.android.gms.auth.api.signin.GoogleSignIn;
import com.google.android.gms.auth.api.signin.GoogleSignInAccount;
import com.google.android.gms.auth.api.signin.GoogleSignInClient;
import com.google.android.gms.auth.api.signin.GoogleSignInOptions;
import com.google.android.gms.common.api.ApiException;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;

public class Login_Fragment extends Fragment {

    private GoogleSignInOptions gso;
    private GoogleSignInClient gsc;
    private ImageButton googleplusBtn;
    private DatabaseHelper dbHelper;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_login_, container, false);

        googleplusBtn = view.findViewById(R.id.imageView2);

        dbHelper = new DatabaseHelper(requireContext());

        gso = new GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
                .requestEmail()
                .build();
        gsc = GoogleSignIn.getClient(requireActivity(), gso);

        GoogleSignInAccount account = GoogleSignIn.getLastSignedInAccount(requireActivity());
        if (account != null) {
            gsc.signOut().addOnCompleteListener(requireActivity(), new OnCompleteListener<Void>() {
                @Override
                public void onComplete(@NonNull Task<Void> task) {
                    // No need to do anything here, just ensuring user is logged out
                }
            });
        }

        if (dbHelper.isUserLoggedIn()) {
            navigateToNotesListFragment();
        }

        googleplusBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                signIn();
            }
        });
        return view;
    }

    void signIn() {
        Intent signInIntent = gsc.getSignInIntent();
        startActivityForResult(signInIntent, 1000);
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == 1000) {
            Task<GoogleSignInAccount> task = GoogleSignIn.getSignedInAccountFromIntent(data);

            try {
                GoogleSignInAccount account = task.getResult(ApiException.class);
                if (account != null) {
                    dbHelper.addUser(account.getEmail(), account.getDisplayName());
                    navigateToNotesListFragment();
                } else {
                    Toast.makeText(requireContext(), "Sign-In Failed", Toast.LENGTH_SHORT).show();
                }
            } catch (ApiException e) {
                Toast.makeText(requireContext(), "Something went wrong: " + e.getStatusCode(), Toast.LENGTH_SHORT).show();
            }
        }
    }

    private void navigateToNotesListFragment() {
        FragmentManager fragmentManager = requireActivity().getSupportFragmentManager();
        FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
        Notes_List notesListFragment = new Notes_List();
        fragmentTransaction.replace(R.id.container_fragment, notesListFragment);
        fragmentTransaction.addToBackStack(null);
        fragmentTransaction.commit();
    }

    public void logoutUser(final LogoutCallback callback) {
        if (isAdded() && getActivity() != null) {
            gsc.signOut().addOnCompleteListener(getActivity(), new OnCompleteListener<Void>() {
                @Override
                public void onComplete(@NonNull Task<Void> task) {
                    dbHelper.logoutUser();
                    if (getActivity() != null) {
                        Toast.makeText(getActivity(), "Logged out successfully", Toast.LENGTH_SHORT).show();
                    } else {
                        // Handle case where getActivity() returns null (should not happen here)
                    }
                    if (callback != null) {
                        callback.onLogoutSuccess();
                    }
                }
            });
        } else {
            if (callback != null) {
                callback.onLogoutSuccess();
            }
        }
    }


    private void navigateToLoginFragment() {
        FragmentManager fragmentManager = requireActivity().getSupportFragmentManager();
        FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
        Login_Fragment loginFragment = new Login_Fragment();
        fragmentTransaction.replace(R.id.container_fragment, loginFragment);
        fragmentTransaction.addToBackStack(null);
        fragmentTransaction.commit();
    }

    public interface LogoutCallback {
        void onLogoutSuccess();
    }
}
