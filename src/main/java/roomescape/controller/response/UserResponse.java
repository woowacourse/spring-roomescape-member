package roomescape.controller.response;

import roomescape.domain.Role;

public record UserResponse(Long id, String name, String email, String password, Role role) {
}
