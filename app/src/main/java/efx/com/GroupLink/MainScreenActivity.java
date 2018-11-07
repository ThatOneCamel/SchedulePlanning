package efx.com.GroupLink;

import androidx.appcompat.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.util.Log;

import androidx.viewpager.widget.ViewPager;
import com.google.android.material.tabs.TabLayout;

public class MainScreenActivity extends AppCompatActivity {

    static TabLayout mTabLayout;
    static PagerAdapter myPagerAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main_screen);

        final ViewPager viewPager = findViewById(R.id.mainPager);
        myPagerAdapter = new PagerAdapter(getSupportFragmentManager());
        viewPager.setAdapter(myPagerAdapter);
        mTabLayout = findViewById(R.id.mainTab);
        mTabLayout.setupWithViewPager(viewPager);


        mTabLayout.addOnTabSelectedListener(new TabLayout.OnTabSelectedListener() {
            @Override
            public void onTabSelected(TabLayout.Tab tab) {
                viewPager.setCurrentItem(tab.getPosition());
                Log.i("TAB OPENED", "Tab" + tab.getPosition());


            }

            @Override
            public void onTabUnselected(TabLayout.Tab tab) {

            }

            @Override
            public void onTabReselected(TabLayout.Tab tab) {

            }
        });

    }

    public void openSettings(View v){
        UserTab reference = (UserTab) getSupportFragmentManager().getFragments().get(0);
        reference.startSettingsActivity();
    }

}
