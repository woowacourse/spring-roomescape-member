package roomescape.domain.reservation.dto;

import jakarta.validation.constraints.NotNull;
import java.time.LocalDate;

// TODO 날짜 제약 걸기
public record ReservationRequest(@NotNull String name, @NotNull LocalDate date,@NotNull Long timeId) {

}
