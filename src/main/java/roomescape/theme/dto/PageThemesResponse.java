package roomescape.theme.dto;

import java.util.List;

public record PageThemesResponse(
        List<ThemeResponse> themes,
        int page,
        int size,
        boolean hasNext
) {
    public static PageThemesResponse from(List<ThemeResponse> themes, int page, int size, boolean hasNext) {
        return new PageThemesResponse(themes, page, size, hasNext);
    }
}
