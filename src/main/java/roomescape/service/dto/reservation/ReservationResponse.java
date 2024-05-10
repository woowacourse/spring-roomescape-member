package roomescape.service.dto.reservation;

import roomescape.domain.Member;
import roomescape.domain.Reservation;
import roomescape.domain.ReservationTime;
import roomescape.domain.Theme;

import java.time.LocalDate;

// TODO: 세부 응답을 ResponseDTO로 응답하도록 변경
public record ReservationResponse(Long id, Member member, LocalDate date, ReservationTime time, Theme theme) {

    public static ReservationResponse from(Reservation reservation) {
        return new ReservationResponse(
                reservation.getId(),
                reservation.getMember(),
                reservation.getDate(),
                reservation.getTime(),
                reservation.getTheme()
        );
    }
}
