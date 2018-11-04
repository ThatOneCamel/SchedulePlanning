package efx.com.GroupLink;

import android.app.DatePickerDialog;
import android.app.Dialog;
import android.app.TimePickerDialog;
import android.content.DialogInterface;
import android.content.Intent;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;

import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.TimePicker;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;

public class EventData extends AppCompatActivity {

    private EditText[] input;
    private String[] fakeGroupNames;
    private int position = -1;
    private Button deleteBtn;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_event_data);

        deleteBtn = findViewById(R.id.btnDelete);

        input = new EditText[]{
                findViewById(R.id.eventTitle),
                findViewById(R.id.fragDesc),
                findViewById(R.id.eventDate),
                findViewById(R.id.eventStart),
                findViewById(R.id.eventEnd),
                findViewById(R.id.eventVisibility)
        };

        unpackExtras();
        setClickListeners();

    }

    void unpackExtras(){
        ArrayList<String> oldData;
        if (getIntent().getExtras() != null) {
            deleteBtn.setVisibility(View.VISIBLE);

            //Order is Title, Date, Time, Description, FlavorText
            oldData = getIntent().getStringArrayListExtra("event");

            TextView header = findViewById(R.id.descriptiveText);
            header.setText("Editing the details of: " + oldData.get(0));

            input[0].setText(oldData.get(0));
            input[1].setText(oldData.get(3));
            input[2].setText(oldData.get(1));

            //Separates the Start and End Times, then removes the whitespace at beginning and end of the strings
            // trim() removes leading and ending whitespace
            String time[] = oldData.get(2).split("-");
            time[0] = time[0].trim();
            time[1] = time[1].trim();
            input[3].setText(time[0]);
            input[4].setText(time[1]);


            //Grabbing the position of the
            position = getIntent().getIntExtra("pos", -1);
            Log.i("INTENT_RESULT", "Extra received @pos:" + position);

        } else {
            Log.i("INTENT_RESULT", "No extras received, pos");
        }
    }

    void setClickListeners(){
        //Creating on click listener
        input[2].setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

            }
        });//End Date button OnClickListener

        //Creating on click listeners for the eventDate, eventStart and eventEnd text fields.

        input[2].setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                createDateDialog(input[2]).show();
            }
        });

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

    public Dialog createDateDialog(final TextView myTextView){
        final Calendar c = Calendar.getInstance();
        int year = c.get(Calendar.YEAR);
        int month = c.get(Calendar.MONTH);
        int day = c.get(Calendar.DAY_OF_MONTH);

        final DatePickerDialog.OnDateSetListener myListener = new DatePickerDialog.OnDateSetListener() {
            @Override
            public void onDateSet(DatePicker view, int year, int month, int dayOfMonth) {
                c.set(year, month, dayOfMonth);
                Date date = c.getTime();
                //Format is Month, Day, Year
                SimpleDateFormat sdf = new SimpleDateFormat("MM/dd/yyyy");

                myTextView.setText(sdf.format(date));
            }
        };

        DatePickerDialog dateDialog = new DatePickerDialog(
                this,
                R.style.TimePickerTheme,
                myListener,
                year,
                month,
                day);

        dateDialog.setTitle("What day is the event on?");
        dateDialog.getDatePicker().setMinDate(System.currentTimeMillis() - 1000);

        return dateDialog;
    }

    //Creates the TimePickerDialog
    public Dialog createTimeDialog(final TextView myTextView){
        //Creating a new calendar instance with the device's current date and time
        final Calendar c = Calendar.getInstance();

        //Pulling the data from said instance and using it to pre-set the dialog picker
        int hour = c.get(Calendar.HOUR_OF_DAY);
        int min = c.get(Calendar.MINUTE);

        //Defining OnTimeSetListener
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
                myTextView.setText(String.format("%2d:%02d" + AM_PM, hourOfDay, minute));

            }
        };//End OnTimeSetListener

        //This is the actual dialog that will pop up when a user initiates a TimePicker event
        TimePickerDialog timePickerDialog = new TimePickerDialog(
                this, //Defining the context
                R.style.TimePickerTheme, //Setting the theme
                myListener, //OnTimeSetListener
                hour,
                min,
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
            mIntent.putExtra("pos", position);

            setResult(123, mIntent);
            finish();
        }

    }

    public void deleteEvent(View v){
        Intent delIntent = new Intent(EventData.this, MainScreenActivity.class);
        delIntent.putExtra("pos", position);
        setResult(111, delIntent);
        finish();
    }

}