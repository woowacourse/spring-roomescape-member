package roomescape.service;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

import java.time.LocalDate;
import java.time.LocalTime;
import java.util.List;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.context.SpringBootTest.WebEnvironment;
import org.springframework.jdbc.core.JdbcTemplate;
import roomescape.dao.ReservationDao;
import roomescape.dao.ReservationTimeDao;
import roomescape.dao.ThemeDao;
import roomescape.domain.Reservation;
import roomescape.domain.ReservationTime;
import roomescape.domain.Theme;

@SpringBootTest(webEnvironment = WebEnvironment.NONE)
class ReservationTimeServiceTest {

    @Autowired
    private JdbcTemplate jdbcTemplate;

    @Autowired
    private ReservationTimeService reservationTimeService;

    @Autowired
    private ReservationTimeDao reservationTimeDao;

    @Autowired
    private ReservationDao reservationDao;

    @Autowired
    private ThemeDao themeDao;

    @BeforeEach
    void setUp() {
        jdbcTemplate.execute("SET REFERENTIAL_INTEGRITY FALSE");
        jdbcTemplate.execute("TRUNCATE TABLE reservation RESTART IDENTITY");
        jdbcTemplate.execute("TRUNCATE TABLE reservation_time RESTART IDENTITY");
        jdbcTemplate.execute("TRUNCATE TABLE theme RESTART IDENTITY");
        jdbcTemplate.execute("SET REFERENTIAL_INTEGRITY TRUE");
    }

    @Test
    void 중복된_시간을_저장하면_예외가_발생한다() {
        // given
        ReservationTime existTime = new ReservationTime(LocalTime.parse("10:00"));
        reservationTimeDao.save(existTime);

        // when & then
        ReservationTime newTime = new ReservationTime(LocalTime.parse("10:00"));

        //then
        assertThatThrownBy(() -> reservationTimeService.save(newTime))
                .isInstanceOf(IllegalArgumentException.class)
                .hasMessageContaining("이미 존재하는 예약시간입니다.");
    }

    @Test
    void 이미_예약시간이_차있으면_삭제할_수_없다() {
        //given
        ReservationTime existTime = new ReservationTime(LocalTime.parse("10:00"));
        ReservationTime savedTime = reservationTimeDao.save(existTime);
        Theme theme = new Theme("공포", "무서움", "https://roomescape.com");
        Theme savedTheme = themeDao.save(theme);
        Reservation reservation = new Reservation("pobi", LocalDate.parse("2030-05-02"), savedTime, savedTheme);
        reservationDao.save(reservation);
        Long savedId = savedTime.getId();

        //when & then
        assertThatThrownBy(() -> reservationTimeService.deleteById(savedId))
                .isInstanceOf(IllegalArgumentException.class)
                .hasMessageContaining("삭제할 수 없습니다");
    }

    @Test
    void 시간을_저장하고_조회한다() {
        ReservationTime savedTime = reservationTimeService.save(new ReservationTime(LocalTime.of(10, 0)));
        List<ReservationTime> times = reservationTimeService.findAll();
        assertThat(times.getFirst().getId()).isEqualTo(savedTime.getId());
    }

    @Test
    void 시간을_저장하고_삭제한다() {
        ReservationTime savedTime = reservationTimeService.save(new ReservationTime(LocalTime.of(10, 0)));
        reservationTimeService.deleteById(savedTime.getId());
        assertThat(reservationTimeService.findAll()).hasSize(0);
    }
}
