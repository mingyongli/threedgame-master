package com.yu.imgpicker.ui;

import android.content.pm.PackageManager;
import android.os.Bundle;
import androidx.annotation.NonNull;
import androidx.core.app.ActivityCompat;
import androidx.appcompat.app.AppCompatActivity;
import android.widget.Toast;

import com.yu.imgpicker.ImagePicker;
import com.yu.imgpicker.R;
import com.yu.imgpicker.ImagePickerConfig;
import com.yu.imgpicker.utils.StatusBarUtil;

/**
 * BaseActivity
 */
public class BaseActivity extends AppCompatActivity {

    protected ImagePickerConfig mConfig;
    protected ImagePicker mImgPicker;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mImgPicker = ImagePicker.getInstance();
        mConfig = mImgPicker.getConfig();

        if (mConfig == null) {
            throw new NullPointerException("ImagePicker.getConfig() == null");
        }

        if (mConfig.titleBarColor != -1) {
            StatusBarUtil.setColor(this, mConfig.titleBarColor);
        } else {
            StatusBarUtil.setColor(this, getResources().getColor(R.color.main_color));
        }
    }

    public boolean checkPermission(@NonNull String permission) {
        return ActivityCompat.checkSelfPermission(this, permission) == PackageManager.PERMISSION_GRANTED;
    }

    public void showToast(String toastText) {
        Toast.makeText(getApplicationContext(), toastText, Toast.LENGTH_SHORT).show();
    }
}
