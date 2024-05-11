package roomescape.domain.member;

public class Member {
    private final Long id;
    private final MemberName memberName;
    private final String email;
    private final String password;
    private final Role role;

    public Member(Long id, MemberName memberName, String email, String password, Role role) {
        // todo email, password validation
        this.id = id;
        this.memberName = memberName;
        this.email = email;
        this.password = password;
        this.role = role;
    }

    public Member(Long id, String name, String email, String password, Role role) {
        this(id, new MemberName(name), email, password, role);
    }

    public boolean matchPassword(String password) {
        return this.password.equals(password);
    }

    public Long getId() {
        return id;
    }

    public String getName() {
        return memberName.getValue();
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
