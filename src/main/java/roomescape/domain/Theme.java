package roomescape.domain;

import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
public class Theme {

    private final Long id;
    private final String name;
    private final String thumbnailUrl;
    private final String description;
    private final ThemeStatus status;

    public static Theme pending(String name, String thumbnailUrl, String description) {
        return new Theme(null, name, thumbnailUrl, description, ThemeStatus.DRAFT);
    }

    public static Theme create(long id, String name, String thumbnailUrl, String description) {
        return new Theme(id, name, thumbnailUrl, description, ThemeStatus.AVAILABLE);
    }

    public Theme delete() {
        return new Theme(this.id, this.name, this.thumbnailUrl ,this.description, ThemeStatus.DELETED);
    }

    public long id() {
        return id;
    }

    public String name() {
        return name;
    }

    public String thumbnailUrl() {
        return thumbnailUrl;
    }

    public String description() {
        return description;
    }

    public ThemeStatus status() {
        return status;
    }
}
