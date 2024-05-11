package roomescape.domain.member;

public record MemberName(String value) {
    private static final int NAME_LENGTH_MIN = 2;
    private static final int NAME_LENGTH_MAX = 10;

    public MemberName {
        validateNotNull(value);
        validateNameLength(value);
    }

    private void validateNotNull(String value) {
        if (value == null) {
            throw new IllegalArgumentException("이름은 null이 될 수 없습니다.");
        }
    }

    private void validateNameLength(String value) {
        if (value.length() < NAME_LENGTH_MIN || NAME_LENGTH_MAX < value.length()) {
            throw new IllegalArgumentException(String.format("사용자 이름은 %d자 이상 %d자 이하여야 합니다. (현재 입력한 이름 길이: %d자)",
                    NAME_LENGTH_MIN, NAME_LENGTH_MAX, value.length()));
        }
    }
}
