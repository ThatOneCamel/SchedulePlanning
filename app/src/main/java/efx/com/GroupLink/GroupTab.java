package efx.com.GroupLink;


import android.content.Intent;
import android.os.Bundle;

import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import static android.app.Activity.RESULT_CANCELED;
import static efx.com.GroupLink.UserTab.mainUser;


/**
 * A simple {@link Fragment} subclass.
 */
public class GroupTab extends Fragment {

    GroupInfo myGroup;

    View mView;


    public GroupTab() {
        // Required empty public constructor
    }
    private RecyclerView groupRecyclerView;
    private GroupRecycleViewAdapter groupAdapter;


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        mView =  inflater.inflate(R.layout.fragment_group_tab, container, false);

        // Inflate the layout for this fragment
        FloatingActionButton fab = mView.findViewById(R.id.groupAddBtn);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getActivity(), EventData.class);
                startActivityForResult(intent, 123);
                Toast.makeText(getContext(), "TOAST", Toast.LENGTH_SHORT).show();

            }
        });

        myGroup = new GroupInfo();
        myGroup.setColor(mainUser.getColor());
        initGroupRecycler();

        return mView;
    }

    void setPlannerText(){
        TextView emptyPlannerTxt = mView.findViewById(R.id.mainEmptyPlannerTxt);

        if (myGroup.groupEventSize() > 0){
            emptyPlannerTxt.setVisibility(View.INVISIBLE);
        } else {
            emptyPlannerTxt.setVisibility(View.VISIBLE);
        }
    }

    void editDatabase(int pos){
        DatabaseReference mRef = myGroup.getDatabaseRef();
        PushFire object = new PushFire(myGroup.getEvent(pos));

        mRef.child("gEvents").child(myGroup.getGroupEventPostID(pos)).setValue(object);
        Log.i("VALUE EDITED", "Something was pushed to Firebase");
    }

    @Override
    public void onActivityResult (int requestCode, int resultCode, Intent data){
        super.onActivityResult(requestCode, resultCode, data);

        Log.i("RESULT:", "USERTAB");

        //If a user presses back
        if (resultCode == RESULT_CANCELED){
            Log.i("Cancelled EventData", "Nothing new was added");

            //Saving and updating data in the event that a user changed settings
            groupAdapter.notifyDataSetChanged();
            //myGroup.saveLocalData(getContext());

        } else if (resultCode == 111 && data != null){
            int position = data.getIntExtra("pos", -1);
            myGroup.deleteGroupEvent(position);
            groupAdapter.notifyItemRemoved(position);
            setPlannerText();

        } else {

            String start = data.getStringExtra("start");
            String end = data.getStringExtra("end");
            String time = start + " -" + end;

            //Title->Date->Time->Description->FlavorText


            int position;

            if((position = data.getIntExtra("pos", -1)) != -1){

                myGroup.editGroupEvent(
                        data.getStringExtra("title"),
                        data.getStringExtra("date"),
                        time,
                        data.getStringExtra("description"),
                        data.getStringExtra("start"),
                        position);
                editDatabase(position);
                Log.i("EventChange:", "Event EDITED");

            } else {
                myGroup.addGroupEvent(
                        data.getStringExtra("title"),
                        data.getStringExtra("date"),
                        time,
                        data.getStringExtra("description"),
                        data.getStringExtra("start"));
                Log.i("EventChange:", "Event CREATED");
                myGroup.pushGroupToDatabase();
                myGroup.sort();

            } //End If-Else

            setPlannerText();
            groupAdapter.notifyDataSetChanged();
            //myGroup.saveLocalData(getContext());
        }
    }

    private void initGroupRecycler(){
        //References the RecyclerView in the MainActivity
        groupRecyclerView = mView.findViewById(R.id.mainRecycler);

        //Creates a new class object from our custom RecycleViewAdapter.Java class
        //This is calling the constructor
        groupAdapter = new GroupRecycleViewAdapter(myGroup, getActivity(), this);

        //Connects our recycler and our adapter
        groupRecyclerView.setAdapter(groupAdapter);

        //This will order the items correctly in a linear fashion
        groupRecyclerView.setLayoutManager(new LinearLayoutManager(getActivity()));

        //DEBUG Statement: Called to ensure that the recycler was created without any fatal errors
        Log.i("init called:", "Recycler created successfully");
    }//End initRecycler

}
