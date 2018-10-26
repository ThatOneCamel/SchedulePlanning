package efx.com.GroupLink;


import android.os.Bundle;
import androidx.fragment.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;


/**
 * A simple {@link Fragment} subclass.
 */
public class BlankFragment extends Fragment {

    //Making references to the fragment's individual objects
    /*private TextView hourDisplay, timeDisplay, descriptionDisplay;
    private ImageView bgImage;
    public static int counter = 0;
    private int myFragmentID;*/


    public BlankFragment() {
        // Required empty public constructor
    }

    //Sets the leftmost time, the hour display
    /*public void setHourOfEvent(int time){
        if (time > 24 || time < 0){
            Toast.makeText(this.getContext(), "DEBUG: Hour is invalid; check setHourOfEvent()", Toast.LENGTH_SHORT).show();

        } else if (time <= 12){
            //Sets the TextView to display the input hour in terms of AM
            //Locale.ENGLISH ensures that the string, when translated for other languages, will not change
            hourDisplay.setText(String.format(Locale.ENGLISH, "%d am", time));
        } else {
            //Sets the TextView to display the input hour in terms of PM
            hourDisplay.setText(String.format(Locale.ENGLISH, "%d pm", time));

        }
    }*/

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        //myFragmentID = counter;
        //counter++;

        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_blank, container, false);
    }

    /*public void assignViews(){
        hourDisplay = getView().findViewById(R.id.hourOfDay);
        timeDisplay = getView().findViewById(R.id.fragTime);
        descriptionDisplay = getView().findViewById(R.id.fragDesc);
        bgImage = getView().findViewById(R.id.fragBackground);
    }*/

}
