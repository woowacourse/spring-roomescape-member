package roomescape.dao;

import static org.assertj.core.api.Assertions.assertThat;

import java.time.LocalDate;
import java.time.LocalTime;
import java.util.List;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.JdbcTest;
import org.springframework.context.annotation.Import;
import org.springframework.test.context.ActiveProfiles;
import roomescape.dao.jdbc.ReservationJdbcDao;
import roomescape.dao.jdbc.ThemeJdbcDao;
import roomescape.dao.jdbc.TimeJdbcDao;
import roomescape.domain.Reservation;
import roomescape.domain.Theme;
import roomescape.domain.Time;
import roomescape.domain.vo.Name;

@JdbcTest
@Import({ReservationJdbcDao.class, TimeJdbcDao.class, ThemeJdbcDao.class})
@ActiveProfiles("test")
class ReservationJdbcDaoTest {

    @Autowired
    private ReservationDao reservationDao;
    @Autowired
    private TimeDao timeDao;
    @Autowired
    private ThemeDao themeDao;

    private Time time;
    private Theme theme;

    @BeforeEach
    void setUp() {
        time = timeDao.insert(new Time(LocalTime.of(13, 0)));
        theme = themeDao.insert(new Theme(new Name("방탈출"), "http://url", "설명"));
    }

    @Test
    @DisplayName("전체 예약 목록을 조회한다")
    void findAll() {
        Reservation r1 = reservationDao.insert(new Reservation("유저1", LocalDate.of(2026, 6, 1), time, theme));
        Reservation r2 = reservationDao.insert(new Reservation("유저2", LocalDate.of(2026, 6, 2), time, theme));

        List<Reservation> result = reservationDao.findAll();

        assertThat(result).hasSize(2).containsExactlyInAnyOrder(r1, r2);
    }
}
