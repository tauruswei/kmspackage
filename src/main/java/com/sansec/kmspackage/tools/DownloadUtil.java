package com.sansec.kmspackage.tools;

import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.servlet.ServletOutputStream;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.File;
import java.io.IOException;
import java.io.PrintWriter;
import java.net.URLEncoder;

public class DownloadUtil {

	private static final Logger logger = LoggerFactory.getLogger(DownloadUtil.class);

	public static void downloadSuccess(HttpServletRequest request, HttpServletResponse response, String sName,
                                       byte[] value) {
		try {
			String filename = URLEncoder.encode(sName, "UTF-8").replaceAll("\\+", "%20");
			ServletOutputStream out = response.getOutputStream();
			response.setCharacterEncoding("utf-8");
			response.setContentType("multipart/form-data");
			response.setHeader("Content-Disposition", "attachment;fileName=" + filename);
			out.write(value);
			out.flush();
			out.close();
		} catch (IOException e) {
			String sMsg = "downloadSuccess error";
			logger.error(sMsg, e);
			response.reset();
			downloadError(request, response, "下载失败:" + e.getMessage());
		}

	}

	public static void downloadError(HttpServletRequest request, HttpServletResponse response, String sErrMsg) {
		try {

			String retUrl = request.getHeader("referer");

			response.setContentType("text/html;charset=utf-8");
			PrintWriter writer = response.getWriter();
			writer.println("<script>");
			writer.println("alert('" + sErrMsg + "');");
			if (StringUtils.isEmpty(retUrl)) {
				writer.println("window.location.href='" + retUrl + "'");
			}
			writer.println("</script>");
			writer.close();
		} catch (IOException e) {
			String sMsg = "downloadError error";
			logger.error(sMsg, e);
		}
	}
	public static void download(HttpServletRequest request, HttpServletResponse response, String packageWithSign) throws IOException {
		System.out.println("文件路径是："+packageWithSign);
		byte[] downloadData = FileTools.readFromFile(packageWithSign);
		File file = new File(packageWithSign);
		String name = file.getName();
		DownloadUtil.downloadSuccess(request, response, name, downloadData);
	}
}
