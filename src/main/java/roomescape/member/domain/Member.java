package roomescape.member.domain;

import roomescape.name.domain.Name;

public class Member {

    private static final String DEFAULT_NAME = "어드민";

    private long id;
    private final Name name;
    private final Email email;
    private final Password password;

    private Member(long id, String name, String email, String password) {
        this.id = id;
        this.name = new Name(DEFAULT_NAME);
        this.email = new Email(email);
        this.password = new Password(password);
    }

    private Member(String name, String email, String password) {
        this(0, name, email, password);
    }

    public static Member memberOf(long id, String name, String email, String password) {
        return new Member(id, name, email, password);
    }

    public static Member saveMemberOf(String email, String password, String name) {
        return new Member(name, email, password);
    }

    public long getId() {
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
