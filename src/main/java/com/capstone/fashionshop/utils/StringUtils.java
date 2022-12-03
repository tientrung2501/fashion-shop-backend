package com.capstone.fashionshop.utils;

import javax.servlet.http.HttpServletRequest;
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

    public static String getBaseURL(HttpServletRequest request) {
        String scheme = request.getScheme();
        String serverName = request.getServerName();
        int serverPort = request.getServerPort();
        String contextPath = request.getContextPath();
        StringBuffer url =  new StringBuffer();
        url.append(scheme).append("://").append(serverName);
        if ((serverPort != 80) && (serverPort != 443)) {
            url.append(":").append(serverPort);
        }
        url.append(contextPath);
        if(url.toString().endsWith("/")){
            url.append("/");
        }
        return url.toString();
    }
}
