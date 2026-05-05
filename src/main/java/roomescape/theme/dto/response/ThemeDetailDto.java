package roomescape.theme.dto.response;

import roomescape.theme.domain.Theme;

public record ThemeDetailDto(
        Long id,
        String name,
        String description,
        String thumbnailUrl,
        boolean isActive
){
    public static ThemeDetailDto from(Theme theme){
        return new ThemeDetailDto(
                theme.id(),
                theme.name(),
                theme.description(),
                theme.thumbnailUrl(),
                theme.isActive()
        );
    }
}
