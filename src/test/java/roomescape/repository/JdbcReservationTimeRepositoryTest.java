package roomescape.repository;

import org.assertj.core.groups.Tuple;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.datasource.DriverManagerDataSource;
import roomescape.domain.Reservation;
import roomescape.domain.ReservationTime;
import roomescape.domain.ReservationTimeAvailability;
import roomescape.domain.Theme;

import java.time.LocalDate;
import java.time.LocalTime;
import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;

class JdbcReservationTimeRepositoryTest {

    private ReservationTimeRepository reservationTimeRepository;
    private ReservationRepository reservationRepository;
    private ThemeRepository themeRepository;

    @BeforeEach
    void setUp() {
        DriverManagerDataSource dataSource = new DriverManagerDataSource();
        dataSource.setDriverClassName("org.h2.Driver");
        dataSource.setUrl("jdbc:h2:mem:test;MODE=MySQL;DB_CLOSE_DELAY=-1");
        dataSource.setUsername("test");
        dataSource.setPassword("");

        JdbcTemplate jdbcTemplate = new JdbcTemplate(dataSource);

        jdbcTemplate.execute("DROP TABLE IF EXISTS reservation");
        jdbcTemplate.execute("DROP TABLE IF EXISTS reservation_time");
        jdbcTemplate.execute("DROP TABLE IF EXISTS theme");

        jdbcTemplate.execute("""
                CREATE TABLE theme (
                    id          BIGINT       NOT NULL AUTO_INCREMENT,
                    name        VARCHAR(255) NOT NULL,
                    description VARCHAR(255) NOT NULL,
                    thumbnail   VARCHAR(255) NOT NULL,
                    PRIMARY KEY (id)
                )
                """);

        jdbcTemplate.execute("""
                CREATE TABLE reservation_time (
                    id       BIGINT NOT NULL AUTO_INCREMENT,
                    start_at TIME   NOT NULL,
                    PRIMARY KEY (id),
                    UNIQUE (start_at)
                )
                """);

        jdbcTemplate.execute("""
                CREATE TABLE reservation (
                    id       BIGINT       NOT NULL AUTO_INCREMENT,
                    name     VARCHAR(255) NOT NULL,
                    date     DATE         NOT NULL,
                    time_id  BIGINT       NOT NULL,
                    theme_id BIGINT       NOT NULL,
                    PRIMARY KEY (id),
                    UNIQUE (date, time_id, theme_id),
                    FOREIGN KEY (time_id) REFERENCES reservation_time (id),
                    FOREIGN KEY (theme_id) REFERENCES theme (id)
                )
                """);

        reservationTimeRepository = new JdbcReservationTimeRepository(jdbcTemplate);
        reservationRepository = new JdbcReservationRepository(jdbcTemplate);
        themeRepository = new JdbcThemeRepository(jdbcTemplate);
    }


    @Test
    void 예약_시간을_저장하고_조회한다() {
        ReservationTime reservationTime = reservationTimeRepository.save(new ReservationTime(LocalTime.of(10, 0)));

        Optional<ReservationTime> found = reservationTimeRepository.findById(reservationTime.getId());

        assertThat(found).isPresent();
        ReservationTime time = found.get();
        assertThat(time.getId()).isEqualTo(reservationTime.getId());
        assertThat(time.getStartAt()).isEqualTo(LocalTime.of(10, 0));
    }

    @Test
    void 예약_시간_목록을_조회한다() {
        reservationTimeRepository.save(new ReservationTime(LocalTime.of(10, 0)));

        List<ReservationTime> reservationTimes = reservationTimeRepository.findAll();

        assertThat(reservationTimes).hasSize(1);
        assertThat(reservationTimes.getFirst().getStartAt()).isEqualTo(LocalTime.of(10, 0));
    }

    @Test
    void 예약_시간_존재_여부를_조회한다() {
        reservationTimeRepository.save(new ReservationTime(LocalTime.of(10, 0)));

        boolean exists = reservationTimeRepository.existsByStartAt(LocalTime.of(10, 0));

        assertThat(exists).isTrue();
    }

    @Test
    void 예약_시간을_삭제한다() {
        ReservationTime reservationTime = reservationTimeRepository.save(new ReservationTime(LocalTime.of(10, 0)));

        reservationTimeRepository.deleteById(reservationTime.getId());

        assertThat(reservationTimeRepository.findAll()).isEmpty();
    }

    @Test
    @DisplayName("이용 가능한 시간을 조회한다.")
    public void findAvailableTimes() {
        // given
        ReservationTime time = reservationTimeRepository.save(new ReservationTime(LocalTime.of(10, 0)));
        ReservationTime time2 = reservationTimeRepository.save(new ReservationTime(LocalTime.of(12, 0)));
        Theme targetTheme = themeRepository.save(new Theme("레벨2 탈출", "우테코 레벨2를 탈출하는 내용입니다.", "https://example.com/theme.png"));
        Theme nonTargetTheme = themeRepository.save(new Theme("레벨3 탈출", "우테코 레벨3을 탈출하는 내용입니다.", "https://example.com/theme.png"));

        LocalDate targetDate = LocalDate.of(2023, 8, 5);
        reservationRepository.save(new Reservation("브라운", targetDate, time, targetTheme));
        reservationRepository.save(new Reservation("브라운", LocalDate.of(2024, 9, 10), time, targetTheme));
        reservationRepository.save(new Reservation("브라운", targetDate, time, nonTargetTheme));

        // when
        List<ReservationTimeAvailability> availableTimes = reservationTimeRepository.findAvailableTimes(targetDate, targetTheme.getId());

        // then
        assertThat(availableTimes).hasSize(2)
                .extracting(ReservationTimeAvailability::getReservationTime,
                        ReservationTimeAvailability::isAvailable)
                .containsExactly(Tuple.tuple(time, false), Tuple.tuple(time2, true));
    }
}
