LeJOS NXT Image Converter is an image converting tool which can convert general format images into LeJOS NXJ source code or LeJOS NXT format images.

---

# Introduction #
**LeJOS NXT Image Converter** is developed by Java SE. It is not depend on LeJOS NXJ Libarary.

The PC side image format support lie on the settings of your Java SE Runtime Environment. It read PC images just using standard [ImageIO](http://java.sun.com/javase/6/docs/api/javax/imageio/ImageIO.html) class.

# Using Guide #
## Start the Application ##
LeJOS NXT Image Converter is a standard java application.

You can download the executable jar file. If you installed the latest Sun standard JRE (Java Runtime Environment), just double-click the jar file in Windows to run it.

If you prefer the command line, use below command to make it execute.
```
java -jar LeJOSNXTImageConvertor.jar
```

## User Interface ##
![http://nxtprojects.googlecode.com/svn/wiki/NXTImageConverter.attach/UserInterface.gif](http://nxtprojects.googlecode.com/svn/wiki/NXTImageConverter.attach/UserInterface.gif)

  * In the Image Preview Area, you can preview the image converted.
  * In the Code Preview Area, you can preview the LeJOS NXJ Image constructing code. Use Ctrl-A to select all and Ctrl-C to copy. The default focus is always on the Code Preview Area, so use the hotkey anytime you want to copy the code.
  * Threshold Adjust controls allow you to adjust the gray value threshold so that you can get a pure black & white image most clear and close to the original color image.
  * Code Edit/Execute Button allow you to edit the generated Image constructing code and preview the image generated by the code. This is very useful to view the image in others NXJ code without NXT brick.

### Menu Items ###
In the Image menu, there are some menu items:
|Import Image...|You can browse and import any image on your PC by this menu item|
|:--------------|:---------------------------------------------------------------|
|Open LeJOS NXT Image File...|Browse and open a LeJOS NXT format image file. You can find a format specification [here](http://dl.dropbox.com/u/3644101/NXT_Docs/NXTUtils/javadoc/org/programus/nxj/util/ImageUtil.html#readImage(java.io.File)). |
|Export LeJOS NXT Image File...|Export the converted image and save it as an LeJOS NXT Image file. File extension would be lni. |
|Exit           |OK, let's shutdown the tool                                     |

## Using Scenarios ##
  * Import an image stored in your PC and copy the code into your LeJOS NXJ program.
  * Click Edit button to make the code panel editable, and then paste the image constructing code into it and click Execute. You now know what the image is.
  * Save image as lni file and upload it into NXT brick. Use [ImageUtil.readImage()](http://dl.dropbox.com/u/3644101/NXT_Docs/NXTUtils/javadoc/org/programus/nxj/util/ImageUtil.html#readImage(java.io.File)) method to read it and display it on the NXT LCD.

Though, this tool provide a color converter function, I still recommand that you would better adjust your images in some professional image editing softwares and convert them into black & white format, then use this tool just to convert images into codes or NXT image files.


# Download #
  * Executable Jar File - [LeJOSNXTImageConvertor.jar](http://nxtprojects.googlecode.com/files/LeJOSNXTImageConvertor.jar)