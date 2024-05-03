package roomescape.domain.reservation;

import roomescape.exception.InvalidValueException;

public class ReservationName {

    private final String value;

    public ReservationName(String value) {
        validateNull(value);
        this.value = value;
    }

    private void validateNull(String value) {
        if (value == null || value.isBlank()) {
            throw new InvalidValueException("이름은 비어있을 수 없습니다.");
        }
    }

    public String getValue() {
        return value;
    }
}
