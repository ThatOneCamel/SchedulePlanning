package efx.com.GroupLink;

import android.app.Application;
import android.util.Log;

import com.google.firebase.database.FirebaseDatabase;

//This class runs once so we are setting up persistence here
public class ApplicationStart extends Application {
    @Override
    public void onCreate() {
        super.onCreate();
        FirebaseDatabase.getInstance().setPersistenceEnabled(true);
        Log.i("PERSISTENCE", "ENABLED");
    }
}
