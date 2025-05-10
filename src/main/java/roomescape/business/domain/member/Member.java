package roomescape.business.domain.member;

public final class Member {

    private final Long id;
    private final MemberName name;
    private final Email email;
    private final MemberPassword password;
    private final MemberRole role;

    public Member(Long id, MemberName name, Email email, MemberPassword password, MemberRole role) {
        this.id = id;
        this.name = name;
        this.email = email;
        this.password = password;
        this.role = role;
    }

    public Member(Long id, String name, String email, String password, MemberRole role) {
        this(id, new MemberName(name), new Email(email), new MemberPassword(password), role);
    }

    public Member(Long id, String name, String email, String password) {
        this(id, name, email, password, MemberRole.MEMBER);
    }

    public Member(String name, String email, String password) {
        this(null, name, email, password, MemberRole.MEMBER);
    }

    public Member(String name, String email, String password, MemberRole role) {
        this(null, name, email, password, role);
    }

    public static Member createWithoutPassword(Long id, String name, String email, MemberRole role) {
        return new Member(id, new MemberName(name), new Email(email), null, role);
    }

    public Long getId() {
        return id;
    }

    public String getName() {
        return name.value();
    }

    public String getEmail() {
        return email.value();
    }

    public String getPassword() {
        return password.value();
    }

    public MemberRole getRole() {
        return role;
    }
}
