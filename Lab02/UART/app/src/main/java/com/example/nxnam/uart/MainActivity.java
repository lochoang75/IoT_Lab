package com.example.nxnam.uart;

import android.app.Activity;
import android.os.Bundle;
import android.os.Handler;
import android.os.HandlerThread;
import android.util.Log;
import android.view.View;
import android.widget.Button;

import com.google.android.things.pio.Gpio;
import com.google.android.things.pio.PeripheralManager;
import com.google.android.things.pio.Pwm;
import com.google.android.things.pio.UartDevice;
import com.google.android.things.pio.UartDeviceCallback;

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

    private static final String TAG = "LoopbackActivity";

    // UART Configuration Parameters
    private static final int BAUD_RATE = 115200;
    private static final int DATA_BITS = 8;
    private static final int STOP_BITS = 1;

    private static final int CHUNK_SIZE = 512;

    private HandlerThread mInputThread;
    private Handler mInputHandler;

    private UartDevice mLoopbackDevice;

    private boolean isLockedKey = true;

    private int doingExerciseNumber = 0;

    //RGB Led
    private static int INTERVAL_BETWEEN_STEPS_MS = 1000;
    private Handler mHandler = new Handler();

    private Gpio[] mGpio = new Gpio[3];
    private static int mGpioState = 0;

    //Exercise 2
    private Button mButton;
    private int mPaceState = 0;
    private int[] mPaceTime = {2000, 1000, 500, 100};

    // Parameters of the servo PWM
    private static final double MIN_ACTIVE_PULSE_DURATION_MS = 0;
    private static final double MAX_ACTIVE_PULSE_DURATION_MS = 10;
    private static final double PULSE_PERIOD_MS = 20;  // Frequency of 50Hz (1000/20)

    // Parameters for the servo movement over time
    private static final double PULSE_CHANGE_PER_STEP_MS = 0.2;

    //Exercise 3
    private Pwm mPwm;
    private boolean mIsPulseIncreasing = true;
    private double mActivePulseDuration;

    //Exercise 4
    private static int mActivePin = 3;

    //Exercise 5
    private static final int[] INTERVAL_BETWEEN_STEPS_MS_ARR = {1000, 2000, 3000};
    private static boolean mGpioStateArr[] = {false, false, false};
    private static int mGpioCount = 0;
    private static boolean mGpioToggle = false;

    private Runnable mTransferUartRunnable = new Runnable() {
        @Override
        public void run() {
            transferUartData();
        }
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Log.d(TAG, "Loopback Created");
        setContentView(R.layout.activity_main);

        mButton = findViewById(R.id._mBtn);
        mButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                switch (doingExerciseNumber) {
                    case 2:
                        ChangePaceofColor();
                        break;
                    case 4:
                        ChangeActivePin();
                        default:
                            break;
                }
            }
        });

        // Create a background looper thread for I/O
        mInputThread = new HandlerThread("InputThread");
        mInputThread.start();
        mInputHandler = new Handler(mInputThread.getLooper());

        // Attempt to access the UART device
        try {
            openUart(BoardDefaults.getUartName(), BAUD_RATE);
            // Read any initially buffered data

            //Open RBG pin
            mGpio[0] = PeripheralManager.getInstance().openGpio("BCM5");
            mGpio[1] = PeripheralManager.getInstance().openGpio("BCM6");
            mGpio[2] = PeripheralManager.getInstance().openGpio("BCM26");

            Log.d(TAG, "Start changing BCM pin mode");

            for (int i = 0; i < 3; i++)
            {
                mGpio[i].setDirection(Gpio.DIRECTION_OUT_INITIALLY_LOW);
                Log.d(TAG, "Set Low to mGpio");
            }

            mPwm = PeripheralManager.getInstance().openPwm("PWM0");

            mInputHandler.post(mTransferUartRunnable);
            mHandler.post(mChangePWMRunnable);
        } catch (IOException e) {
            Log.e(TAG, "Unable to open UART device", e);
            Log.e(TAG, "Error on PeripheralIO API", e);
        }
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        Log.d(TAG, "Loopback Destroyed");

        // Terminate the worker thread
        if (mInputThread != null) {
            mInputThread.quitSafely();
        }

        // Attempt to close the UART device
        try {
            closeUart();

            mPwm.close();
            mGpio[0].close();
            mGpio[1].close();
            mGpio[2].close();
        } catch (IOException e) {
            Log.e(TAG, "Error closing UART device:", e);
            Log.e(TAG, "Error on PeripheralIO API", e);
        } finally {
            mPwm = null;
            mGpio[0] = null;
            mGpio[1] = null;
            mGpio[2] = null;
        }
    }

    /**
     * Callback invoked when UART receives new incoming data.
     */
    private UartDeviceCallback mCallback = new UartDeviceCallback() {
        @Override
        public boolean onUartDeviceDataAvailable(UartDevice uart) {
            // Queue up a data transfer
            transferUartData();
            //Continue listening for more interrupts
            return true;
        }

        @Override
        public void onUartDeviceError(UartDevice uart, int error) {
            Log.w(TAG, uart + ": Error event " + error);
        }
    };

    /* Private Helper Methods */

    /**
     * Access and configure the requested UART device for 8N1.
     *
     * @param name Name of the UART peripheral device to open.
     * @param baudRate Data transfer rate. Should be a standard UART baud,
     *                 such as 9600, 19200, 38400, 57600, 115200, etc.
     *
     * @throws IOException if an error occurs opening the UART port.
     */
    private void openUart(String name, int baudRate) throws IOException {
        mLoopbackDevice = PeripheralManager.getInstance().openUartDevice(name);
        // Configure the UART
        mLoopbackDevice.setBaudrate(baudRate);
        mLoopbackDevice.setDataSize(DATA_BITS);
        mLoopbackDevice.setParity(UartDevice.PARITY_NONE);
        mLoopbackDevice.setStopBits(STOP_BITS);

        mLoopbackDevice.registerUartDeviceCallback(mInputHandler, mCallback);
    }

    /**
     * Close the UART device connection, if it exists
     */
    private void closeUart() throws IOException {
        if (mLoopbackDevice != null) {
            mLoopbackDevice.unregisterUartDeviceCallback(mCallback);
            try {
                mLoopbackDevice.close();
            } finally {
                mLoopbackDevice = null;
            }
        }
    }

    /**
     * Loop over the contents of the UART RX buffer, transferring each
     * one back to the TX buffer to create a loopback service.
     *
     * Potentially long-running operation. Call from a worker thread.
     */
    private void transferUartData() {
        if (mLoopbackDevice != null) {
            // Loop until there is no more data in the RX buffer.
            try {
                byte[] buffer = new byte[CHUNK_SIZE];
                int read;
                while ((read = mLoopbackDevice.read(buffer, buffer.length)) > 0) {
                    mLoopbackDevice.write(buffer, read);
                    Log.i (TAG, "buffer[0]: " + buffer[0]);
                    DoExercise(buffer[0]);
                }

            } catch (IOException e) {
                Log.w(TAG, "Unable to transfer data over UART", e);
            }
        }
    }

    private void DoExercise(byte key) {
        if (isLockedKey)
        {
            if (key == 'o') {

                isLockedKey = false;
                Log.i (TAG, "Open key for exercise.");
            }
            else {
                Log.i (TAG, "The key is being locked.");
            }
        }
        else {
            switch (key) {
                case 'o':
                    Log.i(TAG, "Hello open key.");
                    break;
                case '1':
                    exerciseOneInit();
                    doingExerciseNumber = 1;
                    Log.i (TAG, "Hello exercise One");
                    break;
                case '2':
                    exerciseTwoInit();
                    doingExerciseNumber = 2;
                    Log.i (TAG, "Hello exercise Two");
                    break;
                case '3':
                    exerciseThreeInit();
                    doingExerciseNumber = 3;
                    Log.i (TAG, "Hello exercise Three");
                    break;
                case '4':
                    exerciseFourInit();
                    doingExerciseNumber = 4;
                    Log.i (TAG, "Hello exercise Four");
                    break;
                case '5':
                    exerciseFiveInit();
                    doingExerciseNumber = 5;
                    Log.i (TAG, "Hello exercise Five");
                    break;
                case 'f':
                    isLockedKey = true;
                    Log.i (TAG, "Delay doing exercise! Lock the key.");
                default:
                    Log.i(TAG, "Hello default key");
            }
        }
    }
    private void exerciseOneInit() {
        INTERVAL_BETWEEN_STEPS_MS = 1000;
        for (int i = 0; i < 3; i++)
        {
            try {
                mGpio[i].setDirection(Gpio.DIRECTION_OUT_INITIALLY_LOW);
            } catch (IOException e) {
                Log.e(TAG, "Error on PeripheralIO API", e);
            }
            Log.d(TAG, "Set Low to mGpio");
        }
        try {
            mPwm.setPwmFrequencyHz(1000 / PULSE_PERIOD_MS);
            mPwm.setPwmDutyCycle(100);
            mPwm.setEnabled(true);

            // Post a Runnable that continuously change PWM pulse width, effectively changing the
            // servo position
            Log.d(TAG, "Start changing PWM pulse");
        } catch (IOException e) {
            Log.e(TAG, "Error on PeripheralIO API", e);
        }
    }

    private void exerciseTwoInit() {
        exerciseOneInit();
    }

    private void exerciseThreeInit() {
        exerciseOneInit();

        INTERVAL_BETWEEN_STEPS_MS = 50;
        mIsPulseIncreasing = true;
        mActivePulseDuration = MIN_ACTIVE_PULSE_DURATION_MS;
    }

    private void exerciseFourInit() {
        exerciseThreeInit();
        mIsPulseIncreasing = true;
        mActivePulseDuration = MAX_ACTIVE_PULSE_DURATION_MS;


    }

    private void exerciseFiveInit() {
        exerciseOneInit();

        mGpioToggle = false;
        mGpioCount = 0;
        for (int i = 0; i < 3; i++)
            mGpioStateArr[i] = false;
        INTERVAL_BETWEEN_STEPS_MS = 100;
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
            switch (doingExerciseNumber)
            {
                case 1:
                case 2:
                    try {
                        mGpioState = (mGpioState + 1) % 8;
                        //Log.i(TAG, "mGpioState is " + mGpioState);

                        for (int i = 0; i < 3; i++) {
                            mGpio[i].setValue((mGpioState & ((int)pow(2,i))) == ((int)pow(2,i)));

                            //Log.i(TAG, "Set \"" + ((mGpioState & i) == i) + "\" value to mGpio[" + i + "]");
                        }
                    } catch(IOException e) {
                        Log.i(TAG, "Cant set value to mGpio");
                    }
                    break;
                case 3:
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
                        mIsPulseIncreasing = !mIsPulseIncreasing;
                    } else if (mActivePulseDuration < MIN_ACTIVE_PULSE_DURATION_MS) {
                        mActivePulseDuration = MIN_ACTIVE_PULSE_DURATION_MS;
                        mIsPulseIncreasing = !mIsPulseIncreasing;
                    }

                    //Log.d(TAG, "Changing PWM active pulse duration to " + mActivePulseDuration + " ms");

                    try {

                        // Duty cycle is the percentage of active (on) pulse over the total duration of the
                        // PWM pulse
                        mPwm.setPwmDutyCycle(100 * mActivePulseDuration / PULSE_PERIOD_MS);
                    } catch (IOException e) {
                        Log.e(TAG, "Error on PeripheralIO API", e);
                    }
                    break;
                case 4:
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

                    //Log.d(TAG, "Changing PWM active pulse duration to " + mActivePulseDuration + " ms");

                    try {

                        // Duty cycle is the percentage of active (on) pulse over the total duration of the
                        // PWM pulse
                        mPwm.setPwmDutyCycle(100 * mActivePulseDuration / PULSE_PERIOD_MS);

                        // Reschedule the same runnable in {@link #INTERVAL_BETWEEN_STEPS_MS} milliseconds
                    } catch (IOException e) {
                        Log.e(TAG, "Error on PeripheralIO API", e);
                    }
                    break;
                case 5:
                    try {
                        mGpioCount = (mGpioCount + 1) % 30;
                        //Log.i(TAG, "mGpioState is " + mGpioCount);

                        for (int i = 0; i < 3; i++) {
                            mGpioToggle = (mGpioCount % (INTERVAL_BETWEEN_STEPS_MS_ARR[i] / INTERVAL_BETWEEN_STEPS_MS)) == 0;
                            if (mGpioToggle){
                                mGpioStateArr[i] = !mGpioStateArr[i];
                            }
                            mGpio[i].setValue(mGpioStateArr[i]);

                            //Log.i(TAG, "Set \"" + ((mGpioCount & i) == i) + "\" value to mGpio[" + i + "]");
                        }
                    } catch(IOException e) {
                        Log.i(TAG, "Cant set value to mGpio");
                    }
                    break;
                default:
                    Log.i(TAG,"Hello Init excercise");
            }
            // Reschedule the same runnable in {@link #INTERVAL_BETWEEN_STEPS_MS} milliseconds
            mHandler.postDelayed(this, INTERVAL_BETWEEN_STEPS_MS);

        }
    };

    private void ChangePaceofColor() {
        mPaceState = (mPaceState + 1) % 4;
        INTERVAL_BETWEEN_STEPS_MS = mPaceTime[mPaceState];
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
}
