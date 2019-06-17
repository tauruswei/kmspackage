package com.sansec.kmspackage.controller;

import com.sansec.kmspackage.tools.DownloadUtil;
import com.sansec.kmspackage.tools.ExecShell;
import com.sansec.kmspackage.tools.FileTools;
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
    @GetMapping(value = "/packageWithoutSign")
    public Map<String,Object> packageWithoutSign(){
        String shell ="bash /opt/KmsPackage/shell/PackageWithoutSign.sh";
        Map<String, Object> map = packageZipFile(shell);
        return map;
    }

    private Map<String, Object> packageZipFile(String shell) {
        Map<String,Object> map = new HashMap<>();
        int result = 0;
        try {
            result = ExecShell.getExecShellProcess(shell).waitFor();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        if(result==0){
            map.put("code",0);
            map.put("msg","打包成功！");
        }else{
            map.put("code",1);
            map.put("msg","打包失败！");
        }
        map.put("data",0);
        return map;
    }

    @PostMapping(value = "/packageWithSign")
    public Map<String,Object> packageWithSign(@RequestParam("file") MultipartFile file, Model model){
        //上传签名文件
        Map<String,Object> map = new HashMap<>();
        if (file.isEmpty()){
            model.addAttribute("message", "The file is empty!");
            map.put("code","1");
            map.put("msg","The file is empty!");
            map.put("data","");
            return map;
        }
        try{
            File localFile = new File(filePath,"updateSign");
            File parent = localFile.getParentFile();
            if(parent!=null&&!parent.exists()){
                parent.mkdirs();
            }
            file.transferTo(localFile);
        }catch (Exception e){
            e.printStackTrace();
        }

        //打包
        String shell = "bash /opt/KmsPackage/shell/PackageWithSign.sh";
        map = packageZipFile(shell);
        return map;
    }
    @GetMapping(value = "/downloadWithoutSign")
    public void download(HttpServletRequest request,HttpServletResponse response) throws Exception {
       DownloadUtil.download(request, response, packageWithoutSign);
    }
    @GetMapping(value = "/downloadWithSign")
    public void downloadWithSign(HttpServletRequest request,HttpServletResponse response) throws IOException {
        DownloadUtil.download(request, response, packageWithSign);
    }

//    @GetMapping(value = "/downloadByNmae/{name}")
//    public void downloadWithSign(@PathVariable("name") String name,HttpServletRequest request,HttpServletResponse response) throws IOException {
//        name = "/opt/xtrabackup/backup/"+name+".bak";
//        DownloadUtil.download(request, response, name);
//    }
    @GetMapping(value = "/downloadSqlErrorLog")
    public void downloadSqlErrorLog(HttpServletRequest request,HttpServletResponse response) throws IOException {
        DownloadUtil.download(request, response, sqlErrorLogPath);
    }

}
