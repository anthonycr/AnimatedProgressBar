package com.anthonycr.sample;

import android.app.Activity;
import android.os.Build;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.ViewTreeObserver;

import com.anthonycr.progress.AnimatedProgressBar;

import java.util.Random;


public class MainActivity extends AppCompatActivity {

    private static final String TAG = MainActivity.class.getSimpleName();

    private AnimatedProgressBar mProgressBar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        getSupportActionBar().show();
        mProgressBar = (AnimatedProgressBar) findViewById(R.id.progress_view);

        ViewTreeObserver observer = mProgressBar.getViewTreeObserver();
        observer.addOnGlobalLayoutListener(new ViewTreeObserver.OnGlobalLayoutListener() {
            @Override
            public void onGlobalLayout() {
                mProgressBar.setProgress(50);               // Initially set the progress halfway
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.JELLY_BEAN) {
                    mProgressBar.getViewTreeObserver().removeOnGlobalLayoutListener(this);
                } else {
                    //noinspection deprecation
                    mProgressBar.getViewTreeObserver().removeGlobalOnLayoutListener(this);
                }
                Log.i(TAG, "Current Progress: " + mProgressBar.getProgress());
            }
        });


    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {

        int id = item.getItemId();

        if (id == R.id.action_random) {
            Random m = new Random();
            mProgressBar.setProgress(m.nextInt(100));   // Set the progress of the AnimatedProgressBar
            // Here we use
            return true;
        }

        return super.onOptionsItemSelected(item);
    }
}
