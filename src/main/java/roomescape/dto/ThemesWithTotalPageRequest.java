package roomescape.dto;

import java.util.List;

public record ThemesWithTotalPageRequest(int totalPages, List<ThemeResponse> themes) {
}
