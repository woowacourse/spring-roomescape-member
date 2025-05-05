package roomescape.service;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertAll;
import static roomescape.Fixtures.JUNK_THEME_REQUEST;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import roomescape.controller.theme.dto.ThemeResponse;
import roomescape.repository.fake.ReservationFakeRepository;
import roomescape.repository.fake.ThemeFakeRepository;

class ThemeServiceTest {

    private ThemeService service;

    @BeforeEach
    void setUp() {
        service = new ThemeService(new ReservationFakeRepository(), new ThemeFakeRepository());
    }

    @Test
    @DisplayName("테마를 추가할 수 있다.")
    void add() {
        // given & when
        ThemeResponse response = service.add(JUNK_THEME_REQUEST);

        // then
        var themes = service.findAll();
        assertThat(themes).contains(response);
    }

    @Test
    @DisplayName("테마를 삭제할 수 있다.")
    void removeById() {
        // given
        var response = service.add(JUNK_THEME_REQUEST);

        // when
        boolean isRemoved = service.removeById(response.id());

        // then
        var themes = service.findAll();
        assertAll(
            () -> assertThat(isRemoved).isTrue(),
            () -> assertThat(themes).doesNotContain(response)
        );
    }
}
