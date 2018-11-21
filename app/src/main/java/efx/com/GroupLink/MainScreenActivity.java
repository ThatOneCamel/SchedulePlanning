package efx.com.GroupLink;

import androidx.appcompat.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.util.Log;
import android.widget.ImageButton;

import androidx.viewpager.widget.ViewPager;
import com.google.android.material.tabs.TabLayout;

import java.util.ArrayList;

public class MainScreenActivity extends AppCompatActivity {

    static TabLayout mTabLayout;
    static PagerAdapter myPagerAdapter;
    static ArrayList<GroupInfo> userGroups;
    ViewPager mViewPager;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main_screen);

        final ImageButton memberListBtn = findViewById(R.id.membersBtn);

        userGroups = new ArrayList<>();

        mViewPager = findViewById(R.id.mainPager);
        myPagerAdapter = new PagerAdapter(getSupportFragmentManager());
        mViewPager.setAdapter(myPagerAdapter);
        mViewPager.setOffscreenPageLimit(3);
        mTabLayout = findViewById(R.id.mainTab);
        mTabLayout.setupWithViewPager(mViewPager);


        mTabLayout.addOnTabSelectedListener(new TabLayout.OnTabSelectedListener() {
            @Override
            public void onTabSelected(TabLayout.Tab tab) {
                mViewPager.setCurrentItem(tab.getPosition());
                Log.i("TAB OPENED", "Tab" + tab.getPosition());
                if (tab.getPosition() > 0)
                    memberListBtn.setVisibility(View.VISIBLE);
                else
                    memberListBtn.setVisibility(View.INVISIBLE);

            }

            @Override
            public void onTabUnselected(TabLayout.Tab tab) {

            }

            @Override
            public void onTabReselected(TabLayout.Tab tab) {

            }
        });

    }

    public void goHome(View v){
        mViewPager.setCurrentItem(0);
    }

}
