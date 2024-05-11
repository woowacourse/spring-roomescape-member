package roomescape.dto.response;

import java.time.LocalDate;

import com.fasterxml.jackson.annotation.JsonFormat;

import roomescape.domain.Reservation;
import roomescape.dto.MemberModel;

public record ReservationResponse(Long id,
                                  @JsonFormat(pattern = "yyyy-MM-dd") LocalDate date,
                                  MemberModel member, TimeResponse time, ThemeResponse theme) {
    public static ReservationResponse from(Reservation reservation) {
        return new ReservationResponse(
                reservation.id(),
                reservation.date(),
                MemberModel.from(reservation.member()),
                TimeResponse.from(reservation.time()),
                ThemeResponse.from(reservation.theme()));
    }
}
