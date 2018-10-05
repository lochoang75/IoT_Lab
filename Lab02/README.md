# Lab description #
This is lab 2 for IoT, this week we working with UART.
## Requirement ##
The previous assignment includes 5 exercises that detail as follows:
1. Using three pins to control an RGB LED displaying in different colors.
2. Get input from a button and then change the pace of color displaying (ex 1). For
example, the RGB LED changes colors in 2s by default. After a button is pressed, the rate
will change to 1s, then 0.5s, 0.1s and back to 2s.
3. Similar to exercise 1, control the RGB LED by using PWM to change the brightness of
the led. Please read this link for more details of PWM:
https://developer.android.com/things/sdk/pio/pwm
4. Get input from a button and change the brightness of each color of the RGB LED. For
example, the RGB LED changes the brightness of red, green, blue by default. After a
button is pressed, only the red one is changing its brightness, then green, blue and back
to three colors.
5. Blink each LED in different paces. The red LED is blinking every 0.5s, the green is 2s,
and the blue is 3s.
Your task in this assignment is to implement an app that receives commands from UART
and run corresponding exercises above. The commands are detailed as follows:
a. ‘O’: App starts ready to receive commands.
b. ‘1’: App runs exercises 1.
c. ‘2’: App runs exercises 2.
d. ‘3’: App runs exercises 3.
e. ‘4’: App runs exercises 4.
f. ‘5’: App runs exercises 5.
g. ‘F’: App stops any running
## Pin map ##
* Pin map for RGB led
```
BCM5 - Red pin
BCM6 - Green pin
BCM26 - Blue pin
PWM 0 - I pin
```
* Pin map for UART
```
BCM14 (TX) - RX
BCM15 (RX) - TX
Ground - GND
```
## Result ##
We were demo at laboratory.
