package roomescape.reservation.domain.repository;

import java.time.LocalDate;
import java.util.List;
import roomescape.reservation.domain.Reservation;
import roomescape.reservation.domain.dto.ReservationMember;

public interface ReservationRepository {
    Reservation save(Reservation reservation);

    List<ReservationMember> findAllMemberReservation();

    boolean deleteMemberReservationById(long memberReservationId);

    void deleteMemberReservationByReservationId(long reservationId);

    boolean delete(long reservationId);

    boolean existsByTimeId(long timeId);

    boolean existsByThemeId(long themeId);

    boolean existsBy(LocalDate date, long timeId, long themeId);

    boolean existMemberReservationBy(LocalDate date, long timeId, long themeId);

    long saveMemberReservation(long memberId, long reservationId);
}
