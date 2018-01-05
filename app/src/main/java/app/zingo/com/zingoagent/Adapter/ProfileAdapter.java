package app.zingo.com.zingoagent.Adapter;


import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentStatePagerAdapter;

import app.zingo.com.zingoagent.Fragments.DocumentFragment;
import app.zingo.com.zingoagent.Fragments.ProfileInfoFragment;
import app.zingo.com.zingoagent.Profile;


/**
 * Created by CSC on 1/5/2018.
 */

public class ProfileAdapter extends FragmentStatePagerAdapter {

    //String[] tabTitles = {"New Booking","Upcoming","All","Cancelled","Blocked Rooms"};
    String[] tabTitles = {"My Profile","My Documents"};

    public ProfileAdapter(FragmentManager fm)
    {
        super(fm);

    }

    @Override
    public Fragment getItem(int position) {
        // getItem is called to instantiate the fragment for the given page.
        // Return a PlaceholderFragment (defined as a static inner class below).
        return Profile.PlaceholderFragment.newInstance(position + 1);
    }

    @Override
    public int getCount() {
        // Show 3 total pages.
        return 2;
    }

    @Override
    public CharSequence getPageTitle(int position) {
        switch (position) {
            case 0:
                return "My Profile";
            case 1:
                return "My Documents";
        }
        return null;
    }
}
