package roomescape.unit.service;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.Mockito.when;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import roomescape.domain.Theme;
import roomescape.domain.repository.ThemeRepository;
import roomescape.dto.request.ThemeRequest;
import roomescape.dto.response.ThemeResponse;
import roomescape.exception.ExistedThemeException;
import roomescape.service.ThemeService;
import roomescape.unit.fake.FakeThemeRepository;

public class ThemeServiceTest {

    private ThemeService themeService;
    private final ThemeRepository themeRepository;

    public ThemeServiceTest() {
        this.themeRepository = new FakeThemeRepository();
        this.themeService = new ThemeService(themeRepository);
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
        // given
        themeRepository.create(Theme.createWithoutId("name1", "description1", "thumbnail1"));
        themeRepository.create(Theme.createWithoutId("name2", "description2", "thumbnail2"));
        // when
        List<ThemeResponse> themes = themeService.findAll();
        // then
        assertThat(themes).hasSize(2);
        assertThat(themes.getFirst().name()).isEqualTo("name1");
        assertThat(themes.get(1).name()).isEqualTo("name2");
    }

    @Test
    void 테마를_삭제할_수_있다() {
        // given
        Theme savedTheme = themeRepository.create(Theme.createWithoutId("name1", "description1", "thumbnail1"));
        // when
        themeService.delete(savedTheme.getId());
        // then
        assertThat(themeRepository.findAll()).hasSize(0);
    }

    @Test
    void 중복된_이름으로_테마를_생성할_수_없다() {
        // given
        themeRepository.create(Theme.createWithoutId("name1", "desc1", "thumb1"));
        ThemeRequest themeRequest = new ThemeRequest("name1", "desc2", "thumb2");
        // when & then
        assertThatThrownBy(() -> themeService.create(themeRequest))
                .isInstanceOf(ExistedThemeException.class);
    }

    @Test
    void 인기_테마를_조회한다() {
        // given
        List<Theme> themes = new ArrayList<>();
        for (long i = 1; i <= 10; i++) {
            themes.add(new Theme(i, "name" + i, "desc", "thumb"));
        }
        ThemeRepository mockThemeRepository = Mockito.mock(ThemeRepository.class);
        when(
                mockThemeRepository.findByDateRangeOrderByReservationCountLimitN(
                        LocalDate.now().minusDays(8), LocalDate.now().minusDays(1), 10
                )
        ).thenReturn(themes);
        themeService = new ThemeService(mockThemeRepository);
        // when
        List<ThemeResponse> rank = themeService.getTop10MostReservedThemesInLast7Days();
        // then
        assertThat(rank).hasSize(10);
        assertThat(rank.getFirst().name()).isEqualTo("name1");
    }
}
