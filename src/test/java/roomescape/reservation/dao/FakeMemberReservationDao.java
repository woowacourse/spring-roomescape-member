package roomescape.reservation.dao;

import java.time.LocalDate;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.stream.Collectors;
import roomescape.member.domain.Member;
import roomescape.reservation.domain.MemberReservation;
import roomescape.reservation.domain.Reservation;
import roomescape.reservation.domain.ReservationTime;
import roomescape.reservation.domain.Theme;
import roomescape.reservation.domain.repository.MemberReservationRepository;

public class FakeMemberReservationDao implements MemberReservationRepository {
    private Map<Long, MemberReservation> memberReservations = new HashMap<>();

    @Override
    public long save(Member member, Reservation reservation) {
        memberReservations.put((long) memberReservations.size() + 1,
                new MemberReservation((long) memberReservations.size() + 1, member, reservation));
        return memberReservations.size();
    }

    @Override
    public List<MemberReservation> findAll() {
        return memberReservations.values().stream()
                .toList();
    }

    @Override
    public Optional<MemberReservation> findById(long id) {
        return Optional.of(memberReservations.get(id));
    }

    @Override
    public void deleteById(long memberReservationId) {
        memberReservations.remove(memberReservationId);
    }

    @Override
    public void deleteByReservationId(long reservationId) {
        memberReservations = memberReservations.entrySet().stream()
                .filter(entry -> entry.getValue().getReservation().getId() != reservationId)
                .collect(Collectors.toMap(Map.Entry::getKey, Map.Entry::getValue));
    }

    @Override
    public boolean existBy(LocalDate date, ReservationTime time, Theme theme) {
        return memberReservations.values().stream()
                .anyMatch(memberReservation -> memberReservation.getReservation().isSame(date, time, theme));
    }
}
