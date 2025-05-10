package roomescape.dto.auth;

import roomescape.common.Role;

public record MemberInfoDto(
        Long id,
        Role role
) {
}
