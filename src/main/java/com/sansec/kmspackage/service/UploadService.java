package com.sansec.kmspackage.service;

import com.sansec.kmspackage.result.Result;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.multipart.MultipartFile;

/**
 * @Author: WeiBingtao/13156050650@163.com
 * @Version: 1.0
 * @Description:
 * @Date: 2019/6/17 18:38
 */
public interface UploadService {
    public Result getStringObjectMap(@RequestParam("file") MultipartFile file, Model model, String fileName, String filePath);
}
