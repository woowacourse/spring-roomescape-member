package roomescape.domain.member;

public class Member {

    private final Long id;
    private final Name name;
    private final Email email;
    private final Password password;

    public Member(Long id, String rawName, String rawEmail, String rawPassword) {
        this.id = id;
        this.name = new Name(rawName);
        this.email = new Email(rawEmail);
        this.password = new Password(rawPassword);
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
