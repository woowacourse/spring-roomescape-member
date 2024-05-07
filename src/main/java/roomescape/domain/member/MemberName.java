package roomescape.domain.member;

public class MemberName {

    private final String value;

    private MemberName(String value) {
        validateValue(value);
        this.value = value;
    }

    public MemberName(MemberEmail email) {
        this(email.extractName());
    }

    private void validateValue(String value) {
        if (value == null || value.isBlank()) {
            throw new IllegalArgumentException("사용자 이름은 비어있을 수 없습니다.");
        }
    }

    public String getValue() {
        return value;
    }
}
