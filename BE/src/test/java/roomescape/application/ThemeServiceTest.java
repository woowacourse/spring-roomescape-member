package roomescape.application;

import java.time.LocalDate;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import roomescape.domain.Theme;
import roomescape.domain.ThemeRepository;
import roomescape.fake.FakeThemeRepository;

class ThemeServiceTest {

    private static final String TEST_THEMA_NAME = "테스트 테마";
    private static final String TEST_THEMA_DESCRIPTION = "테스트 테마 설명";
    private static final String TEST_THEMA_THUMBNAIL = "https://good.com/thumb-nail";

    private final ThemeRepository themeRepository = new FakeThemeRepository();
    private final ThemeService themeService = new ThemeService(themeRepository);

    @Test
    @DisplayName("저장 시도 시 오류가 발생하지 않는다")
    void save_success() {
        // when & then
        Assertions.assertDoesNotThrow(
                () -> themeService.save(TEST_THEMA_NAME, TEST_THEMA_DESCRIPTION, TEST_THEMA_THUMBNAIL)
        );
    }

    @Test
    @DisplayName("아이디 기반으로 조회 시 오류가 발생하지 않음.")
    void findById_success() {
        // given
        Theme saved = themeService.save(TEST_THEMA_NAME, TEST_THEMA_DESCRIPTION, TEST_THEMA_THUMBNAIL);

        // when & then
        Assertions.assertDoesNotThrow(
                () -> themeService.findById(saved.id())
        );
    }

    @Test
    @DisplayName("전부 다 잘 찾아오는 것 시도 시 오류 발생 안함.")
    void findAll_success() {
        // given
        Theme saved = themeService.save(TEST_THEMA_NAME, TEST_THEMA_DESCRIPTION, TEST_THEMA_THUMBNAIL);
        Theme saved2 = themeService.save(TEST_THEMA_NAME+"2", TEST_THEMA_DESCRIPTION, TEST_THEMA_THUMBNAIL);

        // when & then
        Assertions.assertDoesNotThrow(
                themeService::findAll
        );
    }

    @Test
    @DisplayName("아이디 기반 삭제 시도 시 오류 발생 안함")
    void deleteById_success() {
        // given
        Theme saved = themeService.save(TEST_THEMA_NAME, TEST_THEMA_DESCRIPTION, TEST_THEMA_THUMBNAIL);
        Long deleteTagetId = saved.id();

        //when & then
        Assertions.assertDoesNotThrow(
                () -> themeService.deleteById(deleteTagetId)
        );
    }

    @Test
    @DisplayName("정렬 기준으로 테마들을 조회 시 오류 발생 안함")
    void findTopNByPeriod_success() {
        // when & then
        Assertions.assertDoesNotThrow(
                () -> themeService.findTopNByPeriod(LocalDate.now().minusDays(1), LocalDate.now(), "POPULAR", 10L)
        );
    }
}