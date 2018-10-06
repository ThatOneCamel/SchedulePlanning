package efx.com.GroupLink;

import android.content.Context;
import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.View;

import java.util.ArrayList;

public class MainScreenActivity extends AppCompatActivity {

    RecyclerView mainRecyclerView;
    RecycleViewAdapter mainAdapter;
    ArrayList<String> randomH;
    ArrayList<String> randomE;
    ArrayList<String> randomT;
    ArrayList<String> randomD;

    int count = 0;
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
        Log.i("function" , "AM I RUNNING?");
        String title = data.getStringExtra("title");
        String description = data.getStringExtra("description");
        String date = data.getStringExtra("date");
        String start = data.getStringExtra("start");
        String end = data.getStringExtra("end");
        createData(title, description, date, start, end);

        mainAdapter.notifyDataSetChanged();
    }

    //This is just a test function to create random data to test our recycled views
    private void createData(String title, String description, String date, String start, String end){
        randomH.add(start);
        randomE.add(title);
        randomT.add(start + "-" + end);
        randomD.add(description);

        initRecycler(randomH, randomE, randomT, randomD);
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

    public void addNewItem(String title,String description, String date, String sTime, String eTime, boolean pEvent) {

    }
}
