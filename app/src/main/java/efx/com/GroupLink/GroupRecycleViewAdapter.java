package efx.com.GroupLink;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.recyclerview.widget.RecyclerView;

public class GroupRecycleViewAdapter extends RecyclerView.Adapter<GroupRecycleViewAdapter.mViewHolder> {

    //RecycleView
    GroupInfo group;
    GroupTab groupFrag;


    //Context is basically the system getting the current state of an object/environment
    //Simply put: Each list item has its own individual context
    private Context context;  //Separate fragment objects
    int colorID;

    //Basically a copy constructor, copies the UserInfo object from main and keeps track of it
    //Also grabs context from MainActivity
    public GroupRecycleViewAdapter(GroupInfo myGroup, Context appContext,  GroupTab frag){
        group = myGroup;
        groupFrag = frag;
        context = appContext;
    }

    //Note: Override methods are default functions/methods of the AndroidOS that we are modifying to suit our needs
    @NonNull
    @Override
    //Method responsible for inflating the view
    //Inflating - To load and render a resource then hold it in memory for quick access
    public GroupRecycleViewAdapter.mViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View mView = LayoutInflater.from(parent.getContext()).inflate(R.layout.fragment_blank, parent, false);
        GroupRecycleViewAdapter.mViewHolder holder = new GroupRecycleViewAdapter.mViewHolder(mView);
        return holder;
    }

    @Override
    //This method is called every single time a new item is added to the list
    //Allows you to modify template items [such as fragments] as they are created in the list
    //This process will be fairly automatic since we are typically creating these objects from existing data
    public void onBindViewHolder(@NonNull final GroupRecycleViewAdapter.mViewHolder holder, final int position) {
        //Sets the fragment colors to the color defined by the static variable
        colorID = context.getResources().getIdentifier("button_" + group.getColor(),
                "drawable", context.getApplicationInfo().packageName);
        holder.fragHour.setText(group.getGroupEventFlavor(position));
        holder.fragEventName.setText(group.getGroupEventName(position));
        holder.fragTime.setText(group.getGroupEventTime(position));
        holder.fragDesc.setText(group.getGroupEventDesc(position));
        holder.fragDate.setText(group.getGroupEventDate(position));
        holder.fragBg.setImageResource(colorID);
        //Remember fragment parent is the ConstraintLayout
        holder.fragmentParent.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Log.d("Clicked at:", group.getGroupEventName(position));
                Toast.makeText(context, group.getGroupEventName(position), Toast.LENGTH_SHORT).show();

                //Using the context from the MainActivity to start a new intent as if we were calling
                //it from MainActivity itself
                Intent mIntent = new Intent(context, EventData.class);
                mIntent.putStringArrayListExtra("event", group.getEvent(position));
                mIntent.putExtra("pos", position);
                groupFrag.startActivityForResult(mIntent, 123);
            }
        });
    }

    @Override
    //Returns the total number of existing items by returning the size of the ArrayList
    public int getItemCount() {
        return group.groupEventSize();
    }

    //Holds individual recyclerview elements in memory, for faster/efficient loading
    //By putting the fragment into the memory findViewById(...), which is expensive,
    //Only has to be called once, instead of every time an object is created
    //IE: References the fragment template, puts it in memory for fast access
    //Process is automated by the AndroidOS
    public class mViewHolder extends RecyclerView.ViewHolder{
        //Variables
        TextView fragHour;
        TextView fragEventName;
        TextView fragTime;
        TextView fragDesc;
        TextView fragDate;
        ImageView fragBg;
        ConstraintLayout fragmentParent;

        public mViewHolder(View itemView){
            super(itemView);
            fragHour = itemView.findViewById(R.id.hourOfDay);
            fragEventName = itemView.findViewById(R.id.fragEventName);
            fragTime = itemView.findViewById(R.id.fragTime);
            fragDesc = itemView.findViewById(R.id.fragDesc);
            fragDate = itemView.findViewById(R.id.fragDate);
            fragBg = itemView.findViewById(R.id.fragBackground);
            fragmentParent = itemView.findViewById(R.id.userFragment);
        }

    }


}
