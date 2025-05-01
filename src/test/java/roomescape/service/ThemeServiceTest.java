package roomescape.service;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatCode;

import java.util.List;
import java.util.Optional;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import roomescape.business.model.entity.Theme;
import roomescape.business.model.repository.ReservationDao;
import roomescape.business.model.repository.ThemeDao;
import roomescape.business.service.ThemeService;
import roomescape.presentation.dto.request.ThemeRequest;
import roomescape.presentation.dto.response.ThemeResponse;

class ThemeServiceTest {

    private ReservationDao reservationDao;
    private ThemeDao themeDao;
    private ThemeService themeService;

    @BeforeEach
    void setUp() {
        this.reservationDao = Mockito.mock(ReservationDao.class);
        this.themeDao = Mockito.mock(ThemeDao.class);
        this.themeService = new ThemeService(reservationDao, themeDao);
    }

    @Test
    void 테마를_모두_조회한다() {
        Mockito.when(themeDao.findAll())
                .thenReturn(List.of(
                                new Theme(1L, "아이언맨", "", ""),
                                new Theme(2L, "캡틴아메리카", "", "")
                        )
                );

        assertThat(themeService.findAll()).isEqualTo(
                List.of(
                        new ThemeResponse(1L, "아이언맨", "", ""),
                        new ThemeResponse(2L, "캡틴아메리카", "", "")
                )
        );
    }

    @Test
    void 테마를_생성한다() {
        ThemeRequest request = new ThemeRequest("아이언맨", "", "");

        Mockito.when(themeDao.existByName(Mockito.any()))
                .thenReturn(false);

        Mockito.when(themeDao.save(Mockito.any()))
                .thenReturn(new Theme(1L, "아이언맨", "", ""));

        assertThat(themeService.add(request)).isEqualTo(
                new ThemeResponse(1L, "아이언맨", "", "")
        );
    }

    @Test
    void id로_테마를_삭제한다() {
        Long id = 1L;

        Mockito.when(reservationDao.existByTimeId(Mockito.any()))
                .thenReturn(false);

        Mockito.when(themeDao.findById(Mockito.any()))
                .thenReturn(Optional.of(new Theme(1L, "", "", "")));

        assertThatCode(() -> themeService.deleteById(id)).doesNotThrowAnyException();
    }


    @Test
    void 많이_예약된_순서로_테마를_조회한다() {
        Mockito.when(themeDao.sortByRank())
                .thenReturn(
                        List.of(
                                new Theme(1L, "아이언맨", "", ""),
                                new Theme(2L, "캡틴아메리카", "", "")
                        )
                );

        assertThat(themeService.sortByRank()).isEqualTo(
                List.of(
                        new ThemeResponse(1L, "아이언맨", "", ""),
                        new ThemeResponse(2L, "캡틴아메리카", "", "")
                )
        );
    }
}
