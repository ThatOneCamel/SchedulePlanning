package efx.com.GroupLink;

import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentStatePagerAdapter;

public class PagerAdapter extends FragmentStatePagerAdapter {

    //Constructor
    public PagerAdapter(FragmentManager manager){
        super(manager);
    }

    @Override
    //Gets all existing items
    public Fragment getItem(int position) {
        switch (position){
            case 0: return new UserTab();
            case 1: return new BlankFragment();
            //case 2: return new UserTab();
        }
        return null;
    }


    @Override
    public int getCount() {
        return 2;
    }

    @Override
    public CharSequence getPageTitle(int position) {
        switch (position){
            case 0:
                return "Main";
            case 1:
                return "Debug";
            default:
                return null;
        }
    }



}
