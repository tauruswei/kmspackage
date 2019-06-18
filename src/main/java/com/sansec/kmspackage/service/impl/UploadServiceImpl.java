package com.sansec.kmspackage.service.impl;

import com.sansec.kmspackage.tools.LogTool;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.slf4j.MDC;
import org.springframework.stereotype.Service;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.util.HashMap;
import java.util.Map;

import static com.sansec.kmspackage.tools.LogTool.returnErrorInfo;

/**
 * @Author: WeiBingtao/13156050650@163.com
 * @Version: 1.0
 * @Description:
 * @Date: 2019/6/17 18:38
 */
@Service
public class UploadServiceImpl {
    public Map<String, Object> getStringObjectMap(@RequestParam("file") MultipartFile file, Model model, String fileName,
                                                  String filePath) {
        Logger logger = LoggerFactory.getLogger(this.getClass());
        Map<String, Object> map = new HashMap<String, Object>();
        if (file.isEmpty()) {
            model.addAttribute("message", "The file is empty!");
            map.put("code", 1);
            map.put("msg", "The file is empty!");
            map.put("data", "");
            logger.error(LogTool.genLogMsg(MDC.get("ip"),"","", "", "", "", (Integer) map.get("code"), map.get("msg").toString(),
                    returnErrorInfo().get(0),returnErrorInfo().get(1)));
            return map;
        }
        try {
            System.out.println("路径是："+filePath);
            File localFile = new File(filePath, fileName);
            File parent = localFile.getParentFile();
            if (parent != null && !parent.exists()) {
                parent.mkdirs();
            }
            file.transferTo(localFile);
            model.addAttribute("message", "succes");
        } catch (Exception e) {
            logger.error(LogTool.genLogMsg(MDC.get("ip"),"","", "", "", "", (Integer) map.get("code"), map.get("msg").toString(),
                    returnErrorInfo().get(0),returnErrorInfo().get(1)));
        }
        map.put("code", 0);
        map.put("msg", "success");
        map.put("data", "");
        return map;
    }
}
