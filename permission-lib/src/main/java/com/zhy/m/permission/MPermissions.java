package com.zhy.m.permission;

import android.annotation.TargetApi;
import android.app.Activity;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.Build;
import android.provider.Settings;
import android.support.v4.app.ActivityCompat;
import android.app.Fragment;
import android.support.v7.app.AlertDialog;

import java.util.ArrayList;
import java.util.List;


public class MPermissions
{
    private static final String SUFFIX = "$$PermissionProxy";
    private static final String PACKAGE_URL_SCHEME = "package:"; //
    private static boolean mIsFinish;

    public static void requestPermissions(Activity object, int requestCode, String... permissions)
    {
        _requestPermissions(object, requestCode, permissions);
    }

    public static void requestPermissions(Fragment object, int requestCode, String... permissions)
    {
        _requestPermissions(object, requestCode, permissions);
    }

    public static boolean shouldShowRequestPermissionRationale(Activity activity, String permission, int requestCode)
    {
        PermissionProxy proxy = findPermissionProxy(activity);
        if (!proxy.needShowRationale(requestCode)) return false;
        if (ActivityCompat.shouldShowRequestPermissionRationale(activity,
                permission))
        {
            proxy.rationale(activity, requestCode);
            return true;
        }
        return false;
    }

    public static void setIsFinish(boolean isFinish){
        mIsFinish = isFinish;
}

    @TargetApi(value = Build.VERSION_CODES.M)
    private static void _requestPermissions(Object object, int requestCode, String... permissions)
    {
        if (!Utils.isOverMarshmallow())
        {
            doExecuteSuccess(object, requestCode);
            return;
        }
        List<String> deniedPermissions = Utils.findDeniedPermissions(Utils.getActivity(object), permissions);

        if (deniedPermissions.size() > 0)
        {
            if (object instanceof Activity)
            {
                ((Activity) object).requestPermissions(deniedPermissions.toArray(new String[deniedPermissions.size()]), requestCode);
            } else if (object instanceof Fragment)
            {
                ((Fragment) object).requestPermissions(deniedPermissions.toArray(new String[deniedPermissions.size()]), requestCode);
            } else
            {
                throw new IllegalArgumentException(object.getClass().getName() + " is not supported!");
            }
        } else
        {
            doExecuteSuccess(object, requestCode);
        }
    }


    private static PermissionProxy findPermissionProxy(Object activity)
    {
        try
        {
            Class clazz = activity.getClass();
            Class injectorClazz = Class.forName(clazz.getName() + SUFFIX);
            return (PermissionProxy) injectorClazz.newInstance();
        } catch (ClassNotFoundException e)
        {
            e.printStackTrace();
        } catch (InstantiationException e)
        {
            e.printStackTrace();
        } catch (IllegalAccessException e)
        {
            e.printStackTrace();
        }
        throw new RuntimeException(String.format("can not find %s , something when compiler.", activity.getClass().getSimpleName() + SUFFIX));
    }


    private static void doExecuteSuccess(Object activity, int requestCode)
    {
        findPermissionProxy(activity).grant(activity, requestCode);

    }

    @TargetApi(Build.VERSION_CODES.HONEYCOMB)
    private static void doExecuteFail(Object activity, int requestCode)
    {
       if(activity == null){
           return;
       }
        Activity activity1 = null;
        if(activity instanceof Activity){
            activity1 = (Activity)activity;
            if(mIsFinish){
                activity1.finish();
            }else {
                showMissingPermissionDialog(activity1);
            }
        }else if(activity instanceof Fragment){
            Fragment fragment = (Fragment)activity;
            activity1 = fragment.getActivity();
            if(mIsFinish){
                activity1.onBackPressed();

            }else {
                showMissingPermissionDialog(activity1);
            }
        }

    }

    //
    private static void showMissingPermissionDialog(final Activity activity) {
        AlertDialog.Builder builder = new AlertDialog.Builder(activity);
        builder.setTitle(R.string.help);
        builder.setMessage(R.string.string_help_text);


        builder.setNegativeButton(R.string.quit, new DialogInterface.OnClickListener() {
            @Override public void onClick(DialogInterface dialog, int which) {

            }
        });

        builder.setPositiveButton(R.string.settings, new DialogInterface.OnClickListener() {
            @Override public void onClick(DialogInterface dialog, int which) {
                startAppSettings(activity);
            }
        });

        builder.setCancelable(false);

        builder.show();
    }
    // 请求应用权限
    private static void startAppSettings(Activity activity) {
        Intent intent = new Intent(Settings.ACTION_APPLICATION_DETAILS_SETTINGS);
        intent.setData(Uri.parse(PACKAGE_URL_SCHEME + activity.getPackageName()));
        activity.startActivity(intent);
    }

    public static void onRequestPermissionsResult(Activity activity, int requestCode, String[] permissions,
                                                  int[] grantResults)
    {
        requestResult(activity, requestCode, permissions, grantResults);
    }

    public static void onRequestPermissionsResult(Fragment fragment, int requestCode, String[] permissions,
                                                  int[] grantResults)
    {
        requestResult(fragment, requestCode, permissions, grantResults);
    }

    private static void requestResult(Object obj, int requestCode, String[] permissions,
                                      int[] grantResults)
    {
        List<String> deniedPermissions = new ArrayList<>();
        for (int i = 0; i < grantResults.length; i++)
        {
            if (grantResults[i] != PackageManager.PERMISSION_GRANTED)
            {
                deniedPermissions.add(permissions[i]);
            }
        }
        if (deniedPermissions.size() > 0)
        {
            doExecuteFail(obj, requestCode);
        } else
        {
            doExecuteSuccess(obj, requestCode);
        }
        mIsFinish = false;//重置
    }
}
