package roomescape.member.model;

public class MemberEmail {

    private static final String REGEX_PATTERN = "^(.+)@(\\S+)$";

    private String value;

    public MemberEmail(final String value) {
        checkNullOrEmpty(value);
        validateEmailRegex(value);

        this.value = value;
    }

    private void checkNullOrEmpty(final String value) {
        if (value == null || value.isBlank()) {
            throw new IllegalArgumentException("이메일 값은 공백일 수 없습니다.");
        }
    }

    private void validateEmailRegex(final String email) {
        if (!email.matches(REGEX_PATTERN)) {
            throw new IllegalArgumentException("유효하지 않은 이메일 형식입니다.");
        }
    }
}
