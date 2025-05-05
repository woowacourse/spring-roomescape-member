package roomescape;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertAll;

import java.time.LocalDate;
import java.time.LocalTime;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.JdbcTest;
import org.springframework.jdbc.core.JdbcTemplate;
import roomescape.dao.ReservationDao;
import roomescape.dao.ReservationTimeDao;
import roomescape.dao.ThemeDao;
import roomescape.domain.Reservation;
import roomescape.domain.ReservationTime;
import roomescape.domain.Theme;

@JdbcTest
class ReservationDaoTest {

    ReservationDao reservationDao;
    ReservationTimeDao reservationTimeDao;
    ThemeDao themeDao;

    @Autowired
    JdbcTemplate jdbcTemplate;

    Reservation savedReservation;

    @BeforeEach
    void beforeEach() {
        reservationDao = new ReservationDao(jdbcTemplate);
        reservationTimeDao = new ReservationTimeDao(jdbcTemplate);
        themeDao = new ThemeDao(jdbcTemplate);
        ReservationTime reservationTime = reservationTimeDao.save(
            new ReservationTime(
                LocalTime.of(10, 5)
            )
        );
        Theme savedTheme = themeDao.save(
            new Theme(
                "탈출 테스트",
                "탈출하는 내용",
                "abc.jpg"
            )
        );
        Reservation reservation = new Reservation(
            "두리",
            LocalDate.of(2025, 10, 5),
            reservationTime,
            savedTheme
        );
        savedReservation = reservationDao.save(reservation);
    }

    @Test
    @DisplayName("Reservation 객체를 저장한다")
    void saveReservation() {
        Reservation reservation = new Reservation(
            "두리",
            LocalDate.of(2025, 10, 5),
            new ReservationTime(
                LocalTime.of(10, 5)
            ),
            new Theme(
                "탈출",
                "탈출하는 내용",
                "abc.jpg"
            )
        );
        Reservation savedReservation = reservationDao.save(reservation);

        assertAll(
            () -> assertThat(savedReservation.getName()).isEqualTo("두리"),
            () -> assertThat(savedReservation.getDate()).isEqualTo(LocalDate.of(2025, 10, 5)),
            () -> assertThat(savedReservation.getTime().getStartAt()).isEqualTo(LocalTime.of(10, 5))
        );
    }

    @Test
    @DisplayName("저장된 Reservation 전체를 조회한다")
    void selectReservation() {
        assertThat(reservationDao.findAll()).contains(savedReservation);
    }

    @Test
    @DisplayName("id로 Reservation을 삭제한다")
    void deleteReservation() {
        reservationDao.deleteById(savedReservation.getId());
        assertThat(reservationDao.findAll()).isEmpty();
    }
}
