package roomescape.reservation.infra;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.JdbcTest;
import org.springframework.context.annotation.Import;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.jdbc.Sql;
import org.springframework.transaction.annotation.Transactional;
import roomescape.reservation.domain.Theme;

import java.time.LocalDate;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;

@JdbcTest
@Transactional
@ActiveProfiles("test")
@Import(JdbcThemeRepository.class)
class JdbcThemeRepositoryTest {
    @Autowired
    private JdbcThemeRepository repository;

    @Test
    void 테마_저장_레포지토리_테스트() {
        Theme theme = new Theme(null, "무서운게 딱 좋아", "무서운 분위기의 방탈출", "https://example.com/theme.jpg");

        Theme savedTheme = repository.save(theme);

        assertThat(savedTheme.getName()).isEqualTo("무서운게 딱 좋아");
        assertThat(savedTheme.getDescription()).isEqualTo("무서운 분위기의 방탈출");
        assertThat(savedTheme.getThumbnailUrl()).isEqualTo("https://example.com/theme.jpg");
    }

    @Test
    void 테마_삭제_레포지토리_테스트() {
        // given
        Theme theme = new Theme(null, "무서운게 딱 좋아", "무서운 분위기의 방탈출", "https://example.com/theme.jpg");
        Theme savedTheme = repository.save(theme);

        // when
        repository.deleteById(savedTheme.getId());

        // then
        List<Theme> themes = repository.findAll();
        assertThat(themes).hasSize(4);
        assertThat(themes).extracting(Theme::getId)
                .doesNotContain(savedTheme.getId());
    }

    @Test
    void 각_날짜에_존재하는_모든_테마_조회_레포토지리_테스트() {
        List<Theme> themes = repository.findByDate(LocalDate.of(2026, 5, 5));

        assertThat(themes).hasSize(4);
        assertThat(themes).extracting(Theme::getId)
                .containsExactlyInAnyOrder(1L, 2L, 3L, 4L);
    }

    @Test
    void 최근_7일_예약_개수에_따른_인기_테마_조회_레포지토리_테스트() {
        // when
        List<Theme> themes = repository.findByDayAndLimit(7, 10);

        assertThat(themes).hasSize(4);
        assertThat(themes).extracting(Theme::getId)
                .containsExactly(2L, 1L, 3L, 4L);
    }

    @Test
    void 테마_전체_조회_레포지토리_테스트() {
        List<Theme> themes = repository.findAll();

        assertThat(themes).hasSize(4);
        assertThat(themes)
                .extracting(Theme::getId)
                .containsExactlyInAnyOrder(1L, 2L, 3L, 4L);
    }
}
