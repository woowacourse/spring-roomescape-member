package roomescape.dto.request;

import java.util.List;
import roomescape.dto.response.ThemeResponse;

public record ThemesWithTotalPageRequest(int totalPages, List<ThemeResponse> themes) {
}
