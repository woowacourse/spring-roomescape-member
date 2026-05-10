package roomescape.service;

import java.time.LocalDate;
import java.util.Optional;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.Test;
import roomescape.controller.dto.ThemeFamousFindRequest;
import roomescape.repository.ThemeRepository;
import roomescape.service.stub.StubThemeRepository;

public class ThemeServiceTest {
    private ThemeRepository themeRepository;
    private ThemeService themeService;

    @Test
    void 존재하지_않는_테마_조회_시_예외가_발생한다() {
        // given
        themeRepository = new StubThemeRepository(Optional.empty());
        themeService = new ThemeService(themeRepository);

        // when & then
        Assertions.assertThatThrownBy(() -> themeService.find(999L)).isInstanceOf(IllegalArgumentException.class);
    }

    @Test
    void 존재하지_않는_테마_삭제_시_예외가_발생한다() {
        // given
        themeRepository = new StubThemeRepository(false);
        themeService = new ThemeService(themeRepository);

        // when & then
        Assertions.assertThatThrownBy(() -> themeService.delete(999L)).isInstanceOf(IllegalArgumentException.class);
    }

    @Test
    void 유명한_테마_조회_시_모든_매개변수가_존재하면_그대로_전달된다() {
        // given
        StubThemeRepository themeRepository = new StubThemeRepository();
        themeService = new ThemeService(themeRepository);

        Long days = 10L;
        LocalDate date = LocalDate.parse("2026-05-01");
        Long limit = 20L;

        // when
        themeService.findFamous(new ThemeFamousFindRequest(days, date, limit));

        // then
        Assertions.assertThat(themeRepository.lastCalledDate).isEqualTo(date);
        Assertions.assertThat(themeRepository.lastCalledDays).isEqualTo(days);
        Assertions.assertThat(themeRepository.lastCalledLimit).isEqualTo(limit);
    }

    @Test
    void 유명한_테마_조회_시_days가_없으면_기본_값으로_대체된다() {
        // given
        StubThemeRepository themeRepository = new StubThemeRepository();
        themeService = new ThemeService(themeRepository);

        Long days = null;
        LocalDate date = LocalDate.parse("2026-05-01");
        Long limit = 20L;

        // when
        themeService.findFamous(new ThemeFamousFindRequest(days, date, limit));

        // then
        Assertions.assertThat(themeRepository.lastCalledDate).isEqualTo(date);
        Assertions.assertThat(themeRepository.lastCalledDays).isEqualTo(7L);
        Assertions.assertThat(themeRepository.lastCalledLimit).isEqualTo(limit);
    }

    @Test
    void 유명한_테마_조회_시_date가_없으면_오늘로_대체된다() {
        // given
        StubThemeRepository themeRepository = new StubThemeRepository();
        themeService = new ThemeService(themeRepository);

        Long days = 7L;
        LocalDate date = null;
        Long limit = 10L;

        // when
        themeService.findFamous(new ThemeFamousFindRequest(days, date, limit));

        // then
        Assertions.assertThat(themeRepository.lastCalledDate).isEqualTo(LocalDate.now());
        Assertions.assertThat(themeRepository.lastCalledDays).isEqualTo(days);
        Assertions.assertThat(themeRepository.lastCalledLimit).isEqualTo(limit);
    }

    @Test
    void 유명한_테마_조회_시_limit이_없으면_기본값으로_대체된다() {
        // given
        StubThemeRepository themeRepository = new StubThemeRepository();
        themeService = new ThemeService(themeRepository);

        Long days = 7L;
        LocalDate date = LocalDate.parse("2026-05-01");
        Long limit = null;

        // when
        themeService.findFamous(new ThemeFamousFindRequest(days, date, limit));

        // then
        Assertions.assertThat(themeRepository.lastCalledDate).isEqualTo(date);
        Assertions.assertThat(themeRepository.lastCalledDays).isEqualTo(days);
        Assertions.assertThat(themeRepository.lastCalledLimit).isEqualTo(10L);
    }
}
