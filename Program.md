This program is made in Java so it is platform independent.  The way it is setup at the moment it has to be launched from a batch script in windows. Currently there is a x86 and x64 version. You need the respective versions of Java installed on your system.

Drivers for Compufly USB2PPM
Windows XP/Vista/7  USB drivers : http://www.flytron.com/pdf/CP210x_VCP_Win_XP_S2K3_Vista_7.zip

Channel resolution v2.0:       2048 (11bit)

Channel resolution v1.0:       1024 (10bit) Old version


## To run ##
Start.bat for x86

Startx64.bat for x64.

It will launch a cmd window and then you should see the program.
Right now it looks very similar to Compufly. You will notice that it has tabs for each usb input device you have hooked up to your computer. From those tabs you can assign each axis to a channel of your choosing. You will also see your buttons laid out and you can assign them to channels.

# Main Window #
## Device Configuration ##
### Select amount of Channels ###
  * urrently only 10 are supported
### Set Negative or Positive PPM ###
### Servo Output ###
  * Current Channel values
  * Invert Channels
  * Trim
  * EPA

### ComPort Settings ###
  * Com Port Selection
  * Flytron v2 Selection

## Device Tabs ##
### Axis ###
You can assign any axis to any channel
  * nly one channel per axis..See Buttons
### Min max Value ###
## Buttons ##
Buttons default value is 100% this acts like a regular ON/OFF button like a switch on your TX
You can assign multiple buttons to a single channel for different actions
You can change the value of the buttons value if needed. This is for more specialized applications
  1. Ex. You want a button 1 to be channel 6 at 0% and button 2 to be channel 6 at 50% and button 3        at 100% this can be 	used to do quick pans to the left or right on a camera mounted to a servo.
  1. Can be used to set mode selections in ardupilot/copter.
  1. Drop bomb bay doors
  1. Trigger landing gear.

## POV HAT ##
Pov HAT has 8 axis positions

  * Currently they always default to center but future versions will not or have an option.