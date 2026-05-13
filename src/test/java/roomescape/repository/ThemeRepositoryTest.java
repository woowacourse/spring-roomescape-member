package roomescape.repository;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatCode;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataIntegrityViolationException;
import roomescape.domain.Theme;
import roomescape.domain.fixture.ThemeFixture;
import roomescape.service.BaseIntegrationTest;

class ThemeRepositoryTest extends BaseIntegrationTest {
    @Autowired
    private ThemeRepository themeRepository;

    @Autowired
    private ThemeDataSource dataSource;

    @BeforeEach
    void setUp() {
        dataSource.clearId();
        dataSource.clearTable();
    }

    @Test
    void 테마를_저장하고_ID로_조회할_수_있다() {
        // given
        Theme theme = ThemeFixture.createDefaultTheme();

        // when
        Theme saved = themeRepository.save(theme);

        // then
        assertThat(themeRepository.findById(saved.getId())).isPresent();
    }

    @Test
    void 활성화된_테마_조회가_있는지_확인한다() {
        // given
        Theme theme = ThemeFixture.createDefaultTheme();
        themeRepository.save(theme);

        // when
        boolean result = themeRepository.isActiveByName(theme.getName());

        // then
        assertThat(result).isTrue();
    }

    @Test
    void 활성화된_테마가_있을_때_같은_테마를_추가하면_제약_위반_예외가_발생한다() {
        // given
        Theme theme = ThemeFixture.createDefaultTheme();
        themeRepository.save(theme);

        // when & then
        assertThatThrownBy(() -> themeRepository.save(theme))
                .isInstanceOf(DataIntegrityViolationException.class);
    }

    @Test
    void 비활성화된_같은_테마는_여러개_존재_가능하다() {
        // given
        Theme first = ThemeFixture.createInactiveTheme("바니의 집", "바니의 집입니다", "http://image.png/image.com");
        Theme second = ThemeFixture.createInactiveTheme("바니의 집", "바니의 집입니다", "http://image.png/image.com");

        // when & then
        assertThatCode(() -> {
            themeRepository.save(first);
            themeRepository.save(second);
        }).doesNotThrowAnyException();
    }

    @Test
    void 테마_정보를_수정한다() {
        // given
        Theme theme = ThemeFixture.createDefaultTheme();
        Theme savedTheme = themeRepository.save(theme);
        savedTheme.deactivate();

        // when
        themeRepository.update(savedTheme);

        // then
        Optional<Theme> found = themeRepository.findById(savedTheme.getId());
        assertThat(found).isPresent();
        assertThat(found.get().isActive()).isFalse();
    }

    @Test
    void 활성화된_테마만_페이징_조건에_맞게_조회한다() {
        // given
        Theme first = themeRepository.save(
                ThemeFixture.createTheme("바니의 집", "바니의 집입니다", "http://image.png/image.com"));
        Theme second = themeRepository.save(
                ThemeFixture.createTheme("이프의 집", "이프의 집입니다", "http://image.png/image.com"));
        Theme inactive = themeRepository.save(
                ThemeFixture.createTheme("아루의 집", "아루의 집입니다", "http://image.png/image.com"));
        inactive.deactivate();
        themeRepository.update(inactive);

        // when
        List<Theme> themes = themeRepository.findAllActiveThemesByPaging(1, 1);

        // then
        assertThat(themes).hasSize(1).extracting(Theme::getId)
                .containsExactly(second.getId());

        assertThat(themes).extracting(Theme::getId)
                .doesNotContain(inactive.getId(), first.getId());
    }

    @Test
    void 예약이_많은_순서대로_상위_10개의_테마를_조회한다() {
        // given
        dataSource.insertThemesByCount(15);
        dataSource.insertTimeByStartToEndWithOneHourRotation(10, 18);

        Long mostReservedThemeId = 2L;
        Long secondReservedThemeId = 1L;
        Long thirdReservedThemeId = 3L;
        dataSource.insertReservedReservationByTheme(mostReservedThemeId, 3);
        dataSource.insertReservedReservationByTheme(secondReservedThemeId, 2);
        dataSource.insertReservedReservationByTheme(thirdReservedThemeId, 1);

        // when
        LocalDate now = LocalDate.now();
        List<Theme> popularThemes = themeRepository.findTopThemesByReservationCount(now.minusDays(7), now.plusDays(1),
                10);

        // then
        assertThat(popularThemes).hasSize(10);
        assertThat(popularThemes.get(0).getId()).isEqualTo(mostReservedThemeId);
        assertThat(popularThemes.get(1).getId()).isEqualTo(secondReservedThemeId);
        assertThat(popularThemes.get(2).getId()).isEqualTo(thirdReservedThemeId);
    }

    @Test
    void 예약_수가_같은_인기_테마는_ID_오름차순으로_조회한다() {
        // given
        dataSource.insertThemesByCount(3);
        dataSource.insertTimeByStartToEndWithOneHourRotation(10, 12);
        dataSource.insertReservedReservation("바니", LocalDate.now(), 2L, 1L);
        dataSource.insertReservedReservation("이프", LocalDate.now(), 1L, 2L);

        // when
        LocalDate now = LocalDate.now();
        List<Theme> popularThemes = themeRepository.findTopThemesByReservationCount(now.minusDays(1), now.plusDays(1),
                3);

        // then
        assertThat(popularThemes)
                .extracting(Theme::getId)
                .containsExactly(1L, 2L, 3L);
    }
}
