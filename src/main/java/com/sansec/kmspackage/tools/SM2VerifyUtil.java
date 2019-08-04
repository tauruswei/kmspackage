package com.sansec.kmspackage.tools;

import com.sansec.asn1.pkcs.SM2SignStructure;
import com.sansec.asn1.pkcs.SM2StructureUtil;
import com.sansec.device.bean.SM2refSignature;


import org.springframework.core.io.ClassPathResource;
import org.springframework.util.Base64Utils;
import org.springframework.util.FileCopyUtils;
import org.springframework.util.ResourceUtils;

import java.io.*;
import java.nio.charset.StandardCharsets;
import java.security.*;
import java.util.Arrays;

public class SM2VerifyUtil {
	
//		public static void main(String[] args) {
//
//			// TODO Auto-generated method stub
//			byte[] bs = getMD5Two("G:/testverify/updatefile.zip");
//			byte b = (byte) 0x01;
//			byte[] bsPad = new byte[32];
//			bsPad = Arrays.copyOf(bs, bs.length+16);
//			Arrays.fill(bsPad, 16, 32, b);
//
//			File file = new File("G:/testverify/updateSign");
//			String result = fileRead2String(file);
//			byte[] decodeRe = Base64Utils.decodeFromString(result);
//
//			PublicKey publicKey = null;
//
//			try {
//				byte[] keyByte = toByteArray("G:/upfiles/verify/signTest/pubkey_ecc.0");
//				publicKey = getPublicKey(keyByte);
//			} catch (IOException e) {
//				// TODO Auto-generated catch block
//				e.printStackTrace();
//			}
//
//			try {
//				System.out.println(verifySign(publicKey, bsPad, decodeRe));
//			} catch (InvalidKeyException e) {
//				// TODO Auto-generated catch block
//				e.printStackTrace();
//			} catch (NoSuchAlgorithmException e) {
//				// TODO Auto-generated catch block
//				e.printStackTrace();
//			} catch (SignatureException e) {
//				// TODO Auto-generated catch block
//				e.printStackTrace();
//			}
//
//
//		}
	
//		private final static String[] strHex = { "0", "1", "2", "3", "4", "5",
//	            "6", "7", "8", "9", "a", "b", "c", "d", "e", "f" };
		
		public static byte[] getMD5Two(String path) {
	        MessageDigest md = null;
			try {
				md = MessageDigest.getInstance("MD5");
			} catch (NoSuchAlgorithmException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
	        md.update(readFileToByteArray(new File(path)));
	        byte b[] = md.digest();
	        return b;
	      }
		
		public static byte[] readFileToByteArray(File file) {
		      byte[] by = new byte[(int) file.length()];
		      try {
		          InputStream is = new FileInputStream(file);
		          ByteArrayOutputStream bytestream = new ByteArrayOutputStream();
		          byte[] bb = new byte[2048];
		          int ch;
		          ch = is.read(bb);
		          while (ch != -1) {
		              bytestream.write(bb, 0, ch);
		              ch = is.read(bb);
		          }
		          by = bytestream.toByteArray();
		      } catch (Exception ex) {
		          ex.printStackTrace();
		      }
		      return by;
		    }
		
		public static String fileRead2String(File file){
	        StringBuilder result = new StringBuilder();
	        try{
	            BufferedReader br = new BufferedReader(new FileReader(file));//构造一个BufferedReader类来读取文件
	            String s = null;
	            while((s = br.readLine())!=null){//使用readLine方法，一次读一行
	                result.append(System.lineSeparator()+s);
	            }
	            br.close();    
	        }catch(Exception e){
	            e.printStackTrace();
	        }
	        return result.toString();
	    }
	
		public static byte[] toByteArray(ClassPathResource cpr) throws IOException {
			byte[] bdata;
			try {
				bdata = FileCopyUtils.copyToByteArray(cpr.getInputStream());
				return bdata;
			} catch (IOException e) {
				e.printStackTrace();
			}
			return "fail".getBytes();
	    }
		
		public static PublicKey getPublicKey(byte[] key) {
			PublicKey publicKeyValue = null;
			 
			KeyFactory keyFactory = null;
	        String alg = "SM2";
	        try {
	        	keyFactory = KeyFactory.getInstance(alg, "SwxaJCE");
	            if (null != key) {
	                //下面的判断是为了支持SM2的裸密钥导入之后还能使用公钥进行包裹
	                if(key.length==68) {
	                    byte[] x = Arrays.copyOfRange(key, 4, 36);
	                    byte[] y = Arrays.copyOfRange(key, 36,68);
	                    publicKeyValue = KeyUtil.getSM2PubicKey(x, y);
	                }
	            }
			} catch (Exception e) {
				// TODO: handle exception
				e.printStackTrace();
			}
	            
			return publicKeyValue;
		}
		
		public static boolean verifySign(PublicKey publicKey ,byte[] inData, byte[] sigData) throws InvalidKeyException, NoSuchAlgorithmException, SignatureException {
			boolean verifyResult = false;
			try {
				// SM2ref Cipher
				byte[] r = new byte[32];
				byte[] s = new byte[32];
				System.arraycopy(sigData, 0, r, 0, 32);
				System.arraycopy(sigData, 32, s, 0, 32);
				SM2refSignature smSignature = new SM2refSignature(r, s);
				SM2SignStructure signStructure = SM2StructureUtil
						.convert(smSignature);

				Signature signature = Signature.getInstance("SM2",
						"SwxaJCE");
				signature.initVerify(publicKey);
				signature.update(inData);
				// //System.out.println("before signature.verify");
				verifyResult = signature.verify(signStructure.getDEREncoded());
			} catch (Exception e) {
				// : handle exception
				e.printStackTrace();
			}
			return verifyResult;
	    }

}
