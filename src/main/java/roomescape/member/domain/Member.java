package roomescape.member.domain;

public class Member {

    private static final long EMPTY_ID = 0L;

    private final Long id;
    private final MemberName name;
    private final Email email;
    private final Password password;

    public Member(final Long id, final String name, final String email, final String password) {
        validateNull(id, name, email, password);
        this.id = id;
        this.name = new MemberName(name);
        this.email = new Email(email);
        this.password = new Password(password);
    }

    public Member(final String name, final String email, final String password) {
        this(EMPTY_ID, name, email, password);
    }

    private void validateNull(final Long id, final String name, final String email, final String password) {
        if (id == null || name == null || email == null || password == null) {
            throw new IllegalArgumentException("사용자 정보가 존재하지 않습니다.");
        }
    }

    public Long getId() {
        return id;
    }

    public String getName() {
        return name.getValue();
    }

    public String getEmail() {
        return email.getValue();
    }

    public String getPassword() {
        return password.getValue();
    }
}
