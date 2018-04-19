package com.almaviva.euregio.helper;

/**
 * Created by ascia on 19/04/2018.
 */

import android.os.SystemClock;
import android.support.v7.widget.SearchView;
import android.util.Log;


public abstract class DebouncedQueryTextListener implements SearchView.OnQueryTextListener {

    private static final String TAG = "DebouncedOnQueryTextLis";
    private static final long THRESHOLD_MILLIS = 1000L;
    private long lastClickMillis;
    private int calls = 0;

    protected DebouncedQueryTextListener() {

    }

    public abstract void onQueryDebounce(String text);

    @Override
    public boolean onQueryTextSubmit(String query) {
        return true;
    }

    @Override
    public boolean onQueryTextChange(String newText) {
        long now = SystemClock.elapsedRealtime();
        if (now - lastClickMillis > THRESHOLD_MILLIS) {
            calls += 1;
            onQueryDebounce(newText);
            Log.d(TAG, "onQueryTextChange: " + calls);
        }
        lastClickMillis = now;
        return true;
    }
}