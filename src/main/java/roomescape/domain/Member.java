package roomescape.domain;

import java.util.regex.Pattern;
import lombok.Getter;
import lombok.NonNull;

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

    public Member(final Long id, @NonNull final String name, @NonNull final String email, @NonNull final String password) {
        validateEmail(email);
        this.id = id;
        this.name = name;
        this.email = email;
        this.password = password;
    }

    private void validateEmail(final String email) {
        if (!EMAIL_PATTERN.matcher(email).matches()) {
            throw new IllegalArgumentException("이메일 형식이 올바르지 않습니다.");
        }
    }
}
