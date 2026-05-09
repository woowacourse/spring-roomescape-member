package roomescape.dao;

import static org.assertj.core.api.Assertions.assertThat;

import java.time.LocalDate;
import java.time.LocalTime;
import java.util.List;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.JdbcTest;
import org.springframework.context.annotation.Import;
import roomescape.domain.Reservation;
import roomescape.domain.ReservationTime;
import roomescape.domain.Theme;

@JdbcTest
@Import({ReservationDao.class, ReservationTimeDao.class, ThemeDao.class})
class ReservationDaoTest {

    @Autowired
    private ReservationDao reservationDao;

    @Autowired
    private ReservationTimeDao reservationTimeDao;

    @Autowired
    private ThemeDao themeDao;

    @Test
    void 사용자가_선택한_날짜와_테마에_해당하는_예약시간Id를_가져온다() {
        // given
        LocalDate targetDate = LocalDate.of(2030, 5, 6);

        ReservationTime targetTime1 = reservationTimeDao.save(new ReservationTime(LocalTime.of(10, 0)));
        ReservationTime targetTime2 = reservationTimeDao.save(new ReservationTime(LocalTime.of(11, 0)));
        ReservationTime otherDateTime = reservationTimeDao.save(new ReservationTime(LocalTime.of(12, 0)));
        ReservationTime otherThemeTime = reservationTimeDao.save(new ReservationTime(LocalTime.of(13, 0)));

        Theme targetTheme = themeDao.save(new Theme("공포", "무서움", "https://roomescape.com/horror"));
        Theme otherTheme = themeDao.save(new Theme("판타지", "신비로움", "https://roomescape.com/fantasy"));

        reservationDao.save(new Reservation("맥스", targetDate, targetTime1, targetTheme));
        reservationDao.save(new Reservation("피노", targetDate, targetTime2, targetTheme));

        reservationDao.save(new Reservation("브라운", LocalDate.of(2030, 5, 5), otherDateTime, targetTheme));

        reservationDao.save(new Reservation("포비", targetDate, otherThemeTime, otherTheme));

        // when
        List<Long> timeIds = reservationDao.findReservedTimeIdsByDateAndThemeId(
                targetDate,
                targetTheme.getId()
        );

        // then
        assertThat(timeIds)
                .containsExactlyInAnyOrder(targetTime1.getId(), targetTime2.getId())
                .doesNotContain(otherDateTime.getId(), otherThemeTime.getId());
    }

    @Test
    void 이미_존재하는_예약이_있는지_확인() {
        //given
        LocalDate date = LocalDate.of(2030, 5, 6);
        ReservationTime reservationTime = reservationTimeDao.save(new ReservationTime(LocalTime.of(12, 0)));
        Theme theme = themeDao.save(new Theme("공포", "무서움", "https://roomescape.com/horror"));
        reservationDao.save(new Reservation("맥스", date, reservationTime, theme));

        //when
        boolean hasAlreadyReservation = reservationDao.hasDuplicateReservation(date, reservationTime.getId(),
                theme.getId());

        //then
        assertThat(hasAlreadyReservation).isTrue();
    }
}
