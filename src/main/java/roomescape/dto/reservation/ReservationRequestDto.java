package roomescape.dto.reservation;

import com.fasterxml.jackson.annotation.JsonFormat;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;

import java.time.LocalDate;

public record ReservationRequestDto(
        @NotBlank(message = "이름은 필수 입력값입니다.")
        String name,

        @NotNull(message = "날짜는 필수 입력값입니다.")
        @JsonFormat(pattern = "yyyy-MM-dd")
        LocalDate date,
        Long timeId,
        Long themeId
) {
}


/*
@NotBlank(message = "이름은 필수 입력값입니다.")
    String name,

    @NotNull(message = "날짜는 필수 입력값입니다.")
    @Future(message = "예약은 미래 날짜만 가능합니다.")
    @JsonFormat(pattern = "yyyy-MM-dd")
    LocalDate date,

    @NotNull(message = "시간 ID는 필수 입력값입니다.")
    Long timeId



    public ReservationResponse addReservation(@Valid @RequestBody ReservationRequest request) {
 */