package roomescape.reservation.dao;

import static org.assertj.core.api.Assertions.assertThat;

import java.time.LocalDate;
import java.time.LocalTime;
import java.util.List;
import java.util.Optional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.JdbcTest;
import org.springframework.context.annotation.Import;
import org.springframework.test.context.jdbc.Sql;
import roomescape.member.Member;
import roomescape.member.dao.JdbcMemberDao;
import roomescape.reservation.Reservation;
import roomescape.reservationtime.ReservationTime;
import roomescape.reservationtime.dao.JdbcReservationTimeDao;
import roomescape.reservationtime.dao.ReservationTimeDao;
import roomescape.theme.Theme;
import roomescape.theme.dao.JdbcThemeDao;
import roomescape.theme.dao.ThemeDao;

import org.junit.jupiter.api.Test;

@Import({JdbcThemeDao.class, JdbcReservationDao.class, JdbcReservationTimeDao.class, JdbcMemberDao.class})
@JdbcTest
@Sql({"/sql/test-schema.sql", "/sql/test-data.sql"})
class JdbcReservationDaoTest {
    @Autowired
    private ReservationDao reservationDao;

    @Autowired
    private ReservationTimeDao reservationTimeDao;

    @Autowired
    private ThemeDao themeDao;

    @Test
    void 예약_시간을_추가할_수_있다() {
        // given
        ReservationTime reservationTime = ReservationTime.createWithoutId(LocalTime.of(10, 0));
        Long id = reservationTimeDao.create(reservationTime);
        Theme theme = Theme.of(1L, "themeName", "description", "thumb.jpg");
        Member member = Member.of(1L, "이승연", "sy@gmail.com", "1234", "USER");
        themeDao.create(theme);
        Reservation reservation = Reservation.createWithoutId(
                member,
                LocalDate.now().plusDays(1),
                new ReservationTime(id, reservationTime.getStartAt()),
                theme
        );

        // when
        Long savedId = reservationDao.create(reservation);

        // then
        assertThat(savedId).isEqualTo(6);
    }

    @Test
    void 예약_시간을_조회할_수_있다() {
        // when
        List<Reservation> reservationDaoAll = reservationDao.findAll();

        // then
        assertThat(reservationDaoAll.getFirst().getMember().getName()).isEqualTo("이뜽연");
    }

    @Test
    void 예약_시간을_삭제할_수_있다() {
        // when
        reservationDao.delete(1L);
        int afterSize = reservationDao.findAll().size();

        // then
        assertThat(afterSize).isEqualTo(4);
    }

    @Test
    void timeId로_예약을_조회한다() {
        // given
        ReservationTime reservationTime = ReservationTime.createWithoutId(LocalTime.of(10, 0));
        Long id = reservationTimeDao.create(reservationTime);
        Theme theme = Theme.of(1L, "themeName", "description", "thumb.jpg");
        Member member = Member.of(1L, "이승연", "sy@gmail.com", "1234", "USER");
        Long themeId = themeDao.create(theme);
        Reservation reservation = Reservation.createWithoutId(
                member,
                LocalDate.now().plusDays(1),
                new ReservationTime(id, reservationTime.getStartAt()),
                Theme.of(themeId, theme.getName(), theme.getDescription(), theme.getThumbnail()));
        reservationDao.create(reservation);
        // when
        Optional<Reservation> foundReservation = reservationDao.findByTimeId(id);
        // then
        assertThat(foundReservation.isPresent()).isTrue();
        assertThat(foundReservation.get().getMember().getName()).isEqualTo("이뜽연");
    }

    @Test
    void id로_예약을_조회한다() {
        // when
        Optional<Reservation> foundReservation = reservationDao.findById(1L);
        // then
        assertThat(foundReservation.isPresent()).isTrue();
        assertThat(foundReservation.get().getMember().getName()).isEqualTo("이뜽연");
    }

    @Test
    void 예약_가능한_시간을_조회할_수_있다() {
        // given
        LocalDate date = LocalDate.of(2025, 1, 4);
        Long themeId = 2L;

        // when
        List<ReservationTime> availableTimes = reservationDao.findAvailableTimesByDateAndThemeId(date, themeId);

        // then
        assertThat(availableTimes.size()).isEqualTo(2);

    }

    @Test
    void 최근_일주일_기준으로_인기테마_10개를_가져올_수_있다() {
        // given
        LocalDate currentDate = LocalDate.of(2025, 1, 6);

        // when
        List<Theme> top10Themes = reservationDao.findTop10Themes(currentDate);

        // then
        assertThat(top10Themes).hasSize(2);
        assertThat(top10Themes.getFirst().getName()).isEqualTo("테마2");
        assertThat(top10Themes.get(1).getName()).isEqualTo("테마1");
    }
}
