package roomescape.domain;

import java.util.regex.Pattern;
import lombok.Getter;
import lombok.NonNull;
import roomescape.error.AuthenticationException;

@Getter
public class Member {

    private static final Pattern EMAIL_PATTERN = Pattern.compile("^[\\w.-]+@[\\w.-]+\\.[a-zA-Z]{2,}$");

    private Long id;

    @NonNull
    private final String name;

    @NonNull
    private final String email;

    @NonNull
    private final String password;

    @NonNull
    private final Role role;

    public Member(final Long id, @NonNull final String name, @NonNull final String email,
                  @NonNull final String password, @NonNull final Role role) {
        validateEmail(email);
        this.id = id;
        this.name = name;
        this.email = email;
        this.password = password;
        this.role = role;
    }

    private void validateEmail(final String email) {
        if (!EMAIL_PATTERN.matcher(email).matches()) {
            throw new IllegalArgumentException("이메일 형식이 올바르지 않습니다.");
        }
    }

    public void validatePassword(final String rawPassword) {
        if (!this.password.equals(rawPassword)) {
            throw new AuthenticationException("비밀번호가 일치하지 않습니다.");
        }
    }

    @Override
    public boolean equals(final Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof Member other)) {
            return false;
        }
        if (this.id == null || other.id == null) {
            return false;
        }
        return this.id.equals(other.id);
    }

    @Override
    public int hashCode() {
        if (id == null) {
            return 0;
        }
        return id.hashCode();
    }
}
