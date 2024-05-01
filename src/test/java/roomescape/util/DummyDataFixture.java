package roomescape.util;

import java.time.LocalDate;
import java.time.LocalTime;
import java.util.List;
import java.util.NoSuchElementException;
import roomescape.reservation.model.Reservation;
import roomescape.reservationtime.model.ReservationTime;
import roomescape.theme.model.Theme;

public class DummyDataFixture {
    private final ReservationTime reservationTime1 = new ReservationTime(1L, LocalTime.of(15, 40));
    private final ReservationTime reservationTime2 = new ReservationTime(2L, LocalTime.of(10, 00));
    private final ReservationTime reservationTime3 = new ReservationTime(3L, LocalTime.of(13, 00));

    private final Theme theme = new Theme(1L, "공포", "아아", "ㅠㅠ");

    private final Reservation reservation1 = new Reservation(1L, "아서", LocalDate.of(2024, 4, 24), reservationTime1,
            theme);
    private final Reservation reservation2 = new Reservation(2L, "몰리", LocalDate.of(2024, 4, 23), reservationTime2,
            theme);

    public List<Reservation> getPreparedReservations() {
        return List.of(reservation1, reservation2);
    }

    public List<ReservationTime> getPreParedReservationTimes() {
        return List.of(reservationTime1, reservationTime2, reservationTime3);
    }

    public Reservation getReservationById(final Long id) {
        return getPreparedReservations().stream()
                .filter(reservation -> reservation.getId().equals(id))
                .findAny().orElseThrow(() -> new NoSuchElementException("해당하는 더미데이터가 없습니다."));
    }

    public ReservationTime getReservationTimeById(final Long id) {
        return getPreParedReservationTimes().stream()
                .filter(reservation -> reservation.getId().equals(id))
                .findAny().orElseThrow(() -> new NoSuchElementException("해당하는 더미데이터가 없습니다."));
    }
}
