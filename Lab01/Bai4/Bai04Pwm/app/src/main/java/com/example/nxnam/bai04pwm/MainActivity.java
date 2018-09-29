package com.example.nxnam.bai04pwm;

import android.app.Activity;
import android.os.Bundle;
import android.os.Handler;
import android.util.Log;
import android.view.View;
import android.widget.Button;

import com.google.android.things.pio.Gpio;
import com.google.android.things.pio.PeripheralManager;
import com.google.android.things.pio.Pwm;

import java.io.IOException;

/**
 * Skeleton of an Android Things activity.
 * <p>
 * Android Things peripheral APIs are accessible through the class
 * PeripheralManagerService. For example, the snippet below will open a GPIO pin and
 * set it to HIGH:
 *
 * <pre>{@code
 * PeripheralManagerService service = new PeripheralManagerService();
 * mLedGpio = service.openGpio("BCM6");
 * mLedGpio.setDirection(Gpio.DIRECTION_OUT_INITIALLY_LOW);
 * mLedGpio.setValue(true);
 * }</pre>
 * <p>
 * For more complex peripherals, look for an existing user-space driver, or implement one if none
 * is available.
 *
 * @see <a href="https://github.com/androidthings/contrib-drivers#readme">https://github.com/androidthings/contrib-drivers#readme</a>
 */
public class MainActivity extends Activity {

    private static final String TAG = MainActivity.class.getSimpleName();

    // Parameters of the servo PWM
    private static final double MIN_ACTIVE_PULSE_DURATION_MS = 0;
    private static final double MAX_ACTIVE_PULSE_DURATION_MS = 2;
    private static final double PULSE_PERIOD_MS = 10;  // Frequency of 50Hz (1000/20)

    // Parameters for the servo movement over time
    private static final double PULSE_CHANGE_PER_STEP_MS = 0.1;
    private static final int INTERVAL_BETWEEN_STEPS_MS = 100;

    private Handler mHandler = new Handler();
    private Pwm mPwm;
    private boolean mIsPulseIncreasing = true;
    private double mActivePulseDuration;
    private Button mButton;


    private Gpio[] mGpio = new Gpio[3];
    private static int mActivePin = 3;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Log.i(TAG, "Starting PwmActivity");
        setContentView(R.layout.activity_main);

        mButton = findViewById(R.id._mBtn);
        mButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ChangeActivePin();
            }
        });
        try {
            String pinName = BoardDefaults.getPWMPort();
            mActivePulseDuration = MAX_ACTIVE_PULSE_DURATION_MS;

            mPwm = PeripheralManager.getInstance().openPwm(pinName);

            // Always set frequency and initial duty cycle before enabling PWM
            mPwm.setPwmFrequencyHz(1000 / PULSE_PERIOD_MS);
            mPwm.setPwmDutyCycle(mActivePulseDuration);
            mPwm.setEnabled(true);

            // Post a Runnable that continuously change PWM pulse width, effectively changing the
            // servo position
            Log.d(TAG, "Start changing PWM pulse");


            mGpio[0] = PeripheralManager.getInstance().openGpio("BCM5");
            mGpio[1] = PeripheralManager.getInstance().openGpio("BCM6");
            mGpio[2] = PeripheralManager.getInstance().openGpio("BCM26");

            Log.d(TAG, "Start changing BCM pin mode");

            for (int i = 0; i < 3; i++)
            {
                mGpio[i].setDirection(Gpio.DIRECTION_OUT_INITIALLY_LOW);
            }

            mHandler.post(mChangePWMRunnable);
        } catch (IOException e) {
            Log.e(TAG, "Error on PeripheralIO API", e);
        }
    }

    private void ChangeActivePin() {
        mActivePin = (mActivePin + 1) % 4;
        mActivePulseDuration = MAX_ACTIVE_PULSE_DURATION_MS;
        if (3 == mActivePin){
            for (int i = 0; i < 3; i++)
            {
                try {
                    mGpio[i].setValue(false);
                } catch (IOException e) {
                    Log.w(TAG, "Stopping runnable since cant setValue to mGpio");
                }
            }
            mIsPulseIncreasing = true;
        }
        else {
            for(int i = 0; i < 3; i++)
            {
                try {
                    mGpio[i].setValue(i != mActivePin);
                    Log.w(TAG, "Gpio[" + i + "] is " + (i != mActivePin) + " " + mActivePin);
                } catch (IOException e) {
                    Log.w(TAG, "Stopping runnable since cant setValue to mGpio");
                }
            }
            mIsPulseIncreasing = false;
        }


    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        // Remove pending Runnable from the handler.
        mHandler.removeCallbacks(mChangePWMRunnable);
        // Close the PWM port.
        Log.i(TAG, "Closing port");
        try {
            mPwm.close();

            mGpio[0].close();
            mGpio[1].close();
            mGpio[2].close();
        } catch (IOException e) {
            Log.e(TAG, "Error on PeripheralIO API", e);
        } finally {
            mPwm = null;
            mGpio[0] = null;
            mGpio[1] = null;
            mGpio[2] = null;
        }
    }

    private Runnable mChangePWMRunnable = new Runnable() {
        @Override
        public void run() {
            // Exit Runnable if the port is already closed
            if (mPwm == null) {
                Log.w(TAG, "Stopping runnable since mPwm is null");
                return;
            }
            for (int i = 0; i < 3; i++){
                if (mGpio[i] == null){
                    Log.w(TAG, "Stopping runnable since mGpio[" + i + "] is null");
                    return;
                }

            }
            // Change the duration of the active PWM pulse, but keep it between the minimum and
            // maximum limits.
            // The direction of the change depends on the mIsPulseIncreasing variable, so the pulse
            // will bounce from MIN to MAX.
            if (mIsPulseIncreasing) {
                mActivePulseDuration += PULSE_CHANGE_PER_STEP_MS;
            } else {
                mActivePulseDuration -= PULSE_CHANGE_PER_STEP_MS;
            }

            // Bounce mActivePulseDuration back from the limits
            if (mActivePulseDuration > MAX_ACTIVE_PULSE_DURATION_MS) {
                mActivePulseDuration = MAX_ACTIVE_PULSE_DURATION_MS;
                //mIsPulseIncreasing = !mIsPulseIncreasing;
            } else if (mActivePulseDuration < MIN_ACTIVE_PULSE_DURATION_MS) {
                mActivePulseDuration = MIN_ACTIVE_PULSE_DURATION_MS;
                //mIsPulseIncreasing = !mIsPulseIncreasing;
            }

            Log.d(TAG, "Changing PWM active pulse duration to " + mActivePulseDuration + " ms");

            try {

                // Duty cycle is the percentage of active (on) pulse over the total duration of the
                // PWM pulse
                mPwm.setPwmDutyCycle(100 * mActivePulseDuration / PULSE_PERIOD_MS);

                // Reschedule the same runnable in {@link #INTERVAL_BETWEEN_STEPS_MS} milliseconds
                mHandler.postDelayed(this, INTERVAL_BETWEEN_STEPS_MS);
            } catch (IOException e) {
                Log.e(TAG, "Error on PeripheralIO API", e);
            }
        }
    };

}
