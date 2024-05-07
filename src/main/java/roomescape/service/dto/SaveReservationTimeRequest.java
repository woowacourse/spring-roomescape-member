package roomescape.service.dto;

import com.fasterxml.jackson.annotation.JsonCreator;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import roomescape.domain.ReservationTime;

import java.time.LocalTime;

public record SaveReservationTimeRequest(@NotNull(message = "예약 시간은 null일 수 없습니다.") LocalTime startAt) {

    public static ReservationTime toEntity(SaveReservationTimeRequest request) {
        return new ReservationTime(request.startAt());
    }
}
