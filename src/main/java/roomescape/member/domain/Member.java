package roomescape.member.domain;

public class Member {
    private final Long id;
    private final String email;
    private final String password;
    private final String name;
    private final Role role;

    public Member(Long id, String email, String password, String name, Role role) {
        this.id = id;
        this.email = email;
        this.password = password;
        this.name = name;
        this.role = role;
    }

    public Member(Long id, String email, String password, String name, String role) {
        this.id = id;
        this.email = email;
        this.password = password;
        this.name = name;
        this.role = Role.from(role);
    }

    public Member createMemberWithId(Long id) {
        return new Member(id, this.email, this.password, this.name, this.role);
    }

    public boolean isSameRole(Role role) {
        return this.role == role;
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
