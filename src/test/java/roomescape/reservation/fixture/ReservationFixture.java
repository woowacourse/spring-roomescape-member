package roomescape.reservation.fixture;

import roomescape.date.domain.ReservationDate;
import roomescape.reservation.domain.Reservation;
import roomescape.reservation.domain.ReservationStatus;
import roomescape.reservation.dto.request.ReservationSaveDto;
import roomescape.theme.domain.Theme;
import roomescape.time.domain.ReservationTime;

public class ReservationFixture {

    public static Reservation reservation(
            String name,
            ReservationDate date,
            ReservationTime time,
            Theme theme
    ) {
        return Reservation.create(name, date, time, theme);
    }

    public static Reservation canceledReservation(
            String name,
            ReservationDate date,
            ReservationTime time,
            Theme theme
    ) {
        Reservation reservation = Reservation.create(name, date, time, theme);
        reservation.updateStatus(ReservationStatus.CANCELED);
        return reservation;
    }

    public static ReservationSaveDto saveDto(
            String name,
            ReservationDate date,
            ReservationTime time,
            Theme theme
    ) {
        return new ReservationSaveDto(name, date.id(), time.id(), theme.id());
    }

    public static ReservationSaveDto saveDto(
            String name,
            ReservationDate date,
            Long timeId,
            Theme theme
    ) {
        return new ReservationSaveDto(name, date.id(), timeId, theme.id());
    }

    public static ReservationSaveDto saveDto(
            String name,
            Long dateId,
            ReservationTime time,
            Theme theme
    ) {
        return new ReservationSaveDto(name, dateId, time.id(), theme.id());
    }

    public static ReservationSaveDto saveDto(
            String name,
            ReservationDate date,
            ReservationTime time,
            Long themeId
    ) {
        return new ReservationSaveDto(name, date.id(), time.id(), themeId);
    }

}
