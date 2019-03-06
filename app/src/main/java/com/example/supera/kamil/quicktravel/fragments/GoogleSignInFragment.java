package com.example.supera.kamil.quicktravel.fragments;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.Toast;

import com.example.supera.kamil.quicktravel.R;
import com.example.supera.kamil.quicktravel.activities.MainActivity;
import com.google.android.gms.auth.api.signin.GoogleSignIn;
import com.google.android.gms.auth.api.signin.GoogleSignInAccount;
import com.google.android.gms.auth.api.signin.GoogleSignInClient;
import com.google.android.gms.auth.api.signin.GoogleSignInOptions;
import com.google.android.gms.common.api.ApiException;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;

import java.util.concurrent.Executor;

public class GoogleSignInFragment extends Fragment implements View.OnClickListener {

    private static final int RC_SIGN_IN = 9001;
    public GoogleSignInClient mGoogleSignInClient;
    View v;

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) { super.onCreate(savedInstanceState); }

    /**
     *
     * @param inflater
     * @param container
     * @param savedInstanceState
     * @return
     * Add to button onClickListener event.
     */
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        GoogleSignInOptions gso = new GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
            .requestEmail()
            .build();

        mGoogleSignInClient = GoogleSignIn.getClient(getContext(), gso);

        v = inflater.inflate(R.layout.google_sign_in_fragment, container, false);

        v.findViewById(R.id.sign_in_button).setOnClickListener(this);
        v.findViewById(R.id.sign_in_button).setVisibility(View.VISIBLE);

        v.findViewById(R.id.log_out_button).setOnClickListener(this);
        v.findViewById(R.id.log_out_button).setVisibility(View.INVISIBLE);

        return v;
    }

    private void signIn() {
        Intent signInIntent = mGoogleSignInClient.getSignInIntent();
        startActivityForResult(signInIntent, RC_SIGN_IN);
    }

    private void signOut() {
        mGoogleSignInClient.signOut()
            .addOnCompleteListener((Activity) getContext(), new OnCompleteListener<Void>() {
                @Override
                public void onComplete(@NonNull Task<Void> task) {
                    v.findViewById(R.id.sign_in_button).setVisibility(View.VISIBLE);
                    v.findViewById(R.id.log_out_button).setVisibility(View.INVISIBLE);
                }
            });
    }

    private void handleSignInResult(Task<GoogleSignInAccount> completedTask) {
        try {
            GoogleSignInAccount account = completedTask.getResult(ApiException.class);
            v.findViewById(R.id.sign_in_button).setVisibility(View.INVISIBLE);
            v.findViewById(R.id.log_out_button).setVisibility(View.VISIBLE);
        } catch (ApiException e) {
            Toast.makeText(getContext(), "Logowanie nie udane.", Toast.LENGTH_LONG).show();
        }
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (requestCode == RC_SIGN_IN) {
            Task<GoogleSignInAccount> task = GoogleSignIn.getSignedInAccountFromIntent(data);
            handleSignInResult(task);
        }
    }

    @Override
    public void onStart() {
        super.onStart();

        GoogleSignInAccount account = GoogleSignIn.getLastSignedInAccount(getContext());

        if (account != null) {
            v.findViewById(R.id.sign_in_button).setVisibility(View.INVISIBLE);
            v.findViewById(R.id.log_out_button).setVisibility(View.VISIBLE);
        }
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.sign_in_button:
                signIn();
                break;
            case R.id.log_out_button:
                signOut();
                break;
        }
    }
}
