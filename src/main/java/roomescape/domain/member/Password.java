package roomescape.domain.member;

import java.util.regex.Pattern;
import org.springframework.util.Assert;

public class Password {
    private static final int MIN_LENGTH = 8;
    private static final int MAX_LENGTH = 20;
    private static final Pattern PASSWORD_PATTERN = Pattern.compile("^[a-zA-Z0-9!@#$%^&*()_+-={}\\[\\];:'\",.<>/?]*$");

    private final String password;

    protected Password(String password) {
        Assert.hasText(password, "비밀번호가 비어있습니다.");
        Assert.doesNotContain(password, " ", "비밀번호에 공백이 포함되어 있습니다.");
        Assert.isTrue(password.length() >= MIN_LENGTH, String.format("비밀번호는 %d글자 이상이어야 합니다.", MIN_LENGTH));
        Assert.isTrue(password.length() <= MAX_LENGTH, String.format("비밀번호는 %d글자 이하여야 합니다.", MAX_LENGTH));
        Assert.isTrue(PASSWORD_PATTERN.matcher(password).matches(), "비밀번호는 영어, 숫자, 특수문자만 가능합니다.");
        this.password = password;
    }

    protected boolean match(String password) {
        return this.password.equals(password);
    }

    protected String getValue() {
        return this.password;
    }
}
