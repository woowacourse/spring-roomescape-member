package roomescape.dao;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.JdbcTest;
import org.springframework.context.annotation.Import;
import org.springframework.test.annotation.DirtiesContext;
import org.springframework.test.context.ActiveProfiles;
import roomescape.domain.Reservation;
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

    private ReservationTimeDao reservationTimeDao;
    private ReservationDao reservationDao;
    private ThemeDao themeDao;

    @Autowired
    public ReservationDaoTest(ReservationTimeDao reservationTimeDao, ReservationDao reservationDao, ThemeDao themeDao) {
        this.reservationTimeDao = reservationTimeDao;
        this.reservationDao = reservationDao;
        this.themeDao = themeDao;
    }

    @Test
    void 중복된_예약을_추가하면_예외가_발생한다() {
        Long themeId = themeDao.insertTheme("테마", "설명", "url");
        Long timeId = reservationTimeDao.insertReservationTime(LocalTime.of(15, 30));

        LocalDate date = LocalDate.of(2026, 5, 6);
        reservationDao.insertReservation("이든", date, timeId, themeId);

        assertThatThrownBy(() -> reservationDao.insertReservation("이든", date, timeId, themeId))
                .isInstanceOf(ReservationAlreadyExistsException.class);
    }

    @Test
    void 테마ID_외래키_제약조건_위반_테스트() {
        Long timeId = reservationTimeDao.insertReservationTime(LocalTime.of(10, 0));
        Long nonExistentThemeId = 999L;

        assertThatThrownBy(() ->
                reservationDao.insertReservation("이든", LocalDate.of(2026, 5, 6), timeId, nonExistentThemeId)
        ).isInstanceOf(NotFoundException.class);
    }

    @Test
    void 시간ID_외래키_제약조건_위반_테스트() {
        Long nonExistentTimeId = 999L;
        Long themeId = themeDao.insertTheme("테마", "설명", "url");

        assertThatThrownBy(() ->
                reservationDao.insertReservation("이든", LocalDate.of(2026, 5, 6), nonExistentTimeId, themeId)
        ).isInstanceOf(NotFoundException.class);
    }

    @Test
    void 예약_조회_매핑_테스트() {
        Long themeId = themeDao.insertTheme("테마", "설명", "url");
        Long timeId = reservationTimeDao.insertReservationTime(LocalTime.of(15, 30));

        Long reservationId = reservationDao.insertReservation("이든", LocalDate.of(2026, 5, 5), timeId, themeId);

        Reservation reservation = reservationDao.findReservationById(reservationId);

        assertThat(reservation.getName()).isEqualTo("이든");
        assertThat(reservation.getTime().getId()).isEqualTo(timeId);
        assertThat(reservation.getTime().getStartAt()).isEqualTo(LocalTime.of(15, 30));
    }
}
