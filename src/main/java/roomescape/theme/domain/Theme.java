package roomescape.theme.domain;

import lombok.EqualsAndHashCode;
import lombok.Getter;

@Getter
@EqualsAndHashCode(onlyExplicitlyIncluded = true)
public class Theme {

    @EqualsAndHashCode.Include
    private final Long id;
    private final String name;
    private final String description;
    private final String imageUrl;

    public Theme(Long id, String name, String description, String imageUrl) {
        this.id = id;
        this.name = name;
        this.description = description;
        this.imageUrl = imageUrl;
    }
}

