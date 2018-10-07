package efx.com.GroupLink;

import android.content.Context;
import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.View;
import android.widget.TextView;

import org.w3c.dom.Text;

import java.util.ArrayList;

public class MainScreenActivity extends AppCompatActivity {

    RecyclerView mainRecyclerView;
    RecycleViewAdapter mainAdapter;
    ArrayList<String> randomH;
    ArrayList<String> randomE;
    ArrayList<String> randomT;
    ArrayList<String> randomD;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main_screen);
        randomH = new ArrayList<>();
        randomE = new ArrayList<>();
        randomT = new ArrayList<>();
        randomD = new ArrayList<>();
        //createData("EventName","Description", "06/10/2018", "STARTTIME", "ENDTIME");
    }

    @Override
    protected void onActivityResult (int requestCode, int resultCode, Intent data){
        super.onActivityResult(requestCode, resultCode, data);
        String title = data.getStringExtra("title");
        String description = data.getStringExtra("description");
        String date = data.getStringExtra("date");
        String start = data.getStringExtra("start");
        String sAMPM = data.getStringExtra("sAMPM");
        String end = data.getStringExtra("end");
        String eAMPM = data.getStringExtra( "eAMPM");
        createData(title, description, date, start, sAMPM, end, eAMPM);
        mainAdapter.notifyDataSetChanged();
    }

    //This is just a test function to create random data to test our recycled views
    private void createData(String title, String description, String date, String start, String sAMPM, String end, String eAMPM) {
        TextView mainEmptyPlannerTxt = (TextView)findViewById(R.id.mainEmptyPlannerTxt);
        //Toggle
        if (mainEmptyPlannerTxt.getVisibility() == View.VISIBLE) {
            mainEmptyPlannerTxt.setVisibility(View.INVISIBLE);
        }
        if (title != null && !title.isEmpty() && description != null && !description.isEmpty() && date != null
                && !date.isEmpty() && start != null && !start.isEmpty() && sAMPM != null && !sAMPM.isEmpty() &&
                end != null && !end.isEmpty() && eAMPM != null && !eAMPM.isEmpty()) {
                randomH.add(start + sAMPM);
                randomE.add(title);
                randomT.add(start + sAMPM + "-" + end + eAMPM);
                randomD.add(description);
                initRecycler(randomH, randomE, randomT, randomD);
         }
    }

    //This will initialize our custom recyclerView by telling it which RecyclerView to reference [The one in Main Activity]
    private void initRecycler(ArrayList<String> hour, ArrayList<String> eventName, ArrayList<String> time, ArrayList<String> description){
        //References the RecyclerView in the MainActivity
        mainRecyclerView = findViewById(R.id.mainRecycler);

        //Creates a new class object from our custom RecycleViewAdapter.Java class
        //This is calling the constructor
        mainAdapter = new RecycleViewAdapter(hour, eventName, time, description,this);

        //Connects our recycler and our adapter
        mainRecyclerView.setAdapter(mainAdapter);

        //This will order the items correctly in a linear fashion
        mainRecyclerView.setLayoutManager(new LinearLayoutManager(this));

        //DEBUG Statement: Called to ensure that the recycler was created without any fatal errors
        Log.i("init called:", "Recycler created successfully");
    }

    public void openActivity(View v){
        Intent intent = new Intent(MainScreenActivity.this, EventData.class);
        startActivityForResult(intent, 123);
    }
}
