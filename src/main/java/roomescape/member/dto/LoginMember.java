package roomescape.member.dto;

import roomescape.common.Role;

public record LoginMember(Long id, Role role, String name, String email) {
}
