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
import roomescape.dao.ReservationRepository;
import roomescape.dao.ReservationTimeRepository;
import roomescape.dao.ThemeRepository;
import roomescape.domain.Reservation;
import roomescape.service.dto.TimeAvailabilityDto;

import java.time.LocalDate;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.junit.jupiter.api.Assertions.assertAll;

class ReservationServiceTest {

    private ReservationService reservationService;

    private LocalDate date = LocalDate.parse("2023-08-05");

    @BeforeEach
    void setup() {
        DriverManagerDataSource dataSource = new DriverManagerDataSource();
        dataSource.setDriverClassName("org.h2.Driver");
        dataSource.setUrl("jdbc:h2:mem:reservation_service_test;DB_CLOSE_DELAY=-1");
        dataSource.setUsername("sa");
        dataSource.setPassword("");

        JdbcTemplate jdbcTemplate = new JdbcTemplate(dataSource);
        jdbcTemplate.execute("DROP TABLE IF EXISTS reservation");

        ResourceDatabasePopulator populator = new ResourceDatabasePopulator();
        populator.addScript(new ClassPathResource("schema.sql"));
        populator.addScript(new ClassPathResource("data.sql"));
        populator.execute(dataSource);
        jdbcTemplate.update("DELETE FROM reservation;");
        jdbcTemplate.update("ALTER TABLE reservation ALTER COLUMN id RESTART WITH 1;");


        ReservationRepository reservationRepository = new ReservationRepository(jdbcTemplate);
        ReservationTimeRepository reservationTimeRepository = new ReservationTimeRepository(jdbcTemplate);
        ThemeRepository themeRepository = new ThemeRepository(jdbcTemplate);
        this.reservationService = new ReservationService(reservationRepository, reservationTimeRepository, themeRepository);
    }

    @Test
    void 예약_생성_테스트() {
        // when
        Reservation result = reservationService.create("브라운", date, 1L, 1L);

        // then
        assertAll(
                () -> assertThat(result.getId()).isNotNull(),
                () -> assertThat(result.getName()).isEqualTo("브라운"),
                () -> assertThat(result.getDate()).isEqualTo(date)
        );
    }

    @Test
    void 전체_예약_조회_테스트() {
        // given
        reservationService.create("브라운", date, 1L, 1L);
        reservationService.create("구구", date, 2L, 1L);

        // when
        List<Reservation> result = reservationService.findAll();

        // then
        assertThat(result).hasSize(2);
    }

    @Test
    void 예약_삭제_테스트() {
        // given
        Reservation created = reservationService.create("브라운", date, 1L, 1L);

        // when
        reservationService.delete(created.getId());

        // then
        assertThat(reservationService.findAll()).isEmpty();
    }

    @ParameterizedTest
    @NullSource
    @ValueSource(longs = {0, -1})
    void 삭제하려는_id가_양수가_아니면_예외_발생(Long id) {
        // when & then
        assertThatThrownBy(() -> reservationService.delete(id))
                .isInstanceOf(IllegalArgumentException.class)
                .hasMessage("[ERROR] id는 양수이어야 합니다.");
    }

    @Test
    void 존재하지_않는_timeId로_예약_생성시_예외_발생() {
        // when & then
        assertThatThrownBy(() -> reservationService.create("홍길동", date, 999L, 1L))
                .isInstanceOf(IllegalArgumentException.class)
                .hasMessage("[ERROR] 존재하지 않는 예약 시간입니다.");
    }

    @Test
    void 존재하지_않는_themeId로_예약_생성시_예외_발생() {
        // when & then
        assertThatThrownBy(() -> reservationService.create("홍길동", date, 1L, 999L))
                .isInstanceOf(IllegalArgumentException.class)
                .hasMessage("[ERROR] 존재하지 않는 테마입니다.");
    }

    @Test
    void 예약_가능한_시간_조회_테스트() {
        // given
        reservationService.create("브라운", date, 1L, 1L);

        // when
        List<TimeAvailabilityDto> result = reservationService.findAvailableTime(1L, date);

        // then
        assertThat(result).extracting(TimeAvailabilityDto::available)
                .containsOnlyOnce(false);
    }
}
