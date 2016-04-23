This is a port of Flytron's Compufly to Java for cross platform use. There have also been enhancements made to the original to support multiple usb sources. The goal is to make a versatile computer based system to control RC Land, Sea, Aircraft and other projects. Using the software with Flytron's USB2PPM hardware solution to get PPM to a RC transmitter.  Everything is kind of setup for my CH Products set up with joystick throttle and rudder at the moment.
The Current version is set up for Windows with a batch file to launch. we are working on getting that into a universal .jar

### To get started ###
  * Run start.bat for x86
  * Run startx64.bat for x64

### Requirements: ###
  * Compufly USB2PPM: http://flytron.com/programmers/56-compufly-usbtoppm-converter.html
  * Windows XP/Vista/7  USB drivers : http://www.flytron.com/pdf/CP210x_VCP_Win_XP_S2K3_Vista_7.zip
  * Works with v1 and v2
### DirectX Joystick Driver Information for Windows Vista and Windows 7 users ###
Microsoft removed the DirectX Joystick Driver DLL(dx8vb.dll) from the windows core on these versions. If you using these versions you have to register the driver DLL by your self.
### Registering DX8VB.DLL ###
  * Copy dx8vb.dll file from our compufly.zip file to SysWOW64 folder of windows
  * Run the command prompt as "run as administrator"
  * go to SysWOW64 folder on command prompt (type "cd c:windowssyswow64")
  * Type "regsrv32 dx8vb.dll"
  * press enter and see the "succeeded" message.
<img src='http://api.ning.com/files/3oLz0N-C75PcxFO5UM7pyI*ut1y41re-wyo7M1n5NgQGN16jLpC92LeBiZOP4HtmyWeysV1-2W3TgtbBsGfX*Q__/MainScreen.PNG' />

<img src='http://api.ning.com/files/NqftaFr6MjpPKaDKTvDXitNmg4wKpT4zsz1o0E2DcN-Bqoc5*bVzw5N3101XTa8xTeKJAaLdwP5kReemCw*XiA__/Axis.PNG' />

<img src='http://api.ning.com/files/FRI4lmbguwtRxlw3SA1nk2LnYx*RC0SUavYP9KAfZ1f3rsS5jPTyM6lyu*jNccoz8YMUVJjr8VWRhCOmvR*l3g__/Rudder.PNG' />
src="http://api.ning.com/files/wbDzrOckBvM5BbT6yZDWuHqqx9ShedIo7sPTJQMWRl7Xo4fruEvhRyvupZukUEbFyZ5FiStI0cx-N7VuuEoG-w__/Button.PNG"/>

<img src='http://api.ning.com/files/4Txx8zASKEY*VWXjFlbLyVmvn*c7jVeMjW951DJ-08r6BW*3uqZZyN1XV1IlpHFlAXmhpJjDc6JfCgb9LMXMDw__/Throttle.PNG' />

### Note: ###
  * Some if not most info relating to Flytron's Compufly has been copied from Flytron's Website www.flytron.com
  * All original code is from Flytron's Compufly Software

_As with all open source projects a lot of blood, sweat and tears goes into projects.
In my case some money was involved in getting this ported to java if you would like to support this project you can donate to help with buying new hardware to test_


[https://www.paypalobjects.com/en\_US/i/btn/btn\_donateCC\_LG.gif ](https://www.paypal.com/cgi-bin/webscr?cmd=_donations&business=8E9NQQ5BWFJ76&lc=US&item_name=joystick%2dto%2dppm&currency_code=USD&bn=PP%2dDonationsBF%3abtn_donateCC_LG%2egif%3aNonHosted)



