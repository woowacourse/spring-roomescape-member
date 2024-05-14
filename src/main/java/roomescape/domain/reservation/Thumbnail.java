package roomescape.domain.reservation;

import roomescape.exception.InvalidReservationException;

import java.util.Objects;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class Thumbnail {
    private static final String THUMBNAIL_REGEX = "^$|(https?|ftp)://.*\\.(jpeg|jpg|png|gif|bmp)$";

    private final String thumbnail;

    public Thumbnail(String thumbnail) {
        validate(thumbnail);
        this.thumbnail = thumbnail;
    }

    private void validate(String thumbnail) {
        Pattern pattern = Pattern.compile(THUMBNAIL_REGEX);
        Matcher matcher = pattern.matcher(thumbnail);
        if (!matcher.matches()) {
            throw new InvalidReservationException("올바르지 않은 썸네일 형식입니다.");
        }
    }

    @Override
    public int hashCode() {
        return Objects.hash(thumbnail);
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Thumbnail that = (Thumbnail) o;
        return Objects.equals(thumbnail, that.thumbnail);
    }

    public String getThumbnail() {
        return thumbnail;
    }
}
