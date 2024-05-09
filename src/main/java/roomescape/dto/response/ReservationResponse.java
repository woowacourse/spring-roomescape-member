package roomescape.dto.response;

import java.time.format.DateTimeFormatter;

import roomescape.domain.Reservation;

public record ReservationResponse(Long id, MemberResponse member, String date, ReservationTimeResponse time, ThemeResponse theme) {

    public ReservationResponse {
        validate(id, member, date, time, theme);
    }

    private void validate(Long id, MemberResponse member, String date, ReservationTimeResponse time, ThemeResponse theme) {
        if (id == null || member == null || time == null || theme == null) {
            throw new IllegalArgumentException("잘못된 응답입니다. id=" + id + ", member=" + member + ", date=" + date + ", time=" + time);
        }
    }

    public static ReservationResponse from(Reservation reservation) {
        return new ReservationResponse(
                reservation.getId(),
                MemberResponse.from(reservation.getMember()),
                reservation.getDate(DateTimeFormatter.ISO_DATE),
                ReservationTimeResponse.from(reservation.getTime()),
                ThemeResponse.from(reservation.getTheme()
                ));
    }
}
