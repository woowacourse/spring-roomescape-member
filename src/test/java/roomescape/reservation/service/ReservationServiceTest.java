package roomescape.reservation.service;

import static org.assertj.core.api.Assertions.assertThat;

import java.time.LocalDate;
import java.time.LocalTime;
import java.util.List;
import org.assertj.core.api.SoftAssertions;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.JdbcTest;
import org.springframework.boot.test.context.TestConfiguration;
import org.springframework.context.annotation.Bean;
import org.springframework.jdbc.core.JdbcTemplate;
import roomescape.reservation.domain.Reservation;
import roomescape.reservation.repository.H2ReservationRepository;
import roomescape.reservation.repository.ReservationRepository;
import roomescape.time.repository.H2ReservationTimeRepository;
import roomescape.time.repository.ReservationTimeRepository;
import roomescape.time.service.ReservationTimeService;


@JdbcTest
class ReservationServiceTest {

    @Autowired
    private ReservationService reservationService;

    @Autowired
    private ReservationTimeService reservationTimeService;

    @Test
    void 예약_정보_목록을_조회한다() {
        // given
        final LocalTime time = LocalTime.parse("10:00");
        final LocalTime time2 = LocalTime.parse("18:00");

        final Long time_id1 = reservationTimeService.save(time);
        final Long time_id2 = reservationTimeService.save(time2);

        final String name = "헤일러";
        final LocalDate date = LocalDate.parse("2025-08-01");

        final String name2 = "머피";
        final LocalDate date2 = LocalDate.parse("2025-08-01");

        reservationService.save(name, date, time_id1);
        reservationService.save(name2, date2, time_id2);

        // when
        final List<Reservation> reservations = reservationService.findAll();

        // then
        assertThat(reservations.size()).isEqualTo(2);
    }

    @Test
    void 예약_정보를_저장한다() {
        // given
        final String name = "헤일러";
        final LocalDate date = LocalDate.parse("2023-08-05");
        final LocalTime time = LocalTime.parse("10:00");
        final Long timeId = reservationTimeService.save(time);

        // when
        final Long savedId = reservationService.save(name, date, timeId);
        final Reservation found = reservationService.getById(savedId);

        // then
        SoftAssertions.assertSoftly(softly -> {
            softly.assertThat(savedId).isEqualTo(found.getId());
            softly.assertThat(name).isEqualTo(found.getName());
            softly.assertThat(date).isEqualTo(found.getDate());
            softly.assertThat(timeId).isEqualTo(found.getTime().getId());
            softly.assertThat(time).isEqualTo(found.getTime().getStartAt());
        });
    }


    @TestConfiguration
    static class TestConfig {

        @Bean
        public ReservationTimeRepository reservationTimeRepository(
                final JdbcTemplate jdbcTemplate
        ) {
            return new H2ReservationTimeRepository(jdbcTemplate);
        }

        @Bean
        public ReservationTimeService reservationTimeService(
                final ReservationTimeRepository reservationTimeRepository
        ) {
            return new ReservationTimeService(reservationTimeRepository);
        }

        @Bean
        public ReservationRepository reservationRepository(
                final JdbcTemplate jdbcTemplate
        ) {
            return new H2ReservationRepository(jdbcTemplate);
        }

        @Bean
        public ReservationService reservationService(
                final ReservationRepository reservationRepository,
                final ReservationTimeService reservationTimeService
        ) {
            return new ReservationService(reservationRepository, reservationTimeService);
        }
    }
}
