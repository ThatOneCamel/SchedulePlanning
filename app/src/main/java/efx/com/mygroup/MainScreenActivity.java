package efx.com.mygroup;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;

import java.util.ArrayList;

public class MainScreenActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main_screen);

        createTestData();

    }

    //This is just a test function to create random data to test our recycled views
    private void createTestData(){
        ArrayList<String> randomH = new ArrayList<>();
        ArrayList<String> randomT = new ArrayList<>();

        for(int i = 0; i < 7; i++){
            randomH.add("A" + i);
            randomT.add("Z" + i);
        }

        initRecycler(randomH, randomT);

    }

    //This will initialize our custom recyclerView by telling it which RecyclerView to reference [The one in Main Activity]
    private void initRecycler(ArrayList<String> hour, ArrayList<String> time){
        //References the RecyclerView in the MainActivity
        RecyclerView mainRecyclerView = findViewById(R.id.mainRecycler);

        //Creates a new class object from our custom RecycleViewAdapter.Java class
        //This is calling the constructor
        RecycleViewAdapter mainAdapter = new RecycleViewAdapter(hour, time, this);

        //Connects our recycler and our adapter
        mainRecyclerView.setAdapter(mainAdapter);

        //This will order the items correctly in a linear fashion
        mainRecyclerView.setLayoutManager(new LinearLayoutManager(this));

        //DEBUG Statement: Called to ensure that the recycler was created without any fatal errors
        Log.i("init called:", "Recycler created successfully");

    }
}
