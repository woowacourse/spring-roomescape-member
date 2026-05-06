package roomescape.reservation;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatCode;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

import java.io.IOException;
import java.io.InputStream;
import java.time.LocalDate;
import java.time.LocalTime;
import java.util.List;
import java.util.Properties;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import roomescape.reservationtime.ReservationTimeNotFoundException;
import roomescape.reservationtime.ReservationTimeService;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.datasource.DriverManagerDataSource;
import roomescape.reservationtime.ReservationTime;
import roomescape.reservationtime.ReservationTimeDao;
import roomescape.reservationtime.ReservationTimeRepository;

class UserReservationServiceTest {
    private static final String TEST_PROPERTIES = "application-test.properties";

    private UserReservationService userReservationService;
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
        userReservationService = new UserReservationService(reservationRepository, reservationTimeRepository);
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
    void 예약을_등록할_수_있다() {
        ReservationTime time = reservationTimeService.createReservationTime(LocalTime.of(10, 0));

        Reservation saved = userReservationService.createReservation("브라운", LocalDate.of(2026, 5, 1), time.id());

        assertThat(saved.getName()).isEqualTo("브라운");
        assertThat(saved.getDate()).isEqualTo(LocalDate.of(2026, 5, 1));
        assertThat(saved.getTime().startAt()).isEqualTo(LocalTime.of(10, 0));
    }

    @Test
    void 예약_시간_ID가_없으면_예외가_발생한다() {
        assertThatThrownBy(() -> userReservationService.createReservation("브라운", LocalDate.of(2026, 5, 1), 999L))
                .isInstanceOf(ReservationTimeNotFoundException.class);
    }

    @Test
    void 예약이_중복되면_예외가_발생한다() {
        ReservationTime time = reservationTimeService.createReservationTime(LocalTime.of(11, 0));

        userReservationService.createReservation("브라운", LocalDate.of(2026, 5, 1), time.id());

        assertThatThrownBy(() -> userReservationService.createReservation("코니", LocalDate.of(2026, 5, 1), time.id()))
                .isInstanceOf(DuplicateReservationException.class)
                .extracting(Throwable::getMessage)
                .isEqualTo("해당 날짜의 해당 시간은 이미 예약되었습니다");
    }

    @Test
    void 전체_예약을_조회할_수_있다() {
        ReservationTime firstTime = reservationTimeService.createReservationTime(LocalTime.of(12, 0));
        ReservationTime secondTime = reservationTimeService.createReservationTime(LocalTime.of(13, 0));

        userReservationService.createReservation("브라운", LocalDate.of(2026, 5, 1), firstTime.id());
        userReservationService.createReservation("코니", LocalDate.of(2026, 5, 2), secondTime.id());

        List<Reservation> reservations = userReservationService.getReservations();

        assertThat(reservations).hasSize(2);
        assertThat(reservations)
                .extracting(Reservation::getName)
                .containsExactly("브라운", "코니");
    }

    @Test
    void 예약을_삭제할_수_있다() {
        ReservationTime time = reservationTimeService.createReservationTime(LocalTime.of(14, 0));
        Reservation saved = userReservationService.createReservation("브라운", LocalDate.of(2026, 5, 1), time.id());

        userReservationService.deleteReservation(saved.getId());

        assertThat(userReservationService.getReservations()).isEmpty();
    }

    @Test
    void 존재하지_않는_ID로_삭제해도_예외가_발생하지_않는다() {
        assertThatCode(() -> userReservationService.deleteReservation(999L))
                .doesNotThrowAnyException();
    }
}
