package roomescape.domain;

public class Theme {
    private final Long id;
    private final String name;
    private final String description;
    private final String thumbnail;

    public Theme(Long id, String name, String description, String thumbnail) {
        validate(name);
        this.id = id;
        this.name = name;
        this.description = description;
        this.thumbnail = thumbnail;
    }

    public static Theme generateWithPrimaryKey(Theme theme, Long newPrimaryKey) {
        return new Theme(newPrimaryKey, theme.name, theme.description, theme.thumbnail);
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

    public void validate(String name) {
        if (name == null || name.isBlank()) {
            throw new IllegalArgumentException("테마 이름은 비워둘 수 없습니다.");
        }
    }
}
