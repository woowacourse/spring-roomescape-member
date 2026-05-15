package roomescape.domain.vo;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonValue;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

public record ThemeImageUrl(
    @JsonValue String value
) {

    private static final Pattern PATTERN = Pattern.compile(
        "^https?://[^\\s/$.?#][^\\s]*[^\\s/.]\\.(jpg|jpeg|png|gif|webp|bmp|svg)(\\?[^\\s]*)?$");

    @JsonCreator
    public ThemeImageUrl {
        validateNotBlank(value);
        validatePattern(value);
    }

    private void validateNotBlank(String value) {
        if (value == null || value.isBlank()) {
            throw new IllegalArgumentException("빈 문자열은 이미지 URL로 사용할 수 없습니다.");
        }
    }

    private void validatePattern(String value) {
        Matcher matcher = PATTERN.matcher(value);
        if (!matcher.matches()) {
            throw new IllegalArgumentException("올바른 이미지 URL 형식이 아닙니다.");
        }
    }

    public static ThemeImageUrl defaultImageUrl() {
        return new ThemeImageUrl("https://i.pinimg.com/736x/87/e8/a2/87e8a2af70721d5b886d4343425d1bd4.jpg");
    }
}
