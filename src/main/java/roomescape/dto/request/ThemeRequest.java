package roomescape.dto.request;

import jakarta.validation.constraints.NotBlank;
import org.hibernate.validator.constraints.URL;
import roomescape.domain.Theme;

public record ThemeRequest(
        @NotBlank String name,
        @NotBlank String description,
        @URL String thumbnail
) {
    public Theme toTheme() {
        return Theme.createWithoutId(
                name,
                description,
                thumbnail
        );
    }
}
