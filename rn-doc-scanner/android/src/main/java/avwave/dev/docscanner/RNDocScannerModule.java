package avwave.dev.docscanner;

/**
 * Created by rayar on 4/8/2018.
 */

import android.Manifest;
import android.app.Activity;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.Build;
import android.support.annotation.NonNull;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.PermissionChecker;
import android.util.Log;

import com.facebook.react.bridge.BaseActivityEventListener;
import com.facebook.react.bridge.Promise;
import com.facebook.react.bridge.ReactApplicationContext;
import com.facebook.react.bridge.ReactContextBaseJavaModule;
import com.facebook.react.bridge.ReactMethod;
import com.facebook.react.bridge.Callback;

import java.io.File;
import java.io.IOException;


import devliving.online.cvscanner.CVScanner;
import devliving.online.cvscanner.util.Util;

import static android.app.Activity.RESULT_OK;

public class RNDocScannerModule extends ReactContextBaseJavaModule {

    private final ReactApplicationContext reactContext;

    final int REQ_SCAN = 11;

    private static final int REQUEST_TAKE_PHOTO = 121;
    private static final int REQUEST_PICK_PHOTO = 123;
    private static final int REQ_CROP_IMAGE = 122;
    private static final int REQ_PERMISSIONS = 120;

    private static final String E_ACTIVITY_DOES_NOT_EXIST = "E_ACTIVITY_DOES_NOT_EXIST";
    private static final String E_PICKER_CANCELLED = "E_PICKER_CANCELLED";
    private static final String E_FAILED_TO_SHOW_PICKER = "E_FAILED_TO_SHOW_PICKER";
    private static final String E_NO_IMAGE_DATA_FOUND = "E_NO_IMAGE_DATA_FOUND";

    Uri currentPhotoUri = null;

    private Promise mPromise;

    private final BaseActivityEventListener mActivityEventListener = new BaseActivityEventListener() {
        @Override
        public void onActivityResult(Activity activity, int requestCode, int resultCode, Intent data) {
            if (resultCode == RESULT_OK) {
                if (mPromise != null) {
                    switch (requestCode) {
                        case REQ_SCAN:
                        case REQ_CROP_IMAGE:
                            if (data != null && data.getExtras() != null) {
                                String path = data.getStringExtra(CVScanner.RESULT_IMAGE_PATH);
                                File file = new File(path);
                                Uri imageUri = Util.getUriForFile(reactContext, file);
                                if (imageUri != null) {
                                    mPromise.resolve(imageUri.toString());
                                }
                            }
                            mPromise = null;
                            break;
                        case REQUEST_TAKE_PHOTO:
                            startImageCropIntent();
                            break;
                        case REQUEST_PICK_PHOTO:
                            if (data.getData() != null) {
                                startImageCropIntent();
                                currentPhotoUri = data.getData();
                                mPromise.resolve(data.getData().toString());
                            }
                            mPromise = null;
                            break;
                    }
                }
            }
        }
    };

    public RNDocScannerModule(ReactApplicationContext reactContext) {
        super(reactContext);
        this.reactContext = reactContext;
        this.reactContext.addActivityEventListener(mActivityEventListener);
    }

    @Override
    public String getName() {
        return "RNDocScanner";
    }

    void startImageCropIntent(){
        CVScanner.startManualCropper(getCurrentActivity(), currentPhotoUri, REQ_CROP_IMAGE);
    }

    void startCameraIntent(){
        if(Build.VERSION.SDK_INT < Build.VERSION_CODES.M ||
                PermissionChecker.checkSelfPermission(getCurrentActivity(), Manifest.permission.WRITE_EXTERNAL_STORAGE) == PackageManager.PERMISSION_GRANTED) {
            try {
                currentPhotoUri = CVScanner.startCameraIntent(getCurrentActivity(), REQUEST_TAKE_PHOTO);
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        else{
            ActivityCompat.requestPermissions(getCurrentActivity(), new String[]{Manifest.permission.WRITE_EXTERNAL_STORAGE}, REQ_PERMISSIONS);
        }
    }

    
    @ReactMethod
    public void getDocumentCrop(boolean isManual, final Promise promise) {
        Log.d("RNDocScannerModule", "ismanual: " + isManual);
        Activity currentActivity = getCurrentActivity();
        if (currentActivity == null) {
          promise.reject(E_ACTIVITY_DOES_NOT_EXIST, "Activity DNE");
          return;
        }

        mPromise = promise;

        try {
            if (isManual) {
                startCameraIntent();
            } else {
                CVScanner.startScanner(currentActivity, false, REQ_SCAN);
            }
          
        } catch (Exception e) {
          mPromise.reject(E_FAILED_TO_SHOW_PICKER, e);
          mPromise = null;
        }
    }
}