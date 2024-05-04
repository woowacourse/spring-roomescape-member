package roomescape.domain;

import java.util.Objects;

public class Theme {
    private final Long id;
    private final ThemeName name;
    private final ThemeDescription description;
    private final ThemeThumbnail thumbnail;

    public Theme(Long id, String name, String description, String thumbnail) {
        this.id = Objects.requireNonNull(id, "인자 중 null 값이 존재합니다.");
        this.name = new ThemeName(name);
        this.description = new ThemeDescription(description);
        this.thumbnail = new ThemeThumbnail(thumbnail);
    }

    public Theme(String name, String description, String thumbnail) {
        this.id = null;
        this.name = new ThemeName(name);
        this.description = new ThemeDescription(description);
        this.thumbnail = new ThemeThumbnail(thumbnail);
    }

    public Theme changeId(Long id) {
        return new Theme(id, this.name.value(), this.description.value(), this.thumbnail.value());
    }

    public Long getId() {
        return id;
    }

    public String getName() {
        return name.value();
    }

    public String getDescription() {
        return description.value();
    }

    public String getThumbnail() {
        return thumbnail.value();
    }
}
