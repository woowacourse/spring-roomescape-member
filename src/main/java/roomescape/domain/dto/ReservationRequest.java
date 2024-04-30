package roomescape.domain.dto;

import roomescape.domain.Reservation;
import roomescape.domain.TimeSlot;

import java.time.LocalDate;

public record ReservationRequest(String name, LocalDate date, Long timeId) {
    public ReservationRequest {
        isValid(name, date, timeId);
    }

    public Reservation toEntity(Long id, TimeSlot time) {
        return new Reservation(id, name, date, time);
    }

    private void isValid(String name, LocalDate date, Long timeId) {
        if (name == null || name.trim().isEmpty()) {
            throw new IllegalArgumentException("[ERROR] 이름은 비워둘 수 없습니다.");
        }

        if (date == null || date.isBefore(LocalDate.now())) {
            throw new IllegalArgumentException("[ERROR] 올바르지 않은 예약 날짜입니다.");
        }

        if (timeId == null || timeId <= 0) {
            throw new IllegalArgumentException("[ERROR] 올바르지 않은 예약 시간입니다.");
        }
    }
}
