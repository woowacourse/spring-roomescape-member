package roomescape.dto;

import jakarta.validation.constraints.FutureOrPresent;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import java.time.LocalDate;
import roomescape.domain.Reservation;
import roomescape.domain.ReservationTime;

public record AddReservationDto(@NotNull @NotBlank(message = "이름이 비어있을 수 없습니다.") String name,
                                @FutureOrPresent(message = "날짜는 현재보다 미래여야합니다.") LocalDate date, Long timeId) {

    public Reservation toReservation(ReservationTime reservationTime) {
        return new Reservation(null, name, date, reservationTime);
    }
}

