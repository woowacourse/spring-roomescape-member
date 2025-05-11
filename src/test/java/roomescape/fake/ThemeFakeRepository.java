package roomescape.fake;

import roomescape.global.exception.EntityNotFoundException;
import roomescape.theme.entity.Theme;
import roomescape.theme.repository.ThemeRepository;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.atomic.AtomicLong;

public class ThemeFakeRepository implements ThemeRepository {

    private final Map<Long, Theme> themes = new HashMap<>();
    private final AtomicLong idGenerator = new AtomicLong(1);

    public ThemeFakeRepository() {
        long themeId = idGenerator.getAndIncrement();

        Theme defaultTheme = new Theme(
                themeId,
                "레벨2 탈출",
                "우테코 레벨2를 탈출하는 내용입니다.",
                "https://i.pinimg.com/236x/6e/bc/46/6ebc461a94a49f9ea3b8bbe2204145d4.jpg");
        themes.put(themeId, defaultTheme);
    }

    @Override
    public Theme findById(Long id) {
        return themes.get(id);
    }

    @Override
    public List<Theme> findAll() {
        return new ArrayList<>(themes.values());
    }

    @Override
    public Theme save(Theme theme) {
        long newId = idGenerator.getAndIncrement();
        Theme savedTheme = new Theme(newId, theme.getName(), theme.getDescription(), theme.getThumbnail());
        themes.put(newId, savedTheme);

        return savedTheme;
    }

    @Override
    public void deleteById(Long id) {
        if (!themes.containsKey(id)) {
            throw new EntityNotFoundException("테마 데이터를 찾을 수 없습니다: " + id);
        }

        themes.remove(id);
    }

    @Override
    public List<Theme> findPopularThemesThisWeek(LocalDate startInclusive, LocalDate endInclusive, int count) {
        return List.of();
    }
}
