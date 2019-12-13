package com.example.demo.util.io;

import lombok.Data;
import org.fusesource.hawtbuf.BufferOutputStream;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.*;
import java.nio.channels.FileLock;
import java.nio.file.Files;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.concurrent.atomic.LongAdder;

/**
 * @author chen_bq
 * @description
 * @create: 2019/10/16 11:36
 **/
@Data
public class IoStreamUtils {

    private static final Logger logger = LoggerFactory.getLogger(IoStreamUtils.class);

    public static final String DEFAULT_CHARACTER = "UTF-8";
    private static final long KILOMETER_BYTE = 1024L;
    private static final long MILLION_BYTE = 1024L * KILOMETER_BYTE;
    private static final LongAdder count = new LongAdder();

    public static boolean writeTextIntoFile(String text, String fullPathName) {
        return writeTextIntoFile(text, fullPathName, false);
    }

    /**
     * @return boolean
     * @Author chen_bq
     * @Description 输出Text文档
     * @Date 2019/10/16 14:11
     * @Param [text 文本, fullPathName 完整路径（包括文件名和后缀）, append 是否追加写入]
     */
    public static boolean writeTextIntoFile(String text, String fullPathName, boolean append) {
        File outFile = new File(fullPathName);
        if (!outFile.getParentFile().exists()) {
            if (!outFile.getParentFile().mkdirs()) {
                logger.warn("- ERROR creating output direcrory {}", outFile.getParentFile().getAbsolutePath());
            }
        }
        Writer out = null;
        try {
            out = new BufferedWriter(new OutputStreamWriter(new FileOutputStream(outFile, append), "UTF-8"));
            out.append(text);
            //关闭流
            out.flush();
            if (out != null) {
                out.close();
                return true;
            }
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
        return false;
    }

    public static String getStringByFile(File file) {
        BufferedReader br = null;
        InputStream inputStream = null;
        try {
            inputStream = new FileInputStream(file);
            br = new BufferedReader(new InputStreamReader(inputStream));
            byte[] bytes = new byte[inputStream.available()];
            inputStream.read(bytes);
            return new String(bytes);
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            if (inputStream != null){
                try {
                    inputStream.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }
        return null;
    }

    public static List getListByFileReadLine(File file) {
        BufferedReader br = null;
        List list = new ArrayList();
        try {
            InputStream inputStream = new FileInputStream(file);
            br = new BufferedReader(new InputStreamReader(inputStream));
            String line;
            while ((line = br.readLine()) != null) {
                list.add(line);
            }
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
        if (list == null) {
            return null;
        }
        return list;
    }

    private static File file;
    private static BufferedWriter bw;

    public static boolean fastWriteText(String text, String fullPathName) {
        if (!init(fullPathName)) {
            return false;
        }
        try {
            bw.append(text + "\r\n");
            bw.flush();
        } catch (IOException e) {
            e.printStackTrace();
        }
        outputPageLog(fullPathName);
        return true;
    }

//    public static void test(String fullPathName) {
//        Thread thread = new Thread(new Runnable() {
//            @Override
//            public void run() {
//                while (true) {
//                    try {
//                        Thread.sleep(100);
//                        outputPageLog(fullPathName);
//                    } catch (InterruptedException e) {
//                        e.printStackTrace();
//                    }
//                }
//            }
//        });
//        thread.start();
//    }

    /**
     * @return void
     * @Author chen_bq
     * @Description 拆分日志文件
     * @Date 2019/10/23 16:56
     * @Param [fullPathName]
     */
    private static void outputPageLog(String fullPathName) {
        if (!init(fullPathName)) {
            return;
        }
        // 限定1Mb
        if (file.length() / MILLION_BYTE >= 1L) {
            if (!separateMegaLog()) {
                init(fullPathName);
            }
        }
    }

    private static boolean separateMegaLog() {
        if (file == null) {
            return false;
        }
        String originName = file.getAbsolutePath();
        String tmpName = originName.replaceAll("(\\..*)", count.longValue() + "$1");
        try {
            Files.copy(file.toPath(), new File(tmpName).toPath());
            FileWriter fileWriter = new FileWriter(file);
            fileWriter.write("");
            fileWriter.flush();
            fileWriter.close();
            count.increment();
        } catch (IOException e) {
            e.printStackTrace();
            return false;
        }
        return true;
    }

    private static boolean init(String fullPathName) {
        return true & initFile(fullPathName) & initBufferedWriter();
    }

    private static boolean initBufferedWriter() {
        if (file == null) {
            return false;
        }
        try {
            bw = new BufferedWriter(new OutputStreamWriter(new FileOutputStream(file, true)));
        } catch (FileNotFoundException e) {
            e.printStackTrace();
            return false;
        }
        return true;
    }

    private static boolean initFile(String fullPathName) {
        if (file == null) {
            file = new File(fullPathName);
            if (!file.getParentFile().exists()) {
                if (!file.getParentFile().mkdirs()) {
                    logger.warn("- ERROR creating output direcrory {}", file.getParentFile().getAbsolutePath());
                    return false;
                }
            }
        }
        return true;
    }

    public static void closeWrite() {
        try {
            if (bw != null) {
                bw.close();
                bw = null;
            }
            file = null;
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

}
