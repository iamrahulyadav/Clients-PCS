package edu10g.android.quiz.testseries.utility;

import android.util.Base64;

import java.math.BigInteger;
import java.security.KeyFactory;
import java.security.PublicKey;
import java.security.spec.RSAPublicKeySpec;
import java.security.spec.X509EncodedKeySpec;

import javax.crypto.Cipher;


public class RSAUtility {
	public static final String EXPONENT = "AQAB";
	public static String encrypt(String plainText, String key){
		try{
			PublicKey publicKey = KeyFactory.getInstance("RSA").generatePublic(new X509EncodedKeySpec(Base64.decode(key, Base64.DEFAULT)));
		    Cipher cipher = Cipher.getInstance("RSA/ECB/PKCS1Padding");
		    cipher.init(Cipher.ENCRYPT_MODE, publicKey);
		    return Base64.encodeToString(cipher.doFinal(plainText.getBytes("UTF-8")), Base64.DEFAULT);
		}catch (Exception e) {
			e.printStackTrace();
		}
		return null;
	}

	public static PublicKey getPublicKey(String key) throws Exception{
		byte[] modulusBytes = Base64.decode(key, 0);
		byte[] exponentBytes = Base64.decode(EXPONENT,    0);

		BigInteger modulus = new BigInteger(1, (modulusBytes) );
		BigInteger exponent = new BigInteger(1, (exponentBytes));

		RSAPublicKeySpec spec = new RSAPublicKeySpec(modulus, exponent);
		KeyFactory kf = KeyFactory.getInstance("RSA");
		return kf.generatePublic(spec);
	}
}