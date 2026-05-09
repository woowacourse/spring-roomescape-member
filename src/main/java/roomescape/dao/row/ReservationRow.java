package roomescape.dao.row;

import roomescape.domain.Reservation;
import roomescape.domain.vo.Name;

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
                reservation.getName().value(),
                reservation.getDate(),
                TimeRow.from(reservation.getTime()),
                ThemeRow.from(reservation.getTheme())
        );
    }

    @Override
    public Reservation toDomain() {
        return new Reservation(id, new Name(name), date, timeRow.toDomain(), themeRow.toDomain());
    }
}
