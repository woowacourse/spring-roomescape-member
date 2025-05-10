package roomescape.auth.dto;

import lombok.Builder;

@Builder
public record AuthenticatedMember(
        Long id,
        String email,
        String name,
        String role
) {
}
