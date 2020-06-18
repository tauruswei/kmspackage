package com.sansec.kmspackage.controller;

import com.sansec.kmspackage.tools.DownloadUtil;
import com.sansec.kmspackage.tools.ExecShell;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

/**
 * @Author: WeiBingtao/13156050650@163.com
 * @Version: 1.0
 * @Description:
 * @Date: 2019/9/27 14:35
 */
@RestController
public class DownloadController {

    @Value("${kmsPackage.updateFilePath}")
    String updatefilePath;
    @Value("${kmsPackage.template.Rest}")
    String rest;
    @Value("${kmsPackage.template.HadoopKMS}")
    String hadoopKMS;
    @Value("${kmsPackage.template.otheroperation}")
    String otheroperation;

    String path;

    @GetMapping(value = "/download/{module}")
    public void download(@PathVariable String module,HttpServletRequest request, HttpServletResponse response) throws Exception {
        int result = 0;
        switch (module){
            case "Rest":
                result = ExecShell.getExecShellProcess("bash /opt/KmsPackage/shell/RestZip.sh").waitFor();
                path = updatefilePath+"/Rest.zip";
                break;
            case "HadoopKMS":
                result = ExecShell.getExecShellProcess("bash /opt/KmsPackage/shell/HadoopKMSZip.sh").waitFor();
                path = updatefilePath+"/HadoopKMS.zip";
                break;
            case "sysfile":
                result = ExecShell.getExecShellProcess("bash /opt/KmsPackage/shell/sysfileZip.sh").waitFor();
                path = updatefilePath+"/sysfile.zip";
                break;
            case "otheroperation":
                path = updatefilePath+"/otheroperation.sh";
                break;
        }
        DownloadUtil.download(request, response, path);
        result = ExecShell.getExecShellProcess("rm -f /opt/KmsPackage/updatefile/updatefile/*.zip").waitFor();
    }

    @GetMapping(value = "/downloadTemplate/{module}")
    public void downloadTemplate(@PathVariable String module,HttpServletRequest request, HttpServletResponse response) throws Exception {
        switch (module){
            case "Rest":
                path=rest;
                break;
            case "HadoopKMS":
                path=hadoopKMS;
                break;
            case "otheroperation":
                path=otheroperation;
                break;
        }
        DownloadUtil.download(request, response, path);
    }
}
