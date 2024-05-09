package roomescape.domain.member;


import roomescape.domain.Name;

public class Member {
    private final Long id;
    private final Name name;
    private final Email email;
    private final Password password;
    private final Role role;

    public Member(Long id, Name name, Email email, Password password, Role role) {
        this.id = id;
        this.name = name;
        this.email = email;
        this.password = password;
        this.role = role;
    }

    public Member(long id, String name, String userEmail, String userPassword, String role) {
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
