package roomescape.theme.repository;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.JdbcTest;
import org.springframework.context.annotation.Import;
import org.springframework.jdbc.core.JdbcTemplate;
import roomescape.theme.domain.Theme;

import java.sql.Date;
import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;

@JdbcTest
@Import(JdbcThemeRepository.class)
class JdbcThemeRepositoryTest {

    @Autowired
    JdbcTemplate jdbcTemplate;

    @Autowired
    JdbcThemeRepository repository;

    private Long setupTimeId;

    @BeforeEach
    void setUp() {
        jdbcTemplate.update("INSERT INTO reservation_time (start_at) VALUES (?)", "10:00");
        setupTimeId = jdbcTemplate.queryForObject(
                "SELECT id FROM reservation_time WHERE start_at = ?",
                Long.class,
                "10:00"
        );
    }

    @DisplayName("테마를 저장하면 생성된 ID가 부여된 객체가 반환된다.")
    @Test
    void saveTest() {
        // given
        Theme theme = Theme.create("테마", "설명", "https://example.com");

        // when
        Theme saved = repository.save(theme);

        // then
        assertThat(saved.getId()).isNotNull();
        assertThat(saved.getName()).isEqualTo("테마");
        assertThat(saved.getDescription()).isEqualTo("설명");
        assertThat(saved.getThumbnailUrl()).isEqualTo("https://example.com");
    }

    @DisplayName("ID로 테마를 삭제한다.")
    @Test
    void deleteByIdTest() {
        // given
        Theme saved = repository.save(Theme.create("테마", "설명", "https://example.com"));

        // when
        repository.deleteById(saved.getId());

        // then
        assertThat(repository.findById(saved.getId())).isEmpty();
    }

    @DisplayName("ID로 테마를 조회한다.")
    @Test
    void findByIdTest() {
        // given
        Theme saved = repository.save(Theme.create("테마", "설명", "https://example.com"));

        // when
        Optional<Theme> found = repository.findById(saved.getId());

        // then
        assertThat(found).isPresent();
        assertThat(found.get().getId()).isEqualTo(saved.getId());
        assertThat(found.get().getName()).isEqualTo("테마");
    }

    @DisplayName("존재하지 않는 ID로 조회하면 빈 Optional을 반환한다.")
    @Test
    void findByIdWhenNotExistsTest() {
        // when
        Optional<Theme> found = repository.findById(999L);

        // then
        assertThat(found).isEmpty();
    }

    @DisplayName("저장된 모든 테마들을 조회한다.")
    @Test
    void findAllTest() {
        // given
        repository.save(Theme.create("테마1", "설명1", "https://example1.com"));
        repository.save(Theme.create("테마2", "설명2", "https://example2.com"));

        // when
        List<Theme> themes = repository.findAll();

        // then
        assertThat(themes).hasSize(2);
        assertThat(themes)
                .extracting(Theme::getName)
                .containsExactlyInAnyOrder("테마1", "테마2");
    }

    @DisplayName("저장된 테마가 없으면 빈 리스트를 반환한다.")
    @Test
    void findAllWhenEmptyTest() {
        // when
        List<Theme> themes = repository.findAll();

        // then
        assertThat(themes).isEmpty();
    }

    @DisplayName("해당 이름의 테마가 존재하는지 조회한다.")
    @Test
    void existsByNameTest() {
        // given
        jdbcTemplate.update("insert into theme(id, name, description, thumbnail_url) values (1, '테마', '설명', 'url')");

        // when
        boolean exists = repository.existsByName("테마");
        boolean notExists = repository.existsByName("other");

        // then
        assertThat(exists).isTrue();
        assertThat(notExists).isFalse();
    }

    @Test
    @DisplayName("직전 period일 동안의 예약 수를 기준으로 상위 limit 개의 테마들을 조회한다.")
    void findPopularThemesTest() {
        // given
        Theme woowaTheme = repository.save(Theme.create("우테코", "우테코 전용 테마", "https://example.com"));
        Theme pairTheme = repository.save(Theme.create("페어", "페어 전용 테마", "https://pair.com"));
        Theme carrotTheme = repository.save(Theme.create("당근", "당근 전용 테마", "https://carrot.com"));

        LocalDate today = LocalDate.now();

        insertReservation("브라운", today.minusDays(1), woowaTheme.getId());
        insertReservation("포비", today.minusDays(2), woowaTheme.getId());
        insertReservation("제이슨", today.minusDays(3), woowaTheme.getId());

        insertReservation("이든", today.minusDays(1), pairTheme.getId());
        insertReservation("레아", today.minusDays(2), pairTheme.getId());

        insertReservation("웨지", today.minusDays(1), carrotTheme.getId());

        insertReservation("오늘예약", today, carrotTheme.getId());
        insertReservation("범위밖예약", today.minusDays(8), carrotTheme.getId());

        // when
        List<Theme> popularThemes = repository.findPopularThemes(7, 2);

        // then
        assertThat(popularThemes)
                .extracting(Theme::getName)
                .containsExactly("우테코", "페어");
    }

    @Test
    @DisplayName("예약이 없는 테마는 인기 테마 조회 결과에 포함되지 않는다.")
    void findPopularThemesExcludesThemesWithoutReservationsTest() {
        // given
        Theme themeWithReservation = repository.save(Theme.create("예약있음", "설명", "https://example.com"));
        repository.save(Theme.create("예약없음", "설명", "https://example.com"));

        insertReservation("브라운", LocalDate.now().minusDays(1), themeWithReservation.getId());

        // when
        List<Theme> popularThemes = repository.findPopularThemes(7, 10);

        // then
        assertThat(popularThemes)
                .extracting(Theme::getName)
                .containsExactly("예약있음");
    }

    private void insertReservation(String name, LocalDate date, Long themeId) {
        jdbcTemplate.update(
                "INSERT INTO reservation (name, reservation_date, time_id, theme_id) VALUES (?, ?, ?, ?)",
                name, Date.valueOf(date), setupTimeId, themeId
        );
    }
}
