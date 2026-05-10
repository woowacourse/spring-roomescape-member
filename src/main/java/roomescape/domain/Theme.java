package roomescape.domain;

import lombok.Getter;

@Getter
public class Theme {

    private final Long id;
    private final String name;
    private final String description;
    private final String imageUrl;

    private Theme(Long id, String name, String description, String imageUrl) {
        this.id = id;
        this.name = name;
        this.description = description;
        this.imageUrl = imageUrl;
    }

    public static Theme createNew(String name, String description, String imageUrl) {
        return new Theme(null, name, description, imageUrl);
    }

    public static Theme from(Long id, String name, String description, String imageUrl) {
        return new Theme(id, name, description, imageUrl);
    }
}
