package efx.com.GroupLink;

import android.content.Intent;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.View;
import android.widget.TextView;

import java.util.Random;

public class MainScreenActivity extends AppCompatActivity {

    RecyclerView mainRecyclerView;
    RecycleViewAdapter mainAdapter;
    UserInfo mainUser;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main_screen);

        //Initializing the UserInfo class
        mainUser = new UserInfo();
        initRecycler();
        //createDemoData(mainUser);

    }


    //Called when this activity receives the result code of an activity
    @Override
    protected void onActivityResult (int requestCode, int resultCode, Intent data){
        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode == RESULT_CANCELED){
            Log.i("Cancelled EventData", "Nothing new was added");

        } else {
            /*String title = data.getStringExtra("title");
            String description = data.getStringExtra("description");
            String date = data.getStringExtra("date");*/
            String start = data.getStringExtra("start");
            String end = data.getStringExtra("end");
            //createData(title, description, date, start, end);

            String time = start + " - " + end;

            /*mainUser.addEventName(data.getStringExtra("title"));
            mainUser.addEventDesc(data.getStringExtra("description"));
            mainUser.addEventDate(data.getStringExtra("date"));
            mainUser.addEventFlavor(data.getStringExtra("start"));
            mainUser.addEventTime(time);*/
            //mainUser.addNumberOfEvents(1);
            //Title->Date->Time->Description->FlavorText
            mainUser.addEvent(
                    data.getStringExtra("title"),
                    data.getStringExtra("date"),
                    time,
                    data.getStringExtra("description"),
                    data.getStringExtra("start")
            );
            TextView emptyPlannerTxt = findViewById(R.id.mainEmptyPlannerTxt);
            emptyPlannerTxt.setVisibility(View.INVISIBLE);
            if (mainUser.size() > 1) {
                //sortingFragments();
            }
            //mainAdapter.setEqual(mainUser);

            if(mainUser.size() > 1){
                //mainUser.sort();
            }

            mainAdapter.notifyDataSetChanged();
        }
    }

    /*private void createDemoData(UserInfo user){

        findViewById(R.id.mainEmptyPlannerTxt).setVisibility(View.INVISIBLE);

        Random rand = new Random();

        int month, day, year, start, end;


        for (int i = 0; i < 10; i++){
            //Random Month 1-12
            month = rand.nextInt(9) + 1;

            //Random Day 1-25
            day = rand.nextInt(21) + 10;

            //Random Year from 2000 - 2018
            year = rand.nextInt(18) + 2000;

            //Random Times from 1-12
            start = rand.nextInt(12) + 1;
            end = rand.nextInt(12) + 1;


            String date = String.format("%02d/%02d/%d", month, day, year);
            String date2 = "0" + month + "/" + day + "/" + year;

            mainUser.addNewEvent(
                    "Event #" + i,
                    date2,
                    start +":00 AM - " + end +":00 PM",
                    "A generic event",
                    start + "AM",
                    false);
            sortingFragments();

        }
        //sortingFragments();
        mainAdapter.notifyDataSetChanged();

    }*/

    //This will initialize our custom recyclerView by telling it which RecyclerView to reference [The one in Main Activity]
    private void initRecycler(){
        //References the RecyclerView in the MainActivity
        mainRecyclerView = findViewById(R.id.mainRecycler);

        //Creates a new class object from our custom RecycleViewAdapter.Java class
        //This is calling the constructor
        //mainAdapter = new RecycleViewAdapter(hour, eventName, time, description,this);
        mainAdapter = new RecycleViewAdapter(mainUser, this);

        //Connects our recycler and our adapter
        mainRecyclerView.setAdapter(mainAdapter);

        //This will order the items correctly in a linear fashion
        mainRecyclerView.setLayoutManager(new LinearLayoutManager(this));

        //Creating a reference to our floating action button and creating an OnScrollListener
         //Whenever a user scrolls down the fab will be hidden, if they scroll up or are near the top
          // the fab will be shown
        final FloatingActionButton fab = findViewById(R.id.mainAddBtn);
        mainRecyclerView.addOnScrollListener(new RecyclerView.OnScrollListener() {
            @Override
            public void onScrolled(RecyclerView recyclerView, int dx, int dy) {
                super.onScrolled(recyclerView, dx, dy);

                if (dy > 0)
                    fab.hide();
                else if (dy < 0)
                    fab.show();
            }
        });

        //DEBUG Statement: Called to ensure that the recycler was created without any fatal errors
        Log.i("init called:", "Recycler created successfully");
    }//End initRecycler

    public void openActivity(View v){
        Intent intent = new Intent(MainScreenActivity.this, EventData.class);
        startActivityForResult(intent, 123);
        //Log.i("Event1:", mainUser.getEventName(0));
    }

    /*public void sortingFragments() {
        int arrLength = mainUser.size();

        for (int i = 0; i < arrLength - 1; i++)
        {
            int min_index = i;
            for (int j = i+1; j < arrLength; j++) {
                //If Date i is greater than Date j, returns a positive number
                if (mainUser.getEventDate(i).compareTo(mainUser.getEventDate(j)) > 0) {
                    //If number is positive, make minimum index = j (the smaller date)
                    min_index = j;
                }
            }
            //Make temporary storage for original data
            String tempEventName = mainUser.getEventName(min_index);
            String tempEventDate = mainUser.getEventDate(min_index);
            String tempEventTime = mainUser.getEventTime(min_index);
            String tempEventDesc = mainUser.getEventDesc(min_index);
            String tempEventFlavor = mainUser.getEventFlavor(min_index);

            mainUser.setEventName(min_index, mainUser.getEventName(i));
            mainUser.setEventDate(min_index, mainUser.getEventDate(i));
            mainUser.setEventTime(min_index, mainUser.getEventTime(i));
            mainUser.setEventDesc(min_index, mainUser.getEventDesc(i));
            mainUser.setEventFlavor(min_index, mainUser.getEventFlavor(i));

            mainUser.setEventName(i, tempEventName);
            mainUser.setEventDate(i, tempEventDate);
            mainUser.setEventTime(i, tempEventTime);
            mainUser.setEventDesc(i, tempEventDesc);
            mainUser.setEventFlavor(i, tempEventFlavor);
        }
    }*/
}
