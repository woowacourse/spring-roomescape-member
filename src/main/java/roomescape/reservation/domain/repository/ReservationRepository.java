package roomescape.reservation.domain.repository;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;
import roomescape.reservation.domain.Reservation;
import roomescape.reservation.domain.dto.ReservationMember;

public interface ReservationRepository {
    Reservation save(Reservation reservation);

    List<ReservationMember> findAll();

    boolean deleteById(long reservationId);

    boolean existsByTimeId(long timeId);

    Optional<ReservationMember> findBy(LocalDate date, long timeId, long themeId);

    void saveReservationList(long memberId, long reservationId);
}
