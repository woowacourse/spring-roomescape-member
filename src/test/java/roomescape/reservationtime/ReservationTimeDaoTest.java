package roomescape.reservationtime;

import static org.assertj.core.api.Assertions.assertThat;

import java.io.IOException;
import java.io.InputStream;
import java.time.LocalTime;
import java.util.List;
import java.util.Properties;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.datasource.DriverManagerDataSource;

class ReservationTimeDaoTest {
    private static final String TEST_PROPERTIES = "application-test.properties";

    private ReservationTimeDao reservationTimeDao;
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
        reservationTimeDao = new ReservationTimeDao(jdbcTemplate);

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
    void 예약_시간을_저장할_수_있다() {
        LocalTime time = LocalTime.of(15, 40);

        ReservationTime saved = reservationTimeDao.save(time);

        assertThat(saved.id()).isNotNull();
        assertThat(saved.startAt()).isEqualTo(time);
    }

    @Test
    void 전체_예약_시간을_조회할_수_있다() {
        reservationTimeDao.save(LocalTime.of(15, 40));
        reservationTimeDao.save(LocalTime.of(16, 0));

        List<ReservationTime> times = reservationTimeDao.findAll();

        assertThat(times).hasSize(2);
        assertThat(times)
                .extracting(ReservationTime::startAt)
                .containsExactly(LocalTime.of(15, 40), LocalTime.of(16, 0));
    }

    @Test
    void 단일_예약_시간을_조회할_수_있다() {
        LocalTime time = LocalTime.of(15, 40);
        ReservationTime saved = reservationTimeDao.save(time);

        ReservationTime found = reservationTimeDao.findById(saved.id()).orElseThrow();

        assertThat(found.id()).isEqualTo(saved.id());
        assertThat(found.startAt()).isEqualTo(time);
    }

    @Test
    void 존재하지_않는_ID로_조회하면_빈_Optional을_반환한다() {
        assertThat(reservationTimeDao.findById(999L)).isEmpty();
    }

    @Test
    void 존재하는_ID로_예약_시간을_삭제할_수_있다() {
        ReservationTime saved = reservationTimeDao.save(LocalTime.of(15, 40));

        reservationTimeDao.delete(saved.id());

        assertThat(reservationTimeDao.findAll()).isEmpty();
    }
}
