package com.sansec.kmspackage.model;

import lombok.Data;

/**
 * @Author: WeiBingtao/13156050650@163.com
 * @Version: 1.0
 * @Description:
 * @Date: 2019/11/15 10:11
 */
@Data
public class FileModel {
   public String fileName;
   public String fileHash;
   public String fileLastModifiedTime;
}

