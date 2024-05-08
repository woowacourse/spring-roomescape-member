package roomescape.domain;

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
            throw new InvalidDomainObjectException(String.format("thumbnail must be less than %d characters",
                    THUMBNAIL_LENGTH_MAX));
        }
        Matcher matcher = URL_PATTERN.matcher(value);
        if (!matcher.matches()) {
            throw new InvalidDomainObjectException("thumbnail must be a valid URL");
        }
    }
}
