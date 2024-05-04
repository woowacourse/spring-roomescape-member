package roomescape.reservation.model;

public class ReservationName {
    private static final int MAXIMUM_NAME_LENGTH = 255;

    private final String value;

    public ReservationName(final String name) {
        validateNameIsBlank(name);
        validateNameLength(name);
        this.value = name;
    }

    private void validateNameIsBlank(final String name) {
        if (name == null || name.isBlank()) {
            throw new IllegalArgumentException("예약자 명은 공백 문자가 불가능합니다.");
        }
    }

    private void validateNameLength(final String name) {
        if (name.length() > MAXIMUM_NAME_LENGTH) {
            throw new IllegalArgumentException("예약자 명은 최대 255자까지 입력이 가능합니다.");
        }
    }

    public String getValue() {
        return value;
    }
}
