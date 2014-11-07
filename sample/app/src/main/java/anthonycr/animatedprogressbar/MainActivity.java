package anthonycr.animatedprogressbar;

import android.app.Activity;
import android.os.Build;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.ViewTreeObserver;

import java.util.Random;


public class MainActivity extends Activity {

    AnimatedProgressBar mProgressBar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        mProgressBar = (AnimatedProgressBar) findViewById(R.id.progress_view);

        ViewTreeObserver observer = mProgressBar.getViewTreeObserver();
        observer.addOnGlobalLayoutListener(new ViewTreeObserver.OnGlobalLayoutListener() {
            @Override
            public void onGlobalLayout() {
                mProgressBar.setProgress(50);               // Initially set the progress halfway
                int progress = mProgressBar.getProgress();
                if(Build.VERSION.SDK_INT >= Build.VERSION_CODES.JELLY_BEAN){
                    mProgressBar.getViewTreeObserver().removeOnGlobalLayoutListener(this);
                } else {
                    mProgressBar.getViewTreeObserver().removeGlobalOnLayoutListener(this);
                }
            }
        });



    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {

        getMenuInflater().inflate(R.menu.menu_main, menu);
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
