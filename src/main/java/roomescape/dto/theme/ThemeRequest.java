package roomescape.dto.theme;

import org.springframework.lang.NonNull;

public record ThemeRequest(
        @NonNull String name,
        @NonNull String description,
        @NonNull String thumbnail
) {
}
