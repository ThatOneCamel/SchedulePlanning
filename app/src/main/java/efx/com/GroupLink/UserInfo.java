package efx.com.GroupLink;

import android.content.Context;
import android.util.Log;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.FirebaseDatabase;

import java.io.FileOutputStream;
import java.io.IOException;
import java.io.ObjectOutputStream;
import java.io.Serializable;

import java.text.ParseException;
import java.text.SimpleDateFormat;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;
import java.util.Locale;

public class UserInfo implements Serializable {

    //private FirebaseAuth userAuthorization;
    private String name, email, databaseKey;
    private String color;

    private ArrayList<Boolean> eventIsPrivate;
    private int numOfEvents;
    //private Context context;

    //Creating a 2d List. A list of lists
    private List<ArrayList<String>> data;
    //private UserTab reference;

    String getColor(){ return color; }
    void setColor(String input){ color = input; Log.i("COLOR_SET_TO" , color); }

    //Column Order is: Name, Date, Time, Description, FlavorText
    //Default Constructor
    UserInfo(){
        data = new ArrayList<>();

        name = email = "default";
        numOfEvents = 0;
        color = "green";

        eventIsPrivate = new ArrayList<>();

        Log.i("NAME" , name);
        Log.i("EMAIL" , email);
        Log.i("DEFAULT_COLOR" , color);

        Log.i("NUM_EVENTS" , Integer.toString(numOfEvents));

    }

    //void setReference(UserTab fragment){ reference = fragment; }

    //UserTab getReference(){ return reference; }

    void importEvent(PushFire holder){
        addEvent(
                holder.getTitle(),
                holder.getDate(),
                holder.getTime(),
                holder.getDescription(),
                holder.getFlavorText()
        );

        Log.i("EVENT_" + size(), "WAS IMPORTED");

    }

    //Serializes class object into a file
    void saveLocalData(Context context){
        try {
            //FileOutputStream file = new FileOutputStream(filepath);
            FileOutputStream file = context.openFileOutput("user.dat", Context.MODE_PRIVATE);

            ObjectOutputStream objectOut = new ObjectOutputStream(file);
            objectOut.writeObject(this);
            objectOut.close();
            file.close();
            Log.i("FILE", "DATA_SAVED");
        } catch (IOException error){
            Log.i("FILE_ERROR", error.getLocalizedMessage());
            error.printStackTrace();
        }
    }

    void deleteLocalData(Context context){
        context.deleteFile("user.dat");
        Log.i("LOCALDATA", "DELETED USER FILES");

    }

    void setDatabaseKey(String key){ databaseKey = key; }
    String getDatabaseKey(){ return databaseKey;}

    void addKey(int pos, String key){
        pos -= 1;
        data.get(pos).add(key);
    }

    //This creates a NEW event
    void addEvent(String myName, String myDate, String myTime, String myDesc, String flavorText){
        //This creates a new ArrayList [Which is a new ROW of data]
        data.add(new ArrayList<String>());
        data.get(numOfEvents).add(myName);
        data.get(numOfEvents).add(myDate);
        data.get(numOfEvents).add(myTime);
        data.get(numOfEvents).add(myDesc);
        data.get(numOfEvents).add(flavorText);
        printData(numOfEvents);
        numOfEvents++;

    }

    void editEvent(String myName, String myDate, String myTime, String myDesc, String flavorText, int pos){
        setEventName(pos, myName);
        setEventDate(pos, myDate);
        setEventTime(pos, myTime);
        setEventDesc(pos, myDesc);
        setEventFlavor(pos, flavorText);
        printData(pos);
        sort();
    }

    void deleteEvent(int pos){
        String tempKey = data.get(pos).get(5);
        Log.i("KEY_VALUE", tempKey);
        FirebaseDatabase.getInstance().getReference().child("users").child(FirebaseAuth.getInstance().getUid()).child("events").child(tempKey).removeValue();

        data.remove(pos);
        numOfEvents--;
    }

