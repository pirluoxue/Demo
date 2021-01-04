package com.example.demo.util;

import org.apache.commons.lang3.StringUtils;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.util.Arrays;
import java.util.List;
import java.util.zip.ZipEntry;
import java.util.zip.ZipOutputStream;

/**
 * @Author bangqiang_chen
 * @Description 压缩工具类
 * @Date 16:24 2020/9/24
 **/
public class CompressUtil {

    public static void zip(List<String> compressedList, String outputFullName)
            throws Exception {
        zip(compressedList, outputFullName, null);
    }

    /**
     * @return void
     * @Author bangqiang_chen
     * @Description 将列表内的文件进行压缩
     * @Date 16:24 2020/9/24
     * @Param [compressedList 文件地址列表(含后缀), outputFullName 压缩包地址(含后缀), innerReNameList 需要重命名的列表 (含后缀)]
     **/
    public static void zip(List<String> compressedList, String outputFullName, List<String> innerReNameList)
            throws Exception {
        if (compressedList == null || compressedList.size() <= 0) {
            return;
        }
        //要生成的压缩文件
        ZipOutputStream out = new ZipOutputStream(new FileOutputStream(outputFullName));
        byte[] buffer = new byte[1024];
        for (int i = 0; i < compressedList.size(); i++) {
            String compressedFile = compressedList.get(i);
            File file = new File(compressedFile);
            if (file == null) {
                continue;
            }
            FileInputStream fis = new FileInputStream(file);
            if (innerReNameList != null && innerReNameList.size() > i && !StringUtils.isEmpty(innerReNameList.get(i))) {
                out.putNextEntry(new ZipEntry(innerReNameList.get(i)));
            } else {
                out.putNextEntry(new ZipEntry(compressedFile));
            }
            int len;
            // 读入需要下载的文件的内容，打包到zip文件
            while ((len = fis.read(buffer)) > 0) {
                out.write(buffer, 0, len);
            }
            out.closeEntry();
            fis.close();
        }
        out.close();
    }

    public static void main(String[] args) {
        try {
            zip(Arrays.asList("D:\\workplace\\collection\\target\\classes\\RuijieCloud-EU-2020-09-24.xls"),
                    "D:\\workplace\\collection\\target\\classes\\Test.zip",
                    Arrays.asList("D:\\workplace\\collection\\target\\classes\\MIE.xls"));
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

}
