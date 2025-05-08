package roomescape.business.domain.member;

public record MemberPassword(
        String value
) {

    public MemberPassword {
        if (value == null || value.isBlank()) {
            throw new IllegalArgumentException("비밀번호는 null이거나 빈 문자열일 수 없습니다.");
        }
    }
}
