package roomescape.service;

import static org.assertj.core.api.Assertions.assertThat;

import java.time.LocalDate;
import java.util.List;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.annotation.DirtiesContext;
import org.springframework.transaction.annotation.Transactional;
import roomescape.dto.ThemeResponse;
import roomescape.model.ReservationTime;
import roomescape.model.Theme;
import roomescape.repository.ReservationRepository;
import roomescape.repository.ThemeRepository;
import roomescape.repository.TimeRepository;

@SpringBootTest
@Transactional
@DirtiesContext(classMode = DirtiesContext.ClassMode.BEFORE_EACH_TEST_METHOD)
public class ThemeServiceTest {

    @Autowired
    private ThemeService themeService;

    @Autowired
    private TimeRepository timeRepository;

    @Autowired
    private ReservationRepository reservationRepository;

    @Autowired
    private ThemeRepository themeRepository;

    @Test
    void 존재하지_않는_테마를_삭제하는경우_예외가_발생한다() {
        // then
        Assertions.assertThatThrownBy(() -> themeService.removeById(-1L))
                .isInstanceOf(IllegalArgumentException.class);
    }

    @Test
    void 존재하는_테마를_삭제하는경우_삭제된다() {
        // then
        Assertions.assertThatCode(() -> themeService.removeById(1L))
                .doesNotThrowAnyException();
    }

    @Test
    void 최근_7일_범위만_인기_테마_집계에_포함된다() {
        // given
        Theme theme = themeRepository.findById(1L).get();
        Theme firstTheme = themeRepository.findById(1L).get();
        Theme secondTheme = themeRepository.findById(2L).get();
        Theme thirdTheme = themeRepository.findById(3L).get();
        ReservationTime reservationTime = timeRepository.findById(1L).get();

        // 포함되어야 하는 날짜
        reservationRepository.save("포함", LocalDate.of(2026, 5, 10), 1L, 1L, reservationTime, firstTheme);

        // 제외되어야 하는 날짜
        reservationRepository.save("제외1", LocalDate.of(2026, 5, 7), 1L, 2L, reservationTime, secondTheme);

        reservationRepository.save("제외2", LocalDate.of(2026, 5, 15), 1L, 3L, reservationTime, thirdTheme);

        // when
        List<ThemeResponse> result =
                themeService.readRanks(LocalDate.of(2026, 5, 15));

        // then
        assertThat(result)
                .extracting(ThemeResponse::id)
                .contains(1L)
                .doesNotContain(2L, 3L);
    }
}
