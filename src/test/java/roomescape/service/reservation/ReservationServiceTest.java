package roomescape.service.reservation;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.JdbcTest;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.test.context.jdbc.Sql;
import roomescape.exception.IllegalUserRequestException;
import roomescape.repository.member.MemberRepository;
import roomescape.repository.reservation.ReservationRepository;
import roomescape.repository.reservation.ReservationTimeRepository;
import roomescape.repository.reservation.ThemeRepository;
import roomescape.service.dto.reservation.AdminReservationSaveRequest;

import java.time.LocalDate;

import static org.assertj.core.api.Assertions.assertThatCode;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

@JdbcTest
@Sql(scripts = {"/truncate-data.sql", "/theme-time-member-data.sql"})
class ReservationServiceTest {

    private JdbcTemplate jdbcTemplate;
    private ReservationService reservationService;

    @Autowired
    public ReservationServiceTest(JdbcTemplate jdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;
        System.out.println("jdbcTemplate = " + jdbcTemplate);
        reservationService = new ReservationService(
                new ReservationRepository(jdbcTemplate),
                new MemberRepository(jdbcTemplate),
                new ReservationTimeRepository(jdbcTemplate),
                new ThemeRepository(jdbcTemplate)
        );
    }

    @Test
    @DisplayName("예약 가능한 시간인 경우 성공한다.")
    void checkDuplicateReservationTime_Success() {
        // given
        AdminReservationSaveRequest request = new AdminReservationSaveRequest(
                LocalDate.now().plusDays(1L), 1L, 1L, 1L);

        // when & then
        assertThatCode(() -> reservationService.createReservation(request))
                .doesNotThrowAnyException();
    }

    @Test
    @DisplayName("이미 예약된 시간인 경우 예외가 발생한다.")
    void checkDuplicateReservationTime_Failure() {
        // given
        AdminReservationSaveRequest request = new AdminReservationSaveRequest(
                LocalDate.now().plusDays(1L), 1L, 1L, 1L);
        reservationService.createReservation(request);

        // when & then
        assertThatThrownBy(() -> reservationService.createReservation(request))
                .isInstanceOf(IllegalUserRequestException.class)
                .hasMessage("해당 시간에 이미 예약된 테마입니다.");
    }

    @Test
    @DisplayName("지나간 날짜와 시간에 대한 예약 생성시 예외가 발생한다.")
    void checkReservationDateTimeIsFuture_Failure() {
        AdminReservationSaveRequest request = new AdminReservationSaveRequest(
                LocalDate.now().minusDays(1L), 1L, 1L, 1L);

        assertThatThrownBy(() -> reservationService.createReservation(request))
                .isInstanceOf(IllegalUserRequestException.class)
                .hasMessage("지나간 날짜와 시간에 대한 예약 생성은 불가능합니다.");
    }

    @Test
    @DisplayName("존재하지 않는 사용자 이름에 대한 예약 생성시 예외가 발생한다.")
    @Sql(scripts = {"/truncate-data.sql", "/theme-time-member-data.sql"})
    void checkReservationExistUser_Failure() {
        AdminReservationSaveRequest request = new AdminReservationSaveRequest(
                LocalDate.now().plusDays(1L), 100L, 1L, 1L);

        assertThatThrownBy(() -> reservationService.createReservation(request))
                .isInstanceOf(IllegalUserRequestException.class)
                .hasMessage("존재하지 않는 사용자입니다.");
    }

    @Test
    @DisplayName("존재하지 않는 예약 시간에 대한 예약 생성시 예외가 발생한다.")
    @Sql(scripts = {"/truncate-data.sql", "/theme-time-member-data.sql"})
    void checkReservationExistTime_Failure() {
        AdminReservationSaveRequest request = new AdminReservationSaveRequest(
                LocalDate.now().plusDays(1L), 1L, 100L, 1L);

        assertThatThrownBy(() -> reservationService.createReservation(request))
                .isInstanceOf(IllegalUserRequestException.class)
                .hasMessage("존재하지 않는 예약 시간 입니다.");
    }

    @Test
    @DisplayName("존재하지 않는 테마에 대한 예약 생성시 예외가 발생한다.")
    @Sql(scripts = {"/truncate-data.sql", "/theme-time-member-data.sql"})
    void checkReservationExistTheme_Failure() {
        AdminReservationSaveRequest request = new AdminReservationSaveRequest(
                LocalDate.now().plusDays(1L), 1L, 1L, 100L);

        assertThatThrownBy(() -> reservationService.createReservation(request))
                .isInstanceOf(IllegalUserRequestException.class)
                .hasMessage("존재하지 않는 테마 입니다.");
    }
}
