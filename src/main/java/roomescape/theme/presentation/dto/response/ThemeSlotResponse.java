package roomescape.theme.presentation.dto.response;

import java.util.List;
import roomescape.theme.application.dto.ThemeDto;

public record ThemeSlotResponse(
        Long id,
        String name
) {
    public static ThemeSlotResponse from(ThemeDto dto) {
        return new ThemeSlotResponse(dto.id(), dto.name());
    }

    public static List<ThemeSlotResponse> from(List<ThemeDto> dtos) {
        return dtos.stream()
                .map(ThemeSlotResponse::from)
                .toList();
    }
}
