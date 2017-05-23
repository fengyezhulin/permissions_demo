package com.zhy.permission_sample;

import android.Manifest;
import android.annotation.TargetApi;
import android.app.Fragment;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import com.zhy.m.permission.MPermissions;
import com.zhy.m.permission.PermissionGrant;

/**
 * Created by zhy on 16/2/21.
 */

@TargetApi(Build.VERSION_CODES.HONEYCOMB)
public class TestFragment extends Fragment
{
    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState)
    {
        return inflater.inflate(R.layout.fragment_test, container, false);
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState)
    {
        view.findViewById(R.id.id_btn_contact).setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View v)
            {
                MPermissions.requestPermissions(TestFragment.this, 4, Manifest.permission.WRITE_CONTACTS);
            }
        });

    }

    @PermissionGrant(4)
    public void requestContactSuccess()
    {
        Toast.makeText(getActivity(), "GRANT ACCESS CONTACTS!", Toast.LENGTH_SHORT).show();
        getFragmentManager().beginTransaction()
                .hide(this)
                .add(R.id.id_fragment, new SecFragment() , "TestFragment")
                .addToBackStack(null)
                .commitAllowingStateLoss();

    }

    @Override
    public void onRequestPermissionsResult(int requestCode, String[] permissions, int[] grantResults)
    {
        MPermissions.onRequestPermissionsResult(this, requestCode, permissions, grantResults);


    }
}
