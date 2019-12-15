package com.example.demo.util.io;

import com.example.demo.model.entity.enums.LogCode;
import com.example.demo.model.entity.simple.MatchInfo;
import com.example.demo.util.common.CommonUtil;
import com.google.common.base.Strings;
import lombok.Data;
import org.apache.xmlbeans.impl.regex.Match;
import org.aspectj.weaver.tools.MatchingContext;
import org.fusesource.hawtbuf.BufferOutputStream;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.*;
import java.nio.channels.FileLock;
import java.nio.file.Files;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;
import java.util.List;
import java.util.concurrent.atomic.LongAdder;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

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

    public static void findPreciseForDir(String key, File dirFile, List outList){
        if (Strings.isNullOrEmpty(key) || dirFile == null){
            return;
        }
        List list = new ArrayList();
        list.add(key);
        findPreciseForDir(list, dirFile, outList);
    }

    /**
     *  @author: chen_bangqiang
     *  @Date: 2019/12/15 20:09
     *  @Description: 匹配路径下的所有文件的正则匹配对象，通过outList返回
     */
    public static void findPreciseForDir(List<String> regexList, File dirFile, List outList){
        if (regexList == null || regexList.size() <= 0 || dirFile == null){
            return;
        }
        if (dirFile.isDirectory()){
            List<File> files = Arrays.asList(dirFile.listFiles());
            if (files != null && files.size() > 0){
                for (File findFile: files){
                    findPreciseForDir(regexList, findFile, outList);
                }
            }
        }else {
            String document = getStringByFile(dirFile);
            for (String regex : regexList){
                if (regexMatchDocument(document, regex) != null){
                    outList.add(regex);
                }
            }
        }
    }

    /**
     *  @author: chen_bangqiang
     *  @Date: 2019/12/15 20:56
     *  @Description: 正则匹配文档
     *  @Param [document, regex]
     *  @return java.lang.String
     */
    private static String regexMatchDocument(String document, String regex){
        if (Strings.isNullOrEmpty(regex) || Strings.isNullOrEmpty(document)){
            return null;
        }
        Pattern pattern = Pattern.compile(regex);
        Matcher matcher = pattern.matcher(document);
        if (matcher.find()) {
            return matcher.group();
        }
        return null;
    }

    public static void main(String[] args) {
        String dirPath = "D:\\worksplace\\java\\Demo\\src\\main\\java\\com\\example\\demo\\util";
        File dirFile = new File(dirPath);
        if (dirFile == null){
            return;
        }
        List<String> matchList = new ArrayList();
        findPreciseForDir("LOG_TEST", dirFile, matchList);
        for (String info: matchList){
            System.out.println(info);
        }
    }

}
