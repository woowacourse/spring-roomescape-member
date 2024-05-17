package roomescape.web.api.resolver;

import roomescape.domain.member.Role;

public record Principal(Long id, String email, Role role) {
}
