package roomescape.business.domain.member;

public final class MemberCredential {

    private final Long id;
    private final Email email;
    private final MemberPassword password;

    public MemberCredential(Long id, Email email, MemberPassword password) {
        this.id = id;
        this.email = email;
        this.password = password;
    }

    public MemberCredential(Long id, String email, String password) {
        this(id, new Email(email), new MemberPassword(password));
    }

    public MemberCredential(String email, String password) {
        this(null, new Email(email), new MemberPassword(password));
    }

    public boolean matchesPassword(String rawPassword) {
        return password != null
                && rawPassword != null
                && password.matches(rawPassword);
    }

    public Long getId() {
        return id;
    }

    public String getEmail() {
        return email.value();
    }

    public String getPassword() {
        return password.value();
    }
}
