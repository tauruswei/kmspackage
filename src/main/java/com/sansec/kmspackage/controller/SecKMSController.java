package com.sansec.kmspackage.controller;

import com.sansec.kmspackage.result.CodeMsg;
import com.sansec.kmspackage.result.Result;
import com.sansec.kmspackage.service.UploadService;
import com.sansec.kmspackage.tools.ExecShell;
import com.sansec.kmspackage.tools.LogTool;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.slf4j.MDC;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

import static com.sansec.kmspackage.tools.LogTool.returnErrorInfo;

/**
 * @Author: WeiBingtao/13156050650@163.com
 * @Version: 1.0
 * @Description:
 * @Date: 2019/6/7 13:03
 */
 @RestController
 @RequestMapping(value = "/upload")
 public class SecKMSController {
    @Value("${kmsPackage.SecKMS}")
    String secKmsPath;
    @Value("${kmsPackage.KMIP}")
    String kmipPath;
    @Value("${kmsPackage.Standard}")
    String standardPath;
    @Value("${kmsPackage.updateFilePath}")
    String updateFilePath;
    @Value("${kmsPackage.sqlErrorLog}")
    String sqlErrorLogPath;

    @Autowired
    UploadService uploadService;

    private Logger logger = LoggerFactory.getLogger(this.getClass());

    @PostMapping("/SecKMS/war")
    public Result uploadSecKMSWar(@RequestParam("file") MultipartFile file, Model model) {
        return uploadService.getStringObjectMap(file, model, "SecKMS.war", secKmsPath);
    }
    @PostMapping("/SecKMS/{sqlName}")
    public Result uploadSecKMSSql(@RequestParam("file") MultipartFile file, @PathVariable String sqlName,Model model, HttpServletRequest request, HttpServletResponse response) throws IOException {
        Result  result = uploadService.getStringObjectMap(file, model, sqlName, secKmsPath);
        if (0!=result.getCode()||"seckmsfb.sql".equals(sqlName)) {
            return result;
        }
        int shellResult = 0;
        String shell = "bash /opt/KmsPackage/shell/testSql.sh";
        try {
            shellResult = ExecShell.getExecShellProcess(shell).waitFor();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        if (shellResult == 0) {
            logger.info(LogTool.genLogMsg(MDC.get("ip"),"","", "", "", "", CodeMsg.SQL_SUCCESS.getCode(), CodeMsg.SQL_SUCCESS.getMsg(),
                    "",""));
            return Result.success("");
        } else {
            logger.error(LogTool.genLogMsg(MDC.get("ip"),"","", "", "", "", CodeMsg.SQL_ERROR.getCode(), CodeMsg.SQL_ERROR.getMsg(),
                    returnErrorInfo().get(0),returnErrorInfo().get(1)));
            return Result.error(CodeMsg.SQL_ERROR);
        }
    }
        @PostMapping("/KMIPController/war")
    public Result uploadKMIPControllerSql(@RequestParam("file") MultipartFile file, Model model) {
        return uploadService.getStringObjectMap(file, model, "KMIP-Controller-0.1.0-SNAPSHOT.war", secKmsPath);
    }

    @PostMapping("/KMIP/zip")
    public Result uploadKMIPZip(@RequestParam("file") MultipartFile file, Model model) {
        return uploadService.getStringObjectMap(file, model, "KMIP.zip", kmipPath);
    }

    @PostMapping("/Rest/zip")
    public Result uploadRestZip(@RequestParam("file") MultipartFile file, Model model) throws InterruptedException {
        Result stringObjectMap = uploadService.getStringObjectMap(file, model, "Rest.zip", updateFilePath);
        if (stringObjectMap.getCode() == 0) {
            ExecShell.getExecShellProcess("rm -rf /opt/KmsPackage/updatefile/updatefile/Rest").waitFor();
            String shell = "unzip -o -d /opt/KmsPackage/updatefile/updatefile/ /opt/KmsPackage/updatefile/updatefile/Rest.zip";
            int shellResult = ExecShell.getExecShellProcess(shell).waitFor();
            if (shellResult == 0) {
                int i = ExecShell.getExecShellProcess("rm -f /opt/KmsPackage/updatefile/updatefile/Rest.zip").waitFor();
                logger.info(LogTool.genLogMsg(MDC.get("ip"),"","", "", "", "", CodeMsg.UPLOAD_SUCCESS.getCode(), CodeMsg.UPLOAD_SUCCESS.getMsg(),
                        "",""));
                return Result.success("");
            } else {
                logger.error(LogTool.genLogMsg(MDC.get("ip"),"","", "", "", "", CodeMsg.UNZIP_REST_ERROR.getCode(), CodeMsg.UNZIP_REST_ERROR.getMsg(),
                        returnErrorInfo().get(0),returnErrorInfo().get(1)));
                return Result.error(CodeMsg.UNZIP_REST_ERROR);
            }
        } else {
            return Result.error(CodeMsg.UPLOAD_ERROR);
        }
    }
    @PostMapping("/sysfile/zip")
    public Result uploadSysfileZip(@RequestParam("file") MultipartFile file, Model model) throws InterruptedException {
        Result stringObjectMap = uploadService.getStringObjectMap(file, model, "sysfile.zip", updateFilePath);
        if (stringObjectMap.getCode() == 0) {
            ExecShell.getExecShellProcess("rm -rf /opt/KmsPackage/updatefile/updatefile/sysfile").waitFor();
            String shell = "unzip -o -d /opt/KmsPackage/updatefile/updatefile/ /opt/KmsPackage/updatefile/updatefile/sysfile.zip";
            int shellResult = ExecShell.getExecShellProcess(shell).waitFor();
            if (shellResult == 0) {
                int i = ExecShell.getExecShellProcess("rm -f /opt/KmsPackage/updatefile/updatefile/sysfile.zip").waitFor();
                logger.info(LogTool.genLogMsg(MDC.get("ip"),"","", "", "", "", CodeMsg.UPLOAD_SUCCESS.getCode(), CodeMsg.UPLOAD_SUCCESS.getMsg(),
                        "",""));
                return Result.success("");
            } else {
                logger.error(LogTool.genLogMsg(MDC.get("ip"),"","", "", "", "", CodeMsg.UNZIP_SYSFILE_ERROR.getCode(), CodeMsg.UNZIP_SYSFILE_ERROR.getMsg(),
                        returnErrorInfo().get(0),returnErrorInfo().get(1)));
                return Result.error(CodeMsg.UNZIP_SYSFILE_ERROR);
            }
        } else {
            return Result.error(CodeMsg.UPLOAD_ERROR);
        }
    }

    @PostMapping("/otherOperation/sh")
    public Result uploadOtherOperationSH(@RequestParam("file") MultipartFile file, Model model) {
        return uploadService.getStringObjectMap(file, model, "otheroperation.sh", updateFilePath);
    }
    @PostMapping("/Standard/zip")
    public Result uploadStandardZip(@RequestParam("file") MultipartFile file, Model model) {
        return uploadService.getStringObjectMap(file, model, "update.zip", standardPath);
    }

    @PostMapping("/Standard/sh")
    public Result uploadStandardSh(@RequestParam("file") MultipartFile file, Model model) {
        return uploadService.getStringObjectMap(file, model, "update.sh", standardPath);
    }
    @PostMapping("/HadoopKMS/zip")
    public Result uploadHadoopKMSZip(@RequestParam("file") MultipartFile file, Model model) throws InterruptedException {
        Result stringObjectMap = uploadService.getStringObjectMap(file, model, "HadoopKMS.zip", updateFilePath);
        if (stringObjectMap.getCode() == 0) {
            ExecShell.getExecShellProcess("rm -rf /opt/KmsPackage/updatefile/updatefile/HadoopKMS").waitFor();
            String shell = "unzip -o -d /opt/KmsPackage/updatefile/updatefile/ /opt/KmsPackage/updatefile/updatefile/HadoopKMS.zip";
            int shellResult = ExecShell.getExecShellProcess(shell).waitFor();
            if (shellResult == 0) {
                int i = ExecShell.getExecShellProcess("rm -f /opt/KmsPackage/updatefile/updatefile/HadoopKMS.zip").waitFor();
                logger.info(LogTool.genLogMsg(MDC.get("ip"),"","", "", "", "", CodeMsg.UPLOAD_SUCCESS.getCode(), CodeMsg.UPLOAD_SUCCESS.getMsg(),
                        "",""));
                return Result.success("");
            } else {
                logger.error(LogTool.genLogMsg(MDC.get("ip"),"","", "", "", "", CodeMsg.UNZIP_HADOOPKMS_ERROR.getCode(), CodeMsg.UNZIP_HADOOPKMS_ERROR.getMsg(),
                        returnErrorInfo().get(0),returnErrorInfo().get(1)));
                return Result.error(CodeMsg.UNZIP_HADOOPKMS_ERROR);
            }
        } else {
            return Result.error(CodeMsg.UPLOAD_ERROR);
        }
    }

 }