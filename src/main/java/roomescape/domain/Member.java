package roomescape.domain;

public class Member {

    private final Long id;
    private final String email;
    private final String password;
    private final String name;
    private final String sessionId;

    public Member(final Long id, final String email, final String password, final String name, final String sessionId) {
        this.id = id;
        this.email = email;
        this.password = password;
        this.name = name;
        this.sessionId = sessionId;
    }

    public static Member createWithOutIdAndSession(final String email, final String password, final String name) {
        return new Member(null, email, password, name, null);
    }

    public Member createWithId(final long id) {
        return new Member(id, email, password, name, sessionId);
    }

    public Long getId() {
        return id;
    }

    public String getEmail() {
        return email;
    }

    public String getPassword() {
        return password;
    }

    public String getName() {
        return name;
    }

    public String getSessionId() {
        return sessionId;
    }
}
