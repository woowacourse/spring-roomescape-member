package roomescape.domain.vo;

public record MemberName(
        String value
) {
    public MemberName {
        validateNotBlank(value);
    }

    public static MemberName from(String name) {
        return new MemberName(name);
    }

    private void validateNotBlank(String value) {
        if (value == null || value.trim().isBlank()) {
            throw new IllegalArgumentException("빈 문자열은 이름으로 사용할 수 없습니다.");
        }
    }
}
