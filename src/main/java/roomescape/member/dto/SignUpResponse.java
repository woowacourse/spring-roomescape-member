package roomescape.member.dto;

import roomescape.member.entity.Member;
import roomescape.member.entity.Role;

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

    public static SignUpResponse from(Member member) {
        return new SignUpResponse(member.getId(), member.getEmail(), member.getName(), member.getRole());
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
