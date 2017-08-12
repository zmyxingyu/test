package com.cn21.onekit.lib.utils;

import java.io.UnsupportedEncodingException;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.Random;


/**
 * byte相关常用方法工具类
 */
public class StringUtil {
	/**
	 * 将byte数组转换为十六进制文本
	 * @param buf
	 * @return
	 */
	public static String toHex(byte[] buf) {
		if (buf == null || buf.length == 0) {
			return "";
		}
		StringBuilder out = new StringBuilder();    // sonar StringBuffer
		for (int i = 0; i < buf.length; i++) {
			out.append(HEX[(buf[i] >> 4) & 0x0f]).append(HEX[buf[i] & 0x0f]);
		}
		return out.toString();
	}
	/**
	 * 将十六进制文本转换为byte数组
	 * @param str
	 * @return
	 */
	public static byte[] hexToBytes(String str) {
		if (str == null) {
			return null;
		}
		char[] hex = str.toCharArray();
		int length = hex.length / 2;
		byte[] raw = new byte[length];
		for (int i = 0; i < length; i++) {
			int high = Character.digit(hex[i * 2], 16);
			int low = Character.digit(hex[i * 2 + 1], 16);
			int value = (high << 4) | low;
			if (value > 127)
				value -= 256;
			raw[i] = (byte) value;
		}
		return raw;
	}
	private final static char[] HEX = { '0', '1', '2', '3', '4', '5', '6', '7', '8', '9', 'A', 'B', 'C', 'D', 'E', 'F' };

	/**
	 * 获取转入的字符串的MD5值
	 * @param inputStr
	 * @return
	 * @throws NoSuchAlgorithmException
	 */
	public static String getMD5Bit32(String inputStr) throws NoSuchAlgorithmException{
		return StringUtil.toHex(MessageDigest.getInstance("MD5").digest(StringUtil.hexToBytes(inputStr)));
	}
	/**生成指定位数的随机数(纯数字)*/
	public static String getRandomNum(int strLength) {
//		ThreadLocalRandom rm = ThreadLocalRandom.current();
		Random rm = new Random();
		StringBuilder randomNumStr = new StringBuilder();
		for (int index = 0; index < strLength; index++) {
			randomNumStr.append(rm.nextInt(10));
		}
		return randomNumStr.toString();
	}


	public static byte[] getBytes(String data){
		String encoding = "UTF-8";
		byte [] bytes = new byte[]{};
		try {
			bytes = data.getBytes(encoding);
		} catch (UnsupportedEncodingException e) {
			e.printStackTrace();
		}
		return  bytes;
	}
    /**
     * 获取转入的字符串的MD5值
     * @param info
     * @return
     **/
	public static String getMD5(String info) {
		try {
			MessageDigest md5 = MessageDigest.getInstance("MD5");
			md5.update(info.getBytes("UTF-8"));
			byte[] encryption = md5.digest();
			StringBuilder strBuf = new StringBuilder();
			for (int i = 0; i < encryption.length; i++) {
				if (Integer.toHexString(0xff & encryption[i]).length() == 1) {
					strBuf.append("0").append(Integer.toHexString(0xff & encryption[i]));
				} else {
					strBuf.append(Integer.toHexString(0xff & encryption[i]));
				}
			}
			return strBuf.toString();
		} catch (NoSuchAlgorithmException e) {
			e.printStackTrace();
		} catch (UnsupportedEncodingException e) {
			e.printStackTrace();
		} catch (Exception e) {
			e.printStackTrace();
		}
		return "";
	}
//
//	public static String md5Update(File file){
//		InputStream fis = null;
//		byte[] buffer = new byte[1024];
//		int numRead ;
//		MessageDigest md5;
//		try{
//			fis = new FileInputStream(file);
//			md5 = MessageDigest.getInstance("MD5");
//			while((numRead=fis.read(buffer)) > 0) {
//				md5.update(buffer,0,numRead);
//			}
//			return toHex(md5.digest());
//		} catch (Exception e) {
//			System.out.println("error");
//			return null;
//		}
//	}

	//new String(bytes,"UTF-8")封装  sonar
	public static String getStringWithBytes(byte bytes[]){
		try{
			return new String(bytes,"UTF-8");//sonar
		}catch (UnsupportedEncodingException e) {
			e.printStackTrace();
		}catch (NullPointerException e){
			e.printStackTrace();
		}catch (Exception e){
			e.printStackTrace();
		}
		return "";
	}



}
