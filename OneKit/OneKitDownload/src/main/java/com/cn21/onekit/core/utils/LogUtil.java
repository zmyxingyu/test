package com.cn21.onekit.core.utils;

import android.annotation.SuppressLint;
import android.os.Environment;
import android.text.TextUtils;
import android.util.Log;



import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStreamWriter;
import java.text.SimpleDateFormat;
import java.util.Date;

/**
 * 日志工具类，日志存储在SDcard下的某个子目录/189Log/下的Log.txt文件,
 * 
 * @author Administrator
 * 
 */
public class LogUtil {
	private static LogUtil logThread;
	private static final String TAG = "OneKit_Log";

	private String LOG_PATH_SDCARD_DIR; // 日志文件在sdcard中的路径
	private String LOG_SERVICE_LOG_PATH; // 本次产生的日志

	private String logServiceLogName = "Log.txt";// 本次服务输出的日志文件名称
	private SimpleDateFormat logContentSdf = new SimpleDateFormat(
			"yyyy-MM-dd HH:mm:ss");
	private OutputStreamWriter writer;

	private LogUtil() {
		init();
	}
	@SuppressLint("NewApi")
	public static File getStorageExternal() {
		return new File(Environment.getExternalStorageDirectory(), "com.cn21.onekit");
	}
	private void init() {
		if (TextUtils.isEmpty(LOG_SERVICE_LOG_PATH)) {
			File mailFile = getStorageExternal();
			if (mailFile != null) {
				LOG_PATH_SDCARD_DIR = mailFile.getAbsolutePath()
						+ File.separator + "log";
				LOG_SERVICE_LOG_PATH = LOG_PATH_SDCARD_DIR + File.separator
						+ logServiceLogName;
			}
		}
		try {
			createLogDir();
			writer = new OutputStreamWriter(new FileOutputStream(
					LOG_SERVICE_LOG_PATH, true));
		} catch (FileNotFoundException e) {
			Log.e(TAG, e.getMessage(), e);
			try {
				createLogDir();
				writer = new OutputStreamWriter(new FileOutputStream(
						LOG_SERVICE_LOG_PATH, true));
			} catch (FileNotFoundException e1) {
				Log.e(TAG, e.getMessage(), e1);
			}
		}
	}

	// 单例
	public static synchronized LogUtil getInstance() {
		if (logThread == null) {
			logThread = new LogUtil();
		}
		return logThread;
	}

	/**
	 * 创建日志目录
	 */
	private void createLogDir() {
		if (LOG_PATH_SDCARD_DIR != null) {
			File file = new File(LOG_PATH_SDCARD_DIR);
			file.mkdirs();
		}
	}

	/**
	 * 拷贝文件
	 * 
	 * @param source
	 * @param target
	 * @return
	 */
	private boolean copyFile(File source, File target) {
		FileInputStream in = null;
		FileOutputStream out = null;
		try {
			if (!target.exists()) {
				boolean createSucc = target.createNewFile();
				if (!createSucc) {
					return false;
				}
			}
			in = new FileInputStream(source);
			out = new FileOutputStream(target);
			byte[] buffer = new byte[8 * 1024];
			int count;
			while ((count = in.read(buffer)) != -1) {
				out.write(buffer, 0, count);
			}
			out.close();
			out = null;
			return true;
		} catch (Exception e) {
			e.printStackTrace();
			writeIntoLog("copy file fail");
			return false;
		} finally {
//			IOUtils.closeQuietly(in);
//			IOUtils.closeQuietly(out);
			if (in != null) {
				try {
					in.close();
				} catch (IOException e) {
					e.printStackTrace();
				}
			}
		}

	}

	/**
	 * 记录日志服务的基本信息 , 此日志名称为Log.log,此方法自动添加时间点
	 * 
	 * @param msg
	 */
	public synchronized void writeIntoLog(final String msg) {
		if (writer == null)
			init();
		if (writer != null) {
			synchronized (writer) {
				try {
					Date time = new Date();
					writer.write(logContentSdf.format(time) + " : " + msg);
					writer.write("\n");
					writer.flush();
				} catch (IOException e) {
					init();
					Date time = new Date();
					try {
						writer.write(logContentSdf.format(time) + " : " + msg);
						writer.write("\n");
						writer.flush();
					} catch (IOException e1) {
						Log.e(TAG, e.getMessage(), e);
					}
				}finally{
					closeFile();
				}
			}
		}

	}

	// 关闭文件
	private void closeFile() {
		if (writer != null) {
			try {
				writer.close();
			} catch (IOException e) {
				e.printStackTrace();
			}
			writer = null;
		}

	}
	
	/**
	 * 错误消息汇报
	 * @param errMsg 错误消息
	 */
//	public synchronized void reportError(String errMsg) {
//	}
	
	/**
	 * 错误消息汇报
	 * @param category 消息类型
	 * @param errMsg 错误消息
	 */
//	public synchronized void reportError(String category, String errMsg) {
//		StringBuffer sb = new StringBuffer(category.length() + errMsg.length() + 16);
//		sb.append("[").append(category).append("]: ").append(errMsg);
//	}

}
