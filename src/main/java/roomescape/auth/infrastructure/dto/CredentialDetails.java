package roomescape.auth.infrastructure.dto;

import lombok.Builder;

@Builder
public record CredentialDetails(
        Long id,
        String email,
        String name,
        String role
) {
}
