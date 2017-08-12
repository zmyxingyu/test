package com.cn21.onekit.lib.utils;

import java.security.InvalidKeyException;
import java.security.KeyFactory;
import java.security.KeyPair;
import java.security.KeyPairGenerator;
import java.security.NoSuchAlgorithmException;
import java.security.PrivateKey;
import java.security.PublicKey;
import java.security.interfaces.RSAPrivateKey;
import java.security.interfaces.RSAPublicKey;
import java.security.spec.PKCS8EncodedKeySpec;
import java.security.spec.X509EncodedKeySpec;
import java.util.HashMap;

import javax.crypto.BadPaddingException;
import javax.crypto.Cipher;
import javax.crypto.IllegalBlockSizeException;
import javax.crypto.Mac;
import javax.crypto.NoSuchPaddingException;
import javax.crypto.spec.SecretKeySpec;

public class RSAUtils {
	private static String RSAC = "RSA/ECB/PKCS1Padding";
//	private static String RSANO = "RSA/ECB/NoPadding";
	/**
	 * 生成公钥和私钥
	 * 
	 * @throws NoSuchAlgorithmException
	 * 
	 */
	public static HashMap<String, Object> getKeys()
			throws NoSuchAlgorithmException {
		HashMap<String, Object> map = new HashMap<String, Object>();
		KeyPairGenerator keyPairGen = KeyPairGenerator.getInstance("RSA");
		keyPairGen.initialize(1024);
		KeyPair keyPair = keyPairGen.generateKeyPair();
		RSAPublicKey publicKey = (RSAPublicKey) keyPair.getPublic();
		RSAPrivateKey privateKey = (RSAPrivateKey) keyPair.getPrivate();
		map.put("public", publicKey);
		map.put("private", privateKey);
		return map;
	}

	public static PublicKey getPublicKey(String key) throws Exception {
		byte[] keyBytes;
		keyBytes =  Base64Utils.decode(key);

		X509EncodedKeySpec keySpec = new X509EncodedKeySpec(keyBytes);
		KeyFactory keyFactory = KeyFactory.getInstance("RSA");
		PublicKey publicKey = keyFactory.generatePublic(keySpec);
		return publicKey;
	}

	public static PrivateKey getPrivateKey(String key) throws Exception {
		byte[] keyBytes;
		keyBytes =  Base64Utils.decode(key);

		PKCS8EncodedKeySpec keySpec = new PKCS8EncodedKeySpec(keyBytes);
		KeyFactory keyFactory = KeyFactory.getInstance("RSA");
		PrivateKey privateKey = keyFactory.generatePrivate(keySpec);
		return privateKey;
	}


	public static String decryptWithBase64(String base64String,
										   RSAPrivateKey privateKey) throws Exception {
		byte[] binaryData = decrypt(privateKey,
				Base64Utils.decode(base64String));
		String string = new String(binaryData);
		return string;
	}

	public static String encryptWithBase64(String string, RSAPublicKey publicKey)
			throws Exception {
		byte[] binaryData = encrypt(publicKey, string.getBytes());
		String base64String = Base64Utils.encode(binaryData);
		return base64String;
	}

	public static String encryptWithHex(String string, RSAPublicKey publicKey) throws Exception{
		byte[] binaryData = encrypt(publicKey, string.getBytes());
		String hexString = StringUtil.toHex(binaryData) ;
		return hexString;
	}
	/**
	 * 加密过程
	 * 
	 * @param publicKey
	 *            公钥
	 * @param plainTextData
	 *            明文数据
	 * @return
	 * @throws Exception
	 *             加密过程中的异常信息
	 */
	public static byte[] encrypt(RSAPublicKey publicKey, byte[] plainTextData)
			throws Exception {
		if (publicKey == null) {
			throw new Exception("加密公钥为空, 请设置");
		}
		Cipher cipher = null;
		try {
			cipher = Cipher.getInstance(RSAC);
			cipher.init(Cipher.ENCRYPT_MODE, publicKey);
			byte[] output = cipher.doFinal(plainTextData);
			return output;
		} catch (NoSuchAlgorithmException e) {
			throw new Exception("无此加密算法");
		} catch (NoSuchPaddingException e) {
			e.printStackTrace();
			return null;
		} catch (InvalidKeyException e) {
			throw new Exception("加密公钥非法,请检查");
		} catch (IllegalBlockSizeException e) {
			throw new Exception("明文长度非法");
		} catch (BadPaddingException e) {
			throw new Exception("明文数据已损坏");
		}
	}


