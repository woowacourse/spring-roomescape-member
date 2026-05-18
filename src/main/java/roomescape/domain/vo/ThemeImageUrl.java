package roomescape.domain.vo;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonValue;
import jakarta.validation.constraints.NotBlank;
import roomescape.exception.BusinessException;
import roomescape.exception.ErrorCode;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

public record ThemeImageUrl(
        String value
) {

    private static final Pattern PATTERN = Pattern.compile(
        "^https?://[^\\s/$.?#][^\\s]*[^\\s/.]\\.(jpg|jpeg|png|gif|webp|bmp|svg)(\\?[^\\s]*)?$");

    public ThemeImageUrl {
        validateNotBlank(value);
        validatePattern(value);
    }

    public static ThemeImageUrl from(String imageUrl) {
        return new ThemeImageUrl(imageUrl);
    }

    private void validateNotBlank(String value) {
        if (value == null || value.isBlank()) {
            throw new BusinessException(ErrorCode.BLANK_INPUT, "빈 문자열은 이미지 URL로 사용할 수 없습니다.");
        }
    }

    private void validatePattern(String value) {
        Matcher matcher = PATTERN.matcher(value);
        if (!matcher.matches()) {
            throw new BusinessException(ErrorCode.INVALID_FORMAT, "올바른 이미지 URL 형식이 아닙니다.");
        }
    }

    public static ThemeImageUrl defaultImageUrl() {
        return new ThemeImageUrl("https://i.pinimg.com/736x/87/e8/a2/87e8a2af70721d5b886d4343425d1bd4.jpg");
    }
}
