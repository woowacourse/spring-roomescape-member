package roomescape.domain.theme;

import roomescape.domain.exception.InvalidDomainObjectException;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

import static java.util.Objects.isNull;

public record Thumbnail(String value) {
    private static final Pattern URL_PATTERN = Pattern.compile("^http(s)?:\\/\\/.*\\.(?:png|jpe?g|gif|bmp)$");
    private static final int THUMBNAIL_LENGTH_MAX = 255;

    public Thumbnail {
        if (isNull(value)) {
            throw new InvalidDomainObjectException("thumbnail must not be null");
        }
        if (value.length() > THUMBNAIL_LENGTH_MAX) {
            throw new InvalidDomainObjectException(String.format("썸네일 URL은 %d자 이하여야 합니다. (현재 입력한 URL 길이: %d자)",
                    THUMBNAIL_LENGTH_MAX, value.length()));
        }
        Matcher matcher = URL_PATTERN.matcher(value);
        if (!matcher.matches()) {
            throw new InvalidDomainObjectException("썸네일 URL 형식이 올바르지 않습니다. (예: http(s)://example.com/image.png)");
        }
    }
}
