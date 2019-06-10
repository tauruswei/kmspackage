package com.sansec.kmspackage.controller;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;

/**
 * @Author: WeiBingtao/13156050650@163.com
 * @Version: 1.0
 * @Description:
 * @Date: 2019/5/23 0023 21:52
 */
@RestController
@RequestMapping(value = "/upload/SecKM")
public class FileUploadController {
    @Value("${kmsPackage.SecKMS}")
    String filePath;
    @PostMapping("/war")
    public String upload(@RequestParam("file") MultipartFile file, Model model){
        if (file.isEmpty()){
            model.addAttribute("message", "The file is empty!");
            return "/uploadStatus";
        }
        try{
            byte[] bytes = file.getBytes();
            Path path = Paths.get(filePath+ File.separator+"SecKMS.war");
            Files.write(path, bytes);
            model.addAttribute("message", "succes");
        }catch (Exception e){
            e.printStackTrace();
        }
        return "/uploadStatus";
    }
}
