package roomescape.dto;

import roomescape.domain.Theme;

public record ReservedThemeResponse(Long id, String name) {

    public static ReservedThemeResponse from(final Theme theme) {
        return new ReservedThemeResponse(theme.getId(), theme.getName());
    }
}
