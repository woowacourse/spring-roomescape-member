package roomescape.auth.dto;

import roomescape.member.entity.Role;

public class LoginMember {

    private final Long id;
    private final String email;
    private final String name;
    private final Role role;

    public LoginMember(Long id, String email, String name, Role role) {
        this.id = id;
        this.email = email;
        this.name = name;
        this.role = role;
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
