package roomescape.fixture;

import java.util.List;
import roomescape.theme.domain.Theme;
import roomescape.theme.domain.ThemeRepository;

public class ThemeFixture {

    public static final Theme NOT_SAVED_THEME_1 = new Theme("테마1", "테마1 설명", "테마1 썸네일");
    public static final Theme NOT_SAVED_THEME_2 = new Theme("테마2", "테마2 설명", "테마2 썸네일");
    public static final Theme NOT_SAVED_THEME_3 = new Theme("테마3", "테마3 설명", "테마3 썸네일");
    public static final Theme NOT_SAVED_THEME_4 = new Theme("테마4", "테마4 설명", "테마4 썸네일");
    public static final Theme NOT_SAVED_THEME_5 = new Theme("테마5", "테마5 설명", "테마5 썸네일");
    public static final Theme NOT_SAVED_THEME_6 = new Theme("테마6", "테마6 설명", "테마6 썸네일");
    public static final Theme NOT_SAVED_THEME_7 = new Theme("테마7", "테마7 설명", "테마7 썸네일");
    public static final Theme NOT_SAVED_THEME_8 = new Theme("테마8", "테마8 설명", "테마8 썸네일");
    public static final Theme NOT_SAVED_THEME_9 = new Theme("테마9", "테마9 설명", "테마9 썸네일");
    public static final Theme NOT_SAVED_THEME_10 = new Theme("테마10", "테마10 설명", "테마10 썸네일");
    public static final Theme NOT_SAVED_THEME_11 = new Theme("테마11", "테마11 설명", "테마11 썸네일");
    public static final Theme NOT_SAVED_THEME_12 = new Theme("테마12", "테마12 설명", "테마12 썸네일");

    public static Theme getSavedTheme1(final ThemeRepository themeRepository) {
        final Long id = themeRepository.save(NOT_SAVED_THEME_1);
        return themeRepository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("테마를 찾을 수 없습니다."));
    }

    public static Theme getSavedTheme2(final ThemeRepository themeRepository) {
        final Long id = themeRepository.save(NOT_SAVED_THEME_2);
        return themeRepository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("테마를 찾을 수 없습니다."));
    }

    public static List<Theme> getAllSavedThemes(final ThemeRepository themeRepository) {
        themeRepository.save(NOT_SAVED_THEME_1);
        themeRepository.save(NOT_SAVED_THEME_2);
        themeRepository.save(NOT_SAVED_THEME_3);
        themeRepository.save(NOT_SAVED_THEME_4);
        themeRepository.save(NOT_SAVED_THEME_5);
        themeRepository.save(NOT_SAVED_THEME_6);
        themeRepository.save(NOT_SAVED_THEME_7);
        themeRepository.save(NOT_SAVED_THEME_8);
        themeRepository.save(NOT_SAVED_THEME_9);
        themeRepository.save(NOT_SAVED_THEME_10);
        themeRepository.save(NOT_SAVED_THEME_11);
        themeRepository.save(NOT_SAVED_THEME_12);

        return themeRepository.findAll();
    }
}
