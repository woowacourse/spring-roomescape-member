package roomescape.domain.theme;

import java.util.Objects;

public class Theme {

    private Long id;
    private final String name;
    private final String description;
    private final String thumbnail;

    public Theme(final Long id, final String name, final String description, final String thumbnail) {
        this.id = id;
        this.name = name;
        this.description = description;
        this.thumbnail = thumbnail;
    }

    public Theme(final String name, final String description, final String thumbnail) {
        this.name = name;
        this.description = description;
        this.thumbnail = thumbnail;
    }

    public Long getId() {
        return id;
    }

    public void setId(final Long id) {
        this.id = id;
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
    public boolean equals(final Object o) {
        if (o == null || getClass() != o.getClass()) {
            return false;
        }
        final Theme theme = (Theme) o;
        return Objects.equals(getId(), theme.getId()) && Objects.equals(getName(), theme.getName())
                && Objects.equals(getDescription(), theme.getDescription()) && Objects.equals(
                getThumbnail(), theme.getThumbnail());
    }

    @Override
    public int hashCode() {
        return Objects.hash(getId(), getName(), getDescription(), getThumbnail());
    }
}
