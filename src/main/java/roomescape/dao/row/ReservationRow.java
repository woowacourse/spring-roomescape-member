package roomescape.dao.row;

import roomescape.domain.Reservation;

import java.time.LocalDate;


public record ReservationRow(Long id,
                             String name,
                             LocalDate date,
                             TimeRow timeRow,
                             ThemeRow themeRow) implements DomainConvertible<Reservation> {

    public ReservationRow(String name, LocalDate date, TimeRow time, ThemeRow theme) {
        this(null, name, date, time, theme);
    }

    public static ReservationRow from(Reservation reservation) {
        return new ReservationRow(
                reservation.getId(),
                reservation.getName(),
                reservation.getDate(),
                TimeRow.from(reservation.getTime()),
                ThemeRow.from(reservation.getTheme())
        );
    }

    @Override
    public Reservation toDomain() {
        return new Reservation(id, name, date, timeRow.toDomain(), themeRow.toDomain());
    }
}
