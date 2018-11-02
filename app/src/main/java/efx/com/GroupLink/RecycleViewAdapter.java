package efx.com.GroupLink;

import android.app.Activity;
import android.content.Context;

import androidx.annotation.DrawableRes;
import androidx.annotation.NonNull;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.content.SharedPreferences;
import android.content.res.Resources;
import android.graphics.drawable.Drawable;
import android.preference.PreferenceManager;
import android.util.Log;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

public class RecycleViewAdapter extends RecyclerView.Adapter<RecycleViewAdapter.mViewHolder> {

    //RecycleView
    UserInfo user;

    //Context is basically the system getting the current state of an object/environment
    //Simply put: Each list item has its own individual context
    private Context context;  //Separate fragment objects
    int colorID;


    //Adapter Constructor [Connects Data set to ArrayLists]
    public RecycleViewAdapter(){

    }

    //Basically a copy constructor, copies the UserInfo object from main and keeps track of it
    //Also grabs context from MainActivity
    public RecycleViewAdapter(UserInfo myUser, Context appContext){
        user = myUser;
        context = appContext;
    }

    //Note: Override methods are default functions/methods of the AndroidOS that we are modifying to suit our needs
    @NonNull
    @Override
    //Method responsible for inflating the view
    //Inflating - To load and render a resource then hold it in memory for quick access
    public mViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View mView = LayoutInflater.from(parent.getContext()).inflate(R.layout.fragment_blank, parent, false);
        mViewHolder holder = new mViewHolder(mView);
        return holder;
    }

    @Override
    //This method is called every single time a new item is added to the list
    //Allows you to modify template items [such as fragments] as they are created in the list
    //This process will be fairly automatic since we are typically creating these objects from existing data
    public void onBindViewHolder(@NonNull final mViewHolder holder, final int position) {
        //Log.i(, )
        /*holder.fragHour.setText(hours.get(position));
        holder.fragEventName.setText(eventName.get(position));
        holder.fragTime.setText(times.get(position));
        holder.fragDesc.setText(description.get(position));*/

        //Sets the fragment colors to the color defined by the static variable
        colorID = context.getResources().getIdentifier("button_" + user.getColor(),
                "drawable", context.getApplicationInfo().packageName);
        holder.fragHour.setText(user.getEventFlavor(position));
        holder.fragEventName.setText(user.getEventName(position));
        holder.fragTime.setText(user.getEventTime(position));
        holder.fragDesc.setText(user.getEventDesc(position));
        holder.fragDate.setText(user.getEventDate(position));
        holder.fragBg.setImageResource(colorID);

        //Remember fragment parent is the ConstraintLayout
        holder.fragmentParent.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Log.d("Clicked at:", user.getEventName(position));
                Toast.makeText(context, user.getEventName(position), Toast.LENGTH_SHORT).show();

                //Using the context from the MainActivity to start a new intent as if we were calling
                 //it from MainActivity itself
                Intent mIntent = new Intent(context, EventData.class);
                mIntent.putStringArrayListExtra("event", user.getEvent(position));
                mIntent.putExtra("pos", position);
                ((Activity)context).startActivityForResult(mIntent, 123);

            }
        });


    }

    @Override
    //Returns the total number of existing items by returning the size of the ArrayList
    public int getItemCount() {
        return user.size();
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
