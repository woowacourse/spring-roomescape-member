package roomescape.reservation.service;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

import java.time.LocalDate;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.test.context.ActiveProfiles;
import roomescape.exception.NotFoundException;
import roomescape.reservation.domain.Reservation;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.NONE)
@ActiveProfiles("test")
class AdminReservationServiceTest {

    @Autowired
    private AdminReservationService adminReservationService;

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

        // Theme A(id=1), time(id=1)=10:00, reservation(id=1)
        jdbcTemplate.update("INSERT INTO themes (name, description, thumbnail) VALUES ('Theme A', 'Desc', 'https://a.png')");
        jdbcTemplate.update("INSERT INTO reservation_time (start_at) VALUES ('10:00:00')");
        jdbcTemplate.update("INSERT INTO reservation (name, date, time_id, theme_id) VALUES ('User1', '2026-05-01', 1, 1)");
    }

    @Test
    void 관리자가_과거_날짜로도_예약을_등록할_수_있다() {
        Reservation saved = adminReservationService.forceCreateReservation(1L, "브라운", LocalDate.of(2020, 1, 1), 1L);

        assertThat(saved.getName()).isEqualTo("브라운");
        assertThat(saved.getDate()).isEqualTo(LocalDate.of(2020, 1, 1));
    }

    @Test
    void 예약_시간이_없으면_예외가_발생한다() {
        assertThatThrownBy(
                () -> adminReservationService.forceCreateReservation(1L, "브라운", LocalDate.now().plusDays(1), 999L))
                .isInstanceOf(NotFoundException.class);
    }

    @Test
    void 테마가_없으면_예외가_발생한다() {
        assertThatThrownBy(
                () -> adminReservationService.forceCreateReservation(999L, "브라운", LocalDate.now().plusDays(1), 1L))
                .isInstanceOf(NotFoundException.class);
    }

    @Test
    void 관리자가_예약을_검증_없이_삭제할_수_있다() {
        int before = jdbcTemplate.queryForObject("SELECT COUNT(*) FROM reservation", Integer.class);
        adminReservationService.forceDeleteReservation(1L);
        int after = jdbcTemplate.queryForObject("SELECT COUNT(*) FROM reservation", Integer.class);

        assertThat(after).isEqualTo(before - 1);
    }
}