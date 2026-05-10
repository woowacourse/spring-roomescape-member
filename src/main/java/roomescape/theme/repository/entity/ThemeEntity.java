package roomescape.theme.repository.entity;

import lombok.EqualsAndHashCode;
import lombok.Getter;

@Getter
@EqualsAndHashCode(onlyExplicitlyIncluded = true)
public class ThemeEntity {

    @EqualsAndHashCode.Include
    private final Long id;
    private final String name;
    private final String description;
    private final String imageUrl;
    private final boolean isDeleted;

    public ThemeEntity(Long id, String name, String description, String imageUrl, boolean isDeleted) {
        this.id = id;
        this.name = name;
        this.description = description;
        this.imageUrl = imageUrl;
        this.isDeleted = isDeleted;
    }
}
