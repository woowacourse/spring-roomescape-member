package roomescape.service;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.BDDMockito.given;
import static org.mockito.BDDMockito.then;

import java.time.LocalDate;
import java.time.LocalTime;
import java.util.List;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import roomescape.dao.ReservationDao;
import roomescape.dao.ThemeDao;
import roomescape.domain.AvailableTime;
import roomescape.domain.ReservationTime;
import roomescape.domain.Theme;

@ExtendWith(MockitoExtension.class)
class ThemeServiceTest {

    @Mock private ThemeDao themeDao;
    @Mock private ReservationDao reservationDao;
    @InjectMocks private ThemeService themeService;

    private final Theme sampleTheme = new Theme(1L, "공포의 저택", "버려진 저택에서 탈출하라!", "https://example.com/img.jpg");

    @Test
    void getAllThemes_전체_테마_조회() {
        given(themeDao.findAll()).willReturn(List.of(sampleTheme));

        List<Theme> result = themeService.getAllThemes();

        assertThat(result).hasSize(1);
        assertThat(result.get(0).getName()).isEqualTo("공포의 저택");
    }

    @Test
    void getPopularThemes_인기_테마_조회() {
        LocalDate from = LocalDate.of(2026, 4, 29);
        LocalDate to = LocalDate.of(2026, 5, 5);
        given(themeDao.findPopularThemes(10, from, to)).willReturn(List.of(sampleTheme));

        List<Theme> result = themeService.getPopularThemes(10, from, to);

        assertThat(result).hasSize(1);
    }

    @Test
    void getAvailableTimes_예약_가능_시간_조회() {
        LocalDate date = LocalDate.of(2026, 5, 20);
        List<AvailableTime> availableTimes = List.of(
                new AvailableTime(new ReservationTime(1L, LocalTime.of(10, 0)), true),
                new AvailableTime(new ReservationTime(2L, LocalTime.of(11, 0)), false)
        );
        given(themeDao.findAvailableTimeById(1L, date)).willReturn(availableTimes);

        List<AvailableTime> result = themeService.getAvailableTimes(1L, date);

        assertThat(result).hasSize(2);
        assertThat(result.get(0).available()).isTrue();
        assertThat(result.get(1).available()).isFalse();
    }

    @Test
    void save_정상_테마_저장() {
        given(themeDao.save("공포의 폐병원", "설명", "https://example.com/img.jpg")).willReturn(5L);

        long id = themeService.save("공포의 폐병원", "설명", "https://example.com/img.jpg");

        assertThat(id).isEqualTo(5L);
    }

    @Test
    void delete_정상_삭제() {
        given(reservationDao.existsByThemeId(1L)).willReturn(false);

        themeService.delete(1L);

        then(themeDao).should().delete(1L);
    }

    @Test
    void delete_예약에_사용중인_테마이면_예외() {
        given(reservationDao.existsByThemeId(1L)).willReturn(true);

        assertThatThrownBy(() -> themeService.delete(1L))
                .isInstanceOf(IllegalArgumentException.class)
                .hasMessage("예약에 사용 중인 테마는 삭제할 수 없습니다.");
    }
}
