package roomescape.dao;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.JdbcTest;
import org.springframework.context.annotation.Import;
import org.springframework.test.annotation.DirtiesContext;
import org.springframework.test.context.ActiveProfiles;
import roomescape.domain.ReservationTime;
import roomescape.exception.ReservationTimeInUseException;

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
public class ReservationTimeDaoTest {

    private ReservationTimeDao reservationTimeDao;
    private ReservationDao reservationDao;
    private ThemeDao themeDao;

    @Autowired
    public ReservationTimeDaoTest(ReservationTimeDao reservationTimeDao, ReservationDao reservationDao, ThemeDao themeDao) {
        this.reservationTimeDao = reservationTimeDao;
        this.reservationDao = reservationDao;
        this.themeDao = themeDao;
    }

    @Test
    void 예약에서_사용중인_시간을_삭제하면_예외가_발생한다() {
        Long timeId = reservationTimeDao.insertReservationTime(LocalTime.of(10, 0));
        Long themeId = themeDao.insertTheme("이든의 하우스", "설명", "링크");
        reservationDao.insertReservation("이든", LocalDate.of(2026, 5, 6), timeId, themeId);

        assertThatThrownBy(() -> reservationTimeDao.delete(timeId))
                .isInstanceOf(ReservationTimeInUseException.class);
    }

    @Test
    void 예약_시간_생성_테스트() {
        LocalTime startTime = LocalTime.of(12, 0);

        Long id = reservationTimeDao.insertReservationTime(startTime);
        ReservationTime actual = reservationTimeDao.findById(id);

        assertThat(actual.getId()).isNotNull();
        assertThat(actual.getStartAt()).isEqualTo(startTime);
    }
}
