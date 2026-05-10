package roomescape.controller.mapper;

import org.springframework.stereotype.Component;
import roomescape.controller.dto.ThemeCreateRequest;
import roomescape.controller.dto.ThemeResponse;
import roomescape.domain.Theme;
import roomescape.service.dto.ThemeCreateCommand;

@Component
public class ThemeMapper {

    public ThemeCreateCommand mapToCommand(
            ThemeCreateRequest request
    ) {
        return new ThemeCreateCommand(
                request.name(),
                request.description(),
                request.imageUrl()
        );
    }

    public ThemeResponse mapToResponse(
            Theme theme
    ) {
        return new ThemeResponse(
                theme.id(),
                theme.name(),
                theme.description(),
                theme.imageUrl()
        );
    }
}
