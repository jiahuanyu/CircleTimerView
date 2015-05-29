# CircleTimerView
Circle timer on Android platform. <br />
<img src="./images/sample.gif" width="200px" height="auto" />
<img src="./images/sample2.gif" width="200px" height="auto" />

## System Requirement
Android v2.2+

## TODO
Developed by AndroidStudio

## Usage
```xml
<com.jiahuan.circletimerview.CircleTimerView
    android:id="@+id/ctv"
    android:layout_width="300dp"
    android:layout_height="300dp"
    android:layout_centerInParent="true">
</com.jiahuan.circletimerview.CircleTimerView>
```
Ensure the height equals to the width

## Notice
If you make the targetSdkVersion greater than or equal to 14, please add the following code in the end of the `initialize` method
```java
setLayerType(View.LAYER_TYPE_SOFTWARE, null);
```

## License
Copyright 2015 JiaHuan

Licensed under the Apache License, Version 2.0 (the "License"); you may not use this file except in compliance with the License. You may obtain a copy of the License at

http://www.apache.org/licenses/LICENSE-2.0

Unless required by applicable law or agreed to in writing, software distributed under the License is distributed on an "AS IS" BASIS, WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied. See the License for the specific language governing permissions and limitations under the License.
