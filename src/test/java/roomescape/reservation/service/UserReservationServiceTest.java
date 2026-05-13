package roomescape.reservation.service;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

import java.time.LocalDate;
import java.util.List;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.test.context.ActiveProfiles;
import roomescape.exception.ApiException;
import roomescape.exception.DuplicateException;
import roomescape.exception.NotFoundException;
import roomescape.exception.UnauthorizedActionException;
import roomescape.reservation.domain.Reservation;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.NONE)
@ActiveProfiles("test")
class UserReservationServiceTest {

    @Autowired
    private UserReservationService userReservationService;

    @Autowired
    private JdbcTemplate jdbcTemplate;

    @BeforeEach
    void setUp() {
        jdbcTemplate.execute("SET REFERENTIAL_INTEGRITY FALSE");
        jdbcTemplate.execute("DELETE FROM reservation");
        jdbcTemplate.execute("DELETE FROM reservation_time");
        jdbcTemplate.execute("DELETE FROM themes");
        jdbcTemplate.execute("SET REFERENTIAL_INTEGRITY TRUE");
        jdbcTemplate.execute("ALTER TABLE themes ALTER COLUMN id RESTART WITH 1");
        jdbcTemplate.execute("ALTER TABLE reservation_time ALTER COLUMN id RESTART WITH 1");
        jdbcTemplate.execute("ALTER TABLE reservation ALTER COLUMN id RESTART WITH 1");

        // Theme A(id=1), Theme B(id=2)
        jdbcTemplate.update("INSERT INTO themes (name, description, thumbnail) VALUES ('Theme A', 'Desc', 'https://a.png')");
        jdbcTemplate.update("INSERT INTO themes (name, description, thumbnail) VALUES ('Theme B', 'Desc', 'https://b.png')");
        // time(id=1)=10:00, time(id=2)=11:00
        jdbcTemplate.update("INSERT INTO reservation_time (start_at) VALUES ('10:00:00')");
        jdbcTemplate.update("INSERT INTO reservation_time (start_at) VALUES ('11:00:00')");
        // reservation(id=1): 2099-12-31 / time=1 / theme=1  → 중복·삭제 테스트용
        jdbcTemplate.update("INSERT INTO reservation (name, date, time_id, theme_id) VALUES ('ScheduleTest', '2099-12-31', 1, 1)");
        // reservation(id=2): 2026-05-01 / time=2 / theme=1  → 이름 불일치 테스트용
        jdbcTemplate.update("INSERT INTO reservation (name, date, time_id, theme_id) VALUES ('User1', '2026-05-01', 2, 1)");
    }

    @Test
    void 예약을_등록할_수_있다() {
        LocalDate tomorrow = LocalDate.now().plusDays(1);
        Reservation saved = userReservationService.createReservation("브라운", tomorrow, 2L, 2L);

        assertThat(saved.getName()).isEqualTo("브라운");
        assertThat(saved.getDate()).isEqualTo(tomorrow);
        assertThat(saved.getTime().startAt()).isNotNull();
        assertThat(saved.getTheme().name()).isEqualTo("Theme B");
    }

    @Test
    void 예약_시간_ID가_없으면_예외가_발생한다() {
        assertThatThrownBy(
                () -> userReservationService.createReservation("브라운", LocalDate.now().plusDays(1), 999L, 1L))
                .isInstanceOf(NotFoundException.class);
    }

    @Test
    void 테마_ID가_없으면_예외가_발생한다() {
        assertThatThrownBy(
                () -> userReservationService.createReservation("브라운", LocalDate.now().plusDays(1), 1L, 999L))
                .isInstanceOf(NotFoundException.class);
    }

    @Test
    void 지나간_날짜에_예약하면_예외가_발생한다() {
        assertThatThrownBy(
                () -> userReservationService.createReservation("브라운", LocalDate.now().minusDays(1), 1L, 1L))
                .isInstanceOf(ApiException.class)
                .hasMessage("지나간 날짜·시간에는 예약할 수 없습니다.");
    }

    @Test
    void 지나간_시간에_예약하면_예외가_발생한다() {
        jdbcTemplate.update("INSERT INTO reservation_time (id, start_at) VALUES (99, '00:00:00')");

        assertThatThrownBy(
                () -> userReservationService.createReservation("브라운", LocalDate.now(), 99L, 1L))
                .isInstanceOf(ApiException.class)
                .hasMessage("지나간 날짜·시간에는 예약할 수 없습니다.");
    }

