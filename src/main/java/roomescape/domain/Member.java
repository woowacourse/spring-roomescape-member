package roomescape.domain;

import roomescape.constant.Role;

public class Member {

    private Long id;
    private String name;
    private String email;
    private String password;
    private Role role;

    public Member() {
    }

    public Member(String name, String email, String password, Role role) {
        this(null, name, email, password, role);
    }

    public Member(Long id, String name, String email, String password, Role role) {
        validate(name, email, password);
        this.id = id;
        this.name = name;
        this.email = email;
        this.password = password;
        this.role = role;
    }

    // TODO: 유효성 검사 추가
    private void validate(String name, String email, String password) {
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
