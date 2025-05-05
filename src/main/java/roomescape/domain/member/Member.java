package roomescape.domain.member;


public class Member {

    private final Long id;
    private final String email;
    private final String password;
    private final Role role;
    private final String name;

    public Member(Long id, String email, String password, String name, Role role) {
        this.id = id;
        this.email = email;
        this.password = password;
        this.role = role;
        this.name = name;
    }

    public static Member of(long id, Member member) {
        return new Member(id, member.email, member.password, member.name, member.role);
    }

    public boolean isSamePassword(String password) {
        return this.password.equals(password);
    }

    public boolean isSameUsername(String username) {
        return this.email.equals(username);
    }

    public String getEmail() {
        return email;
    }

    public Long getId() {
        return id;
    }

    public Role getRole() {
        return role;
    }

    public String getPassword() {
        return password;
    }

    public String getName() {
        return name;
    }
}
