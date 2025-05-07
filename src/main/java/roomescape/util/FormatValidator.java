package roomescape.util;

import java.util.regex.Pattern;

public class FormatValidator {

    public static void validateNameFormat(String name) {
        String nameFormatRegex = "^([가-힣]{2,5}|[a-zA-Z]{2,30})$";
        if (Pattern.matches(nameFormatRegex, name)) {
            return;
        }
        throw new IllegalArgumentException("이름이 잘못되었습니다." + name);
    }
}
