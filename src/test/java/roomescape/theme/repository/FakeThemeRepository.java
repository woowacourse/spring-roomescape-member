package roomescape.theme.repository;

import roomescape.theme.entity.Theme;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Optional;

public class FakeThemeRepository implements ThemeRepository {

    private final List<Theme> entities = new ArrayList<>();

    @Override
    public Theme save(Theme entity) {
        entities.add(entity);
        return entity;
    }

    @Override
    public List<Theme> findAll() {
        return Collections.unmodifiableList(entities);
    }

    @Override
    public boolean deleteById(Long id) {
        return entities.removeIf(entity -> entity.getId().equals(id));
    }

    @Override
    public Optional<Theme> findById(Long id) {
        return entities.stream()
                .filter(entity -> entity.getId().equals(id))
                .findFirst();
    }

    @Override
    public Optional<Theme> findByName(String name) {
        return entities.stream()
                .filter(entity -> entity.getName().equals(name))
                .findFirst();
    }

    @Override
    public List<Theme> findPopularThemesByDateRangeAndLimit(LocalDate startDate, LocalDate endDate, final int limit) {
        // TODO: 테스트 로직 작성하기
        return null;
    }
}
