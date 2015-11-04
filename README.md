# AndroidMapView for ReactNative

## Installation

#### Step 1

*android/settings.gradle*

```
...
include ':com.lugg.ReactMapView', ':app'
project(':com.lugg.ReactMapView').projectDir = new File(rootProject.projectDir, '../node_modules/react-native-android-map-view/android')
```

#### Step 2
*android/app/build.gradle*

```
dependencies {
    ...
    compile project(':com.lugg.ReactMapView')
}
```

#### Step 3

*android/app/src/main/...../MainActivity.java*

```
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

#### Step 4

*android/app/src/main/AndroidManifest.xml*

```
<uses-library android:name="com.google.android.maps" />
<meta-data
    android:name="com.google.android.geo.API_KEY"
    android:value="YOUR_API_KEY"/>
```
