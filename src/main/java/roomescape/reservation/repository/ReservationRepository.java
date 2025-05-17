package roomescape.reservation.repository;

import java.time.LocalDate;
import java.util.List;
import roomescape.reservation.domain.Reservation;

public interface ReservationRepository {

    Long saveAndReturnId(Reservation reservation);

    int deleteById(Long id);

    List<Reservation> findAll();

    Boolean existReservationByTimeId(Long timeId);

    Boolean existReservationByThemeId(Long themeId);

    Boolean existReservationByDateAndTimeIdAndThemeId(LocalDate date, Long timeId, Long themeId);

    List<Reservation> findAllByDateAndThemeId(LocalDate date, Long themeId);

    List<Reservation> findAllByThemeIdAndMemberIdAndPeriod(Long themeId, Long memberId, LocalDate dateFrom,
                                                           LocalDate dateTo);


}
