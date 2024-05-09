package roomescape.theme.domain;

import java.util.Objects;

public record Theme(long id, String name, String description, String thumbnail) {
    private static final long UNDEFINED = 0;

    public Theme(String name, String description, String thumbnail) {
        this(UNDEFINED, name, description, thumbnail);
    }

    @Override
    public boolean equals(Object object) {
        if (this == object) return true;
        if (object == null || getClass() != object.getClass()) return false;
        Theme target = (Theme) object;
        return id == target.id;
    }

    @Override
    public int hashCode() {
        return Objects.hash(id);
    }
}
