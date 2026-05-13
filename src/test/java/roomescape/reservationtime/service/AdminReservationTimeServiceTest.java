package roomescape.reservationtime.service;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatCode;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

import java.time.LocalTime;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.test.context.ActiveProfiles;
import roomescape.exception.DuplicateException;
import roomescape.exception.ResourceInUseException;
import roomescape.reservationtime.domain.ReservationTime;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.NONE)
@ActiveProfiles("test")
class AdminReservationTimeServiceTest {

    @Autowired
    private AdminReservationTimeService adminReservationTimeService;

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

        // time(id=1)=10:00 — 예약이 연결되어 있음
        jdbcTemplate.update("INSERT INTO themes (name, description, thumbnail) VALUES ('Theme A', 'Desc', 'https://a.png')");
        jdbcTemplate.update("INSERT INTO reservation_time (start_at) VALUES ('10:00:00')");
        jdbcTemplate.update("INSERT INTO reservation (name, date, time_id, theme_id) VALUES ('User1', '2026-05-01', 1, 1)");
    }

    @Test
    void 예약_시간을_등록할_수_있다() {
        ReservationTime saved = adminReservationTimeService.createReservationTime(LocalTime.of(16, 0));

        assertThat(saved.startAt()).isEqualTo(LocalTime.of(16, 0));
    }

    @Test
    void 예약_시간이_중복되면_예외가_발생한다() {
        assertThatThrownBy(() -> adminReservationTimeService.createReservationTime(LocalTime.of(10, 0)))
                .isInstanceOf(DuplicateException.class)
                .hasMessage("이미 존재하는 예약 시간입니다");
    }

    @Test
    void 예약이_있으면_예약_시간을_삭제할_수_없다() {
        assertThatThrownBy(() -> adminReservationTimeService.deleteReservationTime(1L))
                .isInstanceOf(ResourceInUseException.class)
                .hasMessage("예약이 있어 삭제할 수 없습니다");
    }

    @Test
    void 예약이_없으면_예약_시간을_삭제할_수_있다() {
        ReservationTime newTime = adminReservationTimeService.createReservationTime(LocalTime.of(16, 0));

        assertThatCode(() -> adminReservationTimeService.deleteReservationTime(newTime.id()))
                .doesNotThrowAnyException();
    }
}