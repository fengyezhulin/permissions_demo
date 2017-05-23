package com.zhy.permission_sample;

import android.Manifest;
import android.app.Fragment;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import com.zhy.m.permission.MPermissions;
import com.zhy.m.permission.PermissionGrant;

/**
 * Created by longdan on 2017/5/18 0018.
 */

public class SecFragment extends Fragment {
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState)
    {
        return inflater.inflate(R.layout.fragment_sec, container, false);
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState)
    {
        MPermissions.setIsFinish(true);
        MPermissions.requestPermissions(SecFragment.this, 4, Manifest.permission.BODY_SENSORS);
    }

    @PermissionGrant(4)
    public void requestContactSuccess()
    {
        Toast.makeText(getActivity(), "GRANT ACCESS CONTACTS!", Toast.LENGTH_SHORT).show();
    }
    @Override
    public void onRequestPermissionsResult(int requestCode, String[] permissions, int[] grantResults)
    {
        MPermissions.onRequestPermissionsResult(this, requestCode, permissions, grantResults);
        super.onRequestPermissionsResult(requestCode,permissions,grantResults);
    }
}
