package roomescape.service.dto.output;

import roomescape.domain.Theme;

public record ThemeOutput(long id, String name, String description, String thumbnail) {

    public static ThemeOutput toOutput(Theme theme) {
        return new ThemeOutput(theme.getId(), theme.getName(), theme.getDescription(), theme.getThumbnailAsString());
    }
}
