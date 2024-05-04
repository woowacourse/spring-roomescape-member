package roomescape.domain;

import java.util.Objects;

public class Theme {
    private final Long id;
    private final ThemeName name;
    private final ThemeDescription description;
    private final ThemeThumbnail thumbnail;

    public Theme(Long id, String name, String description, String thumbnail) {
        this(Objects.requireNonNull(id, "인자 중 null 값이 존재합니다."),
                new ThemeName(name),
                new ThemeDescription(description),
                new ThemeThumbnail(thumbnail));
    }

    public Theme(String name, String description, String thumbnail) {
        this(null, new ThemeName(name), new ThemeDescription(description), new ThemeThumbnail(thumbnail));
    }

    private Theme(Long id, ThemeName name, ThemeDescription description, ThemeThumbnail thumbnail) {
        String errorMessage = "인자 중 null 값이 존재합니다.";
        this.id = id;
        this.name = Objects.requireNonNull(name, errorMessage);
        this.description = Objects.requireNonNull(description, errorMessage);
        this.thumbnail = Objects.requireNonNull(thumbnail, errorMessage);
    }

    public Theme withId(Long id) {
        return new Theme(id, this.name, this.description, this.thumbnail);
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
