package roomescape.domain.reservation;

import java.util.Objects;
import java.util.regex.Pattern;

public record Name(String name) {
    private static final Pattern format = Pattern.compile("^[a-zA-Zㄱ-ㅎ가-힣]*$");

    private static final int MIN_LENGTH = 2;
    private static final int MAX_LENGTH = 10;

    public Name {
        validateName(name);
    }

    private void validateName(String name) {
        validateNull(name);
        validateCharacter(name);
        validateLength(name);
    }

    private void validateNull(String name) {
        if (Objects.isNull(name)) {
            throw new IllegalArgumentException("이름 값은 null이 될 수 없습니다.");
        }
    }

    private void validateLength(String name) {
        if (name.length() < MIN_LENGTH || name.length() > MAX_LENGTH) {
            throw new IllegalArgumentException(String.format("이름은 %d~%d자만 가능합니다.", MIN_LENGTH, MAX_LENGTH));
        }
    }

    private void validateCharacter(String name) {
        if (!format.matcher(name).matches()) {
            throw new IllegalArgumentException(String.format("이름: %s, 이름은 영어 또는 한글만 가능합니다.", name));
        }
    }
}
