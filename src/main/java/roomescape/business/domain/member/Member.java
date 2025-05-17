package roomescape.business.domain.member;

public final class Member {

    private final Long id;
    private final MemberName name;
    private final Email email;
    private final MemberRole role;

    public Member(Long id, MemberName name, Email email, MemberRole role) {
        this.id = id;
        this.name = name;
        this.email = email;
        this.role = role;
    }

    public Member(Long id, String name, String email, MemberRole role) {
        this(id, new MemberName(name), new Email(email), role);
    }

    public Member(Long id, String name, String email) {
        this(id, name, email, MemberRole.MEMBER);
    }

    public Member(String name, String email) {
        this(null, name, email, MemberRole.MEMBER);
    }

    public Member(String name, String email, MemberRole role) {
        this(null, name, email, role);
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

    public MemberRole getRole() {
        return role;
    }
}
