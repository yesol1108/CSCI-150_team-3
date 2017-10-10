package com.example.team3.csci150.loanapp;

import android.app.Activity;
import android.app.Fragment;
import android.app.FragmentManager;
import android.app.FragmentTransaction;
import android.content.Context;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.widget.Toast;

public class MainActivity extends Activity {
    @Override
    protected void onPause() {
        super.onPause();
    }

    @Override
    protected void onResume() {
        super.onResume();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        getFragmentManager().addOnBackStackChangedListener(backStackChangedListener());
        //first init
        //if first time user
       initToplevel(CreateAccountFragment.newInstance());
    }

    private FragmentManager.OnBackStackChangedListener backStackChangedListener() {
        FragmentManager.OnBackStackChangedListener result = new FragmentManager.OnBackStackChangedListener() {
            public void onBackStackChanged() {
                TopLevelFragment fragment = getBackStackFragment(0);
                if (fragment != null) {
                    fragment.onResume();
                }
            }
        };

        return result;
    }

    /**
     * initialize the top level frgament
     */
    private void initToplevel(TopLevelFragment toplevel) {
        FragmentTransaction ft = getFragmentManager().beginTransaction();
        ft.add(R.id.layoutRoot, toplevel, toplevel.getName());
        ft.addToBackStack(toplevel.getName());
        ft.commit();
    }


    void pushToplevel(TopLevelFragment toplevel) {
        if(!isNetWork()) return;

        FragmentTransaction ft = getFragmentManager().beginTransaction();
        ft.setTransition(FragmentTransaction.TRANSIT_FRAGMENT_OPEN);
        ft.add(R.id.layoutRoot, toplevel, toplevel.getName());
        ft.addToBackStack(toplevel.getName());
        ft.commit();
    }

    private TopLevelFragment getBackStackFragment(int depth) {
        FragmentManager fm = getFragmentManager();
        for(int i = 0; i < fm.getBackStackEntryCount(); i++){
            FragmentManager.BackStackEntry backStackEntry = fm.getBackStackEntryAt(i);
        }
        if (fm.getBackStackEntryCount() - 1 - depth >= 0) {
            int index = fm.getBackStackEntryCount() - 1 - depth;
            FragmentManager.BackStackEntry backStackEntry = fm.getBackStackEntryAt(index);
            String fragmentTag = backStackEntry.getName();
            TopLevelFragment currentFragment = (TopLevelFragment) fm.findFragmentByTag(fragmentTag);
            return currentFragment;
        }

        return null;
    }

    /**
     * replace the top level fragment, clear back stack
     */
    void resetToplevel(TopLevelFragment toplevel) {
        FragmentManager fm = getFragmentManager();
        for(int i = 0; i < fm.getBackStackEntryCount(); ++i) {
            fm.popBackStackImmediate(fm.getBackStackEntryAt(0).getId(), FragmentManager.POP_BACK_STACK_INCLUSIVE);
        }
        FragmentTransaction ft = getFragmentManager().beginTransaction();
        ft.replace(R.id.layoutRoot, toplevel, toplevel.getName());
        ft.addToBackStack(toplevel.getName());
        ft.commit();
    }

    public Boolean isNetWork() {
        ConnectivityManager connectivityManager = (ConnectivityManager) getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo networkInfo = connectivityManager.getActiveNetworkInfo();
        if(networkInfo != null) {
            boolean isMobileAvailable = networkInfo.getType() == ConnectivityManager.TYPE_MOBILE && networkInfo.isConnectedOrConnecting();
            boolean isWifiAvailable = networkInfo.getType() == ConnectivityManager.TYPE_WIFI && networkInfo.isConnectedOrConnecting();
            if (isWifiAvailable || isMobileAvailable) {
                return true;
            }
        }
        Toast.makeText(this, "Please check the internet connection", Toast.LENGTH_SHORT).show();
        return false;
    }
}
