package com.example.demo.util;

import com.google.zxing.*;
import com.google.zxing.client.j2se.BufferedImageLuminanceSource;
import com.google.zxing.client.j2se.MatrixToImageWriter;
import com.google.zxing.common.BitMatrix;
import com.google.zxing.common.HybridBinarizer;
import com.google.zxing.qrcode.decoder.ErrorCorrectionLevel;
import com.google.zxing.qrcode.encoder.QRCode;
import org.springframework.core.codec.Hints;

import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.nio.file.Path;
import java.sql.Timestamp;
import java.util.HashMap;

/**
 * @author chen_bq
 * @description QRcode生成二维码
 * @create: 2019-03-22 10:57
 **/
public class QRCodeUtil {

    public static void main(String[] args) {
        createQRcode("微信测试30000028","http://wenap.cn/?deviceCode=30000028");
        createQRcode("微信测试30000029","http://wenap.cn/?deviceCode=30000029");
        createQRcode("微信测试20000746","http://wenap.cn/?deviceCode=20000746");
        createQRcode("微信测试20002639","http://wenap.cn/?deviceCode=20002639");
        createQRcode("支付宝回调测试","http://wenap.cn/order/api/aliNotifyReceive");
    }

    public static void createQRcode(String fileName, String text, int height, int width){
        height = height==0?300:height;
        width = width==0?300:width;
        final String format = "png";
        final String content = text;

        //定义二维码的参数
        HashMap hints = new HashMap();
        hints.put(EncodeHintType.CHARACTER_SET, "utf-8");
        hints.put(EncodeHintType.ERROR_CORRECTION, ErrorCorrectionLevel.M);
        hints.put(EncodeHintType.MARGIN, 1);

        //生成二维码
        try{
            //OutputStream stream = new OutputStreamWriter();
            BitMatrix bitMatrix = new MultiFormatWriter().encode(content, BarcodeFormat.QR_CODE, width, height, hints);
//            Path file = new File("E:/QRcode/" + fileName + ".png").toPath();
            File file = new File("E:/QRcode/" + fileName + ".png");
            //文件夹存在与否判断
            if(!file.getParentFile().exists()){
                file.getParentFile().mkdirs();
            }
            MatrixToImageWriter.writeToPath(bitMatrix, format, file.toPath());
            //MatrixToImageWriter.writeToStream(bitMatrix, format, stream);
        }catch(Exception e){

        }
    }

    public static void createQRcode(String fileName, String text){
        createQRcode(fileName, text, 0, 0);
    }

    public static void readQRcode(String fileName){
        MultiFormatReader formatReader = new MultiFormatReader();
        File file = new File("E:/QRcode/" + fileName + ".png");

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
