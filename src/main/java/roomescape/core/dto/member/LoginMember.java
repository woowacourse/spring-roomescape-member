package roomescape.core.dto.member;

import roomescape.core.domain.Member;

public class LoginMember {
    private Long id;
    private String name;
    private String email;
    private String password;

    public LoginMember() {
    }

    public LoginMember(final Member member) {
        this(member.getId(), member.getName(), member.getEmail(), member.getPassword());
    }

    public LoginMember(final Long id, final String name, final String email, final String password) {
        this.id = id;
        this.email = email;
        this.name = name;
        this.password = password;
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
}
