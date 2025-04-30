package roomescape.fake;

import roomescape.domain.Theme;
import roomescape.domain.ThemeRepository;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

public class FakeThemeRepository implements ThemeRepository {

    private List<Theme> themes = new ArrayList<>();
    private Long id = 0L;

    public FakeThemeRepository(Theme... themes) {
        this((long) themes.length, new ArrayList<>(List.of(themes)));
    }

    private FakeThemeRepository(Long id, List<Theme> themes) {
        this.id = id;
        this.themes = themes;
    }

    @Override
    public List<Theme> findAll() {
        return List.copyOf(themes);
    }

    @Override
    public Long create(Theme theme) {
        themes.add(new Theme(++id, theme.getName(), theme.getDescription(), theme.getThumbnail()));
        return id;
    }

    @Override
    public void deleteById(Long themeId) {
        themes = themes.stream()
                .filter(theme -> theme.getId() != themeId)
                .collect(Collectors.toList());
    }

    @Override
    public List<Theme> findRankByDate(final LocalDate startDate, final LocalDate endDate, final int limit) {
        return List.copyOf(themes); //TODO: 테스트 구현 고민하기
    }

    @Override
    public Optional<Theme> findById(Long themeId) {
        return themes.stream()
                .filter(theme -> theme.getId() == themeId)
                .findFirst();
    }
}
