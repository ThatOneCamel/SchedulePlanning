package efx.com.GroupLink;

import android.content.Intent;

import com.firebase.ui.auth.data.model.User;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import androidx.appcompat.app.AppCompatActivity;
import android.os.Bundle;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import android.util.Log;
import android.view.View;
import android.widget.TextView;

import java.io.FileInputStream;
import java.io.IOException;
import java.io.ObjectInputStream;


public class MainScreenActivity extends AppCompatActivity {

    RecyclerView mainRecyclerView;
    RecycleViewAdapter mainAdapter;
    static UserInfo mainUser;

    FirebaseDatabase database;
    DatabaseReference databaseRef;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main_screen);

        database = FirebaseDatabase.getInstance();
        databaseRef = database.getReference().child("users").child(FirebaseAuth.getInstance().getUid());

        //Initializing the UserInfo class
        //mainUser = new UserInfo();
        //mainUser.setDatabaseKey(databaseRef.push().getKey());
        loadLocalData();
        //mainUser.setDatabaseKey(databaseRef.push().getKey());

        initRecycler();
        //createDemoData(mainUser);

    }

    void pushToDatabase(){
        int n = mainUser.size();
        PushFire object = new PushFire(mainUser.getEvent(n -1));
        //databaseRef.child("events").push().setValue(object);

        String key = databaseRef.child("events").push().getKey();
        mainUser.addKey(n, key);
        databaseRef.child("events").child(key).setValue(object);
        //mainUser.setDatabaseKey();
        Log.i("VALUE SET", "Something was pushed to Firebase");
    }

    void loadLocalData(){
        try
        {
            FileInputStream file = this.openFileInput("user.dat");
            ObjectInputStream objectIn = new ObjectInputStream(file);
            mainUser =  (UserInfo) objectIn.readObject();
            setPlannerText();
            objectIn.close();
            file.close();
            Log.i("FILE", "DATA_LOADED_SUCCESSFULLY");
            mainUser.setDatabaseKey(databaseRef.push().getKey());
            Log.i("COLORSETDEFAULT", mainUser.getColor());


        }catch(IOException error){
            Log.e("INPUT_EXCEPTION", error.getLocalizedMessage());
            mainUser = new UserInfo();
            mainUser.setDatabaseKey(databaseRef.push().getKey());

        }catch(ClassNotFoundException error){
            Log.e("CLASS_EXCEPTION", error.getLocalizedMessage());
            mainUser = new UserInfo();
            mainUser.setDatabaseKey(databaseRef.push().getKey());

        }

    }

    //Called when this activity receives the result code of an activity
    @Override
    protected void onActivityResult (int requestCode, int resultCode, Intent data){
        super.onActivityResult(requestCode, resultCode, data);

        //If a user presses back
        if (resultCode == RESULT_CANCELED){
            Log.i("Cancelled EventData", "Nothing new was added");

            //Saving and updating data in the event that a user changed settings
            mainAdapter.notifyDataSetChanged();
            mainUser.saveLocalData(this);

        } else if (resultCode == 111 && data != null){
            int position = data.getIntExtra("pos", -1);
            mainUser.deleteEvent(position);
            mainAdapter.notifyItemRemoved(position);
            setPlannerText();
           //databaseRef.child("events").removeValue()

        } else {

            String start = data.getStringExtra("start");
            String end = data.getStringExtra("end");
            String time = start + " -" + end;

            //Title->Date->Time->Description->FlavorText


            int position;

            if((position = data.getIntExtra("pos", -1)) != -1){

                mainUser.editEvent(
                        data.getStringExtra("title"),
                        data.getStringExtra("date"),
                        time,
                        data.getStringExtra("description"),
                        data.getStringExtra("start"),
                        position);
                Log.i("EventChange:", "Event EDITED");

            } else {
                mainUser.addEvent(
                        data.getStringExtra("title"),
                        data.getStringExtra("date"),
                        time,
                        data.getStringExtra("description"),
                        data.getStringExtra("start"));
                Log.i("EventChange:", "Event CREATED");

            } //End If-Else

            setPlannerText();
            mainAdapter.notifyDataSetChanged();
            pushToDatabase();
            mainUser.saveLocalData(this);
        }
    }

    void setPlannerText(){
        TextView emptyPlannerTxt = findViewById(R.id.mainEmptyPlannerTxt);

        if (mainUser.size() > 0){
            emptyPlannerTxt.setVisibility(View.INVISIBLE);
        } else {
            emptyPlannerTxt.setVisibility(View.VISIBLE);
        }
    }

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

    public void openSettings(View v){
        Intent intent = new Intent(MainScreenActivity.this, SettingsActivity.class);
        startActivityForResult(intent, 155);
    }

    public void logOut(View logOut) {
        FirebaseAuth.getInstance().signOut();

    }


}
