package roomescape.repository;

import java.time.LocalTime;
import java.util.List;
import java.util.NoSuchElementException;
import java.util.Optional;
import roomescape.domain.Theme;

public interface ThemeRepository {

    List<Theme> findAll();

    Optional<Theme> findById(Long id);

    default Theme getById(Long id) {
        return findById(id)
                .orElseThrow(() -> new NoSuchElementException("해당 id의 테마가 존재하지 않습니다."));
    }

    Theme save(Theme theme);

    void deleteById(Long id);

    boolean existsByName(String name);
}
