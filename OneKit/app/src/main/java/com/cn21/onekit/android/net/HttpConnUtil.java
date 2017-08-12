package com.cn21.onekit.android.net;

import java.io.BufferedOutputStream;
import java.io.BufferedReader;
import java.io.DataOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.Map;

import android.content.Context;
import android.net.ConnectivityManager;
import android.net.Network;
import android.net.NetworkInfo;
import android.os.Build;
import android.text.TextUtils;
import android.util.Log;


public class HttpConnUtil {
	private static String TAG = HttpConnUtil.class.getSimpleName();
	private static  int TIMEOUT = 15*1000 ;

	public static String doGet(Context context,String requestUrl) {
		HttpURLConnection conn = null;
		InputStream stream = null;
		InputStreamReader inReader = null;
		BufferedReader buffer = null;
		String result = "" ;
		try {
			URL realUrl = new URL(requestUrl);
			conn = (HttpURLConnection) realUrl.openConnection();
			conn.setRequestProperty("accept", "*/*");
			conn.setRequestProperty("connection", "Keep-Alive");
			conn.setRequestMethod("GET");
			conn.setDoOutput(false);
			conn.setDoInput(true);
			conn.setConnectTimeout(TIMEOUT);
			conn.setReadTimeout(TIMEOUT);
			conn.setUseCaches(false);
			conn.addRequestProperty("Accept-Charset", "UTF-8");
			conn.connect();

			int responseCode = conn.getResponseCode();
			if (responseCode == HttpURLConnection.HTTP_OK){
				stream = conn.getInputStream();
				inReader = new InputStreamReader(stream);
				buffer = new BufferedReader(inReader);
				String strLine = "";
				while((strLine = buffer.readLine())!=null){
					result += strLine;
				}
			}
			Log.i(TAG ," http result : " + result);

		} catch (Exception e) {
			e.printStackTrace();
		}finally{
			try{
				if(buffer != null){
					buffer.close();
				}
				if(inReader != null){
					inReader.close();
				}
				if(stream != null){
					stream.close();
				}
			}catch (Exception e){
				e.printStackTrace();
			}
		}
		return result;
	}


