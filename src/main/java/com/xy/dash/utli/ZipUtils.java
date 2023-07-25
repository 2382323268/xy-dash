package com.xy.dash.utli;

import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.*;
import java.util.zip.ZipEntry;
import java.util.zip.ZipOutputStream;

/**
 * @Author: xiangwei
 * @Date: 2023/7/18 17:41
 * @Description
 **/
public class ZipUtils {

    /**
     * sourceFile一定要是文件夹
     * 默认会在同目录下生成zip文件
     *
     * @throws Exception
     */
    public static void fileToZip(String path, String fileName) {
        ServletRequestAttributes requestAttributes = (ServletRequestAttributes) RequestContextHolder.getRequestAttributes();
        HttpServletRequest request = requestAttributes.getRequest();
        HttpServletResponse response = requestAttributes.getResponse();

        File sourceFile = new File(path);
        if (!sourceFile.exists()) {
            throw new RuntimeException("不存在");
        }
        if (!sourceFile.isDirectory()) {
            throw new RuntimeException("不是文件夹");
        }
        ZipOutputStream zos = null;
        try {
            formatFileName(response, request, fileName);
            zos = new ZipOutputStream(response.getOutputStream());
            fileToZip(zos, sourceFile, "");

        } catch (Exception e) {

        } finally {
            try {
                zos.close();
            } catch (IOException e) {
                e.printStackTrace();
            }

        }
    }


    private static void fileToZip(ZipOutputStream zos, File sourceFile, String path) throws Exception {

        //如果是文件夹只创建zip实体即可，如果是文件，创建zip实体后还要读取文件内容并写入
        if (sourceFile.isDirectory()) {
            path = path + sourceFile.getName() + "/";
            ZipEntry zipEntry = new ZipEntry(path);
            zos.putNextEntry(zipEntry);
            for (File file : sourceFile.listFiles()) {
                fileToZip(zos, file, path);
            }
        } else {
            //创建ZIP实体，并添加进压缩包
            ZipEntry zipEntry = new ZipEntry(path + sourceFile.getName());
            zos.putNextEntry(zipEntry);
            byte[] bufs = new byte[1024 * 10];
            //读取待压缩的文件并写进压缩包里
            FileInputStream fis = new FileInputStream(sourceFile);
            BufferedInputStream bis = new BufferedInputStream(fis, 1024 * 10);
            int read = 0;
            while ((read = bis.read(bufs, 0, 1024 * 10)) != -1) {
                zos.write(bufs, 0, read);
            }
            bis.close();
            fis.close();
        }
    }


    /**
     * @methodname formatFileName
     * @Description {设置响应信息和格式化附件名字}
     * @author admin
     */
    private static void formatFileName(HttpServletResponse response, HttpServletRequest request, String fileName) throws UnsupportedEncodingException {
        fileName += ".zip";
        // 获取浏览器信息
        if (request.getHeader("User-Agent").toLowerCase().indexOf("firefox") > 0) {
            fileName = new String(fileName.getBytes("GB2312"), "ISO-8859-1");
        } else {
            // 处理中文文件名的问题
            fileName = java.net.URLEncoder.encode(fileName, "UTF-8");
            fileName = new String(fileName.getBytes("UTF-8"), "GBK");
        }
        // 清除首部的空白行
        response.reset();
        // 设置Response容器的编码
        response.setCharacterEncoding("UTF-8");
        // 不同类型的文件对应不同的MIME类型
        response.setContentType("application/x-msdownload");
        response.setHeader("Content-Disposition", "attachment; filename=" + fileName);
    }

}
