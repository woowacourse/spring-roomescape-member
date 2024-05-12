package roomescape.domain;

public record MemberName(String value) {

    private static final int MAX_MEMBER_NAME_LENGTH = 20;

    public MemberName {
        if (value.isBlank()) {
            throw new IllegalArgumentException("이름은 공백을 제외한 1글자 이상이어야 합니다.");
        }

        if (value.length() > MAX_MEMBER_NAME_LENGTH) {
            String message = String.format("이름은 %d글자를 넘을 수 없습니다.", MAX_MEMBER_NAME_LENGTH);
            throw new IllegalArgumentException(message);
        }
    }
}
