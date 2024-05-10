package roomescape.domain.member;


import roomescape.domain.Name;

public class Member {
    private final Long id;
    private final Name name;
    private final Email email;
    private final Password password;
    private final Role role;

    public Member(
            final Long id,
            final Name name,
            final Email email,
            final Password password,
            final Role role) {
        this.id = id;
        this.name = name;
        this.email = email;
        this.password = password;
        this.role = role;
    }

    public Member(
            final long id,
            final String name,
            final String userEmail,
            final String userPassword,
            final String role) {
        this(
                id,
                new Name(name),
                new Email(userEmail),
                new Password(userPassword),
                Role.mapTo(role)
        );
    }

    public String getName() {
        return name.getValue();
    }

    public String getEmail() {
        return email.getEmail();
    }

    public String getPassword() {
        return password.getPassword();
    }

    public Long getId() {
        return id;
    }

    public String getRole() {
        return role.getRole();
    }
}
