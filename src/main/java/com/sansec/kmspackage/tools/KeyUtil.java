package com.sansec.kmspackage.tools;




import com.sansec.asn1.DERBitString;
import com.sansec.asn1.DERNull;
import com.sansec.asn1.pkcs.*;
import com.sansec.asn1.x509.AlgorithmIdentifier;
import com.sansec.asn1.x509.RSAPublicKeyStructure;
import com.sansec.asn1.x509.SubjectPublicKeyInfo;
import com.sansec.jce.provider.SwxaProvider;

import java.io.IOException;
import java.math.BigInteger;
import java.security.*;
import java.security.spec.ECPoint;
import java.security.spec.InvalidKeySpecException;
import java.security.spec.PKCS8EncodedKeySpec;
import java.security.spec.X509EncodedKeySpec;

public class KeyUtil {

	public static final String PROVIDER = "SwxaJCE";

	/**
	 * generate rsa PublicKey from n and e
	 * @param n
	 * @param e
	 * @return
	 * @throws NoSuchAlgorithmException
	 * @throws NoSuchProviderException
	 * @throws InvalidKeySpecException
	 * @throws IOException
	 */
	public static PublicKey getRSAPublicKey(byte[] n, byte[] e) throws NoSuchAlgorithmException, NoSuchProviderException, InvalidKeySpecException, IOException {

		BigInteger bN = new BigInteger(1, n);
		BigInteger bE = new BigInteger(1, e);

		RSAPublicKeyStructure structure = new RSAPublicKeyStructure(bN, bE);
		SubjectPublicKeyInfo info = new SubjectPublicKeyInfo(new AlgorithmIdentifier(PKCSObjectIdentifiers.rsaEncryption, new DERNull()), structure.getDERObject());
		byte[] encodedKey = info.getEncoded();

		X509EncodedKeySpec keySpec = new X509EncodedKeySpec(encodedKey);
		KeyFactory keyFactory = KeyFactory.getInstance("RSA", SwxaProvider.PROVIDER_NAME);
		PublicKey key = keyFactory.generatePublic(keySpec);

		return key;
	}

	/**
	 * generate rsa PrivateKey from n,e,d,p,q,dp,dq and coef
	 * @param n
	 * @param e
	 * @param d
	 * @param p
	 * @param q
	 * @param dp
	 * @param dq
	 * @param coef
	 * @return
	 * @throws NoSuchAlgorithmException
	 * @throws NoSuchProviderException
	 * @throws InvalidKeySpecException
	 * @throws IOException
	 */
	public static PrivateKey getRSAPrivateKey(byte[] n, byte[] e, byte[] d, byte[] p, byte[] q, byte[] dp, byte[] dq, byte[] coef) throws NoSuchAlgorithmException, NoSuchProviderException, InvalidKeySpecException, IOException {

		BigInteger bN = new BigInteger(1, n);
		BigInteger bE = new BigInteger(1, e);
		BigInteger bD = new BigInteger(1, d);
		BigInteger bP = new BigInteger(1, p);
		BigInteger bQ = new BigInteger(1, q);
		BigInteger bP1 = new BigInteger(1, dp);
		BigInteger bQ1 = new BigInteger(1, dq);
		BigInteger bCoef = new BigInteger(1, coef);

		RSAPrivateKeyStructure structure = new RSAPrivateKeyStructure(bN,bE,bD,bP,bQ,bP1,bQ1,bCoef);
		PrivateKeyInfo info = new PrivateKeyInfo(new AlgorithmIdentifier(PKCSObjectIdentifiers.rsaEncryption, new DERNull()), structure.getDERObject());
		byte[] encodedKey = info.getEncoded();

		PKCS8EncodedKeySpec keySpec = new PKCS8EncodedKeySpec(encodedKey);
		KeyFactory keyFactory = KeyFactory.getInstance("RSA", SwxaProvider.PROVIDER_NAME);
		PrivateKey key = keyFactory.generatePrivate(keySpec);

		return key;
	}

	/**
	 * generate sm2 PublicKey from x and y
	 * @param x
	 * @param y
	 * @return
	 * @throws NoSuchAlgorithmException
	 * @throws NoSuchProviderException
	 * @throws InvalidKeySpecException
	 * @throws IOException
	 */
	public static PublicKey getSM2PubicKey(byte[] x, byte[] y) throws NoSuchAlgorithmException, NoSuchProviderException, InvalidKeySpecException, IOException {

		BigInteger bX = new BigInteger(1, x);
		BigInteger bY = new BigInteger(1, y);

		SM2PublicKeyStructure structure = new SM2PublicKeyStructure(new ECPoint(bX, bY));
		SubjectPublicKeyInfo info = new SubjectPublicKeyInfo(new AlgorithmIdentifier(PKCSObjectIdentifiers.ecPublicKey, GBObjectIdentifiers.sm2), structure.getDERObject());
		byte[] encodedKey = info.getEncoded();

		X509EncodedKeySpec keySpec = new X509EncodedKeySpec(encodedKey);
		KeyFactory keyFactory = KeyFactory.getInstance("SM2", SwxaProvider.PROVIDER_NAME);
		PublicKey key = keyFactory.generatePublic(keySpec);

		return key;
	}

	public static PrivateKey getSM2PrivateKey(byte[] x, byte[] y, byte[] d) throws NoSuchAlgorithmException, NoSuchProviderException, InvalidKeySpecException, IOException {

		DERBitString pubkey = null;
		if(x != null && x.length > 0 && y != null && y.length > 0){
			BigInteger bX = new BigInteger(1, x);
			BigInteger bY = new BigInteger(1, y);

			pubkey = new DERBitString(new SM2PublicKeyStructure(new ECPoint(bX, bY)).getPublicKey());
		}

		BigInteger bD = new BigInteger(1, d);

		SM2PrivateKeyStructure structure = new SM2PrivateKeyStructure(bD, pubkey, null);
		PrivateKeyInfo info = new PrivateKeyInfo(new AlgorithmIdentifier(PKCSObjectIdentifiers.ecPublicKey, GBObjectIdentifiers.sm2), structure.getDERObject());
		byte[] encodedKey = info.getEncoded();

		PKCS8EncodedKeySpec keySpec = new PKCS8EncodedKeySpec(encodedKey);
		KeyFactory keyFactory = KeyFactory.getInstance("SM2", SwxaProvider.PROVIDER_NAME);
		PrivateKey key = keyFactory.generatePrivate(keySpec);

		return key;
	}

}
