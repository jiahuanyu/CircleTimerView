package com.github.jiahuanyu.circletimerview.sample;

import android.os.Bundle;
import android.support.v7.app.ActionBarActivity;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

import com.github.jiahuanyu.circletimerview.CircleTimerView;


public class MainActivity extends ActionBarActivity implements CircleTimerView.CircleTimerListener
{

    private static final String TAG = MainActivity.class.getSimpleName();

    private CircleTimerView mTimer;
    private EditText mTimerSet;
    private EditText mHintSet;

    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        mTimer = (CircleTimerView) findViewById(R.id.ctv);
        mTimer.setCircleTimerListener(this);
        mTimerSet = (EditText) findViewById(R.id.time_set_et);
        mHintSet = (EditText) findViewById(R.id.hint_set_et);
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

    public void setTime(View v)
    {
        try
        {
            mTimer.setCurrentTime(Integer.parseInt(mTimerSet.getText().toString()));
        }
        catch (NumberFormatException e)
        {
            e.printStackTrace();
        }
    }

    public void setHint(View v)
    {
        mTimer.setHintText(mHintSet.getText().toString());
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
    public void onTimerTimingValueChanged(int time)
    {
        Log.d(TAG, "onTimerTimingValueChanged");
    }

    @Override
    public void onTimerSetValueChanged(int time)
    {
        Log.d(TAG, "onTimerSetValueChanged");
    }

    @Override
    public void onTimerSetValueChange(int time)
    {
        Log.d(TAG, "onTimerSetValueChange");
    }
}
