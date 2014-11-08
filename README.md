AnimatedProgressBar
===================
A ProgressBar that animates smoothly

![](animation.gif)

#Usage

####[Download Sample App](https://github.com/anthonycr/AnimatedProgressBar/releases/download/1.0.0/release.apk)

####XML Usage
````
xmlns:custom="http://schemas.android.com/apk/res-auto"

<anthonycr.animatedprogressbar.AnimatedProgressBar
        android:id="@+id/progress_view"
        android:layout_width="match_parent"
        android:layout_height="5dp"
        custom:backgroundColor="#424242"
        custom:bidirectionalAnimate="true"
        custom:progressColor="#2196f3" />
````

**backgroundColor:** set the background color of the AnimatedProgressBar, use #00000000 to make it invisible.

**progressColor:** set the progress color of the AnimatedProgressBar.

**bidirectionalAnimate:** set to true to have it animate up and down, set it to false to only have it animate up.

####Java Usage
````
int progressNum = 50;
progressBar = (AnimatedProgressBar) findViewById(R.id.progress_view);
progressBar.setProgress(50);
progressNum = progressBar.getProgres();
````

**void setProgress(int number):** a number between 0 and 100 that sets the progress of the view. If you set it out of these bounds, the view will set it to the closest bound, i.e. setting progress to 150 will correct it to 100.

**int getProgress():** returns an integer of the view's progress between 0 and 100.

####Setting it up

* Download the files in the library folder
* Add the **AnimatedProgressBar.java** file to your java folder
* Add the **attrs.xml** to your /res/values folder, or add its contents to your current attrs.xml file
* Add the **animated_progress_bar.xml** file to your /res/layout folder
* Use the library as is shown above in the XML Usage and Java Usage sections

####License

````
Copyright 2014 Anthony Restaino

   Licensed under the Apache License, Version 2.0 (the "License");
   you may not use this file except in compliance with the License.
   You may obtain a copy of the License at

       http://www.apache.org/licenses/LICENSE-2.0

   Unless required by applicable law or agreed to in writing, software
   distributed under the License is distributed on an "AS IS" BASIS,
   WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
   See the License for the specific language governing permissions and
   limitations under the License.
   ````
