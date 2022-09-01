package com.capstone.fashionshop.utils;

import java.text.Normalizer;
import java.util.regex.Pattern;

public class StringUtils {

    public static String toSlug(String input) {
        String normalized = Normalizer.normalize(input, Normalizer.Form.NFD);
        Pattern pattern = Pattern.compile("\\p{InCombiningDiacriticalMarks}+");
        return pattern.matcher(normalized).replaceAll("").toLowerCase().replaceAll(" ", "-").replaceAll("đ", "d");
    }

    public static boolean isPhoneNumberFormat(String phone) {
        String regex = "((^(\\+84|84|0|0084){1})(3|5|7|8|9))+([0-9]{8})$";
        return Pattern.compile(regex).matcher(phone).matches();
    }
}
