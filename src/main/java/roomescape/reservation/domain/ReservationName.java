package roomescape.reservation.domain;

import java.util.regex.Pattern;

public class ReservationName {

    private static final int NAME_LENGTH = 4;
    private static final Pattern RESERVATION_NAME_FORMAT = Pattern.compile("^[가-힣]+$");
    private final String name;

    public ReservationName(String name) {
        validate(name);
        this.name = name;
    }

    private void validate(String name) {
        validateIsNull(name);
        validateIsBlank(name);
        validateFormat(name);
        validateLength(name);
    }

    private void validateIsNull(String name) {
        if (name == null) {
            throw new IllegalArgumentException("값을 입력하지 않았습니다.");
        }
    }

    private void validateIsBlank(String name) {
        if (name.isBlank()) {
            throw new IllegalArgumentException("예약자명에는 빈 문자열, 공백을 입력할 수 없습니다.");
        }
    }

    private void validateFormat(String name) {
        if (!RESERVATION_NAME_FORMAT.matcher(name).matches()) {
            throw new IllegalArgumentException("예약자명은 한글만 입력 가능합니다.");
        }
    }

    private void validateLength(String name) {
        if (name.length() > NAME_LENGTH) {
            throw new IllegalArgumentException(String.format("예약자명은 %d자까지만 입력가능합니다.", NAME_LENGTH));
        }
    }

    public String getName() {
        return name;
    }
}
