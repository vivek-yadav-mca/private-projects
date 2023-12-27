package com.zero07.rssb.activity;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.text.method.LinkMovementMethod;
import android.util.Log;
import android.view.View;
import android.widget.TextView;

import com.google.android.gms.auth.api.signin.GoogleSignIn;
import com.google.android.gms.auth.api.signin.GoogleSignInAccount;
import com.google.android.gms.auth.api.signin.GoogleSignInClient;
import com.google.android.gms.auth.api.signin.GoogleSignInOptions;
import com.google.android.gms.common.SignInButton;
import com.google.android.gms.tasks.Task;
import com.zero07.rssb.R;
import com.zero07.rssb.userModels.User;
import com.zero07.rssb.userModels.UserContext;
import com.zero07.rssb.databinding.ActivitySignupBinding;

public class SignupActivity extends AppCompatActivity {

    ActivitySignupBinding binding;
    GoogleSignInClient mGoogleSignInClient;
    private User loggedInUser;
    private static int RC_SIGN_IN = 100;
    private static String TAG = SignupActivity.class.getName() ;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivitySignupBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        privacyHyperlink();

// Configure sign-in to request the user's ID, email address, and basic
// profile. ID and basic profile are included in DEFAULT_SIGN_IN.
        GoogleSignInOptions gso = new GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
                .requestEmail()
                .build();

// Build a GoogleSignInClient with the options specified by gso.
        mGoogleSignInClient = GoogleSignIn.getClient(this, gso);

// Check for existing Google Sign In account, if the user is already signed in
// the GoogleSignInAccount will be non-null.
        GoogleSignInAccount account = GoogleSignIn.getLastSignedInAccount(this);

        if (account == null) {
// Set the dimensions of the sign-in button.
            SignInButton signInButton = findViewById(R.id.sign_in_button);
            signInButton.setSize(SignInButton.SIZE_WIDE);

            signInButton.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    signIn();
                }
            });
        } else {
            registerUserAndNavigate(account);
        }

    }


    @Override
    public void onBackPressed() {
        Intent intent = new Intent(SignupActivity.this, MainActivity.class);
        startActivity(intent);
    }

    private void privacyHyperlink() {
        TextView linkTextView = findViewById(R.id.privacy_hyperlink);
        linkTextView.setMovementMethod(LinkMovementMethod.getInstance());
    }

    private void signIn() {
        Intent signInIntent = mGoogleSignInClient.getSignInIntent();
        startActivityForResult(signInIntent, RC_SIGN_IN);
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        // Result returned from launching the Intent from GoogleSignInClient.getSignInIntent(...);
        if (requestCode == RC_SIGN_IN) {
            // The Task returned from this call is always completed, no need to attach
            // a listener.
            Task<GoogleSignInAccount> task = GoogleSignIn.getSignedInAccountFromIntent(data);
            handleSignInResult(task);
        }
    }

    private void handleSignInResult(Task<GoogleSignInAccount> completedTask) {
        try {
            //GoogleSignInAccount account = completedTask.getResult(ApiException.class);
            GoogleSignInAccount account = GoogleSignIn.getLastSignedInAccount(this);
            if (account != null) {
                registerUserAndNavigate(account);
            }
            // Signed in successfully, show authenticated UI.
        } catch (Exception e) {
            // The ApiException status code indicates the detailed failure reason.
            // Please refer to the GoogleSignInStatusCodes class reference for more information.
            Log.e(TAG, e.toString());
        }
    }

    public void registerUserAndNavigate(GoogleSignInAccount googleSignInAccount) {
        loggedInUser = new User(googleSignInAccount.getEmail().replaceAll("[.#$\\[\\]]", ""),
                googleSignInAccount.getDisplayName(),
                googleSignInAccount.getEmail(),
                googleSignInAccount.getId(),
                (googleSignInAccount.getPhotoUrl() != null ? googleSignInAccount.getPhotoUrl().toString() : null));
        UserContext.setLoggedInUser(loggedInUser);
//        UserService userService = new UserService();
//        userService.loadUserData(getApplicationContext(), loggedInUser.getId(), loggedInUser);
//        userService.updateUserDataToFirebase(getApplicationContext());
        startActivity(new Intent(SignupActivity.this, SocialPostActivity.class));
        finish();
    }




}


//        GoogleSignInAccount acct = GoogleSignIn.getLastSignedInAccount(getActivity());
//        if (acct != null) {
//            String personName = acct.getDisplayName();
//            String personGivenName = acct.getGivenName();
//            String personFamilyName = acct.getFamilyName();
//            String personEmail = acct.getEmail();
//            String personId = acct.getId();
//            Uri personPhoto = acct.getPhotoUrl();
//        }



