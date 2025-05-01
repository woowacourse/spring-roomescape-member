package roomescape.theme.service;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

import java.util.List;
import org.junit.jupiter.api.Test;
import roomescape.exception.ExistedThemeException;
import roomescape.reservation.dao.FakeReservationDao;
import roomescape.reservation.dao.ReservationDao;
import roomescape.theme.Theme;
import roomescape.theme.dao.FakeThemeDao;
import roomescape.theme.dao.ThemeDao;
import roomescape.theme.dto.request.ThemeRequest;
import roomescape.theme.dto.response.ThemeResponse;

public class ThemeServiceTest {

    private final ThemeService themeService;
    private final ThemeDao themeDao;
    private final ReservationDao reservationDao;
    Theme theme1 = new Theme(1L, "테마1", "description1", "thumbnail1");
    Theme theme2 = new Theme(2L, "테마2", "description2", "thumbnail2");

    public ThemeServiceTest() {
        this.themeDao = new FakeThemeDao(theme1, theme2);
        this.reservationDao = new FakeReservationDao();
        this.themeService = new ThemeService(themeDao, reservationDao);
    }

    @Test
    void 테마를_생성할_수_있다() {
        // given
        ThemeRequest theme = new ThemeRequest("name3", "description3", "thumbnail3");
        // when
        ThemeResponse savedTheme = themeService.create(theme);
        // then
        assertThat(savedTheme.name()).isEqualTo("name3");
        assertThat(savedTheme.description()).isEqualTo("description3");
        assertThat(savedTheme.thumbnail()).isEqualTo("thumbnail3");
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
        Theme theme3 = new Theme(3L, "테마1", "description3", "thumbnail3");
        ThemeRequest themeRequest = new ThemeRequest(theme3.getName(), theme3.getDescription(), theme3.getThumbnail());
        // when & then
        assertThatThrownBy(() -> themeService.create(themeRequest))
                .isInstanceOf(ExistedThemeException.class);
    }
}
