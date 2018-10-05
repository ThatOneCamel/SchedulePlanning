package efx.com.mygroup;

import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;

import java.util.List;

public class EventData extends AppCompatActivity {

    private String title, description, date, start, end;
    private EditText[] input;

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
                findViewById(R.id.eventDesc),
                findViewById(R.id.eventDate),
                findViewById(R.id.eventStart),
                findViewById(R.id.eventEnd)
        };

    }

    String assignStringData(String inputString, int pos){
        inputString = input[pos].getText().toString();
        Log.i("Value:",inputString);
        return inputString;
    }

    public void saveData(View v){
        title = assignStringData(title, 0);
        description= assignStringData(description, 1);
        date = assignStringData(date, 2);
        start = assignStringData(start, 3);
        end = assignStringData(end, 4);
    }
}
