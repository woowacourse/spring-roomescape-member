package roomescape.domain;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.ToString;

@Getter
@EqualsAndHashCode(of = "id")
@ToString
public class ReservationTime {

    private final Long id;
    private final LocalTime startAt;

    public ReservationTime(Long id, LocalTime startAt) {
        if (startAt == null) {
            throw new IllegalArgumentException("추가 할 예약 시작 시간 정보가 누락되었습니다.");
        }
        this.id = id;
        this.startAt = startAt;
    }

    public ReservationTime(LocalTime startAt) {
        this(null, startAt);
    }

    public LocalDateTime toReservationDateTime(LocalDate date) {
        return LocalDateTime.of(date, this.startAt);
    }

    public boolean isAvailableAt(LocalDate date) {
        return !toReservationDateTime(date).isBefore(LocalDateTime.now());
    }
}
