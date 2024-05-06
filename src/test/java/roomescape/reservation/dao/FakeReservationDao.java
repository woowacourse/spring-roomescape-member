package roomescape.reservation.dao;

import static roomescape.fixture.MemberFixture.getMemberChoco;

import java.time.LocalDate;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;
import roomescape.reservation.domain.Reservation;
import roomescape.reservation.domain.dto.ReservationMember;
import roomescape.reservation.domain.repository.ReservationRepository;

public class FakeReservationDao implements ReservationRepository {
    private final Map<Long, Reservation> reservations = new HashMap<>();
    private Map<Long, Long> reservationList = new HashMap<>();

    @Override
    public Reservation save(final Reservation reservation) {
        reservations.put((long) reservations.size() + 1, reservation);
        return new Reservation((long) reservations.size(), reservation.getDate(), reservation.getTime(),
                reservation.getTheme());
    }

    @Override
    public List<ReservationMember> findAllReservationList() {
        return reservationList.keySet().stream()
                .map(key -> new ReservationMember(key, reservations.get(key), getMemberChoco())).toList();
    }

    @Override
    public boolean deleteReservationListById(long reservationMemberId) {
        if (!reservationList.containsKey(reservationMemberId)) {
            return false;
        }
        reservationList.remove(reservationMemberId);
        return true;
    }

    @Override
    public boolean delete(final long reservationId) {
        if (!reservations.containsKey(reservationId)) {
            return false;
        }
        reservations.remove(reservationId);
        reservationList = reservationList.entrySet().stream().filter(entry -> entry.getValue() == reservationId)
                .collect(Collectors.toMap(Map.Entry::getKey, Map.Entry::getValue));
        return true;
    }

    @Override
    public boolean existsByTimeId(long timeId) {
        return reservations.values().stream().anyMatch(reservation -> reservation.getTime().getId() == timeId);
    }

    @Override
    public boolean existsByThemeId(long themeId) {
        return reservations.values().stream().anyMatch(reservation -> reservation.getTheme().getId() == themeId);
    }

    @Override
    public boolean existsBy(LocalDate date, long timeId, long themeId) {
        return reservations.values().stream().anyMatch(
                reservation -> reservation.getDate().equals(date) && reservation.getTime().getId() == timeId
                        && reservation.getTheme().getId() == themeId);
    }

    @Override
    public boolean existReservationListBy(final LocalDate date, final long timeId, final long themeId) {
        Reservation reservation1 = reservations.values().stream()
                .filter(reservation -> reservation.getDate().equals(date) && reservation.getTime().getId()
                        .equals(timeId) && reservation.getTheme().getId().equals(themeId)).findFirst().orElseThrow();

        return reservationList.containsValue(reservation1.getId());
    }

    @Override
    public long saveReservationList(final long memberId, final long reservationId) {
        reservationList.put(memberId, reservationId);
        return reservationList.size();
    }
}
