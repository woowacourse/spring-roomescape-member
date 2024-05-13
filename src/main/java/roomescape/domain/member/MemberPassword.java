package roomescape.domain.member;

public class MemberPassword {

    private final String value;

    public MemberPassword(String value) {
        validateValue(value);
        this.value = value;
    }

    private void validateValue(String value) {
        if (value == null || value.isBlank()) {
            throw new IllegalArgumentException("비밀번호는 비어있을 수 없습니다.");
        }
    }

    public String getValue() {
        return value;
    }
}
