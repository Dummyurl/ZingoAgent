package app.zingo.com.zingoagent.Fragments;

import android.app.Fragment;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import app.zingo.com.zingoagent.R;

/**
 * Created by CSC on 1/5/2018.
 */

public class ProfileInfoFragment extends Fragment{

    public ProfileInfoFragment(){

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstance){

        View v = inflater.inflate(R.layout.enter_profile_layout, container, false);

        return v;
    }

}
