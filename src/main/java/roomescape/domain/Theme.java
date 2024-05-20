package roomescape.domain;

import java.util.Objects;
import roomescape.exception.CustomBadRequest;

public record Theme(Long id, String name, String description, Thumbnail thumbnail) {

    public Theme {
        validate(name, description);
    }

    public static Theme of(final Long id, final String name, final String description, final String thumbnail) {
        return new Theme(id, name, description, new Thumbnail(thumbnail));
    }

    private void validate(final String name, final String description) {
        validateNull(name, description);
    }

    private void validateNull(final String name, final String description) {
        if (name.isBlank()) {
            throw new CustomBadRequest(String.format("name(%s)이 유효하지 않습니다.", name));
        }
        if (description.isBlank()) {
            throw new CustomBadRequest(String.format("description(%s)이 유효하지 않습니다.", description));
        }
    }

    public String getThumbnailAsString() {
        return thumbnail.asString();
    }

    @Override
    public boolean equals(final Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }
        final Theme theme = (Theme) o;
        return Objects.equals(id, theme.id);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id);
    }
}
