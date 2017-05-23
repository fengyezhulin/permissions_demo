package com.zhy.permission_sample;

import android.Manifest;
import android.annotation.TargetApi;
import android.app.Activity;
import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import com.zhy.m.permission.MPermissions;
import com.zhy.m.permission.PermissionGrant;
import com.zhy.m.permission.ShowRequestPermissionRationale;

public class MainActivity extends Activity
{

    private Button mBtnSdcard, mBtnCallPhone;
    private static final int REQUECT_CODE_SDCARD = 2;
    private static final int REQUECT_CODE_CALL_PHONE = 3;

    private static final String PACKAGE_URL_SCHEME = "package:"; // 方案

    @TargetApi(Build.VERSION_CODES.HONEYCOMB)
    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        mBtnSdcard = (Button) findViewById(R.id.id_btn_sdcard);
        mBtnCallPhone = (Button) findViewById(R.id.id_btn_callphone);


        mBtnSdcard.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View v)
            {

                if (!MPermissions.shouldShowRequestPermissionRationale(MainActivity.this, Manifest.permission.WRITE_EXTERNAL_STORAGE, REQUECT_CODE_SDCARD))
                {
                    MPermissions.requestPermissions(MainActivity.this, REQUECT_CODE_SDCARD, Manifest.permission.WRITE_EXTERNAL_STORAGE);
                }
            }
        });

        mBtnCallPhone.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View v)
            {
                MPermissions.requestPermissions(MainActivity.this, REQUECT_CODE_CALL_PHONE, Manifest.permission.CALL_PHONE);
            }
        });
        TestFragment testFragment = new TestFragment();
        getFragmentManager().beginTransaction()
                .add(R.id.id_fragment, testFragment , "TestFragment")
                .commitAllowingStateLoss();

    }

    @ShowRequestPermissionRationale(REQUECT_CODE_SDCARD)
    public void whyNeedSdCard()
    {
        Toast.makeText(this, "I need write news to sdcard!", Toast.LENGTH_SHORT).show();
        MPermissions.requestPermissions(MainActivity.this, REQUECT_CODE_SDCARD, Manifest.permission.WRITE_EXTERNAL_STORAGE);

    }


    @Override
    public void onRequestPermissionsResult(int requestCode, String[] permissions, int[] grantResults)
    {
        MPermissions.onRequestPermissionsResult(this, requestCode, permissions, grantResults);
        super.onRequestPermissionsResult(requestCode,permissions,grantResults);

    }


    @PermissionGrant(REQUECT_CODE_SDCARD)
    public void requestSdcardSuccess()
    {
        Toast.makeText(this, "GRANT ACCESS SDCARD!", Toast.LENGTH_SHORT).show();
        Intent intent = new Intent(this,SecendActivity.class);
        startActivity(intent);
    }



    @PermissionGrant(REQUECT_CODE_CALL_PHONE)
    public void requestCallPhoneSuccess()
    {
        Toast.makeText(this, "GRANT ACCESS SDCARD!", Toast.LENGTH_SHORT).show();
    }


}
