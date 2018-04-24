package com.zc.multiimageselector;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.widget.Button;
import android.widget.Toast;

import com.tbruyelle.rxpermissions.RxPermissions;

import java.util.ArrayList;

import butterknife.Bind;
import butterknife.ButterKnife;
import butterknife.OnClick;
import me.nereo.multi_image_selector.MultiImageSelector;
import rx.functions.Action1;

/**
 * https://github.com/lovetuzitong/MultiImageSelector
 */
public class MainActivity extends AppCompatActivity {
    private static final int REQUEST_IMAGE = 2;
    private ArrayList<String> mSelectPath;

    @Bind(R.id.btn_open)
    Button btnOpen;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        ButterKnife.bind(this);
        requestPermission();
    }

    @OnClick(R.id.btn_open)
    public void onViewClicked() {
        pickImage();
    }

    /**
     * RxPermission版本使用0.9.1正常运行，如果使用0.7.0会出现异常（android.os.FileUriExposedException: file:///storage/emulated/0/）
     */
    public void requestPermission() {
        RxPermissions rxPermissions = new RxPermissions(this);
        rxPermissions
                .request(android.Manifest.permission.CAMERA,
                        android.Manifest.permission.READ_PHONE_STATE,
                        android.Manifest.permission.READ_EXTERNAL_STORAGE)
                .subscribe(new Action1<Boolean>() {
                    @Override
                    public void call(Boolean aBoolean) {
                        if (aBoolean) {
                            //当所有权限都允许之后，返回true
                            Log.i("permissions", "btn_more_sametime：" + aBoolean);
                        } else {
                            //只要有一个权限禁止，返回false，
                            //下一次申请只申请没通过申请的权限
                            Log.i("permissions", "btn_more_sametime：" + aBoolean);
                        }
                    }
                });
    }

    private void pickImage() {
        MultiImageSelector selector = MultiImageSelector.create(MainActivity.this);
        selector.showCamera(true);//是否显示照相机
        selector.count(9);//最大选择数量，默认9
        selector.multi();//多选模式
        selector.origin(mSelectPath);//选择图片的路径
        selector.start(MainActivity.this, REQUEST_IMAGE);
    }

    //选择完图片回调结果
    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == REQUEST_IMAGE) {
            if (resultCode == RESULT_OK) {
                mSelectPath = data.getStringArrayListExtra(MultiImageSelector.EXTRA_RESULT);
                StringBuilder sb = new StringBuilder();
                for (String p : mSelectPath) {
                    sb.append(p);
                    sb.append("\n");
                }
                Toast.makeText(MainActivity.this, sb.toString(), Toast.LENGTH_SHORT).show();
            }
        }
    }
}
