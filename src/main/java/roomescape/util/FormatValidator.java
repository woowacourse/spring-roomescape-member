package roomescape.util;

import java.util.regex.Pattern;

public class FormatValidator {
    private static final String NAME_FORMAT_REGEX = "^([가-힣]{2,5}|[a-zA-Z]{2,30})$";
    public static void validateNameFormat(String name) {
        if (Pattern.matches(NAME_FORMAT_REGEX, name)) {
            return;
        }
        throw new IllegalArgumentException("이름이 잘못되었습니다. " + name);
    }
}
