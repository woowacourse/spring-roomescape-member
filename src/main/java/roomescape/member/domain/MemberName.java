package roomescape.member.domain;

public class MemberName {
    private static final int MAX_NAME_LENGTH = 10;
    private final String value;

    public MemberName(final String name) {
        validateName(name);
        this.value = name;
    }

    private void validateName(final String name) {
        if (name.isBlank()) {
            throw new IllegalArgumentException("[ERROR] 예약자명은 빈 값일 수 없습니다.");
        }
        if (name.length() > MAX_NAME_LENGTH) {
            throw new IllegalArgumentException("[ERROR] 예약자명의 길이는 " + MAX_NAME_LENGTH + "자 이하입니다.");
        }
    }

    public String getValue() {
        return value;
    }
}
