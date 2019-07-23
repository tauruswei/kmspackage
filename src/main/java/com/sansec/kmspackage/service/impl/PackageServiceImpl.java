package com.sansec.kmspackage.service.impl;

import com.sansec.kmspackage.result.CodeMsg;
import com.sansec.kmspackage.result.Result;
import com.sansec.kmspackage.service.PackageService;
import com.sansec.kmspackage.tools.ExecShell;
import com.sansec.kmspackage.tools.LogTool;
import com.sansec.kmspackage.tools.SM2VerifyUtil;
import com.sansec.util.PrintUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.slf4j.MDC;
import org.springframework.stereotype.Service;
import org.springframework.util.Base64Utils;

import java.io.File;
import java.io.IOException;
import java.security.InvalidKeyException;
import java.security.NoSuchAlgorithmException;
import java.security.PublicKey;
import java.security.SignatureException;
import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;

import static com.sansec.kmspackage.tools.LogTool.returnErrorInfo;

/**
 * @Author: WeiBingtao/13156050650@163.com
 * @Version: 1.0
 * @Description:
 * @Date: 2019/6/17 18:01
 */
@Service
public class PackageServiceImpl implements PackageService {
    private Logger logger = LoggerFactory.getLogger(this.getClass());
    @Override
    public Result packageZipFile(String shell) {
        int result = 0;
        try {
            result = ExecShell.getExecShellProcess(shell).waitFor();
        } catch (InterruptedException e) {
            logger.error(LogTool.genLogMsg(MDC.get("ip"),"","", "", "", "",  CodeMsg.PACKAGE_ERROR.getCode(), CodeMsg.PACKAGE_ERROR.getMsg(),
                    returnErrorInfo().get(0),returnErrorInfo().get(1)));
            return Result.error(CodeMsg.PACKAGE_ERROR.fillArgs(e.getMessage()));
        }
        if(result==0){
            logger.info(LogTool.genLogMsg(MDC.get("ip"),"","", "", "", "", CodeMsg.PACKAGE_SUCCESS.getCode(), CodeMsg.PACKAGE_SUCCESS.getMsg(),
                    "",""));
            return Result.success("");
        }else{
            logger.error(LogTool.genLogMsg(MDC.get("ip"),"","", "", "", "",  CodeMsg.PACKAGE_ERROR.getCode(), CodeMsg.PACKAGE_ERROR.getMsg(),
                    returnErrorInfo().get(0),returnErrorInfo().get(1)));
            return Result.error(CodeMsg.PACKAGE_ERROR);
        }
    }


    @Override
    public Result verifySign(){
        //开始验签
        byte[] bs = SM2VerifyUtil.getMD5Two("/opt/zipfile/updatefile/updatefile.zip");
        byte b = (byte) 0x01;
        byte[] bsPad = new byte[32];
        bsPad = Arrays.copyOf(bs, bs.length+16);
        PrintUtil.printWithHex(bsPad);
        Arrays.fill(bsPad, 16, 32, b);

        File signFile = new File("/opt/zipfile/updatefile/updateSign");
        String fileReadResult = SM2VerifyUtil.fileRead2String(signFile);
        byte[] decodeRe = Base64Utils.decodeFromString(fileReadResult);
        System.out.println("-----------------签名值长度---------------"+decodeRe.length);
        PrintUtil.printWithHex(decodeRe);


        PublicKey publicKey = null;
        try {
            byte[] keyByte = SM2VerifyUtil.toByteArray("pubkey_ecc.0");
            publicKey = SM2VerifyUtil.getPublicKey(keyByte);
        } catch (IOException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
//            return JsonUtil.doError("Read public key:IOException,already rollback");
        }

        boolean verifyResult = false;
        try {
            verifyResult = SM2VerifyUtil.verifySign(publicKey, bsPad, decodeRe);
        } catch (InvalidKeyException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
            LogPrintUtil.printLog("error  ", operIP, uname, dateStr, "Upgrade", "Upload and upgrade", "Verify signature file:InvalidKeyException");
            return JsonUtil.doError("Verify signature file:InvalidKeyException，,already rollback");
        } catch (NoSuchAlgorithmException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
            LogPrintUtil.printLog("error  ", operIP, uname, dateStr, "Upgrade", "Upload and upgrade", "Verify signature file:NoSuchAlgorithmException");
            return JsonUtil.doError("Verify signature file:NoSuchAlgorithmException,already rollback");
        } catch (SignatureException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
            LogPrintUtil.printLog("error  ", operIP, uname, dateStr, "Upgrade", "Upload and upgrade", "Verify signature file:SignatureException");
            return JsonUtil.doError("Verify signature file:SignatureException,already rollback");
        }
        return Result.success("");
    }
}
