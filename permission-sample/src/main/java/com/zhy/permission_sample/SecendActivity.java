package com.zhy.permission_sample;

import android.Manifest;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.widget.Toast;

import com.zhy.m.permission.MPermissions;
import com.zhy.m.permission.PermissionGrant;

public class SecendActivity extends AppCompatActivity {

    // 所需的全部权限
    static final String[] PERMISSIONS = new String[]{
            Manifest.permission.RECORD_AUDIO,
            Manifest.permission.MODIFY_AUDIO_SETTINGS
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_secend);

        requestGrantedPermissions();
    }


    public void requestGrantedPermissions(){
        MPermissions.setIsFinish(true);
        MPermissions.requestPermissions(SecendActivity.this, 10, PERMISSIONS);

    }

    @PermissionGrant(10)
    public void requestGrantedPermissionsSuccrss(){
        Toast.makeText(this, "授权获取成功!", Toast.LENGTH_SHORT).show();
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, String[] permissions, int[] grantResults)
    {
        MPermissions.onRequestPermissionsResult(this, requestCode, permissions, grantResults);

    }
}
