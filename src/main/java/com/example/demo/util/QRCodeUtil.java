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
        createQRcode("喵喵喵1","喵喵喵0.0");
        createQRcode("喵喵喵2","喵喵喵-.-");
        createQRcode("喵喵喵3","喵喵喵233");
//        readQRcode("喵喵喵");
    }

    public static void createQRcode(String fileName, String text){
        final int width = 300;
        final int height = 300;
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
