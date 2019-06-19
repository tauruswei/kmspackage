package com.sansec.kmspackage.controller;

import com.sansec.kmspackage.result.CodeMsg;
import com.sansec.kmspackage.result.Result;
import com.sansec.kmspackage.tools.DownloadUtil;
import com.sansec.kmspackage.tools.ExecShell;
import com.sun.org.apache.bcel.internal.classfile.Code;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.File;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

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
    @Value("${kmsPackage.Rest}")
    String restPath;
    @Value("${kmsPackage.Standard}")
    String standardPath;
    @Value("${kmsPackage.HadoopKMS}")
    String hadoopKMSPath;

    @Value("${kmsPackage.sqlErrorLog}")
    String sqlErrorLogPath;

    @PostMapping("/SecKMS/war")
    public Map<String, Object> uploadSecKMSWar(@RequestParam("file") MultipartFile file, Model model) {
        return getStringObjectMap(file, model, "SecKMS.war", secKmsPath);
    }

    @PostMapping("/SecKMS/sql")
    public Map<String, Object> uploadSecKMSSql(@RequestParam("file") MultipartFile file, Model model, HttpServletRequest request, HttpServletResponse response) throws IOException {
        Map<String, Object> map = getStringObjectMap(file, model, "updatebase.sql", secKmsPath);
        if (!("0".equals(map.get("code").toString()))) {
            return map;
        }
        int result = 0;
        String shell = "bash /opt/KmsPackage/shell/testSql.sh";
        try {
            result = ExecShell.getExecShellProcess(shell).waitFor();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        if (result == 0) {
            map.put("code", "0");
            map.put("msg", "sucess！");
        } else {
            map.put("code", "1");
            map.put("msg", "sql有问题，详情请看error.log");
        }
        return map;
    }

    @PostMapping("/KMIPController/war")
    public Map<String, Object> uploadKMIPControllerSql(@RequestParam("file") MultipartFile file, Model model) {
        return getStringObjectMap(file, model, "KMIP-Controller-0.1.0-SNAPSHOT.war", secKmsPath);
    }

    @PostMapping("/KMIP/zip")
    public Map<String, Object> uploadKMIPZip(@RequestParam("file") MultipartFile file, Model model) {
        return getStringObjectMap(file, model, "KMIP.zip", kmipPath);
    }

    @PostMapping("/Rest/jar")
    public Map<String, Object> uploadRestJar(@RequestParam("file") MultipartFile file, Model model) {
        return getStringObjectMap(file, model, "kms-restful.jar", restPath);
    }

    @PostMapping("/Standard/zip")
    public Map<String, Object> uploadStandardZip(@RequestParam("file") MultipartFile file, Model model) {
        return getStringObjectMap(file, model, "update.zip", standardPath);
    }

    @PostMapping("/Standard/sh")
    public Map<String, Object> uploadStandardSh(@RequestParam("file") MultipartFile file, Model model) {
        return getStringObjectMap(file, model, "update.sh", standardPath);
    }

    @PostMapping("/HadoopKMS/zip")
    public Result uploadHadoopKMSZip(@RequestParam("file") MultipartFile file, Model model) throws InterruptedException {
        Map<String, Object> stringObjectMap = getStringObjectMap(file, model, "HadoopKMS.zip", hadoopKMSPath);
        if ((Integer) stringObjectMap.get("code") == 0) {
            ExecShell.getExecShellProcess("rm -rf /opt/KmsPackage/updatefile/updatefile/HadoopKMS").waitFor();
            String shell = "unzip -o -d /opt/KmsPackage/updatefile/updatefile/ /opt/KmsPackage/updatefile/updatefile/HadoopKMS.zip";
            int shellResult = ExecShell.getExecShellProcess(shell).waitFor();
            if (shellResult == 0) {
                return Result.success("");
            } else {
                return Result.error(CodeMsg.UNZIP_ERROR);
            }
        } else {
            return Result.error(CodeMsg.UPLOAD_ERROR);
        }
    }

    public Map<String, Object> getStringObjectMap(@RequestParam("file") MultipartFile file, Model model, String fileName, String filePath) {
        Map<String, Object> map = new HashMap<String, Object>();
        if (file.isEmpty()) {
            model.addAttribute("message", "The file is empty!");
            map.put("code", 1);
            map.put("msg", "The file is empty!");
            map.put("data", "");
            return map;
        }
        try {
            ExecShell.getExecShellProcess("rm -rf "+filePath+fileName).waitFor();
            System.out.println("路径是：" + filePath);
            File localFile = new File(filePath, fileName);
            File parent = localFile.getParentFile();
            if (parent != null && !parent.exists()) {
                parent.mkdirs();
            }
            file.transferTo(localFile);
            model.addAttribute("message", "succes");
        } catch (Exception e) {
            e.printStackTrace();
        }
        map.put("code", 0);
        map.put("msg", "success");
        map.put("data", "");
        return map;
    }

}