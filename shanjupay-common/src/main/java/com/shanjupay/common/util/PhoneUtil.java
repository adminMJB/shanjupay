package com.shanjupay.common.util;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class PhoneUtil {
    /**
     * 校验用户手机号是否合法
     * @param phone
     * @return
     */
    public static Boolean isMatches(String phone){
        String regex = "^((13[0-9])|(14[5|7])|(15([0-3]|[5-9]))|(17[013678])|(18[0,5-9]))\\d{8}$";
        Pattern p = Pattern.compile(regex);
        Matcher m = p.matcher(phone);
        return m.matches();

//        String value="手机号";
//        String regExp = "^[1]([3][0-9]{1}|59|58|88|89)[0-9]{8}$";
//        Pattern p = Pattern.compile(regExp);
//        Matcher m = p.matcher(phone);
//        return m.find();//boolean
    }

    public static void main(String[] args) {
        Boolean matches = PhoneUtil.isMatches("17309694133");
        System.out.println(matches);
    }
}
