package roomescape.util;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import roomescape.reservation.model.Reservation;
import roomescape.reservationtime.model.ReservationTime;
import roomescape.theme.model.Theme;

public class ReservationFixture {

    public static Reservation getOne() {
        return Reservation.of(
                null,
                "몰리의 신기한 이야기",
                LocalDate.parse("3000-10-10"),
                ReservationTimeFixture.getOne(),
                ThemeFixture.getOne()
        );
    }

    public static Reservation getOne(final ReservationTime reservationTime) {
        return Reservation.of(
                null,
                "몰리의 신기한 이야기",
                LocalDate.parse("3000-10-10"),
                reservationTime,
                ThemeFixture.getOne()
        );
    }

    public static Reservation getOne(final LocalDate date,
                                     final ReservationTime reservationTime,
                                     final Theme theme) {
        return Reservation.of(
                null,
                "몰리의 신기한 이야기",
                date,
                reservationTime,
                theme
        );
    }

    public static List<Reservation> get(final int count) {
        final List<Reservation> reservations = new ArrayList<>();
        for (int i = 0; i < count; i++) {
            reservations.add(Reservation.of(
                            null,
                            "몰리의 신기한 이야기",
                            LocalDate.parse("3000-10-10"),
                            ReservationTimeFixture.getOne(),
                            ThemeFixture.getOne()
                    )
            );
        }

        return reservations;
    }
}
