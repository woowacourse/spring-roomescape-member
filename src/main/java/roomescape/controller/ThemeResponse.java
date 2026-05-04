package roomescape.controller;

import roomescape.domain.Theme;

public record ThemeResponse() {
    public static ThemeResponse from(Theme theme) {
    }
}
