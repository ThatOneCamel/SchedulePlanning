package efx.com.GroupLink;

import android.os.Bundle;
import android.view.ViewGroup;

import java.util.ArrayList;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentStatePagerAdapter;

public class PagerAdapter extends FragmentStatePagerAdapter {

    private int count;
    private ArrayList<String> groups;
    private ArrayList<GroupTab> registeredGroups;

    //Constructor
    public PagerAdapter(FragmentManager manager){
        super(manager);
        count = 1;
        groups = new ArrayList<>();
        registeredGroups = new ArrayList<>();
    }

    @Override
    //Gets all existing items
    public Fragment getItem(int position) {
        switch (position){
            case 0: return new UserTab();
            default: return new GroupTab();
        }
        //return null;
    }

    @Override
    public Object instantiateItem(@NonNull ViewGroup container, int position) {
        Fragment fragment = (Fragment) super.instantiateItem(container, position);

        if(position != 0){
            registeredGroups.add( (GroupTab) fragment);
            registeredGroups.get(position - 1).setup(getPageTitle(position).toString(), position);
        }
        return fragment;
    }

    public GroupTab getGroup(int pos){
        if (pos <= 0){
            return null;
        } else {
            return registeredGroups.get(pos);
        }

    }


    @Override
    public int getCount() { return count; }

    @Override
    public CharSequence getPageTitle(int position) {
        switch (position){
            case 0:
                return "Main";
            default:
                return groups.get(position - 1);
        }
    }

    public void addTabItem(String groupName){
        count++;
        groups.add(groupName);
        notifyDataSetChanged();
    }

}
