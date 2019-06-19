package com.sansec.kmspackage.tools;

import org.apache.commons.lang3.StringUtils;

import javax.servlet.http.HttpServletRequest;
import java.util.ArrayList;
import java.util.List;

public final class LogTool {
    public static final String STATUS_FAIL = "fail";
    public static String getIpAddr(HttpServletRequest request) {
        String ip = request.getHeader("x-forwarded-for");
        if (ip == null || ip.length() == 0 || "unknown".equalsIgnoreCase(ip)) {
            ip = request.getHeader("Proxy-Client-IP");
        }
        if (ip == null || ip.length() == 0 || "unknown".equalsIgnoreCase(ip)) {
            ip = request.getHeader("WL-Proxy-Client-IP");
        }
        if (ip == null || ip.length() == 0 || "unknown".equalsIgnoreCase(ip)) {
            ip = request.getRemoteAddr();
        }
        return ip;
    }
    /**
     * 指定格式生成日志
     *
     * @param ip
     * @param userName
     * @param keyName
     * @param operation
     * @param status
     * @param msg
     * @return
     */
    public static String genLogMsg(String ip, String userName, String subject, String keyName, String operation, String status, Integer code, String msg, String fileName, String lineNum) {
        StringBuilder sbf = new StringBuilder();
//        sbf.append("[operation:").append(operation).append(", ");
//        sbf.append("status:").append(status);
//
//        if (STATUS_FAIL.equals(status)) {
//            sbf.append(", errcode:").append(code).append(", ");
//            sbf.append("errmsg:").append(msg);
//        } else {
//            sbf.append(", tokenid:").append(msg);
//        }
//        sbf.append("] ");
        sbf.append("[code: ").append(code).append(", ");
        sbf.append("msg:").append(msg).append("] ");

        sbf.append("[session: ").append(ip).append("] ");
//        if (StringUtils.isNotBlank(userName)) {
//            sbf.append("[user ").append(userName);
//        }
//        if (StringUtils.isNotBlank(subject)) {
//            sbf.append(" : ").append(subject);
//        }
//        sbf.append("] ");
//
//        if (StringUtils.isNotBlank(keyName)) {
//            sbf.append("[args ");
//            sbf.append("keyname=").append(keyName).append("] ");
//        }
        if (StringUtils.isNotBlank(fileName)) {
            sbf.append("[position: ");
            sbf.append(fileName).append(":").append(lineNum).append("]");
        }
        return sbf.toString();
    }
    /**
     * 获取当前的文件名和行号
     */
    public static List<String> returnErrorInfo() {
        StackTraceElement[] stacktrace = Thread.currentThread().getStackTrace();
        StackTraceElement e = stacktrace[2];
        String errorFileName = e.getFileName();
        String errorLineNum = e.getLineNumber() + "";
        List list = new ArrayList<String>();
        list.add(errorFileName);
        list.add(errorLineNum);
        return list;
    }
}
