package roomescape.service.dto.request;

import com.fasterxml.jackson.annotation.JsonFormat;
import jakarta.validation.constraints.NotBlank;
import java.time.LocalDate;
import roomescape.domain.Member;
import roomescape.domain.Reservation;
import roomescape.domain.ReservationTime;
import roomescape.domain.Theme;

public record ReservationRequest(
        @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd")
        @NotBlank(message = "날짜를 입력해주세요.")
        LocalDate date,
        @NotBlank(message = "시간 ID를 입력해주세요.")
        Long timeId,
        @NotBlank(message = "테마 ID를 입력해주세요.")
        Long themeId,
        @NotBlank(message = "멤버 ID를 입력해주세요.")
        Long memberId) {

    public Reservation toEntity(Member member, ReservationTime reservationTime, Theme theme) {
        return new Reservation(member, date, reservationTime, theme);
    }
}
