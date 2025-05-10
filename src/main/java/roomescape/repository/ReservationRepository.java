package roomescape.repository;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;
import roomescape.domain.member.Member;
import roomescape.domain.reservation.Reservation;
import roomescape.domain.reservation.ReservationDate;
import roomescape.domain.reservation.ReservationDateTime;
import roomescape.domain.theme.Theme;

public interface ReservationRepository {

    Reservation save(Member member, ReservationDateTime reservationDateTime, Theme theme);

    Optional<Reservation> findById(Long id);

    List<Reservation> findAll();

    void deleteById(Long id);

    boolean existSameDateTime(ReservationDate reservationDate, Long timeId);

    boolean existReservationByTimeId(Long timeId);

    boolean existReservationByThemeId(Long themeId);

    List<Reservation> findAllWithCondition(Long memberId, Long themeId, LocalDate fromDate, LocalDate toDate);
}
