package roomescape.domain.member;

import java.util.regex.Pattern;

public class MemberName {
    private static final Pattern NAME_FORMAT = Pattern.compile("^[a-zA-Z가-힣]*$");
    private static final int MIN_LENGTH = 2;
    private static final int MAX_LENGTH = 10;

    private final String name;

    public MemberName(String name) {
        validateBlank(name);
        validatePattern(name);
        validateLength(name);
        this.name = name;
    }

    private void validateBlank(String name) {
        if (name == null || name.isBlank()) {
            throw new IllegalArgumentException("이름이 비어있습니다.");
        }
    }

    private void validatePattern(String name) {
        if (!NAME_FORMAT.matcher(name).matches()) {
            throw new IllegalArgumentException("이름이 영어이거나 한글이 아닙니다.");
        }
    }

    private void validateLength(String name) {
        if (name.length() < MIN_LENGTH || name.length() > MAX_LENGTH) {
            throw new IllegalArgumentException("이름 길이는 2글자 이상, 10글자 이하여야 합니다.");
        }
    }

    public String getValue() {
        return this.name;
    }
}
