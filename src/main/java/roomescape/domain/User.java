package roomescape.domain;

public class User {

    private final Name name;
    private final Email email;
    private final String password;

    public User(Name name, Email email, String password) {
        this.name = name;
        this.email = email;
        this.password = password;
    }

    public boolean isMatchPassword(String password) {
        return this.password.equals(password);
    }

    public String getId() {
        return email.getEmail();
    }
}
