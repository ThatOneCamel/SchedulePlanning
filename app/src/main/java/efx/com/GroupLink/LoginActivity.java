package efx.com.GroupLink;

import android.app.Dialog;
import android.content.Intent;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.firebase.ui.auth.AuthUI;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.UserProfileChangeRequest;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.util.Arrays;
import java.util.List;

public class LoginActivity extends AppCompatActivity {

    private FirebaseAuth auth;
    private EditText email, pass;

    DatabaseReference databaseRef;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        FirebaseDatabase database = FirebaseDatabase.getInstance();
        databaseRef = database.getReference("users");

        auth = FirebaseAuth.getInstance(); //This determines whether or not the user is signed in
        email = findViewById(R.id.editText);
        pass = findViewById(R.id.editText2);

        if (auth.getCurrentUser() != null){
            goToMain();
        } else {

            //Declaring a List of the possible ways users can sign in [Email and Phone Number]
            List<AuthUI.IdpConfig> signInProviders = Arrays.asList(
                    new AuthUI.IdpConfig.EmailBuilder().build(),
                    new AuthUI.IdpConfig.GoogleBuilder().build()
            );

        }

    }

    public void signUpStart(View btn){

        if (invalidInput()){
            Toast.makeText(this, "Please input text into all fields", Toast.LENGTH_LONG).show();
        } else {
            attemptCreateAccount();
        }

    }

    private void attemptCreateAccount(){
        String emailStr = email.getText().toString();
        String passStr = pass.getText().toString();
        auth.createUserWithEmailAndPassword(emailStr, passStr)
                .addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(Task<AuthResult> task) {
                        if (task.isSuccessful()){
                            //Show username dialog and continue
                            createUsernameDialog().show();

                            //Default is set as a user's display name in the event
                            // an error occurred before they could finish signing up
                            databaseRef.child(auth.getUid()).child("username").setValue("Default");

                        } else {
                            Log.w("ERROR Signup", "createUserWithEmail:failure", task.getException());

                            Toast.makeText(getApplication(), task.getException().getMessage(), Toast.LENGTH_LONG).show();
                        }


                    }

                });//End Create User

    }

    public Dialog createUsernameDialog(){

        final AlertDialog dialog = new AlertDialog.Builder(this).setCancelable(false).create();
        View customView = getLayoutInflater().inflate(R.layout.display_name_input, null);
        dialog.setView(customView);

        final EditText name = customView.findViewById(R.id.editDisplayName);
        Button confirmation = customView.findViewById(R.id.displayConfirmBtn);

        confirmation.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(name.getText().toString().isEmpty())
                    Toast.makeText(getApplication(), "Name cannot be empty", Toast.LENGTH_SHORT).show();
                 else
                    inputDisplayName(name.getText().toString());
            }
        });//End OnClickListener

        return dialog;
    }

    private void inputDisplayName(String displayName){
        UserProfileChangeRequest profileUpdates = new UserProfileChangeRequest.Builder()
                .setDisplayName(displayName)
                .build();

        auth.getCurrentUser().updateProfile(profileUpdates)
                .addOnCompleteListener(new OnCompleteListener<Void>() {
                    @Override
                    public void onComplete(@NonNull Task<Void> task) {
                        if (task.isSuccessful()) {
                            databaseRef.child(auth.getUid()).child("username").setValue(auth.getCurrentUser().getDisplayName());
                            goToMain();
                            Log.d("Success!", "User profile updated.");
                        }


                    }
                });//End Update Profile

    }


    public void login(View btn) {

        if (invalidInput()) {
            Toast.makeText(this, "Please input text into all fields", Toast.LENGTH_LONG).show();
        } else {
            attemptLogin();
        }

    }

    private boolean invalidInput(){
        return (email.getText().toString().isEmpty() || pass.getText().toString().isEmpty());
    }

    private void attemptLogin(){
        String emailStr = email.getText().toString();
        String passStr = pass.getText().toString();

        auth.signInWithEmailAndPassword(emailStr, passStr)
            .addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                @Override
                public void onComplete(Task<AuthResult> task) {
                    if (task.isSuccessful()) {
                        goToMain();


                    } else {
                        Log.e("FAILURE", "Sign-in Failed: " + task.getException().getMessage());
                        Toast.makeText(getApplication(), task.getException().getMessage(), Toast.LENGTH_LONG).show();
                    }


                }
            });//End signIn


    }//End attemptLogin

    private void goToMain(){
        Intent intent = new Intent(LoginActivity.this, MainScreenActivity.class);
        Toast.makeText(getApplicationContext(), "DEBUG: Welcome to GroupLink! We have signed you in", Toast.LENGTH_SHORT).show();
        startActivity(intent);
        finish();
    }


}//End Activity
