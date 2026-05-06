package roomescape.service;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.NullSource;
import org.junit.jupiter.params.provider.ValueSource;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.JdbcTest;
import org.springframework.jdbc.core.JdbcTemplate;
import roomescape.repository.ThemeRepository;
import roomescape.domain.Theme;

import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.junit.jupiter.api.Assertions.assertAll;

@JdbcTest
class ThemeServiceTest {

    @Autowired
    private JdbcTemplate jdbcTemplate;
    private ThemeService themeService;

    @BeforeEach
    void setup() {
        this.themeService = new ThemeService(new ThemeRepository(jdbcTemplate));
        jdbcTemplate.update("DELETE FROM reservation");
        jdbcTemplate.update("DELETE FROM theme");
    }

    @Test
    void 테마_생성_테스트() {
        // when
        Theme result = themeService.create("테스트테마", "테스트용 테마입니다.", "/썸네일");

        // then
        assertAll(
                () -> assertThat(result.getId()).isNotNull(),
                () -> assertThat(result.getName()).isEqualTo("테스트테마")
        );
    }

    @Test
    void 전체_테마_조회_테스트() {
        // given
        themeService.create("테스트테마1", "테스트용 테마1입니다.", "/썸네일1");
        themeService.create("테스트테마2", "테스트용 테마2입니다.", "/썸네일2");

        // when
        List<Theme> result = themeService.findAll();

        // then
        assertThat(result).hasSize(2);
    }

    @Test
    void 테마_삭제_테스트() {
        // given
        Theme created = themeService.create("테스트테마", "테스트용 테마입니다.", "/썸네일");

        // when
        themeService.delete(created.getId());

        // then
        assertThat(themeService.findAll()).isEmpty();
    }

    @ParameterizedTest
    @NullSource
    @ValueSource(longs = {0, -1})
    void 삭제하려는_id가_양수가_아니면_예외_발생(Long id) {
        // when & then
        assertThatThrownBy(() -> themeService.delete(id))
                .isInstanceOf(IllegalArgumentException.class)
                .hasMessage("[ERROR] id는 양수이어야 합니다.");
    }
}
