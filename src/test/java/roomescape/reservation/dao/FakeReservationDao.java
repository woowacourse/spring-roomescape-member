package roomescape.reservation.dao;

import java.time.LocalDate;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;
import roomescape.member.domain.repository.MemberRepository;
import roomescape.member.dto.CompletedReservation;
import roomescape.member.dto.MemberResponse;
import roomescape.reservation.domain.Reservation;
import roomescape.reservation.domain.repository.ReservationRepository;
import roomescape.reservation.dto.ReservationResponse;
import roomescape.reservation.dto.ReservationTimeResponse;
import roomescape.reservation.dto.ThemeResponse;

public class FakeReservationDao implements ReservationRepository {
    private final Map<Long, Reservation> reservations = new HashMap<>();
    private final Map<Long, Long> reservationList = new HashMap<>();
    private final MemberRepository memberRepository;

    public FakeReservationDao(MemberRepository memberRepository) {
        this.memberRepository = memberRepository;
    }


    @Override
    public Reservation save(final Reservation reservation) {
        long newId = reservations.size() + 1;
        Reservation savedReservation = new Reservation(
                newId,
                reservation.getDate(),
                reservation.getTime(),
                reservation.getTheme()
        );
        reservations.put(newId, savedReservation);
        return savedReservation;
    }

    @Override
    public List<CompletedReservation> findAll() {
        System.out.println(reservationList);
        List<Reservation> reservationValues = reservations.values().stream().toList();
        return reservationValues.stream()
                .map(reservation -> new CompletedReservation(reservation.getId(), reservation.getDate(),
                                reservation.getTime(), reservation.getTheme(),
                                memberRepository.findById(reservationList.get(reservation.getId()))))
                .toList();
    }

    @Override
    public List<CompletedReservation> findBy(Long themeId, Long memberId, Date dateFrom, Date dateTo) {
        return null;
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
    public boolean existByTimeId(long timeId) {
        return reservations.values().stream()
                .anyMatch(reservation -> reservation.getTime().getId() == timeId);
    }


    @Override
    public boolean existBy(LocalDate date, long timeId, long themeId) {
        return reservations.values().stream()
                .anyMatch(reservation -> reservation.getDate().equals(date) &&
                        reservation.getTime().getId() == timeId &&
                        reservation.getTheme().getId() == themeId);
    }


    @Override
    public void saveReservationList(long reservationId, long memberId) {
        reservationList.put(reservationId, memberId);
    }

    @Override
    public long findMemberIdByReservationId(long reservationId) {
        return reservationList.get(reservationId);
    }
}
