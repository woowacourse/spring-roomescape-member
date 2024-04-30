package roomescape.service.dto;

import roomescape.domain.Reservation;
import roomescape.domain.ReservationTime;

import java.time.LocalDate;

public record SaveReservationRequest(String name, LocalDate date, Long timeId) {

    public SaveReservationRequest {
        validateNameBlank(name);
    }

    private void validateNameBlank(String name) {
        if (name.isBlank()) {
            throw new IllegalArgumentException("이름은 빈칸일 수 없습니다.");
        }
    }

    public static Reservation toEntity(SaveReservationRequest request, ReservationTime reservationTime) {
        return new Reservation(request.name(), request.date(), reservationTime);
    }


}
