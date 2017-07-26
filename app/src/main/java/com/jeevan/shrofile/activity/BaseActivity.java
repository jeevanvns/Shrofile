package com.jeevan.shrofile.activity;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Build;
import android.os.Bundle;
import android.os.PersistableBundle;
import android.provider.MediaStore;
import android.support.annotation.LayoutRes;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.AppCompatActivity;
import android.view.MenuItem;
import android.view.View;
import android.view.inputmethod.InputMethodManager;

import com.jeevan.shrofile.R;

public abstract class BaseActivity extends AppCompatActivity {
    private ProgressDialog mProgressDialog;
    private static final int REQUEST_CAMERA = 100;

    @Override
    protected void onStart() {
        super.onStart();

    }

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(getLayoutId());
        init();
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState, @Nullable PersistableBundle persistentState) {
        super.onCreate(savedInstanceState, persistentState);

    }


    /**
     * initialize toolbar
     * @param status boolean value
     */
    protected void initToolbar(boolean status) {
        if (status) {
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
            getSupportActionBar().setTitle(setToolbarName());
        }
    }


    /**
     *  override function for show toolbar or not
     * @return boolean value
     */
    public boolean showToolbar() {
        return false;
    }

    public void init() {
        initToolbar(showToolbar());
        initView();
        initListener();
        bindDataWithUi();
    }

    protected abstract
    @LayoutRes
    int getLayoutId();

    protected abstract void initView();

    protected abstract void initListener();

    protected abstract void bindDataWithUi();


    /**
     * show Progress Dialog
     */
    public void showLoadingDialog() {
        if (isDestroyingActivity()) return;
        if (mProgressDialog == null || !mProgressDialog.isShowing()) {
            mProgressDialog = new ProgressDialog(this, R.style.DialogTheme);
            mProgressDialog.setIndeterminate(false);
            mProgressDialog.setCancelable(false);
            mProgressDialog.show();
            mProgressDialog.setContentView(R.layout.dialog_progress);
        }
        mProgressDialog.show();
    }


    /**
     * @return boolean value
     * return true if Progress Dialog show else return false
     */
    public boolean isShowingProgressDialog() {
        return !(mProgressDialog == null) && mProgressDialog.isShowing();
    }


    /***
     * hide the Progress Dialog
     */
    public void hideLoadingDialog() {
        if (isDestroyingActivity())
            return;
        if (mProgressDialog != null && mProgressDialog.isShowing()) {
            mProgressDialog.dismiss();
            mProgressDialog = null;
        }
    }


    /**
     * check the base Activity is destroy or not
     * if activity destroy return true else return false
     * @return boolean value
     */
    public boolean isDestroyingActivity() {
        return isFinishing() || Build.VERSION.SDK_INT >= Build.VERSION_CODES.JELLY_BEAN_MR1 && isDestroyed();
    }


    /**
     * hide the keyboard
     */
    public void hideSoftInputKeyboard() {
        View view = this.getCurrentFocus();
        if (view != null) {
            InputMethodManager imm = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
            imm.hideSoftInputFromWindow(view.getWindowToken(), 0);
        }
    }


    /**
     * this function check the internet connection and return the boolean value
     * @return true and false value
     */
    public boolean isNetworkAvailable() {
        ConnectivityManager connectivityManager = (ConnectivityManager) BaseActivity.this.getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo activeNetworkInfo = connectivityManager.getActiveNetworkInfo();
        return activeNetworkInfo != null && activeNetworkInfo.isConnected();
    }


    /**
     * Add Fragment in Frame
     * @param fragment    Fragment Name that attach with frame
     * @param containerId Frame Container Id
     */
    public void addFragment(Fragment fragment, int containerId) {
        getSupportFragmentManager()
                .beginTransaction()
                .add(containerId, fragment)
                .commitAllowingStateLoss();
    }

    /**
     * @param fragment       Replace Fragment Name
     * @param containerId    Container Id
     * @param addToBackStack send the boolean value(true or false) for add to back stack or not
     */
    public void replaceFragment(Fragment fragment, int containerId, boolean addToBackStack) {
        FragmentTransaction replace = getSupportFragmentManager().beginTransaction()
                .replace(containerId, fragment);
        if (addToBackStack) {
            replace.addToBackStack(fragment.getClass().getName());
        }
        replace.commitAllowingStateLoss();
    }


    /**
     * set Toolbar name runtime
     *
     * @return toolbar name
     */
    public String setToolbarName() {
        return getResources().getString(R.string.app_name);
    }


    /**
     * default menu item init
     *
     * @param item
     * @return
     */
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home:
                onBackPressed();
                return true;
        }
        return super.onOptionsItemSelected(item);
    }


    /**
     * check permission in run time and return boolean result
     *
     * @param permission required permission list
     * @return boolean value true false
     */
    public boolean hasPermission(String[] permission) {
        for (String s : permission) {
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                if (PackageManager.PERMISSION_GRANTED != checkSelfPermission(s)) {
                    return false;
                }
            }
        }
        return true;
    }


    /**
     * check permission run time if permission in not have then request permission
     *
     * @param permission     list of permission
     * @param permissionCode constant permission code
     * @return boolean value
     */
    public boolean checkPermission(String[] permission, int permissionCode) {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            requestPermissions(permission, permissionCode);
            hasPermission(permission);
        }
        return true;
    }


    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == REQUEST_CAMERA && resultCode == Activity.RESULT_OK && null != data) {
            Bitmap image = (Bitmap) data.getExtras().get("data");
            onCameraResult(image);
        }
    }


    /**
     * override function that return Bitmap file
     *
     * @param path Bitmap file
     */
    protected void onCameraResult(Bitmap path) {
    }


    /**
     * start Image Capture Action
     */
    public void imagePicker() {
        startActivityForResult(new Intent(MediaStore.ACTION_IMAGE_CAPTURE), REQUEST_CAMERA);
    }


    /**
     * check permission and open camera
     */
    public void clickImage() {
        String[] cameraPermission = {android.Manifest.permission.CAMERA, android.Manifest.permission.WRITE_EXTERNAL_STORAGE};
        if (hasPermission(cameraPermission)) {
            imagePicker();
        } else {
            checkPermission(cameraPermission, REQUEST_CAMERA);
        }
    }


    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        switch (requestCode) {
            case REQUEST_CAMERA:
                if ((grantResults.length > 0) && (grantResults[0] == PackageManager.PERMISSION_GRANTED) && (grantResults[1] == PackageManager.PERMISSION_GRANTED)) {
                    imagePicker();
                }
        }
    }
}
