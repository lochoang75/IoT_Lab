# Lab Description #
In Lab 1, we working with RGB led, state machine and PWM.
## Requirement ##
1. Using three pins to control an RGB LED displaying in different colors.

2. Get input from a button and then change the pace of color displaying (ex 1). For example, the RGB LED changes colors in 2s by default. After a button is pressed, the rate will change to 1s, then 0.5s, 0.1s and back to 2s. 

3. Similar to exercise 1, control the RGB LED by using PWM to change the brightness of the led. Please read this link for more details of PWM: https://developer.android.com/things/sdk/pio/pwm

4. Get input from a button and change the brightness of each color of the RGB LED. For example, the RGB LED changes the brightness of red, green, blue by default. After a button is pressed, only the red one is changing its brightness, then green, blue and back to three colors.

5. Blink each LED in different paces. The RED LED is blinking every 0.5s, the green is 2s, and the blue is 3s.

## Pin map ##
* For requirement 1 and 2, we connect pin as below:
```
BCM5 - Red pin
BCM6 - Green pin
BCM26 - Blue pin
VCC - I pin
```
* For requirement 3,4,5 we using PWM pin instead of VCC to change brightness of RGB led.
## Result ##
We were demo in laboratory.

## Problem ##
We try to use PWM 1 with onscreen button made PWM working with wrong status.

