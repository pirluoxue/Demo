package com.example.demo.entity.clone;

import com.example.demo.util.StringUtil;
import lombok.Data;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * @author chen_bq
 * @description 构造函数复杂的测试类
 * @create: 2019-04-01 11:05
 **/
@Data
public class ComplexCloneEntity implements Cloneable{
    
    private String str;
    
    private static ComplexCloneEntity baseEntity = new ComplexCloneEntity();

    public ComplexCloneEntity(){
        if(StringUtil.isNullOrEmpty(str)){
            str = "<li>\n" +
                    "                        <a href=\"https://www.biqudu.com/\">首页</a>\n" +
                    "                        <a rel=\"nofollow\" href=\"http://www.biqudu.com/modules/article/bookcase.php\">我的书架</a>\n" +
                    "                        <a href=\"http://www.biqudu.com/paihangbang/\">排行榜单</a>\n" +
                    "                        <a href=\"http://www.biqudu.com/wanbenxiaoshuo/\" target=\"_blank\">完本小说</a>\n" +
                    "                <script type=\"text/javascript\">list1();</script>\n" +
                    "                    <a href=\"/\">笔趣阁</a> &gt; 暧昧高手最新章节列表\n" +
                    "                            <a href=\"javascript:;\" onclick=\"showpop('/modules/article/addbookcase.php?bid=1961&ajax_request=1');\">加入书架</a>,\n" +
                    "                            <a href=\"#footer\">直达底部</a>\n" +
                    "                        <p>最后更新：2018-11-22 03:00:43</p>\n" +
                    "                            <a href=\"/1_1961/3474231.html\">第3229章 最后的抉择3(大结局)</a>\n" +
                    "                        <p>各位书友要是觉得《暧昧高手》还不错的话请不要忘记向您QQ群和微博里的朋友推荐哦！</p>\n" +
                    "                <div id=\"sidebar\">\n" +
                    "                        <img alt=\"暧昧高手\" src=\"/files/article/image/2/1961/1961.jpg\" width=\"120\" height=\"150\" />\n" +
                    "                        <span class=\"b\"></span>\n" +
                    "                <div id=\"listtj\"></div>\n" +
                    "            <div class=\"box_con\">\n" +
                    "                        <dt>《暧昧高手》最新章节\n" +
                    "                        </b>（提示：已启用缓存技术，最新章节可能会延时显示，登录书架即可实时查看。）\n" +
                    "                        <a href=\"/1_1961/3474231.html\">第3229章 最后的抉择3(大结局)</a>\n" +
                    "                    </dd>\n";
            String regex = "(href=\")+\\S*?\"";
            Pattern p = Pattern.compile(regex);
            Matcher m = p.matcher(str);
            StringBuilder stringBuilder = new StringBuilder(1000);
            while (m.find()){
                stringBuilder.append(m.group());
            }
            str = stringBuilder.toString();
        }
    }

    public static ComplexCloneEntity getComplexCloneEntity() {
        try {
            return baseEntity.clone();
        } catch (CloneNotSupportedException e) {
            e.printStackTrace();
        }
        return new ComplexCloneEntity();
    }

    @Override
    protected ComplexCloneEntity clone() throws CloneNotSupportedException {
        return (ComplexCloneEntity) super.clone();
    }

    public static ComplexCloneEntity getInstance(){
        return baseEntity;
    }
    
}
