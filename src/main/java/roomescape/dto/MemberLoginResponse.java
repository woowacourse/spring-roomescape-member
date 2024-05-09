package roomescape.dto;

import roomescape.domain.Role;

public record MemberLoginResponse(Long id, String name, String email, Role role) {
}
