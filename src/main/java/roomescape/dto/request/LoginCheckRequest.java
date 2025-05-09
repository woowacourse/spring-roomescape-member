package roomescape.dto.request;

import roomescape.entity.Role;

public record LoginCheckRequest(
    Long id,
    String name,
    String email,
    Role role
) {
    public static LoginCheckRequest of(Long id, String name, String email, Role role) {
        return new LoginCheckRequest(id, name, email, role);
    }
}
