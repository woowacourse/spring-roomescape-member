package roomescape.domain;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;
import java.util.Set;

public interface ReservationRepository {

    List<Reservation> findAll();

    Optional<Reservation> findById(Long id);

    Set<Long> findReservedTimeIdsByDateAndThemeId(LocalDate date, Long themeId);

    boolean existsByTimeId(Long timeId);

    boolean existsByThemeId(Long themeId);

    boolean existsBy(LocalDate date, Long timeId, Long themeId);

    Long save(Reservation Reservation);

    void deleteById(Long id);

    default Reservation getById(Long id) {
        return findById(id).orElseThrow(() -> new IllegalArgumentException("존재하지 않는 예약 ID입니다."));
    }
}
