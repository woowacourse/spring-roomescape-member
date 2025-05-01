package roomescape.reservation.controller.dto;


import roomescape.reservation.domain.Theme;

public record ThemeRankingResponse(String name, String description, String thumbnail) {
    public static ThemeRankingResponse from(Theme theme) {
        return new ThemeRankingResponse(theme.getName(), theme.getDescription(), theme.getThumbnail());
    }
}
