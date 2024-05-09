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
    public Optional<MemberReservation> findById(long id) {
        return Optional.of(memberReservations.get(id));
    }

    @Override
    public List<MemberReservation> findBy(Member member, Theme theme, LocalDate startDate, LocalDate endDate) {
        return memberReservations.values().stream()
                .filter(memberReservation -> memberReservation.isMember(member) &&
                        memberReservation.getReservation().getTheme().equals(theme) &&
                        isInDateRange(memberReservation.getReservation().getDate(), startDate, endDate))
                .toList();
    }

    @Override
    public List<MemberReservation> findBy(Member member, LocalDate startDate, LocalDate endDate) {
        return memberReservations.values().stream()
                .filter(memberReservation -> memberReservation.isMember(member) &&
                        isInDateRange(memberReservation.getReservation().getDate(), startDate, endDate))
                .toList();
    }

    @Override
    public List<MemberReservation> findBy(Theme theme, LocalDate startDate, LocalDate endDate) {
        return memberReservations.values().stream()
                .filter(memberReservation -> memberReservation.getReservation().getTheme().equals(theme) &&
                        isInDateRange(memberReservation.getReservation().getDate(), startDate, endDate))
                .toList();
    }

    @Override
    public List<MemberReservation> findBy(LocalDate startDate, LocalDate endDate) {
        return memberReservations.values().stream()
                .filter(memberReservation -> isInDateRange(memberReservation.getReservation().getDate(), startDate,
                        endDate))
                .toList();
    }

    private boolean isInDateRange(LocalDate today, LocalDate start, LocalDate end) {
        return !today.isBefore(start) && !today.isAfter(end);
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
