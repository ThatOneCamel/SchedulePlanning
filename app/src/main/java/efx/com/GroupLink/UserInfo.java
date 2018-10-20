package efx.com.GroupLink;

import android.util.Log;

import com.firebase.ui.auth.data.model.User;
import com.google.firebase.auth.FirebaseAuth;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.Date;
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
            sortEvents();

    }

    void sortEvents(){

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
        });
    }

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

    void sort(){
        //String datesA[] = getEventDate(0).split("/");
       // String datesB[] = getEventDate(1).split("/");

        Log.i("TAGHERE", "1st is Month, 2nd is Day, 3rd is Year");
        for(int i = 0; i < 3; i++){
            //Log.i("DateA:", datesA[i]);
            //Log.i("DateB:", datesB[i]);

        }

    }

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

