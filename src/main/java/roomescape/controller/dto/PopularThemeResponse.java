package roomescape.controller.dto;

import roomescape.repository.dto.PopularThemeDto;

public record PopularThemeResponse(
        Long id,
        String name,
        String description,
        String thumbnail,
        Long reservationCount
) {
    public static PopularThemeResponse from(PopularThemeDto popularThemeDto) {
        return new PopularThemeResponse(
                popularThemeDto.id(),
                popularThemeDto.name(),
                popularThemeDto.description(),
                popularThemeDto.thumbnail(),
                popularThemeDto.reservationCount()
        );
    }
}
