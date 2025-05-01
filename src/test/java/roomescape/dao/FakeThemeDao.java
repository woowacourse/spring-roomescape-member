package roomescape.dao;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.concurrent.atomic.AtomicLong;
import org.springframework.jdbc.core.JdbcTemplate;
import roomescape.model.Theme;

public class FakeThemeDao extends ThemeJdbcDao {
    public FakeThemeDao(JdbcTemplate jdbcTemplate) {
        super(jdbcTemplate);
    }
    private final List<Theme> themes = new ArrayList<>();
    private final AtomicLong nextId = new AtomicLong(1L);

    @Override
    public List<Theme> findAll() {
        return new ArrayList<>(themes);
    }

    @Override
    public Long saveTheme(Theme theme) {
        Theme newTheme = new Theme(nextId.getAndIncrement(), theme.getName(), theme.getDescription(), theme.getThumbnail());
        themes.add(newTheme);
        return newTheme.getId();
    }

    @Override
    public Optional<Theme> findById(Long id) {
        return themes.stream()
                .filter(theme -> theme.getId().equals(id))
                .findFirst();
    }
}
