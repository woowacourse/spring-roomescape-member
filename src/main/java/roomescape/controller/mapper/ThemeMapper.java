package roomescape.controller.mapper;

import org.springframework.stereotype.Component;
import roomescape.controller.dto.ThemeCreateRequest;
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
}
