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
    private Map<Long, Long> memberReservation = new HashMap<>();

    @Override
    public Reservation save(final Reservation reservation) {
        reservations.put((long) reservations.size() + 1, reservation);
        return new Reservation((long) reservations.size(), reservation.getDate(), reservation.getTime(),
                reservation.getTheme());
    }

    @Override
    public List<ReservationMember> findAllMemberReservation() {
        return memberReservation.keySet().stream()
                .map(key -> new ReservationMember(key, reservations.get(key), getMemberChoco())).toList();
    }

    @Override
    public void deleteMemberReservationById(long memberReservationId) {
        memberReservation.remove(memberReservationId);
    }

    @Override
    public void deleteMemberReservationByReservationId(long reservationId) {
        memberReservation = memberReservation.entrySet().stream()
                .filter(entry -> entry.getValue() != reservationId)
                .collect(Collectors.toMap(Map.Entry::getKey, Map.Entry::getValue));
    }


    @Override
    public void delete(final long reservationId) {
        reservations.remove(reservationId);
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
    public boolean existMemberReservationBy(final LocalDate date, final long timeId, final long themeId) {
        Reservation reservation1 = reservations.values().stream()
                .filter(reservation -> reservation.getDate().equals(date) && reservation.getTime().getId()
                        .equals(timeId) && reservation.getTheme().getId().equals(themeId)).findFirst().orElseThrow();

        return memberReservation.containsValue(reservation1.getId());
    }

    @Override
    public long saveMemberReservation(final long memberId, final long reservationId) {
        memberReservation.put(memberId, reservationId);
        return memberReservation.size();
    }
}
