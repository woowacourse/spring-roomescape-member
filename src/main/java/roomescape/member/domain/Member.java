package roomescape.member.domain;

import java.util.Objects;

public class Member {

    private final Long id;
    private final Name name;
    private final Email email;
    private final Password password;

    private Member(final Long id, final Name name, final Email email, final Password password) {
        this.id = id;
        this.name = Objects.requireNonNull(name);
        this.email = Objects.requireNonNull(email);
        this.password = Objects.requireNonNull(password);
    }

    public static Member createWithId(final Long id, String name, String email, String password) {
        return new Member(Objects.requireNonNull(id), new Name(name), new Email(email), new Password(password));
    }

    public static Member createWithoutId(String name, String email, String password) {
        return new Member(null, new Name(name), new Email(email), new Password(password));
    }

    public boolean isSamePassword(final String password) {
        return this.password.equals(password);
    }

    public Long getId() {
        return id;
    }

    public String getName() {
        return name.getName();
    }

    public String getEmail() {
        return email.getEmail();
    }

    public String getPassword() {
        return password.getPassword();
    }
}
