package roomescape.domain;

import java.util.Objects;

public record ReservationName(String value) {
    public ReservationName {
        Objects.requireNonNull(value);
        if (value.isEmpty()) {
            throw new IllegalArgumentException("예약자 이름은 한글자 이상이어야 합니다.");
        }
    }
}
