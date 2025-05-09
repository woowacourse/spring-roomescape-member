package roomescape.business.domain.member;

public record MemberPassword(
        String value
) {

    public MemberPassword {
        // TODO: 비밀번호를 DB에서 조회하지 않으면 문제가 발생함
//        if (value == null || value.isBlank()) {
//            throw new IllegalArgumentException("비밀번호는 null이거나 빈 문자열일 수 없습니다.");
//        }
    }
}
