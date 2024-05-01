package roomescape.domain;

import java.time.LocalDate;
import java.util.List;

public interface ReservationRepository {

    List<Reservation> findAll();

    Reservation save(Reservation reservation);

    int deleteById(Long id);

    Long countByTimeId(Long id);

    Long countByThemeId(Long id);

    Long countByDateAndTimeId(LocalDate date, Long timeId); // TODO: themeId 인자 추가
}
