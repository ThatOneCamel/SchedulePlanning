package efx.com.GroupLink;

        import android.util.Log;
        import com.google.firebase.auth.FirebaseAuth;
        import com.google.firebase.database.FirebaseDatabase;
        import java.io.Serializable;
        import java.text.ParseException;
        import java.text.SimpleDateFormat;
        import java.util.ArrayList;
        import java.util.Collections;
        import java.util.Comparator;
        import java.util.List;
        import java.util.Locale;
        import com.google.firebase.database.DatabaseReference;

public class GroupInfo implements Serializable{
    private String groupName, groupLead, groupUID, group;
    private int numOfMem;
    private int numOfGroupEvents;
    private String groupColor;
    private String groupKey;
    private int votingVariable;
    private ArrayList<String> groupMembers;
    private List<ArrayList<String>> groupData;
    FirebaseDatabase database;
    DatabaseReference databaseRef;

    //Get the color scheme set in settings
    String getColor(){ return groupColor; }
    void setColor(String input){ groupColor = input; Log.i("COLOR_SET_TO" , groupColor); }

    //Constructor
    GroupInfo(){
        groupData = new ArrayList<>();
        groupLead = groupName = "default";
        numOfGroupEvents = 0;
        numOfMem = 1;
        groupColor = "green";
        groupKey = "";
        database = FirebaseDatabase.getInstance();

        //Referencing root -> users -> UID [This is where user's data will be written]
        databaseRef = database.getReference().child("allgroups");
        databaseRef.keepSynced(true);

        Log.i("GROUP_NAME" , groupName);
        Log.i("GROUP_LEAD", groupLead);
        Log.i("DEFAULT_COLOR" , groupColor);
        Log.d("NUM_OF_MEMBERS", ":" + numOfMem);
        Log.i("NUM_GROUP_EVENTS" , Integer.toString(numOfGroupEvents));
    }

    DatabaseReference getDatabaseRef(){ return databaseRef; }


    //This creates a NEW event
    void addGroupEvent(String myName, String myDate, String myTime, String myDesc, String flavorText){
        //This creates a new ArrayList [Which is a new ROW of data]
        groupData.add(new ArrayList<String>());
        groupData.get(numOfGroupEvents).add(myName);
        groupData.get(numOfGroupEvents).add(myDate);
        groupData.get(numOfGroupEvents).add(myTime);
        groupData.get(numOfGroupEvents).add(myDesc);
        groupData.get(numOfGroupEvents).add(flavorText);
        printGroupData(numOfGroupEvents);
        numOfGroupEvents++;
    }
    void editGroupEvent(String myName, String myDate, String myTime, String myDesc, String flavorText, int pos){
        setGroupEventName(pos, myName);
        setGroupEventDate(pos, myDate);
        setGroupEventTime(pos, myTime);
        setGroupEventDesc(pos, myDesc);
        setGroupEventFlavor(pos, flavorText);
        printGroupData(pos);
        sort();
    }
    void deleteGroupEvent(int pos){
        String tempKey = groupData.get(pos).get(5);
        Log.i("KEY_VALUE", tempKey);
        FirebaseDatabase.getInstance().getReference().child("allgroups").child("gEvents").child(tempKey).removeValue();
        groupData.remove(pos);
        numOfGroupEvents--;
    }
    void importGroupEvent(PushGroupFire holder){
        addGroupEvent(
                holder.getEventName(),
                holder.getEventDate(),
                holder.getEventTime(),
                holder.getEventDescription(),
                holder.getEventFlavorText()
        );
        Log.i("EVENT_" + groupEventSize(), "WAS IMPORTED");
    }

    void verify(){
        groupKey = databaseRef.child(groupName).push().getKey();
        databaseRef.child(groupKey).child("groupname").setValue(groupName);
        Log.i("PUSH", "GROUPNAME PUSHED");
    }

    ArrayList<String> getGroupEvent(int pos){ return groupData.get(pos); }

    void pushGroupToDatabase(){
        int n = groupEventSize();
        PushGroupFire object = new PushGroupFire(getGroupEvent(n -1));
        String key = databaseRef.child("gEvents").push().getKey();
        addKey(n, key);
        databaseRef.child("gEvents").child(key).setValue(object);
        Log.i("VALUE SET", "Something was pushed to Firebase");
    }

    public void setGroupName(String name){ groupName = name; }
    public String getGroupName(){ return groupName; }

