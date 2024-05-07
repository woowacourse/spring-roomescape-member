package roomescape.domain;

import java.util.Objects;
import roomescape.exception.InvalidReservationException;

public class Thumbnail {
    private static final int MINIMUM_THUMBNAIL_LENGTH = 1;
    private static final int MAXIMUM_THUMBNAIL_LENGTH = 250;
    private static final String INVALID_THUMBNAIL_LENGTH = String.format("썸네일 URL은 %d자 이상, %d자 이하여야 합니다.",
            MINIMUM_THUMBNAIL_LENGTH,
            MAXIMUM_THUMBNAIL_LENGTH);

    private final String value;

    public Thumbnail(String value) {
        validateThumbnail(value);
        this.value = value;
    }

    private void validateThumbnail(final String thumbnail) {
        if (thumbnail.isEmpty() || thumbnail.length() > MAXIMUM_THUMBNAIL_LENGTH) {
            throw new InvalidReservationException(INVALID_THUMBNAIL_LENGTH);
        }
    }

    public String getValue() {
        return value;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }
        Thumbnail thumbnail = (Thumbnail) o;
        return Objects.equals(value, thumbnail.value);
    }

    @Override
    public int hashCode() {
        return Objects.hash(value);
    }
}
