package roomescape.theme.service;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.springframework.boot.test.context.SpringBootTest;
import roomescape.global.exception.InvalidRequestException;
import roomescape.theme.repository.ThemeRepository;

import java.time.LocalDate;

import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.BDDMockito.given;
import static org.mockito.BDDMockito.then;

@SpringBootTest
class ThemeServiceTest {

    @InjectMocks
    private ThemeService themeService;

    @Mock
    private ThemeRepository themeRepository;

    @Test
    @DisplayName("이미 존재하는 이름의 테마를 생성하면 예외가 발생한다.")
    public void create_fail() {
        // given
        given(themeRepository.existsByName("레벨2 탈출")).willReturn(true);

        // when, then
        assertThatThrownBy(() -> themeService.create(
                "레벨2 탈출",
                "우테코 레벨2를 탈출하는 내용입니다.",
                "https://example.com/theme.png"
        ))
                .isInstanceOf(InvalidRequestException.class)
                .hasMessage("이미 존재하는 테마 이름입니다.");
    }

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
                        LocalDate.of(2026, 10, 8),
                        LocalDate.of(2026, 10, 14),
                        size
                );
    }

}
