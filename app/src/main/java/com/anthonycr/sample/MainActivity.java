package com.anthonycr.sample;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.Menu;
import android.view.MenuItem;

import com.anthonycr.progress.AnimatedProgressBar;

import java.util.Random;


public class MainActivity extends AppCompatActivity {

    private AnimatedProgressBar mProgressBar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        getSupportActionBar().show();
        mProgressBar = (AnimatedProgressBar) findViewById(R.id.progress_view);
        // Initialize the progress bar to 50/100
        mProgressBar.setProgress(50);
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
