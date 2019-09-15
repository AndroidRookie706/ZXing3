# ZXing3
# How do I use it？

- Add it in your root build.gradle at the end of repositories:
```
	allprojects {
		repositories {
			...
			maven { url 'https://jitpack.io' }
		}
	}
```
- Add the dependency
```
	dependencies {
	        implementation 'com.github.AndroidRookie706:ZXing3:1.0'
	}

```

# Open the camera and scan the qr code
```
public static void openCamera(Activity activity, int request_code)

public static String dispose(Intent data)
```

# Open the gallery and scan the qr code
```
 public static void openImage(Activity activity, int request_code)
 
 public static void identify(Activity context, Intent data, CodeUtils.AnalyzeCallback callback)
```
 
# Generate new qr code
```
public static Bitmap newQRCode(Context context, String text, int logo)

public static Bitmap newQRCode(Context context, String text )
```
