package roomescape.theme.service;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import roomescape.theme.domain.Theme;
import roomescape.theme.repository.ThemeRepository;
import roomescape.time.domain.ReservationTime;
import roomescape.time.repository.ReservationTimeRepository;

import java.time.LocalDate;
import java.time.LocalTime;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class ThemeServiceTest {

    @Mock
    private ThemeRepository themeRepository;

    @Mock
    private ReservationTimeRepository reservationTimeRepository;

    @InjectMocks
    private ThemeService themeService;

    @Test
    void 테마_목록을_조회하면_Repository_findAll_결과를_반환한다() {
        List<Theme> themes = List.of(new Theme(1L, "공포방", "무서운방입니다.", "image-url"));
        when(themeRepository.findAll()).thenReturn(themes);

        List<Theme> result = themeService.findThemes();

        verify(themeRepository, times(1)).findAll();
        assertThat(result).isSameAs(themes);
    }

    @Test
    void 테마를_생성하면_테마_정보를_Repository_save에_전달하고_결과를_반환한다() {
        Theme saved = new Theme(1L, "공포방", "무서운방입니다.", "image-url");
        when(themeRepository.save(any(Theme.class))).thenReturn(saved);

        Theme result = themeService.createTheme("공포방", "무서운방입니다.", "image-url");

        verify(themeRepository, times(1)).save(any(Theme.class));
        assertThat(result).isSameAs(saved);
    }

    @Test
    void 테마를_삭제하면_Repository_deleteById에_id를_전달한다() {
        themeService.deleteTheme(3L);
        verify(themeRepository).deleteById(3L);
    }

    @Test
    void 테마와_날짜로_예약_가능한_시간을_조회한다() {
        List<ReservationTime> reservationTimes = List.of(
                new ReservationTime(1L, LocalTime.of(13,0)),
                new ReservationTime(2L, LocalTime.of(15,0)),
                new ReservationTime(3L, LocalTime.of(18,0)),
                new ReservationTime(4L, LocalTime.of(20,0))
        );
        List<Long> availableTimes = List.of(1L, 3L);
        when(reservationTimeRepository.findAll()).thenReturn(reservationTimes);
        when(themeRepository.findNotAvailableTimes(any(), any())).thenReturn(availableTimes);

        List<AvailableTime> result = themeService.findAvailableTimes(1L, LocalDate.of(2026, 5, 6));

        verify(reservationTimeRepository, times(1)).findAll();
        verify(themeRepository, times(1)).findNotAvailableTimes(any(), any());
        assertThat(result).containsExactly(
                new AvailableTime(1L, LocalTime.of(13, 0), false),
                new AvailableTime(2L, LocalTime.of(15, 0), true),
                new AvailableTime(3L, LocalTime.of(18, 0), false),
                new AvailableTime(4L, LocalTime.of(20, 0), true)
        );
    }

    @Test
    void 인기_테마_목록을_조회하면_Repository_findPopularThemes_결과를_반환한다() {
        List<Theme> themes = List.of(
                new Theme(1L, "공포방1", "무서워요1", "image-url1"),
                new Theme(2L, "공포방2", "무서워요2", "image-url2"),
                new Theme(3L, "공포방3", "무서워요3", "image-url3"),
                new Theme(4L, "공포방4", "무서워요4", "image-url4"),
                new Theme(5L, "공포방5", "무서워요5", "image-url5"),
                new Theme(6L, "공포방6", "무서워요6", "image-url6"),
                new Theme(7L, "공포방7", "무서워요7", "image-url7"),
                new Theme(8L, "공포방8", "무서워요8", "image-url8"),
                new Theme(9L, "공포방9", "무서워요9", "image-url9"),
                new Theme(10L, "공포방10", "무서워요10", "image-url10")
        );

        LocalDate endDate = LocalDate.of(2026, 5, 11);
        LocalDate startDate = LocalDate.of(2026, 5, 4);
        when(themeRepository.findPopularThemes(startDate, endDate, 10)).thenReturn(themes);

        List<PopularTheme> result = themeService.findPopularThemes(endDate, 7, 10);
        verify(themeRepository, times(1)).findPopularThemes(startDate, endDate, 10);

        assertThat(result).containsExactly(
                new PopularTheme(1L, "공포방1", "무서워요1", "image-url1", 1),
                new PopularTheme(2L, "공포방2", "무서워요2", "image-url2", 2),
                new PopularTheme(3L, "공포방3", "무서워요3", "image-url3", 3),
                new PopularTheme(4L, "공포방4", "무서워요4", "image-url4", 4),
                new PopularTheme(5L, "공포방5", "무서워요5", "image-url5", 5),
                new PopularTheme(6L, "공포방6", "무서워요6", "image-url6", 6),
                new PopularTheme(7L, "공포방7", "무서워요7", "image-url7", 7),
                new PopularTheme(8L, "공포방8", "무서워요8", "image-url8", 8),
                new PopularTheme(9L, "공포방9", "무서워요9", "image-url9", 9),
                new PopularTheme(10L, "공포방10", "무서워요10", "image-url10", 10)
        );
    }
}
