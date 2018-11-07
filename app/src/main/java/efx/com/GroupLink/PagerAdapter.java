package efx.com.GroupLink;

import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentStatePagerAdapter;

public class PagerAdapter extends FragmentStatePagerAdapter {

    private int count;

    //Constructor
    public PagerAdapter(FragmentManager manager){
        super(manager);
        count = 1;
    }


    @Override
    //Gets all existing items
    public Fragment getItem(int position) {
        switch (position){
            case 0: return new UserTab();
            case 1: return new BlankFragment();
            default: return new BlankFragment();
            //case 2: return new UserTab();
        }
        //return null;
    }


    @Override
    public int getCount() {
        return count;
    }

    @Override
    public CharSequence getPageTitle(int position) {
        switch (position){
            case 0:
                return "Main";
            case 1:
                return "Debug";
            default:
                return "New Group";
        }
    }

    public void addTabItem(){
        count++;
        notifyDataSetChanged();
    }



}
