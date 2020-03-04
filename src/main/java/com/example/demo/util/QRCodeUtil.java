package com.example.demo.util;

import com.google.zxing.*;
import com.google.zxing.client.j2se.BufferedImageLuminanceSource;
import com.google.zxing.client.j2se.MatrixToImageWriter;
import com.google.zxing.common.BitMatrix;
import com.google.zxing.common.HybridBinarizer;
import com.google.zxing.qrcode.decoder.ErrorCorrectionLevel;

import javax.imageio.ImageIO;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.math.BigDecimal;
import java.math.MathContext;
import java.sql.Timestamp;
import java.util.HashMap;

/**
 * @author chen_bq
 * @description QRcode生成二维码
 * @create: 2019-03-22 10:57
 **/
public class QRCodeUtil {

    public static void main(String[] args) {
//        createQRcode("支付宝QRcode支付测试","https://qr.alipay.com/bax0480672qfy24mewg3204a");
//        createQRcode("微信QRcode支付测试","weixin://wxpay/bizpayurl?pr=d32VoWT");
//        createQRcode("嵌入二维码","weixin://wxpay/bizpayurl?pr=d32VoWT");
//        createLogoQRcode(new File("E:\\test\\659C5583C41D4DA1823BBA5DCD5EB8DC.png"), new File("E:\\test\\企业微信截图_15829834499418.png"));
        createLogoQRcode("E:\\test\\qrcode\\test2", "weixin://wxpay/bizpayurl?pr=d32VoWT", "E:\\test\\test123.png");
//        createLogoQRcode(new File("E:\\test\\659C5583C41D4DA1823BBA5DCD5EB8DC.png"), new File("E:\\test\\1989ff29-b351-4392-bb2c-2a377d0c837b.jpg"));
    }

    private static final Integer LOGO_WIDTH = 120;
    private static final Integer DEFAULT_QRCODE_WIDTH = 300;
    private static final Integer DEFAULT_QRCODE_HEIGH = 300;

    public static void createLogoQRcode(String filePath, String text, String logoPath) {
        createLogoQRcode(filePath, text, 0, 0, logoPath);
    }

    public static void createLogoQRcode(String filePath, String text, int height, int width, String logoPath) {
        BitMatrix bitMatrix = getQRcodeBitMatrix(text, height, width);
        BufferedImage qrCode = MatrixToImageWriter.toBufferedImage(bitMatrix);
        if (qrCode == null) {
            return;
        }
        File logFile = new File(logoPath);
        if (logFile == null) {
            return;
        }
        int qrCodeWidth = qrCode.getWidth();
        int qrCodeHeight = qrCode.getHeight();
        BufferedImage outputImage = new BufferedImage(qrCodeHeight, qrCodeWidth, BufferedImage.TYPE_INT_ARGB);
        Graphics2D g = (Graphics2D)outputImage.getGraphics();
        g.drawImage(qrCode, 0, 0, null);
        try {
            BufferedImage logoImage = ImageIO.read(logFile);//读取logo图片
            final int logoOutputHeigh = getLogHeigh(logoImage.getWidth(), logoImage.getHeight());
            g.setComposite(AlphaComposite.getInstance(AlphaComposite.SRC_OVER, 1f));
            g.drawImage(logoImage, (int)Math.round(qrCodeWidth / 2 - LOGO_WIDTH / 2), (int)Math.round(qrCodeHeight / 2 - logoOutputHeigh / 2), LOGO_WIDTH, logoOutputHeigh, null);
            g.dispose();
            String formatName = "png";
            ImageIO.write(outputImage, formatName, new File(filePath + "." + formatName));
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    /**
     * @return int
     * @Author chen_bq
     * @Description 根据原始的logo长宽，计算默认最大宽度下合适的长度
     * @Date 2020/3/1 10:06
     * @Param [originWidth, originHeigh]
     */
    private static int getLogHeigh(int originWidth, int originHeigh) {
        BigDecimal width = new BigDecimal(originWidth);
        BigDecimal heigh = new BigDecimal(originHeigh);
        BigDecimal scale = heigh.divide(width, new MathContext(2));
        return scale.multiply(new BigDecimal(LOGO_WIDTH)).intValue();
    }

    private static BitMatrix getQRcodeBitMatrix(String text, int height, int width) {
        height = height == 0 ? DEFAULT_QRCODE_HEIGH : height;
        width = width == 0 ? DEFAULT_QRCODE_WIDTH : width;
        final String content = text;
        //定义二维码的参数
        HashMap hints = new HashMap();
        hints.put(EncodeHintType.CHARACTER_SET, "utf-8");
        hints.put(EncodeHintType.ERROR_CORRECTION, ErrorCorrectionLevel.M);
        hints.put(EncodeHintType.MARGIN, 1);
        try {
            BitMatrix bitMatrix = new MultiFormatWriter().encode(content, BarcodeFormat.QR_CODE, width, height, hints);
            return bitMatrix;
        } catch (WriterException e) {
            e.printStackTrace();
        }
        return null;
    }

    public static void createQRcode(String fileName, String text, int height, int width) {
        height = height == 0 ? DEFAULT_QRCODE_HEIGH : height;
        width = width == 0 ? DEFAULT_QRCODE_WIDTH : width;
        final String format = "png";
        final String content = text;

        //定义二维码的参数
        HashMap hints = new HashMap();
        hints.put(EncodeHintType.CHARACTER_SET, "utf-8");
        hints.put(EncodeHintType.ERROR_CORRECTION, ErrorCorrectionLevel.M);
        hints.put(EncodeHintType.MARGIN, 1);

        //生成二维码
        try {
            //OutputStream stream = new OutputStreamWriter();
            BitMatrix bitMatrix = new MultiFormatWriter().encode(content, BarcodeFormat.QR_CODE, width, height, hints);
//            Path file = new File("E:/QRcode/" + fileName + ".png").toPath();
            File file = new File("D:/QRcode/" + fileName + ".png");
            //文件夹存在与否判断
            if (!file.getParentFile().exists()) {
                file.getParentFile().mkdirs();
            }
            MatrixToImageWriter.writeToPath(bitMatrix, format, file.toPath());
            //MatrixToImageWriter.writeToStream(bitMatrix, format, stream);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public static void createQRcode(String fileName, String text) {
        createQRcode(fileName, text, 0, 0);
    }

    public static void readQRcode(String fileName) {
        MultiFormatReader formatReader = new MultiFormatReader();
        File file = new File("D:/QRcode/" + fileName + ".png");

        BufferedImage image = null;
        try {
            image = ImageIO.read(file);
            BinaryBitmap binaryBitmap = new BinaryBitmap(new HybridBinarizer(new BufferedImageLuminanceSource(image)));

            //定义二维码的参数
            HashMap hints = new HashMap();
            hints.put(EncodeHintType.CHARACTER_SET, "utf-8");

            Result result = null;
            result = formatReader.decode(binaryBitmap, hints);
            System.out.println("二维码解析结果：" + result.toString());
            System.out.println("二维码的格式：" + result.getBarcodeFormat());
            System.out.println("二维码的文本内容：" + result.getText());
            System.out.println("getNumBits：" + result.getNumBits());
            System.out.println("getResultMetadata：" + result.getResultMetadata());
            System.out.println("getResultPoints：" + result.getResultPoints());
            System.out.println("getTimestamp：" + new Timestamp(result.getTimestamp()));
        } catch (Exception e) {
            e.printStackTrace();
        }
    }


}
