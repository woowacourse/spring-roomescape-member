package roomescape.service.stub;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;
import roomescape.domain.Theme;
import roomescape.repository.ThemeRepository;

public class StubThemeRepository implements ThemeRepository {
    private Optional<Theme> findResult;
    private boolean existsByIdResult;
    public long lastCalledDays;
    public LocalDate lastCalledDate;
    public long lastCalledLimit;

    public StubThemeRepository(Optional<Theme> findResult) {
        this.findResult = findResult;
    }

    public StubThemeRepository(boolean existsByIdResult) {
        this.existsByIdResult = existsByIdResult;
    }

    public StubThemeRepository() {
    }

    @Override
    public List<Theme> findAll() {
        return List.of();
    }

    @Override
    public List<Theme> findFamous(long days, LocalDate date, long limit) {
        lastCalledDays = days;
        lastCalledDate = date;
        lastCalledLimit = limit;
        return List.of();
    }

    @Override
    public Optional<Theme> findById(long themeId) {
        return findResult;
    }

    @Override
    public Theme save(Theme theme) {
        return Theme.of(1L, theme.getName(), theme.getDescription(), theme.getThumbnailUrl());
    }

    @Override
    public void deleteById(long themeId) {
    }

    @Override
    public boolean existsById(long themeId) {
        return existsByIdResult;
    }
}
