package roomescape.service.reservation;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.JdbcTest;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.test.context.jdbc.Sql;
import roomescape.domain.reservation.ReservationTime;
import roomescape.domain.reservation.ReservationTimeAvailability;
import roomescape.exception.IllegalUserRequestException;
import roomescape.repository.reservation.ReservationRepository;
import roomescape.repository.reservation.ReservationTimeRepository;
import roomescape.service.dto.reservation.ReservationTimeSaveRequest;

import java.time.LocalDate;
import java.time.LocalTime;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatCode;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

@JdbcTest
@Sql("/truncate-data.sql")
class ReservationTimeServiceTest {

    private JdbcTemplate jdbcTemplate;
    private ReservationTimeService reservationTimeService;

    @Autowired
    public ReservationTimeServiceTest(JdbcTemplate jdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;
        reservationTimeService = new ReservationTimeService(
                new ReservationTimeRepository(jdbcTemplate),
                new ReservationRepository(jdbcTemplate)
        );
    }

    @Test
    @DisplayName("존재하지 않는 예약 시간인 경우 생성에 성공한다")
    void checkDuplicateTime_Success() {
        ReservationTimeSaveRequest request = new ReservationTimeSaveRequest(LocalTime.of(12, 0));

        assertThatCode(() -> reservationTimeService.createReservationTime(request))
                .doesNotThrowAnyException();
    }

    @Test
    @DisplayName("이미 존재하는 예약 시간을 생성하는 경우 예외가 발생한다.")
    @Sql(scripts = {"/truncate-data.sql", "/theme-time-member-data.sql"})
    void checkDuplicateTime_Failure() {
        ReservationTimeSaveRequest request = new ReservationTimeSaveRequest(LocalTime.of(10, 0));

        assertThatThrownBy(() -> reservationTimeService.createReservationTime(request))
                .isInstanceOf(IllegalUserRequestException.class)
                .hasMessage("이미 존재하는 예약 시간입니다.");
    }

    @Test
    @DisplayName("날짜와 테마가 주어지면 각 시간의 예약 여부를 구한다.")
    @Sql(scripts = {"/truncate-data.sql", "/theme-time-member-data.sql", "/reservation-data.sql"})
    void findAvailabilityByDateAndTheme() {
        LocalDate date = LocalDate.of(2100, 12, 31);

        assertThat(reservationTimeService.findReservationTimeAvailability(date, 1L))
                .isEqualTo(List.of(
                        new ReservationTimeAvailability(new ReservationTime(1L, LocalTime.of(10, 0)), true),
                        new ReservationTimeAvailability(new ReservationTime(2L, LocalTime.of(11, 0)), false)
                ));
    }

    @Test
    @DisplayName("예약 중이 아닌 시간을 삭제할 시 성공한다.")
    @Sql(scripts = {"/truncate-data.sql", "/theme-time-member-data.sql"})
    void deleteNotReservedTime_Success() {
        assertThatCode(() -> reservationTimeService.deleteReservationTime(2L))
                .doesNotThrowAnyException();
    }

    @Test
    @DisplayName("이미 예약 중인 시간을 삭제할 시 예외가 발생한다.")
    @Sql(scripts = {"/truncate-data.sql", "/theme-time-member-data.sql", "/reservation-data.sql"})
    void deleteReservedTime_Failure() {
        assertThatThrownBy(() -> reservationTimeService.deleteReservationTime(1L))
                .isInstanceOf(IllegalUserRequestException.class)
                .hasMessage("이미 예약중인 시간은 삭제할 수 없습니다.");
    }
}
