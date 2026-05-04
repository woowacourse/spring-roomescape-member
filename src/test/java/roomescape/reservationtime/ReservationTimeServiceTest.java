package roomescape.reservationtime;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatCode;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

import java.io.IOException;
import java.io.InputStream;
import java.time.LocalDate;
import java.time.LocalTime;
import java.util.Properties;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.datasource.DriverManagerDataSource;
import roomescape.reservation.ReservationDao;
import roomescape.reservation.ReservationRepository;

class ReservationTimeServiceTest {
    private static final String TEST_PROPERTIES = "application-test.properties";

    private ReservationTimeService reservationTimeService;
    private JdbcTemplate jdbcTemplate;

    @BeforeEach
    void setUp() {
        Properties properties = loadTestProperties();

        DriverManagerDataSource dataSource = new DriverManagerDataSource();
        dataSource.setDriverClassName(properties.getProperty("spring.datasource.driver-class-name"));
        dataSource.setUrl(properties.getProperty("spring.datasource.url"));
        dataSource.setUsername(properties.getProperty("spring.datasource.username"));
        dataSource.setPassword(properties.getProperty("spring.datasource.password"));

        jdbcTemplate = new JdbcTemplate(dataSource);
        ReservationTimeDao reservationTimeDao = new ReservationTimeDao(jdbcTemplate);
        ReservationTimeRepository reservationTimeRepository = new ReservationTimeRepository(reservationTimeDao);
        ReservationDao reservationDao = new ReservationDao(jdbcTemplate);
        ReservationRepository reservationRepository = new ReservationRepository(reservationDao);
        reservationTimeService = new ReservationTimeService(reservationTimeRepository, reservationRepository);

        jdbcTemplate.execute("RUNSCRIPT FROM 'classpath:reset-test.sql'");
    }

    private Properties loadTestProperties() {
        Properties properties = new Properties();
        try (InputStream inputStream = getClass().getClassLoader().getResourceAsStream(TEST_PROPERTIES)) {
            if (inputStream == null) {
                throw new IllegalStateException("Test properties not found: " + TEST_PROPERTIES);
            }
            properties.load(inputStream);
            return properties;
        } catch (IOException e) {
            throw new IllegalStateException("Failed to load test properties: " + TEST_PROPERTIES, e);
        }
    }

    @Test
    void 예약_시간을_등록할_수_있다() {
        reservationTimeService.createReservationTime(LocalTime.of(10, 30));

        assertThat(reservationTimeService.findReservationTimes())
                .extracting(ReservationTime::startAt)
                .containsExactly(LocalTime.of(10, 30));
    }

    @Test
    void 예약_시간이_중복되면_예외가_발생한다() {
        reservationTimeService.createReservationTime(LocalTime.of(16, 0));

        assertThatThrownBy(() -> reservationTimeService.createReservationTime(LocalTime.of(16, 0)))
                .isInstanceOf(DuplicateReservationTimeException.class)
                .extracting(Throwable::getMessage)
                .isEqualTo("이미 존재하는 예약 시간입니다");
    }

    @Test
    void 전체_예약_시간을_조회할_수_있다() {
        reservationTimeService.createReservationTime(LocalTime.of(9, 0));
        reservationTimeService.createReservationTime(LocalTime.of(11, 0));

        assertThat(reservationTimeService.findReservationTimes())
                .extracting(ReservationTime::startAt)
                .containsExactly(LocalTime.of(9, 0), LocalTime.of(11, 0));
    }

    @Test
    void 예약을_삭제할_수_있다() {
        ReservationTime saved = reservationTimeService.createReservationTime(LocalTime.of(16, 0));

        reservationTimeService.deleteReservationTime(saved.id());
    }

    @Test
    void 예약이_있으면_삭제가_차단된다() {
        ReservationTime saved = reservationTimeService.createReservationTime(LocalTime.of(15, 40));
        insertReservation("kim", LocalDate.of(2026, 5, 1), saved.id());

        assertThatThrownBy(() -> reservationTimeService.deleteReservationTime(saved.id()))
                .isInstanceOf(ReservationTimeNotEmptyException.class);
    }

    @Test
    void 존재하지_않는_ID로_삭제해도_예외가_발생하지_않는다() {
        assertThatCode(() -> reservationTimeService.deleteReservationTime(999L))
                .doesNotThrowAnyException();
    }

    private void insertReservation(String name, LocalDate date, long timeId) {
        String sql = "INSERT INTO reservation (name, date, time_id) VALUES (?, ?, ?)";
        jdbcTemplate.update(sql, name, date, timeId);
    }
}
