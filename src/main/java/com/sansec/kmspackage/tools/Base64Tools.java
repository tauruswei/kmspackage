package com.sansec.kmspackage.tools;

import org.apache.commons.codec.binary.Base64;

public class Base64Tools {
	private static Base64 base64 = new Base64();
	private static Base64 base64Line = new Base64(false);

	public static String encode(byte[] data) {
		return base64.encodeToString(data);
	}

	public static byte[] decode(String data) {
		return base64.decode(data.getBytes());
	}

	public static byte[] decode(byte[] data) {
		return base64.decode(data);
	}

	public static String decodeToString(String data) {
		return new String(decode(data));
	}

	public static String encodeLine(byte[] data) {
		return base64Line.encodeToString(data);
	}

	public static byte[] decodeLine(String data) {
		return base64Line.decode(data.getBytes());
	}

	public static byte[] decodeLine(byte[] data) {
		return base64Line.decode(data);
	}

	public static String decodeToStringLine(String data) {
		return new String(decodeLine(data));
	}
}
