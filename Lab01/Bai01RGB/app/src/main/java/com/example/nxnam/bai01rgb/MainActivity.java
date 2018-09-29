package com.example.nxnam.bai01rgb;

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

import static java.lang.Math.pow;

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

    private static final int INTERVAL_BETWEEN_STEPS_MS = 1000;
    private Handler mHandler = new Handler();

    private Gpio[] mGpio = new Gpio[3];
    private static int mGpioState = 0;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Log.i(TAG, "Starting PwmActivity");

        try {
            mGpio[0] = PeripheralManager.getInstance().openGpio("BCM5");
            mGpio[1] = PeripheralManager.getInstance().openGpio("BCM6");
            mGpio[2] = PeripheralManager.getInstance().openGpio("BCM26");

            Log.d(TAG, "Start changing BCM pin mode");

            for (int i = 0; i < 3; i++)
            {
                mGpio[i].setDirection(Gpio.DIRECTION_OUT_INITIALLY_LOW);
                Log.d(TAG, "Set Low to mGpio");
            }

            mHandler.post(mChangePWMRunnable);
        } catch (IOException e) {
            Log.e(TAG, "Error on PeripheralIO API", e);
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

            mGpio[0].close();
            mGpio[1].close();
            mGpio[2].close();
        } catch (IOException e) {
            Log.e(TAG, "Error on PeripheralIO API", e);
        } finally {
            mGpio[0] = null;
            mGpio[1] = null;
            mGpio[2] = null;
        }
    }

    private Runnable mChangePWMRunnable = new Runnable() {
        @Override
        public void run() {
            for (int i = 0; i < 3; i++){
                if (mGpio[i] == null){
                    Log.w(TAG, "Stopping runnable since mGpio[" + i + "] is null");
                    return;
                }

            }

            try {
                mGpioState = (mGpioState + 1) % 8;
                Log.i(TAG, "mGpioState is " + mGpioState);

                for (int i = 0; i < 3; i++) {
                    mGpio[i].setValue((mGpioState & ((int)pow(2,i))) == ((int)pow(2,i)));

                    Log.i(TAG, "Set \"" + ((mGpioState & i) == i) + "\" value to mGpio[" + i + "]");
                }
                mHandler.postDelayed(this, INTERVAL_BETWEEN_STEPS_MS);
            } catch(IOException e) {
                Log.i(TAG, "Cant set value to mGpio");
            }

        }
    };

}
