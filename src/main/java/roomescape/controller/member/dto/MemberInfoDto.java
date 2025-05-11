package roomescape.controller.member.dto;

import roomescape.common.Role;

public record MemberInfoDto(
        Long id,
        Role role
) {
}
