package roomescape.service;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.mockito.BDDMockito;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.springframework.boot.test.context.SpringBootTest;
import roomescape.repository.ThemeRepository;

import java.time.LocalDate;

import static org.mockito.BDDMockito.then;

@SpringBootTest
class ThemeServiceTest {

    @InjectMocks
    private ThemeService themeService;

    @Mock
    private ThemeRepository themeRepository;

    @Test
    @DisplayName("지정된 일 수 및 갯수를 기준으로 인기 테마를 조회한다.")
    public void findPopularThemes() {
        // given
        int days = 7;
        LocalDate now = LocalDate.of(2026, 10, 15);
        int size = 10;

        // when
        themeService.findPopularThemes(days, now, size);

        // then
        // themeRepository.findTopThemesByReservationCount()는 리포지토리 테스트에서 직접 하므로 호출만 검증
        then(themeRepository).should()
                .findTopThemesByReservationCount(
                        LocalDate.of(2026, 10, 7),
                        LocalDate.of(2026, 10, 14),
                        size
                );
    }

}
