package roomescape.repository;

import java.time.LocalDate;
import java.util.List;
import java.util.Set;
import roomescape.domain.Reservation;

public interface ReservationRepository {

    Reservation save(Reservation reservation);

    void deleteById(Long id);

    boolean existByDateAndTimeIdAndThemeId(LocalDate date, Long timeId, Long themeId);

    List<Reservation> findAllByPaging(int page, int size);

    Set<Long> findReservedTimeIdsByThemeIdAndDate(Long themeId, LocalDate date);

    List<Reservation> findAllByUserName(String name);
}
