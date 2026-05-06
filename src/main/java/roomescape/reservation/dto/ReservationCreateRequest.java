package roomescape.reservation.dto;

import com.fasterxml.jackson.annotation.JsonFormat;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import java.time.LocalDate;
import roomescape.reservation.domain.Reservation;

public record ReservationCreateRequest(
        @NotBlank(message = "[ERROR] 이름은 비어있을 수 없습니다.")
        String name,
        @NotNull(message = "[ERROR] 날짜는 비어있을 수 없습니다.")
        @JsonFormat(pattern = "yyyy-MM-dd")
        LocalDate date,
        @NotNull(message = "[ERROR] 테마는 비어있을 수 없습니다.")
        Long themeId,
        @NotNull(message = "[ERROR] 시간은 비어있을 수 없습니다.")
        Long timeId
) {
    public Reservation toEntity(Long themeId, Long timeId) {
        return Reservation.builder()
                .name(name)
                .date(date)
                .themeId(themeId)
                .timeId(timeId)
                .build();
    }
}
