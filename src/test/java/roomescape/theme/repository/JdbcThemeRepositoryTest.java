package roomescape.theme.repository;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.JdbcTest;
import org.springframework.jdbc.core.JdbcTemplate;
import roomescape.theme.entity.Theme;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.SoftAssertions.assertSoftly;

@JdbcTest
class JdbcThemeRepositoryTest {
    @Autowired
    private JdbcTemplate jdbcTemplate;
    private ThemeRepository themeRepository;
    private final Long memberId = 1L;

    @BeforeEach
    void setup() {
        themeRepository = new JdbcThemeRepository(jdbcTemplate);
        jdbcTemplate.update("INSERT INTO member (id, name, email, password, role) VALUES ( ?, ?, ?, ?, ? )", memberId, "test", "test@example.com", "password", "USER");
    }

    @DisplayName("생성 테스트")
    @Test
    void create() {
        // given
        Theme entity = new Theme(1L, "test", "hello", "hi");

        // when
        themeRepository.save(entity);
        List<Theme> actual = themeRepository.findAll();

        // then
        assertThat(actual).hasSize(1);
    }

    @DisplayName("삭제 테스트")
    @Test
    void delete() {
        // given
        jdbcTemplate.update("INSERT INTO theme (id, name, description, thumbnail) VALUES ( ?, ?, ?, ? )", 1, "test", "hello", "hi");

        // when
        final boolean deleted = themeRepository.deleteById(1L);
        List<Theme> all = themeRepository.findAll();

        // then
        assertSoftly(softly -> {
            softly.assertThat(deleted).isTrue();
            softly.assertThat(all).isEmpty();
        });
    }

    @DisplayName("테마명으로 조회할 수 있다.")
    @Test
    void findByName() {
        // given
        jdbcTemplate.update("INSERT INTO theme (id, name, description, thumbnail) VALUES ( ?, ?, ?, ? )", 1, "test", "hello", "hi");
        jdbcTemplate.update("INSERT INTO theme (id, name, description, thumbnail) VALUES ( ?, ?, ?, ? )", 2, "test2", "hello", "hi");

        // when
        Optional<Theme> actual = themeRepository.findByName("test");

        // then
        assertSoftly(softly -> {
            softly.assertThat(actual).isNotEmpty();
            softly.assertThat(actual.get().getId()).isEqualTo(1L);
        });
    }

    @DisplayName("주어진 기간 내에 인기 순 테마를 원하는 만큼 조회할 수 있다")
    @Test
    void findPopularThemesByDateRangeAndLimit() {
        // given
        // theme
        jdbcTemplate.update("INSERT INTO theme (id, name, description, thumbnail) VALUES ( ?, ?, ?, ? )", 1, "test", "hello", "hi");
        jdbcTemplate.update("INSERT INTO theme (id, name, description, thumbnail) VALUES ( ?, ?, ?, ? )", 2, "test2", "hello", "hi");
        jdbcTemplate.update("INSERT INTO theme (id, name, description, thumbnail) VALUES ( ?, ?, ?, ? )", 3, "test3", "hello", "hi");
        // time
        jdbcTemplate.update("INSERT INTO reservation_time (id, start_at) VALUES ( ?, ? )", 1, "10:00");
        // reservation
        jdbcTemplate.update("INSERT INTO reservation (id, member_id, date, time_id, theme_id) VALUES ( ?, ?, ?, ?, ? )", 1, memberId, "2025-01-01", 1, 1);
        jdbcTemplate.update("INSERT INTO reservation (id, member_id, date, time_id, theme_id) VALUES ( ?, ?, ?, ?, ? )", 2, memberId, "2025-01-02", 1, 1);
        jdbcTemplate.update("INSERT INTO reservation (id, member_id, date, time_id, theme_id) VALUES ( ?, ?, ?, ?, ? )", 3, memberId, "2025-01-03", 1, 2);
        jdbcTemplate.update("INSERT INTO reservation (id, member_id, date, time_id, theme_id) VALUES ( ?, ?, ?, ?, ? )", 4, memberId, "2025-01-04", 1, 1);

        List<Theme> expected = List.of(
                new Theme(1L, "test", "hello", "hi"),
                new Theme(2L, "test2", "hello", "hi")
        );

        // when
        List<Theme> actual = themeRepository.findPopularThemesByDateRangeAndLimit(LocalDate.of(2025, 1, 1), LocalDate.of(2025, 1, 4), 2);

        // then
        assertSoftly(softly -> {
            softly.assertThat(actual).hasSize(expected.size());
            softly.assertThat(actual.getFirst().getId()).isEqualTo(expected.getFirst().getId());
            softly.assertThat(actual.get(1).getId()).isEqualTo(expected.get(1).getId());
        });
    }
}
