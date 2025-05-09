package roomescape.user.dto;

import roomescape.common.vo.Role;
import roomescape.user.entity.User;

public class SignUpResponse {
    private final Long id;
    private final String email;
    private final String name;
    private final Role role;

    public SignUpResponse(Long id, String email, String name, Role role) {
        this.id = id;
        this.email = email;
        this.name = name;
        this.role = role;
    }

    public static SignUpResponse from(User user) {
        return new SignUpResponse(user.getId(), user.getEmail(), user.getName(), user.getRole());
    }

    public Long getId() {
        return id;
    }

    public String getEmail() {
        return email;
    }

    public String getName() {
        return name;
    }

    public Role getRole() {
        return role;
    }
}
