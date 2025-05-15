package roomescape.theme.service;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

import java.time.LocalDate;
import java.time.LocalTime;
import java.util.List;
import roomescape.exception.ExistedException;
import roomescape.member.Member;
import roomescape.reservation.Reservation;
import roomescape.reservation.dao.FakeReservationDao;
import roomescape.reservation.dao.ReservationDao;
import roomescape.reservationtime.ReservationTime;
import roomescape.theme.Theme;
import roomescape.theme.dao.FakeThemeDao;
import roomescape.theme.dao.ThemeDao;
import roomescape.theme.dto.request.ThemeRequest;
import roomescape.theme.dto.response.ThemeResponse;

import org.junit.jupiter.api.Test;

public class ThemeServiceTest {

    private final ThemeService themeService;
    private final ThemeDao themeDao;
    private final ReservationDao reservationDao;

    private final Theme theme1 = Theme.of(1L, "테마1", "description1", "thumbnail1");
    private final Theme theme2 = Theme.of(2L, "테마2", "description2", "thumbnail2");

    private final Member member1 = Member.of(1L, "포라", "sy@gmail.com", "1234", "USER");
    private final Member member2 = Member.of(2L, "짱구", "Wkdrn@gmail.com", "1234", "USER");
    private final Member member3 = Member.of(3L, "라리사", "lalisa@gmail.com", "1234", "USER");


    public ThemeServiceTest() {
        this.themeDao = new FakeThemeDao(theme1, theme2);
        this.reservationDao = new FakeReservationDao();
        this.themeService = new ThemeService(themeDao, reservationDao);
    }

    @Test
    void 테마를_생성할_수_있다() {
        // given
        ThemeRequest theme = new ThemeRequest("테마3", "description3", "thumbnail3.jpg");
        // when
        Long themeId = themeService.create(theme);
        // then
        assertThat(themeId).isEqualTo(1L);
    }

    @Test
    void 테마_목록을_조회할_수_있다() {
        // when
        List<ThemeResponse> themes = themeService.findAll();
        // then
        assertThat(themes).hasSize(2);
    }

    @Test
    void 테마를_삭제할_수_있다() {
        // when
        themeService.delete(1L);
        // then
        assertThat(themeDao.findAll()).hasSize(1);
    }

    @Test
    void 중복된_이름으로_테마를_생성할_수_없다() {
        // given
        Theme theme3 = Theme.of(3L, "테마1", "description3", "thumbnail3.jpg");
        ThemeRequest themeRequest = new ThemeRequest(theme3.getName(), theme3.getDescription(), theme3.getThumbnail());
        // when & then
        assertThatThrownBy(() -> themeService.create(themeRequest))
                .isInstanceOf(ExistedException.class);
    }

    @Test
    void 최근_일주일_기준으로_인기테마_10개를_가져올_수_있다() {
        // given
        Reservation reservation1 = Reservation.of(1L, member1, LocalDate.now().minusDays(1),
                new ReservationTime(1L, LocalTime.of(9, 0)), theme1);
        Reservation reservation2 = Reservation.of(2L, member2, LocalDate.now().minusDays(3),
                new ReservationTime(1L, LocalTime.of(9, 0)), theme2);
        Reservation reservation3 = Reservation.of(3L, member3, LocalDate.now().minusDays(5),
                new ReservationTime(1L, LocalTime.of(9, 0)), theme1);
        reservationDao.create(reservation1);
        reservationDao.create(reservation2);
        reservationDao.create(reservation3);

        // when
        List<ThemeResponse> top10Themes = themeService.getTop10Themes();

        // then
        assertThat(top10Themes).hasSize(2);
        assertThat(top10Themes.getFirst().name()).isEqualTo("테마2");
        assertThat(top10Themes.get(1).name()).isEqualTo("테마1");
    }
}
