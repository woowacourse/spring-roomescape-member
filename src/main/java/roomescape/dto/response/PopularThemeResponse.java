package roomescape.dto.response;

import java.util.List;
import roomescape.dto.PopularThemeProjection;

public record PopularThemeResponse(
        Long id,
        String name,
        String description,
        String imgUrl,
        Long rank,
        Long reservationCount
) {
    public static List<PopularThemeResponse> toDto(List<PopularThemeProjection> popularThemeProjections) {
        return popularThemeProjections.stream()
                .map(
                        popularThemeProjection -> new PopularThemeResponse(
                                popularThemeProjection.id(),
                                popularThemeProjection.name(),
                                popularThemeProjection.description(),
                                popularThemeProjection.imgUrl(),
                                popularThemeProjection.rank(),
                                popularThemeProjection.reservationCount()
                        )
                ).toList();
    }
}
