package roomescape.presentation.dto.response;

import java.util.List;
import roomescape.application.dto.ThemeDto;

public record ThemeResponse(
        Long id,
        String name,
        String description,
        String thumbnail
) {
    public static ThemeResponse from(ThemeDto dto) {
        return new ThemeResponse(dto.id(), dto.name(), dto.description(), dto.thumbnail());
    }

    public static List<ThemeResponse> from(List<ThemeDto> dtos) {
        return dtos.stream()
                .map(ThemeResponse::from)
                .toList();
    }
}