	public static ResponseInfo doPost(Context context, String url, Map<String, String> headerParams , String params , Network network) {
		ResponseInfo responseInfo = new ResponseInfo();
		BufferedReader in = null;
		HttpURLConnection conn = null;
		try {
			URL realUrl = new URL(url);
			// 打开和URL之间的连接
			if (network != null && Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP ) {
				conn = (HttpURLConnection) network.openConnection(realUrl);
			}else{
				conn = (HttpURLConnection) realUrl.openConnection();
			}
			conn.setRequestProperty("accept", "*/*");
			addRequestProperty(conn,headerParams);
			conn.setRequestMethod("POST");
			conn.setDoOutput(true);
			conn.setDoInput(true);
			conn.setConnectTimeout(TIMEOUT);
			conn.setReadTimeout(TIMEOUT);
			conn.setUseCaches(false);
			conn.addRequestProperty("Accept-Charset", "UTF-8");
			if(!TextUtils.isEmpty(params)){
//					conn.connect();   //conn.getOutputStream()包括了先进行conn.connect()
				DataOutputStream out = new DataOutputStream(new BufferedOutputStream(conn.getOutputStream()));
				out.writeBytes(params);
				out.flush();
				out.close();
			}else{
				conn.connect();
			}
			responseInfo.responseCode = conn.getResponseCode();
			if(responseInfo.responseCode == 302 ){
				String location = conn.getHeaderField("Location");
				URL serverUrl = new URL(location);
				if (network != null && Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP ) {
					conn = (HttpURLConnection) network.openConnection(serverUrl);
				}else{
					conn = (HttpURLConnection) serverUrl.openConnection();
				}
				conn.setRequestProperty("accept", "*/*");
				conn.setRequestMethod("POST");
				conn.setDoOutput(true);
				conn.setDoInput(true);
				conn.setConnectTimeout(TIMEOUT);
				conn.setReadTimeout(TIMEOUT);
				conn.setUseCaches(false);
				conn.addRequestProperty("Accept-Charset", "UTF-8");
				conn.connect();
			}
			responseInfo.responseCode = conn.getResponseCode();
			if (responseInfo.responseCode == HttpURLConnection.HTTP_OK) {
				responseInfo.result = convertStreamToString(conn.getInputStream());
			}
		}catch(Exception e){
			e.printStackTrace();
			responseInfo.errorMsg = Log.getStackTraceString(e);
		}finally{
			try {
				if (in != null) {
					in.close();
				}
			} catch (IOException ex) {
				ex.printStackTrace();
			}
		}
		return responseInfo;
	}
	public static ResponseInfo doPost(Context context, String url, Map<String, String> headerParams , String params,
                                      boolean shouldHandle , Network network) {
		ResponseInfo responseInfo = new ResponseInfo();
		BufferedReader in = null;
		HttpURLConnection conn = null;
		try {
			URL realUrl = new URL(url);
			// 打开和URL之间的连接
			if (network != null && Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP ) {
				conn = (HttpURLConnection) network.openConnection(realUrl);
			}else{
				conn = (HttpURLConnection) realUrl.openConnection();
			}
			conn.setRequestProperty("accept", "*/*");
			addRequestProperty(conn,headerParams);
			conn.setRequestMethod("POST");
			conn.setDoOutput(true);
			conn.setDoInput(true);
			conn.setConnectTimeout(TIMEOUT);
			conn.setReadTimeout(TIMEOUT);
			conn.setUseCaches(false);
			if(shouldHandle && Build.VERSION.SDK_INT < Build.VERSION_CODES.LOLLIPOP){
				conn.setInstanceFollowRedirects(false);
			}
			conn.addRequestProperty("Accept-Charset", "UTF-8");
//			conn.setRequestProperty("Cookie", "Cookie:" + DefaultShared.getString(context,Constants.KEY_GET_REQUEST_COOKIES,""));
//			Log.e("fuyanan","Cookie DefaultShared==="+DefaultShared.getString(Constants.KEY_GET_REQUEST_COOKIES,"")+url);
			if(!TextUtils.isEmpty(params)){
//					conn.connect();   //conn.getOutputStream()包括了先进行conn.connect()
				DataOutputStream out = new DataOutputStream(new BufferedOutputStream(conn.getOutputStream()));
				out.writeBytes(params);
				out.flush();
				out.close();
			}else{
				conn.connect();
			}
			responseInfo.responseCode = conn.getResponseCode();
			if(responseInfo.responseCode == 302 ){
				String location = conn.getHeaderField("Location");
				if(shouldHandle && !TextUtils.isEmpty(location) && context !=null &&
						  Build.VERSION.SDK_INT < Build.VERSION_CODES.LOLLIPOP ){
					ConnectivityManager connectivityManager = (ConnectivityManager) context
							.getSystemService(Context.CONNECTIVITY_SERVICE);
					NetworkInfo.State checkState = connectivityManager.getNetworkInfo(ConnectivityManager.TYPE_MOBILE_HIPRI).getState();
				}
				URL serverUrl = new URL(location);
				if (network != null && Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP ) {
					conn = (HttpURLConnection) network.openConnection(serverUrl);
				}else{
					conn = (HttpURLConnection) serverUrl.openConnection();
				}
				conn.setRequestProperty("accept", "*/*");
				conn.setRequestMethod("POST");
				conn.setDoOutput(true);
				conn.setDoInput(true);
				conn.setConnectTimeout(TIMEOUT);
				conn.setReadTimeout(TIMEOUT);
				conn.setUseCaches(false);
				conn.addRequestProperty("Accept-Charset", "UTF-8");
				conn.connect();
			}
			responseInfo.responseCode = conn.getResponseCode();
			if(responseInfo.responseCode == 302 ){
				String location = conn.getHeaderField("Location");
				URL serverUrl = new URL(location);
				if (network != null && Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP ) {
					conn = (HttpURLConnection) network.openConnection(serverUrl);
				}else{
					conn = (HttpURLConnection) serverUrl.openConnection();
				}
				conn.setRequestProperty("accept", "*/*");
				conn.setRequestMethod("POST");
				conn.setDoOutput(true);
				conn.setDoInput(true);
				conn.setConnectTimeout(TIMEOUT);
				conn.setReadTimeout(TIMEOUT);
				conn.setUseCaches(false);
				conn.addRequestProperty("Accept-Charset", "UTF-8");
				conn.connect();
			}
			responseInfo.responseCode = conn.getResponseCode();
			if (responseInfo.responseCode == HttpURLConnection.HTTP_OK) {
				responseInfo.result = convertStreamToString(conn.getInputStream());
			}
		}catch(Exception e){
			e.printStackTrace();
			responseInfo.errorMsg = Log.getStackTraceString(e);
			Log.w(TAG, "doPost Exception : " + responseInfo.errorMsg);
		}finally{
			try {
				if (in != null) {
					in.close();
				}
			} catch (IOException ex) {
				ex.printStackTrace();
			}
		}
		return responseInfo;
	}



	/**
	 * 流转换器
	 * @param is 输入流
	 * @return 如果流转换失败，返回空串
	 */
	private static String convertStreamToString(InputStream is) {
		StringBuilder sb = new StringBuilder();
		BufferedReader reader = null;
		try {
			reader = new BufferedReader(new InputStreamReader(is, "UTF-8"), 8 * 1024);
			String line;
			while (null != (line = reader.readLine())) {
				sb.append(line).append("\n");
			}
		} catch (IOException e) {
			sb.delete(0, sb.length());
		} finally {
			try {
				if(is != null){
					is.close();
				}
				if(reader != null){
					reader.close();
				}
			} catch (IOException e) {
			    e.printStackTrace();
			}
		}
		return sb.toString();
	}
	private static void addRequestProperty(HttpURLConnection conn, Map<String, String> headerParams) {
		if (headerParams != null && !headerParams.isEmpty()) {
			for (Map.Entry<String, String> entry : headerParams.entrySet()) {
				conn.setRequestProperty(entry.getKey(), entry.getValue());
			}
		}
	}

}
