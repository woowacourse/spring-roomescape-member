package roomescape.theme.domain;

import lombok.Getter;

@Getter
public class Theme {

    private final Long id;
    private final String name;
    private final String description;
    private final String thumbnailImgUrl;

    private Theme(Long id, String name, String description, String thumbnailImgUrl) {
        this.id = id;
        this.name = name;
        this.description = description;
        this.thumbnailImgUrl = thumbnailImgUrl;
    }

    public static Theme create(String name, String description, String thumbnailImgUrl) {
        return new Theme(null, name, description, thumbnailImgUrl);
    }

    public static Theme create(Long id, Theme theme) {
        return new Theme(id, theme.name, theme.description, theme.thumbnailImgUrl);
    }
}
