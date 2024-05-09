package roomescape.reservation.domain.repository;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;
import roomescape.member.domain.Member;
import roomescape.reservation.domain.MemberReservation;
import roomescape.reservation.domain.Reservation;
import roomescape.reservation.domain.ReservationTime;
import roomescape.reservation.domain.Theme;

public interface MemberReservationRepository {
    long save(Member member, Reservation reservation);

    Optional<MemberReservation> findById(long id);

    List<MemberReservation> findBy(Member member, Theme theme, LocalDate startDate, LocalDate endDate);

    List<MemberReservation> findBy(Member member, LocalDate startDate, LocalDate endDate);

    List<MemberReservation> findBy(Theme theme, LocalDate startDate, LocalDate endDate);

    List<MemberReservation> findBy(LocalDate startDate, LocalDate endDate);

    void deleteById(long memberReservationId);

    void deleteByReservationId(long reservationId);

    boolean existBy(LocalDate date, ReservationTime time, Theme theme);
}
