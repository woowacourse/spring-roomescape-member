package roomescape.dto;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonFormat.Shape;
import roomescape.model.Reservation;

import java.time.LocalDate;

public record ReservationResponse(
        Long id,
        MemberResponse member,
        @JsonFormat(shape = Shape.STRING, pattern = "yyyy-MM-dd", timezone = "Asia/Seoul")
        LocalDate date,
        ReservationTimeResponse time,
        ThemeResponse theme
) {

    public ReservationResponse(final Reservation reservation) {
        this(
                reservation.getId(),
                new MemberResponse(reservation.getMember()),
                reservation.getDate(),
                new ReservationTimeResponse(reservation.getTime()),
                new ThemeResponse(reservation.getTheme())
        );
    }
}
