package com.sansec.kmspackage.controller;

import com.sansec.kmspackage.tools.FileTools;
import com.sansec.kmspackage.tools.Util;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.util.Base64Utils;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.io.*;
import java.text.SimpleDateFormat;
import java.util.*;

/**
 * @Author: WeiBingtao/13156050650@163.com
 * @Version: 1.0
 * @Description:
 * @Date: 2019/6/7 20:43
 */
@RestController
@RequestMapping
public class VersionController {
    @Value("${kmsPackage.version}")
    String versionFile;
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

    @GetMapping(value = "/getVersionInfo")
    public Map<String, Object> getVersionInfo(@RequestParam(value = "module", required = true, defaultValue = "SecKMS") String module) {
        Map<String, Object> map = new HashMap<>();
        //当前版本号
        Properties pps = new Properties();
        InputStream is = null;
        try {
            is = new FileInputStream(versionFile);
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }
        try {
            pps.load(is);
        } catch (IOException e) {
            e.printStackTrace();
        }
        String version = pps.getProperty(module);
        map.put("version", version);

//        获取文件的修改时间信息
        getLastModifiedTime(module, map);
        return map;
    }

    @GetMapping(value = "/updateVersionInfo")
    public Map<String, Object> updateVersionInfo(@RequestParam(value = "module", required = true, defaultValue = "SecKMS") String module, @RequestParam(value = "version", required = true, defaultValue = "2.13") String version) {

        Map<String, Object> map = new HashMap<>();
        //当前版本号
        Properties pps = new Properties();
        InputStream is = null;
        synchronized ("线程锁"){
            byte[] bytes2 = new byte[0];
            try {
                is = new FileInputStream(versionFile);
            } catch (FileNotFoundException e) {
                e.printStackTrace();
            }
            try {
                pps.load(is);
            } catch (IOException e) {
                e.printStackTrace();
            }

//        改变模块的版本号
            pps.setProperty(module,version);
            OutputStream  output = null;
            try {
                output = new FileOutputStream(versionFile);
            } catch (FileNotFoundException e) {
                e.printStackTrace();
            }
            try {
                pps.store(output, "andieguo modify" + new Date().toString());
            } catch (IOException e) {
                e.printStackTrace();
            }
            map.put("code", 0);
            map.put("msg", "success");
            map.put("data", "");
        }
        return map;
    }

    @GetMapping(value = "/getUploadInfo")
    public Map<String, Object> getUploadInfo(@RequestParam(value = "module", required = true, defaultValue = "SecKMS") String module) {
        Map<String, Object> map = new HashMap<>();
//        获取文件的修改时间信息
        getLastModifiedTime(module, map);
        return map;
    }

    private void getLastModifiedTime(@RequestParam(value = "module", required = true, defaultValue = "SecKMS") String module, Map<String, Object> map) {
        List<File> files = new ArrayList<>();
        List<File> files1 = new ArrayList<>();
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        switch (module) {
            case "SecKMS":
                files1 = FileTools.getFiles(secKmsPath, files);
                for (File file : files1) {
                    map.put(file.getName(), sdf.format(new Date(file.lastModified())));
                }
                break;
            case "ServerA":
                files1 = FileTools.getFiles(kmipPath, files);
                for (File file : files1) {
                    map.put(file.getName(), sdf.format(new Date(file.lastModified())));
                }
                break;
            case "ServerB":
                files1 = FileTools.getFiles(standardPath, files);
                for (File file : files1) {
                    map.put(file.getName(), sdf.format(new Date(file.lastModified())));
                }
                break;
            case "Rest":
                files1 = FileTools.getFiles(restPath, files);
                for (File file : files1) {
                    map.put(file.getName(), sdf.format(new Date(file.lastModified())));
                }
                break;
            case "HadoopKMS":
                files1 = FileTools.getFiles(hadoopKMSPath, files);
                for (File file : files1) {
                    map.put(file.getName(), sdf.format(new Date(file.lastModified())));
                }
                break;
            default:
                break;
        }
    }

}
