package efx.com.GroupLink;

import android.app.Dialog;
import android.app.TimePickerDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;

import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.text.format.DateFormat;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.TimePicker;

import com.google.firebase.auth.FirebaseUser;

import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.Calendar;

public class EventData extends AppCompatActivity {

    private EditText[] input;
    private TextView alertTextView;
    private String[] fakeGroupNames;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_event_data);

        Toolbar topToolBar = findViewById(R.id.toolbar);
        //setSupportActionBar(topToolBar);
        //ActionBar actionBar = getSupportActionBar();
        //actionBar.setDisplayHomeAsUpEnabled(true);

        input = new EditText[]{
                findViewById(R.id.eventTitle),
                findViewById(R.id.fragDesc),
                findViewById(R.id.eventDate),
                findViewById(R.id.eventStart),
                findViewById(R.id.eventEnd),
                findViewById(R.id.eventVisibility)
        };

        //Creating on click listener
        input[2].setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

            }
        });//End Date button OnClickListener

        //Creating on click listeners for the eventStart and eventEnd text fields.

        //When they are clicked they will create and open a new TimePickerDialog
        //eventStart OnClickListener
        input[3].setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                createTimeDialog(input[3]).show();
            }
        });

        //eventEnd OnClickListener
        input[4].setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                createTimeDialog(input[4]).show();
            }
        });

        fakeGroupNames = new String[]{
                "GroupA",
                "GroupB",
                "GroupC"
        };

        //eventVisibility OnClickListener
        input[5].setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                createCheckboxDialog(fakeGroupNames, input[5]).show();
            }
        });

    }

    //public Dialog createDateDialog(final TextView myTextView){
        //return dateDialog;
    //}

    //Creates the TimePickerDialog
    public Dialog createTimeDialog(final TextView myTextView){
        //Creating a new calendar instance with the device's current date and time
        final Calendar c = Calendar.getInstance();

        //Pulling the data from said instance and using it to pre-set the dialog picker
        int hour = c.get(Calendar.HOUR_OF_DAY);
        int min = c.get(Calendar.MINUTE);

        //Defining OnTimeSetListener, this will basically run the method below when the time has
            // been set by the user after pressing "Ok" in the dialog
        final TimePickerDialog.OnTimeSetListener myListener = new TimePickerDialog.OnTimeSetListener() {
            @Override
            public void onTimeSet(TimePicker view, int hourOfDay, int minute) {
                String AM_PM;

                //If the hour set is 12AM or 12PM, this ensures that the time is displayed correctly
                switch(hourOfDay){
                    case 0:
                        hourOfDay = 12;
                        AM_PM = "AM";
                        break;

                    case 12:
                        hourOfDay = 12;
                        AM_PM = "PM";
                        break;

                    default:
                        if (hourOfDay < 12) {
                            AM_PM = "AM";
                        } else {
                            hourOfDay -= 12;
                            AM_PM = "PM";
                        }

                }

                //Sets the text of the TextView that called for the dialog
                myTextView.setText(String.format("%2d:%02d " + AM_PM, hourOfDay, minute));

            }
        };//End OnTimeSetListener

        //This is the actual dialog that will pop up when a user initiates a TimePicker event
        TimePickerDialog timePickerDialog = new TimePickerDialog(
                this, //Defining the context
                R.style.TimePickerTheme, //Setting the theme
                myListener, //Setting the OnTimeSetListener
                hour, //Grabbing the pre-set hour
                min, //Grabbing the pre-set minute
                false); //Determining whether the picker is 12-hour or 24-hour

        //Sets the title of the dialog and the surrounding background's appearance
        timePickerDialog.setTitle("Choose the time:");
        //timePickerDialog.getWindow().setBackgroundDrawableResource(android.R.color.transparent);
        return timePickerDialog;

    }// End createTimeDialog(...)

    public Dialog createCheckboxDialog(String userGroups[], final TextView myTextView){

        //Clearing the TextView before it is appended to
        myTextView.setText("");

        final ArrayList<Integer> selectedGroups = new ArrayList<>();

        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle("Set which groups can view this event");

        builder.setMultiChoiceItems(userGroups, null, new DialogInterface.OnMultiChoiceClickListener() {


            //What happens when the Dialog is exited by pressing "Ok"
            //Groups will be presented by their index order, [Ex: (GroupA, GroupB, GroupC) = (0, 1, 2)
            @Override
            public void onClick(DialogInterface dialog, int which, boolean isChecked) {
                //If a selected item is checked, add it to the ArrayList
                if (isChecked){
                    selectedGroups.add(which);

                //If the arrayList contains a selected item, remove it
                } else if (selectedGroups.contains(which)){
                    selectedGroups.remove(Integer.valueOf(which));
                }
            }


        });

        //When Done is touched, parse the user's choices from an integer to a string [Ex: 0 -> GroupA]
        builder.setPositiveButton("Done", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {

                //parseUserGroups(selectedGroups);
                for (int i = 0; i < selectedGroups.size(); i++){

                    //If 'i' is at the last index position, append the text without a comma
                    if (i == selectedGroups.size() -1){
                        myTextView.append(fakeGroupNames[selectedGroups.get(i)]);
                    } else {

                        //Checks the Array for the names of the groups using the selected group indexes
                        //fakeGroupNames holds the strings
                        //selectedGroups is being used as an index of what strings to use
                        myTextView.append(fakeGroupNames[selectedGroups.get(i)] + ", ");

                    }//End If-Else

                    Log.d("Checkbox has returned:", selectedGroups.get(i).toString());
                    dialog.dismiss();

                }//End For loop


            }
        }); //End of Positive Button

        //When Cancel is touched, close dialog
        builder.setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                dialog.dismiss();
            }
        });//End of Negative Button



        return builder.create();
    }

    //Returns list of groups
    private ArrayList<String> parseUserGroups(ArrayList<Integer> index){

        ArrayList<String> userGroups = new ArrayList<>();
        String temp;

        //PseudoCode
        /*for (int i = 0; i < index.size(); i++){
            temp = FirebaseUserHere.GetUserGroups(index.get(i));
            userGroups.add(temp);
        }*/

        return userGroups;
    }


    //Creating the error alert box
    public Dialog createDialog(){
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setMessage("You must fill every text slot.");
        builder.setPositiveButton("Close", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                emptyField = false;
                dialog.dismiss();
            }
        });

        //Creates the builder, waits for .show() to be called
        return builder.create();

    }


    boolean emptyField = false;
    String assignStringData(int pos){
        if (input[pos].getText().toString().isEmpty()){
            emptyField = true;
            return null;

        } else {
            String inputString = input[pos].getText().toString();
            Log.i("Value:", inputString);
            return inputString;
        }

    }

    public void saveData(View v){
        //String title, description, date, start, end, sAMPM, eAMPM;

        /*title = assignStringData(0);
        description= assignStringData(1);
        date = assignStringData(2);
        start = assignStringData(3);
        end = assignStringData(5); */


        //If any of these fields are empty, emptyField will be set to true, causing an error dialog
        String arr[] = new String[5];

        for (int i = 0; i < 5; i++){
            arr[i] = assignStringData(i);
        }

        //If any of the fields were empty, show  error message
        if (emptyField){
            //When error message is closed, emptyField is set to false again
            createDialog().show();


        //Otherwise, put all of the userInput into an intent bundle to be transferred to the MainActivity
        } else {
            Intent mIntent = new Intent(EventData.this, MainScreenActivity.class);
            mIntent.putExtra("title", arr[0]);
            mIntent.putExtra("description", arr[1]);
            mIntent.putExtra("date", arr[2]);
            mIntent.putExtra("start", arr[3]);
            mIntent.putExtra("end", arr[4]);
            setResult(123, mIntent);
            finish();
        }

    }
}