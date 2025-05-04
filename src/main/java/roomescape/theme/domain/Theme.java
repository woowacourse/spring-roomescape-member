package roomescape.theme.domain;

import java.util.Objects;

public class Theme {
    private static final String NULL_VALUE_EXCEPTION_MESSAGE = "널 값은 저장될 수 없습니다.";

    private final Long id;
    private final String name;
    private final String description;
    private final String thumbnail;

    public Theme(String name, String description, String thumbnail) {
        this.id = null;
        this.name = Objects.requireNonNull(name, NULL_VALUE_EXCEPTION_MESSAGE);
        this.description = Objects.requireNonNull(description, NULL_VALUE_EXCEPTION_MESSAGE);
        this.thumbnail = Objects.requireNonNull(thumbnail, NULL_VALUE_EXCEPTION_MESSAGE);
    }

    public Theme(Long id, String name, String description, String thumbnail) {
        this.id = Objects.requireNonNull(id, NULL_VALUE_EXCEPTION_MESSAGE);
        this.name = Objects.requireNonNull(name, NULL_VALUE_EXCEPTION_MESSAGE);
        this.description = Objects.requireNonNull(description, NULL_VALUE_EXCEPTION_MESSAGE);
        this.thumbnail = Objects.requireNonNull(thumbnail, NULL_VALUE_EXCEPTION_MESSAGE);
    }

    public Long getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public String getDescription() {
        return description;
    }

    public String getThumbnail() {
        return thumbnail;
    }

    @Override
    public boolean equals(Object other) {
        if (other == null || getClass() != other.getClass()) {
            return false;
        }
        Theme theme = (Theme) other;
        return Objects.equals(id, theme.id)
                && Objects.equals(name, theme.name);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, name);
    }
}
