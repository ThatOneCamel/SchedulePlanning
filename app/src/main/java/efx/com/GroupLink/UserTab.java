package efx.com.GroupLink;


import android.app.AlertDialog;
import android.app.Dialog;
import android.content.Intent;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.android.material.tabs.TabLayout;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.io.FileInputStream;
import java.io.IOException;
import java.io.ObjectInputStream;

import static android.app.Activity.RESULT_CANCELED;
import static efx.com.GroupLink.MainScreenActivity.mTabLayout;
import static efx.com.GroupLink.MainScreenActivity.myPagerAdapter;

/**
 * A simple {@link Fragment} subclass.
 */
public class UserTab extends Fragment {


    public UserTab() {
        // Required empty public constructor
    }

    private View fragView;

    private RecycleViewAdapter mainAdapter;
    static UserInfo mainUser;

    private DatabaseReference databaseRef;
    private boolean shown;

    private void retrieveEvents(){

        databaseRef.child("events").addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {


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
                    mainUser.setUsername(databaseRef.child("username").getKey());
                    Log.i("USERNAME RECEIVED", mainUser.getName());

                } catch (NullPointerException e) {
                    Log.i("NO DATA FOUND", "User has no FireBase events");
                }
                initRecycler();
                setPlannerText();
                mainAdapter.notifyDataSetChanged();

            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {
                Toast.makeText(getActivity(),
                        "Could not connect to FireBase; showing local backup", Toast.LENGTH_SHORT).show();
                Log.i("ERROR_DATABASE", databaseError.getMessage());
                //If a connection to the database could not be made or if a network error occurs
                //We load whatever data the user had previously
            }
        });

        Toast.makeText(getActivity(), "EVENT DATA RECIEVED", Toast.LENGTH_SHORT).show();

    }



    private void pushToDatabase(){
        int n = mainUser.size();
        PushFire object = new PushFire(mainUser.getEvent(n -1));
        //databaseRef.child("events").push().setValue(object);

        String key = databaseRef.child("events").push().getKey();
        mainUser.addKey(n, key);
        databaseRef.child("events").child(key).setValue(object);
        //mainUser.setDatabaseKey();
        Log.i("VALUE SET", "Something was pushed to FireBase");
    }

    private void editDatabase(int pos){
        PushFire object = new PushFire(mainUser.getEvent(pos));
        //databaseRef.child("events").push().setValue(object);

        databaseRef.child("events").child(mainUser.getEventPostID(pos)).setValue(object);
        //mainUser.setDatabaseKey();
        Log.i("VALUE EDITED", "Something was pushed to FireBase");
    }

    private void loadLocalData(){
        try {
            //Finding and opening the user.dat file, then assigning it to an InputStream
            FileInputStream file = getContext().openFileInput("user.dat");
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
            Log.i("COLOR_SET_DEFAULT", mainUser.getColor());

        }catch(IOException error){
            //File not found
            Log.e("INPUT_EXCEPTION", error.getLocalizedMessage());

        }catch(ClassNotFoundException error){
            //Error with the class object that was saved
            Log.e("CLASS_EXCEPTION", error.getLocalizedMessage());

        }catch (NullPointerException error){
            Log.e("NULL_EXCEPTION", error.getLocalizedMessage());
        }

    }

    //Called when this activity receives the result code of an activity
    @Override
    public void onActivityResult (int requestCode, int resultCode, Intent data){
        super.onActivityResult(requestCode, resultCode, data);

        //If a user presses back
        if (resultCode == RESULT_CANCELED){
            Log.i("Cancelled EventData", "Nothing new was added");

            //Saving and updating data in the event that a user changed settings
            mainAdapter.notifyDataSetChanged();
            mainUser.saveLocalData(getContext());

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
            mainUser.saveLocalData(getContext());
        }
    }

    private void setPlannerText(){
        TextView emptyPlannerTxt = fragView.findViewById(R.id.mainEmptyPlannerTxt);

        if (mainUser.size() > 0){
            emptyPlannerTxt.setVisibility(View.INVISIBLE);
        } else {
            emptyPlannerTxt.setVisibility(View.VISIBLE);
        }

    }

    //This will initialize our custom recyclerView by telling it which RecyclerView to reference [The one in Main Activity]
    private void initRecycler(){
        //References the RecyclerView in the MainActivity
        RecyclerView mainRecyclerView = fragView.findViewById(R.id.mainRecycler);

        //Creates a new class object from our custom RecycleViewAdapter.Java class
        //This is calling the constructor
        mainAdapter = new RecycleViewAdapter(mainUser, getActivity(), this);

        //Connects our recycler and our adapter
        mainRecyclerView.setAdapter(mainAdapter);

        //This will order the items correctly in a linear fashion
        mainRecyclerView.setLayoutManager(new LinearLayoutManager(getActivity()));

        initFloatingButtons(mainRecyclerView);


        //DEBUG Statement: Called to ensure that the recycler was created without any fatal errors
        Log.i("init called:", "Recycler created successfully");

    }//End initRecycler

    //Setting up the OnClickListeners of the floating action buttons
    //Also attaching an OnScrollListener to the RecyclerView
    private void initFloatingButtons(RecyclerView mRecyclerView){

        final FloatingActionButton fab = fragView.findViewById(R.id.mainAddBtn);
        final FloatingActionButton eventFab = fragView.findViewById(R.id.dialAddEvent);
        final FloatingActionButton groupFab = fragView.findViewById(R.id.dialAddGroup);
        eventFab.hide();
        groupFab.hide();
        shown = false;

        mRecyclerView.addOnScrollListener(new RecyclerView.OnScrollListener() {
            @Override
            public void onScrolled(@NonNull RecyclerView recyclerView, int dx, int dy) {
                super.onScrolled(recyclerView, dx, dy);

                if (dy > 0)
                    fab.hide();
                else if (dy < 0)
                    fab.show();
            }
        });


        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                if (shown){
                    fab.animate().rotation(0f);
                    eventFab.hide();
                    groupFab.hide();
                    shown = !shown;
                } else {
                    fab.animate().rotation(45f);
                    eventFab.show();
                    groupFab.show();
                    shown = !shown;
                }
            }
        });

        eventFab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                openActivity();
            }
        });

        groupFab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Toast.makeText(getActivity(), "Group Create CLICKED", Toast.LENGTH_LONG).show();
                //mTabLayout.newTab();
                enterGroupName().show();
                //mTabLayout.addTab(mTabLayout.newTab().setText("Group"));
                if (myPagerAdapter.getCount() > 3){
                    mTabLayout.setTabMode(TabLayout.MODE_SCROLLABLE);
                    mTabLayout.setPaddingRelative(80, 0, 0, 0);
                }

            }
        });
    }

    private void openActivity(){
        Intent intent = new Intent(getActivity(), EventData.class);
        startActivityForResult(intent, 123);
    }

    private void startSettingsActivity(){
        Intent intent = new Intent(getActivity(), SettingsActivity.class);
        startActivityForResult(intent, 155);
    }

    private Dialog enterGroupName(){
        final AlertDialog mDialog = new AlertDialog.Builder(getActivity()).create();
        View customView = View.inflate(getActivity(), R.layout.display_name_input, null);
        mDialog.setView(customView);

        TextView displayText = customView.findViewById(R.id.displayText);
        displayText.setText("Please input your group's name");
        final EditText nameField = customView.findViewById(R.id.editDisplayName);
        nameField.setHint("Group Name");

        Button myBtn = customView.findViewById(R.id.displayConfirmBtn);
        myBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(nameField.getText().toString().isEmpty()){
                    Toast.makeText(getActivity(), "Please enter a name for your group.", Toast.LENGTH_LONG).show();
                } else {
                    //Creates new group tab
                    String groupName = nameField.getText().toString();
                    mTabLayout.addTab(mTabLayout.newTab());
                    myPagerAdapter.addTabItem(groupName);
                    mDialog.dismiss();
                }

            }
        });


        return mDialog;
    }



    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        fragView =  inflater.inflate(R.layout.fragment_user_tab, container, false);

        ImageButton settingsBtn = container.getRootView().findViewById(R.id.settingsBtn);
        settingsBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startSettingsActivity();
            }
        });

        //Creating a connection to to the FireBase database
        FirebaseDatabase database = FirebaseDatabase.getInstance();

        //Referencing root -> users -> UID [This is where user's data will be written]
        databaseRef = database.getReference().child("users").child(FirebaseAuth.getInstance().getUid());
        databaseRef.keepSynced(true);

        mainUser = new UserInfo();
        loadLocalData();
        retrieveEvents();

        return fragView;
    }


}
