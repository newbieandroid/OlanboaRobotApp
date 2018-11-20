package com.olanboa.robot.util;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class RegUtils {


    public static boolean isMobile(String mobile) {
        final String regex = "^((13[0-9])|(14[5,7])|(15[0-3,5-9])|(17[0,3,5-8])|(18[0-9])|166|198|199|(147))\\d{8}$";
        final Pattern p = Pattern.compile(regex, Pattern.CASE_INSENSITIVE);
        final Matcher m = p.matcher(mobile);
        return m.matches();
    }

    public static boolean isEmail(String email) {
        final String pattern1 = "^\\s*\\w+(?:\\.{0,1}[\\w-]+)*@[a-zA-Z0-9]+(?:[-.][a-zA-Z0-9]+)*\\.[a-zA-Z]+\\s*$";
        final Pattern pattern = Pattern.compile(pattern1);
        final Matcher mat = pattern.matcher(email);
        return mat.matches();
    }


}
