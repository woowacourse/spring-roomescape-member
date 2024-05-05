package roomescape.dao;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.JdbcTest;
import org.springframework.context.annotation.Import;
import org.springframework.test.context.jdbc.Sql;
import org.springframework.test.context.jdbc.Sql.ExecutionPhase;
import roomescape.domain.reservation.Reservation;
import roomescape.domain.theme.Theme;
import roomescape.domain.time.Time;

import java.time.LocalDate;
import java.time.LocalTime;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;

@JdbcTest
@Import({ReservationDao.class, TimeDao.class, ThemeDao.class})
@Sql(scripts = "/truncate.sql", executionPhase = ExecutionPhase.BEFORE_TEST_METHOD)
public class ReservationDaoTest {

    @Autowired
    private ReservationDao reservationDao;

    @Autowired
    private TimeDao timeDao;

    @Autowired
    private ThemeDao themeDao;

    @Test
    @DisplayName("전체 예약 정보를 조회한다")
    void readDbReservations() {
        // given
        Time time = timeDao.insert(new Time(LocalTime.of(17, 30)));
        Theme theme = themeDao.insert(new Theme("테마명", "설명", "썸네일URL"));
        reservationDao.insert(new Reservation(
                "브라운",
                LocalDate.of(2024, 4, 25),
                time,
                theme
        ));

        // when
        List<Reservation> reservations = reservationDao.findAll();

        // then
        assertThat(reservations.size()).isEqualTo(1);
    }

    @Test
    @DisplayName("하나의 예약만 등록한 경우, 예약 삭제 뒤 DB를 조회 했을 때 조회 결과 개수는 0개이다.")
    void readReservationsSizeFromDbAfterPostAndDelete() {
        // given
        Time time = timeDao.insert(new Time(LocalTime.of(17, 30)));
        Theme theme = themeDao.insert(new Theme("테마명", "설명", "썸네일URL"));

        // when
        Reservation savedReservation = reservationDao.insert(new Reservation(
                "브라운",
                LocalDate.of(2024, 4, 25),
                time,
                theme
        ));
        int deleteCount = reservationDao.deleteById(savedReservation.getId());

        // then
        assertThat(deleteCount).isEqualTo(1);
    }
}
