package roomescape.theme;

import java.util.Objects;
import roomescape.exception.ArgumentException;

public class Theme {
    private final Long id;
    private final String name;
    private final String description;
    private final String thumbnail;

    public Theme(Long id, String name, String description, String thumbnail) {
        this.id = id;
        this.name = name;
        this.description = description;
        this.thumbnail = thumbnail;
    }

    private static void validateNull(String name, String description, String thumbnail) {
        if (name == null || name.isBlank()) {
            throw new ArgumentException("테마 이름이 존재하지 않습니다.");
        }
        if (description == null || description.isBlank()) {
            throw new ArgumentException("테마 설명이 존재하지 않습니다.");
        }
        if (thumbnail == null || thumbnail.isBlank()) {
            throw new ArgumentException("테마 썸네일이 존재하지 않습니다.");
        }
    }

    public static Theme of(Long id, String name, String description, String thumbnail) {
        validateNull(name, description, thumbnail);
        return new Theme(id, name, description, thumbnail);
    }

    public static Theme createWithoutId(String name, String description, String thumbnail) {
        validateNull(name, description, thumbnail);
        validateDescriptionLength(description);
        validateImageType(thumbnail);
        return new Theme(null, name, description, thumbnail);
    }

    private static void validateDescriptionLength(String description) {
        if (description.length() < 5 || description.length() > 100) {
            throw new ArgumentException("테마 소개는 최소 5글자, 최대 100글자여야 합니다.");
        }
    }

    private static void validateImageType(String thumbnail) {
        if (!thumbnail.matches("(?i)^.+\\.(jpg|jpeg|png|gif|bmp|webp)$")) {
            throw new ArgumentException("썸네일은 이미지 형식이어야 합니다.");
        }
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
    public boolean equals(Object o) {
        if (o == null || getClass() != o.getClass()) {
            return false;
        }
        Theme theme = (Theme) o;
        return Objects.equals(id, theme.id) && Objects.equals(name, theme.name)
                && Objects.equals(description, theme.description) && Objects.equals(thumbnail,
                theme.thumbnail);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, name, description, thumbnail);
    }
}
