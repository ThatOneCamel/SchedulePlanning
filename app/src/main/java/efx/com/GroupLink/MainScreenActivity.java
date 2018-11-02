package efx.com.GroupLink;

import android.content.Intent;

import com.firebase.ui.auth.data.model.User;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import androidx.appcompat.app.AppCompatActivity;
import android.os.Bundle;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import android.util.Log;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

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

        //Creating a connection to to the Firebase database
        database = FirebaseDatabase.getInstance();

        //Referencing root -> users -> UID [This is where user's data will be written]
        databaseRef = database.getReference().child("users").child(FirebaseAuth.getInstance().getUid());
        databaseRef.keepSynced(true);
        mainUser = new UserInfo();

        //Getting any settings that
        loadLocalData();
        retrieveEvents();
        //initRecycler();
        //createDemoData(mainUser);

    }

    void retrieveEvents(){

        databaseRef.child("events").addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {


                try {
                    //A For-Each loop that finds every child [which is an event in this case)
                    for (DataSnapshot snap : dataSnapshot.getChildren()) {

                        //PushFire is the class we used to push data to the database
                        //So now it needs to be what we use to read and retrieve that data
                        PushFire temp = snap.getValue(PushFire.class);

                        //Importing each event into our mainUser
                        mainUser.importEvent(temp);
                        mainUser.addKey(mainUser.size(), snap.getKey());
                        mainUser.sort();
                        Log.i("EVENT_KEY_RECIEVED", snap.getKey());

                    }
                    mainUser.setUsername(dataSnapshot.getValue().toString());
                    Log.i("USERNAME RECIEVED", mainUser.getName());

                } catch (NullPointerException e) {
                    Log.i("NO DATA FOUND", "User has no firebase events");
                }
                initRecycler();
                setPlannerText();
                mainAdapter.notifyDataSetChanged();

            }

            @Override
            public void onCancelled(DatabaseError databaseError) {
                Toast.makeText(MainScreenActivity.this,
                        "Could not connect to Firebase; showing local backup", Toast.LENGTH_SHORT).show();
                Log.i("ERROR_DATABASE", databaseError.getMessage());
                //If a connection to the database could not be made or if a network error occurs
                 //We load whatever data the user had previously
            }
        });

        Toast.makeText(this, "EVENT DATA RECIEVED", Toast.LENGTH_SHORT).show();
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

    void editDatabase(int pos){
        PushFire object = new PushFire(mainUser.getEvent(pos));
        //databaseRef.child("events").push().setValue(object);

        databaseRef.child("events").child(mainUser.getEventPostID(pos)).setValue(object);
        //mainUser.setDatabaseKey();
        Log.i("VALUE EDITED", "Something was pushed to Firebase");
    }

    void loadLocalData(){
        try {
            //Finding and opening the user.dat file, then assigning it to an InputStream
            FileInputStream file = this.openFileInput("user.dat");
            ObjectInputStream objectIn = new ObjectInputStream(file);

            //Assigning the data that was saved in our file to a temporary object
             //This is because we do not want to overwrite local changes when there are potentially
              // newer changes server-side
            UserInfo fileObject = (UserInfo) objectIn.readObject();

            mainUser.setColor(fileObject.getColor());

            //Closing the InputStream and the file
            objectIn.close();
            file.close();

            Log.i("FILE", "DATA_LOADED_SUCCESSFULLY");
            Log.i("COLORSETDEFAULT", mainUser.getColor());

        }catch(IOException error){
            //File not found
            Log.e("INPUT_EXCEPTION", error.getLocalizedMessage());

        }catch(ClassNotFoundException error){
            //Error with the class object that was saved
            Log.e("CLASS_EXCEPTION", error.getLocalizedMessage());

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
                editDatabase(position);
                Log.i("EventChange:", "Event EDITED");

            } else {
                mainUser.addEvent(
                        data.getStringExtra("title"),
                        data.getStringExtra("date"),
                        time,
                        data.getStringExtra("description"),
                        data.getStringExtra("start"));
                Log.i("EventChange:", "Event CREATED");
                pushToDatabase();
                mainUser.sort();

            } //End If-Else

            setPlannerText();
            mainAdapter.notifyDataSetChanged();
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
        mainAdapter = new RecycleViewAdapter(mainUser, this);

        //Connects our recycler and our adapter
        mainRecyclerView.setAdapter(mainAdapter);

        //This will order the items correctly in a linear fashion
        mainRecyclerView.setLayoutManager(new LinearLayoutManager(this));

        //Creating a reference to our floating action button and creating an OnScrollListener
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
    }

    public void openSettings(View v){
        Intent intent = new Intent(MainScreenActivity.this, SettingsActivity.class);
        startActivityForResult(intent, 155);
    }

}
