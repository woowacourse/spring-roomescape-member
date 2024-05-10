package roomescape.domain.theme;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

public interface ThemeRepository {
    Theme save(Theme theme);

    default Theme getById(Long id) {
        return findById(id)
                .orElseThrow(() -> new IllegalArgumentException(String.format("존재하지 않는 테마입니다. themeId: %d", id)));
    }

    List<Theme> findAll();

    Optional<Theme> findById(Long id);

    void deleteById(Long id);

    List<Theme> findPastReservations(LocalDate startDate, LocalDate endDate, int limitCount);
}
