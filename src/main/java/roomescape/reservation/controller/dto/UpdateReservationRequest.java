package roomescape.reservation.controller.dto;

import jakarta.validation.constraints.FutureOrPresent;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;
import java.time.LocalDate;
import org.springframework.format.annotation.DateTimeFormat;

public record UpdateReservationRequest (
        @FutureOrPresent(message = "예약 날짜는 과거일 수 없습니다. 예약 가능한 날짜를 확인해주세요.")
        @DateTimeFormat(pattern = "yyyy-MM-dd")
        @NotNull
        LocalDate date,

        @NotNull(message = "시간 정보는 필수입니다.")
        @Positive(message = "식별자는 양수여야 합니다.")
        Long timeId
){
}
