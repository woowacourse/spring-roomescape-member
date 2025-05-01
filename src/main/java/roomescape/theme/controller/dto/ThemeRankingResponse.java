package roomescape.theme.controller.dto;


import roomescape.theme.domain.Theme;

public record ThemeRankingResponse(String name, String description, String thumbnail) {

    public static ThemeRankingResponse from(Theme theme) {
        return new ThemeRankingResponse(theme.getName(), theme.getDescription(), theme.getThumbnail());
    }

}
