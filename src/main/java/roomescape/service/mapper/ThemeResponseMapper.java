package roomescape.service.mapper;

import org.springframework.stereotype.Component;
import roomescape.controller.dto.ThemeResponse;
import roomescape.domain.Theme;

@Component
public class ThemeResponseMapper {

    public ThemeResponse map(
            Theme theme
    ) {
        return new ThemeResponse(
                theme.id().getValueAsString(),
                theme.name(),
                theme.description(),
                theme.imageUrl()
        );
    }
}
