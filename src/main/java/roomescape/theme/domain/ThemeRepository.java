package roomescape.theme.domain;


public interface ThemeRepository {
    Theme save(Theme theme);
    void delete(Long id);
    boolean existsThemeById(Long id);
}
