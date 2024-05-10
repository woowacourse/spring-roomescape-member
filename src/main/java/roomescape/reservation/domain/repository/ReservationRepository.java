package roomescape.reservation.domain.repository;

import java.time.LocalDate;
import java.util.Arrays;
import java.util.Date;
import java.util.List;
import roomescape.member.dto.CompletedReservation;
import roomescape.reservation.domain.Reservation;

public interface ReservationRepository {
    Reservation save(Reservation reservation);

    List<CompletedReservation> findAll();

    List<CompletedReservation> findBy(Long themeId, Long memberId, LocalDate dateFrom, LocalDate dateTo);

    boolean deleteById(long reservationId);

    boolean existByTimeId(long timeId);

    boolean existBy(LocalDate date, long timeId, long themeId);
    void saveReservationList(long memberId, long reservationId);

    long findMemberIdByReservationId(long reservationId);
}
