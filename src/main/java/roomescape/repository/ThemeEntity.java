package roomescape.repository;

import roomescape.domain.Theme;


public class ThemeEntity {

    private final Long id;
    private final Theme theme;

    public ThemeEntity(Long id, Theme theme) {
        this.id = id;
        this.theme = theme;
    }

    public Long getId() {
        return id;
    }

    public Theme getTheme() {
        return theme;
    }
}
