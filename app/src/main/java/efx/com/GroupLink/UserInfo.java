package efx.com.GroupLink;

import android.util.Log;

import com.firebase.ui.auth.data.model.User;
import com.google.firebase.auth.FirebaseAuth;

import java.util.ArrayList;
import java.util.Date;

public class UserInfo {

    private FirebaseAuth userAuthorization;
    private String name, email, uid;

    private ArrayList<String> eventNames, eventDates, eventTimes, eventDescriptions, eventFlavorText, groups;
    //private ArrayList<Date> eventDates;
    private ArrayList<Boolean> eventIsPrivate;
    private int numOfEvents;


    //private RecycleViewAdapter userAdapter;

    UserInfo(){
        userAuthorization = FirebaseAuth.getInstance();
        //uid = userAuthorization.getUid();

        name = email = "default";
        numOfEvents = 0;

        eventNames = new ArrayList<>();
        eventDates = new ArrayList<>();
        eventTimes = new ArrayList<>();
        eventDescriptions = new ArrayList<>();
        eventFlavorText = new ArrayList<>();
        //groups = new ArrayList<>();Log.i("UID:" , uid);

        Log.i("NAME:" , name);
        Log.i("EMAIL:" , email);
        Log.i("NUMEVENTS:" , Integer.toString(numOfEvents));

    }

    //Set & Get functions

    void addNewEvent(String myName, String myTime, String myDesc, String flavorText, Boolean privacy){
        eventNames.add(myName);
        eventTimes.add(myTime);
        eventDescriptions.add(myDesc);
        eventFlavorText.add(flavorText);
        eventIsPrivate.add(privacy);
        numOfEvents++;
    }

    boolean isEmpty(){ return numOfEvents == 0; }
    void addNumberOfEvents(int i){ numOfEvents += i; }
    int getNumberOfEvents(){ return numOfEvents; }
    int size(){ return numOfEvents; }

    String getUid(){ return uid; }

    void setUsername(String input){ name = input; }
    void setEmail(String input){ email = input; }

    String getName(){ return name; }
    String getEmail(){ return email; }

    //These create NEW event attributes
    //These are for DEBUG purposes, generally we will use addNewEvent(...)
    void addEventName(String input){ eventNames.add(input); }
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
    String getEventFlavor(int i){ return eventFlavorText.get(i); }
    String getGroup(int i){ return groups.get(i); }





    private class Groups {
        private ArrayList<String> groupName, key;
        private ArrayList<UserInfo> groupMembers;

        Groups(){
            //Add Group Members
        }

    }
}

