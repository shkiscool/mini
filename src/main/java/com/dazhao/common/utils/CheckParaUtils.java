package com.dazhao.common.utils;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class CheckParaUtils {

    /**
     * 只能匹配数字
     */
    public static boolean isNumberJ(String num) {
        String reg = "^[0-9]+$";
        Pattern pattern = Pattern.compile(reg);
        Matcher matcher = pattern.matcher(num);
        return matcher.matches();
    }

    /**
     * 匹配所有大小写字母
     */
    public static boolean isNumAndEnglishJ(String str) {
        String reg = "^[A-Za-z0-9]+$";
        Pattern pattern = Pattern.compile(reg);
        Matcher matcher = pattern.matcher(str);
        return matcher.matches();
    }


    /**
     * 验证日期格式是否正确 格式为yyyy-MM-dd,yyyy/MM/dd,yyyyMMdd
     */
    public static boolean isDayTrue(String str) {
        String reg = "^((\\d{2}(([02468][048])|([13579][26]))[\\-\\/\\s]?((((0?[13578])|(1[02]))[\\-\\/\\s]?((0?[1-9])|([1-2][0-9])|(3[01])))"
                + "|(((0?[469])|(11))[\\-\\/\\s]?((0?[1-9])|([1-2][0-9])|(30)))|(0?2[\\-\\/\\s]?((0?[1-9])|([1-2][0-9])))))|"
                + "(\\d{2}(([02468][1235679])|([13579][01345789]))[\\-\\/\\s]?((((0?[13578])|(1[02]))[\\-\\/\\s]?((0?[1-9])"
                + "|([1-2][0-9])|(3[01])))|(((0?[469])|(11))[\\-\\/\\s]?((0?[1-9])|([1-2][0-9])"
                + "|(30)))|(0?2[\\-\\/\\s]?((0?[1-9])|(1[0-9])|(2[0-8]))))))";
        Pattern pattern = Pattern.compile(reg);
        Matcher matcher = pattern.matcher(str);
        return matcher.matches();
    }

    /**
     * 过滤特殊字符
     */
    public static String isCharacter(String str) {
        String regEx = "[`~!@#$^&*()=|{}''\\[\\]<>/?~！@#￥……&*（）——|{}【】‘”“'、？%+_]";
        Pattern p = Pattern.compile(regEx);
        Matcher m = p.matcher(str);
        return m.replaceAll("").trim();
    }

    /**
     * 查询字符中是否还含有特殊字符
     */
    public static boolean isSpecialChar(String str) {
        String regEx = "[`~!@#$^&*()=|{}''\\[\\]<>/?~！@#￥……&*（）——|{}【】‘”“'、？%+_]";
        Pattern p = Pattern.compile(regEx);
        Matcher m = p.matcher(str);
        return m.find();
    }

    /**
     * 判断是否不为空的字符串
     */
    public static boolean isNotNull(String str) {
        if (str != null && !"".equals(str) && !"null".equals(str)) {
            return true;
        }
        return false;
    }

    /**
     * 判断只能空，和数字和英文字母
     */
    public static boolean isNullAndNumAndEnglishJ(String str) {
        if (isNotNull(str)) {
            if (isNumAndEnglishJ(str.trim())) {
                return true;
            }
        } else {
            return true;
        }
        return false;
    }

    /**
     * 判断只能空，和数字
     */
    public static boolean isNullAndNum(String str) {
        if (str != null && !"".equals(str)) {
            if (isNumberJ(str.trim())) {
                return true;
            }
        } else {
            return true;
        }
        return false;
    }

    /**
     * 判断只能空，和日期
     */
    public static boolean isNullAndDate(String str) {
        if (str != null && !"".equals(str)) {
            if (isDayTrue(str.trim())) {
                return true;
            }
        } else {
            return true;
        }
        return false;
    }


}
