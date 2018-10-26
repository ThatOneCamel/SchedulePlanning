package efx.com.GroupLink;

import android.app.Activity;
import android.content.Context;
import androidx.annotation.NonNull;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.util.Log;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;

public class RecycleViewAdapter extends RecyclerView.Adapter<RecycleViewAdapter.mViewHolder> {

    //RecycleView Variables

    //Array that will contain the information from the data set, the info to be displayed on individual fragments
   /* public ArrayList<String> hours = new ArrayList<>(); //used to store hours on the left side of fragment
    private ArrayList<String> eventName = new ArrayList<>(); //used to store eventName inside the fragment
    private ArrayList<String> times = new ArrayList<>(); //used to store time inside the fragment
    private ArrayList<String> description = new ArrayList<>(); //used to store description inside the fragment*/

    UserInfo user;

    //Context is basically the system getting the current state of an object/environment
    //Simply put: Each list item has its own individual context
    private Context context;  //Separate fragment objects

    //Adapter Constructor [Connects Data set to ArrayLists]
    //'d' prefix represents "data"
    /*public RecycleViewAdapter(ArrayList<String> dHours, ArrayList<String> dName, ArrayList<String> dTimes, ArrayList<String> dDescription, Context dContext){
        hours = dHours;
        eventName = dName;
        times = dTimes;
        description = dDescription;
        context = dContext;

        user.setEventName(0, eventName.get(0));
        //eventTimes.add(myTime);
        //eventDescriptions.add(myDesc);
        //eventTimeDisplays.add(myHour);
        //eventIsPrivate.add(privacy);
    }*/

    public RecycleViewAdapter(){

    }

    public RecycleViewAdapter(UserInfo myUser, Context appContext){
        user = myUser;
        context = appContext;
        //user.addEventName("TEST TEST CONSTRCUTOR");

    }

    public void setEqual(UserInfo main){
        user = main;
    }

    public void setContext(Context appContext){
        context = appContext;
    }

    //Note: Override methods are default methods of the AndroidOS that we are modifying to suit our needs

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

        holder.fragHour.setText(user.getEventFlavor(position));
        holder.fragEventName.setText(user.getEventName(position));
        holder.fragTime.setText(user.getEventTime(position));
        holder.fragDesc.setText(user.getEventDesc(position));
        holder.fragDate.setText(user.getEventDate(position));

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
        ConstraintLayout fragmentParent;

        public mViewHolder(View itemView){
            super(itemView);
            fragHour = itemView.findViewById(R.id.hourOfDay);
            fragEventName = itemView.findViewById(R.id.fragEventName);
            fragTime = itemView.findViewById(R.id.fragTime);
            fragDesc = itemView.findViewById(R.id.fragDesc);
            fragDate = itemView.findViewById(R.id.fragDate);
            fragmentParent = itemView.findViewById(R.id.userFragment);

        }
    }

}
