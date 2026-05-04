package roomescape.reservation.dto;

import com.fasterxml.jackson.annotation.JsonFormat;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import java.time.LocalDate;
import roomescape.reservation.domain.Reservation;
import roomescape.reservationtime.domain.ReservationTime;

public record ReservationCreateRequest(
        @NotBlank(message = "[ERROR] 이름은 비어있을 수 없습니다.")
        String name,
        @NotNull(message = "[ERROR] 날짜는 비어있을 수 없습니다.")
        @JsonFormat(pattern = "yyyy-MM-dd")
        LocalDate date,
        @NotNull(message = "[ERROR] 시간은 비어있을 수 없습니다.")
        Long timeId
) {
    public Reservation toEntity(ReservationTime time) {
        return Reservation.builder()
                .name(name)
                .date(date)
                .time(time)
                .build();
    }
}
