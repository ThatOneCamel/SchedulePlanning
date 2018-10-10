package efx.com.GroupLink;

import android.app.Dialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.util.EventLog;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;

public class EventData extends AppCompatActivity {

    private EditText[] input;
    private TextView alertTextView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_event_data);
        Toolbar topToolBar = findViewById(R.id.toolbar);
        setSupportActionBar(topToolBar);
        ActionBar actionBar = getSupportActionBar();
        actionBar.setDisplayHomeAsUpEnabled(true);

        input = new EditText[]{
                findViewById(R.id.eventTitle),
                findViewById(R.id.fragDesc),
                findViewById(R.id.eventDate),
                findViewById(R.id.eventStart),
                findViewById(R.id.startAMPM),
                findViewById(R.id.eventEnd),
                findViewById(R.id.endAMPM)
        };

    }

    public Dialog createDialog(){
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setMessage("You must fill every text slot");
        builder.setPositiveButton("Close", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                emptyField = false;
                dialog.dismiss();
            }
        });

        return builder.create();

    }

    boolean emptyField = false;
    String assignStringData(int pos){
        if (input[pos].getText().toString().isEmpty()){
            emptyField = true;
            return null;

        } else {
            String inputString = input[pos].getText().toString();
            Log.i("Value:", inputString);
            return inputString;
        }

    }

    public void saveData(View v){
        //String title, description, date, start, end, sAMPM, eAMPM;

        /*title = assignStringData(0);
        description= assignStringData(1);
        date = assignStringData(2);
        start = assignStringData(3);
        sAMPM = assignStringData(4);
        end = assignStringData(5);
        eAMPM = assignStringData(6);*/


        //If any of these fields are empty, emptyField will be set to true, causing an error dialog
        String arr[] = new String[7];

        for (int i = 0; i < 7; i++){
            arr[i] = assignStringData(i);
        }

        //If any of the fields were empty, show  error message
        if (emptyField){
            //When error message is closed, emptyField is set to false again
            createDialog().show();


        //Otherwise, put all of the userInput into an intent bundle to be transferred to the MainActivity
        } else {
            Intent mIntent = new Intent(EventData.this, MainScreenActivity.class);
            mIntent.putExtra("title", arr[0]);
            mIntent.putExtra("description", arr[1]);
            mIntent.putExtra("date", arr[2]);
            mIntent.putExtra("start", arr[3]);
            mIntent.putExtra("sAMPM", arr[4]);
            mIntent.putExtra("end", arr[5]);
            mIntent.putExtra("eAMPM", arr[6]);
            setResult(123, mIntent);
            finish();

        }

    }
}