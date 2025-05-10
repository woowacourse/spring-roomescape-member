package roomescape.service.fake_dao;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Objects;
import roomescape.dao.ThemeDao;
import roomescape.entity.Theme;

public class FakeThemeDao implements ThemeDao {

    private final List<Theme> fakeMemory = new ArrayList<>();
    private Long id = 1L;

    @Override
    public List<Theme> findAll() {
        return Collections.unmodifiableList(fakeMemory);
    }

    @Override
    public Theme findById(Long id) {
        return fakeMemory.stream()
                .filter(theme -> Objects.equals(theme.getId(), id))
                .findFirst()
                .orElseThrow(IllegalArgumentException::new);
    }

    @Override
    public List<Theme> findAllById(List<Long> themeIds) {
        return List.of();
    }

    @Override
    public Theme create(Theme theme) {
        Long timeId = id++;
        Theme createdTheme = theme.copyWithId(timeId);
        fakeMemory.add(createdTheme);
        return createdTheme;
    }

    @Override
    public void deleteById(Long idRequest) {
        fakeMemory.removeIf(theme -> Objects.equals(theme.getId(), idRequest));
    }

    @Override
    public boolean existsByName(Theme theme) {
        return false;
    }
}
