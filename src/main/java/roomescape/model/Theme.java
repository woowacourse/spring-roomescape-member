package roomescape.model;

import roomescape.service.dto.ThemeDto;

import java.util.Objects;

public class Theme {

    private long id;
    private String name;
    private String description;
    private String thumbnail;

    public Theme(long id, String name, String description, String thumbnail) {
        validate(id, name, description, thumbnail);
        this.id = id;
        this.name = name;
        this.description = description;
        this.thumbnail = thumbnail;
    }

    private Theme(String name, String description, String thumbnail) {
        validate(name, description, thumbnail);
        this.id = 0;
        this.name = name;
        this.description = description;
        this.thumbnail = thumbnail;
    }

    private Theme() {
    }

    public static Theme from(ThemeDto themeDto) {
        return new Theme(themeDto.getName(), themeDto.getDescription(), themeDto.getThumbnail());
    }

    private void validate(long id, String name, String description, String thumbnail) {
        validateRange(id);
        validate(name, description, thumbnail);
    }

    private void validate(String name, String description, String thumbnail) {
        validateNull(name);
        validateNull(description);
        validateNull(thumbnail);
    }

    private void validateRange(long id) {
        if (id <= 0) {
            throw new IllegalStateException("id는 0 이하일 수 없습니다.");
        }
    }

    private void validateNull(String value) {
        if (value == null || value.isBlank()) {
            throw new IllegalStateException("데이터는 null 혹은 빈 문자열일 수 없습니다.");
        }
    }

    public long getId() {
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
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Theme theme = (Theme) o;
        return id == theme.id && Objects.equals(name, theme.name) && Objects.equals(description, theme.description) && Objects.equals(thumbnail, theme.thumbnail);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, name, description, thumbnail);
    }

    @Override
    public String toString() {
        return "Theme{" +
                "themeId=" + id +
                ", name='" + name + '\'' +
                ", description='" + description + '\'' +
                ", thumbnail='" + thumbnail + '\'' +
                '}';
    }
}
