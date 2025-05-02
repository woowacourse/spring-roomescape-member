package roomescape.reservation.repository;

import roomescape.reservation.entity.Theme;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Optional;

public class FakeThemeRepository implements ThemeRepository {

    private final List<Theme> themes = new ArrayList<>();

    @Override
    public Theme save(Theme theme) {
        themes.add(theme);
        return theme;
    }

    @Override
    public List<Theme> findAll() {
        return Collections.unmodifiableList(themes);
    }

    @Override
    public boolean deleteById(Long id) {
        return themes.removeIf(entity -> entity.getId().equals(id));
    }

    @Override
    public Optional<Theme> findById(Long id) {
        return themes.stream()
                .filter(entity -> entity.getId().equals(id))
                .findFirst();
    }

    @Override
    public Optional<Theme> findByName(String name) {
        return themes.stream()
                .filter(entity -> entity.getName().equals(name))
                .findFirst();
    }

    @Override
    public List<Theme> findPopularDescendingUpTo(LocalDate startDate, LocalDate endDate, final int limit) {
        // TODO: 테스트 로직 작성하기
        return null;
    }
}
