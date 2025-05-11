package roomescape.theme.dto.request;

import java.util.List;
import roomescape.theme.dto.response.ThemeResponse;

public record ThemesWithTotalPageRequest(int totalPages, List<ThemeResponse> themes) {
}
