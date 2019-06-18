package com.sansec.kmspackage.controller;

import com.sansec.kmspackage.result.CodeMsg;
import com.sansec.kmspackage.result.Result;
import com.sansec.kmspackage.service.PackageService;
import com.sansec.kmspackage.tools.DownloadUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.*;
import java.util.HashMap;
import java.util.Map;

/**
 * @Author: WeiBingtao/13156050650@163.com
 * @Version: 1.0
 * @Description:
 * @Date: 2019/6/7 20:43
 */
@RestController
@RequestMapping(value = "/package")
public class PackageController {

    @Value("${kmsPackage.version}")
    String versionFile;
    @Value("${kmsPackage.updateSign}")
    String filePath;
    @Value("${kmsPackage.packageWithoutSign}")
    String packageWithoutSign;
    @Value("${kmsPackage.packageWithSign}")
    String packageWithSign;
    @Value("${kmsPackage.sqlErrorLog}")
    String sqlErrorLogPath;

    @Autowired
    PackageService packageService;

    @GetMapping(value = "/packageWithoutSign")
    public Result packageWithoutSign() {
        String shell = "bash /opt/KmsPackage/shell/PackageWithoutSign.sh";
        Result result = packageService.packageZipFile(shell);
        return result;
    }
    @PostMapping(value = "/packageWithSign")
    public Result packageWithSign(@RequestParam("file") MultipartFile file, Model model) {
        //上传签名文件
        Map<String, Object> map = new HashMap<>();
        if (file.isEmpty()) {
            model.addAttribute("message", "The file is empty!");
            return Result.error(CodeMsg.UPLOAD_ERROR.fillArgs("The file is empty!"));
        }
        try {
            File localFile = new File(filePath, "updateSign");
            File parent = localFile.getParentFile();
            if (parent != null && !parent.exists()) {
                parent.mkdirs();
            }
            file.transferTo(localFile);
        } catch (Exception e) {
            return Result.error(CodeMsg.UPLOAD_ERROR.fillArgs(e.getMessage()));
        }

        //打包
        String shell = "bash /opt/KmsPackage/shell/PackageWithSign.sh";
        Result result = packageService.packageZipFile(shell);
        return result;
    }

    @GetMapping(value = "/downloadWithoutSign")
    public void download(HttpServletRequest request, HttpServletResponse response) throws Exception {
        DownloadUtil.download(request, response, packageWithoutSign);
    }

    @GetMapping(value = "/downloadWithSign")
    public void downloadWithSign(HttpServletRequest request, HttpServletResponse response) throws IOException {
        DownloadUtil.download(request, response, packageWithSign);
    }

    @GetMapping(value = "/downloadSqlErrorLog")
    public void downloadSqlErrorLog(HttpServletRequest request, HttpServletResponse response) throws IOException {
        DownloadUtil.download(request, response, sqlErrorLogPath);
    }

}
