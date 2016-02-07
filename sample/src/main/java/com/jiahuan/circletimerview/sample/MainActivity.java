package com.jiahuan.circletimerview.sample;

import android.os.Bundle;
import android.support.v7.app.ActionBarActivity;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Toast;

import com.jiahuan.circletimerview.CircleTimerView;


public class MainActivity extends ActionBarActivity implements CircleTimerView.CircleTimerListener
{

    private static final String TAG = MainActivity.class.getSimpleName();

    private CircleTimerView mTimer;

    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        mTimer = (CircleTimerView) findViewById(R.id.ctv);
        mTimer.setCircleTimerListener(this);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu)
    {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item)
    {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings)
        {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    public void start(View v)
    {
        mTimer.startTimer();
    }

    public void pause(View v)
    {
        mTimer.pauseTimer();
    }

    @Override
    public void onTimerStop()
    {
        Toast.makeText(this, "onTimerStop", Toast.LENGTH_LONG).show();
    }

    @Override
    public void onTimerStart(int currentTime)
    {
        Toast.makeText(this, "onTimerStart", Toast.LENGTH_LONG).show();
    }

    @Override
    public void onTimerPause(int currentTime)
    {
        Toast.makeText(this, "onTimerPause", Toast.LENGTH_LONG).show();
    }

    @Override
    public void onTimerValueChanged(int time)
    {
        Log.d(TAG, "onTimerValueChanged");
    }

    @Override
    public void onTimerValueChange(int time)
    {
        Log.d(TAG, "onTimerValueChange");
    }
}
