package com.sansec.kmspackage.controller;

import com.sansec.kmspackage.result.Result;
import com.sansec.kmspackage.service.UploadService;
import com.sansec.kmspackage.service.impl.PackageServiceImpl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;


/**
 * @Author: WeiBingtao/13156050650@163.com
 * @Version: 1.0
 * @Description:
 * @Date: 2019/11/15 11:35
 */
@Controller
@RequestMapping("/file")
public class FileListController {


    @Autowired
    PackageServiceImpl service;
    @Autowired
    UploadService uploadService;

    @RequestMapping("/getFileList")
    public String getFileList(Model model) {
        Result result = service.updateFileList();
        model.addAttribute("fileList", result.getData());
        return "thymeleaf/FileList";
    }
}
