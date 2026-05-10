package roomescape.admin.dto;

import java.util.List;

public record AdminThemesResponse(
        List<AdminThemeResponse> themes,
        int count
) {
}
