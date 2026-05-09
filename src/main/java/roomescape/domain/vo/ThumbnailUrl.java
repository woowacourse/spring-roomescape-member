package roomescape.domain.vo;

import roomescape.common.exception.DomainException;

import java.util.Objects;
import java.util.regex.Pattern;

public record ThumbnailUrl(String value) {
    private static final Pattern URL_PATTERN = Pattern.compile("^https?://.+");

    public ThumbnailUrl {
        if (!URL_PATTERN.matcher(value).matches()) {
            throw new DomainException("올바른 URL 형식이 아닙니다");
        }
        if (value.length() > 500) {
            throw new DomainException("URL은 500자 이하여야 합니다");
        }
    }
}