package com.wyzk.lottery.video.activity;

import android.Manifest;
import android.app.AlertDialog;
import android.app.AlertDialog.Builder;
import android.content.DialogInterface;
import android.content.pm.PackageManager;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.annotation.RequiresApi;
import android.support.v4.content.PermissionChecker;
import android.util.Log;
import android.view.KeyEvent;
import android.view.WindowManager;
import android.widget.Toast;

import com.wyzk.lottery.event.NetworkEvent;
import com.wyzk.lottery.ui.LotteryBaseActivity;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

/**
 * demo中界面的基类，定义了一些界面使用的公共方法和功能；<br/>
 * <li>复写{@link #onKeyDown(int, KeyEvent)}，实现双击back键退出界面</li><br/>
 * <li>添加{@link #checkAndRequestPermission()}，实现6.0及以上平台动态检查存储、摄像头、麦克风权限</li><br/>
 * <li>复写{@link #onRequestPermissionsResult(int, String[], int[])}，配合动态权限检查</li><br/>
 * <li>复写{@link #showRequestPermissinDialog(List)}，显示权限提示对话框</li><br/>
 */
public abstract class VideoBaseActivity extends LotteryBaseActivity {
    private static final String TAG = "LotteryBaseActivity";
    protected static long EXIT_INTERVAL = 2 * 1000;
    /**
     * 动态请求权限的权限列表
     */
    protected String[] PERMISSIONS = new String[]{Manifest.permission.WRITE_EXTERNAL_STORAGE,
            Manifest.permission.CAMERA,
            Manifest.permission.RECORD_AUDIO,
            Manifest.permission.READ_PHONE_STATE};
    /**
     * 动态请求权限的权限列表的提示信息
     */
    protected String[] PERMISSION_TOAST_STRING = new String[]{"存储", "相机", "麦克风"};
    private long mBackKeyLastPressedTime;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        getWindow().addFlags(WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            checkAndRequestPermission();
        }

    }

    /**
     * 复写系统onKeyDown方法，实现双击back键退出功能。
     *
     * @param keyCode
     * @param event
     * @return
     */
    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        if (keyCode == KeyEvent.KEYCODE_BACK) {
            long currentTime = System.currentTimeMillis();
            if (currentTime - mBackKeyLastPressedTime > EXIT_INTERVAL) {
                Toast.makeText(this, "再按一次离开界面", Toast.LENGTH_SHORT).show();
                mBackKeyLastPressedTime = currentTime;
                return false;
            }
            return super.onKeyDown(keyCode, event);
        }
        return super.onKeyDown(keyCode, event);
    }

    /**
     * 6.0及以上平台动态请求{@link #PERMISSIONS}数组中定义的权限，<br/>
     * 通过{@link #onRequestPermissionsResult(int, String[], int[])}方法回调，获得权限获取情况，<br/>
     * 如果缺少权限，则通过{@link #showRequestPermissinDialog(List)}显示提示对话框，提示缺少权限。
     *
     * @return
     */
    @RequiresApi(api = Build.VERSION_CODES.M)
    protected boolean checkAndRequestPermission() {
        ArrayList<String> lackedPermissions = new ArrayList<String>(PERMISSIONS.length);
        for (int i = 0; i < PERMISSIONS.length; i++) {
            int result = PermissionChecker.checkCallingOrSelfPermission(this, PERMISSIONS[i]);
            if (result != PackageManager.PERMISSION_GRANTED) {
                lackedPermissions.add(PERMISSIONS[i]);
            }
        }
        if (lackedPermissions.size() > 0) {
            String[] rP = new String[lackedPermissions.size()];
            lackedPermissions.toArray(rP);
            if (Build.VERSION.SDK_INT > Build.VERSION_CODES.M - 1) {
                requestPermissions(rP, 0);
                Log.i(TAG, "requestPermissions " + lackedPermissions.toString() + " !");
            } else {
                int[] grantResults = new int[rP.length];
                for (int i = 0; i < grantResults.length; i++) {
                    grantResults[i] = PackageManager.PERMISSION_GRANTED;
                }
                onRequestPermissionsResult(0, rP, grantResults);
                Log.i(TAG, "the platform versin below 23 M , cann't request permissions  !");
            }
            return false;
        }
        Log.i(TAG, "checkPermission success , All permission has granted !");
        return true;
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, String[] permissions, int[] grantResults) {
        Log.i(TAG, "permissions :" + Arrays.toString(permissions));
        Log.i(TAG, "grantResults :" + Arrays.toString(grantResults));
        ArrayList<String> lackedPermissions = new ArrayList<String>(PERMISSIONS.length);
        for (int i = 0; i < permissions.length; i++) {
            if (grantResults[i] != PackageManager.PERMISSION_GRANTED) {
                lackedPermissions.add(permissions[i]);
            }
        }
        showRequestPermissinDialog(lackedPermissions);
    }

    /**
     * 显示一个提示对话框
     *
     * @param mLackedPermissions
     */
    protected void showRequestPermissinDialog(List<String> mLackedPermissions) {
        if (mLackedPermissions == null || mLackedPermissions.size() == 0)
            return;
        StringBuilder stringBuilder = new StringBuilder();
        for (String permission : mLackedPermissions) {
            for (int i = 0; i < PERMISSIONS.length; i++) {
                if (PERMISSIONS[i].equals(permission)) {
                    stringBuilder.append(PERMISSION_TOAST_STRING[i]).append(",");
                }
            }
        }
        stringBuilder.deleteCharAt(stringBuilder.length() - 1);
        Builder builder = new Builder(this);
        builder.setMessage("应用需要如下权限： " + stringBuilder.toString() + "，请从“设置”中打开相应权限。");
        builder.setTitle(android.R.string.dialog_alert_title);
        builder.setPositiveButton(android.R.string.ok, new DialogInterface.OnClickListener() {

            @Override
            public void onClick(DialogInterface dialog, int which) {
                // do nothing
            }
        });
        builder.setCancelable(false);
        builder.create().show();
    }

    protected void showdialog(String title, String msg, String okStr, DialogInterface.OnClickListener ok) {
        /* @setIcon 设置对话框图标
         * @setTitle 设置对话框标题
         * @setMessage 设置对话框消息提示
         * setXXX方法返回Dialog对象，因此可以链式设置属性
         */
        final AlertDialog.Builder normalDialog =
                new AlertDialog.Builder(this);
        normalDialog.setTitle(title);
        normalDialog.setMessage(msg);
        normalDialog.setPositiveButton(okStr, ok);
        normalDialog.setCancelable(false);
        // 显示
        normalDialog.show();
    }

    protected void showdialog(String title, String msg, String okStr, String cancleStr, DialogInterface.OnClickListener ok, DialogInterface.OnClickListener cancle) {
        /* @setIcon 设置对话框图标
         * @setTitle 设置对话框标题
         * @setMessage 设置对话框消息提示
         * setXXX方法返回Dialog对象，因此可以链式设置属性
         */
        final AlertDialog.Builder normalDialog =
                new AlertDialog.Builder(this);
        normalDialog.setTitle(title);
        normalDialog.setMessage(msg);
        normalDialog.setPositiveButton(okStr, ok);
        normalDialog.setNegativeButton(cancleStr, cancle);
        normalDialog.setCancelable(false);
        // 显示
        normalDialog.show();
    }

}
