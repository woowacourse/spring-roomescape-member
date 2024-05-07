package roomescape.repository;

import java.time.LocalDate;
import java.util.List;
import roomescape.domain.Reservation;

public interface ReservationRepository {

    List<Reservation> findAll();

    Reservation save(Reservation reservation);

    int deleteById(Long id);

    Boolean isTimeIdUsed(Long id);

    Boolean isThemeIdUsed(Long id);

    Boolean isDuplicated(LocalDate date, Long timeId, Long themeId);

    List<Reservation> findAllByDateAndThemeId(LocalDate date, Long themeId);
}
