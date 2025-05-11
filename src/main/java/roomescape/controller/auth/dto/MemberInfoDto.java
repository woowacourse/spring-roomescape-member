package roomescape.controller.auth.dto;

import roomescape.common.Role;

public record MemberInfoDto(
        Long id,
        Role role
) {
}
