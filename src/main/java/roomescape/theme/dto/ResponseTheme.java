package roomescape.theme.dto;

import roomescape.theme.domain.Theme;

public record ResponseTheme(
        Long id,
        String name,
        String description,
        String thumbnail
) {

    public static ResponseTheme from(Theme theme) {
        return new ResponseTheme(
            theme.getId(),
            theme.getName(),
            theme.getDescription(),
            theme.getThumbnail()
        );
    }
}
