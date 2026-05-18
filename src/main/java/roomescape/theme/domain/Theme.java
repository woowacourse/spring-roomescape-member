package roomescape.theme.domain;

import java.util.Objects;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import roomescape.global.exception.validation.InvalidIdException;

@Getter
@EqualsAndHashCode(onlyExplicitlyIncluded = true)
public class Theme {

    @EqualsAndHashCode.Include
    private final Long id;
    private final String name;
    private final String description;
    private final String imageUrl;

    public Theme(Long id, String name, String description, String imageUrl) {
        validateId(id);
        this.id = id;
        this.name = name;
        this.description = description;
        this.imageUrl = imageUrl;
    }

    private void validateId(Long id) {
        if(Objects.isNull(id) || id <= 0) {
            throw new InvalidIdException(id);
        }
    }
}

