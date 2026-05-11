package roomescape.domain;

import java.util.Arrays;

public enum ReservationStatus {
    DRAFT, AVAILABLE, DELETED;

    public static ReservationStatus from(String value) {
        return Arrays.stream(ReservationStatus.values())
                .filter(status -> status.name().equals(value))
                .findFirst()
                .orElseThrow(() -> new IllegalArgumentException("존재하지 않는 상태입니다: " + value));
    }
}
