package roomescape.dto;

import roomescape.domain.Member;
import roomescape.domain.Role;

public class LoginMemberDto {
    private Long id;
    private String name;
    private String email;
    private String password;
    private Role role;

    public LoginMemberDto() {
    }

    public LoginMemberDto(final Member member) {
        this(member.getId(), member.getName(), member.getEmail(), member.getPassword(), member.getRole());
    }

    public LoginMemberDto(
            final Long id,
            final String name,
            final String email,
            final String password,
            final Role role
    ) {
        this.id = id;
        this.name = name;
        this.email = email;
        this.password = password;
        this.role = role;
    }

    public Member toMember() {
        return new Member(id, name, email, password, role);
    }

    public Long getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public String getEmail() {
        return email;
    }

    public String getPassword() {
        return password;
    }

    public Role getRole() {
        return role;
    }
}
