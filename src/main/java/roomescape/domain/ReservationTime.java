package roomescape.domain;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import lombok.Getter;
import roomescape.global.exception.InactiveException;
import roomescape.global.exception.ValidationException;

@Getter
public class ReservationTime {

    private final Long id;
    private final LocalTime startAt;
    private final boolean isActive;

    private ReservationTime(Long id, LocalTime startAt, boolean isActive) {
        this.id = id;
        this.startAt = startAt;
        this.isActive = isActive;
    }

    public static ReservationTime create(LocalTime startAt) {
        validateStartAt(startAt);

        return new ReservationTime(null, startAt, true);
    }

    public static ReservationTime restore(Long id, LocalTime startAt, boolean isActive) {
        return new ReservationTime(id, startAt, isActive);
    }

    private static void validateStartAt(LocalTime startAt) {
        if (startAt == null) {
            throw new ValidationException("추가 할 예약 시작 시간 정보가 누락되었습니다.");
        }
    }

    public LocalDateTime toReservationDateTime(LocalDate date) {
        return LocalDateTime.of(date, this.startAt);
    }

    public boolean isAvailableAt(LocalDate date) {
        return !toReservationDateTime(date).isBefore(LocalDateTime.now());
    }

    public void validateInactive() {
        if (!isActive()) {
            throw new InactiveException("비활성화 된 시간대는 예약할 수 없습니다.");
        }
    }

    public ReservationTime deactivate() {
        return restore(id, startAt, false);
    }
}
