package roomescape.member.domain;

import java.util.Objects;

public class Password {

    private final String password;

    public Password(String password) {
        this.password = Objects.requireNonNull(password);
    }

    public String getPassword() {
        return password;
    }
}
