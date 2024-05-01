package roomescape.domain;

import java.time.LocalDate;
import java.util.List;

public interface ReservationRepository {

    List<Reservation> findAll();

    Reservation save(Reservation reservation);

    int deleteById(Long id);

    Long countByTimeId(Long id);

    Long countByThemeId(Long id);

    Long countDuplication(LocalDate date, Long timeId, Long themeId);

    List<Reservation> findAllByDateAndThemeId(LocalDate date, Long themeId);
}
