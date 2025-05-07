package roomescape.repository;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.assertj.core.api.SoftAssertions.assertSoftly;

import java.time.LocalDate;
import java.time.LocalTime;
import java.util.List;
import java.util.Optional;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.jdbc.datasource.embedded.EmbeddedDatabase;
import org.springframework.jdbc.datasource.embedded.EmbeddedDatabaseBuilder;
import org.springframework.jdbc.datasource.embedded.EmbeddedDatabaseType;
import roomescape.domain.Reservation;
import roomescape.domain.ReservationTime;
import roomescape.domain.Theme;
import roomescape.dto.PopularThemeResponse;
import roomescape.error.NotFoundException;

class JdbcThemeRepositoryTest {

    private EmbeddedDatabase db;
    private JdbcThemeRepository repository;
    private JdbcReservationRepository jdbcReservationRepository;

    @BeforeEach
    void setUp() {
        db = new EmbeddedDatabaseBuilder()
                .setType(EmbeddedDatabaseType.H2)
                .addScript("classpath:schema.sql")
                .addScript("classpath:data.sql")
                .build();
        repository = new JdbcThemeRepository(db);
        jdbcReservationRepository = new JdbcReservationRepository(db);
    }

    @AfterEach
    void tearDown() {
        db.shutdown();
    }

    @Test
    void 테마를_올바르게_저장한다() {
        // given
        Theme theme = new Theme("테마이름", "테마설명", "테마썸네일");

        // when
        Theme savedTheme = repository.save(theme);

        // then
        assertSoftly(soft -> {
            soft.assertThat(savedTheme.getId()).isNotNull();
            soft.assertThat(savedTheme.getName()).isEqualTo("테마이름");
            soft.assertThat(savedTheme.getDescription()).isEqualTo("테마설명");
            soft.assertThat(savedTheme.getThumbnail()).isEqualTo("테마썸네일");
        });
    }

    @Test
    void 모든_테마를_조회한다() {
        // when
        List<Theme> themes = repository.findAll();

        // then
        assertThat(themes).hasSize(2);
    }

    @Test
    void 인기있는_테마_10개를_조회한다() {
        // given
        jdbcReservationRepository.save(
                new Reservation("예약1", LocalDate.now().minusDays(3), new ReservationTime(1L, LocalTime.of(10, 0)),
                        new Theme(1L, "이름1", "썸네일1", "설명1")));
        jdbcReservationRepository.save(
                new Reservation("예약2", LocalDate.now().minusDays(3), new ReservationTime(2L, LocalTime.of(11, 0)),
                        new Theme(1L, "이름1", "썸네일1", "설명1")));
        jdbcReservationRepository.save(
                new Reservation("예약3", LocalDate.now().minusDays(3), new ReservationTime(1L, LocalTime.of(10, 0)),
                        new Theme(2L, "이름2", "썸네일2", "설명2")));

        // when
        List<PopularThemeResponse> allPopular = repository.findAllPopular();

        // then
        assertSoftly(soft -> {
                    soft.assertThat(allPopular).hasSize(2);
                    soft.assertThat(allPopular.get(0).name()).isEqualTo("이름1");
                    soft.assertThat(allPopular.get(1).name()).isEqualTo("이름2");
                }
        );
    }

    @Test
    void id에_알맞은_테마을_삭제한다() {
        // given
        Long id = 1L;

        // when
        repository.deleteById(id);
        List<Theme> themes = repository.findAll();

        // then
        assertThat(themes).hasSize(1)
                .extracting(Theme::getId)
                .doesNotContain(1L);
    }

    @Test
    void 존재하지_않는_테마를_삭제할_때_예외_처리() {
        // then
        assertThatThrownBy(() -> repository.deleteById(999L))
                .isInstanceOf(NotFoundException.class)
                .hasMessage("삭제할 테마가 없습니다. id=999");
    }

    @Test
    void id에_알맞은_테마을_가져온다() {
        // given
        Long id = 1L;

        // when
        Theme theme = repository.findById(id).get();

        // then
        assertThat(theme.getId()).isEqualTo(id);
    }

    @Test
    void 존재하지_않는_id면_빈_Optional을_반환한다() {
        // given
        Long invalidId = 999L;

        // when
        final Optional<Theme> optionalTheme = repository.findById(invalidId);

        // then
        assertThat(optionalTheme).isEmpty();
    }
}
