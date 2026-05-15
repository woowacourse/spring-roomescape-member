package roomescape.domain.reservationtime.dto;

import com.fasterxml.jackson.annotation.JsonFormat;
import jakarta.validation.constraints.NotNull;

import java.time.LocalTime;

public class ReservationTimeCreateRequest {

    @NotNull(message = "시간은 필수입니다.")
    @JsonFormat(pattern = "HH:mm")
    private final LocalTime startAt;

    public ReservationTimeCreateRequest(LocalTime startAt) {
        this.startAt = startAt;
    }

    public LocalTime getStartAt() {
        return startAt;
    }
}