    @Test
    void 예약이_중복되면_예외가_발생한다() {
        assertThatThrownBy(
                () -> userReservationService.createReservation("코니", LocalDate.of(2099, 12, 31), 1L, 1L))
                .isInstanceOf(DuplicateException.class)
                .hasMessage("해당 날짜의 해당 시간은 이미 예약되었습니다.");
    }

    @Test
    void 예약을_삭제할_수_있다() {
        List<Reservation> before = userReservationService.getReservations();
        userReservationService.deleteReservation(1L, "ScheduleTest");
        List<Reservation> after = userReservationService.getReservations();

        assertThat(after).hasSize(before.size() - 1);
    }

    @Test
    void 예약자_이름이_다르면_예외가_발생한다() {
        assertThatThrownBy(() -> userReservationService.deleteReservation(1L, "WrongName"))
                .isInstanceOf(UnauthorizedActionException.class)
                .hasMessage("예약자 이름이 일치하지 않습니다.");
    }

    @Test
    void 존재하지_않는_예약을_삭제하면_예외가_발생한다() {
        assertThatThrownBy(() -> userReservationService.deleteReservation(999L, "누구"))
                .isInstanceOf(NotFoundException.class);
    }

    @Test
    void 이미_지난_예약을_취소하면_예외가_발생한다() {
        // reservation(id=2): User1, 2026-05-01(과거), time=2
        assertThatThrownBy(() -> userReservationService.deleteReservation(2L, "User1"))
                .isInstanceOf(ApiException.class)
                .hasMessage("이미 지난 예약은 취소하거나 변경할 수 없습니다.");
    }

    @Test
    void 본인의_예약_목록을_조회할_수_있다() {
        List<Reservation> result = userReservationService.getMyReservations("ScheduleTest");

        assertThat(result).hasSize(1);
        assertThat(result.get(0).getName()).isEqualTo("ScheduleTest");
    }

    @Test
    void 예약이_없는_이름은_빈_목록이_반환된다() {
        List<Reservation> result = userReservationService.getMyReservations("Nobody");

        assertThat(result).isEmpty();
    }

    @Test
    void 예약의_날짜와_시간을_변경할_수_있다() {
        LocalDate newDate = LocalDate.now().plusDays(3);
        Reservation updated = userReservationService.updateReservation(1L, "ScheduleTest", newDate, 2L);

        assertThat(updated.getDate()).isEqualTo(newDate);
        assertThat(updated.getTime().id()).isEqualTo(2L);
    }

    @Test
    void 다른_사람의_예약을_변경하면_예외가_발생한다() {
        assertThatThrownBy(
                () -> userReservationService.updateReservation(1L, "WrongName", LocalDate.now().plusDays(1), 2L))
                .isInstanceOf(UnauthorizedActionException.class)
                .hasMessage("예약자 이름이 일치하지 않습니다.");
    }

    @Test
    void 이미_지난_예약을_변경하면_예외가_발생한다() {
        // reservation(id=2): User1, 2026-05-01(과거)
        assertThatThrownBy(
                () -> userReservationService.updateReservation(2L, "User1", LocalDate.now().plusDays(1), 1L))
                .isInstanceOf(ApiException.class)
                .hasMessage("이미 지난 예약은 취소하거나 변경할 수 없습니다.");
    }

    @Test
    void 변경하려는_날짜와_시간이_이미_차있으면_예외가_발생한다() {
        // 2099-12-31 / time=2 / theme=1 을 미리 예약하여 슬롯 점유
        jdbcTemplate.update("INSERT INTO reservation (name, date, time_id, theme_id) VALUES ('Other', '2099-12-31', 2, 1)");

        assertThatThrownBy(
                () -> userReservationService.updateReservation(1L, "ScheduleTest", LocalDate.of(2099, 12, 31), 2L))
                .isInstanceOf(DuplicateException.class)
                .hasMessage("해당 날짜의 해당 시간은 이미 예약되었습니다.");
    }

    @Test
    void 존재하지_않는_예약을_변경하면_예외가_발생한다() {
        assertThatThrownBy(
                () -> userReservationService.updateReservation(999L, "누구", LocalDate.now().plusDays(1), 1L))
                .isInstanceOf(NotFoundException.class);
    }
}