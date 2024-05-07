package roomescape.service.dto;

import roomescape.domain.Reservation;
import roomescape.domain.ReservationTime;
import roomescape.domain.Theme;
import roomescape.exception.IllegalUserRequestException;

import java.time.LocalDate;

public record SaveReservationRequest(String name, LocalDate date, Long timeId, Long themeId) {

    public SaveReservationRequest {
        validateNameBlank(name);
    }

    private void validateNameBlank(String name) {
        if (name.isBlank()) {
            throw new IllegalUserRequestException("이름은 빈칸일 수 없습니다.");
        }
    }

    public static Reservation toEntity(SaveReservationRequest request, ReservationTime reservationTime, Theme theme) {
        return new Reservation(request.name(), request.date(), reservationTime, theme);
    }


}
