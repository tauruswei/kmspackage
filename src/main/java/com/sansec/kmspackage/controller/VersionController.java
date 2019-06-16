package com.sansec.kmspackage.controller;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpServletRequest;
import java.io.*;
import java.lang.reflect.MalformedParameterizedTypeException;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import java.util.Properties;

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
        return map;
    }

    @GetMapping(value = "/updateVersionInfo")
    public Map<String, Object> updateVersionInfo(@RequestParam(value = "module", required = true, defaultValue = "SecKMS") String module, @RequestParam(value = "version", required = true, defaultValue = "2.13") String version) {

        Map<String, Object> map = new HashMap<>();
        //当前版本号
        Properties pps = new Properties();
        InputStream is = null;
        synchronized ("线程锁"){
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
            map.put("code", "0");
            map.put("msg", "success");
            map.put("data", "");
        }
        return map;
    }
}
