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
    public static PopularThemeResponse from(PopularThemeProjection popularThemeProjection) {
        return new PopularThemeResponse(
                popularThemeProjection.id(),
                popularThemeProjection.name(),
                popularThemeProjection.description(),
                popularThemeProjection.imgUrl(),
                popularThemeProjection.rank(),
                popularThemeProjection.reservationCount()
        );
    }

    public static List<PopularThemeResponse> fromAll(List<PopularThemeProjection> popularThemeProjections) {
        return popularThemeProjections.stream()
                .map(PopularThemeResponse::from)
                .toList();
    }
}
