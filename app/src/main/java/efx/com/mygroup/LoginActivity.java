package efx.com.mygroup;

import android.app.Activity;
import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;

import com.firebase.ui.auth.AuthUI;
import com.google.firebase.auth.FirebaseAuth;

import java.util.Arrays;
import java.util.List;

public class LoginActivity extends AppCompatActivity {

    private FirebaseAuth auth;
    private static int RC_SIGN_IN = 123;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        //setContentView(R.layout.activity_login);
        auth = FirebaseAuth.getInstance(); //This determines whether or not the user is signed in

        //Declaring a List of the possible ways users can sign in [Email, Google, and Twitter]
        List<AuthUI.IdpConfig> signInProviders = Arrays.asList(
                new AuthUI.IdpConfig.EmailBuilder().build(),
                new AuthUI.IdpConfig.GoogleBuilder().build()
        );

        startSignIn(signInProviders);
    }

    void startSignIn(List<AuthUI.IdpConfig> providers){
        //An activity is a new page, this method opens a new screen which is the login page
        startActivityForResult(
            AuthUI.getInstance() //
                    .createSignInIntentBuilder()
                    .setAvailableProviders(providers)
                    //.setLogo(R.drawable.fileHere)
                    //.setTheme(R.style.Theme_AppCompat)
                    .build(),
            RC_SIGN_IN);


        //setContentView(R.layout.activity_login);

    }

    @Override
    protected void onActivityResult (int requestCode, int resultCode, Intent data){
        if (resultCode == RESULT_CANCELED){
            Activity myActivity = this;
            myActivity.finish();
            System.exit(0);
        }
    }

}
