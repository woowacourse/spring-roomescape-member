package roomescape.domain;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.ToString;
import roomescape.global.exception.InactiveException;

@Getter
@EqualsAndHashCode(of = "id")
@ToString
public class ReservationTime {

    private final Long id;
    private final LocalTime startAt;
    private boolean isActive;

    public ReservationTime(Long id, LocalTime startAt, boolean isActive) {
        if (startAt == null) {
            throw new IllegalArgumentException("추가 할 예약 시작 시간 정보가 누락되었습니다.");
        }
        this.id = id;
        this.startAt = startAt;
        this.isActive = isActive;
    }

    public ReservationTime(LocalTime startAt) {
        this(null, startAt, true);
    }

    public LocalDateTime toReservationDateTime(LocalDate date) {
        return LocalDateTime.of(date, this.startAt);
    }

    public boolean isAvailableAt(LocalDate date) {
        return !toReservationDateTime(date).isBefore(LocalDateTime.now());
    }

    public void validateInactiveTime() {
        if (!isActive()) {
            throw new InactiveException("비활성화 된 시간대는 예약할 수 없습니다.");
        }
    }

    public void deactivate() {
        this.isActive = false;
    }
}
