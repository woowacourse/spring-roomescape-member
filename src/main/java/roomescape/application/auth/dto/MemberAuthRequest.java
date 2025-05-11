package roomescape.application.auth.dto;

import jakarta.validation.constraints.NotNull;

public record MemberAuthRequest(@NotNull Long id) {
}
