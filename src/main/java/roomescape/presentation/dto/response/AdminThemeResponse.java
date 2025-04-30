package roomescape.presentation.dto.response;

import java.util.List;
import java.util.stream.Collectors;
import roomescape.application.dto.ThemeDto;

public record AdminThemeResponse(
        Long id,
        String name,
        String description,
        String thumbnail
) {
    public static AdminThemeResponse from(ThemeDto dto) {
        return new AdminThemeResponse(dto.id(), dto.name(), dto.description(), dto.thumbnail());
    }

    public static List<AdminThemeResponse> from(List<ThemeDto> dtos) {
        return dtos.stream()
                .map(AdminThemeResponse::from)
                .toList();
    }
}
