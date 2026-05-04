package roomescape.controller.mapper;

import org.springframework.stereotype.Component;
import roomescape.controller.dto.ThemeResponse;
import roomescape.domain.Theme;

@Component
public class ThemeMapper {

    public ThemeResponse mapToResponse(
            Theme theme
    ) {
        return new ThemeResponse(
                theme.getId(),
                theme.getName(),
                theme.getDescription(),
                theme.getImageUrl()
        );
    }
}