    void sort(){

        //Date is position 1, Time is position 2
        //Will sort entire List based on a single column [in this case, sorting based on the Date]
        Collections.sort(data, new Comparator<ArrayList<String>>() {
            @Override
            public int compare(ArrayList<String> iteratorA, ArrayList<String> iteratorB) {

                //The format our dates are in [Month/Day/Year]
                SimpleDateFormat dateFormat = new SimpleDateFormat("MM/dd/yyyy", Locale.US);

                // dateFormat.parse(stringVar) converts a string to a Date based on the above format
                //Will convert our strings to Date variables, compare them, then sort based on the result
                try {
                    return dateFormat.parse(iteratorA.get(1)).compareTo(dateFormat.parse(iteratorB.get(1)));
                } catch (ParseException e) {
                    //What to do on fail
                    Log.i("EXCEPTION PARSE", "Dates could not be parsed");
                }

                //Finish sort
                return 0;

                //return iteratorA.get(0).compareTo(iteratorB.get(0));
            }
        }); //End Sort by Calendar Date

        sortByTime();

    }//End Sort()

    private void sortByTime(){
        //Loops through events to check for events that take place on the same day
        //Then checks those events' times and sorts them accordingly
        for(int i = numOfEvents - 1; i > 0; i--){
            //If two dates are the same...
            if (data.get(i).get(1).equals( data.get(i-1).get(1))){
                ArrayList<String> temp = data.get(i);

                //timeX[0] is equal to the event's start time
                //timeA is the newer event
                //timeB is the older event
                String timeA[] = getEventTime(i).split("-");
                String timeB[] = getEventTime(i-1).split("-");


                //If Old event takes place before New event, no change
                if(timeB[0].contains("AM") && timeA[0].contains("PM")){
                    Log.i("FIRST IF", "No Swap Made");

                } else if (timeB[0].contains("PM") && timeA[0].contains("AM")){
                    //If Old event takes place after New event, swap the two
                    data.set(i, data.get(i-1));
                    data.set(i-1, temp);
                    Log.i("SECOND IF", "Swapped");


                } else if (timeB[0].compareTo(timeA[0]) > 0){
                    //If both events have AM, or both have PM, compare the two times
                    //If the old event takes place after the new event, swap the two
                    //Setting A=B
                    data.set(i, data.get(i-1));

                    //Setting B=Temp which was A
                    //So now A and B have been swapped
                    data.set(i-1, temp);
                    Log.i("THIRD IF", "Swapped");

                }//End CompareTo If-Statement

            }//End Equals If-Statement

        }
    } //End sortByTime()


    //Returns entire event
    ArrayList<String> getEvent(int pos){ return data.get(pos); }

    // 0=Title, 1=Date, 2=Time, 3=Description, 4=FlavorText
    //These RETURN a SPECIFIC event attribute
    String getEventName(int i){ return data.get(i).get(0); }
    String getEventDate(int i){ return data.get(i).get(1); }
    String getEventTime(int i){ return data.get(i).get(2); }
    String getEventDesc(int i){ return data.get(i).get(3); }
    String getEventFlavor(int i){ return data.get(i).get(4); }
    String getEventPostID(int i){ return data.get(i).get(5); }


    //These override EXISTING event attributes
    private void setEventName(int i, String input){ data.get(i).set(0, input); }
    private void setEventDate(int i, String input){ data.get(i).set(1, input); }
    private void setEventTime(int i, String input){ data.get(i).set(2, input); }
    private void setEventDesc(int i, String input){ data.get(i).set(3, input); }
    private void setEventFlavor(int i, String input){ data.get(i).set(4, input); }

    boolean isEmpty(){ return numOfEvents == 0; }
    int size(){ return numOfEvents; }


    void setUsername(String input){ name = input; }
    void setEmail(String input){ email = input; }

    String getName(){ return name; }
    String getEmail(){ return email; }

    void printData(int row){ Log.i("Row" + row, data.get(row).toString()); }

}

class PushFire implements Serializable{
    private String title, date, time, description, flavorText;

    //Constructor
    PushFire(ArrayList<String> event){
        title = event.get(0);
        date = event.get(1);
        time = event.get(2);
        description = event.get(3);
        flavorText = event.get(4);
    }

    PushFire(String title_in, String date_in, String time_in, String desc_in, String flavor_in){
       title = time_in;
       date = date_in;
       time = time_in;
       description = desc_in;
       flavorText = flavor_in;
    }

    public PushFire() {
    }

    public String getTitle() {
        return title;
    }

    public String getDate() {
        return date;
    }

    public String getTime() {
        return time;
    }

    public String getDescription() {
        return description;
    }

    public String getFlavorText() { return flavorText; }

    public void setTitle(String title) { this.title = title; }

    public void setDate(String date) { this.date = date; }

    public void setTime(String time) { this.time = time; }

    public void setDescription(String description) { this.description = description; }

    public void setFlavorText(String flavorText) { this.flavorText = flavorText; }
}

