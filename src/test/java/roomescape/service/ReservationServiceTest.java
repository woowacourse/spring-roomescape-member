package roomescape.service;

import java.time.LocalDate;
import java.time.LocalTime;
import javax.sql.DataSource;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.JdbcTest;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.test.annotation.DirtiesContext;
import org.springframework.test.annotation.DirtiesContext.ClassMode;
import roomescape.domain.time.Time;
import roomescape.dto.reservation.ReservationRequest;
import roomescape.global.exception.model.ConflictException;
import roomescape.repository.ReservationRepository;
import roomescape.repository.TimeRepository;

import static org.assertj.core.api.Assertions.*;

@JdbcTest
@DirtiesContext(classMode = ClassMode.BEFORE_EACH_TEST_METHOD)
class ReservationServiceTest {

    @Autowired
    private JdbcTemplate jdbcTemplate;

    @Autowired
    private DataSource dataSource;

    private ReservationService reservationService;
    private TimeRepository timeRepository;
    private ReservationRepository reservationRepository;

    @BeforeEach
    void init() {
        timeRepository = new TimeRepository(jdbcTemplate, dataSource);
        reservationRepository = new ReservationRepository(jdbcTemplate, dataSource);
        reservationService = new ReservationService(reservationRepository, timeRepository);
    }

    @Test
    @DisplayName("동일한 날짜와 시간에 예약을 생성하면 예외가 발생한다")
    void duplicateTimeReservationAddFail() {
        Time time = timeRepository.save(new Time(LocalTime.of(12, 30)));
        reservationService.createReservation(new ReservationRequest("예약", LocalDate.now().plusDays(1L), time.getId()));

        assertThatThrownBy(() -> reservationService.createReservation(
                        new ReservationRequest("예약", LocalDate.now().plusDays(1L), time.getId())))
                .isInstanceOf(ConflictException.class);
    }

    @Test
    @DisplayName("이미 지난 날짜로 예약을 생성하면 예외가 발생한다")
    void beforeDateReservationFail() {
        Time time = timeRepository.save(new Time(LocalTime.of(12, 30)));
        LocalDate beforeDate = LocalDate.now().minusDays(1L);

        assertThatThrownBy(() -> reservationService.createReservation(
                new ReservationRequest("예약", beforeDate, time.getId())))
                .isInstanceOf(ConflictException.class);
    }

    @Test
    @DisplayName("현재 날짜가 예약 당일이지만, 이미 지난 시간으로 예약을 생성하면 예외가 발생한다")
    void beforeTimeReservationFail() {
        LocalTime requestTime = LocalTime.now();
        LocalTime beforeTime = requestTime.minusHours(1L);
        Time time = timeRepository.save(new Time(beforeTime));

        assertThatThrownBy(() -> reservationService.createReservation(
                new ReservationRequest("예약", LocalDate.now(), time.getId())))
                .isInstanceOf(ConflictException.class);
    }
}
