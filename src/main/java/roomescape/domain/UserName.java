package roomescape.domain;

public record UserName(
        String value
) {
    private static final int NAME_MAX_LENGTH = 10;

    public UserName {
        if (value.isBlank()) {
            throw new IllegalArgumentException("예약자 이름은 비어 있을 수 없습니다.");
        }

        if (value.length() > NAME_MAX_LENGTH) {
            throw new IllegalArgumentException("예약자 이름은 10자를 초과할 수 없습니다.");
        }
    }

    public static UserName parse(String value) {
        return new UserName(value);
    }
}
