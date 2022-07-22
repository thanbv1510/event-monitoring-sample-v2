package dev.thanbv1510.eventmonitoringsamplev2.utils;

import lombok.experimental.UtilityClass;

import java.nio.charset.StandardCharsets;
import java.util.Base64;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

@UtilityClass
public class TextUtils {
    public static String encryptString(String string) {
        byte[] bytesEncoded = Base64.getEncoder().encode(string.getBytes(StandardCharsets.UTF_8));
        return new String(bytesEncoded);
    }

    public static String decryptString(String string) {
        try {
            byte[] valueDecoded = Base64.getDecoder().decode(string);
            return new String(valueDecoded);
        } catch (Exception ex) {
            return string;
        }
    }

    public static String getValueBetweenSingleQuotes(String input) {
        String strPattern = "'([^']*)'";

        Pattern pattern = Pattern.compile(strPattern);
        Matcher matcher = pattern.matcher(input);

        return matcher.find() ? matcher.group(1) : "";
    }
}
