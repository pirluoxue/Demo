package com.example.demo.util.io;

import com.google.common.base.Strings;
import org.apache.http.Consts;

import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.io.*;
import java.util.Base64;

/**
 * @author chen_bq
 * @description
 * @create: 2019/11/22 18:02
 **/
public class EncoderUtils {

    private static final String PICTURE_PREFIX = "data:image/jpeg;base64,";

    public static String getPictureBase64(String fileName) {
        File file = new File(fileName);
        if (!file.exists()) {
            return null;
        }
        return getPictureBase64(file);
    }

    /**
     * @return java.lang.String
     * @Author chen_bq
     * @Description 图片转base64
     * @Date 2019/11/22 18:08
     * @Param [file]
     */
    public static String getPictureBase64(File file) {
        if (file == null) {
            return null;
        }
        InputStream inputStream = null;
        byte[] data = null;
        try {
            inputStream = new FileInputStream(file);
            data = new byte[inputStream.available()];
            inputStream.read(data);
            inputStream.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
        String fileBase64 = Base64.getEncoder().encodeToString(data);
        return PICTURE_PREFIX + fileBase64;
    }

    /**
     * @Author chen_bq
     * @Description 根据base64创建图片文件
     * @Date 2019/11/25 9:54
     * @Param [fileName, base64]
     * @return java.lang.String
     */
    public static boolean writeImageByBase64(String fileName, String base64) {
        File file = new File(fileName);
        if (!file.getParentFile().exists()) {
            if (!file.getParentFile().mkdirs()){
                return false;
            }
        }
        return writeImageByBase64(file, base64);
    }

    public static boolean writeImageByBase64(File file, String base64) {
        if (Strings.isNullOrEmpty(base64)) {
            return false;
        }
        if (base64.length() > Integer.MAX_VALUE){
            return false;
        }
        StringBuilder sb = new StringBuilder();
        sb.append(base64);
        byte[] decodeFile = Base64.getDecoder().decode(sb.toString().getBytes(Consts.UTF_8));
        OutputStream op = null;
        try {
            op = new FileOutputStream(file);
            op.write(decodeFile);
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            if (op != null) {
                try {
                    op.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }
        return true;
    }

    public static boolean compressImage(File file, File compressFile, int width, int height) {
        if (compressFile == null || file == null){
            return false;
        }
        if (width <= 0 || height <= 0){
            return false;
        }
        BufferedImage img = null;//原图
        BufferedImage bufferedImage = null;
        try {
            img = ImageIO.read(file);
            bufferedImage = new BufferedImage(width, height, BufferedImage.TYPE_INT_RGB);
            bufferedImage.getGraphics().drawImage(img, 0, 0, width, height, null); // 绘制缩小后的图
        } catch (IOException e) {
            e.printStackTrace();
        }
        OutputStream op = null;
        try {
            op = new FileOutputStream(compressFile);
            op.write(new byte[0]);
            ImageIO.write(bufferedImage, "jpg", op);
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            if (op != null) {
                try {
                    op.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }
        return true;
    }

}
