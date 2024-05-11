package roomescape.dto.auth;

import roomescape.domain.member.Role;

public record MemberLoginResponse(Long id, String name, String email, Role role) {
}
