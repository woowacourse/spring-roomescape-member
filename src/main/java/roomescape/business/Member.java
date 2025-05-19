package roomescape.business;

import jakarta.validation.constraints.NotEmpty;

public final class Member {

    private final Long id;
    private final String name;
    private final String email;
    private final String password;
    private final MemberRole role;

    public Member(Long id, String name, String email, String password, MemberRole role) {
        this.id = id;
        this.name = name;
        this.email = email;
        this.password = password;
        this.role = role;
    }

    public Member(String name, String email, String password, MemberRole role) {
        this(null, name, email, password, role);
    }

    public boolean checkValidPassword(@NotEmpty String password) {
        return this.password.equals(password);
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

    public MemberRole getRole() {
        return role;
    }

    public boolean isAdmin() {
        return this.role == MemberRole.ADMIN;
    }
}
