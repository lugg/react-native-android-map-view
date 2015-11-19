# MapViewAndroid for ReactNative

This is very much a work in progress.

## Usage

See [index.android.js](https://github.com/luggg/react-native-android-map-view/blob/master/index.android.js)

See [Example/index.android.js](https://github.com/luggg/react-native-android-map-view/blob/master/Example/index.android.js)

---

## Setup

#### Step 1

Include this module in `android/settings.gradle`:

```java
...
include ':com.lugg.ReactMapView', ':app'
project(':com.lugg.ReactMapView').projectDir = new File(rootProject.projectDir, '../node_modules/react-native-android-map-view/android')
```

#### Step 2

Add a dependency to your app build in `android/app/build.gradle`:

```java
dependencies {
    ...
    compile project(':com.lugg.ReactMapView')
}
```

#### Step 3

Change your main activity to add a new package, in `android/app/src/main/.../MainActivity.java`:

```java
...
import com.lugg.ReactMapView.ReactMapViewPackage;
...

public class MainActivity extends Activity implements DefaultHardwareBackBtnHandler {

    private ReactInstanceManager mReactInstanceManager;
    private ReactRootView mReactRootView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mReactRootView = new ReactRootView(this);

        mReactInstanceManager = ReactInstanceManager.builder()
                .setApplication(getApplication())
                .setBundleAssetName("index.android.bundle")
                .setJSMainModuleName("index.android")
                .addPackage(new MainReactPackage())
                .addPackage(new ReactMapViewPackage()) <-- Register package here
                .setUseDeveloperSupport(BuildConfig.DEBUG)
                .setInitialLifecycleState(LifecycleState.RESUMED)
                .build();

```
#### To build and run the example application add your Google Maps API key.

```shell
$ mv Example/android/app/src/main/res/values/strings.xml.example Example/android/app/src/main/res/values/strings.xml
```

```xml
<resources>
    <string name="app_name">Example</string>
    <string name="google_maps_key" translatable="false" templateMergeStrategy="preserve">
      YOUR_GOOGLE_MAPS_API_KEY
    </string>
</resources>
```
