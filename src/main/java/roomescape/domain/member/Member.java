package roomescape.domain.member;


import roomescape.domain.Name;

public class Member {
    private final Long id;
    private final Name name;
    private final Email email;
    private final Password password;

    public Member(Long id, Name name, Email email, Password password) {
        this.id = id;
        this.name = name;
        this.email = email;
        this.password = password;
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
}
