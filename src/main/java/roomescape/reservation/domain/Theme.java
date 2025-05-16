package roomescape.reservation.domain;

import java.util.Objects;

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
        validate();
    }

    private void validate() {
        Objects.requireNonNull(name, "테마 이름은 null일 수 없습니다.");
        Objects.requireNonNull(description, "테마 설명은 null일 수 없습니다.");
        Objects.requireNonNull(thumbnail, "테마 대표 이미지는 null일 수 없습니다.");
    }

    public Theme withId(Long id) {
        return new Theme(id, this.name, this.description, this.thumbnail);
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

    public boolean isSameId(Long themeId) {
        return id.equals(themeId);
    }

}
