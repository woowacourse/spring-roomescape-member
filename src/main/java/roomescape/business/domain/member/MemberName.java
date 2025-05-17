package roomescape.business.domain.member;

import roomescape.exception.MemberException;

public record MemberName(
        String value
) {

    private static final int MIN_LENGTH = 2;
    private static final int MAX_LENGTH = 8;

    public MemberName {
        if (value == null || value.isBlank()) {
            throw new MemberException("사용자 이름은 null이거나 빈 문자열일 수 없습니다.");
        }
        if (value.length() < MIN_LENGTH || MAX_LENGTH < value.length()) {
            throw new MemberException("사용자 이름은 %d자 이상 %d자 이하이어야 합니다.".formatted(MIN_LENGTH, MAX_LENGTH));
        }
    }

    public String getValue() {
        return value;
    }
}
