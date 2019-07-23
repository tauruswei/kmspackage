package com.sansec.kmspackage.test;


import org.springframework.http.ResponseEntity;
import org.springframework.web.client.RestTemplate;

import java.io.File;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

/**
 * @Author: WeiBingtao/13156050650@163.com
 * @Version: 1.0
 * @Description:
 * @Date: 2019/5/27 13:15
 */
public class Test {
//    @org.junit.Test
//    public void test(){
//        String caHost = "10.0.64.63";
//        int caPort= 8086;
//        String raPassword = "66666666";
//        String raPkcs12="";
//
//        try {
//            raPkcs12 = Base64Utils.encodeToString(FileTools.readFromFile("E:\\三未信安\\四维图新\\navinfo-ra.pfx"));
//        } catch (IOException e) {
//            e.printStackTrace();
//        }
//        KMS_SIWEI kms_siwei = new KMS_SIWEI();
//        Result result = kms_siwei.genCIToolpfxAndKey("weitest","Swxa1234.","0527-7", "0527-7passwd",24,
//                "CN=zhengsiwei,C=CN","",0,0,"",caHost,caPort,raPkcs12,raPassword);
//        System.out.println(JSON.toJSONString(result));
//    }

    @org.junit.Test
   public void test1(){
//        KMS_SIWEI kms_siwei = new KMS_SIWEI("E:\\三未信安\\四维图新\\现场技术支持\\swsds.ini");
//        Cipher cipher;
//        String caHost = "192.168.145.119";
//        int caPort = 8086;
//        String raPassword = "66666666";
//        String raPkcs12="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";

//        try {
//            raPkcs12 = Base64Utils.encodeToString(FileTools.readFromFile("E:\\三未信安\\四维图新\\navinfo-ra.pfx"));
//        } catch (IOException e) {
//            e.printStackTrace();
//        }
//      生成证书
//        Result result = kms_siwei.genCIToolpfxAndKey("fota","Siwei1234.","0530-7", "0530-7passwd",24, "CN=zhengsiwei,C=CN","",1024,0,"",
//                caHost,caPort,raPkcs12,raPassword);
//        System.out.println(JSON.toJSONString(result));
//
////      获取对称密钥
//        Result weitest = kms_siwei.getSymmKey("weitest", "Swxa1234.", "0528-17", null);
//        System.out.println(JSON.toJSONString(weitest));
//
////      获取证书
//        Result weitest1 = kms_siwei.getCertificate("weitest", "Swxa1234.", "0528-17");
//        System.out.println(JSON.toJSONString(weitest1));
////      获取口令
//        Result weitest2 = kms_siwei.getCertKey("weitest", "Swxa1234.", "0528-17");
//        System.out.println(JSON.toJSONString(weitest2));
//
////      签名
//        Result weitest3 = kms_siwei.kmsSign("fota", "Siwei1234.", "ef86515e1b5940d7a4fb3ab4a4b5efb2-Swxa-RSA", null, "swxa".getBytes());
//        System.out.println(new String((byte[]) weitest3.getData()));
////
////      验签
//        Result weitest4 = kms_siwei.kmsVerify("MIIDsTCCApmgAwIBAgIIJ5SAOKsLrkswDQYJKoZIhvcNAQELBQAwgZQxCzAJBgNVBAYTAkNOMQswCQYDVQQIDAJTRDELMAkGA1UEBwwCSk4xDzANBgNVBAoMBlNBTlNFQzELMAkGA1UECwwCQ0ExFzAVBgNVBAMMDlNhbnNlYyBEZW1vIENBMSQwIgYJKoZIhvcNAQkBFhVzdXBwb3J0QHNhbnNlYy5jb20uY24xDjAMBgNVBBAMBUppbmFuMB4XDTE5MDUyODEwNDI1OVoXDTIxMDUxNzEwNDI1OVowIjELMAkGA1UEBhMCQ04xEzARBgNVBAMMCnpoZW5nc2l3ZWkwgZ8wDQYJKoZIhvcNAQEBBQADgY0AMIGJAoGBAJaC1F9Z6m5JrDZMzURzbnStBVkQSrmKo1MMmzlNC8kBZb6qnoiYMquLgzz03isFIHdUpFPQuetAd5FBsSroHfv0DQMWFQD7bIMp/QMGhgvSyVNv8XPlcSxAah/ZmIoWQCCJh3qzXfG3JWED2Qg9ZJ5Bzo6KvOwTTb9jLuL5JFzHAgMBAAGjgfswgfgwgcgGA1UdIwSBwDCBvYAUMkRydS7+wLzUqEnltY+9v43+tw+hgZqkgZcwgZQxCzAJBgNVBAYTAkNOMQswCQYDVQQIDAJTRDELMAkGA1UEBwwCSk4xDzANBgNVBAoMBlNBTlNFQzELMAkGA1UECwwCQ0ExFzAVBgNVBAMMDlNhbnNlYyBEZW1vIENBMSQwIgYJKoZIhvcNAQkBFhVzdXBwb3J0QHNhbnNlYy5jb20uY24xDjAMBgNVBBAMBUppbmFugggtRlreu76CZjAdBgNVHQ4EFgQUYKAaCa6ewStToB0xCh5pccmv3t8wDAYDVR0TAQH/BAIwADANBgkqhkiG9w0BAQsFAAOCAQEAQTpoS21Dz2EJaXsOUNud7lM+GraS4uH9mQvDH3GeiAJ7qxb3vZ/yfZUAi+6hrNnoXyEA6oiBzl2/Z4YfXkg3IE9A9JRNuEg6X9rRn4hTu2/UdoXkIA+KQF6gYkjTtrp0U07FYrhCGwscPZojPJExRsFw+W1LYEVbITx7NnPom2G4kGbTzgSXHNrV6ZxTOd/FMcpJu93fofdxak0KhaI8uMiLF8AH3duz59EHRgryAz1DtXhUS7ibxS2kdeAtD8duIRhxI1Pe/mUGPCBNtaY+Z1kze5xgoFggZahVal55tp0fBGZPp47rCfY2BqDBEvvqyIbO/LLTMsIxrqXV9qnFtQ==",
//                null, "swxa".getBytes(), (byte[]) weitest3.getData());
//        System.out.println(result.getMsg());
//
////      加密测试
//        Result weitest5 = kms_siwei.getSymmKey("weitest", "Swxa1234.", "0528-17", null);
//        Result encrypt = kms_siwei.encrypt(Base64Utils.decodeFromString((String) weitest5.getData()), "SWXA".getBytes());
//
//        //解密测试
//        Result decrypt = kms_siwei.decrypt(Base64Utils.decodeFromString((String) weitest5.getData()), (byte[]) encrypt.getData());
//        System.out.println("--------------------解密结果-------------------");
//        System.out.println(new String((byte[]) decrypt.getData()));

    }
//    @org.junit.Test
//    public void test2(){
//        String fileName = "hello.txt";
//        String path = "e:/hello/";
//        File localFile = new File(path,fileName);
//        File file =new File("E:\\worklist.sql");
//
//        try {
//            FileTools.readFromFile("E:\\worklist.sql");
//            FileTools.writeToFile();
//        } catch (IOException e) {
//            e.printStackTrace();
//        }
//
//    }
    @org.junit.Test
    public void test3(){
        RestTemplate restTemplate = new RestTemplate();
        ResponseEntity<String> responseEntity = restTemplate.getForEntity("http://http://39.106.212.6:8080/KmsPackage/package/downloadByNmae/"+1, String.class);
    }
    @org.junit.Test
    public void test4(){
        Map<String,Object> map = new HashMap<>();
        map.put("code",1);
        map.put("msg","hello");
        System.out.println((Integer) map.get("code")+1);
        System.out.println(map.get("msg").toString());
    }

}
