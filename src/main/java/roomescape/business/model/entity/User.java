package roomescape.business.model.entity;

public class User {

    private final String name;
    private final String email;
    private final String password;

    private User(final String name, final String email, final String password) {
        this.name = name;
        this.email = email;
        this.password = password;
    }

    public static User beforeSave(final String name, final String email, final String password) {
        return new User(name, email, password);
    }

    public boolean isPasswordCorrect(final String password) {
        return this.password.equals(password);
    }

    public String email() {
        return email;
    }
}
