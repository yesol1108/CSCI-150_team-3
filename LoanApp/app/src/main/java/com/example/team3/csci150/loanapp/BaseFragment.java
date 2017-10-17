package com.example.team3.csci150.loanapp;

import android.app.Fragment;

/**
 * Created by yesol on 10/9/2017.
 */

public class BaseFragment extends Fragment {

    public String getName() {
        return null;
    }

    public MainActivity getMainActivity() {
        return (MainActivity) getActivity();
    }

    /**
     * return true if it's top level
     * or return false
     */
    protected boolean isToplevel() {
        return false;
    }

}
