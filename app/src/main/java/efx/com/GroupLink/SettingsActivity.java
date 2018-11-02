package efx.com.GroupLink;

import androidx.appcompat.app.AppCompatActivity;

import android.app.AlertDialog;
import android.app.Dialog;
import android.content.Intent;
import android.content.res.ColorStateList;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.RadioButton;
import android.widget.RadioGroup;

import com.google.firebase.auth.FirebaseAuth;

public class SettingsActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_settings);

    }

    public void showProfileSettings(View v){

    }

    public void showColorOptions(View v){
        createColorDialog().show();

    }

    public void showGroupSettings(View v){

    }

    public void logOut(View logOut) {
        FirebaseAuth.getInstance().signOut();
        Intent restart = new Intent(SettingsActivity.this, LoginActivity.class);
        MainScreenActivity.mainUser.deleteLocalData(getApplicationContext());
        restart.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
        startActivity(restart);
        finish();
    }

    public Dialog createColorDialog(){

        final AlertDialog dialog = new AlertDialog.Builder(this).create();
        View customView = getLayoutInflater().inflate(R.layout.settings_colors, null);
        dialog.setView(customView);

        final RadioGroup group = customView.findViewById(R.id.radioGroup);
        //Getting the color that the user currently has set
        String presetColor = MainScreenActivity.mainUser.getColor().substring(0, 1).toUpperCase() +
                MainScreenActivity.mainUser.getColor().substring(1);

        //Setting the name of the id's string and capitalizing the first letter of the color
        String radioColor = "radio" + presetColor;

        //Setting our preset color to be checked when the user opens the dialog
        group.check(getResources().getIdentifier(radioColor, "id", getPackageName()));
        //group.check(R.id.radioYellow);

        //Referencing the confirm button and assigning it a function when clicked
        final Button confirmationBtn = customView.findViewById(R.id.confirm);
        int confirmID = getResources().getIdentifier(presetColor, "color", getPackageName());

        //Setting the color of the confirmation button when the dialog loads
        confirmationBtn.setBackgroundTintList(getColorStateList(confirmID));

        //Changing the color of the confirmation button whenever a radio button is checked
        group.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener()
        {
            @Override
            public void onCheckedChanged(RadioGroup group, int checkedId) {
                RadioButton userChoice = group.findViewById(checkedId);
                confirmationBtn.setBackgroundTintList(userChoice.getButtonTintList());
            }
        });

        //Detecting when the confirmation button is finally clicked and saving the color
        confirmationBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //Getting the checked radioButton
                int check = group.getCheckedRadioButtonId();
                RadioButton userChoice = group.findViewById(check);

                //Converting the text of the checked radioButton
                String choice = userChoice.getText().toString().toLowerCase();

                //Setting the value of the fragments' background color
                MainScreenActivity.mainUser.setColor(choice);
                Log.i("CONFIRM", "CLICKED");
                Log.i("SELECTED BUTTON", choice);
                dialog.dismiss();
            }
        });


        return dialog;
    }


}
