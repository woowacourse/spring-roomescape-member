package roomescape.infrastructure;

import java.util.List;
import java.util.Optional;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.JdbcTest;
import org.springframework.context.annotation.Import;
import roomescape.entity.Theme;

@JdbcTest
@Import(ThemeJdbcTemplateRepository.class)
class ThemeJdbcTemplateRepositoryTest {

    private static final String TEST_THEMA_NAME = "테스트 테마";
    private static final String TEST_THEMA_DESCRIPTION = "테스트 테마 설명";
    private static final String TEST_THEMA_THUMBNAIL = "https://good.com/thumb-nail";

    private final ThemeJdbcTemplateRepository themeRepository;

    @Autowired
    public ThemeJdbcTemplateRepositoryTest(ThemeJdbcTemplateRepository themeRepository) {
        this.themeRepository = themeRepository;
    }

    @Test
    @DisplayName("테마를 잘 저장한다")
    void save_success() {
        // given
        Theme theme = Theme.createWithNullId(TEST_THEMA_NAME, TEST_THEMA_DESCRIPTION, TEST_THEMA_THUMBNAIL);

        // when
        Theme savedTheme = themeRepository.save(theme);

        // then
        Assertions.assertNotNull(savedTheme.id());

        Theme expect = theme.appendId(savedTheme.id());
        Assertions.assertEquals(expect, savedTheme);
    }

    @Test
    @DisplayName("아이디를 기반으로 테마를 찾는다")
    void findById_success() {
        // given
        Theme theme = Theme.createWithNullId(TEST_THEMA_NAME, TEST_THEMA_DESCRIPTION, TEST_THEMA_THUMBNAIL);
        Theme savedTheme = themeRepository.save(theme);

        // when
        Optional<Theme> foundTheme = themeRepository.findById(savedTheme.id());

        // then
        Assertions.assertEquals(savedTheme.id(), foundTheme.get().id());
    }

    @Test
    @DisplayName("모든 테마를 가져온다")
    void findAll_success() {
        // given
        Theme theme1 = Theme.createWithNullId(TEST_THEMA_NAME, TEST_THEMA_DESCRIPTION, TEST_THEMA_THUMBNAIL);
        Theme theme2 = Theme.createWithNullId(TEST_THEMA_NAME, TEST_THEMA_DESCRIPTION, TEST_THEMA_THUMBNAIL);
        themeRepository.save(theme1);
        themeRepository.save(theme2);

        // when
        List<Theme> result = themeRepository.findAll();

        // then
        Assertions.assertEquals(2, result.size());
    }

    @Test
    @DisplayName("아이디를 기반으로 테마를 삭제한다")
    void deleteById_success() {
        // given
        Theme theme = Theme.createWithNullId(TEST_THEMA_NAME, TEST_THEMA_DESCRIPTION, TEST_THEMA_THUMBNAIL);
        Theme savedTheme = themeRepository.save(theme);

        // when
        themeRepository.deleteById(savedTheme.id());

        // then
        Optional<Theme> result = themeRepository.findById(savedTheme.id());
        Assertions.assertFalse(result.isPresent());
    }
}