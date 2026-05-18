package roomescape.controller.dto.reservation;

import com.fasterxml.jackson.annotation.JsonFormat;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import roomescape.domain.vo.MemberName;
import roomescape.domain.vo.ReservationDate;
import roomescape.service.command.ReservationCommand;

import java.time.LocalDate;

public record ReservationRequestDto(
        @NotBlank(message = "이름은 필수 입력값입니다.")
        String name,

        @NotNull(message = "날짜는 필수 입력값입니다.")
        @JsonFormat(pattern = "yyyy-MM-dd")
        LocalDate date,

        @NotNull(message = "시간 ID는 필수 입력값입니다.")
        Long timeId,

        @NotNull(message = "테마 ID는 필수 입력값입니다.")
        Long themeId
) {
        public ReservationCommand toCommand() {
                return new ReservationCommand(
                        MemberName.from(name),
                        ReservationDate.from(date),
                        timeId,
                        themeId
                );
        }
}