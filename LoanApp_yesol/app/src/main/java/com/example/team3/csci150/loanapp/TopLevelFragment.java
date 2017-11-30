package com.example.team3.csci150.loanapp;

/**
 * Created by yesol on 10/9/2017.
 */

import android.view.KeyEvent;

/**
 * fragments that are added directly to the activity
 *
 * Only one fragment can be the top level fragment
 */

public class TopLevelFragment extends BaseFragment{

    @Override
    protected boolean isToplevel() {
        return true;
    }

    /**
     * return true if the fragment uses full screen
     * or, return false
     */
    protected boolean isFullScreen() {
        return false;
    }

    @Override
    public void onResume() {
        super.onResume();
    }

}
