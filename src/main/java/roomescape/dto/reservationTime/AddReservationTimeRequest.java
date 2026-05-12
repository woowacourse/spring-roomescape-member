package roomescape.dto.reservationTime;

import com.fasterxml.jackson.annotation.JsonFormat;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Pattern;
import roomescape.domain.reservationTime.ReservationTime;

import java.time.LocalTime;

public record AddReservationTimeRequest(

        @NotNull(message = "시작 시간은 반드시 포함되어야 합니다.")
        @JsonFormat(pattern = "HH:mm")
        LocalTime startAt
) {

    public ReservationTime toEntity() {
        return new ReservationTime(startAt);
    }
}
