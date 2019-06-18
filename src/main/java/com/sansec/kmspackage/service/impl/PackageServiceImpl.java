package com.sansec.kmspackage.service.impl;

import com.sansec.kmspackage.result.CodeMsg;
import com.sansec.kmspackage.result.Result;
import com.sansec.kmspackage.service.PackageService;
import com.sansec.kmspackage.tools.ExecShell;
import com.sansec.kmspackage.tools.LogTool;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.slf4j.MDC;
import org.springframework.stereotype.Service;

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
}
