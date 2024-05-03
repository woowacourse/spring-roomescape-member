package roomescape.domain.reservation;

import java.util.regex.Pattern;

public record Name(String name) {
    private static final Pattern format = Pattern.compile("^[a-zA-Zㄱ-ㅎ가-힣]*$");
    private static final int MIN_LENGTH = 2;
    private static final int MAX_LENGTH = 10;

    public Name {
        validateName(name);
    }

    private void validateName(String name) {
        if (format.matcher(name).matches()) {
            throw new IllegalArgumentException("이름은 영어 또는 한글만 가능합니다.");
        }

        if (name.length() < MIN_LENGTH || name.length() > MAX_LENGTH) {
            throw new IllegalArgumentException(String.format("이름은 %d~%d자만 가능합니다.", MAX_LENGTH, MAX_LENGTH));
        }
    }
}
