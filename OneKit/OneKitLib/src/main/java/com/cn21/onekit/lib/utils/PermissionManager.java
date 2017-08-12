package com.cn21.onekit.lib.utils;

import android.Manifest;
import android.content.Context;
import android.content.pm.PackageManager;
import android.os.Build;
import android.text.TextUtils;

import java.util.ArrayList;

public class PermissionManager {


	public static String[] READ_PHONE_STATE_PERMISSION = {Manifest.permission.READ_PHONE_STATE};

	public static boolean checkPermission(Context context, String permission) {
		if (context == null || TextUtils.isEmpty(permission)) {
			return false;
		}
		PackageManager pm = context.getPackageManager();
		if (PackageManager.PERMISSION_GRANTED == pm.checkPermission(permission, context.getPackageName())) {
			return true;
		}
		return false;
	}


	private static boolean checkHasPermissonDeny(Context context, String[] permissions) {
		if (context == null || permissions == null || permissions.length <= 0) {
			return false;
		}
		PackageManager pm = context.getPackageManager();
		ArrayList<String> pList = new ArrayList<String>();
		for (int i = 0; i < permissions.length; i++) {
			if (PackageManager.PERMISSION_DENIED == pm.checkPermission(permissions[i], context.getPackageName())) {
				pList.add(permissions[i]);
			}
		}
		if (!pList.isEmpty()) {
			return true;
		} else {
			return false;
		}
	}

	public static boolean checkPhoneStatePermission(Context context) {
		if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
			try{
				if (checkHasPermissonDeny(context, READ_PHONE_STATE_PERMISSION)) {
					return false;
				}
			}catch (Exception e){
				e.printStackTrace();
			}
		}
		return true;
	}
}