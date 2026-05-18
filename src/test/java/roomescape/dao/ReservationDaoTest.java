package roomescape.dao;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.JdbcTest;
import org.springframework.context.annotation.Import;
import org.springframework.test.annotation.DirtiesContext;
import org.springframework.test.context.ActiveProfiles;
import roomescape.domain.Reservation;
import roomescape.domain.ReservationTime;
import roomescape.domain.Theme;
import roomescape.exception.NotFoundException;
import roomescape.exception.ReservationAlreadyExistsException;

import java.time.LocalDate;
import java.time.LocalTime;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

@JdbcTest
@ActiveProfiles("test")
@Import({
        ReservationTimeDao.class,
        ReservationDao.class,
        ThemeDao.class
})
@DirtiesContext(classMode = DirtiesContext.ClassMode.BEFORE_EACH_TEST_METHOD)
public class ReservationDaoTest {

    private final ReservationTimeDao reservationTimeDao;
    private final ReservationDao reservationDao;
    private final ThemeDao themeDao;

    @Autowired
    public ReservationDaoTest(ReservationTimeDao reservationTimeDao, ReservationDao reservationDao, ThemeDao themeDao) {
        this.reservationTimeDao = reservationTimeDao;
        this.reservationDao = reservationDao;
        this.themeDao = themeDao;
    }

    @Test
    void 중복된_예약을_추가하면_예외가_발생한다() {
        Long themeId = themeDao.insertTheme(Theme.from("테마", "설명", "url"));
        Long timeId = reservationTimeDao.insertReservationTime(ReservationTime.from(LocalTime.of(15, 30)));
        LocalDate date = LocalDate.of(2026, 5, 6);
        Reservation reservation = Reservation.from("이든", date, timeId, themeId);
        reservationDao.insertReservation(reservation);

        assertThatThrownBy(() -> reservationDao.insertReservation(reservation))
                .isInstanceOf(ReservationAlreadyExistsException.class)
                .hasMessage("해당 날짜, 시간, 테마에 대한 예약이 이미 존재입니다.");
    }

    @Test
    void 예약_조회_매핑_테스트() {
        Long themeId = themeDao.insertTheme(Theme.from("테마", "설명", "url"));
        Long timeId = reservationTimeDao.insertReservationTime(ReservationTime.from(LocalTime.of(15, 30)));
        Reservation reservationToSave = Reservation.from("이든", LocalDate.of(2026, 5, 5), timeId, themeId);
        Long reservationId = reservationDao.insertReservation(reservationToSave);

        Reservation reservation = reservationDao.findReservationById(reservationId);

        assertThat(reservation.getName()).isEqualTo("이든");
        assertThat(reservation.getTimeId()).isEqualTo(timeId);
        assertThat(reservation.getThemeId()).isEqualTo(themeId);
        assertThat(reservation.getDate()).isEqualTo(LocalDate.of(2026, 5, 5));
    }
}
