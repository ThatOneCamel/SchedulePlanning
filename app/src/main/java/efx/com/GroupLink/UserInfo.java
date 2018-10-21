package efx.com.GroupLink;

import android.util.Log;

import com.firebase.ui.auth.data.model.User;
import com.google.firebase.auth.FirebaseAuth;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;
import java.util.Locale;

public class UserInfo {

    private FirebaseAuth userAuthorization;
    private String name, email, uid;

    //private ArrayList<String> eventNames, eventDates, eventTimes, eventDescriptions, eventFlavorText, groups;
    //private ArrayList<Date> eventDates;
    private ArrayList<Boolean> eventIsPrivate;
    private int numOfEvents;

    //Creating a 2d List. A list of lists
    private List<ArrayList<String>> data;

    //Column Order is: Name, Date, Time, Description, FlavorText


    //private RecycleViewAdapter userAdapter;

    UserInfo(){
        userAuthorization = FirebaseAuth.getInstance();
        //uid = userAuthorization.getUid();

        data = new ArrayList<>();

        name = email = "default";
        numOfEvents = 0;

        /*eventNames = new ArrayList<>();
        eventDates = new ArrayList<>();
        eventTimes = new ArrayList<>();
        eventDescriptions = new ArrayList<>();
        eventFlavorText = new ArrayList<>();*/
        //groups = new ArrayList<>();Log.i("UID:" , uid);
        eventIsPrivate = new ArrayList<>();

        Log.i("NAME:" , name);
        Log.i("EMAIL:" , email);
        Log.i("NUMEVENTS:" , Integer.toString(numOfEvents));

    }

    //Set & Get functions

    /*void addNewEvent(String myName, String myDate, String myTime, String myDesc, String flavorText, Boolean privacy){
        eventNames.add(myName);
        eventDates.add(myDate);
        eventTimes.add(myTime);
        eventDescriptions.add(myDesc);
        eventFlavorText.add(flavorText);
        eventIsPrivate.add(privacy);
        numOfEvents++;
    }*/

    void addEvent(String myName, String myDate, String myTime, String myDesc, String flavorText){
        //This creates a new ArrayList [Which is a new ROW of data]
            //if (numOfEvents != 0)
            data.add(new ArrayList<String>());

            data.get(numOfEvents).add(myName);
            data.get(numOfEvents).add(myDate);
            data.get(numOfEvents).add(myTime);
            data.get(numOfEvents).add(myDesc);
            data.get(numOfEvents).add(flavorText);
            printData(numOfEvents);
            numOfEvents++;
            sort();

    }

    ArrayList<String> getEvent(int pos){ return data.get(pos); }

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

        } //End For Loop


    }//End Sort

    // 0=Name, 1=Date, 2=Time, 3=Description, 4=FlavorText
    // 4=FlavorText
    String getEventName(int i){ return data.get(i).get(0); }
    String getEventDate(int i){ return data.get(i).get(1); }
    String getEventTime(int i){ return data.get(i).get(2); }
    String getEventDesc(int i){ return data.get(i).get(3); }
    String getEventFlavor(int i){ return data.get(i).get(4); }


    boolean isEmpty(){ return numOfEvents == 0; }
    void addNumberOfEvents(int i){ numOfEvents += i; }
    int size(){ return numOfEvents; }

    String getUid(){ return uid; }

    void setUsername(String input){ name = input; }
    void setEmail(String input){ email = input; }

    String getName(){ return name; }
    String getEmail(){ return email; }

    //These create NEW event attributes
    //These are for DEBUG purposes, generally we will use addNewEvent(...)
    /*void addEventName(String input){ eventNames.add(input); }
    void addEventDate(String input){ eventDates.add(input); }
    void addEventTime(String input){ eventTimes.add(input); }
    void addEventDesc(String input){ eventDescriptions.add(input); }
    void addEventFlavor(String input){ eventFlavorText.add(input); }
    void addGroup(String input){ groups.add(input); }

    //These override EXISTING event attributes
    void setEventName(int i, String input){ eventNames.set(i, input); }
    void setEventDate(int i, String input){ eventDates.set(i, input); }
    void setEventTime(int i, String input){ eventTimes.set(i, input); }
    void setEventDesc(int i, String input){ eventDescriptions.set(i, input); }
    void setEventFlavor(int i, String input){ eventFlavorText.set(i, input); }
    void setGroup(int i, String input){ groups.set(i, input); }

    //These RETURN a SPECIFIC event attribute
    String getEventName(int i){ return eventNames.get(i); }
    String getEventDate(int i){ return eventDates.get(i); }
    String getEventTime(int i){ return eventTimes.get(i); }
    String getEventDesc(int i){ return eventDescriptions.get(i); }
    String getEventFlavor(int i){ return eventFlavorText.get(i); }*/
    //String getGroup(int i){ return groups.get(i); }

    void printData(int row){
        Log.i("Row" + row, data.get(row).toString());


    }





    private class Groups {
        private ArrayList<String> groupName, key;
        private ArrayList<UserInfo> groupMembers;

        Groups(){
            //Add Group Members
        }

    }
}

