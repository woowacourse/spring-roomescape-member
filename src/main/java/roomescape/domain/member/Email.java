package roomescape.domain.member;

import java.util.regex.Pattern;
import org.springframework.util.Assert;

public class Email {
    private static final int MIN_LENGTH = 5;
    private static final int MAX_LENGTH = 30;
    private static final Pattern EMAIL_PATTERN = Pattern.compile("^[a-zA-Z0-9._%+-]+@[a-zA-Z0-9.-]+\\.[a-zA-Z]{2,6}$");

    private final String email;

    protected Email(String email) {
        Assert.hasText(email, "이메일이 비어있습니다.");
        Assert.isTrue(email.length() >= MIN_LENGTH, String.format("이메일은 %d자 이상이어야 합니다.", MIN_LENGTH));
        Assert.isTrue(email.length() <= MAX_LENGTH, String.format("이메일은 %d자 이하여야 합니다.", MAX_LENGTH));
        Assert.isTrue(EMAIL_PATTERN.matcher(email).matches(), "이메일 형식이 올바르지 않습니다.");
        this.email = email;
    }

    protected String getValue() {
        return email;
    }
}
