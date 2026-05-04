package roomescape.domain;

import java.time.LocalTime;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeParseException;

public class ReservationTime {

    private final Long id;
    private final String startAt;

    public ReservationTime(Long id, String startAt) {
        validateTime(startAt);

        this.id = id;
        this.startAt = startAt;
    }

    public Long getId() {
        return id;
    }

    public String getStartAt() {
        return startAt;
    }

    private void validateTime(String startAt) {
        if (startAt == null || startAt.isBlank()) {
            throw new IllegalArgumentException("[ERROR] 시간은 비어 있을 수 없습니다.");
        }
    }
}
