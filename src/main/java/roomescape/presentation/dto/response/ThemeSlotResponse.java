package roomescape.presentation.dto.response;

import java.util.List;
import java.util.stream.Collectors;
import roomescape.application.dto.ThemeDto;

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
