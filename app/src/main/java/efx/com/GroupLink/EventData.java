package efx.com.GroupLink;

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

    private String title, description, date, start, end, sAMPM, eAMPM;
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

    String assignStringData(int pos){
        String inputString = input[pos].getText().toString();
        Log.i("Value:",inputString);
        return inputString;
    }

    public void saveData(View v){
        title = assignStringData(0);
        description= assignStringData(1);
        date = assignStringData(2);
        start = assignStringData(3);
        sAMPM = assignStringData(4);
        end = assignStringData(5);
        eAMPM = assignStringData(6);

        //if any textview box in event data is empty... present alert message... otherwise,
        //make a fragment with the data
        if (title != null && !title.isEmpty() && description != null && !description.isEmpty() && date != null
                && !date.isEmpty() && start != null && !start.isEmpty() && sAMPM != null && !sAMPM.isEmpty() &&
                end != null && !end.isEmpty() && eAMPM != null && !eAMPM.isEmpty()) {
            Intent intent = new Intent(EventData.this, MainScreenActivity.class);
            intent.putExtra("title", title);
            intent.putExtra("description", description);
            intent.putExtra("date", date);
            intent.putExtra("start", start);
            intent.putExtra("sAMPM", sAMPM);
            intent.putExtra("end", end);
            intent.putExtra("eAMPM", eAMPM);
            setResult(123, intent);
            finish();
        }

        alertTextView = (TextView) findViewById(R.id.AlertTextView);
        AlertDialog.Builder builder = new AlertDialog.Builder(EventData.this);
        builder.setCancelable(true);
        builder.setMessage("You must fill every text slot.");
        builder.setNegativeButton("OK", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i){
                dialogInterface.cancel();
            }
        });
        builder.setPositiveButton("Cancel", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i){
                dialogInterface.cancel();
            }
        });

        if (title != null && !title.isEmpty() && description != null && !description.isEmpty() && date != null
                && !date.isEmpty() && start != null && !start.isEmpty() && sAMPM != null && !sAMPM.isEmpty() &&
                end != null && !end.isEmpty() && eAMPM != null && !eAMPM.isEmpty()){
            AlertDialog alertDialog = builder.create();
            alertDialog.dismiss();
        }
        else {
            AlertDialog alertDialog = builder.create();
            alertDialog.show();
        }
    }
}