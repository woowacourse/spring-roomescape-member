package roomescape.service.reservation;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.JdbcTest;
import org.springframework.jdbc.core.JdbcTemplate;
import roomescape.domain.ReservationTime;
import roomescape.repository.ReservationRepository;
import roomescape.repository.ReservationTimeRepository;
import roomescape.service.dto.SaveReservationRequest;
import roomescape.service.dto.SaveReservationTimeRequest;
import roomescape.service.reservationtime.ReservationTimeCreateService;

import java.time.LocalDate;
import java.time.LocalTime;

import static org.assertj.core.api.Assertions.assertThatCode;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

@JdbcTest
class ReservationCreateServiceTest {

    private JdbcTemplate jdbcTemplate;
    private ReservationCreateService reservationCreateService;
    private ReservationTimeCreateService reservationTimeCreateService;

    @Autowired
    public ReservationCreateServiceTest(JdbcTemplate jdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;
        reservationCreateService = new ReservationCreateService(
                new ReservationRepository(jdbcTemplate),
                new ReservationTimeRepository(jdbcTemplate)
        );
        reservationTimeCreateService = new ReservationTimeCreateService(
                new ReservationTimeRepository(jdbcTemplate)
        );
    }

    @Test
    @DisplayName("예약 가능한 시간인 경우 성공한다.")
    void checkDuplicateReservationTime_Success() {
        ReservationTime reservationTime = reservationTimeCreateService.createReservationTime(
                new SaveReservationTimeRequest(LocalTime.now().plusHours(1L)));
        SaveReservationRequest request = new SaveReservationRequest(
                "capy", LocalDate.now().plusDays(1L), reservationTime.getId());

        assertThatCode(() -> reservationCreateService.createReservation(request))
                .doesNotThrowAnyException();
    }

    @Test
    @DisplayName("이미 예약된 시간인 경우 예외가 발생한다.")
    void checkDuplicateReservationTime_Failure() {
        ReservationTime reservationTime = reservationTimeCreateService.createReservationTime(
                new SaveReservationTimeRequest(LocalTime.now().plusHours(1L)));
        SaveReservationRequest request = new SaveReservationRequest(
                "capy", LocalDate.now().plusDays(1L), reservationTime.getId());
        reservationCreateService.createReservation(request);

        assertThatThrownBy(() -> reservationCreateService.createReservation(request))
                .isInstanceOf(IllegalArgumentException.class)
                .hasMessage("이미 예약된 시간입니다.");
    }
}
