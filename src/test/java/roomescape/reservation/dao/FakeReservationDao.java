package roomescape.reservation.dao;

import static roomescape.fixture.MemberFixture.getMemberChoco;

import java.time.LocalDate;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import roomescape.reservation.domain.Reservation;
import roomescape.reservation.domain.dto.ReservationMember;
import roomescape.reservation.domain.repository.ReservationRepository;

public class FakeReservationDao implements ReservationRepository {
    private final Map<Long, Reservation> reservations = new HashMap<>();
    private final Map<Long, Long> reservationList = new HashMap<>();

    @Override
    public Reservation save(final Reservation reservation) {
        reservations.put((long) reservations.size() + 1, reservation);
        return new Reservation(
                (long) reservations.size(),
                reservation.getDate(),
                reservation.getTime(),
                reservation.getTheme()
        );
    }

    @Override
    public List<ReservationMember> findAll() {
        return reservations.values()
                .stream()
                .map(reservation -> new ReservationMember(reservation, getMemberChoco()))
                .toList();
    }

    @Override
    public boolean deleteById(final long reservationId) {
        if (!reservations.containsKey(reservationId)) {
            return false;
        }
        reservations.remove(reservationId);
        return true;
    }

    @Override
    public boolean existsByTimeId(final long timeId) {
        return reservations.values().stream()
                .anyMatch(reservation -> reservation.getTime().getId() == timeId);
    }

    @Override
    public boolean existReservationListBy(final LocalDate date, final long timeId, final long themeId) {
        Reservation reservation1 = reservations.values().stream()
                .filter(reservation -> reservation.getDate().equals(date) && reservation.getTime().getId()
                        .equals(timeId) && reservation.getTheme().getId().equals(themeId))
                .findFirst().orElseThrow();

        return reservationList.containsValue(reservation1.getId());
    }

    @Override
    public void saveReservationList(final long memberId, final long reservationId) {
        reservationList.put(memberId, reservationId);
    }
}
