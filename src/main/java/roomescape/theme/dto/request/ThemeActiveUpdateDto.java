package roomescape.theme.dto.request;

import jakarta.validation.constraints.NotNull;

public record ThemeActiveUpdateDto(
        @NotNull(message="상태 유무는 필수입니다.")
        boolean isActive
) {
}
