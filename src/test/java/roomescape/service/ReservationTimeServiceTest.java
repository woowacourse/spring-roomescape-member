package roomescape.service;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.NullSource;
import org.junit.jupiter.params.provider.ValueSource;
import org.springframework.core.io.ClassPathResource;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.datasource.DriverManagerDataSource;
import org.springframework.jdbc.datasource.init.ResourceDatabasePopulator;
import roomescape.domain.ReservationTime;
import roomescape.repository.ReservationRepository;
import roomescape.repository.ReservationTimeRepository;

import java.time.LocalTime;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.junit.jupiter.api.Assertions.assertAll;

class ReservationTimeServiceTest {

    private ReservationTimeService reservationTimeService;
    private JdbcTemplate jdbcTemplate;

    @BeforeEach
    void setup() {
        DriverManagerDataSource dataSource = new DriverManagerDataSource();
        dataSource.setDriverClassName("org.h2.Driver");
        dataSource.setUrl("jdbc:h2:mem:reservation_time_service_test;DB_CLOSE_DELAY=-1");
        dataSource.setUsername("sa");
        dataSource.setPassword("");

        jdbcTemplate = new JdbcTemplate(dataSource);

        ResourceDatabasePopulator populator = new ResourceDatabasePopulator();
        populator.addScript(new ClassPathResource("schema.sql"));
        populator.execute(dataSource);

        jdbcTemplate.update("DELETE FROM reservation;");
        jdbcTemplate.update("DELETE FROM reservation_time;");

        ReservationTimeRepository reservationTimeRepository = new ReservationTimeRepository(jdbcTemplate);
        ReservationRepository reservationRepository = new ReservationRepository(jdbcTemplate);
        this.reservationTimeService = new ReservationTimeService(reservationTimeRepository, reservationRepository);
    }

    @Test
    void 시간_생성_테스트() {
        // when
        ReservationTime result = reservationTimeService.create(LocalTime.of(8, 0));

        // then
        assertAll(
                () -> assertThat(result.getId()).isNotNull(),
                () -> assertThat(result.getStartAt()).isEqualTo(LocalTime.of(8, 0))
        );
    }

    @Test
    void 전체_시간_조회_테스트() {
        // given
        reservationTimeService.create(LocalTime.of(8, 0));
        reservationTimeService.create(LocalTime.of(21, 0));

        // when
        List<ReservationTime> result = reservationTimeService.findAll();

        // then
        assertThat(result).hasSize(2);
    }

    @Test
    void 시간_삭제_테스트() {
        // given
        ReservationTime created = reservationTimeService.create(LocalTime.of(8, 0));

        // when
        reservationTimeService.delete(created.getId());

        // then
        assertThat(reservationTimeService.findAll()).isEmpty();
    }

    @ParameterizedTest
    @NullSource
    @ValueSource(longs = {0, -1})
    void 삭제하려는_id가_양수가_아니면_예외_발생(Long id) {
        // when & then
        assertThatThrownBy(() -> reservationTimeService.delete(id))
                .isInstanceOf(IllegalArgumentException.class)
                .hasMessage("[ERROR] id는 양수이어야 합니다.");
    }

    @Test
    void 예약이_존재하는_시간은_삭제시_예외_발생() {
        // given
        ReservationTime created = reservationTimeService.create(LocalTime.of(8, 0));
        jdbcTemplate.update(
                "INSERT INTO theme(name, description, thumbnail) VALUES (?, ?, ?)",
                "테스트테마",
                "테스트용 테마입니다.",
                "/썸네일");
        Long themeId = jdbcTemplate.queryForObject("SELECT id FROM theme WHERE name = ?", Long.class, "테스트테마");
        jdbcTemplate.update(
                "INSERT INTO reservation (name, date, time_id, theme_id) VALUES (?, ?, ?, ?)",
                "브라운",
                "2023-08-05",
                created.getId(),
                themeId);

        // when & then
        assertThatThrownBy(() -> reservationTimeService.delete(created.getId()))
                .isInstanceOf(IllegalArgumentException.class)
                .hasMessage("[ERROR] 예약이 존재하는 시간은 삭제할 수 없습니다.");
    }
}
