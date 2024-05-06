package roomescape.reservation.domain.repository;

import java.time.LocalDate;
import java.util.List;
import roomescape.reservation.domain.Reservation;
import roomescape.reservation.domain.dto.ReservationMember;

public interface ReservationRepository {
    Reservation save(Reservation reservation);

    List<ReservationMember> findAllReservationList();

    boolean deleteReservationListById(long reservationMemberId);

    boolean delete(long reservationId);

    boolean existsByTimeId(long timeId);

    boolean existsByThemeId(long themeId);

    boolean existsBy(LocalDate date, long timeId, long themeId);

    boolean existReservationListBy(LocalDate date, long timeId, long themeId);

    long saveReservationList(long memberId, long reservationId);
}
