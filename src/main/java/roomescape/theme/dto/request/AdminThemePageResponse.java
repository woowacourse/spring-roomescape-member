package roomescape.theme.dto.request;

import java.util.List;
import roomescape.theme.domain.Theme;

public record AdminThemePageResponse(int totalPages, List<AdminThemePageElementResponse> themes) {

    public record AdminThemePageElementResponse(
            Long id,
            String name,
            String description,
            String thumbnail
    ) {
        public static AdminThemePageElementResponse from(Theme theme) {
            return new AdminThemePageElementResponse(
                    theme.getId(),
                    theme.getName(),
                    theme.getDescription(),
                    theme.getThumbnail());
        }
    }
}
