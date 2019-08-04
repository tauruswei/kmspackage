package com.sansec.kmspackage.service;


import com.sansec.kmspackage.result.Result;
import org.springframework.stereotype.Service;

/**
 * @Author: WeiBingtao/13156050650@163.com
 * @Version: 1.0
 * @Description:
 * @Date: 2019/6/17 18:01
 */

@Service
public interface PackageService {
    Result packageZipFile(String shell);
    Result verifySign();
}
