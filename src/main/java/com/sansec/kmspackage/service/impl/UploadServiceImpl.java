package com.sansec.kmspackage.service.impl;

import com.sansec.kmspackage.result.CodeMsg;
import com.sansec.kmspackage.result.Result;
import com.sansec.kmspackage.service.UploadService;
import com.sansec.kmspackage.tools.LogTool;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.slf4j.MDC;
import org.springframework.stereotype.Service;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.multipart.MultipartFile;

import java.io.*;

import static com.sansec.kmspackage.tools.LogTool.returnErrorInfo;

/**
 * @Author: WeiBingtao/13156050650@163.com
 * @Version: 1.0
 * @Description:
 * @Date: 2019/6/17 18:38
 */
@Service
public class UploadServiceImpl implements UploadService {
    @Override
    public Result getStringObjectMap(@RequestParam("file") MultipartFile file, Model model, String fileName, String filePath) {
        Logger logger = LoggerFactory.getLogger(this.getClass());
        if (file.isEmpty()) {
            logger.error(LogTool.genLogMsg(MDC.get("ip"), "", "", "", "", "",
                    CodeMsg.UPLOAD_ERROR.getCode(), CodeMsg.UPLOAD_ERROR.fillArgs("The file is empty!").getMsg(), returnErrorInfo().get(0), returnErrorInfo().get(1)));
            return Result.error(CodeMsg.UPLOAD_ERROR.fillArgs("The file is empty!"));
        }
        try {
            File localFile = new File(filePath, fileName);
            File parent = localFile.getParentFile();
            if (parent != null && !parent.exists()) {
                parent.mkdirs();
            }
            file.transferTo(localFile);
        } catch (Exception e) {
            logger.error(LogTool.genLogMsg(MDC.get("ip"), "", "", "", "", "",
                    CodeMsg.UPLOAD_ERROR.getCode(), CodeMsg.UPLOAD_ERROR.fillArgs(e.getMessage()).getMsg(), returnErrorInfo().get(0), returnErrorInfo().get(1)));
            return Result.error(CodeMsg.UPLOAD_ERROR.fillArgs(e.getMessage()));
        }
        logger.info(LogTool.genLogMsg(MDC.get("ip"), "", "", "", "", "",
                CodeMsg.UPLOAD_SUCCESS.getCode(), CodeMsg.UPLOAD_SUCCESS.getMsg(), "", ""));

        return Result.success("");
    }


}