	/**
	 * 解密过程
	 * 
	 * @param privateKey
	 *            私钥
	 * @param cipherData
	 *            密文数据
	 * @return 明文
	 * @throws Exception
	 *             解密过程中的异常信息
	 */
	public static byte[] decrypt(RSAPrivateKey privateKey, byte[] cipherData)
			throws Exception {
		if (privateKey == null) {
			throw new Exception("解密私钥为空, 请设置");
		}
		Cipher cipher = null;
		try {
			cipher = Cipher.getInstance(RSAC);
			cipher.init(Cipher.DECRYPT_MODE, privateKey);
			byte[] output = cipher.doFinal(cipherData);
			return output;
		} catch (NoSuchAlgorithmException e) {
			throw new NoSuchAlgorithmException("无此解密算法");
		} catch (NoSuchPaddingException e) {
			throw new NoSuchPaddingException("解密出错！不支持该填充机制");
		} catch (InvalidKeyException e) {
			throw new InvalidKeyException("解密私钥非法,请检查");
		} catch (IllegalBlockSizeException e) {
			throw new IllegalBlockSizeException("密文长度非法");
		} catch (BadPaddingException e) {
			throw new BadPaddingException("密文数据已损坏");
		}
	}



	//本地资源包更新接口Rsa解密公钥
	private static final String keyStr = "MIICdwIBADANBgkqhkiG9w0BAQEFAASCAmEwggJdAgEAAoGBAKaP70kWFd5V1zyP\n" +
			"bByYfLOoPudvGLmLWxICokur6m8pHZVQPBTX0tQ5GTUrPnGxoxM7pbnctizZ8NzY\n" +
			"pXiLznJsXPj1cSQWXmtnnodX4c7AjJbb7Rzhsi9eAILMRLpEysyYWDlC7ypQtBwx\n" +
			"vaiGwUz4xgHy1DhwRmEIdfH9Fd73AgMBAAECgYBHSLKQBPO3A+Xp9Te/xUMZYJgL\n" +
			"VHCweAOx4Y6z9A+RI3eVv5D3GqcQTJ+NMcS3w96XDKAQu3T8HQoIem/W936SqRCc\n" +
			"SkM7omVvVlDwNdezNaUY1QPXiR7kfyKAUswc0LVNuD+3HsgrFg4UKFHgOTxRHa3e\n" +
			"lpZMjoLBz4/Sjr5WAQJBANYLnggxCKQXa4xwwaaZ5PkrOws03YVDC42SrJ/b9gpD\n" +
			"RpqFkni3fPnWrl5xogBsys9B3E6Q43ebH8ywHGw7qhECQQDHNbSjpyeEFXpFyDfU\n" +
			"jJz9C6ZY2uOtt5heT8IeRr5qjkFgkPD9VhvCHgeKzvD0w7Fi6tgOYtjMImqvtcBB\n" +
			"/DCHAkBfT/cEEMmXQDKM7BzfUgILDjwRhm3qDGvVk+24lZsfAMwsvLP3e0V0fytQ\n" +
			"NOt9ovRAjERM2+nedb4YSIevjnXxAkEAjvqvAl7w785yii9gUv/xlXWNGW1RyNdj\n" +
			"Vx2r2AsylEwyBFtDfLEXD0Z6Z1iMfUEPyk2AcSLlM+7n3+F1cmbNdQJBAIgC8pQd\n" +
			"zRLKtp7NiJtMUehltfVkcUlwVrGiwdGqhClEWW5hDzHlvv7EvmEqtgo6XE4H+Bvf\n" +
			"hbK2/5XF36JrpPs=";

	public static RSAPrivateKey getRSAPrivateKey() {
		try {
			return (RSAPrivateKey) getPrivateKey(keyStr);
		} catch (Exception e) {
			e.printStackTrace();
		}
		return null;
	}


	public static String hamcsha1(byte[] data, byte[] key) {
		try {
			SecretKeySpec signingKey = new SecretKeySpec(key, "HmacSHA1");
			Mac mac = Mac.getInstance("HmacSHA1");
			mac.init(signingKey);
			return byte2hex(mac.doFinal(data));
		} catch (NoSuchAlgorithmException e) {
			e.printStackTrace();
		} catch (InvalidKeyException e) {
			e.printStackTrace();
		}
		return null;
	}

	//二行制转字符串
	public static String byte2hex(byte[] b) {
		StringBuilder hs = new StringBuilder();
		String stmp;
		for (int n = 0; b != null && n < b.length; n++) {
			stmp = Integer.toHexString(b[n] & 0XFF);
			if (stmp.length() == 1)
				hs.append('0');
			hs.append(stmp);
		}
		return hs.toString().toUpperCase();
	}

}