    public void setGroupLead(String name){ groupLead = name; }
    //These override EXISTING event attributes
    private void setGroupEventName(int i, String input){ groupData.get(i).set(0, input); }
    private void setGroupEventDate(int i, String input){ groupData.get(i).set(1, input); }
    private void setGroupEventTime(int i, String input){ groupData.get(i).set(2, input); }
    private void setGroupEventDesc(int i, String input){ groupData.get(i).set(3, input); }
    private void setGroupEventFlavor(int i, String input){ groupData.get(i).set(4, input); }

    // 0=Title, 1=Date, 2=Time, 3=Description, 4=FlavorText
    //These RETURN a SPECIFIC event attribute
    String getGroupEventName(int i){ return groupData.get(i).get(0); }
    String getGroupEventDate(int i){ return groupData.get(i).get(1); }
    String getGroupEventTime(int i){ return groupData.get(i).get(2); }
    String getGroupEventDesc(int i){ return groupData.get(i).get(3); }
    String getGroupEventFlavor(int i){ return groupData.get(i).get(4); }
    String getGroupEventPostID(int i){ return groupData.get(i).get(5); }
    void printGroupData(int row){ Log.i("Row" + row, groupData.get(row).toString()); }

    String getGroupKey(){ return groupKey; }

    //Returns entire event
    ArrayList<String> getEvent(int pos){ return groupData.get(pos); }

    String getUid(){ return groupUID; }
    boolean isEmpty(){ return numOfGroupEvents == 0; }
    int groupEventSize(){ return numOfGroupEvents; }

    void addKey(int pos, String key){
        pos -= 1;
        groupData.get(pos).add(key);
    }

    public void getGroupUID(){

    }

    void sort(){
        //Date is position 1, Time is position 2
        //Will sort entire List based on a single column [in this case, sorting based on the Date]
        Collections.sort(groupData, new Comparator<ArrayList<String>>() {
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
        for(int i = numOfGroupEvents - 1; i > 0; i--){
            //If two dates are the same...
            if (groupData.get(i).get(1).equals( groupData.get(i-1).get(1))){
                ArrayList<String> temp = groupData.get(i);
                //timeX[0] is equal to the event's start time
                //timeA is the newer event
                //timeB is the older event
                String timeA[] = getGroupEventTime(i).split("-");
                String timeB[] = getGroupEventTime(i-1).split("-");

                //If Old event takes place before New event, no change
                if(timeB[0].contains("AM") && timeA[0].contains("PM")){
                    Log.i("FIRST IF", "No Swap Made");

                } else if (timeB[0].contains("PM") && timeA[0].contains("AM")){
                    //If Old event takes place after New event, swap the two
                    groupData.set(i, groupData.get(i-1));
                    groupData.set(i-1, temp);
                    Log.i("SECOND IF", "Swapped");

                } else if (timeB[0].compareTo(timeA[0]) > 0){
                    //If both events have AM, or both have PM, compare the two times
                    //If the old event takes place after the new event, swap the two
                    //Setting A=B
                    groupData.set(i, groupData.get(i-1));

                    //Setting B=Temp which was A
                    //So now A and B have been swapped
                    groupData.set(i-1, temp);
                    Log.i("THIRD IF", "Swapped");
                }//End CompareTo If-Statement
            }//End Equals If-Statement
        }
    } //End sortByTime()

}
class PushGroupFire implements Serializable {

    private String groupName;
    private String groupLeader;
    private String eventName, eventDate, eventTime, eventDescription, eventFlavorText;

    //Constructor
    PushGroupFire(ArrayList<String> events){
        eventName = events.get(0);
        eventDate = events.get(1);
        eventTime = events.get(2);
        eventDescription = events.get(3);
        eventFlavorText = events.get(4);
    }

    public PushGroupFire() {
    }
    public String getGroupName(){ return groupName; }
    //GetGroupMembers

    public String getGroupLeader(){ return groupLeader; }
    public String getEventName() {
        return eventName;
    }
    public String getEventDate() {
        return eventDate;
    }
    public String getEventTime() {
        return eventTime;
    }
    public String getEventDescription() {
        return eventDescription;
    }
    public String getEventFlavorText() { return eventFlavorText; }

    public String setGroupLeader(String leader){ return this.groupLeader = leader; }
    public void setEventName(String title) { this.eventName = title; }
    public void setEventDate(String date) { this.eventDate = date; }
    public void setEventTime(String time) { this.eventTime = time; }
    public void setEventDescription(String description) { this.eventDescription = description; }
    public void setEventFlavorText(String flavorText) { this.eventFlavorText = flavorText; }
}