package roomescape.theme.domain.dto;

import roomescape.theme.domain.Theme;

public record ThemeResDto(Long id, String name, String description, String thumbnail) {


    public static ThemeResDto from(Theme theme) {
        return new ThemeResDto(
            theme.getId(),
            theme.getName(),
            theme.getDescription(),
            theme.getThumbnail()
        );
    }
}
