package roomescape.domain.member;

import java.util.regex.Pattern;

public class MemberName {
    private static final Pattern NAME_PATTERN = Pattern.compile("^[a-zA-Z가-힣]*$");
    private static final int MIN_LENGTH = 2;
    private static final int MAX_LENGTH = 10;

    private final String name;

    protected MemberName(String name) {
        validateBlank(name);
        validatePattern(name);
        validateLength(name);
        this.name = name;
    }

    private void validateBlank(String name) {
        if (name == null || name.isBlank()) {
            throw new IllegalArgumentException("사용자명이 비어있습니다.");
        }
    }

    private void validatePattern(String name) {
        if (!NAME_PATTERN.matcher(name).matches()) {
            throw new IllegalArgumentException("사용자명은 영어, 한글만 가능합니다.");
        }
    }

    private void validateLength(String name) {
        if (name.length() < MIN_LENGTH || MAX_LENGTH < name.length()) {
            throw new IllegalArgumentException(
                    String.format("사용자명은 %d글자 이상, %d글자 이하여야 합니다.", MIN_LENGTH, MAX_LENGTH));
        }
    }

    protected String getValue() {
        return this.name;
    }
}
